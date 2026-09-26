import { useEffect, useRef, useState } from 'react'
import { AUDIO_BASE_URL } from '../../utils/constants'

const BAR_COUNT = 32

export default function AudioPlayer({ src }) {
  const audioRef = useRef(null)
  const [isPlaying, setIsPlaying] = useState(false)
  const [progress, setProgress] = useState(0)
  const [duration, setDuration] = useState(0)
  const [volume, setVolume] = useState(1)

  const audioSrc = src
    ? src.startsWith('http')
      ? src
      : ${AUDIO_BASE_URL}
    : null

  useEffect(() => {
    setIsPlaying(false)
    setProgress(0)
    setDuration(0)

    if (audioRef.current) {
      audioRef.current.pause()
      audioRef.current.load()
      audioRef.current.volume = volume
    }
  }, [src])

  const togglePlay = async () => {
    const audio = audioRef.current
    if (!audio) return

    try {
      if (audio.paused) {
        await audio.play()
        setIsPlaying(true)
      } else {
        audio.pause()
        setIsPlaying(false)
      }
    } catch (error) {
      console.error('Audio playback failed:', error)
      setIsPlaying(false)
    }
  }

  const handleTimeUpdate = () => {
    const audio = audioRef.current
    if (!audio || !audio.duration) return

    setProgress(audio.currentTime / audio.duration)
  }

  const handleSeek = (e) => {
    const audio = audioRef.current
    const rect = e.currentTarget.getBoundingClientRect()

    const ratio = Math.max(
      0,
      Math.min(1, (e.clientX - rect.left) / rect.width)
    )

    if (audio?.duration) {
      audio.currentTime = ratio * audio.duration
      setProgress(ratio)
    }
  }

  const handleVolume = (e) => {
    const value = Number(e.target.value)

    setVolume(value)

    if (audioRef.current) {
      audioRef.current.volume = value
    }
  }

  if (!src) {
    return null
  }

  return (
    <div className="talkivo-player">
      <audio
        ref={audioRef}
        src={audioSrc}
        preload="metadata"
        onTimeUpdate={handleTimeUpdate}
        onLoadedMetadata={(e) => {
          setDuration(e.target.duration)
        }}
        onEnded={() => {
          setIsPlaying(false)
          setProgress(1)
        }}
        onError={(e) => {
          console.error('Audio loading error:', e)
        }}
      />

      <div className="talkivo-player-top">
        <div className="talkivo-player-info">
          <div className="talkivo-player-icon">
            <svg
              viewBox="0 0 24 24"
              fill="none"
              stroke="currentColor"
              strokeWidth="1.8"
              aria-hidden="true"
            >
              <path d="M11 5 6 9H3v6h3l5 4V5Z" />
              <path d="M15.5 8.5a5 5 0 0 1 0 7" />
              <path d="M18.5 6a8.5 8.5 0 0 1 0 12" />
            </svg>
          </div>

          <div>
            <span className="talkivo-player-title">Voice preview</span>
            <span className="talkivo-player-subtitle">
              Talkivo generated audio
            </span>
          </div>
        </div>

        <div className="talkivo-player-status">
          <span className={isPlaying ? 'playing' : ''} />
          {isPlaying ? 'Playing' : 'Ready'}
        </div>
      </div>

      <div className="talkivo-waveform-area">
        <div
          className="talkivo-waveform"
          onClick={handleSeek}
          role="slider"
          tabIndex={0}
          aria-label="Seek audio"
          aria-valuemin="0"
          aria-valuemax="100"
          aria-valuenow={Math.round(progress * 100)}
          onKeyDown={(e) => {
            if (!audioRef.current?.duration) return

            if (e.key === 'ArrowRight') {
              audioRef.current.currentTime = Math.min(
                audioRef.current.duration,
                audioRef.current.currentTime + 5
              )
            }

            if (e.key === 'ArrowLeft') {
              audioRef.current.currentTime = Math.max(
                0,
                audioRef.current.currentTime - 5
              )
            }
          }}
        >
          {Array.from({ length: BAR_COUNT }).map((_, i) => {
            const played = i / BAR_COUNT < progress
            const heights = [
              28, 44, 65, 38, 78, 52, 88, 48,
              70, 35, 60, 82, 46, 72, 92, 55,
              76, 42, 68, 86, 50, 74, 40, 62,
              84, 46, 72, 58, 88, 43, 67, 35,
            ]

            return (
              <span
                key={i}
                className={	alkivo-wave-bar  }
                style={{
                  height: ${heights[i]}%,
                }}
              />
            )
          })}

          <div
            className="talkivo-wave-progress"
            style={{ width: ${progress * 100}% }}
          />
        </div>
      </div>

      <div className="talkivo-player-controls">
        <button
          type="button"
          onClick={togglePlay}
          aria-label={isPlaying ? 'Pause audio' : 'Play audio'}
          className="talkivo-play-button"
        >
          {isPlaying ? (
            <svg
              viewBox="0 0 24 24"
              fill="currentColor"
              aria-hidden="true"
            >
              <rect x="7" y="5" width="3.5" height="14" rx="1" />
              <rect x="13.5" y="5" width="3.5" height="14" rx="1" />
            </svg>
          ) : (
            <svg
              viewBox="0 0 24 24"
              fill="currentColor"
              aria-hidden="true"
            >
              <path d="M8 5.5v13a1 1 0 0 0 1.53.85l9.7-6.5a1 1 0 0 0 0-1.7l-9.7-6.5A1 1 0 0 0 8 5.5Z" />
            </svg>
          )}
        </button>

        <div className="talkivo-time">
          <span>{formatTime(progress * duration)}</span>
          <span className="talkivo-time-divider">/</span>
          <span>{formatTime(duration)}</span>
        </div>

        <div className="talkivo-volume">
          <svg
            viewBox="0 0 24 24"
            fill="none"
            stroke="currentColor"
            strokeWidth="1.8"
            aria-hidden="true"
          >
            <path d="M11 5 6 9H3v6h3l5 4V5Z" />
            <path d="M15.5 8.5a5 5 0 0 1 0 7" />
          </svg>

          <input
            type="range"
            min="0"
            max="1"
            step="0.01"
            value={volume}
            onChange={handleVolume}
            aria-label="Volume"
            style={{
              '--volume-progress': ${volume * 100}%,
            }}
          />
        </div>
      </div>
    </div>
  )
}

function formatTime(seconds) {
  if (!seconds || Number.isNaN(seconds)) {
    return '0:00'
  }

  const minutes = Math.floor(seconds / 60)
  const remainingSeconds = Math.floor(seconds % 60)

  return ${minutes}:
}
