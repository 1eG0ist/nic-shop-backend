package com.nic.nic_shop_task;

import com.nic.nic_shop_task.models.Category;
import com.nic.nic_shop_task.models.Product;
import com.nic.nic_shop_task.models.Role;
import com.nic.nic_shop_task.repositories.CategoryRepository;
import com.nic.nic_shop_task.repositories.ProductRepository;
import com.nic.nic_shop_task.repositories.RoleRepository;
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

    private final String DEFAULT_DESCRIPTION = "В этой модели установлены три основные камеры (48 Мп, 12" +
            "Мп и 12 Мп) и фронтальный модуль (7 Мп), можно записывать " +
            "видео в качестве 4К с частотой 30 кадров в секунду. Основной " +
            "широкоугольный объектив снабжен сапфировым защитным " +
            "стеклом.";

    @Override
    public void run(String... args) throws Exception {
        initializeRoles();
        initializeMinimalData();
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

        createProduct(
                "Смартфон Apple iPhone 14",
                DEFAULT_DESCRIPTION,
                115999.0,
                6,
                "app/images/products/iphone14.png",
                10,
                Collections.singletonList(apple)
        );

        createProduct(
                "Смартфон Apple iPhone 13",
                DEFAULT_DESCRIPTION,
                80999.0,
                4,
                "app/images/products/iphone13.png",
                15,
                Collections.singletonList(apple)
        );

        createProduct(
                "Смартфон Samsung Galaxy S8",
                DEFAULT_DESCRIPTION,
                59999.0,
                0,
                "app/images/products/samsunggalaxys8.png",
                15,
                Collections.singletonList(samsung)
        );

        createProduct(
                "Смартфон Huawei P50 ",
                DEFAULT_DESCRIPTION,
                59999.0,
                0,
                "app/images/products/huaweip50.png",
                20,
                Collections.singletonList(smartphones)
        );

        createProduct(
                "Умная колонка Яндекс Станция",
                "Супер мега крутая колонка, такой ни у кого нет.",
                23999.0,
                3,
                "app/images/products/yandexstation.png",
                0,
                Collections.singletonList(audioTech)
        );

        createProduct(
                "Наушники Apple AirPods Pro",
                "Ну эти наушники - это база, такие у каждого быть должны (у меня нет)",
                19999.0,
                12,
                "app/images/products/appleairpodspro.png",
                15,
                Collections.singletonList(audioTech)
        );
    }

    private Category createCategory(String name, Category parentCategory) {
        return categoryRepository.save(
            new Category(null, name, parentCategory, new ArrayList<>())
        );
    }

    private void createProduct(String name, String description, Double price, Integer count,
         String ImagePath, Integer sale, List<Category> categories) {

        productRepository.save(new Product(null, name, description, price, count,
                ImagePath, sale, categories));

    }
}
