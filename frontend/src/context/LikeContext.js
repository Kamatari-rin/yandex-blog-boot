import React, { createContext, useContext, useState, useEffect } from "react";
import { getPosts } from "../api/postsAPI"; // Импорт постов
import { getPostLikesStatuses, toggleLike } from "../api/likesAPI"; // Импорт лайков

const LikeContext = createContext();

export const LikeProvider = ({ children }) => {
    const [likedPosts, setLikedPosts] = useState({});
    const userId = 1; // Константа пользователя

    // Загружаем данные о лайках для всех постов
    useEffect(() => {
        const fetchPostsAndLikes = async () => {
            try {
                const posts = await getPosts();
                const postIds = posts.map((post) => post.id);
                const likesData = await getPostLikesStatuses(postIds, userId);
                const likedState = likesData.reduce((acc, { targetId, liked, targetType }) => {
                    acc[targetId] = { liked, targetType };
                    return acc;
                }, {});
                setLikedPosts(likedState);
            } catch (error) {
                console.error("Ошибка при загрузке лайков:", error);
            }
        };

        fetchPostsAndLikes();
    }, []);

    const handleToggleLike = async (targetId, currentLikeState, targetType) => {
        const newLikedState = !currentLikeState;  // Новое состояние
        // Обновляем локальное состояние оптимистично
        setLikedPosts((prevState) => ({
            ...prevState,
            [targetId]: { liked: newLikedState, targetType },
        }));

        try {
            await toggleLike({ userId, targetId, targetType, liked: newLikedState });
        } catch (error) {
            console.error("Ошибка при обновлении лайка на сервере", error);
            // Откат в случае ошибки
            setLikedPosts((prevState) => ({
                ...prevState,
                [targetId]: { liked: currentLikeState, targetType },
            }));
        }
    };

    return (
        <LikeContext.Provider value={{ likedPosts, toggleLike: handleToggleLike }}>
            {children}
        </LikeContext.Provider>
    );
};

export const useLikes = () => useContext(LikeContext);
