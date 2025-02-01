import React from "react";
import PostPreview from "./PostPreview";

function PostFeed({ posts, likedPosts, updateLikeState }) {
    return (
        <div className="post-list">
            {posts.map((post) => (
                <PostPreview
                    key={post.id}
                    post={post}
                    liked={likedPosts[post.id]?.liked || false} // Передаем состояние лайка
                    likeId={likedPosts[post.id]?.likeId || null} // Передаем ID лайка
                    updateLikeState={updateLikeState} // Функция для обновления состояния лайков
                />
            ))}
        </div>
    );
}

export default PostFeed;
