import React, { useEffect, useState } from 'react';
import { toggleLike } from '../services/postService'; // Для запроса на сервер
import { useLikes } from '../context/LikeContext'; // Для доступа к контексту лайков

const LikeButton = ({ postId, initialLikesCount, initialLikeId }) => {
    const { likedPosts, toggleLike: updateContextLike } = useLikes(); // Контекст для лайков
    const [likes, setLikes] = useState(initialLikesCount);
    const [likeId, setLikeId] = useState(initialLikeId);

    const handleLike = async () => {
        const currentLikeState = likedPosts[postId] || false;

        // Инвертируем состояние лайка в контексте
        updateContextLike(postId, currentLikeState);

        // Мгновенно обновляем количество лайков на фронте
        setLikes(currentLikeState ? likes - 1 : likes + 1);

        try {
            // Отправляем запрос на сервер
            const { liked, likeId } = await toggleLike(postId, 1, likeId, currentLikeState);

            // Обновляем состояние на сервере
            setLikes(liked ? likes + 1 : likes - 1);
            setLikeId(likeId); // Обновляем ID лайка
        } catch (error) {
            console.error('Ошибка при изменении лайка:', error);

            // В случае ошибки откатываем изменения
            setLikes(likes);
        }
    };

    useEffect(() => {
        // Инициализируем состояние лайка, если он есть
        if (likedPosts[postId]) {
            setLikes(likedPosts[postId] ? likes + 1 : likes - 1);
        }
    }, [likedPosts, postId]);

    return (
        <div className="like-button">
            <button onClick={handleLike} style={{ cursor: 'pointer' }}>
                {likedPosts[postId] ? 'Снять лайк' : 'Поставить лайк'}
            </button>
            <span>{likes} лайков</span>
        </div>
    );
};

export default LikeButton;
