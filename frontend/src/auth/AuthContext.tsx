import { createContext, useContext, useMemo, useState } from 'react'
import type { ReactNode } from 'react'

export const TOKEN_STORAGE_KEY = 'auth_token'

export function getStoredToken(): string | null {
  return localStorage.getItem(TOKEN_STORAGE_KEY)
}

type AuthContextValue = {
  token: string | null
  setToken: (value: string | null) => void
  isAuthenticated: boolean
  logout: () => void
}

const AuthContext = createContext<AuthContextValue | null>(null)

type AuthProviderProps = {
  children: ReactNode
}

export function AuthProvider({ children }: AuthProviderProps) {
  const [token, setTokenState] = useState<string | null>(() => getStoredToken())

  const setToken = (value: string | null) => {
    setTokenState(value)
    if (value) {
      localStorage.setItem(TOKEN_STORAGE_KEY, value)
      return
    }
    localStorage.removeItem(TOKEN_STORAGE_KEY)
  }

  const logout = () => setToken(null)

  const value = useMemo(
    () => ({
      token,
      setToken,
      isAuthenticated: Boolean(token),
      logout,
    }),
    [token],
  )

  return <AuthContext.Provider value={value}>{children}</AuthContext.Provider>
}

export function useAuth() {
  const context = useContext(AuthContext)
  if (!context) {
    throw new Error('useAuth must be used within AuthProvider')
  }
  return context
}
