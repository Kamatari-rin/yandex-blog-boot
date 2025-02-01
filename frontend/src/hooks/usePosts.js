import { useState, useEffect } from "react";
import { getPosts, getPostsByTag } from "../services/postService";

const usePosts = (debouncedTag, page) => {
    const [posts, setPosts] = useState([]);
    const [loading, setLoading] = useState(true);
    const [error, setError] = useState(null);

    useEffect(() => {
        const fetchPosts = async () => {
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
        };
        fetchPosts();
    }, [debouncedTag, page]);

    return { posts, loading, error };
};

export default usePosts;
