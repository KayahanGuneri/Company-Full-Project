import { apiClient } from "./authService"; // Token içeren axios instance

// Tüm proje deneyimlerini getir (GET)
export const getAllExperiences = async () => {
  const response = await apiClient.get("/api/experiences/selectallprojectexp");
  return response.data;
};

// Çalışan ID'sine göre proje deneyimlerini getir (GET)
export const getExperiencesByEmployee = async (employeeId) => {
  const response = await apiClient.get(`/api/experiences/projects/${employeeId}`);
  return response.data;
};

// Yeni proje deneyimi ekle (POST)
export const addExperience = async (experienceData) => {
  const response = await apiClient.post("/api/experiences/insertproject", experienceData);
  return response.data;
};

// Var olan proje deneyimini güncelle (PUT) - proje bilgisi için
export const updateProjectExperience = async (projectId, projectData) => {
  const response = await apiClient.put(`/api/experiences/updateproject/${projectId}`, projectData, {
    headers: { "Content-Type": "application/json" },
  });
  return response.data;
};

// Çalışanın proje atamasını (assignment) güncelle (PUT) - Tarih güncelleme için
export const updateProjectAssignment = async (assignmentData) => {
  const response = await apiClient.put(`/api/experiences/assignments/update-dates`, assignmentData, {
    headers: { "Content-Type": "application/json" },
  });
  return response.data;
};



export const assignEmployeeToProject = async (assignmentData) => {
  const response = await apiClient.post(`/api/experiences/assignment`, assignmentData, {
    headers: { "Content-Type": "application/json" },
  });
  return response.data;
};



