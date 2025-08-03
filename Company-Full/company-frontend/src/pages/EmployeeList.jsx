import { useEffect, useState } from "react";
import { getEmployees, deleteEmployee } from "../services/employeeService";
import { getExperiencesByEmployee, getAllExperiences } from "../services/projectExperienceService";
import { useNavigate } from "react-router-dom";
import EmployeeDetailsModal from "../components/EmployeeDetailsModal";
import AssignEmployeeForm from "../components/AssignEmployeeForm";
import { UserPlusIcon, ClipboardDocumentListIcon } from "@heroicons/react/24/solid";

function EmployeeList() {
  const [employees, setEmployees] = useState([]);
  const [employeeProjects, setEmployeeProjects] = useState([]);
  const [projects, setProjects] = useState([]);
  const [showModal, setShowModal] = useState(false);
  const [showAssignModal, setShowAssignModal] = useState(false);
  const [error, setError] = useState("");
  const [message, setMessage] = useState("");
  const navigate = useNavigate();

  const fetchEmployees = async () => {
    try {
      const data = await getEmployees();
      const uniqueEmployees = Array.from(new Map(data.map(emp => [emp.id, emp])).values());
      setEmployees(uniqueEmployees);
    } catch {
      setError("Failed to load employees. Check your login or role.");
    }
  };

  const fetchProjects = async () => {
    try {
      const projData = await getAllExperiences();
      const uniqueProjects = Array.from(new Map(projData.map(proj => [proj.id, proj])).values());
      setProjects(uniqueProjects);
    } catch {
      setError("Failed to load projects for assignment.");
    }
  };

  useEffect(() => {
    fetchEmployees();
  }, []);

  const handleDelete = async (id) => {
    if (!window.confirm("Are you sure you want to delete this employee?")) return;
    try {
      await deleteEmployee(id);
      setMessage("Employee deleted successfully.");
      fetchEmployees();
    } catch {
      setError("Failed to delete employee. Admin role required.");
    }
  };

  const handleViewDetails = async (employeeId) => {
    try {
      const projects = await getExperiencesByEmployee(employeeId);
      setEmployeeProjects(projects);
      setShowModal(true);
    } catch {
      setError("Failed to load employee projects.");
    }
  };

  const handleOpenAssignModal = async () => {
    await fetchProjects();
    setShowAssignModal(true);
  };

  return (
    <div className="min-h-screen w-screen flex flex-col items-center p-8 bg-gradient-to-br from-gray-900 to-gray-800 text-white">
      <h1 className="text-3xl font-bold mb-6">Employee List</h1>

      {message && <p className="text-green-400 mb-4">{message}</p>}
      {error && <p className="text-red-400 mb-4">{error}</p>}

      <div className="flex gap-4 mb-6">
        <button
          onClick={() => navigate("/employees/new")}
          className="flex items-center gap-2 bg-gradient-to-r from-blue-500 to-blue-700 px-4 py-2 rounded-lg hover:scale-105 transition"
        >
          <UserPlusIcon className="w-5 h-5" /> Add Employee
        </button>
        <button
          onClick={handleOpenAssignModal}
          className="flex items-center gap-2 bg-gradient-to-r from-purple-500 to-purple-700 px-4 py-2 rounded-lg hover:scale-105 transition"
        >
          <ClipboardDocumentListIcon className="w-5 h-5" /> Assign Project
        </button>
      </div>

      <div className="overflow-x-auto shadow-lg rounded-xl w-full max-w-5xl">
        <table className="min-w-full text-left bg-white/10 backdrop-blur-md border border-white/20 rounded-lg">
          <thead className="bg-white/20">
            <tr>
              <th className="p-3">Name</th>
              <th className="p-3">Email</th>
              <th className="p-3">Details</th>
              <th className="p-3">Actions</th>
            </tr>
          </thead>
          <tbody>
            {employees.map((emp) => (
              <tr key={emp.id} className="border-b border-white/10 hover:bg-white/10 transition">
                <td className="p-3">{emp.name} {emp.surname}</td>
                <td className="p-3">{emp.email}</td>
                <td className="p-3">
                  <button
                    onClick={() => handleViewDetails(emp.id)}
                    className="bg-green-500 hover:bg-green-600 px-3 py-1 rounded text-sm"
                  >
                    View Details
                  </button>
                </td>
                <td className="p-3 space-x-2">
                  <button
                    onClick={() => navigate(`/employees/edit/${emp.id}`)}
                    className="text-blue-400 hover:underline"
                  >
                    Edit
                  </button>
                  <button
                    onClick={() => handleDelete(emp.id)}
                    className="text-red-400 hover:underline"
                  >
                    Delete
                  </button>
                </td>
              </tr>
            ))}
          </tbody>
        </table>
      </div>

      {showModal && (
        <EmployeeDetailsModal
          projects={employeeProjects}
          onClose={() => setShowModal(false)}
        />
      )}

      {showAssignModal && (
        <div className="fixed inset-0 bg-black bg-opacity-30 flex justify-center items-center">
          <AssignEmployeeForm
            employees={employees}
            projects={projects}
            onClose={() => setShowAssignModal(false)}
            onSuccess={() => {
              setMessage("Employee assigned successfully!");
              fetchEmployees();
            }}
          />
        </div>
      )}
    </div>
  );
}

export default EmployeeList;
