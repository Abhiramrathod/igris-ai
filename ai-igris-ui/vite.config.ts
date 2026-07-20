/// <reference types="vitest/config" />
import { defineConfig } from 'vite'
import react from '@vitejs/plugin-react'

export default defineConfig({
  plugins: [react()],
  build: {
    outDir: '../ai-igris-app/src/main/resources/static',
    emptyOutDir: true,
  },
  server: {
    port: 3000,
    proxy: {
      '/ai': {
        target: 'http://localhost:8080',
        changeOrigin: true,
      }
    }
  },
  test: {
    globals: true,
    environment: 'jsdom',
    setupFiles: './src/test/setup.ts',
    css: true,
  }
})
