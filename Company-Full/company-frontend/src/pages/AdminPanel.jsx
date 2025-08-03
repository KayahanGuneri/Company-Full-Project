import { useEffect, useState } from "react";
import { getAllUsers, promoteToAdmin, changeUserStatus } from "../services/adminService";
import { FaUserShield, FaToggleOn, FaToggleOff } from "react-icons/fa";

function AdminPanel() {
  const [users, setUsers] = useState([]);
  const currentUserEmail = localStorage.getItem("email");

  // Fetch all users from backend
  const fetchUsers = async () => {
    try {
      const data = await getAllUsers();
      setUsers(data);
    } catch (error) {
      console.error("Failed to fetch users:", error);
      alert("Failed to load users.");
    }
  };

  useEffect(() => {
    fetchUsers();
  }, []);

  // Promote selected user to admin
  const handlePromote = async (userId) => {
    try {
      await promoteToAdmin(userId);
      alert("User has been promoted to ADMIN.");
      fetchUsers();
    } catch (error) {
      console.error("Promote failed:", error);
      alert("Failed to promote user to admin.");
    }
  };

  // Toggle user status between ACTIVE and PASSIVE
  const handleToggleStatus = async (user) => {
    const newStatus = user.status === "ACTIVE" ? "PASSIVE" : "ACTIVE";
    try {
      await changeUserStatus(user.id, newStatus);
      alert(`User status changed to ${newStatus}.`);
      fetchUsers();
    } catch (error) {
      console.error("Status change failed:", error);
      alert("Failed to change user status.");
    }
  };

  return (
    <div className="min-h-screen w-full bg-gradient-to-br from-gray-900 to-gray-800 p-8 text-white">
      <h2 className="text-4xl font-bold mb-8 text-center">⚙️ Admin Panel</h2>

      <div className="overflow-x-auto bg-white/10 backdrop-blur-md rounded-xl shadow-lg border border-white/20">
        <table className="min-w-full text-sm">
          <thead>
            <tr className="bg-white/20 text-left">
              <th className="p-4">👤 Name</th>
              <th className="p-4">📧 Email</th>
              <th className="p-4">🎭 Role</th>
              <th className="p-4">📌 Status</th>
              <th className="p-4">⚡ Actions</th>
            </tr>
          </thead>
          <tbody>
            {Array.isArray(users) && users.map((user) => (
              <tr key={user.id} className="hover:bg-white/10 transition border-b border-white/10">
                <td className="p-4">{(user.name || "—") + " " + (user.surname || "")}</td>
                <td className="p-4">{user.email}</td>
                <td className={`p-4 font-bold ${user.role === "ADMIN" ? "text-yellow-300" : "text-blue-300"}`}>
                  {user.role}
                </td>
                <td className={`p-4 font-bold ${user.status === "ACTIVE" ? "text-green-400" : "text-red-400"}`}>
                  {user.status}
                </td>
                <td className="p-4 flex gap-3">
                  {/* Prevent actions on the current logged-in user */}
                  {user.email !== currentUserEmail && (
                    <>
                      <button
                        onClick={() => handlePromote(user.id)}
                        className="flex items-center gap-1 bg-gradient-to-r from-yellow-500 to-yellow-700 px-3 py-1 rounded-lg text-black hover:scale-105 transition"
                      >
                        <FaUserShield /> Promote to Admin
                      </button>
                      <button
                        onClick={() => handleToggleStatus(user)}
                        className={`flex items-center gap-1 px-3 py-1 rounded-lg hover:scale-105 transition ${
                          user.status === "ACTIVE"
                            ? "bg-gradient-to-r from-red-500 to-red-700"
                            : "bg-gradient-to-r from-green-500 to-green-700"
                        }`}
                      >
                        {user.status === "ACTIVE" ? <FaToggleOff /> : <FaToggleOn />}
                        {user.status === "ACTIVE" ? "Deactivate" : "Activate"}
                      </button>
                    </>
                  )}
                </td>
              </tr>
            ))}
          </tbody>
        </table>
      </div>
    </div>
  );
}

export default AdminPanel;
