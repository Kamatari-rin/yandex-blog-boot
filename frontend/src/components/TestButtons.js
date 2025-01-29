import React, { useState } from "react";
import api from "../config/api";

function TestButtons() {
  const [message, setMessage] = useState("Нажмите на кнопку для проверки API");

  const handlePostTest = async () => {
    try {
      const response = await api.get("/api/post/test"); // Базовый URL из api.js автоматически добавится
      setMessage(response.data);
    } catch (error) {
      setMessage("Ошибка при обращении к PostController");
    }
  };

  const handleUserTest = async () => {
    try {
      const response = await api.get("/user/test");
      setMessage(response.data);
    } catch (error) {
      setMessage("Ошибка при обращении к UserController");
    }
  };

  return (
    <div>
      <h2>{message}</h2>
      <button onClick={handlePostTest}>Проверить PostController</button>
      <button onClick={handleUserTest}>Проверить UserController</button>
    </div>
  );
}

export default TestButtons;
