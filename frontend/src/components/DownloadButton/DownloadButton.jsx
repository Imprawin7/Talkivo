
export default function DownloadButton({ src, disabled }) {
  const downloadUrl = src || '#'

  return (
    <a
      href={downloadUrl}
      download="talkivo-speech.wav"
      aria-disabled={disabled}
      className={`talkivo-download-button ${
        disabled ? 'disabled' : ''
      }`}
      onClick={(e) => {
        if (disabled || !src) {
          e.preventDefault()
        }
      }}
    >
      <span className="talkivo-download-icon">
        <svg
          viewBox="0 0 24 24"
          fill="none"
          stroke="currentColor"
          strokeWidth="1.8"
          strokeLinecap="round"
          strokeLinejoin="round"
          aria-hidden="true"
        >
          <path d="M12 3v11" />
          <path d="m7.5 10.5 4.5 4.5 4.5-4.5" />
          <path d="M5 20h14" />
        </svg>
      </span>

      <span className="talkivo-download-text">
        <strong>Download</strong>
        <small>WAV audio</small>
      </span>
    </a>
  )
}
