import React, { useState, useRef, useEffect } from "react";

function PostEdit({ post, onSave, onCancel }) {
    const [editedPost, setEditedPost] = useState({
        ...post,
        tags: post.tags.join(", "),
    });
    const contentRef = useRef(null);

    useEffect(() => {
        if (contentRef.current) {
            adjustTextareaHeight(contentRef.current);
        }
    }, []);

    const adjustTextareaHeight = (el) => {
        el.style.height = "auto";
        el.style.height = el.scrollHeight + "px";
    };

    const handleSave = () => {
        const updatedTags = editedPost.tags
            .split(",")
            .map((tag) => tag.trim())
            .filter((tag) => tag.length > 0);
        onSave({ ...editedPost, tags: updatedTags });
    };

    return (
        <>
            <input
                type="text"
                className="post-page-image-url"
                value={editedPost.imageUrl}
                onChange={(e) =>
                    setEditedPost({ ...editedPost, imageUrl: e.target.value })
                }
                placeholder="Введите URL изображения"
            />
            <input
                type="text"
                className="post-page-title-input"
                value={editedPost.title}
                onChange={(e) =>
                    setEditedPost({ ...editedPost, title: e.target.value })
                }
            />
            <span className="post-page-date">
        📅 {new Date(editedPost.createdAt).toLocaleDateString()}
      </span>
            <textarea
                ref={contentRef}
                className="post-page-content-input"
                value={editedPost.content}
                onChange={(e) => {
                    setEditedPost({ ...editedPost, content: e.target.value });
                    adjustTextareaHeight(e.target);
                }}
            />
            <input
                type="text"
                className="post-page-tags-input"
                value={editedPost.tags}
                onChange={(e) =>
                    setEditedPost({ ...editedPost, tags: e.target.value })
                }
                placeholder="Введите теги через запятую"
            />
            <div className="post-page-buttons">
                <button className="post-page-save-button" onClick={handleSave}>
                    Сохранить
                </button>
                <button className="post-page-cancel-button" onClick={onCancel}>
                    Отмена
                </button>
            </div>
        </>
    );
}

export default PostEdit;
