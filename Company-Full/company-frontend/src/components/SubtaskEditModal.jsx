import { useState, useEffect } from "react";
import { updateSubtask } from "../services/taskService";
import { getEmployees } from "../services/employeeService";
import { XMarkIcon, PencilSquareIcon } from "@heroicons/react/24/solid";

function SubtaskEditModal({ subtask, onClose, onSave }) {
  const [formData, setFormData] = useState(subtask);
  const [employees, setEmployees] = useState([]);
  const [error, setError] = useState("");

  useEffect(() => {
    getEmployees().then(setEmployees).catch(console.error);
  }, []);

  const handleChange = (e) =>
    setFormData({ ...formData, [e.target.name]: e.target.value });

  const handleSubmit = async (e) => {
    e.preventDefault();
    try {
      await updateSubtask(formData.id, formData);
      onSave();
      onClose();
    } catch (err) {
      setError("Failed to update subtask. Please check your input.");
    }
  };

  return (
    <div className="fixed inset-0 bg-black/60 backdrop-blur-sm flex justify-center items-center z-50">
      <div className="bg-gray-900 border border-white/20 p-6 rounded-2xl shadow-xl w-full max-w-md animate-scaleIn text-white">
        
        {/* Header */}
        <div className="flex justify-between items-center mb-4">
          <h2 className="flex items-center gap-2 text-xl font-bold">
            <PencilSquareIcon className="w-6 h-6 text-yellow-300" />
            Edit Subtask
          </h2>
          <button onClick={onClose} className="hover:text-red-400">
            <XMarkIcon className="w-6 h-6" />
          </button>
        </div>

        {error && <p className="text-red-400 mb-3">{error}</p>}

        {/* Form */}
        <form onSubmit={handleSubmit} className="space-y-4">
          <input
            name="title"
            value={formData.title || ""}
            onChange={handleChange}
            placeholder="Subtask Title"
            className="w-full border border-white/20 bg-gray-800 text-white rounded-lg p-2 placeholder-gray-400"
          />
          <select
            name="assignedUserId"
            value={formData.assignedUserId || ""}
            onChange={handleChange}
            className="w-full border border-white/20 bg-gray-800 text-white rounded-lg p-2"
          >
            <option value="">Select Assigned Employee</option>
            {employees.map((emp) => (
              <option key={emp.id} value={emp.id}>
                {emp.name} {emp.surname}
              </option>
            ))}
          </select>
          <select
            name="status"
            value={formData.status || "TODO"}
            onChange={handleChange}
            className="w-full border border-white/20 bg-gray-800 text-white rounded-lg p-2"
          >
            <option value="TODO">TODO</option>
            <option value="IN_PROGRESS">IN PROGRESS</option>
            <option value="DONE">DONE</option>
          </select>

          {/* Actions */}
          <div className="flex justify-end gap-4">
            <button
              type="button"
              onClick={onClose}
              className="bg-gray-600 px-4 py-2 rounded-lg hover:bg-gray-700"
            >
              Cancel
            </button>
            <button
              type="submit"
              className="bg-gradient-to-r from-yellow-500 to-yellow-700 px-4 py-2 rounded-lg hover:scale-105 transition"
            >
              Save Changes
            </button>
          </div>
        </form>
      </div>
    </div>
  );
}

export default SubtaskEditModal;
