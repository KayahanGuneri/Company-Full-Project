import { useEffect, useState } from "react";
import { useNavigate } from "react-router-dom";
import { UsersIcon, ClipboardDocumentListIcon, ShieldCheckIcon, UserPlusIcon } from "@heroicons/react/24/solid";

function Dashboard() {
  const [username, setUsername] = useState("");
  const [role, setRole] = useState("");
  const navigate = useNavigate();

  useEffect(() => {
    const storedUser = localStorage.getItem("username") || "User";
    const storedRole = localStorage.getItem("role") || "USER";
    setUsername(storedUser);
    setRole(storedRole);
  }, []);

  const cards = [
    {
      title: "Employees",
      icon: <UsersIcon className="w-10 h-10 text-blue-300" />,
      bg: "from-blue-500 to-blue-700",
      click: () => navigate("/employees")
    },
    {
      title: "Projects",
      icon: <ClipboardDocumentListIcon className="w-10 h-10 text-green-300" />,
      bg: "from-green-500 to-green-700",
      click: () => navigate("/projects")
    }
  ];

  if (role === "ADMIN") {
    cards.push(
      {
        title: "Add Employee",
        icon: <UserPlusIcon className="w-10 h-10 text-pink-300" />,
        bg: "from-pink-500 to-pink-700",
        click: () => navigate("/employees/new")
      },
      {
        title: "Admin Panel",
        icon: <ShieldCheckIcon className="w-10 h-10 text-yellow-300" />,
        bg: "from-yellow-500 to-yellow-700",
        click: () => navigate("/admin")
      }
    );
  }

  return (
    <div className="flex flex-col items-center justify-center min-h-screen bg-gradient-to-br from-purple-500 to-pink-600 text-white p-6">
      <h1 className="text-4xl font-bold mb-2">Welcome, {username}!</h1>
      <p className="text-lg mb-10 opacity-90">
        This is your dashboard. Choose an option to continue.
      </p>

      <div className="grid grid-cols-1 sm:grid-cols-2 lg:grid-cols-3 gap-8 w-full max-w-5xl">
        {cards.map((card, idx) => (
          <div
            key={idx}
            onClick={card.click}
            className={`cursor-pointer p-8 rounded-2xl shadow-lg bg-gradient-to-br ${card.bg} hover:scale-105 transition transform`}
          >
            <div className="flex items-center gap-4 mb-4">{card.icon}</div>
            <h3 className="text-2xl font-semibold">{card.title}</h3>
          </div>
        ))}
      </div>
    </div>
  );
}

export default Dashboard;
