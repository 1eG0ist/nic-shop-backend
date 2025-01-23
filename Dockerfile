# Используем базовый образ Maven для сборки приложения
FROM maven:3.8.6-jdk-8 AS build

# Устанавливаем рабочую директорию
WORKDIR /app

# Копируем файлы приложения в контейнер
COPY . .

# Создаем директории для хранения изображений
RUN mkdir -p /app/images/products

# Копируем изображения продуктов в контейнер
COPY src/main/resources/images/products /app/images/products

# Устанавливаем переменную окружения для пути к изображениям
ENV IMAGE_PATH=/app/images/products

# Сборка приложения с использованием Maven
RUN mvn clean package

# Используем базовый образ OpenJDK 8 для запуска приложения
FROM openjdk:8-jdk-alpine

# Устанавливаем рабочую директорию
WORKDIR /app

# Копируем собранный JAR файл из предыдущего этапа
COPY --from=build /app/target/nic-shop-task-0.0.1-SNAPSHOT.jar /app/nic-shop-task.jar

# Копируем изображения продуктов в контейнер
COPY --from=build /app/images/products /app/images/products

# Запускаем приложение
CMD ["java", "-jar", "/app/nic-shop-task.jar"]
