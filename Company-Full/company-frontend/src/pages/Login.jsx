// src/pages/Login.jsx
import { useState } from "react";
import { login } from "../services/authService";
import { Link, useNavigate } from "react-router-dom";
import { LockClosedIcon, UserIcon } from "@heroicons/react/24/solid";

function Login() {
  const [email, setEmail] = useState("");
  const [password, setPassword] = useState("");
  const [message, setMessage] = useState("");
  const navigate = useNavigate();

  const handleSubmit = async (e) => {
    e.preventDefault();
    try {
      const data = await login({ email, password });
      localStorage.setItem("username", data.username || "User");
      if (data.token) localStorage.setItem("token", data.token);
      navigate("/dashboard");
    } catch {
      setMessage("Login failed. Please check your credentials.");
    }
  };

  return (
    <div className="h-screen w-screen flex items-center justify-center bg-gradient-to-br from-blue-500 to-purple-600">
      <div className="backdrop-blur-md bg-white/10 p-8 rounded-2xl shadow-2xl w-full max-w-md border border-white/20">
        <h2 className="text-3xl font-bold text-center text-white mb-6">
          Welcome Back
        </h2>
        <form onSubmit={handleSubmit} className="flex flex-col gap-4">
          <div className="relative">
            <UserIcon className="w-5 h-5 text-gray-300 absolute top-3 left-3" />
            <input
              type="email"
              placeholder="Email"
              value={email}
              onChange={(e) => setEmail(e.target.value)}
              className="pl-10 pr-3 py-3 rounded-lg w-full bg-white/20 text-white placeholder-gray-300 focus:outline-none focus:ring-2 focus:ring-blue-300"
              required
            />
          </div>
          <div className="relative">
            <LockClosedIcon className="w-5 h-5 text-gray-300 absolute top-3 left-3" />
            <input
              type="password"
              placeholder="Password"
              value={password}
              onChange={(e) => setPassword(e.target.value)}
              className="pl-10 pr-3 py-3 rounded-lg w-full bg-white/20 text-white placeholder-gray-300 focus:outline-none focus:ring-2 focus:ring-purple-300"
              required
            />
          </div>
          <button
            type="submit"
            className="bg-gradient-to-r from-blue-400 to-purple-500 text-white py-3 rounded-lg font-semibold hover:scale-[1.02] transition transform duration-200"
          >
            Sign In
          </button>
        </form>
        {message && (
          <p className="mt-4 text-center text-red-300 font-medium">{message}</p>
        )}
        <p className="mt-6 text-center text-sm text-gray-200">
          Don’t have an account?{" "}
          <Link to="/register" className="text-blue-200 hover:underline">
            Sign Up
          </Link>
        </p>
      </div>
    </div>
  );
}

export default Login;
