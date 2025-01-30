import api from "../api";

export const getPosts = async (page = 1, limit = 10) => {
    try {
        const response = await api.get(`/api/posts?page=${page}&limit=${limit}`);
        return response.data;
    } catch (error) {
        console.error("Ошибка при получении постов:", error);
        throw error;
    }
};

export const getPostById = async (postId) => {
    try {
        const response = await api.get(`/api/posts/${postId}`);
        return response.data;
    } catch (error) {
        console.error(`Ошибка при получении поста с id: ${postId}`, error);
        throw error;
    }
};

export const createPost = async (postData) => {
    try {
        const response = await api.post("/api/posts", postData);
        return response.data;
    } catch (error) {
        console.error("Ошибка при создании поста:", error);
        throw error;
    }
};

export const updatePost = async (postId, postData) => {
    try {
        const response = await api.put(`/api/posts/${postId}`, postData);
        return response.data;
    } catch (error) {
        console.error(`Ошибка при обновлении поста с id: ${postId}`, error);
        throw error;
    }
};

export const deletePost = async (postId) => {
    try {
        const response = await api.delete(`/api/posts/${postId}`);
        return response.data;
    } catch (error) {
        console.error(`Ошибка при удалении поста с id: ${postId}`, error);
        throw error;
    }
};