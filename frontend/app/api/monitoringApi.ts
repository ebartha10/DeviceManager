import { apiClient } from "./apiClient";

export interface HourlyConsumption {
  hourTimestamp: string;
  consumption: number;
}

export interface DailyEnergyConsumption {
  deviceId: string;
  date: string;
  totalConsumption: number;
  hourlyConsumptions: HourlyConsumption[];
}

export interface RealtimeConsumption {
  deviceId: string;
  timestamp: string;
  consumption: number;
  hourTimestamp: string;
  hourlyTotal: number;
}

export const monitoringApi = {
  getDailyConsumption: async (
    deviceId: string,
    date: string
  ): Promise<DailyEnergyConsumption> => {
    // Monitoring service is accessible at /monitoring via Traefik
    return apiClient.get<DailyEnergyConsumption>(
      `/monitoring/energy-consumption/daily?deviceId=${deviceId}&date=${date}`
    );
  },
};

