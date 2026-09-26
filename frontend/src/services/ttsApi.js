
import axios from 'axios'
import { API_BASE_URL } from '../utils/constants'

const client = axios.create({
  baseURL: API_BASE_URL,
  timeout: 90000,
  headers: {
    'Content-Type': 'application/json',
  },
})

function toFriendlyError(error) {
  if (error.response) {
    const status = error.response.status
    const message = error.response.data?.message

    if (status === 400) {
      return message || 'The request contains invalid information.'
    }

    if (status === 401 || status === 403) {
      return 'Authentication failed. Please check the API credentials.'
    }

    if (status === 404) {
      return 'The requested service could not be found.'
    }

    if (status === 408) {
      return 'The request took too long. Please try again.'
    }

    if (status >= 500) {
      return 'The server encountered a problem. Please try again.'
    }

    return message || 'The server could not process that request.'
  }

  if (error.request) {
    if (error.code === 'ECONNABORTED') {
      return 'The request timed out. Please try again.'
    }

    return 'Could not reach the server. Check your connection and try again.'
  }

  return 'Something went wrong while sending your request.'
}

export async function fetchVoices() {
  try {
    const { data } = await client.get('/voices')
    return data
  } catch (error) {
    throw new Error(toFriendlyError(error))
  }
}

export async function generateSpeech({
  text,
  language,
  voice,
}) {
  try {
    const { data } = await client.post('/tts', {
      text,
      language,
      voice,
    })

    if (!data?.success || !data?.audioUrl) {
      throw new Error(
        'Speech was generated, but no audio file was returned.'
      )
    }

    return data
  } catch (error) {
    if (error instanceof Error && !axios.isAxiosError(error)) {
      throw error
    }

    throw new Error(toFriendlyError(error))
  }
}

export async function checkHealth() {
  try {
    const { data } = await client.get('/health')
    return data
  } catch (error) {
    throw new Error(toFriendlyError(error))
  }
}
