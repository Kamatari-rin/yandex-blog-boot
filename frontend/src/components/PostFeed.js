import React from "react";
import PostPreview from "./PostPreview"; // Используем компонент PostPreview

const PostFeed = ({ posts, likedPosts, updateLikeState, refetchPosts }) => {
    return (
        <div className="post-list">
            {posts.map((post) => (
                <PostPreview
                    key={post.id}
                    post={post}
                    likedPosts={likedPosts}
                    updateLikeState={updateLikeState}
                    refetchPosts={refetchPosts} // Передаем refetchPosts в PostPreview
                />
            ))}
        </div>
    );
};

export default PostFeed;
