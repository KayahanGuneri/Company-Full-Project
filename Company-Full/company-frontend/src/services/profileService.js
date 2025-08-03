import { apiClient } from "./authService";

export const getProfile = async () => {
    const response = await apiClient.get("/api/profile");
    return response.data;
};