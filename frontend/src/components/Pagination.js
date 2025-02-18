import React from "react";

const Pagination = ({ page, onPageChange, isNextDisabled, className = "" }) => (
    <nav className={`pagination ${className}`} aria-label="Пагинация">
        <button
            onClick={() => onPageChange(page - 1)}
            disabled={page === 1}
            className="button"
            aria-label="Предыдущая страница"
        >
            Назад
        </button>
        <span className="page-number">{page}</span>
        <button
            onClick={() => onPageChange(page + 1)}
            disabled={isNextDisabled}
            className="button"
            aria-label="Следующая страница"
        >
            Вперед
        </button>
    </nav>
);

export default Pagination;

