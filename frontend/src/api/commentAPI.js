import api from "./index";

export const getCommentsByPostId = async (postId) => {
    try {
        const data = await api.get(`/api/comments?postId=${postId}`);
        console.log("Ответ из API в getCommentsByPostId:", data);
        return data;
    } catch (error) {
        console.error(`Ошибка при загрузке комментариев для поста ${postId}:`, error);
        throw error;
    }
};

export const createComment = async (commentData) => {
    try {
        const data = await api.post("/api/comments", commentData);
        return data;
    } catch (error) {
        console.error("Ошибка при создании комментария:", error);
        throw error;
    }
};

export const updateComment = async (commentId, updatedContent) => {
    try {
        const data = await api.put(`/api/comments/${commentId}`, { content: updatedContent });
        return data;
    } catch (error) {
        console.error(`Ошибка при обновлении комментария с id: ${commentId}`, error);
        throw error;
    }
};

export const deleteComment = async (commentId) => {
    try {
        await api.delete(`/api/comments/${commentId}`);
    } catch (error) {
        console.error(`Ошибка при удалении комментария с id: ${commentId}`, error);
        throw error;
    }
};
