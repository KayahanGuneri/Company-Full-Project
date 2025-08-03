// src/services/taskService.js
import { apiClient } from "./authService"; // axios instance with JWT

const BASE_URL = "/api/tasks";

/**
 * Fetch all tasks by project ID.
 * GET /project/{projectId}
 */
export const getTasksByProject = async (projectId) => {
  const response = await apiClient.get(`${BASE_URL}/project/${projectId}`);
  return response.data;
};

/**
 * Create a new task for a project.
 * POST /project/{projectId}
 */
export const createTaskForProject = async (projectId, taskData) => {
  const response = await apiClient.post(`${BASE_URL}/project/${projectId}`, taskData);
  return response.data;
};

/**
 * Update an existing task (title, assigned user, status).
 * PUT /{taskId}
 */
export const updateTask = async (taskId, taskData) => {
  const response = await apiClient.put(`${BASE_URL}/${taskId}`, taskData);
  return response.data;
};

/**
 * Create a new subtask for a given task.
 * POST /{taskId}/subtask
 */
export const createSubtask = async (taskId, subtaskData) => {
  const response = await apiClient.post(`${BASE_URL}/${taskId}/subtasks`, subtaskData);
  return response.data;
};

/**
 * Update an existing subtask.
 * PUT /subtasks/{subtaskId}
 */
export const updateSubtask = async (subtaskId, subtaskData) => {
  const response = await apiClient.put(`${BASE_URL}/subtasks/${subtaskId}`, subtaskData);
  return response.data;
};

/**
 * Delete a task.
 * DELETE /{taskId}
 */
export const deleteTask = async (taskId) => {
  const response = await apiClient.delete(`${BASE_URL}/${taskId}`);
  return response.data;
};

/**
 * Delete a subtask.
 * DELETE /subtasks/{subtaskId}
 */
export const deleteSubtask = async (subtaskId) => {
  await apiClient.delete(`${BASE_URL}/subtasks/${subtaskId}`);
};


export const createTask = async (projectId, taskData) => {
  const response = await apiClient.post(`${BASE_URL}/project/${projectId}`, taskData);
  return response.data;
};
