export default function LanguageSelector({ languages, value, onChange }) {
  return (
    <div className="talkivo-select-field">
      <label htmlFor="language-select">
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
            <circle cx="12" cy="12" r="9" />
            <path d="M3 12h18" />
            <path d="M12 3c2.2 2.4 3.4 5.4 3.4 9s-1.2 6.6-3.4 9c-2.2-2.4-3.4-5.4-3.4-9S9.8 5.4 12 3Z" />
          </svg>
        </span>
        <span>Language</span>
      </label>

      <div className="talkivo-select-wrapper">
        <select
          id="language-select"
          value={value}
          onChange={(e) => onChange(e.target.value)}
          className="talkivo-premium-select"
        >
          <option value="" disabled>
            Select a language
          </option>

          {languages.map((lang) => (
            <option key={lang} value={lang}>
              {lang.toUpperCase()}
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