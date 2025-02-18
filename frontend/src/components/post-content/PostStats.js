import React from "react";
import PropTypes from "prop-types";
import LikeButton from "../LikeButton";
import CommentCounter from "../CommentCounter";

function PostStats({ postId, likesCount, commentsCount }) {
    return (
        <div className="post-page-info">
            <LikeButton postId={postId} likesCount={likesCount} />
            <CommentCounter postId={postId} commentsCount={commentsCount} />
        </div>
    );
}

PostStats.propTypes = {
    postId: PropTypes.number.isRequired,
    likesCount: PropTypes.number.isRequired,
    commentsCount: PropTypes.number.isRequired,
};

export default PostStats;
