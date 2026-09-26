export default function ErrorMessage({ message }) {
  if (!message) return null

  return (
    <div
      role="alert"
      className="border-l-2 border-amber-dark bg-amber/10 px-4 py-3 font-sans text-sm text-ink"
    >
      {message}
    </div>
  )
}
