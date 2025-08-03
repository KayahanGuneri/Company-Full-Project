import { useState } from "react";
import { updateTask } from "../services/taskService";
import { XMarkIcon, PencilSquareIcon } from "@heroicons/react/24/solid";

function TaskEditModal({ task, onClose, onSave }) {
  const [formData, setFormData] = useState(task);

  const handleChange = (e) => setFormData({ ...formData, [e.target.name]: e.target.value });

  const handleSubmit = async (e) => {
    e.preventDefault();
    try {
      await updateTask(task.id, formData);
      onSave();
      onClose();
    } catch {
      alert("Failed to update task. Check your input.");
    }
  };

  return (
    <div className="fixed inset-0 bg-black/50 backdrop-blur-sm flex justify-center items-center z-50">
      <div className="bg-white/10 backdrop-blur-lg border border-white/20 p-6 rounded-2xl shadow-xl w-full max-w-md animate-scaleIn">
        
        {/* Header */}
        <div className="flex justify-between items-center mb-4">
          <h2 className="flex items-center gap-2 text-white text-xl font-bold">
            <PencilSquareIcon className="w-6 h-6 text-yellow-300" />
            Edit Task
          </h2>
          <button onClick={onClose} className="text-white hover:text-red-300">
            <XMarkIcon className="w-6 h-6" />
          </button>
        </div>

        {/* Form */}
        <form onSubmit={handleSubmit} className="space-y-4">
          <input name="title" value={formData.title || ""} onChange={handleChange} placeholder="Task Title"
            className="w-full border border-white/20 bg-white/10 text-white rounded-lg p-2 placeholder-gray-300" />
          
          <input name="assignedUserFullName" value={formData.assignedUserFullName || ""} onChange={handleChange} placeholder="Assigned User Name"
            className="w-full border border-white/20 bg-white/10 text-white rounded-lg p-2 placeholder-gray-300" />
          
          <select name="status" value={formData.status || "TODO"} onChange={handleChange}
            className="w-full border border-white/20 bg-white/10 text-white rounded-lg p-2">
            <option value="TODO">TODO</option>
            <option value="IN_PROGRESS">IN PROGRESS</option>
            <option value="DONE">DONE</option>
          </select>

          {/* Actions */}
          <div className="flex justify-end gap-4">
            <button type="button" onClick={onClose} className="bg-gray-500 px-4 py-2 rounded-lg text-white hover:bg-gray-600">Cancel</button>
            <button type="submit" className="bg-gradient-to-r from-yellow-500 to-yellow-700 px-4 py-2 rounded-lg text-white hover:scale-105 transition">Save Changes</button>
          </div>
        </form>
      </div>
    </div>
  );
}

export default TaskEditModal;
