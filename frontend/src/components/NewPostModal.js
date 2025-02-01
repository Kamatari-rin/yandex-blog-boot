import React, { useState } from "react";
import { createPost } from "../services/postService";

function NewPostModal({ onClose }) {
    const [title, setTitle] = useState("");
    const [imageUrl, setImageUrl] = useState("");
    const [content, setContent] = useState("");
    const [tags, setTags] = useState("");

    const handleSubmit = async (e) => {
        e.preventDefault();

        // Формируем объект для отправки
        const newPost = {
            userId: 1, // Пока что хардкодим
            title,
            imageUrl,
            content,
            tags: tags.split(",").map(tag => tag.trim()), // Превращаем строку в массив
        };

        try {
            await createPost(newPost);
            alert("Пост успешно создан!");
            onClose(); // Закрываем модальное окно
            window.location.reload(); // Обновляем список постов
        } catch (error) {
            console.error("Ошибка при создании поста:", error);
        }
    };

    return (
        <div className="modal-overlay">
            <div className="modal-content">
                <h2>Создать новый пост</h2>
                <form onSubmit={handleSubmit}>
                    <label>Заголовок:</label>
                    <input type="text" value={title} onChange={(e) => setTitle(e.target.value)} required />

                    <label>Ссылка на изображение:</label>
                    <input type="text" value={imageUrl} onChange={(e) => setImageUrl(e.target.value)} required />

                    <label>Содержание:</label>
                    <textarea value={content} onChange={(e) => setContent(e.target.value)} required />

                    <label>Теги (через запятую):</label>
                    <input type="text" value={tags} onChange={(e) => setTags(e.target.value)} />

                    <div className="modal-buttons">
                        <button type="submit" className="submit-button">Создать</button>
                        <button type="button" className="cancel-button" onClick={onClose}>Отмена</button>
                    </div>
                </form>
            </div>
        </div>
    );
}

export default NewPostModal;
