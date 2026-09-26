# Talkivo — Frontend

React + Vite + Tailwind frontend for Talkivo.

## Run locally

```bash
npm install
npm run dev
```

Runs on http://localhost:5173 by default and expects the backend at
the URL in `.env` (`VITE_API_BASE_URL`, defaults to
`http://localhost:8080/api`).

## Structure

- `components/` — one folder per UI piece (TextInput, LanguageSelector,
  VoiceSelector, GenerateButton, AudioPlayer, DownloadButton, ErrorMessage)
- `pages/Home` — assembles the components into the app layout
- `services/ttsApi.js` — all backend calls live here
- `hooks/useTextToSpeech.js` — app state + validation + generate flow
- `utils/` — constants and shared validators

## Design

Editorial/studio look: `Fraunces` display serif + `IBM Plex Sans` UI type,
stone-paper background, deep teal for actions, amber reserved for the
waveform/active state. Tokens live in `tailwind.config.js`.
