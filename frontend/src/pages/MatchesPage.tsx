import { useEffect, useState } from 'react'
import { Link, useNavigate } from 'react-router-dom'
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

export default function MatchesPage() {
  const { logout } = useAuth()
  const navigate = useNavigate()
  const [matches, setMatches] = useState<JobMatch[]>([])
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

  const onLogout = () => {
    logout()
    navigate('/login', { replace: true })
  }

  return (
    <main className="page">
      <section className="card">
        <div className="topBar">
          <h1>Job matches</h1>
          <button type="button" onClick={onLogout}>
            Logout
          </button>
        </div>

        <div className="links">
          <Link to="/skills">Skills</Link>
          <span className="active">Matches</span>
        </div>

        <button type="button" onClick={fetchMatches}>
          Refresh matches
        </button>

        {error && <p className="error">{error}</p>}

        <pre>{JSON.stringify(matches, null, 2)}</pre>
      </section>
    </main>
  )
}
