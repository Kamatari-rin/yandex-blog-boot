import React, { useState, useEffect } from "react";
import { getPosts } from "../services/postService";
import PostPreview from "./PostPreview"; // Превью поста

function PostList() {
    const [posts, setPosts] = useState([]);
    const [loading, setLoading] = useState(true);
    const [error, setError] = useState(null);
    const [page, setPage] = useState(1);
    const [tagsFilter, setTagsFilter] = useState("");

    useEffect(() => {
        const fetchPosts = async () => {
            setLoading(true);
            try {
                const postsData = await getPosts(page, 10);
                setPosts(postsData);
            } catch (err) {
                setError("Не удалось загрузить посты");
            } finally {
                setLoading(false);
            }
        };
        fetchPosts();
    }, [page]);

    return (
        <div>
            <h1>Лента постов</h1>
            {error && <p style={{ color: "red" }}>{error}</p>}
            {loading ? (
                <p>Загрузка...</p>
            ) : (
                <div>
                    <div>
                        <input
                            type="text"
                            placeholder="Поиск по тегам"
                            value={tagsFilter}
                            onChange={(e) => setTagsFilter(e.target.value)}
                        />
                    </div>

                    {posts.map((post) => (
                        <PostPreview key={post.id} post={post} />
                    ))}

                    <div>
                        <button onClick={() => setPage(page - 1)} disabled={page === 1}>
                            Назад
                        </button>
                        <button onClick={() => setPage(page + 1)}>Вперед</button>
                    </div>
                </div>
            )}
        </div>
    );
}

export default PostList;