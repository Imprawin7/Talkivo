export default function VoiceSelector({ voices, value, onChange, disabled }) {
  return (
    <div className="talkivo-select-field">
      <label htmlFor="voice-select">
        <span className="talkivo-field-icon">
          <svg
            viewBox="0 0 24 24"
            fill="none"
            stroke="currentColor"
            strokeWidth="1.7"
            strokeLinecap="round"
            strokeLinejoin="round"
            aria-hidden="true"
          >
            <path d="M11 5 6 9H3v6h3l5 4V5Z" />
            <path d="M15.5 8.5a5 5 0 0 1 0 7" />
            <path d="M18.5 6a8.5 8.5 0 0 1 0 12" />
          </svg>
        </span>
        <span>Voice</span>
      </label>

      <div className="talkivo-select-wrapper">
        <select
          id="voice-select"
          value={value}
          onChange={(e) => onChange(e.target.value)}
          disabled={disabled}
          className="talkivo-premium-select"
        >
          <option value="" disabled>
            {disabled ? 'Choose a language first' : 'Select a voice'}
          </option>

          {voices.map((v) => (
            <option key={v.id} value={v.id}>
              {v.name} — {v.gender}
            </option>
          ))}
        </select>

        <span className="talkivo-select-arrow">
          <svg
            viewBox="0 0 24 24"
            fill="none"
            stroke="currentColor"
            strokeWidth="1.8"
            strokeLinecap="round"
            strokeLinejoin="round"
            aria-hidden="true"
          >
            <path d="m6 9 6 6 6-6" />
          </svg>
        </span>
      </div>
    </div>
  )
}