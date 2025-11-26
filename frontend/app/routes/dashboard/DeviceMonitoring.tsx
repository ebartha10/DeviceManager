import { useState, useEffect, useRef } from "react";
import {
  LineChart,
  Line,
  XAxis,
  YAxis,
  CartesianGrid,
  Tooltip,
  Legend,
  ResponsiveContainer,
} from "recharts";
import { ArrowBack as ArrowBackIcon } from "@mui/icons-material";
import { monitoringApi, type DailyEnergyConsumption, type RealtimeConsumption } from "../../api/monitoringApi";
import { websocketClient } from "../../api/websocketClient";
import type { Device } from "../../api/deviceApi";
import {
  MonitoringContainer,
  MonitoringHeader,
  MonitoringTitle,
  MonitoringBackButton,
  MonitoringHeaderContent,
  ChartContainer,
  DatePickerContainer,
  DatePickerButton,
  DatePickerTextField,
  BackToRealtimeButton,
  EmptyChartContainer,
  EmptyChartText,
} from "./StyledComponents";

interface DeviceMonitoringProps {
  device: Device;
  onClose: () => void;
}

interface ChartDataPoint {
  hour: string;
  consumption: number;
  timestamp?: string;
}

// Initialize with all 24 hours (00:00 to 23:50)
// Last hour is 23:50 to match the requirement
const initializeFullDayData = (): ChartDataPoint[] => {
  const fullDay: ChartDataPoint[] = [];
  for (let i = 0; i < 24; i++) {
    const hourLabel = i === 23 ? "23:50" : i.toString().padStart(2, "0") + ":00";
    fullDay.push({
      hour: hourLabel,
      consumption: 0,
    });
  }
  return fullDay;
};

export function DeviceMonitoring({ device, onClose }: DeviceMonitoringProps) {
  const [realtimeData, setRealtimeData] = useState<ChartDataPoint[]>(initializeFullDayData());
  const [dailyData, setDailyData] = useState<ChartDataPoint[]>([]);
  const [selectedDate, setSelectedDate] = useState<string>(
    new Date().toISOString().split("T")[0]
  );
  const [isLoadingDaily, setIsLoadingDaily] = useState(false);
  const [viewMode, setViewMode] = useState<"realtime" | "daily">("realtime");
  const unsubscribeRef = useRef<(() => void) | null>(null);
  const currentDayRef = useRef<string>(new Date().toISOString().split("T")[0]);
  const lastHourRef = useRef<number>(new Date().getHours());

  useEffect(() => {
    // Initialize with current day
    currentDayRef.current = new Date().toISOString().split("T")[0];
    lastHourRef.current = new Date().getHours();
    setRealtimeData(initializeFullDayData());

    // Connect and subscribe to WebSocket
    websocketClient
      .connect()
      .then(() => {
        const unsubscribe = websocketClient.subscribeToDevice(
          device.id,
          (data: RealtimeConsumption) => {
            setRealtimeData((prev) => {
              const timestamp = new Date(data.hourTimestamp);
              const dataDate = timestamp.toISOString().split("T")[0];
              const dataHour = timestamp.getHours();
              const hourLabel = dataHour === 23 ? "23:50" : dataHour.toString().padStart(2, "0") + ":00";
              
              if (dataDate !== currentDayRef.current) {
                currentDayRef.current = dataDate;
                lastHourRef.current = dataHour;
                const clearedData = initializeFullDayData();
                clearedData[dataHour] = {
                  hour: hourLabel,
                  consumption: data.hourlyTotal,
                  timestamp: data.timestamp,
                };
                return clearedData;
              }

              if (lastHourRef.current === 23 && dataHour === 0) {
                currentDayRef.current = dataDate;
                lastHourRef.current = dataHour;
                const clearedData = initializeFullDayData();
                clearedData[dataHour] = {
                  hour: hourLabel,
                  consumption: data.hourlyTotal,
                  timestamp: data.timestamp,
                };
                return clearedData;
              }

              lastHourRef.current = dataHour;

              const updated = initializeFullDayData();
              
              prev.forEach((point) => {
                const hourStr = point.hour.split(":")[0];
                const idx = parseInt(hourStr);
                if (idx >= 0 && idx < 24) {
                  updated[idx] = { ...point }; // Preserve existing data
                }
              });

              updated[dataHour] = {
                hour: hourLabel,
                consumption: data.hourlyTotal,
                timestamp: data.timestamp,
              };

              return updated;
            });
          }
        );
        unsubscribeRef.current = unsubscribe;
      })
      .catch((error) => {
        console.error("Failed to connect WebSocket:", error);
      });

    return () => {
      if (unsubscribeRef.current) {
        unsubscribeRef.current();
      }
    };
  }, [device.id]);

  const loadDailyData = async () => {
    setIsLoadingDaily(true);
    try {
      const data: DailyEnergyConsumption = await monitoringApi.getDailyConsumption(
        device.id,
        selectedDate
      );

      const chartData: ChartDataPoint[] = data.hourlyConsumptions.map((hc) => ({
        hour: new Date(hc.hourTimestamp).getHours().toString().padStart(2, "0") + ":00",
        consumption: hc.consumption,
      }));

      // Fill in missing hours with 0 consumption
      const fullDayData: ChartDataPoint[] = [];
      for (let i = 0; i < 24; i++) {
        const hour = i.toString().padStart(2, "0") + ":00";
        const existing = chartData.find((d) => d.hour === hour);
        fullDayData.push(
          existing || {
            hour,
            consumption: 0,
          }
        );
      }

      setDailyData(fullDayData);
      setViewMode("daily");
    } catch (error) {
      console.error("Error loading daily data:", error);
      setDailyData([]);
    } finally {
      setIsLoadingDaily(false);
    }
  };

  // For real-time view, always show all 24 hours, for daily view use dailyData
  const currentData = viewMode === "realtime" 
    ? realtimeData.length === 24 
      ? realtimeData 
      : initializeFullDayData()
    : dailyData;
  
  // In real-time mode, always show the graph (even if all zeros)
  // In daily mode, only show if there's data
  const shouldShowEmpty = viewMode === "daily" && (!currentData.length || !currentData.some((d) => d.consumption > 0));

  return (
    <MonitoringContainer>
      <MonitoringHeader>
        <MonitoringHeaderContent>
          <MonitoringBackButton startIcon={<ArrowBackIcon />} onClick={onClose}>
            Back
          </MonitoringBackButton>
          <MonitoringTitle>
            {device.name || "Unnamed Device"} - Energy Consumption
          </MonitoringTitle>
        </MonitoringHeaderContent>
        <DatePickerContainer>
          <DatePickerTextField
            type="date"
            value={selectedDate}
            onChange={(e) => setSelectedDate(e.target.value)}
            InputLabelProps={{
              shrink: true,
            }}
          />
          <DatePickerButton
            variant="contained"
            onClick={loadDailyData}
            disabled={isLoadingDaily}
          >
            View Daily Consumption
          </DatePickerButton>
          {viewMode === "daily" && (
            <BackToRealtimeButton
              variant="outlined"
              onClick={() => setViewMode("realtime")}
            >
              Back to Real-time
            </BackToRealtimeButton>
          )}
        </DatePickerContainer>
      </MonitoringHeader>

      <ChartContainer>
        {shouldShowEmpty ? (
          <EmptyChartContainer>
            <EmptyChartText>
              No consumption data available for the selected date.
            </EmptyChartText>
          </EmptyChartContainer>
        ) : (
          <ResponsiveContainer width="100%" height="100%">
            <LineChart data={currentData}>
              <CartesianGrid strokeDasharray="3 3" stroke="#00ffff" opacity={0.3} />
              <XAxis
                dataKey="hour"
                stroke="#00ffff"
                style={{ fontSize: "12px" }}
                type="category"
                interval={viewMode === "realtime" ? 3 : 1}
                angle={viewMode === "realtime" ? -45 : 0}
                textAnchor={viewMode === "realtime" ? "end" : "middle"}
                height={viewMode === "realtime" ? 80 : 30}
              />
              <YAxis
                stroke="#00ffff"
                style={{ fontSize: "12px" }}
                label={{
                  value: "Energy (kWh)",
                  angle: -90,
                  position: "insideLeft",
                  style: { textAnchor: "middle", fill: "#00ffff" },
                }}
              />
              <Tooltip
                contentStyle={{
                  backgroundColor: "rgba(0, 0, 0, 0.9)",
                  border: "1px solid #00ffff",
                  color: "#00ffff",
                }}
                labelStyle={{ color: "#00ffff" }}
              />
              <Legend
                wrapperStyle={{ color: "#00ffff" }}
              />
              <Line
                type="monotone"
                dataKey="consumption"
                stroke="#00ffff"
                strokeWidth={2}
                dot={{ fill: "#00ffff", r: 4 }}
                name="Energy Consumption (kWh)"
              />
            </LineChart>
          </ResponsiveContainer>
        )}
      </ChartContainer>
    </MonitoringContainer>
  );
}

