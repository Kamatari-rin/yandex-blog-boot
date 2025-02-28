import React from "react";

function PostHeader({ title, createdAt, onEdit, onDelete }) {
    return (
        <div className="post-page-header">
            <h1 className="post-page-title">{title}</h1>
            <span className="post-page-date">
                📅 {createdAt
                    ? new Intl.DateTimeFormat("ru-RU", {
                        day: "2-digit",
                        month: "long",
                        year: "numeric",
                    }).format(new Date(createdAt))
                : "Дата неизвестна"}
            </span>

            <div className="post-page-actions">
                <button type="button" className="post-page-action-btn" onClick={onEdit}>
                    Редактировать
                </button>
                <button type="button" className="post-page-action-btn" onClick={onDelete}>
                    Удалить
                </button>
            </div>
        </div>
    );
}

export default PostHeader;
