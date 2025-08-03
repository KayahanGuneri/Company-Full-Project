import { Navigate } from "react-router-dom";

function AdminRoute({ children }) {
  const token = localStorage.getItem("token");
  const role = localStorage.getItem("role");

  // Check if token exists and role contains ADMIN (safe check for ROLE_ADMIN or ADMIN)
  if (!token || !role?.includes("ADMIN")) {
    return <Navigate to="/dashboard" replace />;
  }

  return children;
}

export default AdminRoute;
