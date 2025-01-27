CREATE SCHEMA IF NOT EXISTS post_service;

-- Таблица постов
CREATE TABLE IF NOT EXISTS post_service.posts (
                                                  id BIGSERIAL PRIMARY KEY,
                                                  title VARCHAR(100) NOT NULL,
                                                  content TEXT NOT NULL,
                                                  image_url VARCHAR(255),
                                                  user_id BIGINT NOT NULL,
                                                  created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
                                                  updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Таблица тегов
CREATE TABLE IF NOT EXISTS post_service.tags (
                                                 id BIGSERIAL PRIMARY KEY,
                                                 name VARCHAR(50) NOT NULL UNIQUE
);

-- Таблица связей постов и тегов
CREATE TABLE IF NOT EXISTS post_service.post_tags (
                                                      post_id BIGINT NOT NULL,
                                                      tag_id BIGINT NOT NULL,
                                                      created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                                                      PRIMARY KEY (post_id, tag_id),
                                                      FOREIGN KEY (post_id) REFERENCES post_service.posts(id) ON DELETE CASCADE,
                                                      FOREIGN KEY (tag_id) REFERENCES post_service.tags(id) ON DELETE CASCADE
);

-- Таблица комментариев
CREATE TABLE IF NOT EXISTS post_service.comments (
                                                     id BIGSERIAL PRIMARY KEY,
                                                     content VARCHAR(500) NOT NULL,
                                                     post_id BIGINT NOT NULL,
                                                     user_id BIGINT NOT NULL,
                                                     parent_comment_id BIGINT,
                                                     created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
                                                     updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                                                     FOREIGN KEY (post_id) REFERENCES post_service.posts(id) ON DELETE CASCADE,
                                                     FOREIGN KEY (parent_comment_id) REFERENCES post_service.comments(id) ON DELETE CASCADE
);

CREATE INDEX IF NOT EXISTS idx_post_id ON post_service.comments(post_id);
CREATE INDEX IF NOT EXISTS idx_parent_comment_id ON post_service.comments(parent_comment_id);
