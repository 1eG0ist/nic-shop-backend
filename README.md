# nic-shop-backend
Требования к запуску:
* Docker
* Maven
### Команда для запуска
По умолчанию папка в которую будут сохраняться данные - C:/nic_shop, если требуется изменить эту папку то нужно:
* Зайти в файл docker-compose.yml в корне проекта
* Ctrl + f
* Заменить в двух местах C:/nic_shop на более удобную директорию
* При этом в папке все так же должны быть подпапки products и comments (При запуске в случае если нет корневой папки или подпапок - все будет создано автоматически)

Для запуска необходимо прописать в директории проекта
```shell
docker-compose up -d
```
### Команда для запуска тестов
```bash
mvn clean test
```
При этом должен быть запущен Docker, т.к. тесты используют testcontainers