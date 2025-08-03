import { useEffect, useState } from "react";
import { createTaskForProject } from "../services/taskService";
import { getActiveEmployees } from "../services/employeeService"; // 🔹 aktif çalışanları çekecek
import { XMarkIcon, ClipboardDocumentListIcon } from "@heroicons/react/24/solid";

function TaskAddModal({ projectId, onClose, onSave }) {
  const [formData, setFormData] = useState({ title: "", assignedUserId: "", status: "TODO" });
  const [employees, setEmployees] = useState([]); // 🔹 çalışan listesi
  const [error, setError] = useState("");
  const [loading, setLoading] = useState(false);

  // 🔹 Sayfa açıldığında aktif çalışanları getir
  useEffect(() => {
    getActiveEmployees()
      .then((data) => setEmployees(data))
      .catch((err) => console.error("Error fetching employees:", err));
  }, []);

  const handleChange = (e) => setFormData({ ...formData, [e.target.name]: e.target.value });

  const handleSubmit = async (e) => {
    e.preventDefault();
    setLoading(true);
    try {
      await createTaskForProject(projectId, formData);
      onSave();
      onClose();
    } catch {
      setError("Failed to create task. Please check your input or role.");
    } finally {
      setLoading(false);
    }
  };

  return (
    <div className="fixed inset-0 flex items-center justify-center bg-black/50 backdrop-blur-sm z-50">
      <div className="bg-white/10 backdrop-blur-lg border border-white/20 p-6 rounded-2xl shadow-xl w-full max-w-md animate-scaleIn">
        <div className="flex justify-between items-center mb-4">
          <h2 className="flex items-center gap-2 text-white text-xl font-bold">
            <ClipboardDocumentListIcon className="w-6 h-6 text-blue-300" />
            Add New Task
          </h2>
          <button onClick={onClose} className="text-white hover:text-red-300">
            <XMarkIcon className="w-6 h-6" />
          </button>
        </div>

        {error && <p className="text-red-300 mb-2">{error}</p>}

        <form onSubmit={handleSubmit} className="space-y-4">
          {/* Task Title */}
          <input
            name="title"
            value={formData.title}
            onChange={handleChange}
            placeholder="Task Title"
            className="w-full border border-white/20 bg-white/10 text-white rounded-lg p-2 placeholder-gray-300"
            required
          />

          {/* Assigned User Dropdown */}
          <select
            name="assignedUserId"
            value={formData.assignedUserId}
            onChange={handleChange}
            className="w-full border border-white/20 bg-white/10 text-white rounded-lg p-2 appearance-none cursor-pointer focus:outline-none focus:ring-2 focus:ring-blue-400"
            required
          >
            <option value="">Select Employee</option>
            {employees.map((emp) => (
              <option key={emp.id} value={emp.id} className="bg-gray-800 text-white">
                {emp.name} {emp.surname}
              </option>
            ))}
          </select>

          {/* Status Dropdown */}
          <select
            name="status"
            value={formData.status}
            onChange={handleChange}
            className="w-full border border-white/20 bg-gray-800 text-white rounded-lg p-2 appearance-none cursor-pointer"
          >
            <option value="TODO">TODO</option>
            <option value="IN_PROGRESS">IN PROGRESS</option>
            <option value="DONE">DONE</option>
          </select>

          {/* Buttons */}
          <div className="flex justify-end gap-4">
            <button
              type="button"
              onClick={onClose}
              className="bg-gray-500 px-4 py-2 rounded-lg text-white hover:bg-gray-600"
            >
              Cancel
            </button>
            <button
              type="submit"
              disabled={loading}
              className="bg-gradient-to-r from-blue-500 to-blue-700 px-4 py-2 rounded-lg text-white hover:scale-105 transition"
            >
              {loading ? "Adding..." : "Add Task"}
            </button>
          </div>
        </form>
      </div>
    </div>
  );
}

export default TaskAddModal;
