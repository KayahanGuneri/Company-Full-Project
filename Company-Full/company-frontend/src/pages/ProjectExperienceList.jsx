import { useEffect, useState } from "react";
import { getAllExperiences, updateProjectAssignment } from "../services/projectExperienceService";
import { useNavigate } from "react-router-dom";
import { MagnifyingGlassIcon, PlusCircleIcon, PencilSquareIcon, ClipboardDocumentListIcon } from "@heroicons/react/24/solid";

function ProjectExperienceList() {
  const [experiences, setExperiences] = useState([]);
  const [searchQuery, setSearchQuery] = useState("");
  const [error, setError] = useState("");
  const [editingAssignment, setEditingAssignment] = useState(null);
  const [editForm, setEditForm] = useState({ startDate: "", endDate: "" });
  const navigate = useNavigate();
  const role = localStorage.getItem("role");

  const fetchExperiences = async () => {
    try {
      const data = await getAllExperiences();
      const uniqueProjects = data.filter(
        (exp, index, self) =>
          index === self.findIndex((e) => e.projectName === exp.projectName)
      );
      setExperiences(uniqueProjects);
    } catch {
      setError("Failed to load project experiences.");
    }
  };

  useEffect(() => {
    fetchExperiences();
  }, []);

  const filteredProjects = experiences.filter((proj) =>
    proj.projectName?.toLowerCase().includes(searchQuery.toLowerCase())
  );

  const handleEditClick = (assignment) => {
    setEditingAssignment(assignment);
    setEditForm({
      startDate: assignment.employeeStartDate || "",
      endDate: assignment.employeeEndDate || "",
    });
  };

  const handleSaveEdit = async () => {
    try {
      await updateProjectAssignment({
        projectId: editingAssignment.projectId ?? editingAssignment.id,
        employeeId: editingAssignment.employeeId,
        startDate: editForm.startDate,
        endDate: editForm.endDate,
      });
      setEditingAssignment(null);
      fetchExperiences();
    } catch {
      alert("Failed to update assignment.");
    }
  };

  return (
    <div className="min-h-screen w-screen bg-gradient-to-br from-indigo-900 to-purple-800 p-10 text-white">
      <h1 className="text-4xl font-extrabold text-center mb-8">Projects</h1>

      <div className="flex flex-col sm:flex-row items-center justify-between gap-4 mb-8">
        <div className="flex items-center bg-white/20 rounded-lg px-4 py-2 backdrop-blur-md w-full sm:w-1/2">
          <MagnifyingGlassIcon className="w-5 h-5 mr-2 text-gray-300" />
          <input
            type="text"
            placeholder="Search by project name..."
            value={searchQuery}
            onChange={(e) => setSearchQuery(e.target.value)}
            className="bg-transparent outline-none text-white w-full placeholder-gray-300"
          />
        </div>

        {role?.toUpperCase() === "ADMIN" && (
          <button
            onClick={() => navigate("/projects/new")}
            className="flex items-center gap-2 bg-gradient-to-r from-green-500 to-green-700 px-4 py-2 rounded-lg hover:scale-105 transition"
          >
            <PlusCircleIcon className="w-5 h-5" /> Add Project
          </button>
        )}
      </div>

      {error && <p className="text-red-400 mb-4">{error}</p>}

      <div className="grid grid-cols-1 sm:grid-cols-2 lg:grid-cols-3 gap-6">
        {filteredProjects.map((proj, index) => (
          <div
            key={proj.projectId ?? proj.id ?? index}
            className="bg-white/10 backdrop-blur-lg p-6 rounded-2xl shadow-lg hover:scale-105 transition"
          >
            <h2 className="text-2xl font-semibold mb-2">{proj.projectName}</h2>
            <p className="text-gray-300 text-sm mb-4">
              {proj.startDate || "-"} → {proj.endDate || "-"}
            </p>

            <div className="flex gap-2 flex-wrap">
              <button
                onClick={() =>
                  navigate(`/projects/${proj.projectId ?? proj.id}/tasks`)
                }
                className="flex items-center gap-1 text-blue-300 hover:underline"
              >
                <ClipboardDocumentListIcon className="w-4 h-4" /> View Tasks
              </button>

              {role?.toUpperCase() === "ADMIN" && (
                <button
                  onClick={() => handleEditClick(proj)}
                  className="flex items-center gap-1 bg-yellow-500 px-3 py-1 rounded-lg text-black hover:bg-yellow-600"
                >
                  <PencilSquareIcon className="w-4 h-4" /> Edit Dates
                </button>
              )}
            </div>
          </div>
        ))}
      </div>

      {filteredProjects.length === 0 && !error && (
        <p className="text-gray-300 text-center mt-8">No projects found.</p>
      )}

      {editingAssignment && (
        <div className="fixed inset-0 bg-black/50 flex justify-center items-center z-50">
          <div className="bg-white text-black p-6 rounded-lg w-full max-w-md">
            <h2 className="text-xl font-bold mb-4">Edit Assignment Dates</h2>
            <input
              type="date"
              value={editForm.startDate}
              onChange={(e) => setEditForm({ ...editForm, startDate: e.target.value })}
              className="w-full border p-2 mb-4 rounded"
            />
            <input
              type="date"
              value={editForm.endDate}
              onChange={(e) => setEditForm({ ...editForm, endDate: e.target.value })}
              className="w-full border p-2 mb-4 rounded"
            />
            <div className="flex justify-end gap-4">
              <button
                onClick={() => setEditingAssignment(null)}
                className="bg-gray-400 px-4 py-2 rounded text-white"
              >
                Cancel
              </button>
              <button
                onClick={handleSaveEdit}
                className="bg-green-500 px-4 py-2 rounded text-white"
              >
                Save
              </button>
            </div>
          </div>
        </div>
      )}
    </div>
  );
}

export default ProjectExperienceList;
