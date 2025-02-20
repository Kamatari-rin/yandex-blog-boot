import { useState, useEffect, useCallback } from "react";
import { getPosts, getPostsByTag } from "../api/postsAPI";
import { getPostLikesStatuses } from "../api/likesAPI";
import { DEFAULT_USER_ID } from "../config/constants";

const DEFAULT_LIMIT = 10;

const usePosts = (debouncedTag, page, limit = DEFAULT_LIMIT) => {
    const [posts, setPosts] = useState([]);
    const [loading, setLoading] = useState(true);
    const [error, setError] = useState(null);
    const [likesStatuses, setLikesStatuses] = useState({});
    const [isLastPage, setIsLastPage] = useState(false);

    const userId = DEFAULT_USER_ID;

    const fetchPosts = useCallback(async () => {
        setLoading(true);
        setError(null);

        try {
            let postsData;
            let fetchedPosts = [];

            if (debouncedTag) {
                postsData = await getPostsByTag(debouncedTag, page, limit);
            } else {
                postsData = await getPosts(page, limit);
            }

            if (postsData.posts) {
                fetchedPosts = postsData.posts;
            } else {
                fetchedPosts = postsData;
            }

            setPosts(fetchedPosts);
            setIsLastPage(fetchedPosts.length < limit);

            if (fetchedPosts.length > 0 && userId) {
                const postIds = fetchedPosts.map((post) => post.id);
                const likesData = await getPostLikesStatuses(postIds, userId);
                setLikesStatuses(
                    likesData.reduce((acc, { targetId, liked }) => {
                        acc[targetId] = liked;
                        return acc;
                    }, {})
                );
            }
        } catch (err) {
            setError(err.message || "Ошибка загрузки постов");
        } finally {
            setLoading(false);
        }
    }, [debouncedTag, page, limit, userId]);

    useEffect(() => {
        fetchPosts();
    }, [fetchPosts]);

    return { posts, loading, error, likesStatuses, refetchPosts: fetchPosts, isLastPage };
};

export default usePosts;
