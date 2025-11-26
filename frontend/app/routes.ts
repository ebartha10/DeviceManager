import { type RouteConfig, index, route } from "@react-router/dev/routes";

export default [
  index("routes/home.tsx"),
  route("login", "routes/login/login.tsx"),
  route("dashboard", "routes/dashboard/dashboard.tsx", [
    route("profile", "routes/dashboard/profile.tsx"),
  ]),
  route("admin", "routes/admin/admin.tsx"),
  // Catch-all for .well-known paths (Chrome DevTools, etc.)
  route(".well-known/*", "routes/well-known.tsx"),
] satisfies RouteConfig;
