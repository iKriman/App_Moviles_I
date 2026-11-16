import { defineConfig, loadEnv } from 'vite';
import tailwindcss from '@tailwindcss/vite';

export default defineConfig(({ mode }) => {
  const env = loadEnv(mode, process.cwd(), 'VITE_');

  return {
    // GitHub Pages proporciona su ruta al compilar; en local se usa la raíz.
    base: env.VITE_BASE_PATH || '/',
    plugins: [tailwindcss()],
  };
});
