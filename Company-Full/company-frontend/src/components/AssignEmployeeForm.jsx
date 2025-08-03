import { useState } from "react";
import { assignEmployeeToProject, getExperiencesByEmployee } from "../services/projectExperienceService";
import { XMarkIcon, ClipboardDocumentListIcon, UserPlusIcon } from "@heroicons/react/24/solid";

function AssignEmployeeForm({ employees, projects, onClose, onSuccess }) {
  const [employeeId, setEmployeeId] = useState("");
  const [projectId, setProjectId] = useState("");
  const [startDate, setStartDate] = useState("");
  const [endDate, setEndDate] = useState("");
  const [error, setError] = useState("");

  const checkOverlap = async () => {
    if (!employeeId) return false;
    try {
      const assignments = await getExperiencesByEmployee(employeeId);
      return assignments.some(a => {
        const s1 = new Date(a.startDate);
        const e1 = a.endDate ? new Date(a.endDate) : null;
        const s2 = new Date(startDate);
        const e2 = endDate ? new Date(endDate) : null;

        if (!e1 || !e2) {
          return s1 <= (e2 || new Date()) && s2 <= (e1 || new Date());
        }
        return s1 <= e2 && s2 <= e1;
      });
    } catch {
      return false;
    }
  };

  const handleSubmit = async (e) => {
    e.preventDefault();
    setError("");

    try {
      const hasConflict = await checkOverlap();
      if (hasConflict) {
        setError("This employee is already assigned during the selected dates.");
        return;
      }

      await assignEmployeeToProject({ employeeId, projectId, startDate, endDate });
      if (onSuccess) onSuccess();
      onClose();
    } catch (err) {
      const backendMessage =
        err.response?.data?.message || err.response?.data || "Failed to assign employee.";
      setError(backendMessage);
    }
  };

  return (
    <div className="fixed inset-0 bg-black/50 backdrop-blur-sm flex justify-center items-center z-50">
      <div className="bg-white/10 backdrop-blur-lg border border-white/20 p-6 rounded-2xl shadow-xl w-full max-w-md animate-scaleIn">
        
        {/* Header */}
        <div className="flex justify-between items-center mb-4">
          <h2 className="flex items-center gap-2 text-white text-xl font-bold">
            <UserPlusIcon className="w-6 h-6 text-green-300" />
            Assign Employee to Project
          </h2>
          <button
            onClick={onClose}
            className="text-white hover:text-red-300"
          >
            <XMarkIcon className="w-6 h-6" />
          </button>
        </div>

        {error && <p className="text-red-300 mb-3">{error}</p>}

        {/* Form */}
        <form onSubmit={handleSubmit} className="space-y-4">
          <select
            value={employeeId}
            onChange={(e) => setEmployeeId(e.target.value)}
            required
            className="border border-white/20 bg-white/10 text-white rounded-lg p-2 focus:outline-none focus:ring-2 focus:ring-green-400"
          >
            <option value="" className="text-gray-700">Select Employee</option>
            {employees?.map((emp) => (
              <option key={emp.id} value={emp.id} className="text-black">
                {emp.name} {emp.surname}
              </option>
            ))}
          </select>

          <select
            value={projectId}
            onChange={(e) => setProjectId(e.target.value)}
            required
            className="border border-white/20 bg-white/10 text-white rounded-lg p-2 focus:outline-none focus:ring-2 focus:ring-green-400"
          >
            <option value="" className="text-gray-700">Select Project</option>
            {projects?.map((proj) => (
              <option key={proj.id} value={proj.id} className="text-black">
                {proj.projectName}
              </option>
            ))}
          </select>

          <input
            type="date"
            value={startDate}
            onChange={(e) => setStartDate(e.target.value)}
            className="border border-white/20 bg-white/10 text-white rounded-lg p-2 focus:outline-none focus:ring-2 focus:ring-green-400"
            required
          />
          <input
            type="date"
            value={endDate}
            onChange={(e) => setEndDate(e.target.value)}
            className="border border-white/20 bg-white/10 text-white rounded-lg p-2 focus:outline-none focus:ring-2 focus:ring-green-400"
          />

          {/* Buttons */}
          <div className="flex justify-end gap-4 mt-4">
            <button
              type="button"
              onClick={onClose}
              className="bg-gray-500 px-4 py-2 rounded-lg text-white hover:bg-gray-600"
            >
              Cancel
            </button>
            <button
              type="submit"
              className="bg-gradient-to-r from-green-500 to-green-700 px-4 py-2 rounded-lg text-white font-semibold hover:scale-105 transition"
            >
              Assign
            </button>
          </div>
        </form>
      </div>
    </div>
  );
}

export default AssignEmployeeForm;
