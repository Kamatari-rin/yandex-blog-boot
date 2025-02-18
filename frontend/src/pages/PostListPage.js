import React, { useState, useEffect, useCallback } from "react";
import { useSearchParams } from "react-router-dom";
import PostList from "../components/post-list/PostList";
import AddPostForm from "../components/AddPostForm";
import LoadingError from "../components/error/LoadingError";
import Pagination from "../components/Pagination";
import useDebounce from "../hooks/useDebounce";
import usePosts from "../hooks/usePosts"; // <-- ВАЖНО: сюда импортируем хук для загрузки постов

const PostListPage = () => {
    const [searchParams, setSearchParams] = useSearchParams();
    const initialPage = Number(searchParams.get("page")) || 1;
    const initialTag = searchParams.get("tag") || "";

    // Локальные состояния для страницы и фильтра
    const [page, setPage] = useState(initialPage);
    const [tagsFilter, setTagsFilter] = useState(initialTag);

    // Управляем формой создания поста
    const [isAddPostFormVisible, setAddPostFormVisible] = useState(false);

    // Дебаунс для фильтра тегов
    const debouncedTagsFilter = useDebounce(tagsFilter, 500);

    // ВАЖНО: здесь вызываем хук usePosts, чтобы получить данные
    const { posts, loading, error, refetchPosts, totalPages } = usePosts(
        debouncedTagsFilter,
        page
    );

    // Синхронизируем URL (page, tag) с состоянием
    useEffect(() => {
        const params = { page };
        if (debouncedTagsFilter.trim()) {
            params.tag = debouncedTagsFilter;
        }
        setSearchParams(params);
    }, [page, debouncedTagsFilter, setSearchParams]);

    // Обработка изменения страницы
    const onPageChange = useCallback((newPage) => {
        if (newPage > 0) {
            setPage(newPage);
        }
    }, []);

    // Открытие/закрытие формы создания поста
    const handleAddPostClick = () => setAddPostFormVisible(true);
    const handleCloseForm = () => setAddPostFormVisible(false);

    return (
        <main className="container">
            <h1>Блог про Котиков</h1>
            <div
                className="search-bar-container"
                style={{ display: "flex", justifyContent: "space-between", alignItems: "center" }}
            >
                <input
                    type="text"
                    placeholder="Поиск по тегам..."
                    value={tagsFilter}
                    onChange={(e) => setTagsFilter(e.target.value)}
                    className="search-input"
                />
                <button onClick={handleAddPostClick} className="new-post-button">
                    Новый пост
                </button>
            </div>

            {isAddPostFormVisible && <AddPostForm onClose={handleCloseForm} />}

            {/* Можно выводить ошибки и загрузку либо здесь, либо внутри PostList */}
            <LoadingError loading={loading} error={error} />

            {/* Передаём готовые данные в PostList */}
            <PostList
                posts={posts}
                refetchPosts={refetchPosts}
            />

            <Pagination
                page={page}
                onPageChange={onPageChange}
                isNextDisabled={page >= totalPages}
            />
        </main>
    );
};

export default PostListPage;
