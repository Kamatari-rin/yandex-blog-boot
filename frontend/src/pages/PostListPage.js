import React, { useState, useEffect, useCallback } from "react";
import { useSearchParams } from "react-router-dom";
import PostList from "../components/post-list/PostList";
import AddPostForm from "../components/AddPostForm";
import LoadingError from "../components/error/LoadingError";
import Pagination from "../components/Pagination";
import useDebounce from "../hooks/useDebounce";
import usePosts from "../hooks/usePosts";

const PostListPage = () => {
    const [searchParams, setSearchParams] = useSearchParams();
    const initialPage = Number(searchParams.get("page")) || 1;
    const initialTag = searchParams.get("tag") || "";

    const [page, setPage] = useState(initialPage);
    const [tagsFilter, setTagsFilter] = useState(initialTag);
    const [limit, setLimit] = useState(10);
    const [isAddPostFormVisible, setAddPostFormVisible] = useState(false);

    const debouncedTagsFilter = useDebounce(tagsFilter, 500);

    const { posts, loading, error, refetchPosts, isLastPage } = usePosts(
        debouncedTagsFilter,
        page,
        limit
    );

    useEffect(() => {
        const params = { page };
        if (debouncedTagsFilter.trim()) {
            params.tag = debouncedTagsFilter;
        }
        setSearchParams(params);
    }, [page, debouncedTagsFilter, setSearchParams]);

    const onPageChange = useCallback((newPage) => {
        if (newPage > 0) {
            setPage(newPage);
        }
    }, []);

    const handleLimitChange = (newLimit) => {
        setLimit(newLimit);
        setPage(1);
    };

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
            <LoadingError loading={loading} error={error} />
            <PostList posts={posts} refetchPosts={refetchPosts} />
            <Pagination
                page={page}
                onPageChange={onPageChange}
                isNextDisabled={isLastPage}
                limit={limit}
                onLimitChange={handleLimitChange}
            />
        </main>
    );
};

export default PostListPage;
