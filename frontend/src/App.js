import logo from './logo.svg';
import React from "react";
import { BrowserRouter as Router, Route, Routes } from "react-router-dom";
import PostListPage from "./pages/PostListPage";
import PostPage from "./pages/PostPage";

function App() {
    return (
        <Router>
            <Routes>
                <Route path="/" element={<PostListPage />} />
                <Route path="/post/:postId" element={<PostPage />} />
            </Routes>
        </Router>
    );
}

export default App;
