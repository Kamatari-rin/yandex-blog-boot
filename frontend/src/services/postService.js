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

export const toggleLike = async (postId, userId, currentLikeId) => {
  try {
    if (currentLikeId) {
      // ✅ Удаляем лайк по `id`
      await api.delete(`http://localhost:8081/api/likes/${currentLikeId}`);
      return { liked: false, likeId: null };
    } else {
      // ✅ Ставим лайк
      const response = await api.post("http://localhost:8081/api/likes", {
        userId: userId,
        targetId: postId,
        targetType: "POST",
        liked: true // ✅ Здесь заменили `isLiked` → `liked`
      });

      if (response.data && response.data.id) {
        return { liked: true, likeId: response.data.id }; // ✅ Теперь `likeId` сохраняется
      } else {
        console.error("Ошибка: сервер не вернул ID лайка", response.data);
        return { liked: true, likeId: null };
      }
    }
  } catch (error) {
    console.error(`Ошибка при изменении лайка поста с id: ${postId}`, error);
    throw error;
  }
};

export const getPostsByTag = async (tagName) => {
  try {
    const response = await api.get(`/api/posts?tag=${tagName}`);
    return response.data;
  } catch (error) {
    console.error(`Ошибка при получении постов по тегу: ${tagName}`, error);
    throw error;
  }
};









