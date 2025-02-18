const LoadingError = ({ loading, error }) => {
    if (loading) return <p className="text-center">Загрузка...</p>;
    if (error) return <p className="error">{error}</p>;
    return null;
};

export default LoadingError;
