import { useState } from "react";
import { addEmployee } from "../services/employeeService";
import { useNavigate } from "react-router-dom";
import { XMarkIcon, UserPlusIcon } from "@heroicons/react/24/solid";

function EmployeeForm() {
  const [name, setName] = useState("");
  const [surname, setSurname] = useState("");
  const [email, setEmail] = useState("");
  const [error, setError] = useState("");
  const navigate = useNavigate();

  const handleSubmit = async (e) => {
    e.preventDefault();
    try {
      const newEmployee = {
        name,
        surname,
        email,
        is_deleted: false,
      };
      await addEmployee(newEmployee);
      navigate("/employees");
    } catch (err) {
      setError("Failed to save employee. Check your ADMIN role or inputs.");
    }
  };

  return (
    <div className="h-screen w-screen flex items-center justify-center bg-gradient-to-br from-gray-900 via-gray-800 to-black">
      <div className="bg-white/10 backdrop-blur-lg rounded-2xl shadow-2xl border border-white/20 p-8 w-full max-w-md animate-scaleIn">
        
        {/* Header */}
        <div className="flex justify-between items-center mb-6">
          <h2 className="flex items-center gap-2 text-2xl font-bold text-white">
            <UserPlusIcon className="w-6 h-6 text-green-300" />
            Add Employee
          </h2>
          <button onClick={() => navigate(-1)} className="text-white hover:text-red-300">
            <XMarkIcon className="w-6 h-6" />
          </button>
        </div>

        {error && <p className="text-red-300 text-center mb-4">{error}</p>}

        {/* Form */}
        <form onSubmit={handleSubmit} className="flex flex-col gap-4">
          <input
            type="text"
            placeholder="First Name"
            value={name}
            onChange={(e) => setName(e.target.value)}
            className="border border-white/20 bg-white/10 text-white rounded-lg p-3 placeholder-gray-300 focus:outline-none focus:ring-2 focus:ring-green-400"
            required
          />
          <input
            type="text"
            placeholder="Last Name"
            value={surname}
            onChange={(e) => setSurname(e.target.value)}
            className="border border-white/20 bg-white/10 text-white rounded-lg p-3 placeholder-gray-300 focus:outline-none focus:ring-2 focus:ring-green-400"
            required
          />
          <input
            type="email"
            placeholder="Email"
            value={email}
            onChange={(e) => setEmail(e.target.value)}
            className="border border-white/20 bg-white/10 text-white rounded-lg p-3 placeholder-gray-300 focus:outline-none focus:ring-2 focus:ring-green-400"
            required
          />

          {/* Buttons */}
          <div className="flex justify-end gap-4 mt-4">
            <button
              type="button"
              onClick={() => navigate(-1)}
              className="bg-gray-500 px-4 py-2 rounded-lg text-white hover:bg-gray-600"
            >
              Cancel
            </button>
            <button
              type="submit"
              className="bg-gradient-to-r from-green-500 to-green-700 px-4 py-2 rounded-lg text-white font-semibold hover:scale-105 transition"
            >
              Save Employee
            </button>
          </div>
        </form>
      </div>
    </div>
  );
}

export default EmployeeForm;
