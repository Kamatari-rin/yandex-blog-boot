import logo from './logo.svg';
import React from "react";
import { BrowserRouter as Router, Route, Routes } from "react-router-dom";
import PostListPage from "./pages/PostListPage";
import PostPage from "./pages/PostPage";
import { LikeProvider } from "./context/LikeContext"; // Импортируем LikeProvider

function App() {
    return (
        <LikeProvider> {/* Оборачиваем все приложение в провайдер */}
            <Router>
                <Routes>
                    <Route path="/" element={<PostListPage />} />
                    <Route path="/post/:postId" element={<PostPage />} />
                </Routes>
            </Router>
        </LikeProvider>
    );
}

export default App;
