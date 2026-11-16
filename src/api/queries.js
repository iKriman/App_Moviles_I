import { QueryClient } from '@tanstack/react-query';
export function createQueryClient() {
  return new QueryClient({
    defaultOptions: {
      queries: {
        staleTime: 5 * 60 * 1000,
        gcTime: 30 * 60 * 1000,
        retry: false,
        refetchOnWindowFocus: false,
        refetchOnReconnect: false,
        networkMode: 'always',
      },
    },
  });
}
export const catalogOptions = (api, path, validate) => ({
  queryKey: ['catalog', path],
  queryFn: ({ signal }) => api.get(path, validate, signal),
});
