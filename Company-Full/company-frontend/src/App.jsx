import { Routes, Route } from "react-router-dom";
import Login from "./pages/Login";
import Register from "./pages/Register";
import Dashboard from "./pages/Dashboard";
import EmployeeList from "./pages/EmployeeList";
import EmployeeForm from "./pages/EmployeeForm";
import EmployeeEditForm from "./components/EmployeeEditForm";
import ProjectExperienceList from "./pages/ProjectExperienceList";
import ProjectExperienceForm from "./pages/ProjectExperienceForm";
import AdminPanel from './pages/AdminPanel';
import ProtectedRoute from "./components/ProtectedRoute";
import ProtectedLayout from "./components/ProtectedLayout";
import AdminRoute from "./components/AdminRoute";
import Profile from "./pages/Profile"; 


import TaskList from "./pages/TaskList";  // DÜZELTİLDİ (components değil pages)
import TaskForm from "./pages/TaskForm";

function App() {
  return (
    <Routes>
      {/* Public routes */}
      <Route path="/login" element={<Login />} />
      <Route path="/register" element={<Register />} />

      {/* Protected routes */}
      <Route
        path="/dashboard"
        element={
          <ProtectedRoute>
            <ProtectedLayout>
              <Dashboard />
            </ProtectedLayout>
          </ProtectedRoute>
        }
      />

      {/* Employees */}
      <Route
        path="/employees"
        element={
          <ProtectedRoute>
            <ProtectedLayout>
              <EmployeeList />
            </ProtectedLayout>
          </ProtectedRoute>
        }
      />
      <Route
        path="/employees/new"
        element={
          <ProtectedRoute>
            <AdminRoute>
              <ProtectedLayout>
                <EmployeeForm />
              </ProtectedLayout>
            </AdminRoute>
          </ProtectedRoute>
        }
      />
      <Route
        path="/employees/edit/:id"
        element={
          <ProtectedRoute>
            <AdminRoute>
              <ProtectedLayout>
                <EmployeeEditForm />
              </ProtectedLayout>
            </AdminRoute>
          </ProtectedRoute>
        }
      />

      {/* Projects */}
      <Route
        path="/projects"
        element={
          <ProtectedRoute>
            <ProtectedLayout>
              <ProjectExperienceList />
            </ProtectedLayout>
          </ProtectedRoute>
        }
      />
      <Route
        path="/projects/new"
        element={
          <ProtectedRoute>
            <AdminRoute>
              <ProtectedLayout>
                <ProjectExperienceForm />
              </ProtectedLayout>
            </AdminRoute>
          </ProtectedRoute>
        }
      />

      {/* Tasks */}
      <Route
        path="/projects/:projectId/tasks"
        element={
          <ProtectedRoute>
            <ProtectedLayout>
              <TaskList />
            </ProtectedLayout>
          </ProtectedRoute>
        }
      />
      <Route
        path="/projects/:projectId/tasks/new"
        element={
          <ProtectedRoute>
            <AdminRoute>
              <ProtectedLayout>
                <TaskForm />
              </ProtectedLayout>
            </AdminRoute>
          </ProtectedRoute>
        }
      />

      {/* Admin Panel */}
      <Route
        path="/admin"
        element={
          <ProtectedRoute>
            <AdminRoute>
              <ProtectedLayout>
                <AdminPanel/>
              </ProtectedLayout>
            </AdminRoute>
          </ProtectedRoute>
        }></Route>


      <Route
        path="/profile"
        element={
        <ProtectedRoute>
          <ProtectedLayout>
            <Profile />
          </ProtectedLayout>
        </ProtectedRoute>
      }></Route>

      {/* Default */}
      <Route path="*" element={<Login />} />
    </Routes>
  );
}

export default App;
