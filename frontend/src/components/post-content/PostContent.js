import React, { useState } from "react";
import PostView from "./PostView";
import PostEdit from "./PostEdit";
import DeletePostModal from "./DeletePostModal";

function PostContent({ post, onPostUpdate, onPostDelete }) {
    const [isEditing, setIsEditing] = useState(false);
    const [isDeleteModalOpen, setDeleteModalOpen] = useState(false);

    const handleEditClick = () => setIsEditing(true);
    const handleCancelEdit = () => setIsEditing(false);

    const handleSaveEdit = (updatedPost) => {
        setIsEditing(false);
        onPostUpdate(updatedPost);
    };

    const handleDelete = () => {
        onPostDelete(post.id);
    };

    return (
        <div className="post-page">
            {isEditing ? (
                <PostEdit
                    post={post}
                    onSave={handleSaveEdit}
                    onCancel={handleCancelEdit}
                />
            ) : (
                <PostView
                    post={post}
                    onEdit={handleEditClick}
                    onDelete={() => setDeleteModalOpen(true)}
                />
            )}

            <DeletePostModal
                isOpen={isDeleteModalOpen}
                onClose={() => setDeleteModalOpen(false)}
                onConfirm={handleDelete}
            />
        </div>
    );
}

export default PostContent;
