import React, { useState, useEffect } from "react";
import { useParams } from "react-router-dom";
import { getPostById } from "../services/postService";
import PostContent from "../components/PostContent";
import CommentsSection from "../components/CommentsSection";

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
            <PostContent post={post} />
            <CommentsSection postId={postId} /> {/* ✅ Передаём postId */}
        </div>
    );
}

export default PostPage;
