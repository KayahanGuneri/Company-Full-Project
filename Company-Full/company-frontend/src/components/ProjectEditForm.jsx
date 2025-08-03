import { useState } from "react";
import { updateProjectExperience } from "../services/projectExperienceService";

function ProjectEditForm({ project, onSave, onCancel }) {
  const [formData, setFormData] = useState(project);

  const handleChange = (e) => {
    const { name, value } = e.target;
    setFormData({ ...formData, [name]: value });
  };

  const handleSubmit = async (e) => {
    e.preventDefault();
    try {
      await updateProjectExperience(formData.id, formData);
      onSave(); // Listeyi yenile veya modalı kapat
    } catch (err) {
      alert("Update failed. Please check the console for details.");
    }
  };

  return (
    <div className="bg-white shadow-md rounded p-6 max-w-md mx-auto">
      <h2 className="text-xl font-bold mb-4">Edit Project</h2>
      <form onSubmit={handleSubmit} className="space-y-4">
        <input
          name="projectName"
          value={formData.projectName}
          onChange={handleChange}
          className="w-full border p-2 rounded"
          placeholder="Project Name"
        />
        <input
          type="date"
          name="startDate"
          value={formData.startDate}
          onChange={handleChange}
          className="w-full border p-2 rounded"
        />
        <input
          type="date"
          name="endDate"
          value={formData.endDate}
          onChange={handleChange}
          className="w-full border p-2 rounded"
        />
        <textarea
          name="description"
          value={formData.description}
          onChange={handleChange}
          className="w-full border p-2 rounded"
          placeholder="Description"
        />
        <div className="flex justify-end space-x-4">
          <button
            type="button"
            onClick={onCancel}
            className="bg-gray-400 text-white px-4 py-2 rounded"
          >
            Cancel
          </button>
          <button
            type="submit"
            className="bg-blue-500 text-white px-4 py-2 rounded"
          >
            Save
          </button>
        </div>
      </form>
    </div>
  );
}

export default ProjectEditForm;
