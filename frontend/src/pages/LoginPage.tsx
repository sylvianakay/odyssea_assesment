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
    <main className="page">
      <section className="card">
        <h1>Welcome back</h1>
        <p className="hint">Login to manage your skills and view matches.</p>

        <form className="form" onSubmit={onSubmit}>
          <input
            type="email"
            placeholder="Email"
            value={email}
            onChange={(event) => setEmail(event.target.value)}
            required
          />
          <input
            type="password"
            placeholder="Password"
            value={password}
            onChange={(event) => setPassword(event.target.value)}
            required
          />
          <button type="submit">Login</button>
        </form>

        {error && <p className="error">{error}</p>}

        <p className="hint">
          No account yet? <Link to="/register">Register</Link>
        </p>
      </section>
    </main>
  )
}
