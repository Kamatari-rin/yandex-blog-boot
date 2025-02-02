import React, { createContext, useContext, useState } from 'react';

// Создание контекста для лайков
const LikeContext = createContext();

// Провайдер контекста
export const LikeProvider = ({ children }) => {
    // Храним лайки каждого поста по ID
    const [likedPosts, setLikedPosts] = useState({});

    // Функция для обновления состояния лайка
    const toggleLike = (postId, liked) => {
        setLikedPosts((prevState) => {
            const newState = { ...prevState };
            if (liked) {
                // Если лайк уже поставлен, снимаем его
                delete newState[postId];
            } else {
                // Если лайк не поставлен, ставим его
                newState[postId] = true;
            }
            return newState;
        });
    };

    return (
        <LikeContext.Provider value={{ likedPosts, toggleLike }}>
            {children}
        </LikeContext.Provider>
    );
};

// Хук для использования контекста
export const useLikes = () => useContext(LikeContext);
