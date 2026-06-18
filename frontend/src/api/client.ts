import axios, { AxiosHeaders } from 'axios'
import { getStoredToken } from '../auth/AuthContext.tsx'

const API_BASE = import.meta.env.VITE_API_BASE_URL ?? 'http://localhost:8080'

const client = axios.create({
  baseURL: API_BASE,
})

client.interceptors.request.use((config) => {
  const token = getStoredToken()
  if (token) {
    const headers =
      config.headers instanceof AxiosHeaders ? config.headers : new AxiosHeaders(config.headers)
    headers.set('Authorization', `Bearer ${token}`)
    config.headers = headers
  }
  return config
})

export default client
