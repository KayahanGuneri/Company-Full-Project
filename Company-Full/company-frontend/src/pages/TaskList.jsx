import { useEffect, useState } from "react";
import {
  getTasksByProject,
  createTaskForProject,
  deleteTask,
  deleteSubtask
} from "../services/taskService";
import { useParams } from "react-router-dom";
import {
  PlusCircleIcon,
  ChevronDownIcon,
  ChevronUpIcon,
  PencilSquareIcon,
  TrashIcon
} from "@heroicons/react/24/solid";
import TaskAddModal from "../components/TaskAddModal";
import SubtaskAddModal from "../components/SubtaskAddModal";
import SubtaskEditModal from "../components/SubtaskEditModal";

function TaskList() {
  const { projectId } = useParams();
  const [tasks, setTasks] = useState([]);
  const [error, setError] = useState("");
  const [expandedTaskId, setExpandedTaskId] = useState(null);

  const [isAddTaskModalOpen, setIsAddTaskModalOpen] = useState(false);
  const [isAddSubtaskModalOpen, setIsAddSubtaskModalOpen] = useState(false);
  const [isEditSubtaskModalOpen, setIsEditSubtaskModalOpen] = useState(false);

  const [activeTaskId, setActiveTaskId] = useState(null);
  const [editingSubtask, setEditingSubtask] = useState(null);

  const fetchTasks = async () => {
    try {
      const data = await getTasksByProject(projectId);
      setTasks(data);
    } catch {
      setError("Failed to load tasks.");
    }
  };

  useEffect(() => {
    fetchTasks();
  }, [projectId]);

  const toggleTask = (taskId) => {
    setExpandedTaskId((prev) => (prev === taskId ? null : taskId));
  };

  const handleDeleteTask = async (taskId) => {
    if (!window.confirm("Are you sure you want to delete this task?")) return;
    await deleteTask(taskId);
    fetchTasks();
  };

  const handleDeleteSubtask = async (subtaskId) => {
    if (!window.confirm("Are you sure you want to delete this subtask?")) return;
    await deleteSubtask(subtaskId);
    fetchTasks();
  };

  return (
    <div className="min-h-screen w-screen bg-gradient-to-br from-gray-900 to-black text-white p-10">
      <h1 className="text-3xl font-bold mb-6">Project Tasks</h1>
      {error && <p className="text-red-400 mb-4">{error}</p>}

      <button
        onClick={() => setIsAddTaskModalOpen(true)}
        className="flex items-center gap-2 bg-gradient-to-r from-blue-500 to-blue-700 px-4 py-2 rounded-lg mb-6 hover:scale-105 transition"
      >
        <PlusCircleIcon className="w-5 h-5" /> Add Task
      </button>

      <div className="overflow-x-auto">
        <table className="min-w-full bg-white/10 backdrop-blur-md rounded-lg border border-white/20">
          <thead className="bg-white/20">
            <tr>
              <th className="p-3">Task</th>
              <th className="p-3">Assigned User</th>
              <th className="p-3">Status</th>
              <th className="p-3">Actions</th>
            </tr>
          </thead>
          <tbody>
            {tasks.map((task) => (
              <>
                <tr key={task.id} className="hover:bg-white/10 transition">
                  <td className="p-3 font-semibold">{task.title}</td>
                  <td className="p-3">{task.assignedUserFullName || "-"}</td>
                  <td className="p-3">{task.status}</td>
                  <td className="p-3 flex gap-2">
                    <button onClick={() => toggleTask(task.id)}>
                      {expandedTaskId === task.id ? (
                        <ChevronUpIcon className="w-5 h-5 text-blue-400" />
                      ) : (
                        <ChevronDownIcon className="w-5 h-5 text-blue-400" />
                      )}
                    </button>
                    <button
                      onClick={() => {
                        setActiveTaskId(task.id);
                        setIsAddSubtaskModalOpen(true);
                      }}
                      className="text-green-400 hover:underline"
                    >
                      Add Subtask
                    </button>
                    <button
                      onClick={() => handleDeleteTask(task.id)}
                      className="text-red-400 hover:underline"
                    >
                      Delete
                    </button>
                  </td>
                </tr>

                {expandedTaskId === task.id && (
                  <tr>
                    <td colSpan="4" className="p-4 bg-black/30">
                      {task.subTasks?.length > 0 ? (
                        <ul className="space-y-2">
                          {task.subTasks.map((sub) => (
                            <li
                              key={sub.id}
                              className="bg-white/10 p-2 rounded-lg flex justify-between"
                            >
                              <span>
                                {sub.title} -{" "}
                                <span className="text-sm text-gray-400">
                                  {sub.assignedUserFullName || "Unassigned"}
                                </span>
                              </span>
                              <span className="flex gap-2 items-center">
                                <span className="text-sm text-gray-300">
                                  {sub.status}
                                </span>
                                <button
                                  onClick={() => {
                                    setEditingSubtask(sub);
                                    setIsEditSubtaskModalOpen(true);
                                  }}
                                >
                                  <PencilSquareIcon className="w-5 h-5 text-yellow-400" />
                                </button>
                                <button
                                  onClick={() => handleDeleteSubtask(sub.id)}
                                >
                                  <TrashIcon className="w-5 h-5 text-red-400" />
                                </button>
                              </span>
                            </li>
                          ))}
                        </ul>
                      ) : (
                        <p className="text-gray-400 italic">
                          No subtasks found.
                        </p>
                      )}
                    </td>
                  </tr>
                )}
              </>
            ))}
          </tbody>
        </table>
      </div>

      {isAddTaskModalOpen && (
        <TaskAddModal
          projectId={projectId}
          onClose={() => setIsAddTaskModalOpen(false)}
          onSave={fetchTasks}
        />
      )}

      {isAddSubtaskModalOpen && (
        <SubtaskAddModal
          taskId={activeTaskId}
          onClose={() => setIsAddSubtaskModalOpen(false)}
          onSave={fetchTasks}
        />
      )}

      {isEditSubtaskModalOpen && (
        <SubtaskEditModal
          subtask={editingSubtask}
          onClose={() => setIsEditSubtaskModalOpen(false)}
          onSave={fetchTasks}
        />
      )}
    </div>
  );
}

export default TaskList;
