import React, { createContext, useContext, useState } from 'react';
import { createApiClient } from './client';
import { browserStorage } from '../lib/storage';
const ApiContext = createContext(null);
export function ApiProvider({ children }) {
  const [api] = useState(() => {
    const defaultUrl = new URL('api/', new URL(import.meta.env.BASE_URL, window.location.origin))
      .href;
    const baseUrl = import.meta.env.VITE_API_URL || defaultUrl;
    return createApiClient({
      baseUrl: baseUrl.endsWith('/') ? baseUrl : baseUrl + '/',
      storage: browserStorage(),
      scenario:
        import.meta.env.VITE_API_MODE !== 'real'
          ? new URLSearchParams(window.location.search).get('escenario') || ''
          : '',
    });
  });
  return <ApiContext.Provider value={api}>{children}</ApiContext.Provider>;
}
export function useApi() {
  const api = useContext(ApiContext);
  if (!api) throw new Error('useApi debe estar dentro de ApiProvider.');
  return api;
}
