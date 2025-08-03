import { useState } from "react";
import { register } from "../services/authService";
import { Link } from "react-router-dom";
import { UserIcon, LockClosedIcon, EnvelopeIcon } from "@heroicons/react/24/solid";

function Register() {
  const [formData, setFormData] = useState({
    email: "",
    password: "",
    name: "",
    surname: "",
    tc: "",
    birthday: "",
    gender: "",
    university: "",
    department: "",
    gradutaionYear: "",
    city: "",
    jobStartDate: "",
    title: "",
    skills: ""
  });

  const [message, setMessage] = useState("");

  const handleChange = (e) => {
    const { name, value } = e.target;
    setFormData((prev) => ({ ...prev, [name]: value }));
  };

  const handleSubmit = async (e) => {
    e.preventDefault();

    const payload = {
      ...formData,
      gradutaionYear: Number(formData.gradutaionYear),
      skills: formData.skills ? formData.skills.split(",").map((s) => s.trim()) : [],
      birthday: new Date(formData.birthday).toISOString(),
      jobStartDate: new Date(formData.jobStartDate).toISOString()
    };

    try {
      await register(payload);
      setMessage("Account created successfully! You can now log in.");
    } catch {
      setMessage("Registration failed. Please try again.");
    }
  };

  return (
    <div className="h-screen w-screen flex items-center justify-center bg-gradient-to-br from-gray-900 via-emerald-900 to-green-900 overflow-auto">
      <div className="backdrop-blur-md bg-black/30 p-8 rounded-2xl shadow-2xl w-full max-w-4xl border border-white/10">
        <h2 className="text-3xl font-bold text-center text-white mb-6">
          Create an Account
        </h2>
        <form onSubmit={handleSubmit} className="grid grid-cols-2 gap-4">
          <div className="relative col-span-1">
            <UserIcon className="w-5 h-5 text-gray-400 absolute top-3 left-3" />
            <input name="name" placeholder="First Name" value={formData.name} onChange={handleChange} className="pl-10 pr-3 py-3 rounded-lg bg-white/10 text-white placeholder-gray-400 focus:outline-none focus:ring-2 focus:ring-green-500" required />
          </div>
          <input name="surname" placeholder="Last Name" value={formData.surname} onChange={handleChange} className="p-3 rounded-lg bg-white/10 text-white placeholder-gray-400 focus:ring-2 focus:ring-green-500" required />
          <div className="relative col-span-1">
            <EnvelopeIcon className="w-5 h-5 text-gray-400 absolute top-3 left-3" />
            <input name="email" type="email" placeholder="Email" value={formData.email} onChange={handleChange} className="pl-10 pr-3 py-3 rounded-lg bg-white/10 text-white placeholder-gray-400 focus:ring-2 focus:ring-green-500" required />
          </div>
          <div className="relative col-span-1">
            <LockClosedIcon className="w-5 h-5 text-gray-400 absolute top-3 left-3" />
            <input name="password" type="password" placeholder="Password" value={formData.password} onChange={handleChange} className="pl-10 pr-3 py-3 rounded-lg bg-white/10 text-white placeholder-gray-400 focus:ring-2 focus:ring-green-500" required />
          </div>

          <input name="tc" placeholder="TC" value={formData.tc} onChange={handleChange} className="p-3 rounded-lg bg-white/10 text-white placeholder-gray-400" />
          <input name="birthday" type="date" value={formData.birthday} onChange={handleChange} className="p-3 rounded-lg bg-white/10 text-white placeholder-gray-400" />
          <input name="gender" placeholder="Gender" value={formData.gender} onChange={handleChange} className="p-3 rounded-lg bg-white/10 text-white placeholder-gray-400" />
          <input name="university" placeholder="University" value={formData.university} onChange={handleChange} className="p-3 rounded-lg bg-white/10 text-white placeholder-gray-400" />
          <input name="department" placeholder="Department" value={formData.department} onChange={handleChange} className="p-3 rounded-lg bg-white/10 text-white placeholder-gray-400" />
          <input name="gradutaionYear" type="number" placeholder="Graduation Year" value={formData.gradutaionYear} onChange={handleChange} className="p-3 rounded-lg bg-white/10 text-white placeholder-gray-400" />
          <input name="city" placeholder="City" value={formData.city} onChange={handleChange} className="p-3 rounded-lg bg-white/10 text-white placeholder-gray-400" />
          <input name="jobStartDate" type="date" value={formData.jobStartDate} onChange={handleChange} className="p-3 rounded-lg bg-white/10 text-white placeholder-gray-400" />
          <input name="title" placeholder="Title" value={formData.title} onChange={handleChange} className="p-3 rounded-lg bg-white/10 text-white placeholder-gray-400" />
          <input name="skills" placeholder="Skills (comma separated)" value={formData.skills} onChange={handleChange} className="p-3 rounded-lg bg-white/10 text-white placeholder-gray-400 col-span-2" />

          <button type="submit" className="col-span-2 bg-gradient-to-r from-green-600 to-emerald-700 text-white py-3 rounded-lg font-semibold hover:scale-[1.02] transition">
            Register
          </button>
        </form>
        {message && <p className="mt-4 text-center text-gray-300 font-medium">{message}</p>}
        <p className="mt-6 text-center text-sm text-gray-400">
          Already have an account?{" "}
          <Link to="/login" className="text-green-300 hover:underline">
            Sign In
          </Link>
        </p>
      </div>
    </div>
  );
}

export default Register;
