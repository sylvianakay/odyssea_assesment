import { useEffect, useState } from 'react'
import { useNavigate } from 'react-router-dom'
import client from '../api/client.ts'
import { getApiErrorMessage } from '../api/getApiErrorMessage.ts'
import { useAuth } from '../auth/AuthContext.tsx'

type JobMatch = {
  id: number
  title: string
  company: string
  description: string
  matchedSkills: string[]
}

export default function SkillsPage() {
  const { logout } = useAuth()
  const navigate = useNavigate()
  const [skillsInput, setSkillsInput] = useState('')
  const [savedSkills, setSavedSkills] = useState<string[]>([])
  const [matches, setMatches] = useState<JobMatch[]>([])
  const [message, setMessage] = useState('')
  const [error, setError] = useState('')

  const fetchSavedSkills = async () => {
    setError('')
    try {
      const response = await client.get<{ skills: string[] }>('/api/users/me/skills')
      setSavedSkills(response.data.skills ?? [])
    } catch (requestError) {
      setError(getApiErrorMessage(requestError, 'Could not load saved skills.'))
    }
  }

  const fetchMatches = async () => {
    setError('')
    try {
      const response = await client.get<JobMatch[]>('/api/jobs/matches')
      setMatches(response.data ?? [])
    } catch (requestError) {
      setError(getApiErrorMessage(requestError, 'Could not load job matches.'))
    }
  }

  useEffect(() => {
    void fetchSavedSkills()
    void fetchMatches()
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
      setSavedSkills(response.data.skills ?? [])
      await fetchMatches()
      setSkillsInput('')
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
          <h1>My profile</h1>
          <button type="button" onClick={onLogout}>
            Logout
          </button>
        </div>

        <label className="login-label" htmlFor="skills-input">
          Skills input
        </label>

        <input
          id="skills-input"
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

        <section className="section-block">
          <h2>Saved skills</h2>
          {savedSkills.length === 0 ? (
            <p className="hint">No saved skills yet.</p>
          ) : (
            <div className="chips">
              {savedSkills.map((skill) => (
                <span key={skill} className="chip">
                  {skill}
                </span>
              ))}
            </div>
          )}
        </section>

        <section className="section-block">
          <h2>Job listings</h2>
          {matches.length === 0 ? (
            <p className="hint">No matches yet. Add skills to get job suggestions.</p>
          ) : (
            <div className="match-list">
              {matches.map((match) => (
                <article key={match.id} className="match-card">
                  <h3>{match.title}</h3>
                  <p className="hint">{match.company}</p>
                  <p>{match.description}</p>
                  <div className="chips">
                    {match.matchedSkills.map((skill) => (
                      <span key={`${match.id}-${skill}`} className="chip">
                        {skill}
                      </span>
                    ))}
                  </div>
                </article>
              ))}
            </div>
          )}
        </section>
      </section>
    </main>
  )
}
