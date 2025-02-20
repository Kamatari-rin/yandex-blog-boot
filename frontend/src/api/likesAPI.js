import api from "./index";

export const getPostLikesStatuses = async (postIds, userId) => {
    try {
        return await api.get("/api/likes/posts/statuses", {
            params: { postIds, userId },
        });
    } catch (error) {
        console.error("Ошибка при получении статуса лайков для постов:", error);
        throw error;
    }
};

export const getCommentLikesStatuses = async (commentIds, userId) => {
    try {
        return await api.get("/api/likes/comments/statuses", {
            params: { commentIds, userId },
        });
    } catch (error) {
        console.error("Ошибка при получении статуса лайков для комментариев:", error);
        throw error;
    }
};

export const toggleLike = async ({ targetId, userId, targetType, liked }) => {
    try {
        return await api.post("/api/likes", {
            userId,
            targetId,
            targetType,
            liked,
        });
    } catch (error) {
        console.error(
            `Ошибка при изменении лайка для ${targetType} с id: ${targetId}`,
            error
        );
        throw error;
    }
};
