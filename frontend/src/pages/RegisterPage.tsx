import { useState } from 'react'
import type { FormEvent } from 'react'
import { Link, Navigate } from 'react-router-dom'
import client from '../api/client.ts'
import { getApiErrorMessage } from '../api/getApiErrorMessage.ts'
import { useAuth } from '../auth/AuthContext.tsx'

export default function RegisterPage() {
  const { isAuthenticated } = useAuth()
  const [email, setEmail] = useState('')
  const [password, setPassword] = useState('')
  const [message, setMessage] = useState('')
  const [error, setError] = useState('')

  if (isAuthenticated) {
    return <Navigate to="/skills" replace />
  }

  const onSubmit = async (event: FormEvent) => {
    event.preventDefault()
    setMessage('')
    setError('')
    try {
      const response = await client.post<{ message: string }>('/api/auth/register', { email, password })
      setMessage(response.data.message || 'Account created. You can log in now.')
    } catch (submitError) {
      setError(getApiErrorMessage(submitError, 'Could not register right now.'))
    }
  }

  return (
    <main className="page">
      <section className="card">
        <h1>Create account</h1>
        <p className="hint">Use your email and password to register.</p>

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
          <button type="submit">Register</button>
        </form>

        {message && <p className="success">{message}</p>}
        {error && <p className="error">{error}</p>}

        <p className="hint">
          Already have an account? <Link to="/login">Login</Link>
        </p>
      </section>
    </main>
  )
}
