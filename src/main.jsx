import React from 'react';
import { createRoot } from 'react-dom/client';
import { HashRouter } from 'react-router';
import { QueryClientProvider } from '@tanstack/react-query';
import { createQueryClient } from './api/queries';
import { ApiProvider } from './api/ApiProvider';
import App from './App';
import './styles.css';
const root = createRoot(document.getElementById('root'));
const queryClient = createQueryClient();
function StartupError() {
  return (
    <main className="page-width py-20">
      <h1 className="font-serif text-3xl text-ink">No pudimos abrir la librería</h1>
      <p className="mt-5 max-w-xl text-sm leading-7 text-muted">
        El catálogo no pudo iniciarse. Comprueba la conexión, permite los servicios del sitio y
        vuelve a cargar la página.
      </p>
      <button className="button-primary mt-6" onClick={() => window.location.reload()}>
        Volver a cargar
      </button>
    </main>
  );
}
async function start() {
  try {
    if (import.meta.env.VITE_API_MODE !== 'real') {
      const { startMock } = await import('./mocks/browser');
      await startMock();
    }
    root.render(
      <React.StrictMode>
        <QueryClientProvider client={queryClient}>
          <ApiProvider>
            <HashRouter>
              <App />
            </HashRouter>
          </ApiProvider>
        </QueryClientProvider>
      </React.StrictMode>,
    );
  } catch {
    root.render(<StartupError />);
  }
}
root.render(
  <p role="status" className="page-width py-20 text-center text-sm text-muted">
    Abriendo EntreLíneas…
  </p>,
);
void start();
