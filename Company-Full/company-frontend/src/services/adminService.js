import { apiClient } from "./authService";

const ADMIN_URL = "/api/admin/users";

// Tüm kullanıcılar
export const getAllUsers = async () => {
  const response = await apiClient.get(ADMIN_URL);
  return response.data;
};

// Admin yap
export const promoteToAdmin = async (id) => {
  const response = await apiClient.patch(`${ADMIN_URL}/${id}/promote`);
  return response.data;
};

// Durum değiştir (ACTIVE / PASSIVE)
export const changeUserStatus = async (id, status) => {
  const response = await apiClient.patch(`${ADMIN_URL}/${id}/status?status=${status}`);
  return response.data;
};
