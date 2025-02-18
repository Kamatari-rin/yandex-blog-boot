import api from "./index";

export const getUserById = async (userId) => {
    try {
        return await api.get(`/api/users/${userId}`);
    } catch (error) {
        console.error(`Ошибка при получении пользователя с id: ${userId}`, error);
        throw error;
    }
};

export const getAllUsers = async (limit = 10, offset = 0) => {
    try {
        return await api.get("/api/users", { params: { limit, offset } });
    } catch (error) {
        console.error("Ошибка при получении списка пользователей", error);
        throw error;
    }
};

export const createUser = async (userData) => {
    try {
        return await api.post("/api/users", userData);
    } catch (error) {
        console.error("Ошибка при создании пользователя:", error);
        throw error;
    }
};

export const updateUser = async (userId, userData) => {
    try {
        return await api.put(`/api/users/${userId}`, userData);
    } catch (error) {
        console.error(`Ошибка при обновлении пользователя с id: ${userId}`, error);
        throw error;
    }
};

export const deleteUser = async (userId) => {
    try {
        return await api.delete(`/api/users/${userId}`);
    } catch (error) {
        console.error(`Ошибка при удалении пользователя с id: ${userId}`, error);
        throw error;
    }
};
