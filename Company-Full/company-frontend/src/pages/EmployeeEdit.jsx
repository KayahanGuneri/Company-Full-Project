import { useEffect, useState } from "react";
import { useParams, useNavigate } from "react-router-dom";
import { getEmloyeeById, updateEmploye } from "../services/employeeService";
import { PencilSquareIcon } from "@heroicons/react/24/solid";

function EmployeeEdit() {
  const { id } = useParams();
  const navigate = useNavigate();

  const [name, setName] = useState("");
  const [surname, setSurname] = useState("");
  const [email, setEmail] = useState("");
  const [error, setError] = useState("");

  useEffect(() => {
    const fetchEmployee = async () => {
      try {
        const data = await getEmloyeeById(id);
        setName(data.name);
        setSurname(data.surname);
        setEmail(data.email);
      } catch (err) {
        setError("Failed to load employee data.");
      }
    };
    fetchEmployee();
  }, [id]);

  const handleSubmit = async (e) => {
    e.preventDefault();
    try {
      await updateEmploye(id, { name, surname, email });
      navigate("/employees");
    } catch (err) {
      setError("Failed to update employee. Check your ADMIN role.");
    }
  };

  return (
    <div className="min-h-screen bg-gray-50 flex items-center justify-center px-4">
      <div className="bg-white rounded-lg shadow-md border border-gray-200 w-full max-w-lg p-6">
        
        {/* Başlık */}
        <div className="flex items-center gap-2 mb-6">
          <PencilSquareIcon className="w-6 h-6 text-blue-600" />
          <h2 className="text-xl font-semibold text-gray-800">Edit Employee</h2>
        </div>

        {/* Hata Mesajı */}
        {error && (
          <p className="text-red-500 text-sm text-center mb-4">{error}</p>
        )}

        {/* Form */}
        <form onSubmit={handleSubmit} className="space-y-4">
          <input
            type="text"
            placeholder="First Name"
            value={name}
            onChange={(e) => setName(e.target.value)}
            className="w-full border border-gray-300 rounded-md p-2 focus:ring-1 focus:ring-blue-400 focus:outline-none"
            required
          />
          <input
            type="text"
            placeholder="Last Name"
            value={surname}
            onChange={(e) => setSurname(e.target.value)}
            className="w-full border border-gray-300 rounded-md p-2 focus:ring-1 focus:ring-blue-400 focus:outline-none"
            required
          />
          <input
            type="email"
            placeholder="Email"
            value={email}
            onChange={(e) => setEmail(e.target.value)}
            className="w-full border border-gray-300 rounded-md p-2 focus:ring-1 focus:ring-blue-400 focus:outline-none"
            required
          />

          {/* Butonlar */}
          <div className="flex justify-end gap-3 pt-4">
            <button
              type="button"
              onClick={() => navigate(-1)}
              className="bg-gray-200 hover:bg-gray-300 text-gray-700 px-4 py-2 rounded-md"
            >
              Cancel
            </button>
            <button
              type="submit"
              className="bg-blue-600 hover:bg-blue-700 text-white px-4 py-2 rounded-md"
            >
              Save Changes
            </button>
          </div>
        </form>
      </div>
    </div>
  );
}

export default EmployeeEdit;
