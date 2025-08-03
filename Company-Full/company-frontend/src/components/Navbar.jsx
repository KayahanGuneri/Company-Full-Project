import { Link, useNavigate } from "react-router-dom";
import { UserCircleIcon, HomeIcon, UsersIcon } from "@heroicons/react/24/solid";

function Navbar() {
  const username = localStorage.getItem("username") || "User";
  const navigate = useNavigate();

  const handleLogout = () => {
    localStorage.removeItem("token");
    localStorage.removeItem("username");
    navigate("/login");
  };

  return (
    <nav className="bg-gradient-to-r from-gray-800 via-gray-900 to-black text-white px-6 py-4 flex justify-between items-center shadow-lg">
      {/* Logo */}
      <div className="flex items-center gap-2 text-xl font-bold">
        <HomeIcon className="w-6 h-6 text-blue-400" />
        Company Portal
      </div>

      {/* Links */}
      <div className="flex gap-6">
        <Link to="/dashboard" className="flex items-center gap-1 hover:text-blue-300 transition">
          <HomeIcon className="w-5 h-5" /> Dashboard
        </Link>
        <Link to="/employees" className="flex items-center gap-1 hover:text-blue-300 transition">
          <UsersIcon className="w-5 h-5" /> Employees
        </Link>
        <Link to="/profile" className="hover:text-blue-300 transition">Profile</Link>
      </div>

      {/* Right */}
      <div className="flex items-center gap-4">
        <span className="flex items-center gap-1">
          <UserCircleIcon className="w-6 h-6" /> {username}
        </span>
        <button
          onClick={handleLogout}
          className="bg-gradient-to-r from-red-500 to-pink-500 hover:scale-[1.02] transition px-4 py-2 rounded-lg"
        >
          Logout
        </button>
      </div>
    </nav>
  );
}

export default Navbar;
