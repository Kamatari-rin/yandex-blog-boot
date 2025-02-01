import React, { useState } from "react";
import PostList from "../components/PostList";


function PostListPage() {
    const [page, setPage] = useState(1);
    const [postsPerPage, setPostsPerPage] = useState(10);

    const handlePageChange = (newPage) => {
        setPage(newPage);
    };

    const handlePostsPerPageChange = (event) => {
        setPostsPerPage(Number(event.target.value));
    };

    return (
        <div>
            <h1>Лента постов</h1>

            <div>
                <label htmlFor="postsPerPage">Постов на страницу:</label>
                <select id="postsPerPage" value={postsPerPage} onChange={handlePostsPerPageChange}>
                    <option value={10}>10</option>
                    <option value={20}>20</option>
                    <option value={50}>50</option>
                </select>
            </div>

            <PostList page={page} postsPerPage={postsPerPage} />

            <div>
                <button onClick={() => handlePageChange(page - 1)} disabled={page === 1}>
                    Назад
                </button>
                <span>{page}</span>
                <button onClick={() => handlePageChange(page + 1)}>Вперед</button>
            </div>
        </div>
    );
}

export default PostListPage;