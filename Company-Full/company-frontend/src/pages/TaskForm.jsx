// src/pages/TaskList.jsx
import { useEffect, useState } from "react";
import { useParams } from "react-router-dom";
import {
  getTasksByProject,
  createSubtask,
  updateSubtask,
  deleteSubtask,
} from "../services/taskService";

import SubtaskAddModal from "../components/SubtaskAddModal";
import SubtaskEditModal from "../components/SubtaskEditModal";

function TaskList() {
  const { projectId } = useParams();
  const [tasks, setTasks] = useState([]);
  const [error, setError] = useState("");
  const [expandedTaskId, setExpandedTaskId] = useState(null);

  // Modal State
  const [showAddModal, setShowAddModal] = useState(false);
  const [showEditModal, setShowEditModal] = useState(false);
  const [activeTaskId, setActiveTaskId] = useState(null);
  const [activeSubtask, setActiveSubtask] = useState(null);

  const fetchTasks = async () => {
    try {
      const data = await getTasksByProject(projectId);
      setTasks(data);
    } catch (err) {
      setError("Failed to load tasks. Please check your permissions.");
    }
  };

  useEffect(() => {
    fetchTasks();
  }, [projectId]);

  const toggleTask = (taskId) => {
    setExpandedTaskId((prev) => (prev === taskId ? null : taskId));
  };

  // Open Add Subtask Modal
  const openAddModal = (taskId) => {
    setActiveTaskId(taskId);
    setShowAddModal(true);
  };

  // Open Edit Subtask Modal
  const openEditModal = (subtask) => {
    setActiveSubtask(subtask);
    setShowEditModal(true);
  };

  // Handle Delete Subtask
  const handleDeleteSubtask = async (subtaskId) => {
    if (!window.confirm("Are you sure you want to delete this subtask?")) return;
    try {
      await deleteSubtask(subtaskId);
      fetchTasks(); // Refresh list
    } catch (err) {
      alert("Failed to delete subtask.");
    }
  };

  return (
    <div className="h-screen w-screen bg-gray-100 flex flex-col items-center p-10">
      <h1 className="text-4xl font-bold text-gray-800 mb-6">Project Tasks</h1>

      {error && <p className="text-red-500 mb-4">{error}</p>}

      <div className="bg-white shadow-xl rounded-lg overflow-hidden w-full max-w-5xl">
        <table className="w-full border-collapse">
          <thead className="bg-gray-200">
            <tr>
              <th className="p-4 text-left">Task Title</th>
              <th className="p-4 text-left">Assigned User</th>
              <th className="p-4 text-left">Status</th>
              <th className="p-4 text-left">Actions</th>
            </tr>
          </thead>
          <tbody>
            {tasks.map((task) => (
              <>
                <tr key={task.id} className="hover:bg-gray-50">
                  <td className="p-4 font-semibold">{task.title}</td>
                  <td className="p-4">{task.assignedUserFullName || "-"}</td>
                  <td className="p-4">{task.status}</td>
                  <td className="p-4 space-x-4">
                    <button
                      onClick={() => toggleTask(task.id)}
                      className="text-blue-500 hover:underline"
                    >
                      {expandedTaskId === task.id
                        ? "Hide Subtasks"
                        : "Show Subtasks"}
                    </button>
                    <button
                      onClick={() => openAddModal(task.id)}
                      className="text-green-500 hover:underline"
                    >
                      Add Subtask
                    </button>
                  </td>
                </tr>

                {expandedTaskId === task.id && task.subTasks?.length > 0 && (
                  <tr>
                    <td colSpan="4" className="bg-gray-50 p-4">
                      <h3 className="font-bold mb-2">Subtasks</h3>
                      <ul className="space-y-2">
                        {task.subTasks.map((sub) => (
                          <li
                            key={sub.id}
                            className="flex justify-between items-center bg-white p-2 rounded shadow"
                          >
                            <span>
                              {sub.title} -{" "}
                              <span className="text-sm text-gray-500">
                                {sub.assignedUserFullName || "Unassigned"}
                              </span>
                            </span>
                            <span className="flex space-x-4">
                              <span className="text-gray-600">{sub.status}</span>
                              <button
                                onClick={() => openEditModal(sub)}
                                className="text-yellow-500 hover:underline"
                              >
                                Edit
                              </button>
                              <button
                                onClick={() => handleDeleteSubtask(sub.id)}
                                className="text-red-500 hover:underline"
                              >
                                Delete
                              </button>
                            </span>
                          </li>
                        ))}
                      </ul>
                    </td>
                  </tr>
                )}
              </>
            ))}
            {tasks.length === 0 && !error && (
              <tr>
                <td
                  colSpan="4"
                  className="p-6 text-center text-gray-500 italic"
                >
                  No tasks found for this project.
                </td>
              </tr>
            )}
          </tbody>
        </table>
      </div>

      {/* Subtask Add Modal */}
      {showAddModal && (
        <SubtaskAddModal
          taskId={activeTaskId}
          onClose={() => setShowAddModal(false)}
          onSave={fetchTasks}
        />
      )}

      {/* Subtask Edit Modal */}
      {showEditModal && activeSubtask && (
        <SubtaskEditModal
          subtask={activeSubtask}
          onClose={() => setShowEditModal(false)}
          onSave={fetchTasks}
        />
      )}
    </div>
  );
}

export default TaskList;
