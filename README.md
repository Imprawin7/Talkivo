## 1. Overview

**Talkivo** is a full-stack web-based Text-to-Speech (TTS) application that converts written text into natural-sounding speech.

The application provides a clean and responsive interface where users can enter or paste text, select a language and voice, generate speech, preview the resulting audio, control playback, and download the generated WAV file.

Talkivo uses:

* **React.js + Vite** for the frontend
* **Java + Spring Boot** for the backend
* **Piper TTS** for local speech synthesis
* **ONNX voice models** for language and voice generation
* **REST APIs** for frontend-backend communication

Unlike cloud-based TTS implementations, the current Talkivo implementation performs speech synthesis locally through Piper and does not require a paid third-party TTS API or expose cloud API credentials to the frontend.

---

# 2. Project Goals

The primary goal of Talkivo is to demonstrate the complete lifecycle of a modern Text-to-Speech application:

```text
User Input
    ↓
React Frontend
    ↓
REST API
    ↓
Spring Boot Backend
    ↓
Request Validation
    ↓
Piper TTS
    ↓
Voice Model
    ↓
WAV Audio
    ↓
Audio Storage
    ↓
Audio URL
    ↓
Browser Audio Player
```

The project also demonstrates:

* Frontend/backend communication
* REST API development
* Request validation
* Service-layer architecture
* TTS provider integration
* Local audio processing
* Audio file storage
* Browser audio playback
* Download functionality
* Error handling
* Network failure handling
* Basic security practices
* Responsive UI development

---

# 3. Problem Statement

A Text-to-Speech application needs to provide more than simply converting text into audio.

A complete implementation should allow users to:

* Enter or paste text
* Understand the text length
* Select an appropriate language
* Select a voice
* Generate speech
* Listen to the generated audio
* Control playback
* Download the generated audio
* Recover gracefully from invalid input and system failures

Talkivo addresses these requirements through a separated frontend and backend architecture.

The React application handles the user experience, while Spring Boot manages validation, voice selection, TTS processing, audio storage, and API responses.

---

# 4. Objectives

Talkivo was developed with the following objectives:

1. Build a complete React-based TTS interface.
2. Develop a Java Spring Boot REST backend.
3. Establish communication between frontend and backend.
4. Integrate a Text-to-Speech engine.
5. Process TTS requests securely on the backend.
6. Validate user input on both frontend and backend.
7. Generate and store audio files.
8. Provide browser-based audio playback.
9. Allow users to download generated audio.
10. Handle invalid input gracefully.
11. Handle backend, provider, and network failures.
12. Implement basic security practices.
13. Build a responsive and professional user interface.
14. Maintain a modular and extensible project structure.

---

# 5. Main Features

## 5.1 Text Editor

The Talkivo script editor supports:

* Text entry
* Text pasting
* Live word count
* Live character count
* Maximum text length validation
* Remaining-character indicator
* Over-limit detection
* Clear button
* Text modification at any time

### Maximum Text Length

```text
1000 characters
```

---

## 5.2 Language Selection

The available languages are provided dynamically by the backend based on the configured voice models.

Currently supported languages:

| Language | Code |
| -------- | ---- |
| English  | `en` |
| Hindi    | `hi` |
| Nepali   | `ne` |
| Spanish  | `es` |
| Urdu     | `ur` |

The architecture allows additional languages to be added by installing the required Piper models and registering the appropriate mappings.

---

## 5.3 Voice Selection

Users select a voice after selecting a language.

Current configured voices:

| Voice          | ID           | Language | Type     |
| -------------- | ------------ | -------- | -------- |
| English Male   | `en-male`    | English  | Male     |
| English Female | `en-female`  | English  | Female   |
| Hindi Male     | `hi-male`    | Hindi    | Male     |
| Hindi Female   | `hi-female`  | Hindi    | Female   |
| Nepali Chitwan | `ne-chitwan` | Nepali   | Standard |
| Nepali Google  | `ne-google`  | Nepali   | Standard |
| Spanish Male   | `es-male`    | Spanish  | Male     |
| Spanish Female | `es-female`  | Spanish  | Female   |
| Urdu Male      | `ur-male`    | Urdu     | Male     |
| Urdu Female    | `ur-female`  | Urdu     | Female   |

The backend verifies that the selected voice belongs to the selected language before speech generation.

---

# 6. Text-to-Speech Workflow

When the user clicks **Generate Speech**, the following process occurs:

```text
1. User enters text
        ↓
2. User selects language
        ↓
3. User selects voice
        ↓
4. Frontend validates the basic input
        ↓
5. Frontend sends POST /api/tts
        ↓
6. Spring Boot receives the request
        ↓
7. Backend validates the request
        ↓
8. Backend validates language and voice
        ↓
9. Voice is mapped to a Piper model
        ↓
10. Piper generates WAV audio
        ↓
11. Audio is stored
        ↓
12. Backend returns audio URL
        ↓
13. Frontend loads the audio
        ↓
14. User plays or downloads the audio
```

---

# 7. Audio Player

Talkivo includes a custom audio player rather than relying only on the browser's default audio controls.

The player provides:

* Play
* Pause
* Current time
* Total duration
* Interactive seeking
* Keyboard seeking
* Volume control
* Playback status
* Visual waveform
* Audio-ready state

The audio player automatically resets when a new generated audio file is received.

---

# 8. Audio Download

Users can download generated speech directly from the interface.

Current output format:

```text
WAV
```

Default filename:

```text
talkivo-speech.wav
```

Piper directly generates WAV audio, so no additional audio conversion dependency is required.

Additional formats such as MP3 or OGG can be added in the future through an audio conversion layer.

---

# 9. Technology Stack

## Frontend

| Technology   | Version / Purpose             |
| ------------ | ----------------------------- |
| React        | 18.3.1                        |
| Vite         | 5.4.21                        |
| Tailwind CSS | 3.4.13                        |
| Axios        | HTTP communication            |
| JavaScript   | Application logic             |
| HTML5        | Structure                     |
| CSS3         | Styling and responsive design |

## Backend

| Technology        | Version / Purpose               |
| ----------------- | ------------------------------- |
| Java              | 17 source compatibility         |
| Spring Boot       | 3.3.4                           |
| Spring Web        | REST APIs                       |
| Spring Validation | Request validation              |
| Maven             | Build and dependency management |
| Embedded Tomcat   | Web server                      |

## TTS

| Technology        | Purpose                   |
| ----------------- | ------------------------- |
| Piper TTS         | Local speech synthesis    |
| Python            | Piper runtime             |
| ONNX              | Voice model format        |
| Piper ONNX Models | Language/voice generation |

---

# 10. System Architecture

```text
┌────────────────────────────────────┐
│              Browser               │
│                                    │
│        React + Vite Frontend       │
│                                    │
│  Text │ Language │ Voice │ Player  │
└──────────────────┬─────────────────┘
                   │
                   │ HTTP / JSON
                   ▼
┌────────────────────────────────────┐
│          Spring Boot API           │
│                                    │
│ Controllers                        │
│ DTOs                               │
│ Validation                         │
│ Services                           │
│ Exception Handling                 │
│ Configuration                      │
└──────────────────┬─────────────────┘
                   │
                   ▼
┌────────────────────────────────────┐
│          Piper TTS Client          │
│                                    │
│ Voice Mapping                      │
│ Model Selection                    │
│ Piper Process Execution            │
└──────────────────┬─────────────────┘
                   │
                   ▼
┌────────────────────────────────────┐
│        Piper ONNX Voice Models     │
└──────────────────┬─────────────────┘
                   │
                   ▼
┌────────────────────────────────────┐
│          WAV Audio Output          │
└──────────────────┬─────────────────┘
                   │
                   ▼
┌────────────────────────────────────┐
│          Audio Storage             │
│          generated-audio/          │
└──────────────────┬─────────────────┘
                   │
                   ▼
            Browser Player
```

---

# 11. Frontend Architecture

The frontend follows a component-based React architecture.

```text
frontend/
│
├── src/
│   ├── components/
│   │   ├── AudioPlayer/
│   │   ├── DownloadButton/
│   │   ├── ErrorMessage/
│   │   ├── GenerateButton/
│   │   ├── LanguageSelector/
│   │   ├── TextInput/
│   │   └── VoiceSelector/
│   │
│   ├── hooks/
│   │   └── useTextToSpeech.js
│   │
│   ├── pages/
│   │   └── Home/
│   │       └── Home.jsx
│   │
│   ├── services/
│   │   └── ttsApi.js
│   │
│   ├── utils/
│   │   ├── constants.js
│   │   └── validators.js
│   │
│   ├── App.jsx
│   └── index.css
│
└── package.json
```

### Component Responsibilities

#### `TextInput`

Handles:

* Text entry
* Word count
* Character count
* Maximum length
* Clear action

#### `LanguageSelector`

Displays supported languages received from the backend.

#### `VoiceSelector`

Displays voices associated with the selected language.

#### `GenerateButton`

Controls speech generation and loading state.

#### `AudioPlayer`

Provides:

* Playback
* Pause
* Seeking
* Volume
* Duration
* Waveform visualization

#### `DownloadButton`

Provides generated WAV audio download.

#### `ErrorMessage`

Displays user-friendly application errors.

#### `useTextToSpeech`

Centralizes:

* Voice retrieval
* Language state
* Voice state
* Text state
* Speech generation
* Audio state
* Reset behavior
* Error state

#### `ttsApi`

Provides the frontend API communication layer using Axios.

---

# 12. Backend Architecture

The backend follows a layered Spring Boot architecture.

```text
backend/
│
├── src/
│   └── main/
│       ├── java/
│       │   └── com/example/talkivo/
│       │       ├── config/
│       │       ├── controller/
│       │       ├── dto/
│       │       ├── exception/
│       │       └── service/
│       │
│       └── resources/
│           └── application.properties
│
├── generated-audio/
├── pom.xml
└── mvnw.cmd
```

### Backend responsibilities

The backend is responsible for:

* Receiving TTS requests
* Validating requests
* Validating supported languages
* Validating voice-language compatibility
* Mapping voices to Piper models
* Executing Piper
* Reading generated WAV data
* Storing audio
* Returning audio URLs
* Serving generated audio
* Handling exceptions
* Logging provider failures
* Enforcing configured application limits

---

# 13. REST API

## Health Check

### Endpoint

```http
GET /api/health
```

### Example response

```json
{
  "status": "UP",
  "timestamp": "2026-09-25T00:00:00Z"
}
```

---

## Available Voices

### Endpoint

```http
GET /api/voices
```

### Example response

```json
[
  {
    "id": "en-male",
    "name": "English Male",
    "language": "en",
    "gender": "male"
  }
]
```

The frontend uses this endpoint to construct the language and voice selection interface.

---

## Generate Speech

### Endpoint

```http
POST /api/tts
```

### Content-Type

```http
application/json
```

### Request

```json
{
  "text": "Hello, this is Talkivo.",
  "language": "en",
  "voice": "en-male"
}
```

### Successful response

```json
{
  "success": true,
  "audioUrl": "/audio/example.wav"
}
```

---

## Audio Delivery

### Endpoint

```http
GET /audio/{filename}
```

The endpoint serves generated audio files from the configured storage directory.

The resolved file path is normalized and verified against the configured storage directory before the file is served.

---

# 14. Request Validation

Talkivo validates requests at multiple levels.

## Frontend

The frontend checks:

* Empty text
* Text length
* Language selection
* Voice selection

## Backend

The backend validates:

* Required fields
* Maximum text length
* Supported language
* Voice existence
* Voice-language compatibility
* JSON request format
* Content type

This layered validation prevents invalid requests from reaching the TTS engine.

---

# 15. Error Handling

Talkivo provides controlled error handling for application and infrastructure failures.

| Scenario                  | User-facing result                |
| ------------------------- | --------------------------------- |
| Empty text                | Text validation message           |
| Text over 1000 characters | Maximum-length message            |
| Unsupported language      | Language validation message       |
| Invalid voice             | Voice-language validation message |
| Malformed JSON            | Request format error              |
| Wrong content type        | Content-Type error                |
| Backend unavailable       | Network/server connection message |
| TTS failure               | Speech generation failure message |
| Missing audio             | Audio resource failure            |
| Internal server error     | Safe generic server message       |

The backend uses centralized exception handling so internal implementation details are not exposed directly to the frontend.

---

# 16. Security

Talkivo implements several basic security practices.

## Input Validation

Requests are validated before TTS processing.

## Voice Validation

The backend verifies that a selected voice belongs to the requested language.

## Path Traversal Protection

The audio endpoint normalizes requested paths and ensures the resolved file remains inside the configured audio storage directory.

## CORS

The backend restricts API access to the configured frontend origin.

## Controlled Error Responses

Internal provider and server details are not returned directly to the user.

## Local TTS

The current architecture does not require a cloud TTS API key.

This avoids placing third-party TTS credentials in frontend code.

---

# 17. Piper TTS Integration

Talkivo uses Piper TTS as its local speech synthesis engine.

Piper models are stored locally and selected according to the requested language and voice.

The backend creates a temporary text input, invokes Piper, reads the generated WAV file, and then stores the resulting audio in Talkivo's audio directory.

```text
User Text
    ↓
Temporary Text File
    ↓
Piper Process
    ↓
Selected ONNX Model
    ↓
Temporary WAV File
    ↓
AudioStorageService
    ↓
generated-audio/
```

### Advantages

* Local speech generation
* No paid cloud TTS dependency
* No external TTS API billing
* No cloud API key exposed to the frontend
* Full control over installed voice models
* Easy local development

---

# 18. Piper Voice Mapping

Application voice IDs are mapped to Piper model names.

```text
en-male
    → en_US-hfc_male-medium

en-female
    → en_US-hfc_female-medium

hi-male
    → hi_IN-pratham-medium

hi-female
    → hi_IN-priyamvada-medium

ne-chitwan
    → ne_NP-chitwan-medium

ne-google
    → ne_NP-google-medium

es-male
    → es_MX-ald-medium

es-female
    → es_AR-daniela-high

ur-male
    → ur_PK-fasih-medium

ur-female
    → ur_PK-aegis_female-medium
```

---

# 19. Installed Piper Models

The current model directory contains:

```text
en_US-amy-medium.onnx
en_US-hfc_female-medium.onnx
en_US-hfc_male-medium.onnx

es_AR-daniela-high.onnx
es_MX-ald-medium.onnx

hi_IN-pratham-medium.onnx
hi_IN-priyamvada-medium.onnx
hi_IN-rohan-medium.onnx

ne_NP-chitwan-medium.onnx
ne_NP-google-medium.onnx

ur_PK-aegis_female-medium.onnx
ur_PK-fasih-medium.onnx
```

Not every installed model needs to be exposed through the application. The backend explicitly controls which models are available to users.

---

# 20. Configuration

Important application configuration is maintained outside the main business logic.

Example:

```properties
spring.application.name=talkivo

server.port=8080

tts.text.max-length=1000

app.cors.allowed-origin=http://localhost:5173

app.audio.storage-dir=./generated-audio
```

Piper runtime and model paths are also configurable so that the project can be moved between development environments without changing the TTS service logic.

---

# 21. Running the Application

## Prerequisites

Install the following:

* Java 17+
* Node.js
* npm
* Python 3.12
* Piper TTS
* Required Piper voice models

---

## Start the Backend

Open PowerShell:

```powershell
cd D:\Coding\Talkivo\backend
.\mvnw.cmd spring-boot:run
```

Backend:

```text
http://localhost:8080
```

Health endpoint:

```text
http://localhost:8080/api/health
```

---

## Start the Frontend

Open another PowerShell window:

```powershell
cd D:\Coding\Talkivo\frontend
npm install
npm run dev
```

Frontend:

```text
http://localhost:5173
```

Open the frontend URL in a modern browser.

---

# 22. Build Commands

## Frontend

Install dependencies:

```powershell
npm install
```

Start development server:

```powershell
npm run dev
```

Create production build:

```powershell
npm run build
```

---

## Backend

Start development server:

```powershell
.\mvnw.cmd spring-boot:run
```

Run tests:

```powershell
.\mvnw.cmd test
```

Create production JAR:

```powershell
.\mvnw.cmd clean package
```

---

# 23. Testing

The application has been tested across functional, validation, integration, and failure scenarios.

## Functional Testing

Verified:

* Backend health endpoint
* Voice retrieval
* Speech generation
* Audio storage
* Audio delivery
* Audio playback
* Play/pause
* Seek
* Volume control
* WAV download
* Text clearing
* Word counting
* Character counting
* Responsive UI
* Configured voice mappings

## Validation Testing

Verified:

* Empty text
* 1000-character limit
* Text exceeding the limit
* Unsupported language
* Invalid voice-language combination
* Malformed JSON
* Unsupported content type

## Failure Testing

Verified:

* Backend unavailable
* Network failure
* TTS provider failure
* Missing generated audio
* Invalid audio resource

---

# 24. Example User Journey

```text
┌─────────────────────┐
│     Open Talkivo    │
└──────────┬──────────┘
           ↓
┌─────────────────────┐
│    Enter Text       │
└──────────┬──────────┘
           ↓
┌─────────────────────┐
│ Select Language     │
└──────────┬──────────┘
           ↓
┌─────────────────────┐
│ Select Voice        │
└──────────┬──────────┘
           ↓
┌─────────────────────┐
│  Generate Speech    │
└──────────┬──────────┘
           ↓
┌─────────────────────┐
│ Backend Validation  │
└──────────┬──────────┘
           ↓
┌─────────────────────┐
│     Piper TTS       │
└──────────┬──────────┘
           ↓
┌─────────────────────┐
│   WAV Generation    │
└──────────┬──────────┘
           ↓
┌─────────────────────┐
│    Audio Storage    │
└──────────┬──────────┘
           ↓
┌─────────────────────┐
│    Audio Player     │
└──────────┬──────────┘
           ↓
┌─────────────────────┐
│ Play / Seek /       │
│ Volume / Download   │
└─────────────────────┘
```

---

# 25. User Interface

Talkivo uses a premium dark interface focused on the speech-generation workflow.

The main interface is divided into three stages:

```text
01 — Your Script
02 — Voice Settings
03 — Generated Voice
```

### UI characteristics

* Dark premium theme
* Glass-style cards
* Subtle gradients and background effects
* Responsive layout
* Clear visual hierarchy
* Script editor
* Voice configuration panel
* Generation state
* Audio waveform
* Playback controls
* Download action
* Error states
* Mobile-friendly layout

---

# 26. Project Structure

```text
Talkivo/
│
├── backend/
│   ├── src/
│   │   ├── main/
│   │   │   ├── java/
│   │   │   │   └── com/example/talkivo/
│   │   │   │       ├── config/
│   │   │   │       ├── controller/
│   │   │   │       ├── dto/
│   │   │   │       ├── exception/
│   │   │   │       └── service/
│   │   │   │
│   │   │   └── resources/
│   │   │       └── application.properties
│   │   │
│   │   └── test/
│   │
│   ├── generated-audio/
│   ├── pom.xml
│   └── mvnw.cmd
│
├── frontend/
│   ├── src/
│   │   ├── components/
│   │   │   ├── AudioPlayer/
│   │   │   ├── DownloadButton/
│   │   │   ├── ErrorMessage/
│   │   │   ├── GenerateButton/
│   │   │   ├── LanguageSelector/
│   │   │   ├── TextInput/
│   │   │   └── VoiceSelector/
│   │   │
│   │   ├── hooks/
│   │   │   └── useTextToSpeech.js
│   │   │
│   │   ├── pages/
│   │   │   └── Home/
│   │   │       └── Home.jsx
│   │   │
│   │   ├── services/
│   │   │   └── ttsApi.js
│   │   │
│   │   ├── utils/
│   │   │   ├── constants.js
│   │   │   └── validators.js
│   │   │
│   │   ├── App.jsx
│   │   └── index.css
│   │
│   ├── package.json
│   └── vite.config.js
│
├── piper/
│   └── models/
│
└── README.md
```

---

# 27. Assignment Requirement Mapping

| Assignment Requirement         | Talkivo Implementation                               |
| ------------------------------ | ---------------------------------------------------- |
| Text input                     | `TextInput`                                          |
| Enter/paste text               | Script editor                                        |
| Character count                | Live counter                                         |
| Word count                     | `countWords()` utility                               |
| Maximum text length            | 1000-character validation                            |
| Clear/modify text              | Clear button + editable textarea                     |
| Language selection             | `LanguageSelector`                                   |
| Voice selection                | `VoiceSelector`                                      |
| Male/female voices             | Configured Piper voices                              |
| Generate speech                | `POST /api/tts`                                      |
| Frontend/backend communication | Axios + REST                                         |
| Backend processing             | Spring Boot services                                 |
| TTS integration                | Piper TTS                                            |
| Audio generation               | WAV                                                  |
| Audio player                   | Custom React player                                  |
| Play/pause                     | AudioPlayer                                          |
| Seek                           | Waveform interaction                                 |
| Volume                         | Volume slider                                        |
| Download                       | WAV DownloadButton                                   |
| Empty input handling           | Frontend + backend validation                        |
| Length validation              | Frontend + backend validation                        |
| Invalid language               | Backend validation                                   |
| Invalid voice                  | Backend validation                                   |
| API/service failure            | Global exception handling                            |
| Network failure                | Axios error handling                                 |
| API credential protection      | Local Piper architecture                             |
| Responsive UI                  | Responsive CSS                                       |
| Basic security                 | Validation, CORS, path protection, controlled errors |

---

# 28. Design Decisions

## Why React?

React provides a component-based architecture that makes the interface modular and easy to maintain.

## Why Spring Boot?

Spring Boot provides a clean Java backend architecture with REST support, validation, configuration, exception handling, and service-layer separation.

## Why Piper?

Piper provides local speech synthesis without requiring a paid cloud TTS API.

## Why WAV?

Piper generates WAV audio directly, avoiding the need for an additional conversion process.

## Why a Dedicated Audio Endpoint?

Returning an audio URL keeps the TTS JSON response small and separates audio delivery from API response data.

## Why Validate on Both Frontend and Backend?

Frontend validation provides immediate feedback, while backend validation ensures that invalid or malicious requests cannot bypass application rules simply by calling the API directly.

---

# 29. Security Considerations

The application follows a defense-in-depth approach appropriate for the current project scope.

### Client-side validation

Improves user experience.

### Server-side validation

Provides authoritative request validation.

### Restricted CORS

Limits browser-based API access to the configured frontend origin.

### Safe file serving

Audio file paths are normalized and checked against the configured storage directory.

### Controlled exceptions

Internal exceptions are not exposed directly to users.

### No exposed cloud credentials

The current TTS implementation uses local Piper models instead of placing third-party API credentials in the frontend.

---

# 30. Limitations

The current implementation has several intentional limitations:

1. Available languages depend on installed Piper models.
2. Available voices depend on configured models.
3. Generated audio is currently WAV.
4. Audio is stored locally on the backend.
5. The application performs speech synthesis but does not translate text between languages.
6. Audio lifecycle cleanup can be enhanced for long-running production deployments.
7. Advanced voice controls such as pitch and speaking rate are not currently exposed.

---

# 31. Future Enhancements

Possible future improvements include:

* MP3 export
* OGG export
* Additional languages
* Additional Piper voices
* Voice preview
* Speech rate control
* Pitch control
* Audio history
* Saved projects
* User accounts
* Authentication
* Cloud deployment
* Persistent object storage
* Automatic audio cleanup
* Background TTS jobs
* Batch speech generation
* Generation progress tracking
* Advanced voice metadata
* Text-to-speech history
* Multi-format export

---

# 32. Development Principles

The project follows several core development principles:

### Separation of Concerns

Frontend, API communication, business logic, TTS execution, storage, and error handling are separated.

### Reusable Components

The React UI is divided into reusable functional components.

### Centralized Validation

Validation logic is kept in dedicated utilities and backend validation layers.

### Centralized Error Handling

Backend exceptions are handled consistently through a global exception handler.

### Configuration over Hardcoding

Environment-specific configuration is separated from application logic.

### Security by Default

User input, file paths, API access, and error responses are handled with basic security controls.

---

# 33. Project Completion Status

**Status: Completed**

The current Talkivo implementation includes the complete core Text-to-Speech workflow.

### Completed

* React frontend
* Spring Boot backend
* REST API
* Piper TTS integration
* Local voice models
* Language selection
* Voice selection
* Text editor
* Character counter
* Word counter
* Clear functionality
* Input validation
* Speech generation
* Audio storage
* Audio delivery
* Audio player
* Play/pause
* Seek
* Volume control
* WAV download
* Loading states
* Error states
* Network failure handling
* Backend exception handling
* CORS configuration
* Audio path security
* Configurable application settings
* Backend logging
* End-to-end testing
* Responsive UI

---

# 34. Conclusion

Talkivo demonstrates a complete full-stack Text-to-Speech solution using a modern React frontend, Java Spring Boot backend, and locally hosted Piper TTS engine.

The project combines frontend engineering, REST API development, backend validation, local AI/TTS integration, audio processing, browser media functionality, responsive design, error handling, and basic security practices into a single application.

The modular architecture also provides a strong foundation for future expansion into a more advanced voice-generation platform.

---

## Talkivo

**AI Voice Studio**

> Transform your words into voice.
