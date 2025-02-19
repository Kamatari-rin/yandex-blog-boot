import React from "react";
import { useNavigate } from "react-router-dom";
import LikeButton from "../LikeButton";
import CommentCounter from "../CommentCounter";
import PropTypes from "prop-types";

const PostPreview = React.memo(({ post }) => {
    const navigate = useNavigate();

    const handlePostClick = () => {
        if (post.id) {
            navigate(`/post/${post.id}`);
        }
    };

    const handleImageError = (e) => {
        e.target.src = "/assets/placeholder.jpg";
    };

    return (
        <div className="post-card">
            <img
                src={post.imageUrl || "/assets/placeholder.jpg"}
                alt={post.title}
                className="post-image"
                onClick={handlePostClick}
                onError={handleImageError}
                loading="lazy"
                style={{ cursor: post.id ? "pointer" : "default" }}
            />

            <div className="post-content-wrapper">
                <h2
                    className="post-title"
                    onClick={handlePostClick}
                    style={{ cursor: post.id ? "pointer" : "default" }}
                >
                    {post.title}
                </h2>

                {Array.from(post.tags)?.length > 0 && (
                    <div className="post-tags">
                        {Array.from(post.tags).map((tag, index) => (
                            <span key={index} className="tag">#{tag}</span>
                        ))}
                    </div>
                )}

                <p className="post-meta">
                    Автор: <strong>{post.authorName || "Неизвестный автор"}</strong> •{" "}
                    {post.createdAt
                        ? new Intl.DateTimeFormat("ru-RU", {
                            day: "2-digit",
                            month: "long",
                            year: "numeric",
                        }).format(new Date(post.createdAt * 1000))
                        : "Дата неизвестна"}
                </p>

                <p className="post-content">
                    {post.content?.length > 100 ? `${post.content.substring(0, 200)}...` : post.content}
                </p>

                <div className="post-footer">
                    <div className="post-footer-left">
                        <LikeButton postId={post.id} likesCount={post.likesCount} />
                        <CommentCounter postId={post.id} commentsCount={post.commentsCount} />
                    </div>
                    <div className="post-footer-right">
                        {post.id && (
                            <button className="read-more" onClick={handlePostClick}>
                                Читать далее
                            </button>
                        )}
                    </div>
                </div>
            </div>
        </div>
    );
});

PostPreview.propTypes = {
    post: PropTypes.shape({
        id: PropTypes.number.isRequired,
        title: PropTypes.string.isRequired,
        content: PropTypes.string,
        imageUrl: PropTypes.string,
        authorName: PropTypes.string,
        createdAt: PropTypes.number,
        tags: PropTypes.arrayOf(PropTypes.string),
        likesCount: PropTypes.number,
        commentsCount: PropTypes.number,
    }).isRequired,
};

export default PostPreview;