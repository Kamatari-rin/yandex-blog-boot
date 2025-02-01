import React from "react";
import { useNavigate } from "react-router-dom";
import { toggleLike } from "../services/postService"; // Теперь используем toggleLike

function PostPreview({ post, liked, likeId, updateLikeState }) {
  const navigate = useNavigate();
  const [likes, setLikes] = React.useState(post.likesCount);

  const handleLike = async () => {
    try {
      const { liked: newLiked, likeId: newLikeId } = await toggleLike(post.id, 1, likeId);
      updateLikeState(post.id, newLiked, newLikeId);
      setLikes(newLiked ? likes + 1 : likes - 1);
    } catch (error) {
      console.error("Ошибка при лайке:", error);
    }
  };

  return (
      <div className="post-card">
        <img
            src={post.imageUrl || "/assets/placeholder.jpg"}
            alt={post.title}
            className="post-image"
            onClick={() => navigate(`/post/${post.id}`)}
            style={{ cursor: "pointer" }}
        />

        <div className="post-content-wrapper">
          <h2
              className="post-title"
              onClick={() => navigate(`/post/${post.id}`)}
              style={{ cursor: "pointer" }}
          >
            {post.title}
          </h2>

          {/* Блок тегов */}
          {post.tags && post.tags.length > 0 && (
              <div className="post-tags">
                {post.tags.map((tag, index) => (
                    <span key={index} className="tag">
                      {tag}
                    </span>
                ))}
              </div>
          )}

          <p className="post-meta">
            Автор: <strong>{post.authorName}</strong> •{" "}
            {new Date(post.createdAt * 1000).toLocaleDateString()}
          </p>

          <p className="post-content">
            {post.content.length > 100 ? post.content.substring(0, 100) + "..." : post.content}
          </p>

          <div className="post-footer">
            <div className="post-info">
              <span className="icon" onClick={handleLike} style={{ cursor: "pointer" }}>
                <svg viewBox="0 0 24 24">
                  <path d="M12 21.35l-1.45-1.32C5.4 15.36 2 12.28 2 8.5 2 5.42 4.42 3 7.5 3c1.74 0 3.41.81 4.5 2.09C13.09 3.81 14.76 3 16.5 3 19.58 3 22 5.42 22 8.5c0 3.78-3.4 6.86-8.55 11.54L12 21.35z"></path>
                </svg>
                {likes ?? 0}
              </span>
              <span className="icon">
                <svg viewBox="0 0 24 24">
                  <path d="M21 6h-2V4c0-1.1-.9-2-2-2H7c-1.1 0-2 .9-2 2v2H3c-1.1 0-2 .9-2 2v12c0 1.1.9 2 2 2h18c1.1 0 2-.9 2-2V8c0-1.1-.9-2-2-2zM7 4h10v2H7V4zm14 16H3V8h18v12z"></path>
                </svg>
                {post.commentsCount ?? 0}
              </span>
            </div>

            <button className="read-more" onClick={() => navigate(`/post/${post.id}`)}>
              Читать далее
            </button>
          </div>
        </div>
      </div>
  );
}

export default PostPreview;
