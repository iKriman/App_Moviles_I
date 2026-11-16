import { setupWorker } from 'msw/browser';
import { createHandlers } from './handlers';
export async function startMock() {
  const appBase = new URL(import.meta.env.BASE_URL, window.location.origin);
  const worker = setupWorker(...createHandlers(new URL('api/', appBase).href));
  await worker.start({
    serviceWorker: {
      url: new URL('mockServiceWorker.js', appBase).href,
      options: { scope: appBase.pathname },
    },
    onUnhandledRequest: 'bypass',
    quiet: true,
  });
  return worker;
}
