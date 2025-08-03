import { useState } from "react";
import { createSubtask } from "../services/taskService";
import { XMarkIcon, PlusCircleIcon } from "@heroicons/react/24/solid";

function SubTaskFormModal({ taskId, onClose, onSave }) {
  const [title, setTitle] = useState("");
  const [assignedUserId, setAssignedUserId] = useState("");
  const [loading, setLoading] = useState(false);
  const [error, setError] = useState("");

  // Submit new subtask
  const handleSubmit = async (e) => {
    e.preventDefault();
    setLoading(true);
    setError("");

    try {
      const subTaskData = {
        title,
        assignedUserId: assignedUserId || null,
        status: "TODO"
      };
      await createSubtask(taskId, subTaskData);
      onSave();
      onClose();
    } catch (err) {
      setError("Failed to create subtask. Please check your input.");
    } finally {
      setLoading(false);
    }
  };

  return (
    <div className="fixed inset-0 bg-black/50 backdrop-blur-sm flex justify-center items-center z-50">
      <div className="bg-white/10 backdrop-blur-lg border border-white/20 p-6 rounded-2xl shadow-xl w-full max-w-md animate-scaleIn">
        {/* Header */}
        <div className="flex justify-between items-center mb-4">
          <h2 className="flex items-center gap-2 text-white text-xl font-bold">
            <PlusCircleIcon className="w-6 h-6 text-green-300" />
            Add Subtask
          </h2>
          <button onClick={onClose} className="text-white hover:text-red-300">
            <XMarkIcon className="w-6 h-6" />
          </button>
        </div>

        {error && <p className="text-red-300 mb-2">{error}</p>}

        {/* Form */}
        <form onSubmit={handleSubmit} className="space-y-4">
          <input
            type="text"
            placeholder="Subtask Title"
            value={title}
            onChange={(e) => setTitle(e.target.value)}
            className="w-full border border-white/20 bg-white/10 text-white rounded-lg p-2 placeholder-gray-300"
            required
          />
          <input
            type="number"
            placeholder="Assigned User ID (optional)"
            value={assignedUserId}
            onChange={(e) => setAssignedUserId(e.target.value)}
            className="w-full border border-white/20 bg-white/10 text-white rounded-lg p-2 placeholder-gray-300"
          />

          {/* Actions */}
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
              className="bg-gradient-to-r from-green-500 to-green-700 px-4 py-2 rounded-lg text-white hover:scale-105 transition"
            >
              {loading ? "Saving..." : "Save"}
            </button>
          </div>
        </form>
      </div>
    </div>
  );
}

export default SubTaskFormModal;
