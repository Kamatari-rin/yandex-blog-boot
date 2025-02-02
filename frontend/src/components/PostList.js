import React, { useState } from "react";
import PostFeed from "./PostFeed"; // Используем новый компонент
import usePosts from "../hooks/usePosts"; // Используем хук для получения постов
import LoadingError from "../components/LoadingError";
import Pagination from "../components/Pagination";

const PostList = () => {
    const [tagsFilter, setTagsFilter] = useState("");
    const [page, setPage] = useState(1);

    // Получаем посты через хук usePosts
    const { posts, loading, error, refetchPosts } = usePosts(tagsFilter, page); // refetchPosts теперь есть

    // Состояние для лайков
    const [likedPosts, setLikedPosts] = useState({});

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

            {/* Поле поиска */}
            <div className="search-bar-container">
                <input
                    type="text"
                    placeholder="Поиск по тегам..."
                    value={tagsFilter}
                    onChange={(e) => setTagsFilter(e.target.value)} // Обновляем фильтр тегов
                    className="search-input"
                />
            </div>

            {/* Отображение постов */}
            <PostFeed
                posts={posts}
                likedPosts={likedPosts}
                updateLikeState={updateLikeState}
                refetchPosts={refetchPosts} // Передаем refetchPosts для обновления постов
            />

            {/* Навигация по страницам */}
            <Pagination page={page} setPage={setPage} />
        </div>
    );
};

export default PostList;
