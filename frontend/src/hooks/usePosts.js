import { useState, useEffect, useCallback } from "react";
import { getPosts, getPostsByTag } from "../services/postService";

const usePosts = (debouncedTag, page) => {
    const [posts, setPosts] = useState([]);
    const [loading, setLoading] = useState(true);
    const [error, setError] = useState(null);

    // Логика для загрузки постов
    const fetchPosts = useCallback(async () => {
        setLoading(true);
        try {
            let postsData;
            if (debouncedTag) {
                postsData = await getPostsByTag(debouncedTag);
            } else {
                postsData = await getPosts(page, 10);
            }
            setPosts(postsData);
        } catch (err) {
            setError("Не удалось загрузить посты");
        } finally {
            setLoading(false);
        }
    }, [debouncedTag, page]);

    // useEffect для загрузки постов при изменении страницы или фильтра
    useEffect(() => {
        fetchPosts();
    }, [fetchPosts]);

    // Функция для перезагрузки постов
    const refetchPosts = () => {
        fetchPosts(); // Просто вызываем fetchPosts заново
    };

    return { posts, loading, error, refetchPosts }; // Возвращаем refetchPosts
};

export default usePosts;
