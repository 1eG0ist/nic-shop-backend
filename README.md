# nic-shop-backend
Требования к запуску:
* Docker
* Maven
### Команда для запуска
По умолчанию папка в которую будут сохраняться данные - C:/nic_shop, если требуется изменить эту папку то нужно:
* Зайти в файл docker-compose.yml в корне проекта
* Ctrl + f
* Заменить в двух местах C:/nic_shop на более удобную директорию
* При этом в папке все так же должны быть подпапки products и comments
Для запуска необходимо прописать один из слудующих вариантов в директории проекта

🔹 Пример для Linux/macOS:
```bash
pg_data_path_variable=/путь/на/локальной/машине/nic_postgres_data docker-compose up -d
```
🔹 Пример для Windows (PowerShell):
```shell
$env:pg_data_path_variable="C:\nic_postgres_data"; docker-compose up -d
```
🔹 Пример для Windows (cmd):
```shell
set pg_data_path_variable=C:\nic_postgres_data
docker-compose up -d
```
### Команда для запуска тестов
```bash
mvn clean test
```
При этом должен быть запущен Docker, т.к. тесты используют testcontainers