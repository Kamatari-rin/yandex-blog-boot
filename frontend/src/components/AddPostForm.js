import React, { useState } from "react";
import { createPost } from "../api/postsAPI";
import { DEFAULT_USER_ID } from "../config/constants";

const AddPostForm = ({ onClose }) => {
    const [title, setTitle] = useState("");
    const [imageUrl, setImageUrl] = useState("");
    const [content, setContent] = useState("");
    const [tags, setTags] = useState("");
    const [error, setError] = useState("");

    const handleSubmit = async (e) => {
        e.preventDefault();
        if (!title.trim() || !content.trim()) {
            setError("Название и текст поста обязательны.");
            return;
        }
        const tagsArray = tags.split(",").map((tag) => tag.trim()).filter((tag) => tag);

        const postData = {
            userId: DEFAULT_USER_ID,
            title,
            imageUrl,
            content,
            tags: tagsArray,
        };

        try {
            await createPost(postData);
            onClose();
        } catch (err) {
            console.error("Ошибка при создании поста:", err);
            setError("Не удалось создать пост.");
        }
    };

    return (
        <div className="add-post-form-container">
            <form onSubmit={handleSubmit} className="add-post-form">
                <h2>Создать новый пост</h2>
                {error && <p className="error">{error}</p>}
                <input
                    type="text"
                    placeholder="Название"
                    value={title}
                    onChange={(e) => setTitle(e.target.value)}
                    required
                />
                <input
                    type="text"
                    placeholder="URL изображения"
                    value={imageUrl}
                    onChange={(e) => setImageUrl(e.target.value)}
                />
                <textarea
                    placeholder="Текст поста"
                    value={content}
                    onChange={(e) => setContent(e.target.value)}
                    required
                />
                <input
                    type="text"
                    placeholder="Теги (через запятую)"
                    value={tags}
                    onChange={(e) => setTags(e.target.value)}
                />
                <div className="form-buttons">
                    <button type="submit">Создать пост</button>
                    <button type="button" onClick={onClose}>
                        Отмена
                    </button>
                </div>
            </form>
        </div>
    );
};

export default AddPostForm;
