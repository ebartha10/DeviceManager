import { styled } from "@mui/material/styles";
import {
  Box,
  Button,
  Typography,
  Card,
  Container,
} from "@mui/material";
import { Link as RouterLink } from "react-router";

// Page container
export const LandingContainer = styled(Box)({
  minHeight: "100vh",
  display: "flex",
  flexDirection: "column",
  background: "#000000",
  position: "relative",
  overflow: "hidden",
});

// Animated background gradient
export const BackgroundGradient = styled(Box)({
  position: "absolute",
  top: 0,
  left: 0,
  right: 0,
  bottom: 0,
  background: `
    radial-gradient(circle at 20% 50%, rgba(0, 255, 255, 0.1) 0%, transparent 50%),
    radial-gradient(circle at 80% 80%, rgba(255, 0, 255, 0.1) 0%, transparent 50%),
    radial-gradient(circle at 40% 20%, rgba(255, 255, 0, 0.05) 0%, transparent 50%)
  `,
  zIndex: 0,
});

// Main content wrapper
export const ContentWrapper = styled(Container)({
  position: "relative",
  zIndex: 1,
  display: "flex",
  flexDirection: "column",
  alignItems: "center",
  justifyContent: "center",
  minHeight: "100vh",
  padding: "48px 24px",
});

// Logo container
export const LogoContainer = styled(Box)({
  display: "flex",
  flexDirection: "column",
  alignItems: "center",
  gap: "16px",
  marginBottom: "48px",
});

// Logo icon
export const LogoIcon = styled(Box)({
  width: "80px",
  height: "80px",
  borderRadius: "16px",
  background: "linear-gradient(135deg, #ff00ff 0%, #00ffff 100%)",
  display: "flex",
  alignItems: "center",
  justifyContent: "center",
  boxShadow: "0 0 30px rgba(255, 0, 255, 0.5)",
  "& svg": {
    width: "48px",
    height: "48px",
    color: "white",
  },
});

// Main title
export const MainTitle = styled(Typography)({
  fontFamily: "'Orbitron', sans-serif",
  fontSize: "64px",
  fontWeight: 700,
  color: "white",
  letterSpacing: "2px",
  textAlign: "center",
  textShadow: "0 0 20px rgba(0, 255, 255, 0.5)",
  background: "linear-gradient(135deg, #00ffff 0%, #ff00ff 100%)",
  WebkitBackgroundClip: "text",
  WebkitTextFillColor: "transparent",
  backgroundClip: "text",
  marginBottom: "16px",
  "@media (max-width: 768px)": {
    fontSize: "40px",
  },
});

// Subtitle
export const Subtitle = styled(Typography)({
  fontFamily: "'Inter', sans-serif",
  fontSize: "18px",
  color: "rgba(255, 255, 255, 0.7)",
  textAlign: "center",
  maxWidth: "600px",
  lineHeight: 1.6,
  marginBottom: "48px",
});

// Features grid
export const FeaturesGrid = styled(Box)({
  display: "flex",
  flexWrap: "wrap",
  justifyContent: "center",
  gap: "24px",
  width: "100vw",
  marginBottom: "64px",
});

// Feature card
export const FeatureCard = styled(Card)({
  background: "rgba(0, 0, 0, 0.6)",
  border: "1px solid rgba(0, 255, 255, 0.2)",
  borderRadius: "16px",
  padding: "32px 24px",
  textAlign: "center",
  width: "280px",
  flex: "0 0 auto",
  transition: "all 0.3s ease",
  "@media (max-width: 640px)": {
    width: "100%",
    maxWidth: "100%",
  },
  "&:hover": {
    border: "1px solid rgba(255, 0, 255, 0.5)",
    boxShadow: "0 0 20px rgba(255, 0, 255, 0.3)",
    transform: "translateY(-4px)",
  },
});

// Feature icon container
export const FeatureIconContainer = styled(Box)({
  width: "64px",
  height: "64px",
  borderRadius: "12px",
  background: "linear-gradient(135deg, #ff00ff 0%, #00ffff 100%)",
  display: "flex",
  alignItems: "center",
  justifyContent: "center",
  margin: "0 auto 24px",
  boxShadow: "0 0 20px rgba(0, 255, 255, 0.3)",
  "& svg": {
    width: "32px",
    height: "32px",
    color: "white",
  },
});

// Feature title
export const FeatureTitle = styled(Typography)({
  fontFamily: "'Orbitron', sans-serif",
  fontSize: "20px",
  fontWeight: 600,
  color: "white",
  marginBottom: "12px",
});

// Feature description
export const FeatureDescription = styled(Typography)({
  fontFamily: "'Inter', sans-serif",
  fontSize: "14px",
  color: "rgba(255, 255, 255, 0.6)",
  lineHeight: 1.6,
});

// CTA section
export const CTASection = styled(Box)({
  display: "flex",
  gap: "24px",
  flexWrap: "wrap",
  justifyContent: "center",
  marginTop: "32px",
});

// Primary CTA button
export const PrimaryCTAButton = styled(Button)({
  background: "linear-gradient(90deg, #ff00ff 0%, #00ffff 100%)",
  color: "white",
  fontFamily: "'Inter', sans-serif",
  fontWeight: 600,
  fontSize: "16px",
  textTransform: "none",
  padding: "16px 32px",
  borderRadius: "12px",
  boxShadow: "0 0 20px rgba(255, 0, 255, 0.4)",
  "&:hover": {
    background: "linear-gradient(90deg, #ff00ff 0%, #00ffff 100%)",
    boxShadow: "0 0 30px rgba(255, 0, 255, 0.6)",
    transform: "translateY(-2px)",
  },
});

// Secondary CTA button
export const SecondaryCTAButton = styled(Button)({
  background: "transparent",
  color: "white",
  fontFamily: "'Inter', sans-serif",
  fontWeight: 600,
  fontSize: "16px",
  textTransform: "none",
  padding: "16px 32px",
  borderRadius: "12px",
  border: "2px solid rgba(0, 255, 255, 0.5)",
  "&:hover": {
    background: "rgba(0, 255, 255, 0.1)",
    border: "2px solid rgba(0, 255, 255, 0.8)",
    boxShadow: "0 0 20px rgba(0, 255, 255, 0.3)",
  },
});

// Styled router link
export const StyledRouterLink = styled(RouterLink)({
  textDecoration: "none",
  color: "inherit",
});

