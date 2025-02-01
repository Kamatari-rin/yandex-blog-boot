import React, { useState, useRef, useEffect } from "react";
import { useNavigate } from "react-router-dom";
import { updatePost, deletePost } from "../services/postService";
import DeletePostModal from "./DeletePostModal";

function PostContent({ post }) {
    const [isEditing, setIsEditing] = useState(false);
    const [editedPost, setEditedPost] = useState({
        ...post,
        tags: post.tags.join(", "), // ✅ Преобразуем массив в строку при загрузке
    });
    const [isDeleteModalOpen, setDeleteModalOpen] = useState(false);
    const navigate = useNavigate();
    const contentRef = useRef(null); // ✅ Ссылка на textarea

    // Автоматическое увеличение textarea при вводе текста
    useEffect(() => {
        if (contentRef.current) {
            adjustTextareaHeight(contentRef.current);
        }
    }, [isEditing]); // Вызываем при начале редактирования

    // ✅ Функция авто-растяжения textarea
    const adjustTextareaHeight = (el) => {
        el.style.height = "auto"; // Сбрасываем высоту
        el.style.height = el.scrollHeight + "px"; // Устанавливаем новую высоту
    };

    // Функция сохранения изменений
    const handleSave = async () => {
        try {
            const updatedTags = editedPost.tags
                .split(",")
                .map((tag) => tag.trim())
                .filter((tag) => tag.length > 0);

            await updatePost(post.id, { ...editedPost, tags: updatedTags });
            setIsEditing(false);
        } catch (error) {
            console.error("Ошибка при обновлении поста:", error);
        }
    };

    const handleDelete = async () => {
        try {
            await deletePost(post.id);
            navigate("/"); // После удаления переходим на главную
        } catch (error) {
            console.error("Ошибка при удалении поста:", error);
        }
    };

    return (
        <div className="post-page">
            {/* Картинка */}
            {isEditing ? (
                <input
                    type="text"
                    className="post-page-image-url"
                    value={editedPost.imageUrl}
                    onChange={(e) => setEditedPost({ ...editedPost, imageUrl: e.target.value })}
                    placeholder="Введите URL изображения"
                />
            ) : (
                <img src={post.imageUrl} alt={post.title} className="post-page-image" />
            )}

            {/* Заголовок + Дата + Действия */}
            <div className="post-page-header">
                {isEditing ? (
                    <input
                        type="text"
                        className="post-page-title-input"
                        value={editedPost.title}
                        onChange={(e) => setEditedPost({ ...editedPost, title: e.target.value })}
                    />
                ) : (
                    <h1 className="post-page-title">{post.title}</h1>
                )}

                <span className="post-page-date">📅 {new Date(post.createdAt * 1000).toLocaleDateString()}</span>

                <div className="post-page-actions">
                    {isEditing ? (
                        <div className="post-page-buttons">
                            <button className="post-page-save-button" onClick={handleSave}>Сохранить</button>
                            <button className="post-page-cancel-button" onClick={() => setIsEditing(false)}>Отмена</button>
                        </div>
                    ) : (
                        <>
                            <button className="post-page-action-btn" onClick={() => setIsEditing(true)}>Редактировать</button>
                            <button className="post-page-action-btn" onClick={() => setDeleteModalOpen(true)}>Удалить</button>
                        </>
                    )}
                </div>
            </div>

            {/* Контент */}
            {isEditing ? (
                <textarea
                    ref={contentRef}
                    className="post-page-content-input"
                    value={editedPost.content}
                    onChange={(e) => {
                        setEditedPost({ ...editedPost, content: e.target.value });
                        adjustTextareaHeight(e.target); // ✅ Авто-изменение высоты
                    }}
                />
            ) : (
                <p className="post-page-content">{post.content}</p>
            )}

            {/* Теги */}
            {isEditing ? (
                <input
                    type="text"
                    className="post-page-tags-input"
                    value={editedPost.tags}
                    onChange={(e) => setEditedPost({ ...editedPost, tags: e.target.value })}
                    placeholder="Введите теги через запятую"
                />
            ) : (
                <div className="post-page-tags">
                    {post.tags.map((tag, index) => (
                        <span key={index} className="post-page-tag">#{tag}</span>
                    ))}
                </div>
            )}

            {/* Лайки и комментарии */}
            <div className="post-page-info">
                <span className="icon">
                    <svg viewBox="0 0 24 24">
                        <path d="M12 21.35l-1.45-1.32C5.4 15.36 2 12.28 2 8.5 2 5.42 4.42 3 7.5 3c1.74 0 3.41.81 4.5 2.09C13.09 3.81 14.76 3 16.5 3 19.58 3 22 5.42 22 8.5c0 3.78-3.4 6.86-8.55 11.54L12 21.35z"></path>
                    </svg>
                    {post.likesCount}
                </span>
                <span className="icon">
                    <svg viewBox="0 0 24 24">
                        <path d="M21 6h-2V4c0-1.1-.9-2-2-2H7c-1.1 0-2 .9-2 2v2H3c-1.1 0-2 .9-2 2v12c0 1.1.9 2 2 2h18c1.1 0 2-.9 2-2V8c0-1.1-.9-2-2-2z"></path>
                    </svg>
                    {post.commentsCount}
                </span>
            </div>

            {/* Модальное окно для удаления */}
            <DeletePostModal isOpen={isDeleteModalOpen} onClose={() => setDeleteModalOpen(false)} onConfirm={handleDelete} />
        </div>
    );
}

export default PostContent;
