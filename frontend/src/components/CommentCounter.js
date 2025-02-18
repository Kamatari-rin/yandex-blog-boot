import React from "react";
import PropTypes from "prop-types";
import { useNavigate } from "react-router-dom";

const CommentCounter = ({ postId, commentsCount }) => {
    const navigate = useNavigate();

    const handleClick = () => {
        navigate(`/post/${postId}#comments`);
    };

    return (
        <button className="comment-button" onClick={handleClick}>
            <svg className="comment-button__svg" viewBox="0 0 24 24">
                <path d="M21 6h-2V4c0-1.1-.9-2-2-2H7c-1.1 0-2 .9-2 2v2H3c-1.1 0-2 .9-2 2v12c0 1.1.9 2 2 2h18c1.1 0 2-.9 2-2V8c0-1.1-.9-2-2-2z"></path>
            </svg>
            <span className="comment-count">{commentsCount}</span>
        </button>
    );
};

CommentCounter.propTypes = {
    postId: PropTypes.number.isRequired,
    commentsCount: PropTypes.number.isRequired,
};

export default CommentCounter;
