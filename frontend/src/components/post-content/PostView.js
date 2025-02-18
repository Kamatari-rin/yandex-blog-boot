// PostView.js
import React from "react";
import { useNavigate } from "react-router-dom";
import PostHeader from "./PostHeader";
import PostTags from "./PostTags";
import PostStats from "./PostStats";

function PostView({ post, onEdit, onDelete }) {
    const navigate = useNavigate();

    const handlePostClick = () => {
        navigate(`/post/${post.id}`);
    };

    return (
        <>
            <img
                src={post.imageUrl || "/assets/placeholder.jpg"}
                alt={post.title}
                className="post-page-image"
                onClick={handlePostClick}
                style={{ cursor: post.id ? "pointer" : "default" }}
            />
            <PostHeader
                title={post.title}
                createdAt={post.createdAt}
                onEdit={onEdit}
                onDelete={onDelete}
            />
            <p className="post-page-content" style={{ whiteSpace: "pre-wrap" }}>
                {post.content}
            </p>
            <PostTags tags={post.tags} />
            <PostStats
                postId={post.id}
                likesCount={post.likesCount}
                commentsCount={post.commentsCount}
            />
        </>
    );
}

export default PostView;
