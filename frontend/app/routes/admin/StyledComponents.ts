import { styled } from "@mui/material/styles";
import {
  Box,
  Button,
  Card,
  Typography,
  Avatar,
  TextField,
  Table,
  TableBody,
  TableCell,
  TableContainer,
  TableHead,
  TableRow,
  Chip,
  Dialog,
  DialogTitle,
  DialogContent,
  DialogActions,
  Autocomplete,
} from "@mui/material";
import { Link as RouterLink } from "react-router";

// Main layout container
export const AdminContainer = styled(Box)({
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
  flexDirection: "column",
  gap: "4px",
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
  marginBottom: "8px",
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

// App subtitle
export const AppSubtitle = styled(Typography)({
  fontFamily: "'Inter', sans-serif",
  fontSize: "11px",
  color: "#ff00ff",
  fontWeight: 500,
  marginTop: "-4px",
});

// Admin profile card wrapper
export const AdminProfileCardWrapper = styled(Box)({
  position: "relative",
  padding: "2px",
  borderRadius: "14px",
  background: "linear-gradient(135deg, #ff00ff 0%, #ff00ff 50%, #00ffff 100%)",
});

// Admin profile card
export const AdminProfileCard = styled(Card)({
  background: "rgba(0, 0, 0, 0.9)",
  borderRadius: "12px",
  border: "none",
  padding: "16px",
  display: "flex",
  alignItems: "center",
  gap: "12px",
});

// Admin avatar
export const AdminAvatar = styled(Avatar)({
  width: "48px",
  height: "48px",
  background: "linear-gradient(135deg, #ff8800 0%, #ffff00 100%)",
  fontFamily: "'Inter', sans-serif",
  fontWeight: 600,
  fontSize: "18px",
});

// Admin info container
export const AdminInfoContainer = styled(Box)({
  display: "flex",
  flexDirection: "column",
  flex: 1,
});

// Admin name
export const AdminName = styled(Typography)({
  fontFamily: "'Inter', sans-serif",
  fontSize: "14px",
  fontWeight: 600,
  color: "white",
});

// Admin email
export const AdminEmail = styled(Typography)({
  fontFamily: "'Inter', sans-serif",
  fontSize: "12px",
  color: "#ff00ff",
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
  background: "linear-gradient(90deg, #00ffff 0%, #ff00ff 100%)",
  color: "white",
  fontFamily: "'Inter', sans-serif",
  fontSize: "14px",
  fontWeight: 600,
  padding: "12px 16px",
  borderRadius: "8px",
  justifyContent: "flex-start",
  textTransform: "none",
  "&:hover": {
    background: "linear-gradient(90deg, #00ffff 0%, #ff00ff 100%)",
  },
  "& .MuiButton-startIcon": {
    marginRight: "12px",
  },
});

// Inactive nav router link
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

// Action buttons container
export const ActionButtonsContainer = styled(Box)({
  display: "flex",
  gap: "12px",
});

// Primary action button (Add User)
export const AddUserButton = styled(Button)({
  background: "linear-gradient(90deg, #00ff00 0%, #00ffff 100%)",
  color: "white",
  fontFamily: "'Inter', sans-serif",
  fontWeight: 600,
  fontSize: "14px",
  textTransform: "uppercase",
  padding: "12px 24px",
  borderRadius: "8px",
  boxShadow: "0 0 20px rgba(0, 255, 255, 0.5)",
  "&:hover": {
    background: "linear-gradient(90deg, #00ff00 0%, #00ffff 100%)",
    boxShadow: "0 0 30px rgba(0, 255, 255, 0.7)",
  },
  "& .MuiButton-startIcon": {
    marginRight: "8px",
  },
});

// Secondary action button (Add Device)
export const AddDeviceButton = styled(Button)({
  background: "linear-gradient(90deg, #ff00ff 0%, #ffff00 100%)",
  color: "white",
  fontFamily: "'Inter', sans-serif",
  fontWeight: 600,
  fontSize: "14px",
  textTransform: "uppercase",
  padding: "12px 24px",
  borderRadius: "8px",
  boxShadow: "0 0 20px rgba(255, 0, 255, 0.5)",
  "&:hover": {
    background: "linear-gradient(90deg, #ff00ff 0%, #ffff00 100%)",
    boxShadow: "0 0 30px rgba(255, 0, 255, 0.7)",
  },
  "& .MuiButton-startIcon": {
    marginRight: "8px",
  },
});

// Summary cards container
export const SummaryCardsContainer = styled(Box)({
  display: "grid",
  gridTemplateColumns: "repeat(4, 1fr)",
  gap: "16px",
  marginBottom: "32px",
  
});

// Summary card
export const SummaryCard = styled(Card)({
  background: "rgba(0, 0, 0, 0.8)",
  borderRadius: "12px",
  border: "2px solid",
  padding: "24px",
  display: "flex",
  flexDirection: "column",
  gap: "12px",
});

// Summary card teal
export const SummaryCardTeal = styled(SummaryCard)({
  borderColor: "#00ffff",
});

// Summary card pink
export const SummaryCardPink = styled(SummaryCard)({
  borderColor: "#ff00ff",
});

// Summary card yellow
export const SummaryCardYellow = styled(SummaryCard)({
  borderColor: "#ffff00",
});

// Summary card green
export const SummaryCardGreen = styled(SummaryCard)({
  borderColor: "#00ff00",
});

// Summary icon container
export const SummaryIconContainer = styled(Box)({
  display: "flex",
  alignItems: "center",
  gap: "12px",
});

// Summary icon
export const SummaryIcon = styled(Box)({
  width: "40px",
  height: "40px",
  display: "flex",
  alignItems: "center",
  justifyContent: "center",
  "& svg": {
    fontSize: "24px",
  },
});

// Summary value
export const SummaryValue = styled(Typography)({
  fontFamily: "'Orbitron', sans-serif",
  fontSize: "32px",
  fontWeight: 700,
  color: "white",
  letterSpacing: "1px",
});

// Summary label
export const SummaryLabel = styled(Typography)({
  fontFamily: "'Inter', sans-serif",
  fontSize: "14px",
  color: "rgba(255, 255, 255, 0.7)",
});

// Tabs and search container
export const TabsSearchContainer = styled(Box)({
  display: "flex",
  justifyContent: "space-between",
  alignItems: "center",
  marginBottom: "24px",
  gap: "16px",
});

// Tabs container
export const TabsContainer = styled(Box)({
  display: "flex",
  gap: "8px",
});

// Active tab
export const ActiveTab = styled(Button)({
  background: "#00ff00",
  color: "white",
  fontFamily: "'Inter', sans-serif",
  fontSize: "14px",
  fontWeight: 600,
  textTransform: "uppercase",
  padding: "12px 24px",
  borderRadius: "8px",
  "&:hover": {
    background: "#00cc00",
  },
});

// Inactive tab
export const InactiveTab = styled(Button)({
  background: "transparent",
  color: "white",
  fontFamily: "'Inter', sans-serif",
  fontSize: "14px",
  fontWeight: 600,
  textTransform: "uppercase",
  padding: "12px 24px",
  borderRadius: "8px",
  border: "1px solid rgba(255, 255, 255, 0.3)",
  "&:hover": {
    background: "rgba(255, 255, 255, 0.1)",
    border: "1px solid rgba(255, 255, 255, 0.5)",
  },
});

// Search field
export const SearchField = styled(TextField)({
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
    "& .MuiInputAdornment-root": {
      color: "#00ffff",
      marginLeft: "8px",
      "& svg": {
        fontSize: "20px",
      },
    },
  },
  "& .MuiInputLabel-root": {
    display: "none",
  },
});

// Table container
export const StyledTableContainer = styled(TableContainer)({
  background: "rgba(0, 0, 0, 0.6)",
  borderRadius: "12px",
  border: "1px solid rgba(0, 255, 255, 0.3)",
});

// Table
export const StyledTable = styled(Table)({
  "& .MuiTableCell-root": {
    borderBottom: "1px solid rgba(0, 255, 255, 0.2)",
    fontFamily: "'Inter', sans-serif",
  },
});

// Table header cell
export const StyledTableHeadCell = styled(TableCell)({
  fontFamily: "'Inter', sans-serif",
  fontSize: "14px",
  fontWeight: 600,
  color: "#00ffff",
  textTransform: "uppercase",
  letterSpacing: "1px",
  padding: "16px",
});

// Table body cell
export const StyledTableCell = styled(TableCell)({
  fontFamily: "'Inter', sans-serif",
  fontSize: "14px",
  color: "white",
  padding: "16px",
});

// Status chip active
export const StatusChipActive = styled(Chip)({
  background: "#00ff00",
  color: "white",
  fontFamily: "'Inter', sans-serif",
  fontWeight: 600,
  fontSize: "12px",
  "& .MuiChip-icon": {
    color: "white",
  },
});

// Status chip suspended
export const StatusChipSuspended = styled(Chip)({
  background: "#ff00ff",
  color: "white",
  fontFamily: "'Inter', sans-serif",
  fontWeight: 600,
  fontSize: "12px",
  "& .MuiChip-icon": {
    color: "white",
  },
});

// Status chip pending
export const StatusChipPending = styled(Chip)({
  background: "#ffff00",
  color: "black",
  fontFamily: "'Inter', sans-serif",
  fontWeight: 600,
  fontSize: "12px",
  "& .MuiChip-icon": {
    color: "black",
  },
});

// Actions container
export const ActionsContainer = styled(Box)({
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
  border: "1px solid",
  "&:hover": {
    opacity: 0.8,
  },
  "& svg": {
    fontSize: "16px",
  },
});

// Edit action button
export const EditActionButton = styled(ActionIconButton)({
  borderColor: "#00ffff",
  color: "#00ffff",
  "&:hover": {
    backgroundColor: "rgba(0, 255, 255, 0.1)",
  },
});

// View action button
export const ViewActionButton = styled(ActionIconButton)({
  borderColor: "#00ffff",
  color: "#00ffff",
  "&:hover": {
    backgroundColor: "rgba(0, 255, 255, 0.1)",
  },
});

// Delete action button
export const DeleteActionButton = styled(ActionIconButton)({
  borderColor: "#ff00ff",
  color: "#ff00ff",
  "&:hover": {
    backgroundColor: "rgba(255, 0, 255, 0.1)",
  },
});

// Status dot
export const StatusDot = styled(Box)({
  width: "8px",
  height: "8px",
  borderRadius: "50%",
  marginRight: "8px",
});

// Status dot green
export const StatusDotGreen = styled(StatusDot)({
  backgroundColor: "#00ff00",
});

// Status dot pink
export const StatusDotPink = styled(StatusDot)({
  backgroundColor: "#ff00ff",
});

// Status dot yellow
export const StatusDotYellow = styled(StatusDot)({
  backgroundColor: "#ffff00",
});

// Modal/Dialog styled components
// Dialog container
export const StyledDialog = styled(Dialog)({
  "& .MuiDialog-paper": {
    background: "rgba(0, 0, 0, 0.95)",
    borderRadius: "16px",
    border: "2px solid",
    borderImage: "linear-gradient(135deg, #00ffff 0%, #ff00ff 100%) 1",
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
  marginTop: "12px",
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

// Dialog Autocomplete
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
});

// User info section in dialog
export const UserInfoSection = styled(Box)({
  display: "flex",
  flexDirection: "column",
  gap: "16px",
  paddingBottom: "24px",
  borderBottom: "1px solid rgba(0, 255, 255, 0.3)",
  marginBottom: "24px",
});

// User name in dialog
export const DialogUserName = styled(Typography)({
  fontFamily: "'Orbitron', sans-serif",
  fontSize: "20px",
  fontWeight: 600,
  color: "white",
  letterSpacing: "1px",
});

// User email in dialog
export const DialogUserEmail = styled(Typography)({
  fontFamily: "'Inter', sans-serif",
  fontSize: "14px",
  color: "rgba(255, 255, 255, 0.7)",
});

// Device list section title
export const DeviceListTitle = styled(Typography)({
  fontFamily: "'Orbitron', sans-serif",
  fontSize: "16px",
  fontWeight: 600,
  color: "#00ffff",
  letterSpacing: "1px",
  marginBottom: "16px",
});

// Device list container
export const DeviceListContainer = styled(Box)({
  display: "flex",
  flexDirection: "column",
  gap: "12px",
  maxHeight: "300px",
  overflowY: "auto",
  "&::-webkit-scrollbar": {
    width: "8px",
  },
  "&::-webkit-scrollbar-track": {
    background: "rgba(0, 0, 0, 0.3)",
    borderRadius: "4px",
  },
  "&::-webkit-scrollbar-thumb": {
    background: "rgba(0, 255, 255, 0.5)",
    borderRadius: "4px",
    "&:hover": {
      background: "rgba(0, 255, 255, 0.7)",
    },
  },
});

// Device item card
export const DeviceItemCard = styled(Card)({
  background: "rgba(0, 0, 0, 0.6)",
  border: "1px solid rgba(0, 255, 255, 0.2)",
  borderRadius: "8px",
  padding: "16px",
  display: "flex",
  justifyContent: "space-between",
  alignItems: "center",
  "&:hover": {
    border: "1px solid rgba(255, 0, 255, 0.5)",
    boxShadow: "0 0 10px rgba(255, 0, 255, 0.3)",
  },
});

// Device item info
export const DeviceItemInfo = styled(Box)({
  display: "flex",
  flexDirection: "column",
  gap: "4px",
  flex: 1,
});

// Device item name
export const DeviceItemName = styled(Typography)({
  fontFamily: "'Inter', sans-serif",
  fontSize: "14px",
  fontWeight: 600,
  color: "white",
});

// Device item type
export const DeviceItemType = styled(Typography)({
  fontFamily: "'Inter', sans-serif",
  fontSize: "12px",
  color: "rgba(255, 255, 255, 0.6)",
});

// Empty devices message
export const EmptyDevicesMessage = styled(Typography)({
  fontFamily: "'Inter', sans-serif",
  fontSize: "14px",
  color: "rgba(255, 255, 255, 0.5)",
  textAlign: "center",
  padding: "32px",
  fontStyle: "italic",
});

