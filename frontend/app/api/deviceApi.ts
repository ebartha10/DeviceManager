import { apiClient } from "./apiClient";

export interface Device {
  id: string;
  name?: string;
  type?: string;
  description?: string;
}

export interface CreateDeviceRequest {
  name?: string;
  type?: string;
  description?: string;
}

export interface UserDeviceRequest {
  userId: string;
  deviceId: string;
}

export const deviceApi = {
  getAllDevices: async (): Promise<Device[]> => {
    return apiClient.get<Device[]>("/device/get-all");
  },

  getDeviceById: async (id: string): Promise<Device> => {
    return apiClient.get<Device>(`/device?id=${id}`);
  },

  createDevice: async (device: CreateDeviceRequest): Promise<Device> => {
    return apiClient.post<Device>("/device", device);
  },

  updateDevice: async (device: Device): Promise<Device> => {
    return apiClient.put<Device>("/device", device);
  },

  deleteDevice: async (id: string): Promise<string> => {
    return apiClient.delete<string>(`/device?id=${id}`);
  },

  getDevicesForUser: async (userId: string): Promise<Device[]> => {
    return apiClient.get<Device[]>(`/device/user-device?userId=${userId}`);
  },

  addDeviceForUser: async (userDevice: UserDeviceRequest): Promise<Device> => {
    return apiClient.post<Device>("/device/user-device", userDevice);
  },

  removeDeviceFromUser: async (userId: string, deviceId: string): Promise<string> => {
    return apiClient.delete<string>(`/device/user-device?userId=${userId}&deviceId=${deviceId}`);
  },
};

