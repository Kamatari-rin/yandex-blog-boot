import api from "./index";

const calculateOffset = (page, limit) => (page - 1) * limit;

export const getPosts = async (page = 1, limit = 10) => {
    const offset = calculateOffset(page, limit);
    try {
        return await api.get("/api/posts", { params: { offset, limit } });
    } catch (error) {
        console.error("Ошибка при получении постов:", error);
        throw error;
    }
};

export const getPostById = async (postId) => {
    try {
        return await api.get(`/api/posts/${postId}`);
    } catch (error) {
        console.error(`Ошибка при получении поста с id: ${postId}`, error);
        throw error;
    }
};

export const createPost = async (postData) => {
    try {
        return await api.post("/api/posts", postData);
    } catch (error) {
        console.error("Ошибка при создании поста:", error);
        throw error;
    }
};

export const updatePost = async (postId, postData) => {
    try {
        return await api.put(`/api/posts/${postId}`, postData);
    } catch (error) {
        console.error(`Ошибка при обновлении поста с id: ${postId}`, error);
        throw error;
    }
};

export const deletePost = async (postId) => {
    try {
        return await api.delete(`/api/posts/${postId}`);
    } catch (error) {
        console.error(`Ошибка при удалении поста с id: ${postId}`, error);
        throw error;
    }
};

export const getPostsByTag = async (tagName, page = 1, limit = 10) => {
    const offset = calculateOffset(page, limit);
    try {
        return await api.get("/api/posts", { params: { tag: tagName, offset, limit } });
    } catch (error) {
        console.error(`Ошибка при получении постов по тегу: ${tagName}`, error);
        throw error;
    }
};
