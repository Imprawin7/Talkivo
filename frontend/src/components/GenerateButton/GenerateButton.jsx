export default function GenerateButton({ onClick, isGenerating, disabled }) {
  return (
    <button
      type="button"
      onClick={onClick}
      disabled={disabled || isGenerating}
      className="flex w-full items-center justify-center gap-2 bg-teal px-5 py-3 font-sans text-sm font-medium text-paper transition-colors hover:bg-teal-dark disabled:cursor-not-allowed disabled:bg-ink/20"
    >
      {isGenerating ? (
        <>
          <span className="h-3 w-3 animate-spin rounded-full border-2 border-paper/40 border-t-paper" />
          Generating…
        </>
      ) : (
        'Generate speech'
      )}
    </button>
  )
}
