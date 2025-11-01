import { styled } from "@mui/material/styles";
import {
  Box,
  Button,
  TextField,
  Card,
  Typography,
  Link,
  Alert,
  Container,
} from "@mui/material";
import { Email as EmailIcon } from "@mui/icons-material";
import { Link as RouterLink } from "react-router";

// Page container
export const PageContainer = styled(Box)({
  minHeight: '100vh',
  display: "flex",
  alignItems: "center",
  justifyContent: "center",
  background: "#000000",
  padding: "24px",
});

// Container wrapper
export const ContainerWrapper = styled(Container)({
  maxWidth: "sm",
});

// Gradient border wrapper
export const NexusCardWrapper = styled(Box)({
  position: "relative",
  padding: "2px",
  borderRadius: "18px",
  background: "linear-gradient(180deg, #00ffff 0%, #ff00ff 100%)",
  maxWidth: "484px",
  width: "100%",
});

// Main card container with neon gradient border
export const NexusCard = styled(Card)(({ theme }) => ({
  background: "rgba(0, 0, 0, 0.9)",
  borderRadius: "16px",
  padding: "48px 40px",
  width: "100%",
  position: "relative",
  border: "none",
  boxShadow: `
    inset 0 0 20px rgba(0, 255, 255, 0.1),
    0 0 20px rgba(0, 255, 255, 0.3),
    0 0 40px rgba(255, 0, 255, 0.2)
  `,
  [theme.breakpoints.down("sm")]: {
    padding: "32px 24px",
  },
}));

// Tab button - Active state
export const ActiveTab = styled(Button)(({ theme }) => ({
  background: "linear-gradient(90deg, #ff00ff 0%, #00ffff 100%)",
  color: "white",
  fontFamily: "'Inter', sans-serif",
  fontWeight: 600,
  fontSize: "14px",
  textTransform: "uppercase",
  padding: "12px 24px",
  borderRadius: "8px",
  boxShadow: "0 0 15px rgba(255, 0, 255, 0.5)",
  "&:hover": {
    background: "linear-gradient(90deg, #ff00ff 0%, #00ffff 100%)",
    boxShadow: "0 0 20px rgba(255, 0, 255, 0.7)",
  },
}));

// Tab button - Inactive state
export const InactiveTab = styled(Button)(({ theme }) => ({
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
}));

// Tab navigation container
export const TabContainer = styled(Box)({
  display: "flex",
  gap: "8px",
  marginTop: "32px",
});

// Logo container
export const LogoContainer = styled(Box)({
  display: "flex",
  alignItems: "center",
  gap: "12px",
  marginBottom: "8px",
});

// Shield logo SVG container
export const ShieldIcon = styled(Box)({
  width: "40px",
  height: "40px",
  display: "flex",
  alignItems: "center",
  justifyContent: "center",
  background: "#00ffff",
  borderRadius: "8px",
  "& svg": {
    width: "24px",
    height: "24px",
  },
});

// Title container
export const TitleContainer = styled(Box)({});

// Title text (NEXUS)
export const MainTitle = styled(Typography)({
  fontFamily: "'Orbitron', sans-serif",
  fontSize: "32px",
  fontWeight: 700,
  color: "white",
  letterSpacing: "2px",
});

// Subtitle text (SECURE ACCESS)
export const Subtitle = styled(Typography)({
  fontFamily: "'Inter', sans-serif",
  fontSize: "12px",
  fontWeight: 600,
  color: "white",
  textTransform: "uppercase",
  letterSpacing: "1px",
  marginTop: "-4px",
});

// Form container
export const FormContainer = styled(Box)({
  marginTop: "32px",
});

// Input field label
export const InputLabel = styled(Typography)({
  fontFamily: "'Inter', sans-serif",
  fontSize: "12px",
  fontWeight: 600,
  color: "white",
  textTransform: "uppercase",
  letterSpacing: "1px",
  marginBottom: "8px",
});

// Input wrapper with icon
export const InputWrapper = styled(Box)({
  position: "relative",
  width: "100%",
  marginBottom: "16px",
});

// Custom TextField for email
export const EmailTextField = styled(TextField)({
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
      padding: "12px 40px 12px 16px",
    },
  },
  "& .MuiInputLabel-root": {
    display: "none",
  },
});

// Custom TextField for password
export const PasswordTextField = styled(TextField)({
  "& .MuiOutlinedInput-root": {
    background: "rgba(0, 0, 0, 0.6)",
    borderRadius: "8px",
    "& fieldset": {
      borderColor: "#ff00ff",
      borderWidth: "1px",
    },
    "&:hover fieldset": {
      borderColor: "#ff00ff",
      borderWidth: "1px",
    },
    "&.Mui-focused fieldset": {
      borderColor: "#ff00ff",
      borderWidth: "2px",
    },
    "& input": {
      color: "white",
      fontFamily: "'Inter', sans-serif",
      fontSize: "14px",
      padding: "12px 48px 12px 16px",
    },
    "& .MuiInputAdornment-root": {
      "& button": {
        color: "white",
        padding: "4px",
        "&:hover": {
          backgroundColor: "rgba(255, 255, 255, 0.1)",
        },
      },
      "& svg": {
        color: "white",
      },
    },
  },
  "& .MuiInputLabel-root": {
    display: "none",
  },
});

// Custom TextField for full name
export const FullNameTextField = styled(TextField)({
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
      padding: "12px 40px 12px 16px",
    },
  },
  "& .MuiInputLabel-root": {
    display: "none",
  },
});

// Input icon container
export const InputIconContainer = styled(Box)({
  position: "absolute",
  right: "12px",
  top: "50%",
  transform: "translateY(-50%)",
  color: "white",
  display: "flex",
  alignItems: "center",
});

// Email icon
export const EmailIconStyled = styled(EmailIcon)({
  fontSize: "20px",
});

// Primary action button (ACCESS SYSTEM)
export const PrimaryButton = styled(Button)({
  background: "linear-gradient(90deg, #ff00ff 0%, #00ffff 100%)",
  color: "white",
  fontFamily: "'Inter', sans-serif",
  fontWeight: 700,
  fontSize: "14px",
  textTransform: "uppercase",
  padding: "16px 24px",
  borderRadius: "8px",
  boxShadow: "0 0 20px rgba(255, 0, 255, 0.5)",
  width: "100%",
  marginTop: "24px",
  "&:hover": {
    background: "linear-gradient(90deg, #ff00ff 0%, #00ffff 100%)",
    boxShadow: "0 0 30px rgba(255, 0, 255, 0.7)",
  },
  "& .MuiButton-startIcon": {
    marginRight: "8px",
  },
});

// Forgot password link
export const ForgotPasswordLink = styled(Link)({
  color: "#ffff00",
  fontFamily: "'Inter', sans-serif",
  fontSize: "14px",
  textDecoration: "none",
  marginTop: "8px",
  display: "inline-block",
  "&:hover": {
    color: "#ffff00",
    textDecoration: "underline",
  },
});

// Error alert
export const ErrorAlert = styled(Alert)({
  background: "rgba(139, 0, 0, 0.8)",
  border: "1px solid #8b0000",
  borderRadius: "8px",
  color: "white",
  fontFamily: "'Inter', sans-serif",
  fontSize: "14px",
  marginTop: "16px",
  padding: "12px 16px",
  "& .MuiAlert-icon": {
    color: "white",
    "& .MuiSvgIcon-root": {
      backgroundColor: "#8b0000",
      borderRadius: "50%",
      padding: "4px",
      fontSize: "16px",
    },
  },
  "& .MuiAlert-message": {
    color: "white",
  },
});

// Error icon wrapper
export const ErrorIconStyled = styled(Box)({
  fontSize: "20px",
  display: "flex",
  alignItems: "center",
  "& svg": {
    fontSize: "20px",
  },
});

// OR divider
export const DividerText = styled(Typography)({
  fontFamily: "'Inter', sans-serif",
  fontSize: "14px",
  color: "white",
  textAlign: "center",
  margin: "16px 0",
});

// Secondary link (Sign Up)
export const SecondaryLink = styled(Link)({
  color: "#00ffff",
  fontFamily: "'Inter', sans-serif",
  fontSize: "14px",
  textDecoration: "none",
  fontWeight: 600,
  "&:hover": {
    color: "#00ffff",
    textDecoration: "underline",
  },
});

// Secondary text
export const SecondaryText = styled(Typography)({
  fontFamily: "'Inter', sans-serif",
  fontSize: "14px",
  color: "white",
  textAlign: "center",
});

// Footer text
export const FooterText = styled(Typography)({
  fontFamily: "'Inter', sans-serif",
  fontSize: "11px",
  color: "rgba(255, 255, 255, 0.7)",
  textAlign: "center",
  marginTop: "32px",
});

// Footer link
export const FooterLink = styled(Link)({
  color: "#00ffff",
  fontFamily: "'Inter', sans-serif",
  fontSize: "11px",
  textDecoration: "none",
  "&:hover": {
    color: "#00ffff",
    textDecoration: "underline",
  },
});

// Footer router link - styled wrapper for React Router Link
export const FooterRouterLink = styled(RouterLink)({
  color: "#00ffff",
  fontFamily: "'Inter', sans-serif",
  fontSize: "11px",
  textDecoration: "none",
  "&:hover": {
    color: "#00ffff",
    textDecoration: "underline",
  },
});

