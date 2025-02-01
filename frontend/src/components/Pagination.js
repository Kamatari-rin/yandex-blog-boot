const Pagination = ({ page, setPage }) => (
    <div className="pagination">
        <button onClick={() => setPage(page - 1)} disabled={page === 1} className="button">
            Назад
        </button>
        <button onClick={() => setPage(page + 1)} className="button">
            Вперед
        </button>
    </div>
);

export default Pagination;
