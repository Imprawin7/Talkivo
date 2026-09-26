
import { countWords } from '../../utils/validators'
import { MAX_TEXT_LENGTH } from '../../utils/constants'

export default function TextInput({ value, onChange }) {
  const charCount = value.length
  const wordCount = countWords(value)
  const overLimit = charCount > MAX_TEXT_LENGTH
  const remaining = MAX_TEXT_LENGTH - charCount

  const handleClear = () => {
    onChange('')
  }

  return (
    <div className="talkivo-text-editor">
      <div className="talkivo-text-editor-top">
        <div className="talkivo-text-editor-label">
          <span className="talkivo-text-editor-dot" />
          <label htmlFor="tts-text">Script</label>
        </div>

        <div className="talkivo-text-editor-actions">
          <span className="talkivo-text-editor-limit">
            {remaining >= 0
              ? `${remaining} remaining`
              : 'Limit exceeded'}
          </span>

          {value && (
            <button
              type="button"
              className="talkivo-clear-button"
              onClick={handleClear}
              aria-label="Clear text"
            >
              <svg
                viewBox="0 0 24 24"
                fill="none"
                stroke="currentColor"
                strokeWidth="1.8"
                strokeLinecap="round"
                aria-hidden="true"
              >
                <path d="M4 7h16" />
                <path d="M10 11v6" />
                <path d="M14 11v6" />
                <path d="M6 7l1 13h10l1-13" />
                <path d="M9 7V4h6v3" />
              </svg>

              <span>Clear</span>
            </button>
          )}
        </div>
      </div>

      <textarea
        id="tts-text"
        value={value}
        onChange={(e) => onChange(e.target.value)}
        placeholder="Write something worth hearing..."
        className={`talkivo-textarea ${
          overLimit ? 'talkivo-textarea-error' : ''
        }`}
        maxLength={MAX_TEXT_LENGTH + 100}
        spellCheck="true"
      />

      <div className="talkivo-text-editor-bottom">
        <div className="talkivo-text-stats">
          <span>
            <strong>{wordCount}</strong> words
          </span>

          <span className="talkivo-stat-divider" />

          <span className={overLimit ? 'talkivo-count-error' : ''}>
            <strong>{charCount}</strong> / {MAX_TEXT_LENGTH} characters
          </span>
        </div>

        <div
          className={`talkivo-text-status ${
            overLimit ? 'error' : value ? 'active' : ''
          }`}
        >
          <span />
          {overLimit
            ? `${Math.abs(remaining)} over limit`
            : value
              ? 'Ready'
              : 'Start writing'}
        </div>
      </div>
    </div>
  )
}
