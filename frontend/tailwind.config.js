/** @type {import('tailwindcss').Config} */
export default {
  content: ['./index.html', './src/**/*.{js,jsx}'],
  theme: {
    extend: {
      colors: {
        paper: '#EFEDE6',
        ink: '#1E2A28',
        surface: '#FFFFFF',
        teal: {
          DEFAULT: '#2F6F64',
          dark: '#234F47',
        },
        amber: {
          DEFAULT: '#D9A441',
          dark: '#B5842E',
        },
        line: '#D7D3C7',
      },
      fontFamily: {
        display: ['Fraunces', 'serif'],
        sans: ['"IBM Plex Sans"', 'sans-serif'],
      },
    },
  },
  plugins: [],
}
