import { apiClient } from "./apiClient";
import { tokenStorage } from "./tokenStorage";

export interface AuthenticationRequest {
  email: string;
  password: string;
}

export interface RegisterRequest {
  email: string;
  password: string;
  fullName: string;
  role?: string;
}

export interface AuthenticationResponse {
  token: string;
  userId: string;
}

export const authApi = {
  login: async (credentials: AuthenticationRequest): Promise<AuthenticationResponse> => {
    const response = await apiClient.post<AuthenticationResponse>(
      "/api/auth/login",
      credentials,
      { skipAuth: true }
    );
    if (response.token && response.userId) {
      tokenStorage.setToken(response.token, response.userId);
    }
    return response;
  },

  register: async (data: RegisterRequest): Promise<AuthenticationResponse> => {
    const response = await apiClient.post<AuthenticationResponse>(
      "/api/auth/register",
      data,
      { skipAuth: true }
    );
    if (response.token && response.userId) {
      tokenStorage.setToken(response.token, response.userId);
    }
    return response;
  },

  logout: () => {
    tokenStorage.clearToken();
  },

  isAuthenticated: (): boolean => {
    return tokenStorage.hasToken();
  },
};

