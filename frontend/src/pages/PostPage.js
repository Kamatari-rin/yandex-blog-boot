import React, { useState, useEffect } from "react";
import { useParams } from "react-router-dom";
import { getPostById } from "../services/postService";

function PostPage() {
    const { postId } = useParams();
    const [post, setPost] = useState(null);
    const [loading, setLoading] = useState(true);
    const [error, setError] = useState(null);

    useEffect(() => {
        const fetchPost = async () => {
            setLoading(true);
            try {
                const postData = await getPostById(postId); // Запрос для получения поста
                setPost(postData);
            } catch (err) {
                setError("Не удалось загрузить пост");
            } finally {
                setLoading(false);
            }
        };
        fetchPost();
    }, [postId]);

    if (loading) return <p>Загрузка...</p>;
    if (error) return <p style={{ color: "red" }}>{error}</p>;

    return (
        <div>
            <h1>{post.title}</h1>
            <img src={post.imageUrl} alt={post.title} width="300" />
            <p>{post.content}</p>
            <div>
                <span>Теги: {post.tags.join(", ")}</span>
            </div>

            <button>Редактировать</button>
            <button>Удалить</button>

            {/* Комментарии */}
            <div>
                <h3>Комментарии</h3>
            </div>
        </div>
    );
}

export default PostPage;