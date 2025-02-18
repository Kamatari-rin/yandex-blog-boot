import React from "react";
import PropTypes from "prop-types";

function PostTags({ tags }) {
    return (
        <div className="post-page-tags">
            {tags.map((tag, index) => (
                <span key={index} className="post-page-tag">#{tag}</span>
            ))}
        </div>
    );
}

PostTags.propTypes = {
    tags: PropTypes.arrayOf(PropTypes.string).isRequired,
};

export default PostTags;
