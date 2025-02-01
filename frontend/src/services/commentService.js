import api from "../api";

// ✅ Получить комментарии по `postId`
export const getCommentsByPostId = async (postId) => {
    try {
        const response = await api.get(`/api/comments?postId=${postId}`);
        return response.data;
    } catch (error) {
        console.error(`Ошибка при загрузке комментариев для поста ${postId}:`, error);
        throw error;
    }
};

// ✅ Создать новый комментарий
export const createComment = async (commentData) => {
    try {
        const response = await api.post("/api/comments", commentData);
        return response.data;
    } catch (error) {
        console.error("Ошибка при создании комментария:", error);
        throw error;
    }
};

// ✅ Обновить комментарий
export const updateComment = async (commentId, updatedContent) => {
    try {
        const response = await api.put(`/api/comments/${commentId}`, { content: updatedContent });
        return response.data;
    } catch (error) {
        console.error(`Ошибка при обновлении комментария с id: ${commentId}`, error);
        throw error;
    }
};

// ✅ Удалить комментарий
export const deleteComment = async (commentId) => {
    try {
        await api.delete(`/api/comments/${commentId}`);
    } catch (error) {
        console.error(`Ошибка при удалении комментария с id: ${commentId}`, error);
        throw error;
    }
};
