import React, { useState } from "react";
import { useLikes } from "../context/LikeContext";
import PropTypes from "prop-types";

const LikeButton = ({ postId, likesCount = 0 }) => {
    const { likedPosts, toggleLike: toggleLikeContext } = useLikes();
    const [isLoading, setIsLoading] = useState(false);
    const [likeCount, setLikeCount] = useState(likesCount);

    const liked = likedPosts[postId]?.liked || false;

    const handleClick = async () => {
        if (isLoading) return;
        setIsLoading(true);

        const currentLiked = liked;
        const newLikedState = !currentLiked;
        setLikeCount((prevCount) => prevCount + (newLikedState ? 1 : -1));

        try {
            await toggleLikeContext(postId, currentLiked, "POST");
        } catch (error) {
            console.error("Ошибка при изменении лайка", error);
            setLikeCount((prevCount) => prevCount - (newLikedState ? 1 : -1));
        } finally {
            setIsLoading(false);
        }
    };


    return (
        <div className="like-button">
            <button
                onClick={handleClick}
                className={`icon-button like-button__icon ${liked ? "liked" : ""}`}
                disabled={isLoading}
                style={{ cursor: isLoading ? "default" : "pointer" }}
            >
                <svg
                    viewBox="0 0 24 24"
                    className={`like-button__svg ${liked ? "liked" : ""}`}
                >
                    <path d="M12 21.35l-1.45-1.32C5.4 15.36 2 12.28 2 8.5
                             2 5.42 4.42 3 7.5 3c1.74 0 3.41.81 4.5 2.09
                             C13.09 3.81 14.76 3 16.5 3 19.58 3 22 5.42 22 8.5
                             c0 3.78-3.4 6.86-8.55 11.54L12 21.35z"></path>
                </svg>
                <span className="like-count">{likeCount}</span>
            </button>
        </div>
    );
};

LikeButton.propTypes = {
    postId: PropTypes.number.isRequired,
    likesCount: PropTypes.number,
};

export default LikeButton;
