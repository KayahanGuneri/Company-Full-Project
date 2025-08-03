import { useEffect, useState } from "react";
import { useParams, useNavigate } from "react-router-dom";
import { getEmployeeById, updateEmployee } from "../services/employeeService";
import { XMarkIcon, PencilSquareIcon } from "@heroicons/react/24/solid";

function EmployeeEditForm() {
  const { id } = useParams();
  const navigate = useNavigate();

  const [name, setName] = useState("");
  const [surname, setSurname] = useState("");
  const [email, setEmail] = useState("");
  const [error, setError] = useState("");

  useEffect(() => {
    const fetchEmployee = async () => {
      try {
        const data = await getEmployeeById(id);
        setName(data.name);
        setSurname(data.surname);
        setEmail(data.email);
      } catch {
        setError("Failed to load employee data.");
      }
    };
    fetchEmployee();
  }, [id]);

  const handleSubmit = async (e) => {
    e.preventDefault();
    try {
      await updateEmployee(id, { name, surname, email });
      navigate("/employees");
    } catch {
      setError("Failed to update employee. Check your ADMIN role.");
    }
  };

  return (
    <div className="min-h-screen w-screen flex items-center justify-center p-8 bg-gradient-to-br from-gray-900 to-gray-800 text-white">
      <div className="bg-white/10 backdrop-blur-lg rounded-xl border border-white/20 p-8 w-full max-w-md shadow-lg animate-scaleIn">
        
        {/* Başlık */}
        <div className="flex justify-between items-center mb-6">
          <h2 className="flex items-center gap-2 text-2xl font-bold">
            <PencilSquareIcon className="w-6 h-6 text-yellow-300" />
            Edit Employee
          </h2>
          <button onClick={() => navigate(-1)} className="hover:text-red-300">
            <XMarkIcon className="w-6 h-6" />
          </button>
        </div>

        {/* Hata Mesajı */}
        {error && <p className="text-red-300 text-center mb-4">{error}</p>}

        {/* Form */}
        <form onSubmit={handleSubmit} className="flex flex-col gap-4">
          <input
            type="text"
            placeholder="First Name"
            value={name}
            onChange={(e) => setName(e.target.value)}
            className="border border-white/20 bg-white/10 text-white rounded-lg p-3 placeholder-gray-300 focus:outline-none focus:ring-2 focus:ring-yellow-400"
            required
          />
          <input
            type="text"
            placeholder="Last Name"
            value={surname}
            onChange={(e) => setSurname(e.target.value)}
            className="border border-white/20 bg-white/10 text-white rounded-lg p-3 placeholder-gray-300 focus:outline-none focus:ring-2 focus:ring-yellow-400"
            required
          />
          <input
            type="email"
            placeholder="Email"
            value={email}
            onChange={(e) => setEmail(e.target.value)}
            className="border border-white/20 bg-white/10 text-white rounded-lg p-3 placeholder-gray-300 focus:outline-none focus:ring-2 focus:ring-yellow-400"
            required
          />

          {/* Butonlar */}
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
              className="bg-gradient-to-r from-yellow-500 to-yellow-700 px-4 py-2 rounded-lg text-white font-semibold hover:scale-105 transition"
            >
              Save Changes
            </button>
          </div>
        </form>
      </div>
    </div>
  );
}

export default EmployeeEditForm;
