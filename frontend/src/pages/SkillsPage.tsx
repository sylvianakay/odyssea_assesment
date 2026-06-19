import { useEffect, useState } from 'react'
import { useNavigate } from 'react-router-dom'
import client from '../api/client.ts'
import { getApiErrorMessage } from '../api/getApiErrorMessage.ts'
import { useAuth } from '../auth/AuthContext.tsx'

const SKILLS_INPUT_STORAGE_KEY = 'skills_input_draft'

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
  const [skillsInput, setSkillsInput] = useState(() => localStorage.getItem(SKILLS_INPUT_STORAGE_KEY) ?? '')
  const [matches, setMatches] = useState<JobMatch[]>([])
  const [message, setMessage] = useState('')
  const [error, setError] = useState('')

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
    void fetchMatches()
  }, [])

  useEffect(() => {
    if (skillsInput.trim() === '') {
      localStorage.removeItem(SKILLS_INPUT_STORAGE_KEY)
      return
    }
    localStorage.setItem(SKILLS_INPUT_STORAGE_KEY, skillsInput)
  }, [skillsInput])

  const saveSkills = async () => {
    setMessage('')
    setError('')
    try {
      const parsed = skillsInput
        .split(',')
        .map((value) => value.trim())
        .filter(Boolean)
      await client.put('/api/users/me/skills', { skills: parsed })
      await fetchMatches()
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
