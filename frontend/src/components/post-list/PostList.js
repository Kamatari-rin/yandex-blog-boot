import React from "react";
import PostFeed from "./PostFeed";

const PostList = ({ posts, refetchPosts }) => {
    return (
        <div>
            <PostFeed posts={posts} refetchPosts={refetchPosts} />
        </div>
    );
};

export default PostList;
