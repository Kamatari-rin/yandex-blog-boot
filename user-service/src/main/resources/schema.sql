CREATE SCHEMA IF NOT EXISTS user_service;

-- Таблица пользователей
CREATE TABLE IF NOT EXISTS user_service.users (
                                                  id BIGSERIAL PRIMARY KEY,
                                                  username VARCHAR(100) NOT NULL UNIQUE,
                                                  email VARCHAR(100) NOT NULL UNIQUE,
                                                  password VARCHAR(255) NOT NULL,
                                                  created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
                                                  updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);

-- Таблица справочников для типов целей (target)
CREATE TABLE IF NOT EXISTS user_service.target_types (
                                                         id SERIAL PRIMARY KEY,
                                                         type_name VARCHAR(50) NOT NULL UNIQUE
);

-- Таблица лайков
CREATE TABLE IF NOT EXISTS user_service.likes (
                                                  id BIGSERIAL PRIMARY KEY,
                                                  user_id BIGINT NOT NULL,
                                                  target_id BIGINT NOT NULL,
                                                  target_type_id INT NOT NULL,
                                                  is_liked BOOLEAN NOT NULL,
                                                  FOREIGN KEY (user_id) REFERENCES user_service.users(id) ON DELETE CASCADE,
                                                  FOREIGN KEY (target_type_id) REFERENCES user_service.target_types(id)
);

-- Добавляем типы целей (если они еще не существуют)
INSERT INTO user_service.target_types (type_name)
VALUES ('POST'), ('COMMENT')
ON CONFLICT (type_name) DO NOTHING;
