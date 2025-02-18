import axios from "axios";
import qs from "qs";

const api = axios.create({
    baseURL: process.env.REACT_APP_API_BASE_URL || "",
    timeout: 5000,
    paramsSerializer: (params) =>
        qs.stringify(params, { arrayFormat: "repeat" }),
});

api.interceptors.request.use(
    (config) => {
        console.info(
            `Запрос отправлен: ${config.method.toUpperCase()} ${config.url}`,
            config
        );
        return config;
    },
    (error) => {
        console.error("Ошибка перед отправкой запроса:", error);
        return Promise.reject(error);
    }
);

api.interceptors.response.use(
    (response) => {
        console.info(
            `Ответ получен: ${response.status} ${response.config.url}`,
            response
        );
        return response.data;
    },
    (error) => {
        if (error.response) {
            console.error(
                `Ошибка ответа: ${error.response.status} ${error.response.config.url}`,
                error.response
            );
        } else if (error.request) {
            console.error("Ошибка запроса: сервер не ответил", error.request);
        } else {
            console.error("Ошибка:", error.message);
        }
        return Promise.reject(error);
    }
);

export default api;
