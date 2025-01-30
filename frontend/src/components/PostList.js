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
        <div className="max-w-4xl mx-auto p-4">
            <h1 className="text-3xl font-bold text-center mb-6">Лента постов</h1>

            {error && <p className="text-red-500 text-center">{error}</p>}

            {loading ? (
                <p className="text-center text-gray-500">Загрузка...</p>
            ) : (
                <div>
                    {/* Поле ввода для поиска */}
                    <div className="mb-4 flex justify-center">
                        <input
                            type="text"
                            placeholder="Поиск по тегам"
                            value={tagsFilter}
                            onChange={(e) => setTagsFilter(e.target.value)}
                            className="w-full max-w-md p-2 border border-gray-300 rounded-md shadow-sm focus:ring focus:ring-blue-200"
                        />
                    </div>

                    {/* Грид с постами */}
                    <div className="grid md:grid-cols-2 lg:grid-cols-3 gap-6">
                        {posts.map((post) => (
                            <PostPreview key={post.id} post={post} />
                        ))}
                    </div>

                    {/* Навигация по страницам */}
                    <div className="flex justify-center space-x-4 mt-6">
                        <button
                            onClick={() => setPage(page - 1)}
                            disabled={page === 1}
                            className="px-4 py-2 bg-gray-300 text-gray-700 rounded-md disabled:opacity-50 disabled:cursor-not-allowed"
                        >
                            Назад
                        </button>
                        <button
                            onClick={() => setPage(page + 1)}
                            className="px-4 py-2 bg-blue-500 text-white rounded-md hover:bg-blue-600"
                        >
                            Вперед
                        </button>
                    </div>
                </div>
            )}
        </div>
    );
}

export default PostList;
