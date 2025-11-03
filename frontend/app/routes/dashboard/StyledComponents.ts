import { styled } from "@mui/material/styles";
import {
  Box,
  Button,
  Card,
  Typography,
  Avatar,
  Link,
  Dialog,
  DialogTitle,
  DialogContent,
  DialogActions,
  TextField,
  Autocomplete,
} from "@mui/material";
import { Link as RouterLink } from "react-router";

// Main layout container
export const DashboardContainer = styled(Box)({
  minHeight: "100vh",
  display: "flex",
  background: "#000000",
});

// Left sidebar
export const Sidebar = styled(Box)({
  width: "280px",
  minHeight: "100vh",
  background: "rgba(0, 0, 0, 0.95)",
  borderRight: "1px solid #00ffff",
  padding: "24px",
  display: "flex",
  flexDirection: "column",
  gap: "32px",
});

// Logo container
export const LogoContainer = styled(Box)({
  display: "flex",
  alignItems: "center",
  gap: "12px",
});

// Logo icon
export const LogoIcon = styled(Box)({
  width: "48px",
  height: "48px",
  borderRadius: "8px",
  background: "linear-gradient(135deg, #ff00ff 0%, #00ffff 100%)",
  display: "flex",
  alignItems: "center",
  justifyContent: "center",
  "& svg": {
    width: "28px",
    height: "28px",
  },
});

// App title
export const AppTitle = styled(Typography)({
  fontFamily: "'Orbitron', sans-serif",
  fontSize: "24px",
  fontWeight: 700,
  color: "white",
  letterSpacing: "1px",
});

// User profile card
export const UserProfileCard = styled(Card)({
  background: "rgba(0, 0, 0, 0.8)",
  borderRadius: "12px",
  border: "1px solid #00ffff",
  padding: "16px",
  display: "flex",
  alignItems: "center",
  gap: "12px",
});

// User avatar
export const UserAvatar = styled(Avatar)({
  width: "48px",
  height: "48px",
  background: "linear-gradient(135deg, #ff00ff 0%, #00ffff 100%)",
  fontFamily: "'Inter', sans-serif",
  fontWeight: 600,
  fontSize: "18px",
});

// User info container
export const UserInfoContainer = styled(Box)({
  display: "flex",
  flexDirection: "column",
  flex: 1,
});

// User name
export const UserName = styled(Typography)({
  fontFamily: "'Inter', sans-serif",
  fontSize: "14px",
  fontWeight: 600,
  color: "white",
});

// User email
export const UserEmail = styled(Typography)({
  fontFamily: "'Inter', sans-serif",
  fontSize: "12px",
  color: "#00ffff",
  marginTop: "2px",
});

// Navigation section
export const NavigationSection = styled(Box)({
  display: "flex",
  flexDirection: "column",
  gap: "8px",
});

// Navigation heading
export const NavigationHeading = styled(Typography)({
  fontFamily: "'Inter', sans-serif",
  fontSize: "11px",
  fontWeight: 600,
  color: "rgba(255, 255, 255, 0.6)",
  textTransform: "uppercase",
  letterSpacing: "1px",
  marginBottom: "8px",
});

// Active nav link (Dashboard)
export const ActiveNavLink = styled(Button)({
  background: "linear-gradient(90deg, #ff00ff 0%, #00ffff 100%)",
  color: "white",
  fontFamily: "'Inter', sans-serif",
  fontSize: "14px",
  fontWeight: 600,
  padding: "12px 16px",
  borderRadius: "8px",
  justifyContent: "flex-start",
  textTransform: "none",
  "&:hover": {
    background: "linear-gradient(90deg, #ff00ff 0%, #00ffff 100%)",
  },
  "& .MuiButton-startIcon": {
    marginRight: "12px",
  },
});

// Inactive nav link
export const InactiveNavLink = styled(Link)({
  color: "rgba(255, 255, 255, 0.7)",
  fontFamily: "'Inter', sans-serif",
  fontSize: "14px",
  fontWeight: 500,
  textDecoration: "none",
  padding: "12px 16px",
  borderRadius: "8px",
  display: "flex",
  alignItems: "center",
  gap: "12px",
  transition: "all 0.2s",
  "&:hover": {
    color: "white",
    background: "rgba(255, 255, 255, 0.05)",
  },
  "& svg": {
    fontSize: "20px",
  },
});

// Router link styled for inactive nav
export const InactiveNavRouterLink = styled(RouterLink)({
  color: "rgba(255, 255, 255, 0.7)",
  fontFamily: "'Inter', sans-serif",
  fontSize: "14px",
  fontWeight: 500,
  textDecoration: "none",
  padding: "12px 16px",
  borderRadius: "8px",
  display: "flex",
  alignItems: "center",
  gap: "12px",
  transition: "all 0.2s",
  "&:hover": {
    color: "white",
    background: "rgba(255, 255, 255, 0.05)",
  },
  "& svg": {
    fontSize: "20px",
  },
});

// Main content area
export const MainContent = styled(Box)({
  flex: 1,
  padding: "40px",
  overflowY: "auto",
  background: "#000000",
});

// Page header container
export const PageHeader = styled(Box)({
  display: "flex",
  justifyContent: "space-between",
  alignItems: "flex-start",
  marginBottom: "32px",
});

// Page title container
export const PageTitleContainer = styled(Box)({
  display: "flex",
  flexDirection: "column",
  gap: "8px",
});

// Page title
export const PageTitle = styled(Typography)({
  fontFamily: "'Orbitron', sans-serif",
  fontSize: "32px",
  fontWeight: 700,
  color: "white",
  letterSpacing: "2px",
});

// Page subtitle
export const PageSubtitle = styled(Typography)({
  fontFamily: "'Inter', sans-serif",
  fontSize: "14px",
  color: "rgba(255, 255, 255, 0.6)",
});

// Primary action button (Add Device)
export const PrimaryActionButton = styled(Button)({
  background: "linear-gradient(90deg, #ff00ff 0%, #00ffff 100%)",
  color: "white",
  fontFamily: "'Inter', sans-serif",
  fontWeight: 600,
  fontSize: "14px",
  textTransform: "uppercase",
  padding: "12px 24px",
  borderRadius: "8px",
  boxShadow: "0 0 20px rgba(255, 0, 255, 0.5)",
  "&:hover": {
    background: "linear-gradient(90deg, #ff00ff 0%, #00ffff 100%)",
    boxShadow: "0 0 30px rgba(255, 0, 255, 0.7)",
  },
  "& .MuiButton-startIcon": {
    marginRight: "8px",
  },
});

// Device cards container
export const DeviceCardsContainer = styled(Box)({
  display: "flex",
  flexDirection: "column",
  gap: "16px",
});

// Device card
export const DeviceCard = styled(Card)({
  background: "rgba(0, 0, 0, 0.8)",
  borderRadius: "12px",
  border: "2px solid",
  padding: "20px",
  display: "flex",
  justifyContent: "space-between",
  alignItems: "center",
  boxShadow: "0 4px 12px rgba(0, 0, 0, 0.3)",
});

// Device card with cyan border
export const DeviceCardCyan = styled(DeviceCard)({
  borderColor: "#00ffff",
});

// Device card with pink border
export const DeviceCardPink = styled(DeviceCard)({
  borderColor: "#ff00ff",
});

// Device card with yellow border
export const DeviceCardYellow = styled(DeviceCard)({
  borderColor: "#ffff00",
});

// Device info container
export const DeviceInfoContainer = styled(Box)({
  display: "flex",
  flexDirection: "column",
  gap: "8px",
  flex: 1,
});

// Device name
export const DeviceName = styled(Typography)({
  fontFamily: "'Inter', sans-serif",
  fontSize: "18px",
  fontWeight: 700,
  color: "white",
});

// Device type
export const DeviceType = styled(Typography)({
  fontFamily: "'Inter', sans-serif",
  fontSize: "14px",
  fontWeight: 500,
});

// Device type with cyan color
export const DeviceTypeCyan = styled(DeviceType)({
  color: "#00ffff",
});

// Device type with pink color
export const DeviceTypePink = styled(DeviceType)({
  color: "#ff00ff",
});

// Device type with yellow color
export const DeviceTypeYellow = styled(DeviceType)({
  color: "#ffff00",
});

// Device metrics container
export const DeviceMetricsContainer = styled(Box)({
  display: "flex",
  flexDirection: "column",
  gap: "8px",
  alignItems: "flex-end",
  marginRight: "24px",
});

// Metric item
export const MetricItem = styled(Box)({
  display: "flex",
  alignItems: "center",
  gap: "8px",
});

// Metric label
export const MetricLabel = styled(Typography)({
  fontFamily: "'Inter', sans-serif",
  fontSize: "14px",
  color: "rgba(255, 255, 255, 0.7)",
});

// Metric value
export const MetricValue = styled(Typography)({
  fontFamily: "'Inter', sans-serif",
  fontSize: "14px",
  fontWeight: 600,
});

// Metric value green
export const MetricValueGreen = styled(MetricValue)({
  color: "#00ff00",
});

// Metric value pink
export const MetricValuePink = styled(MetricValue)({
  color: "#ff00ff",
});

// Metric value yellow
export const MetricValueYellow = styled(MetricValue)({
  color: "#ffff00",
});

// Metric value cyan
export const MetricValueCyan = styled(MetricValue)({
  color: "#00ffff",
});

// Metric dot
export const MetricDot = styled(Box)({
  width: "8px",
  height: "8px",
  borderRadius: "50%",
});

// Metric dot green
export const MetricDotGreen = styled(MetricDot)({
  backgroundColor: "#00ff00",
});

// Metric dot pink
export const MetricDotPink = styled(MetricDot)({
  backgroundColor: "#ff00ff",
});

// Metric dot yellow
export const MetricDotYellow = styled(MetricDot)({
  backgroundColor: "#ffff00",
});

// Metric dot cyan
export const MetricDotCyan = styled(MetricDot)({
  backgroundColor: "#00ffff",
});

// Device actions container
export const DeviceActionsContainer = styled(Box)({
  display: "flex",
  gap: "8px",
});

// Action icon button
export const ActionIconButton = styled(Button)({
  minWidth: "32px",
  width: "32px",
  height: "32px",
  padding: 0,
  borderRadius: "4px",
  "&:hover": {
    opacity: 0.8,
  },
  "& svg": {
    fontSize: "16px",
  },
});

// Edit button cyan
export const EditButtonCyan = styled(ActionIconButton)({
  backgroundColor: "#00ffff",
  color: "white",
  "&:hover": {
    backgroundColor: "#00ccff",
  },
});

// Edit button pink
export const EditButtonPink = styled(ActionIconButton)({
  backgroundColor: "#ff00ff",
  color: "white",
  "&:hover": {
    backgroundColor: "#cc00ff",
  },
});

// Edit button yellow
export const EditButtonYellow = styled(ActionIconButton)({
  backgroundColor: "#ffff00",
  color: "white",
  "&:hover": {
    backgroundColor: "#cccc00",
  },
});

// Delete button
export const DeleteButton = styled(ActionIconButton)({
  backgroundColor: "#ff00ff",
  color: "white",
  "&:hover": {
    backgroundColor: "#cc00ff",
  },
});

// Modal/Dialog styled components
// Dialog container
export const StyledDialog = styled(Dialog)({
  "& .MuiDialog-paper": {
    background: "rgba(0, 0, 0, 0.95)",
    borderRadius: "16px",
    border: "2px solid rgba(0, 255, 255, 0.3)",
    padding: "2px",
  },
  "& .MuiDialog-container": {
    "& .MuiDialog-paper": {
      background: "rgba(0, 0, 0, 0.9)",
      border: "1px solid rgba(0, 255, 255, 0.3)",
    },
  },
});

// Dialog title
export const StyledDialogTitle = styled(DialogTitle)({
  fontFamily: "'Orbitron', sans-serif",
  fontSize: "24px",
  fontWeight: 700,
  color: "white",
  letterSpacing: "1px",
  padding: "24px 24px 16px",
  borderBottom: "1px solid rgba(0, 255, 255, 0.3)",
});

// Dialog content
export const StyledDialogContent = styled(DialogContent)({
  padding: "24px",
  display: "flex",
  flexDirection: "column",
  gap: "16px",
});

// Dialog actions
export const StyledDialogActions = styled(DialogActions)({
  padding: "16px 24px",
  borderTop: "1px solid rgba(0, 255, 255, 0.3)",
  gap: "12px",
});

// Dialog text field
export const DialogTextField = styled(TextField)({
  "& .MuiOutlinedInput-root": {
    background: "rgba(0, 0, 0, 0.6)",
    borderRadius: "8px",
    "& fieldset": {
      borderColor: "#00ffff",
      borderWidth: "1px",
    },
    "&:hover fieldset": {
      borderColor: "#00ffff",
      borderWidth: "1px",
    },
    "&.Mui-focused fieldset": {
      borderColor: "#00ffff",
      borderWidth: "2px",
    },
    "& input": {
      color: "white",
      fontFamily: "'Inter', sans-serif",
      fontSize: "14px",
      padding: "12px 16px",
    },
    "& .MuiInputLabel-root": {
      color: "rgba(255, 255, 255, 0.7)",
    },
  },
  "& .MuiInputLabel-root.Mui-focused": {
    color: "#00ffff",
  },
});

// Dialog button - primary
export const DialogPrimaryButton = styled(Button)({
  background: "linear-gradient(90deg, #00ffff 0%, #ff00ff 100%)",
  color: "white",
  fontFamily: "'Inter', sans-serif",
  fontWeight: 600,
  fontSize: "14px",
  textTransform: "uppercase",
  padding: "12px 24px",
  borderRadius: "8px",
  boxShadow: "0 0 15px rgba(0, 255, 255, 0.5)",
  "&:hover": {
    background: "linear-gradient(90deg, #00ffff 0%, #ff00ff 100%)",
    boxShadow: "0 0 20px rgba(0, 255, 255, 0.7)",
  },
});

// Dialog button - secondary/cancel
export const DialogCancelButton = styled(Button)({
  background: "transparent",
  color: "white",
  fontFamily: "'Inter', sans-serif",
  fontWeight: 600,
  fontSize: "14px",
  textTransform: "uppercase",
  padding: "12px 24px",
  borderRadius: "8px",
  border: "1px solid rgba(255, 255, 255, 0.3)",
  "&:hover": {
    background: "rgba(255, 255, 255, 0.1)",
    border: "1px solid rgba(255, 255, 255, 0.5)",
  },
});

// Dialog autocomplete (for device selection)
export const DialogAutocomplete = styled(Autocomplete)({
  "& .MuiOutlinedInput-root": {
    background: "rgba(0, 0, 0, 0.6)",
    borderRadius: "8px",
    "& fieldset": {
      borderColor: "#00ffff",
      borderWidth: "1px",
    },
    "&:hover fieldset": {
      borderColor: "#00ffff",
      borderWidth: "1px",
    },
    "&.Mui-focused fieldset": {
      borderColor: "#00ffff",
      borderWidth: "2px",
    },
    "& input": {
      color: "white",
      fontFamily: "'Inter', sans-serif",
      fontSize: "14px",
      padding: "12px 16px",
    },
    "& .MuiInputLabel-root": {
      color: "rgba(255, 255, 255, 0.7)",
    },
  },
  "& .MuiInputLabel-root.Mui-focused": {
    color: "#00ffff",
  },
  "& .MuiAutocomplete-popupIndicator": {
    color: "#00ffff",
  },
  "& .MuiAutocomplete-clearIndicator": {
    color: "#00ffff",
  },
  "& .MuiAutocomplete-option": {
    color: "white",
    fontFamily: "'Inter', sans-serif",
    fontSize: "14px",
    "&:hover": {
      backgroundColor: "rgba(0, 255, 255, 0.1)",
    },
    "&[aria-selected='true']": {
      backgroundColor: "rgba(0, 255, 255, 0.2)",
    },
  },
  "& .MuiAutocomplete-listbox": {
    background: "rgba(0, 0, 0, 0.95)",
    border: "1px solid rgba(0, 255, 255, 0.3)",
  },
});

// Device details container
export const DeviceDetailsContainer = styled(Box)({
  marginTop: "16px",
  padding: "12px",
  background: "rgba(0, 255, 255, 0.1)",
  borderRadius: "8px",
});

// Device details label
export const DeviceDetailsLabel = styled(Typography)({
  fontFamily: "'Inter', sans-serif",
  fontSize: "12px",
  color: "rgba(255, 255, 255, 0.7)",
  marginBottom: "4px",
});

// Device details text
export const DeviceDetailsText = styled(Typography)({
  fontFamily: "'Inter', sans-serif",
  fontSize: "14px",
  color: "white",
  marginBottom: "4px",
});

// Logout button
export const LogoutButton = styled(Button)({
  background: "transparent",
  color: "#ff00ff",
  fontFamily: "'Inter', sans-serif",
  fontSize: "14px",
  fontWeight: 600,
  textTransform: "uppercase",
  padding: "12px 16px",
  borderRadius: "8px",
  border: "1px solid #ff00ff",
  marginTop: "auto",
  "&:hover": {
    background: "rgba(255, 0, 255, 0.1)",
    border: "1px solid #ff00ff",
    boxShadow: "0 0 15px rgba(255, 0, 255, 0.5)",
  },
  "& .MuiButton-startIcon": {
    marginRight: "8px",
  },
});

