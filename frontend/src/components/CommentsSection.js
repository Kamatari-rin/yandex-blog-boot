import React, { useState, useEffect } from "react";
import { getCommentsByPostId, createComment, deleteComment, updateComment } from "../services/commentService";

function CommentsSection({ postId }) {
    const [comments, setComments] = useState([]);
    const [newComment, setNewComment] = useState("");
    const [loading, setLoading] = useState(true);
    const [error, setError] = useState(null);
    const [editingCommentId, setEditingCommentId] = useState(null);
    const [editedCommentContent, setEditedCommentContent] = useState("");

    // Загружаем комментарии при монтировании или изменении postId
    const fetchComments = async () => {
        try {
            setLoading(true);
            const data = await getCommentsByPostId(postId);
            setComments(data);
        } catch (err) {
            setError("Ошибка при загрузке комментариев");
        } finally {
            setLoading(false);
        }
    };

    useEffect(() => {
        fetchComments(); // Загружаем комментарии при монтировании компонента или изменении postId
    }, [postId]);

    // Функция для добавления нового комментария
    const handleAddComment = async () => {
        if (newComment.trim() === "") return;

        try {
            await createComment({
                postId,
                userId: 1, // ⚠ Заменить на реальный userId (например, из авторизации)
                content: newComment,
            });

            fetchComments(); // Перезагружаем комментарии с сервера
            setNewComment(""); // Очищаем поле ввода
        } catch (err) {
            console.error("Ошибка при добавлении комментария:", err);
        }
    };

    // Функция для удаления комментария
    const handleDeleteComment = async (commentId) => {
        try {
            await deleteComment(commentId);
            fetchComments(); // Перезагружаем комментарии с сервера
        } catch (err) {
            console.error("Ошибка при удалении комментария:", err);
        }
    };

    // Функция для начала редактирования комментария
    const handleEditComment = (commentId, content) => {
        setEditingCommentId(commentId);
        setEditedCommentContent(content);
    };

    // Функция для сохранения изменений редактируемого комментария
    const handleSaveEdit = async () => {
        if (editedCommentContent.trim() === "") return;

        try {
            await updateComment(editingCommentId, editedCommentContent);
            fetchComments(); // Перезагружаем комментарии с сервера
            setEditingCommentId(null); // Выход из режима редактирования
            setEditedCommentContent(""); // Очищаем поле ввода
        } catch (err) {
            console.error("Ошибка при сохранении изменений комментария:", err);
        }
    };

    // Функция для отмены редактирования
    const handleCancelEdit = () => {
        setEditingCommentId(null); // Выход из режима редактирования
        setEditedCommentContent(""); // Сбросить изменения
    };

    // Обработчик нажатия на Ctrl+Enter для сохранения комментария
    const handleKeyDown = (e) => {
        if (e.key === "Enter" && e.ctrlKey) {
            handleSaveEdit();
        }
    };

    return (
        <div className="post-page-comments">
            <h3 className="post-page-comments-title">Комментарии</h3>

            {/* Загрузка */}
            {loading && <p>Загрузка комментариев...</p>}

            {/* Отображение ошибок */}
            {error && <p className="error">{error}</p>}

            {/* Список комментариев */}
            <ul className="comment-list">
                {comments.map((comment) => (
                    <li key={comment.id} className="comment">
                        {/* Если редактируем этот комментарий, показываем поле ввода */}
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
                                <small>{new Date(comment.createdAt).toLocaleDateString()}</small>
                                <button onClick={() => handleEditComment(comment.id, comment.content)} className="comment-edit">Редактировать</button>
                                <button onClick={() => handleDeleteComment(comment.id)} className="comment-delete">Удалить</button>
                            </p>
                        )}
                    </li>
                ))}
            </ul>

            {/* Форма для добавления нового комментария */}
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
