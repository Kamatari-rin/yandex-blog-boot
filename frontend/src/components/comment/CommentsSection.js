import React, { useState, useEffect } from "react";
import { getCommentsByPostId, createComment, deleteComment, updateComment } from "../../api/commentAPI";

function CommentsSection({ postId }) {
    const [comments, setComments] = useState([]);
    const [newComment, setNewComment] = useState("");
    const [loading, setLoading] = useState(true);
    const [error, setError] = useState(null);
    const [editingCommentId, setEditingCommentId] = useState(null);
    const [editedCommentContent, setEditedCommentContent] = useState("");

    const fetchComments = async () => {
        try {
            setLoading(true);
            const data = await getCommentsByPostId(postId);
            setComments(data || []);
        } catch (err) {
            setError("Ошибка при загрузке комментариев");
        } finally {
            setLoading(false);
        }
    };

    useEffect(() => {
        fetchComments();
    }, [postId]);

    const handleAddComment = async () => {
        if (newComment.trim() === "") return;

        try {
            await createComment({
                postId,
                userId: 1,
                content: newComment,
            });

            setTimeout(fetchComments, 300);
            setNewComment("");
        } catch (err) {
            console.error("Ошибка при добавлении комментария:", err);
        }
    };

    const handleDeleteComment = async (commentId) => {
        try {
            await deleteComment(commentId);
            fetchComments();
        } catch (err) {
            console.error("Ошибка при удалении комментария:", err);
        }
    };

    const handleEditComment = (commentId, content) => {
        setEditingCommentId(commentId);
        setEditedCommentContent(content);
    };

    const handleSaveEdit = async () => {
        if (editedCommentContent.trim() === "") return;

        try {
            await updateComment(editingCommentId, editedCommentContent);
            fetchComments();
            setEditingCommentId(null);
            setEditedCommentContent("");
        } catch (err) {
            console.error("Ошибка при сохранении изменений комментария:", err);
        }
    };

    const handleCancelEdit = () => {
        setEditingCommentId(null);
        setEditedCommentContent("");
    };

    const handleKeyDown = (e) => {
        if (e.key === "Enter" && e.ctrlKey) {
            handleSaveEdit();
        }
    };

    return (
        <div className="post-page-comments">
            <h3 className="post-page-comments-title">Комментарии</h3>

            {loading && <p>Загрузка комментариев...</p>}

            {error && <p className="error">{error}</p>}

            <ul className="comment-list">
                {comments.map((comment) => (
                    <li key={comment.id} className="comment">
                        {editingCommentId === comment.id ? (
                            <div>
                                <textarea
                                    value={editedCommentContent}
                                    onChange={(e) => setEditedCommentContent(e.target.value)}
                                    onKeyDown={handleKeyDown}
                                    placeholder="Редактировать комментарий..."
                                    className="comment-input"
                                />
                                <button onClick={handleSaveEdit} className="comment-submit">Сохранить</button>
                                <button onClick={handleCancelEdit} className="comment-cancel">Отмена</button>
                            </div>
                        ) : (
                            <p>
                                <strong>{comment.userName}:</strong> {comment.content}
                                <small>
                                    {comment.createdAt
                                        ? new Intl.DateTimeFormat("ru-RU", {
                                            day: "2-digit",
                                            month: "long",
                                            year: "numeric",
                                        }).format(new Date(comment.createdAt))
                                        : "Дата неизвестна"}
                                </small>
                                <button onClick={() => handleEditComment(comment.id, comment.content)} className="comment-edit">Редактировать</button>
                                <button onClick={() => handleDeleteComment(comment.id)} className="comment-delete">Удалить</button>
                            </p>
                        )}
                    </li>
                ))}
            </ul>

            <div className="comment-form">
                <textarea
                    value={newComment}
                    onChange={(e) => setNewComment(e.target.value)}
                    placeholder="Напишите комментарий..."
                    className="comment-input"
                />
                <button onClick={handleAddComment} className="comment-submit">Добавить</button>
            </div>
        </div>
    );
}

export default CommentsSection;
