import { useEffect, useState } from 'react'
import { Link, useNavigate } from 'react-router-dom'
import client from '../api/client.ts'
import { getApiErrorMessage } from '../api/getApiErrorMessage.ts'
import { useAuth } from '../auth/AuthContext.tsx'

export default function SkillsPage() {
  const { logout } = useAuth()
  const navigate = useNavigate()
  const [skillsInput, setSkillsInput] = useState('')
  const [skills, setSkills] = useState<string[]>([])
  const [message, setMessage] = useState('')
  const [error, setError] = useState('')

  const fetchSkills = async () => {
    setError('')
    try {
      const response = await client.get<{ skills: string[] }>('/api/users/me/skills')
      setSkills(response.data.skills ?? [])
    } catch (requestError) {
      setError(getApiErrorMessage(requestError, 'Could not load your skills.'))
    }
  }

  useEffect(() => {
    void fetchSkills()
  }, [])

  const saveSkills = async () => {
    setMessage('')
    setError('')
    try {
      const parsed = skillsInput
        .split(',')
        .map((value) => value.trim())
        .filter(Boolean)
      const response = await client.put<{ skills: string[] }>('/api/users/me/skills', { skills: parsed })
      setSkills(response.data.skills ?? [])
      setMessage('Skills saved.')
    } catch (requestError) {
      setError(getApiErrorMessage(requestError, 'Could not save skills.'))
    }
  }

  const onLogout = () => {
    logout()
    navigate('/login', { replace: true })
  }

  return (
    <main className="page">
      <section className="card">
        <div className="topBar">
          <h1>My skills</h1>
          <button type="button" onClick={onLogout}>
            Logout
          </button>
        </div>

        <div className="links">
          <span className="active">Skills</span>
          <Link to="/matches">Matches</Link>
        </div>

        <input
          type="text"
          placeholder="e.g. java, sql"
          value={skillsInput}
          onChange={(event) => setSkillsInput(event.target.value)}
        />

        <div className="row">
          <button type="button" onClick={saveSkills}>
            Save skills
          </button>
        </div>

        {message && <p className="success">{message}</p>}
        {error && <p className="error">{error}</p>}

        <pre>{JSON.stringify(skills, null, 2)}</pre>
      </section>
    </main>
  )
}
