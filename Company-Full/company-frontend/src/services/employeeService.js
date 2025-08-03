import { apiClient } from "./authService"; // axios + token ile

const EMPLOYEE_URL = "/api/employees";

// Tüm çalışanları getir
export const getEmployees = async () => {
  const response = await apiClient.get(`${EMPLOYEE_URL}/all`);
  return response.data;
};

// Tek bir çalışanı ID ile getir
export const getEmployeeById = async (id) => {
  const response = await apiClient.get(`${EMPLOYEE_URL}/${id}`);
  return response.data;
};

// Yeni çalışan ekle
export const addEmployee = async (employee) => {
  const response = await apiClient.post(`${EMPLOYEE_URL}/save`, employee);
  return response.data;
};

// Çalışanı güncelle (PUT)
export const updateEmployee = async (id, employee) => {
  const response = await apiClient.put(`${EMPLOYEE_URL}/${id}`, employee);
  return response.data;
};

// Çalışanı sil (ADMIN only)
export const deleteEmployee = async (id) => {
  await apiClient.delete(`${EMPLOYEE_URL}/delete/${id}`);
};

export const getActiveEmployees = async () => {
  const response = await apiClient.get(`${EMPLOYEE_URL}/active`);
  return response.data;
};