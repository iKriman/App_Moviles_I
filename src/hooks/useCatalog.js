import { useMemo, useState } from 'react';
import { useQuery } from '@tanstack/react-query';
import { useApi } from '../api/ApiProvider';
import { catalogOptions } from '../api/queries';
export function useCatalog(path, validate) {
  const api = useApi();
  const query = useQuery(catalogOptions(api, path, validate));
  const [fallbackPath, setFallbackPath] = useState(null);
  const saved = useMemo(
    () => (query.isError ? api.saved(path, validate) : null),
    [api, path, validate, query.isError],
  );
  const usingSaved = fallbackPath === path && Boolean(saved) && query.isError;
  const result = usingSaved ? saved : query.data;
  return {
    data: result?.data,
    savedAt: result?.savedAt,
    canSave: result?.canSave,
    pending: query.isPending,
    fetching: query.isFetching,
    error: query.isError ? query.error : null,
    saved,
    usingSaved,
    useSaved: () => setFallbackPath(path),
    retry: () => {
      setFallbackPath(null);
      void query.refetch();
    },
  };
}
