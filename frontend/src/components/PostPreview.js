import React from "react";
import { Link } from "react-router-dom";

function PostPreview({ post }) {
    return (
        <div>
            <h2>{post.title}</h2>
            <img src={post.imageUrl} alt={post.title} width="100" height="100" />
            <p>{post.content.substring(0, 100)}...</p>
            <div>
                <span>{post.likesCount} лайков</span>
                <span>{post.commentsCount} комментариев</span>
            </div>
            <Link to={`/post/${post.id}`}>Читать полностью</Link>
        </div>
    );
}

export default PostPreview;