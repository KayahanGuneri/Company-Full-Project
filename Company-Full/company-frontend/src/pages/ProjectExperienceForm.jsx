import { useEffect, useState } from "react";
import { useNavigate } from "react-router-dom";
import { addExperience } from "../services/projectExperienceService";
import { getActiveEmployees } from "../services/employeeService";
import { XMarkIcon, ClipboardDocumentListIcon } from "@heroicons/react/24/solid";

function ProjectExperienceForm() {
  const [projectName, setProjectName] = useState("");
  const [startDate, setStartDate] = useState("");
  const [endDate, setEndDate] = useState("");
  const [employeeId, setEmployeeId] = useState("");
  const [employees, setEmployees] = useState([]);
  const [employeeStartDate, setEmployeeStartDate] = useState("");
  const [employeeEndDate, setEmployeeEndDate] = useState("");
  const [active, setActive] = useState(true);
  const [description, setDescription] = useState("");
  const [durationText, setDurationText] = useState("");
  const [message, setMessage] = useState("");

  const navigate = useNavigate();

  // Sayfa açıldığında aktif çalışanları getir
  useEffect(() => {
    getActiveEmployees()
      .then((data) => setEmployees(data))
      .catch((err) => console.error("Error fetching employees:", err));
  }, []);

  const handleSubmit = async (e) => {
    e.preventDefault();
    try {
      await addExperience({
        id: 0,
        projectName,
        startDate,
        endDate,
        employeeId: parseInt(employeeId),
        employeeStartDate,
        employeeEndDate,
        active,
        description,
        durationText,
      });

      setMessage("Project experience added successfully!");
      setTimeout(() => navigate("/projects"), 1500);
    } catch (error) {
      console.error(error);
      setMessage("Failed to add project experience.");
    }
  };

  return (
    <div className="h-screen w-screen flex items-center justify-center bg-gradient-to-br from-gray-900 via-gray-800 to-black">
      <div className="bg-white/10 backdrop-blur-lg rounded-2xl shadow-2xl border border-white/20 p-8 w-full max-w-2xl animate-scaleIn">
        
        {/* Header */}
        <div className="flex justify-between items-center mb-6">
          <h2 className="flex items-center gap-2 text-2xl font-bold text-white">
            <ClipboardDocumentListIcon className="w-6 h-6 text-blue-300" />
            Add Project Experience
          </h2>
          <button onClick={() => navigate(-1)} className="text-white hover:text-red-300">
            <XMarkIcon className="w-6 h-6" />
          </button>
        </div>

        {/* Message */}
        {message && (
          <p className="text-center text-blue-300 mb-4 font-medium">{message}</p>
        )}

        {/* Form */}
        <form onSubmit={handleSubmit} className="flex flex-col gap-4">
          {/* Project Name */}
          <input
            type="text"
            placeholder="Project Name"
            value={projectName}
            onChange={(e) => setProjectName(e.target.value)}
            className="border border-white/20 bg-white/10 text-white rounded-lg p-3 
                       placeholder-gray-300 focus:outline-none focus:ring-2 focus:ring-blue-400"
            required
          />

          {/* Project Dates */}
          <div className="flex gap-4">
            <input
              type="date"
              value={startDate}
              onChange={(e) => setStartDate(e.target.value)}
              className="border border-white/20 bg-white/10 text-white rounded-lg p-3 w-1/2 
                         focus:outline-none focus:ring-2 focus:ring-blue-400"
              required
            />
            <input
              type="date"
              value={endDate}
              onChange={(e) => setEndDate(e.target.value)}
              className="border border-white/20 bg-white/10 text-white rounded-lg p-3 w-1/2 
                         focus:outline-none focus:ring-2 focus:ring-blue-400"
              required
            />
          </div>

          {/* Employee Dropdown */}
          <select
            value={employeeId}
            onChange={(e) => setEmployeeId(e.target.value)}
            className="border border-white/20 bg-white/10 text-white rounded-lg p-3 
                       focus:outline-none focus:ring-2 focus:ring-blue-400 
                       appearance-none cursor-pointer"
            required
          >
            <option value="" className="bg-gray-800 text-white">Select Employee</option>
            {employees.map((emp) => (
              <option 
                key={emp.id} 
                value={emp.id} 
                className="bg-gray-800 text-white"
              >
                {emp.name} {emp.surname}
              </option>
            ))}
          </select>

          {/* Employee Dates */}
          <div className="flex gap-4">
            <input
              type="date"
              value={employeeStartDate}
              onChange={(e) => setEmployeeStartDate(e.target.value)}
              className="border border-white/20 bg-white/10 text-white rounded-lg p-3 w-1/2 
                         focus:outline-none focus:ring-2 focus:ring-blue-400"
              required
            />
            <input
              type="date"
              value={employeeEndDate}
              onChange={(e) => setEmployeeEndDate(e.target.value)}
              className="border border-white/20 bg-white/10 text-white rounded-lg p-3 w-1/2 
                         focus:outline-none focus:ring-2 focus:ring-blue-400"
              required
            />
          </div>

          {/* Description */}
          <textarea
            placeholder="Description"
            value={description}
            onChange={(e) => setDescription(e.target.value)}
            className="border border-white/20 bg-white/10 text-white rounded-lg p-3 
                       placeholder-gray-300 focus:outline-none focus:ring-2 focus:ring-blue-400"
          />

          {/* Duration */}
          <input
            type="text"
            placeholder="Duration (e.g., 6 months)"
            value={durationText}
            onChange={(e) => setDurationText(e.target.value)}
            className="border border-white/20 bg-white/10 text-white rounded-lg p-3 
                       placeholder-gray-300 focus:outline-none focus:ring-2 focus:ring-blue-400"
          />

          {/* Active Checkbox */}
          <label className="flex items-center gap-2 text-white">
            <input
              type="checkbox"
              checked={active}
              onChange={(e) => setActive(e.target.checked)}
              className="accent-blue-500"
            />
            Active
          </label>

          {/* Buttons */}
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
              className="bg-gradient-to-r from-blue-500 to-blue-700 px-4 py-2 rounded-lg 
                         text-white font-semibold hover:scale-105 transition"
            >
              Save Project Experience
            </button>
          </div>
        </form>
      </div>
    </div>
  );
}

export default ProjectExperienceForm;
