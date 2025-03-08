# nic-shop-backend
Требования к запуску:
* Docker
* Maven
### Команда для запуска
```bash
docker-compose up
```
в директории проекта
### Команда для запуска тестов
```bash
mvn clean test
```
При этом должен быть запущен Docker, т.к. тесты используют testcontainers