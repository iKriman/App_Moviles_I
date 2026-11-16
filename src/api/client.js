import { saveSnapshot, readSnapshot } from '../lib/storage.js';
export class ApiError extends Error {
  constructor(kind, message, status) {
    super(message);
    this.name = 'ApiError';
    this.kind = kind;
    this.status = status;
  }
}
export function createApiClient({
  baseUrl,
  storage,
  fetchImpl = fetch,
  timeoutMs = 8000,
  scenario = '',
}) {
  async function get(path, validate, signal) {
    const controller = new AbortController();
    const cancel = () => controller.abort(signal.reason);
    if (signal?.aborted) controller.abort(signal.reason);
    else signal?.addEventListener('abort', cancel, { once: true });
    const timer = setTimeout(
      () => controller.abort(new DOMException('Tiempo agotado', 'TimeoutError')),
      timeoutMs,
    );
    try {
      const headers = { Accept: 'application/json' };
      if (scenario) headers['x-demo-scenario'] = scenario;
      const response = await fetchImpl(new URL(path, baseUrl), {
        headers,
        signal: controller.signal,
      });
      if (!response.ok)
        throw new ApiError(
          response.status === 404 ? 'not-found' : 'server',
          response.status === 404
            ? 'No encontramos el contenido solicitado.'
            : 'El catálogo no está disponible temporalmente.',
          response.status,
        );
      let payload;
      try {
        payload = await response.json();
      } catch {
        throw new ApiError('invalid', 'El catálogo respondió con un formato que no podemos leer.');
      }
      if (!validate(payload?.data))
        throw new ApiError('invalid', 'Los datos recibidos están incompletos. Inténtalo de nuevo.');
      const canSave = storage
        ? saveSnapshot(storage, new URL(path, baseUrl).href, payload.data, validate)
        : false;
      return { data: payload.data, savedAt: Date.now(), canSave };
    } catch (error) {
      if (signal?.aborted) throw error;
      if (controller.signal.aborted)
        throw new ApiError(
          'timeout',
          'La consulta está tardando demasiado. Puedes volver a intentarlo.',
        );
      if (error instanceof ApiError) throw error;
      throw new ApiError(
        'network',
        'No pudimos conectar con el catálogo. Revisa tu conexión e inténtalo de nuevo.',
      );
    } finally {
      clearTimeout(timer);
      signal?.removeEventListener('abort', cancel);
    }
  }
  return {
    get,
    saved: (path, validate) =>
      storage ? readSnapshot(storage, new URL(path, baseUrl).href, validate) : null,
  };
}
