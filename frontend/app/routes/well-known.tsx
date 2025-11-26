export async function loader() {
  // Return 404 for .well-known paths (Chrome DevTools, etc.)
  // This suppresses the error in the console
  throw new Response("Not Found", { status: 404 });
}

export default function WellKnown() {
  return null;
}

