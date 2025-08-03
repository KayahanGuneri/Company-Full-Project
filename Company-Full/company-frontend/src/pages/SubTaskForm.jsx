// src/pages/SubTaskForm.jsx
import { useState } from "react";
import { useParams, useNavigate } from "react-router-dom";
import { createSubTask, updateSubTask } from "../services/taskService"; // API functions

function SubTaskForm({ subTask = null, onSave }) {
  const { taskId } = useParams(); // SubTask belongs to a specific task
  const navigate = useNavigate();

  const [formData, setFormData] = useState(
    subTask || {
      title: "",
      assignedUserId: "",
      status: "TODO",
    }
  );
  const [error, setError] = useState("");

  // Handle input change
  const handleChange = (e) => {
    const { name, value } = e.target;
    setFormData({ ...formData, [name]: value });
  };

  // Submit subtask (create or update)
  const handleSubmit = async (e) => {
    e.preventDefault();
    try {
      if (subTask) {
        await updateSubTask(taskId, formData); // Update existing subtask
      } else {
        await createSubTask(taskId, formData); // Create new subtask
      }
      if (onSave) onSave(); // Refresh parent task list if provided
      navigate(`/tasks/${taskId}`); // Go back to task detail page
    } catch (err) {
      setError("Failed to save subtask. Please check input or permissions.");
    }
  };

  return (
    <div className="h-screen w-screen flex items-center justify-center bg-gray-100">
      <div className="bg-white shadow-lg rounded-lg p-8 w-full max-w-md">
        <h2 className="text-2xl font-bold mb-6">
          {subTask ? "Edit Subtask" : "Add New Subtask"}
        </h2>

        {error && <p className="text-red-500 mb-4">{error}</p>}

        <form onSubmit={handleSubmit} className="space-y-4">
          {/* Subtask Title */}
          <input
            name="title"
            value={formData.title}
            onChange={handleChange}
            className="w-full border p-2 rounded"
            placeholder="Subtask Title"
            required
          />

          {/* Assigned User */}
          <input
            type="number"
            name="assignedUserId"
            value={formData.assignedUserId}
            onChange={handleChange}
            className="w-full border p-2 rounded"
            placeholder="Assigned User ID (optional)"
          />

          {/* Status Dropdown */}
          <select
            name="status"
            value={formData.status}
            onChange={handleChange}
            className="w-full border p-2 rounded"
          >
            <option value="TODO">TODO</option>
            <option value="IN_PROGRESS">IN_PROGRESS</option>
            <option value="DONE">DONE</option>
          </select>

          {/* Buttons */}
          <div className="flex justify-end space-x-4">
            <button
              type="button"
              onClick={() => navigate(`/tasks/${taskId}`)}
              className="bg-gray-400 text-white px-4 py-2 rounded"
            >
              Cancel
            </button>
            <button
              type="submit"
              className="bg-green-500 text-white px-4 py-2 rounded hover:bg-green-600"
            >
              Save Subtask
            </button>
          </div>
        </form>
      </div>
    </div>
  );
}

export default SubTaskForm;
