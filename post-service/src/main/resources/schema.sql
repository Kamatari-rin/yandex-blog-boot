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

INSERT INTO post_service.posts (title, content, image_url, user_id, created_at, updated_at) VALUES
                                                                                                ('Почему коты так любят коробки?',
                                                                                                 'Если ваша кошка приносит вам пойманную добычу – это знак заботы. В дикой природе кошки учат котят охотиться именно таким образом.
                                                                                                 Кошки имеют уникальное зрение: они видят в темноте в 6 раз лучше, чем люди. Это объясняется особенностями строения их глаз.
                                                                                                 Кошки могут развивать дружеские отношения не только с людьми, но и с другими животными, включая собак и даже попугаев.',
                                                                                                 'https://plus.unsplash.com/premium_photo-1667030474693-6d0632f97029?q=80&w=3774&auto=format&fit=crop&ixlib=rb-4.0.3&ixid=M3wxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8fA%3D%3D', 1, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),

                                                                                                ('Как ухаживать за котёнком в первые месяцы',
                                                                                                 'Знаете ли вы, что у кошек есть свой язык тела? Их хвост, уши и даже усики могут многое рассказать о настроении питомца.
                                                                                                 Кошки могут запоминать лица людей и отличать их друг от друга. Они также могут запоминать ваши привычки и распорядок дня.
                                                                                                 Коты – одни из самых загадочных существ. Они могут спать до 16 часов в день, но при этом сохранять невероятную грацию и ловкость.',
                                                                                                 'https://images.unsplash.com/photo-1519052537078-e6302a4968d4?q=80&w=3870&auto=format&fit=crop&ixlib=rb-4.0.3&ixid=M3wxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8fA%3D%3D', 1, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),

                                                                                                ('Топ-10 самых популярных пород кошек',
                                                                                                 'Коты – одни из самых загадочных существ. Они могут спать до 16 часов в день, но при этом сохранять невероятную грацию и ловкость.
                                                                                                 Знаете ли вы, что у кошек есть свой язык тела? Их хвост, уши и даже усики могут многое рассказать о настроении питомца.
                                                                                                 Кошки могут запоминать лица людей и отличать их друг от друга. Они также могут запоминать ваши привычки и распорядок дня.',
                                                                                                 'https://images.unsplash.com/photo-1491485880348-85d48a9e5312?q=80&w=3870&auto=format&fit=crop&ixlib=rb-4.0.3&ixid=M3wxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8fA%3D%3D', 1, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),

                                                                                                ('Что делать, если ваш кот вас игнорирует',
                                                                                                 'Знаете ли вы, что у кошек есть свой язык тела? Их хвост, уши и даже усики могут многое рассказать о настроении питомца.
                                                                                                 Некоторые породы кошек, такие как сиамские, могут быть очень разговорчивыми. Они любят общаться с хозяином и выражать свои эмоции различными звуками.
                                                                                                 Кошки могут развивать дружеские отношения не только с людьми, но и с другими животными, включая собак и даже попугаев.',
                                                                                                 'https://images.unsplash.com/photo-1501820488136-72669149e0d4?q=80&w=3870&auto=format&fit=crop&ixlib=rb-4.0.3&ixid=M3wxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8fA%3D%3D', 1, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),

                                                                                                ('Как кошки выражают любовь',
                                                                                                 'Знаете ли вы, что у кошек есть свой язык тела? Их хвост, уши и даже усики могут многое рассказать о настроении питомца.
                                                                                                 Некоторые учёные утверждают, что мурлыканье кошек помогает им быстрее заживлять раны. Возможно, именно поэтому они так часто мурчат, когда находятся рядом с хозяином.
                                                                                                 Коты – одни из самых загадочных существ. Они могут спать до 16 часов в день, но при этом сохранять невероятную грацию и ловкость.',
                                                                                                 'https://images.unsplash.com/photo-1493406300581-484b937cdc41?q=80&w=3870&auto=format&fit=crop&ixlib=rb-4.0.3&ixid=M3wxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8fA%3D%3D', 1, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),

                                                                                                ('Почему коты урчат и что это значит',
                                                                                                 'Знаете ли вы, что у кошек есть свой язык тела? Их хвост, уши и даже усики могут многое рассказать о настроении питомца.
                                                                                                 Коты – одни из самых загадочных существ. Они могут спать до 16 часов в день, но при этом сохранять невероятную грацию и ловкость.
                                                                                                 Многие думают, что кошки не привязываются к людям, но это не так. Исследования показывают, что они могут испытывать сильную привязанность к своим хозяевам.',
                                                                                                 'https://images.unsplash.com/photo-1618826411640-d6df44dd3f7a?q=80&w=2048&auto=format&fit=crop&ixlib=rb-4.0.3&ixid=M3wxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8fA%3D%3D', 1, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),

                                                                                                ('Как правильно кормить кошку',
                                                                                                 'Кошки имеют уникальное зрение: они видят в темноте в 6 раз лучше, чем люди. Это объясняется особенностями строения их глаз.
                                                                                                 Кошки могут развивать дружеские отношения не только с людьми, но и с другими животными, включая собак и даже попугаев.
                                                                                                 Кошки могут запоминать лица людей и отличать их друг от друга. Они также могут запоминать ваши привычки и распорядок дня.',
                                                                                                 'https://images.unsplash.com/photo-1533738363-b7f9aef128ce?q=80&w=3024&auto=format&fit=crop&ixlib=rb-4.0.3&ixid=M3wxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8fA%3D%3D', 1, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),

                                                                                                ('Кошки против собак: кто лучший компаньон?',
                                                                                                 'Коты – одни из самых загадочных существ. Они могут спать до 16 часов в день, но при этом сохранять невероятную грацию и ловкость.
                                                                                                 Кошки имеют уникальное зрение: они видят в темноте в 6 раз лучше, чем люди. Это объясняется особенностями строения их глаз.
                                                                                                 Если ваша кошка приносит вам пойманную добычу – это знак заботы. В дикой природе кошки учат котят охотиться именно таким образом.',
                                                                                                 'https://images.unsplash.com/photo-1494256997604-768d1f608cac?q=80&w=3929&auto=format&fit=crop&ixlib=rb-4.0.3&ixid=M3wxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8fA%3D%3D', 1, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),

                                                                                                ('Почему кошки любят лазить на высокие места',
                                                                                                 'Многие думают, что кошки не привязываются к людям, но это не так. Исследования показывают, что они могут испытывать сильную привязанность к своим хозяевам.
                                                                                                 Кошки имеют уникальное зрение: они видят в темноте в 6 раз лучше, чем люди. Это объясняется особенностями строения их глаз.
                                                                                                 Если ваша кошка приносит вам пойманную добычу – это знак заботы. В дикой природе кошки учат котят охотиться именно таким образом.',
                                                                                                 'https://images.unsplash.com/photo-1511044568932-338cba0ad803?q=80&w=3870&auto=format&fit=crop&ixlib=rb-4.0.3&ixid=M3wxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8fA%3D%3D', 1, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),

                                                                                                ('Как понять настроение вашей кошки',
                                                                                                 'Если кошка трётся о ваши ноги – это знак привязанности. Таким образом, она оставляет свой запах и «маркирует» вас как часть своей территории.
                                                                                                 Кошки имеют уникальное зрение: они видят в темноте в 6 раз лучше, чем люди. Это объясняется особенностями строения их глаз.
                                                                                                 Знаете ли вы, что у кошек есть свой язык тела? Их хвост, уши и даже усики могут многое рассказать о настроении питомца.',
                                                                                                 'https://images.unsplash.com/photo-1487300001871-12053913095d?q=80&w=3870&auto=format&fit=crop&ixlib=rb-4.0.3&ixid=M3wxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8fA%3D%3D', 1, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP)
ON CONFLICT DO NOTHING;



-- CREATE OR REPLACE FUNCTION post_service.update_updated_at_column()
--     RETURNS TRIGGER AS $$ BEGIN NEW.updated_at := CURRENT_TIMESTAMP;
--     RETURN NEW;
-- END;
-- $$ LANGUAGE plpgsql;