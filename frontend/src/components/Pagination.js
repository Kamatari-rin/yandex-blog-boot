import React from "react";
import PropTypes from "prop-types";

const Pagination = ({
                        page,
                        onPageChange,
                        isNextDisabled,
                        limit,
                        onLimitChange,
                        className = ""
                    }) => (
    <nav className={`pagination ${className}`} aria-label="Пагинация">
        <div className="pagination-left">
            <button
                onClick={() => onPageChange(page - 1)}
                disabled={page === 1}
                className="pagination-button"
                aria-label="Предыдущая страница"
            >
                Назад
            </button>
            <span className="page-number">{page}</span>
            <button
                onClick={() => onPageChange(page + 1)}
                disabled={isNextDisabled}
                className="pagination-button"
                aria-label="Следующая страница"
            >
                Вперед
            </button>
        </div>
        <div className="pagination-right">
            <label htmlFor="limit-select" className="limit-label">Постов на страницу:</label>
            <select
                id="limit-select"
                value={limit}
                onChange={(e) => onLimitChange(Number(e.target.value))}
                className="limit-select"
            >
                <option value={10}>10</option>
                <option value={20}>20</option>
                <option value={50}>50</option>
            </select>
        </div>
    </nav>
);

Pagination.propTypes = {
    page: PropTypes.number.isRequired,
    onPageChange: PropTypes.func.isRequired,
    isNextDisabled: PropTypes.bool.isRequired,
    limit: PropTypes.number.isRequired,
    onLimitChange: PropTypes.func.isRequired,
    className: PropTypes.string,
};

export default Pagination;
