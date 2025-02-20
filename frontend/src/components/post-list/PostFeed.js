import React from "react";
import PostPreview from "./PostPreview";

const PostFeed = React.memo(({ posts, refetchPosts }) => {
    if (!posts.length) {
        return <p className="empty-message">Постов нет</p>;
    }

    return (
        <div className="post-list">
            {posts.map((post) => (
                <PostPreview key={post.id} post={post} refetchPosts={refetchPosts} />
            ))}
        </div>
    );
});

export default PostFeed;
