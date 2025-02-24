package com.nic.nic_shop_task.repositories;

import com.nic.nic_shop_task.dtos.FilterPropertyDto;
import com.nic.nic_shop_task.models.Product;
import com.nic.nic_shop_task.models.ProductProperties;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import javax.persistence.criteria.Subquery;
import java.util.ArrayList;
import java.util.List;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long>, JpaSpecificationExecutor<Product> {

    default List<Product> findByCategoryIdsAndFilters(List<Long> categoryIds, List<FilterPropertyDto> filters) {
        return findAll((Specification<Product>) (root, query, criteriaBuilder) -> {
            List<Predicate> predicates = new ArrayList<>();

            // Фильтр по категориям
            if (categoryIds != null && !categoryIds.isEmpty()) {
                predicates.add(root.join("categories").get("id").in(categoryIds));
            }

            // Фильтр по свойствам
            if (filters != null && !filters.isEmpty()) {
                for (FilterPropertyDto filter : filters) {
                    // Создаем подзапрос для фильтрации по свойствам
                    Subquery<Long> subquery = query.subquery(Long.class);
                    Root<ProductProperties> ppRoot = subquery.from(ProductProperties.class);
                    subquery.select(ppRoot.get("product").get("id"));

                    Predicate propertyPredicate = criteriaBuilder.equal(
                            ppRoot.get("property").get("id"),
                            filter.getPropertyId()
                    );

                    // Фильтр по числовым значениям
                    if (filter.getNumericValue() != null) {
                        propertyPredicate = criteriaBuilder.and(
                                propertyPredicate,
                                criteriaBuilder.equal(
                                        ppRoot.get("numericValue"),
                                        filter.getNumericValue()
                                )
                        );
                    }

                    // Фильтр по диапазону
                    if (filter.getMinRangeValue() != null && filter.getMaxRangeValue() != null) {
                        propertyPredicate = criteriaBuilder.and(
                                propertyPredicate,
                                criteriaBuilder.between(
                                        ppRoot.get("rangeStartValue"),
                                        filter.getMinRangeValue(),
                                        filter.getMaxRangeValue()
                                )
                        );
                    }

                    // Фильтр по текстовым значениям
                    if (filter.getTextValue() != null) {
                        propertyPredicate = criteriaBuilder.and(
                                propertyPredicate,
                                criteriaBuilder.equal(
                                        ppRoot.get("textValue"),
                                        filter.getTextValue()
                                )
                        );
                    }

                    // Фильтр по boolean значениям
                    if (filter.getBooleanValue() != null) {
                        propertyPredicate = criteriaBuilder.and(
                                propertyPredicate,
                                criteriaBuilder.equal(
                                        ppRoot.get("isValue"),
                                        filter.getBooleanValue()
                                )
                        );
                    }

                    subquery.where(propertyPredicate);
                    predicates.add(criteriaBuilder.in(root.get("id")).value(subquery));
                }
            }

            return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
        });
    }

    @Query("SELECT p FROM Product p WHERE p.id IN :ids")
    List<Product> findProductsByIds(List<Long> ids);

    @Modifying
    @Query(value = "DELETE FROM product_categories WHERE category_id = :categoryId", nativeQuery = true)
    void deleteCategoryFromProducts(@Param("categoryId") Long categoryId);

    @Modifying
    @Query(value = "UPDATE product_categories pc SET category_id = :newCategoryId WHERE category_id = :categoryId", nativeQuery = true)
    void swapCategoryToDeletedCategoryParent(@Param("categoryId") Long categoryId, @Param("newCategoryId") Long newCategoryId);
}
