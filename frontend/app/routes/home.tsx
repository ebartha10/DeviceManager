import type { Route } from "./+types/home";
import HomeComponent from "./home/home";

export function meta({}: Route.MetaArgs) {
  return [
    { title: "NEXUS - Device Management Platform" },
    {
      name: "description",
      content: "NEXUS - Secure and efficient device management platform",
    },
  ];
}

export default function Home() {
  return <HomeComponent />;
}
