import TextInput from '../../components/TextInput/TextInput.jsx'
import LanguageSelector from '../../components/LanguageSelector/LanguageSelector.jsx'
import VoiceSelector from '../../components/VoiceSelector/VoiceSelector.jsx'
import GenerateButton from '../../components/GenerateButton/GenerateButton.jsx'
import AudioPlayer from '../../components/AudioPlayer/AudioPlayer.jsx'
import DownloadButton from '../../components/DownloadButton/DownloadButton.jsx'
import ErrorMessage from '../../components/ErrorMessage/ErrorMessage.jsx'
import { useTextToSpeech } from '../../hooks/useTextToSpeech.js'

export default function Home() {
  const {
    languages,
    voicesForLanguage,
    voicesError,
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
  } = useTextToSpeech()

  return (
    <div className="talkivo-app">
      <div className="talkivo-glow talkivo-glow-one" />
      <div className="talkivo-glow talkivo-glow-two" />

      <header className="talkivo-header">
        <div className="talkivo-brand">
          <div className="talkivo-logo">
            <span>T</span>
          </div>

          <div>
            <div className="talkivo-brand-name">Talkivo</div>
            <div className="talkivo-brand-subtitle">AI Voice Studio</div>
          </div>
        </div>

        <div className="talkivo-status">
          <span className="talkivo-status-dot" />
          <span>Studio Ready</span>
        </div>
      </header>

      <main className="talkivo-main">
        <section className="talkivo-hero">
          <div className="talkivo-eyebrow">
            <span>✦</span>
            TEXT TO SPEECH
          </div>

          <h1>
            Bring your words
            <br />
            <span>to life.</span>
          </h1>

          <p>
            Transform your text into natural, expressive speech
            with a voice that fits your message.
          </p>
        </section>

        <section className="talkivo-workspace">
          <div className="talkivo-editor-card">
            <div className="talkivo-card-header">
              <div>
                <span className="talkivo-card-label">Your script</span>
                <span className="talkivo-card-hint">
                  Write or paste anything you want to hear.
                </span>
              </div>

              <div className="talkivo-step">
                <span>01</span>
              </div>
            </div>

            <div className="talkivo-textarea-wrap">
              <TextInput value={text} onChange={setText} />
            </div>

            <div className="talkivo-editor-footer">
              <div className="talkivo-editor-tip">
                <span>⌘</span>
                <span>Natural speech generation</span>
              </div>

              <div className="talkivo-ready-indicator">
                <span className={text ? 'active' : ''} />
                {text ? 'Ready to generate' : 'Waiting for text'}
              </div>
            </div>
          </div>

          <div className="talkivo-control-card">
            <div className="talkivo-card-header">
              <div>
                <span className="talkivo-card-label">Voice settings</span>
                <span className="talkivo-card-hint">
                  Choose how your text should sound.
                </span>
              </div>

              <div className="talkivo-step">
                <span>02</span>
              </div>
            </div>

            {voicesError && (
              <div className="talkivo-error-wrapper">
                <ErrorMessage message={voicesError} />
              </div>
            )}

            <div className="talkivo-selectors">
              <div className="talkivo-field">
                <label>Language</label>
                <LanguageSelector
                  languages={languages}
                  value={language}
                  onChange={(v) => {
                    setLanguage(v)
                    setVoice('')
                  }}
                />
              </div>

              <div className="talkivo-field">
                <label>Voice</label>
                <VoiceSelector
                  voices={voicesForLanguage}
                  value={voice}
                  onChange={setVoice}
                  disabled={!language}
                />
              </div>
            </div>

            <div className="talkivo-generate">
              <GenerateButton
                onClick={handleGenerate}
                isGenerating={isGenerating}
                disabled={!text}
              />
            </div>

            <ErrorMessage message={error} />
          </div>

          <div className={`talkivo-output-card ${audioUrl ? 'has-audio' : ''}`}>
            <div className="talkivo-card-header">
              <div>
                <span className="talkivo-card-label">Generated voice</span>
                <span className="talkivo-card-hint">
                  {audioUrl
                    ? 'Your audio is ready to listen.'
                    : 'Your generated audio will appear here.'}
                </span>
              </div>

              <div className="talkivo-step">
                <span>03</span>
              </div>
            </div>

            {!audioUrl && !isGenerating && (
              <div className="talkivo-empty-output">
                <div className="talkivo-wave-icon">
                  <span />
                  <span />
                  <span />
                  <span />
                  <span />
                  <span />
                  <span />
                </div>

                <strong>Your voice preview</strong>
                <p>Generate speech to hear your text come alive.</p>
              </div>
            )}

            {isGenerating && (
              <div className="talkivo-generating">
                <div className="talkivo-generating-orb">
                  <span />
                </div>

                <strong>Creating your voice...</strong>
                <p>Talkivo is generating your audio.</p>
              </div>
            )}

            {audioUrl && (
              <div className="talkivo-audio-content">
                <div className="talkivo-audio-badge">
                  <span>●</span>
                  AUDIO READY
                </div>

                <AudioPlayer src={audioUrl} />

                <div className="talkivo-download">
                  <DownloadButton
                    src={audioUrl}
                    disabled={!audioUrl}
                  />
                </div>
              </div>
            )}
          </div>
        </section>
      </main>

      <footer className="talkivo-footer">
        <span>Talkivo</span>
        <span className="talkivo-footer-separator">•</span>
        <span>Natural voice generation</span>
      </footer>
    </div>
  )
}