import { apiClient } from "./apiClient";

export interface User {
  id: string;
  fullName: string;
  email: string;
}

export interface CreateUserRequest {
  fullName: string;
  email: string;
}

export const userApi = {
  getAllUsers: async (): Promise<User[]> => {
    return apiClient.get<User[]>("/users/get-all");
  },

  getUserById: async (id: string): Promise<User> => {
    return apiClient.get<User>(`/users?id=${id}`);
  },

  createUser: async (user: CreateUserRequest): Promise<User> => {
    return apiClient.post<User>("/users", user);
  },

  updateUser: async (user: User): Promise<User> => {
    return apiClient.put<User>("/users", user);
  },

  deleteUser: async (id: string): Promise<string> => {
    return apiClient.delete<string>(`/users?id=${id}`);
  },
};

