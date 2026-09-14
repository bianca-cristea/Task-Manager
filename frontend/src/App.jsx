import { BrowserRouter, Routes, Route, Navigate } from "react-router-dom";
import LoginPage from "./pages/LoginPage";
import RegisterPage from "./pages/RegisterPage";
import ProjectsPage from "./pages/ProjectsPage";
import TasksPage from "./pages/TasksPage";
import ProtectedRoute from "./components/ProtectedRoute";

function App() {
  return (
    <BrowserRouter>
      <Routes>
        <Route path="/login" element={<LoginPage />} />
        <Route path="/register" element={<RegisterPage />} />

        <Route
          path="/projects"
          element={
            <ProtectedRoute>
              <ProjectsPage />
            </ProtectedRoute>
          }
        />
        <Route
          path="/projects/:projectId/tasks"
          element={
            <ProtectedRoute>
              <TasksPage />
            </ProtectedRoute>
          }
        />

        <Route path="*" element={<Navigate to="/login" replace />} />
        <Route path="/" element={<Navigate to="/login" replace />} />
      </Routes>
    </BrowserRouter>
  );
}

export default App;
