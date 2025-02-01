import React, { useState, useEffect } from "react";
import PostFeed from "./PostFeed"; // Теперь используем новый компонент
import NewPostModal from "./NewPostModal"; // Импортируем модальное окно
import usePosts from "../hooks/usePosts";
import useDebounce from "../hooks/useDebounce";
import LoadingError from "../components/LoadingError";
import Pagination from "../components/Pagination";

const PostList = () => {
    // Состояние для фильтра тегов, страницы и модального окна
    const [tagsFilter, setTagsFilter] = useState("");
    const [page, setPage] = useState(1);
    const [isModalOpen, setIsModalOpen] = useState(false);

    // Дебаунсинг для фильтра тегов
    const debouncedTag = useDebounce(tagsFilter, 500);

    // Получаем посты через хук usePosts
    const { posts, loading, error } = usePosts(debouncedTag, page);

    // Состояние для лайков
    const [likedPosts, setLikedPosts] = useState({});

    // Функция для обновления состояния лайков
    const updateLikeState = (postId, liked, likeId) => {
        setLikedPosts((prev) => ({
            ...prev,
            [postId]: { liked, likeId },
        }));
    };

    return (
        <div className="container">
            <h1>Блог про Котиков</h1>

            {/* Ошибки и состояние загрузки */}
            <LoadingError loading={loading} error={error} />

            {/* Поле поиска и кнопка нового поста */}
            <div className="search-bar-container">
                <input
                    type="text"
                    placeholder="Поиск по тегам..."
                    value={tagsFilter}
                    onChange={(e) => setTagsFilter(e.target.value)} // Обновляем фильтр тегов
                    className="search-input"
                />
                <button className="new-post-button" onClick={() => setIsModalOpen(true)}>
                    Новый пост
                </button>
            </div>

            {/* Отображение постов */}
            <PostFeed posts={posts} likedPosts={likedPosts} updateLikeState={updateLikeState} />

            {/* Навигация по страницам */}
            <Pagination page={page} setPage={setPage} />

            {/* Модальное окно для создания поста */}
            {isModalOpen && <NewPostModal onClose={() => setIsModalOpen(false)} />}
        </div>
    );
};

export default PostList;
