const TOKEN_KEY = "nexus_jwt_token";
const USER_ID_KEY = "nexus_user_id";

const isBrowser = typeof window !== "undefined";

export const tokenStorage = {
  setToken: (token: string, userId: string) => {
    if (isBrowser) {
      localStorage.setItem(TOKEN_KEY, token);
      localStorage.setItem(USER_ID_KEY, userId);
    }
  },

  getToken: (): string | null => {
    if (!isBrowser) return null;
    return localStorage.getItem(TOKEN_KEY);
  },

  getUserId: (): string | null => {
    if (!isBrowser) return null;
    return localStorage.getItem(USER_ID_KEY);
  },

  clearToken: () => {
    if (isBrowser) {
      localStorage.removeItem(TOKEN_KEY);
      localStorage.removeItem(USER_ID_KEY);
    }
  },

  hasToken: (): boolean => {
    if (!isBrowser) return false;
    return localStorage.getItem(TOKEN_KEY) !== null;
  },
};

