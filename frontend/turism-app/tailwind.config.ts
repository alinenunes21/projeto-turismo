import type { Config } from 'tailwindcss'

export default {
  content: [
    "./index.html",
    "./src/**/*.{js,ts,jsx,tsx}",
  ],
  theme: {
    extend: {
      colors: {
        primary: {
          50: '#eff6ff',
          100: '#dbeafe',
          200: '#bfdbfe',
          300: '#93c5fd',
          400: '#60a5fa',
          500: '#673bf6ff',
          600: '#6425ebff',
          700: '#5e1dd8ff',
          800: '#4e1eafff',
          900: '#461e8aff',
        }
      }
    },
  },
  plugins: [],
} satisfies Config