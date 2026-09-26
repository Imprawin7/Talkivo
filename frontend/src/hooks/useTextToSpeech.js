import { useCallback, useEffect, useState } from 'react'
import { fetchVoices, generateSpeech } from '../services/ttsApi'
import { validateSelection, validateText } from '../utils/validators'

export function useTextToSpeech() {
  const [voices, setVoices] = useState([])
  const [voicesError, setVoicesError] = useState(null)

  const [text, setText] = useState('')
  const [language, setLanguage] = useState('')
  const [voice, setVoice] = useState('')

  const [audioUrl, setAudioUrl] = useState(null)
  const [isGenerating, setIsGenerating] = useState(false)
  const [error, setError] = useState(null)

  useEffect(() => {
    fetchVoices()
      .then((data) => setVoices(data))
      .catch((err) => setVoicesError(err.message))
  }, [])

  const languages = [...new Set(voices.map((v) => v.language))]
  const voicesForLanguage = voices.filter((v) => v.language === language)

  const handleGenerate = useCallback(async () => {
    const textError = validateText(text)
    const selectionError = validateSelection(language, voice)
    const validationError = textError || selectionError

    if (validationError) {
      setError(validationError)
      return
    }

    setError(null)
    setIsGenerating(true)
    setAudioUrl(null)

    try {
      const response = await generateSpeech({ text, language, voice })
      setAudioUrl(response.audioUrl)
    } catch (err) {
      setError(err.message)
    } finally {
      setIsGenerating(false)
    }
  }, [text, language, voice])

  const reset = useCallback(() => {
    setText('')
    setAudioUrl(null)
    setError(null)
  }, [])

  return {
    voices,
    voicesError,
    languages,
    voicesForLanguage,
    text,
    setText,
    language,
    setLanguage,
    voice,
    setVoice,
    audioUrl,
    isGenerating,
    error,
    handleGenerate,
    reset,
  }
}
