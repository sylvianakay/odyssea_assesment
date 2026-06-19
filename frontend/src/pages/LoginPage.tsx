import { useState } from 'react'
import type { FormEvent } from 'react'
import { Link, Navigate, useNavigate } from 'react-router-dom'
import client from '../api/client.ts'
import { getApiErrorMessage } from '../api/getApiErrorMessage.ts'
import { useAuth } from '../auth/AuthContext.tsx'

export default function LoginPage() {
  const { isAuthenticated, setToken } = useAuth()
  const navigate = useNavigate()
  const [email, setEmail] = useState('')
  const [password, setPassword] = useState('')
  const [error, setError] = useState('')

  if (isAuthenticated) {
    return <Navigate to="/skills" replace />
  }

  const onSubmit = async (event: FormEvent) => {
    event.preventDefault()
    setError('')
    try {
      const response = await client.post<{ token: string }>('/api/auth/login', { email, password })
      setToken(response.data.token)
      navigate('/skills', { replace: true })
    } catch (submitError) {
      setError(getApiErrorMessage(submitError, 'Login failed. Please try again.'))
    }
  }

  return (
    <main className="page login-page">
      <section className="card login-card">
        <div className="login-header">
          <h1 className="login-title">Welcome back</h1>
          <p className="hint login-subtitle">
            Don&apos;t have an account? <Link to="/register">Sign up</Link>
          </p>
        </div>

        <form className="form login-form" onSubmit={onSubmit}>
          <label className="login-label" htmlFor="login-email">
            Email
          </label>
          <input
            id="login-email"
            type="email"
            placeholder="m@example.com"
            value={email}
            onChange={(event) => setEmail(event.target.value)}
            required
          />
          <label className="login-label" htmlFor="login-password">
            Password
          </label>
          <input
            id="login-password"
            type="password"
            placeholder="Password"
            value={password}
            onChange={(event) => setPassword(event.target.value)}
            required
          />
          <button type="submit">Login</button>
        </form>

        {error && <p className="error">{error}</p>}
      </section>
    </main>
  )
}
