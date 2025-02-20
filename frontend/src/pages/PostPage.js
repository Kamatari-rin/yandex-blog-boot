import React, { useState, useEffect } from "react";
import { useParams, useNavigate } from "react-router-dom";
import { getPostById, updatePost, deletePost } from "../api/postsAPI";
import PostContent from "../components/post-content/PostContent";
import CommentsSection from "../components/comment/CommentsSection";

function PostPage() {
    const { postId } = useParams();
    const navigate = useNavigate();
    const [post, setPost] = useState(null);
    const [loading, setLoading] = useState(true);
    const [error, setError] = useState(null);

    useEffect(() => {
        const fetchPost = async () => {
            setLoading(true);
            setError(null);
            try {
                const postData = await getPostById(postId);
                setPost(postData);
            } catch (err) {
                setError("Не удалось загрузить пост");
            } finally {
                setLoading(false);
            }
        };
        fetchPost();
    }, [postId]);

    const handlePostUpdate = async (updatedPostData) => {
        try {
            const updatedPost = await updatePost(post.id, updatedPostData);
            setPost(updatedPost);
        } catch (error) {
            console.error("Ошибка при обновлении поста:", error);
        }
    };

    const handlePostDelete = async (postId) => {
        try {
            await deletePost(postId);
            navigate("/");
        } catch (error) {
            console.error("Ошибка при удалении поста:", error);
        }
    };

    if (loading) return <p>Загрузка...</p>;
    if (error) return <p style={{ color: "red" }}>{error}</p>;

    return (
        <div>
            <PostContent
                post={post}
                onPostUpdate={handlePostUpdate}
                onPostDelete={handlePostDelete}
            />
            <CommentsSection postId={postId} />
        </div>
    );
}

export default PostPage;
