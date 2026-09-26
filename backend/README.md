# Talkivo — Backend

Spring Boot backend for Talkivo, a Text-to-Speech web application.

## Run locally

1. Copy `.env` and fill in real values for `TTS_API_KEY`, `TTS_REGION`, `TTS_ENDPOINT`.
2. Export them (or use a plugin like `spring-dotenv` / your IDE's env-file support):
   ```bash
   export $(cat .env | xargs)
   ```
3. Build and run:
   ```bash
   mvn spring-boot:run
   ```
4. Confirm it's up: `GET http://localhost:8080/api/health`

## Endpoints

| Method | Path | Description |
|---|---|---|
| GET | `/api/health` | Health check |
| GET | `/api/voices` | List available voices |
| POST | `/api/tts` | Generate speech — body: `{ "text", "language", "voice" }` |

## TTS provider

Wired to **Google Cloud Text-to-Speech** (`GoogleTtsClient`), called via its
REST API (`POST https://texttospeech.googleapis.com/v1/text:synthesize`) using
plain `java.net.http.HttpClient` — no extra SDK dependency needed.

To use it:
1. In Google Cloud Console, enable the **Cloud Text-to-Speech API** and create an API key.
2. Put that key in `backend/.env` as `TTS_API_KEY`.
3. Voice names in `TtsServiceImpl.AVAILABLE_VOICES` (e.g. `en-US-Standard-C`)
   must match real Google voice names for the run to succeed — see the
   [voice list](https://cloud.google.com/text-to-speech/docs/voices) if you
   want to add more.

To switch providers later (Azure Speech, Amazon Polly, ElevenLabs), replace
`GoogleTtsClient` with an equivalent client and swap it into
`TtsServiceImpl`'s constructor — nothing else needs to change.

## Tests

```bash
mvn test
```
