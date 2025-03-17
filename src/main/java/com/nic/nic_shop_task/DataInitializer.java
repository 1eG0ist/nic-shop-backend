package com.nic.nic_shop_task;

import com.nic.nic_shop_task.models.*;
import com.nic.nic_shop_task.repositories.*;
import lombok.AllArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
@AllArgsConstructor
public class DataInitializer implements CommandLineRunner {

    private final RoleRepository roleRepository;
    private final CategoryRepository categoryRepository;
    private final ProductRepository productRepository;
    private final ProductPropertiesRepository productPropertiesRepository;
    private final PropertyRepository propertyRepository;

    private final String DEFAULT_DESCRIPTION = "В этой модели установлены три основные камеры (48 Мп, 12" +
            "Мп и 12 Мп) и фронтальный модуль (7 Мп), можно записывать " +
            "видео в качестве 4К с частотой 30 кадров в секунду. Основной " +
            "широкоугольный объектив снабжен сапфировым защитным " +
            "стеклом.";

    @Override
    public void run(String... args) {
        if (needInitializeStartDataInDb()) {
            initializeRoles();
            initializeMinimalData();
        }
    }

    private boolean needInitializeStartDataInDb() {
        long roles = roleRepository.count();
        return roles == 0;
    }

    private void initializeRoles() {
        List<String> requiredRoles = Arrays.asList(
                "ROLE_USER",
                "ROLE_ADMIN"
        );

        for (String roleName : requiredRoles) {
            if (!roleRepository.findByName(roleName).isPresent()) {
                Role role = new Role();
                role.setName(roleName);
                roleRepository.save(role);
            }
        }
    }

    private void initializeMinimalData() {
        // Создание корневых категорий
        Category smartphones = createCategory("Смартфоны", null);
        Category audioTech = createCategory("Аудиотехника", null);

        // Создание подкатегорий
        Category apple = createCategory("Apple", smartphones);
        Category samsung = createCategory("Samsung", smartphones);

        // Создание продуктов и их свойств
        Product iphone14 = createProduct(
                "Смартфон Apple iPhone 14",
                DEFAULT_DESCRIPTION,
                115999.0,
                6,
                "/app/images/products/iphone14.png",
                10,
                Collections.singletonList(apple)
        );
        addProductProperty(iphone14, "Количество ядер", "number", 6.0);
        addProductProperty(iphone14, "Диагональ экрана", "number", 6.1);
        addProductProperty(iphone14, "Цвет", "text", "Черный");
        addProductProperty(iphone14, "Наличие чехла", "boolean", true);
        addProductProperty(iphone14, "Объем памяти", "number", 128.0);

        Product iphone13 = createProduct(
                "Смартфон Apple iPhone 13",
                DEFAULT_DESCRIPTION,
                80999.0,
                4,
                "/app/images/products/iphone13.png",
                15,
                Collections.singletonList(apple)
        );
        addProductProperty(iphone13, "Количество ядер", "number", 4.0);
        addProductProperty(iphone13, "Диагональ экрана", "number", 6.1);
        addProductProperty(iphone13, "Цвет", "text", "Синий");
        addProductProperty(iphone13, "Наличие чехла", "boolean", false);
        addProductProperty(iphone13, "Объем памяти", "number", 64.0);

        Product galaxyS8 = createProduct(
                "Смартфон Samsung Galaxy S8",
                DEFAULT_DESCRIPTION,
                59999.0,
                0,
                "/app/images/products/samsunggalaxys8.png",
                15,
                Collections.singletonList(samsung)
        );
        addProductProperty(galaxyS8, "Количество ядер", "number", 8.0);
        addProductProperty(galaxyS8, "Диагональ экрана", "number", 6.2);
        addProductProperty(galaxyS8, "Цвет", "text", "Черный");
        addProductProperty(galaxyS8, "Наличие чехла", "boolean", true);
        addProductProperty(galaxyS8, "Объем памяти", "number", 128.0);

        Product huaweiP50 = createProduct(
                "Смартфон Huawei P50",
                DEFAULT_DESCRIPTION,
                59999.0,
                0,
                "/app/images/products/huaweip50.png",
                20,
                Collections.singletonList(smartphones)
        );
        addProductProperty(huaweiP50, "Количество ядер", "number", 8.0);
        addProductProperty(huaweiP50, "Диагональ экрана", "number", 6.5);
        addProductProperty(huaweiP50, "Цвет", "text", "Золотой");
        addProductProperty(huaweiP50, "Наличие чехла", "boolean", false);
        addProductProperty(huaweiP50, "Объем памяти", "number", 256.0);

        Product yandexStation = createProduct(
                "Умная колонка Яндекс Станция",
                "Супер мега крутая колонка, такой ни у кого нет.",
                23999.0,
                3,
                "/app/images/products/yandexstation.png",
                0,
                Collections.singletonList(audioTech)
        );
        addProductProperty(yandexStation, "Цвет", "text", "Черный");

        Product airPodsPro = createProduct(
                "Наушники Apple AirPods Pro",
                "Ну эти наушники - это база, такие у каждого быть должны (у меня нет)",
                19999.0,
                12,
                "/app/images/products/appleairpodspro.png",
                15,
                Collections.singletonList(audioTech)
        );
        addProductProperty(airPodsPro, "Цвет", "text", "Белый");
    }

    private void addProductProperty(Product product, String propertyName, String propertyType, Object value) {
        Property property = propertyRepository.findByName(propertyName)
                .orElseGet(() -> propertyRepository.save(new Property(null, propertyName, propertyType)));

        ProductProperties productProperty = new ProductProperties();
        productProperty.setProduct(product);
        productProperty.setProperty(property);

        switch (propertyType) {
            case "number":
                productProperty.setNumericValue((Double) value);
                break;
            case "text":
                productProperty.setTextValue((String) value);
                break;
            case "boolean":
                productProperty.setIsValue((Boolean) value);
                break;
        }

        productPropertiesRepository.save(productProperty);
    }

    private Category createCategory(String name, Category parentCategory) {
        return categoryRepository.save(
            new Category(null, name, parentCategory, new ArrayList<>())
        );
    }

    private Product createProduct(String name, String description, Double price, Integer count,
         String ImagePath, Integer sale, List<Category> categories) {

        return productRepository.save(new Product(null, name, description, price, count,
                ImagePath, sale, 0, 0, categories));

    }
}
