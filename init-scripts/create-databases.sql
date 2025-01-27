-- Создание баз данных
CREATE DATABASE postdb;
CREATE DATABASE userdb;

-- Создание пользователей и их привязка к базам
CREATE USER post_service_user WITH ENCRYPTED PASSWORD 'post-service_password';
CREATE USER user_service_user WITH ENCRYPTED PASSWORD 'user-service_password';

GRANT ALL PRIVILEGES ON DATABASE postdb TO post_service_user;
GRANT ALL PRIVILEGES ON DATABASE userdb TO user_service_user;
