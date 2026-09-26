import { MAX_TEXT_LENGTH } from './constants'

export function validateText(text) {
  if (!text || !text.trim()) {
    return 'Enter some text before generating speech.'
  }
  if (text.length > MAX_TEXT_LENGTH) {
    return `Text exceeds the ${MAX_TEXT_LENGTH}-character limit.`
  }
  return null
}

export function validateSelection(language, voice) {
  if (!language) return 'Choose a language.'
  if (!voice) return 'Choose a voice.'
  return null
}

export function countWords(text) {
  const trimmed = text.trim()
  return trimmed ? trimmed.split(/\s+/).length : 0
}
