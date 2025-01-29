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

INSERT INTO post_service.posts (title, content, image_url, user_id, created_at, updated_at)
VALUES
    ('Как котики меняют нашу жизнь',
     'Коты – удивительные создания. Они помогают нам расслабиться, радуют своим мурлыканьем и приносят уют в дом.',
     'https://cdn2.thecatapi.com/images/bvs.jpg',
     1,
     CURRENT_TIMESTAMP,
     CURRENT_TIMESTAMP),

    ('Почему коты такие независимые?',
     'В отличие от собак, коты не требуют постоянного внимания. Они умеют развлекать себя сами и даже дрессировать своих хозяев.',
     'https://cdn2.thecatapi.com/images/cot.jpg',
     1,
     CURRENT_TIMESTAMP,
     CURRENT_TIMESTAMP),

    ('5 способов сделать кота счастливым',
     'Коты любят комфорт, вкусную еду и заботу. Вот несколько простых советов, которые помогут вашему питомцу чувствовать себя прекрасно.',
     'https://cdn2.thecatapi.com/images/3g9.jpg',
     1,
     CURRENT_TIMESTAMP,
     CURRENT_TIMESTAMP),

    ('Что означают звуки, которые издают коты?',
     'Мяуканье, мурчание и даже фырканье – всё это язык кошек. Узнайте, как понять вашего пушистого друга.',
     'https://cdn2.thecatapi.com/images/a5s.jpg',
     1,
     CURRENT_TIMESTAMP,
     CURRENT_TIMESTAMP),

    ('Можно ли понять мысли кота?',
     'Ученые выяснили, что кошки могут выражать эмоции не только через звуки, но и через поведение. Разбираемся, как понять своего питомца.',
     'https://cdn2.thecatapi.com/images/MTkwODQ1OQ.jpg',
     1,
     CURRENT_TIMESTAMP,
     CURRENT_TIMESTAMP)
ON CONFLICT DO NOTHING;


CREATE OR REPLACE FUNCTION update_updated_at_column()
    RETURNS TRIGGER AS $$
BEGIN
    NEW.updated_at = CURRENT_TIMESTAMP;
    RETURN NEW;
END;
$$ LANGUAGE plpgsql;

CREATE TRIGGER trigger_update_updated_at
    BEFORE UPDATE ON post_service.posts
    FOR EACH ROW
EXECUTE FUNCTION update_updated_at_column();