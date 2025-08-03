
import { useEffect, useState } from "react";
import { getTasksByProject, createSubtask } from "../services/taskService";
import { useParams } from "react-router-dom";

function TaskList() {
  const { projectId } = useParams();
  const [tasks, setTasks] = useState([]);
  const [error, setError] = useState("");
  const [expandedTaskId, setExpandedTaskId] = useState(null);

  // Subtask Modal State
  const [showModal, setShowModal] = useState(false);
  const [activeTaskId, setActiveTaskId] = useState(null);
  const [subtaskForm, setSubtaskForm] = useState({
    title: "",
    assignedUserId: "",
    status: "TODO",
  });

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

  // Open Subtask Modal
  const openSubtaskModal = (taskId) => {
    setActiveTaskId(taskId);
    setShowModal(true);
  };

  // Handle Subtask Form Change
  const handleSubtaskChange = (e) => {
    const { name, value } = e.target;
    setSubtaskForm({ ...subtaskForm, [name]: value });
  };

  // Save Subtask
  const handleSaveSubtask = async () => {
    try {
      await createSubtask(activeTaskId, subtaskForm);
      setShowModal(false);
      setSubtaskForm({ title: "", assignedUserId: "", status: "TODO" });
      fetchTasks(); // Refresh tasks after save
    } catch (err) {
      alert("Failed to add subtask. Check console for details.");
      console.error(err);
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
                      onClick={() => openSubtaskModal(task.id)}
                      className="text-green-500 hover:underline"
                    >
                      Add Subtask
                    </button>
                    <button className="text-yellow-500 hover:underline">
                      Edit
                    </button>
                    <button className="text-red-500 hover:underline">
                      Delete
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
                              <button className="text-yellow-500 hover:underline">
                                Edit
                              </button>
                              <button className="text-red-500 hover:underline">
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

      {/* Subtask Modal */}
      {showModal && (
        <div className="fixed inset-0 bg-black bg-opacity-50 flex items-center justify-center">
          <div className="bg-white p-6 rounded-lg shadow-lg w-96">
            <h2 className="text-xl font-bold mb-4">Add Subtask</h2>
            <input
              name="title"
              value={subtaskForm.title}
              onChange={handleSubtaskChange}
              placeholder="Subtask Title"
              className="w-full border p-2 mb-3 rounded"
            />
            <input
              name="assignedUserId"
              value={subtaskForm.assignedUserId}
              onChange={handleSubtaskChange}
              placeholder="Assigned User ID"
              className="w-full border p-2 mb-3 rounded"
            />
            <select
              name="status"
              value={subtaskForm.status}
              onChange={handleSubtaskChange}
              className="w-full border p-2 mb-3 rounded"
            >
              <option value="TODO">TODO</option>
              <option value="IN_PROGRESS">IN PROGRESS</option>
              <option value="DONE">DONE</option>
            </select>

            <div className="flex justify-end space-x-4">
              <button
                onClick={() => setShowModal(false)}
                className="bg-gray-400 text-white px-4 py-2 rounded"
              >
                Cancel
              </button>
              <button
                onClick={handleSaveSubtask}
                className="bg-blue-500 text-white px-4 py-2 rounded hover:bg-blue-600"
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

export default TaskList;
