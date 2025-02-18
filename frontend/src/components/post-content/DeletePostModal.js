import React from "react";

function DeletePostModal({ isOpen, onClose, onConfirm }) {
    if (!isOpen) return null;

    return (
        <div className="modal-overlay">
            <div className="modal-content">
                <h2 className="modal-title">Удалить пост?</h2>
                <p className="modal-text">Вы уверены, что хотите удалить этот пост? Это действие нельзя отменить.</p>
                <div className="modal-buttons">
                    <button className="confirm-button" onClick={onConfirm}>Да, удалить</button>
                    <button className="cancel-button" onClick={onClose}>Отмена</button>
                </div>
            </div>
        </div>
    );
}

export default DeletePostModal;
