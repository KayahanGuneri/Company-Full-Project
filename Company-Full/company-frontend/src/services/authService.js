import axios from "axios";

// Backend adresi
const BASE_URL = "http://localhost:8080";

// Axios instance
const api = axios.create({
  baseURL: BASE_URL,
});

// Her isteğe token ekle
api.interceptors.request.use((config) => {
  const token = localStorage.getItem("token");
  if (token) {
    config.headers.Authorization = `Bearer ${token}`;
  }
  return config;
});

// Yanıt interceptor → 403 olursa otomatik logout
api.interceptors.response.use(
  (response) => response,
  (error) => {
    if (error.response?.status === 403) {
      alert("Oturumunuz sonlandırıldı. Hesabınız pasif olabilir.");
      localStorage.removeItem("token");
      localStorage.removeItem("role");
      localStorage.removeItem("email");
      window.location.href = "/login";
    }
    return Promise.reject(error);
  }
);

// Auth fonksiyonları
export const login = async (credentials) => {
  const response = await api.post("/api/auth/login", credentials);

  // Token ve kullanıcı bilgilerini sakla
  localStorage.setItem("token", response.data.token);
  localStorage.setItem("role", response.data.role || "USER");
  localStorage.setItem("email", response.data.email || "");

  return response.data;
};

export const register = async (userData) => {
  const response = await api.post("/api/auth/register", userData);
  return response.data;
};

// Genel API client export
export const apiClient = api;

export const getToken = () => {
  return localStorage.getItem("token");
};
