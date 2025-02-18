import { useState, useEffect, useCallback } from "react";
import { getPosts, getPostsByTag } from "../api/postsAPI"; // Импортируем правильный сервис для постов
import { getPostLikesStatuses } from "../api/likesAPI"; // Импортируем правильный сервис для лайков
import { DEFAULT_USER_ID } from "../config/constants";

const DEFAULT_LIMIT = 10;

const usePosts = (debouncedTag, page) => {
    const [posts, setPosts] = useState([]);
    const [loading, setLoading] = useState(true);
    const [error, setError] = useState(null);
    const [likesStatuses, setLikesStatuses] = useState({});
    const [totalPages, setTotalPages] = useState(1);

    const userId = DEFAULT_USER_ID; // Используем константный userId

    const fetchPosts = useCallback(async () => {
        setLoading(true);
        setError(null);

        try {
            let postsData;
            let fetchedPosts = [];
            let fetchedTotalPages = 1;

            if (debouncedTag) {
                postsData = await getPostsByTag(debouncedTag, page, DEFAULT_LIMIT);
            } else {
                postsData = await getPosts(page, DEFAULT_LIMIT);
            }

            // Если API возвращает объект с постами и totalPages
            if (postsData.posts) {
                fetchedPosts = postsData.posts;
                fetchedTotalPages = postsData.totalPages || 1;
            } else {
                // Если возвращается массив напрямую
                fetchedPosts = postsData;
            }

            setPosts(fetchedPosts);
            setTotalPages(fetchedTotalPages);

            // Запрашиваем лайки, если посты получены и указан userId
            if (fetchedPosts.length > 0 && userId) {
                const postIds = fetchedPosts.map(post => post.id);
                const likesData = await getPostLikesStatuses(postIds, userId); // Используем новый сервис
                setLikesStatuses(likesData.reduce((acc, { targetId, liked }) => {
                    acc[targetId] = liked;
                    return acc;
                }, {})); // Формируем объект с лайками для каждого поста
            }
        } catch (err) {
            setError(err.message || "Ошибка загрузки постов");
        } finally {
            setLoading(false);
        }
    }, [debouncedTag, page, userId]);

    useEffect(() => {
        fetchPosts();
    }, [fetchPosts]);

    return { posts, loading, error, likesStatuses, refetchPosts: fetchPosts, totalPages };
};

export default usePosts;
