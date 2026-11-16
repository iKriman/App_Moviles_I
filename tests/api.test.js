import test, { before, after, afterEach } from 'node:test';
import assert from 'node:assert/strict';
import { setupServer } from 'msw/node';
import { http, HttpResponse } from 'msw';
import { createHandlers } from '../src/mocks/handlers.js';
import { createApiClient } from '../src/api/client.js';
import { isCategories, isBooks, isBook } from '../src/api/contracts.js';
import { createQueryClient, catalogOptions } from '../src/api/queries.js';
const baseUrl = 'http://catalog.test/api/';
const server = setupServer(...createHandlers(baseUrl, { latency: 0 }));
before(() => server.listen({ onUnhandledRequest: 'error' }));
afterEach(() => server.resetHandlers());
after(() => server.close());
function storage() {
  const data = new Map();
  return { getItem: (key) => data.get(key) ?? null, setItem: (key, value) => data.set(key, value) };
}
const client = (extra) => createApiClient({ baseUrl, storage: storage(), ...extra });

test('GET de categorías: respuesta REST válida con categorías y cantidades', async () => {
  const result = await client().get('categories', isCategories);
  assert.ok(isCategories(result.data));
  assert.equal(result.data.length, 6);
  assert.equal(result.data.find((item) => item.id === 'teatro').bookCount, 0);
});
test('GET de libros por categoría y detalle: datos relacionados coherentes', async () => {
  const api = client();
  const list = await api.get('books?category=ciencia-ficcion', isBooks);
  assert.equal(list.data.length, 2);
  assert.ok(list.data.every((book) => book.categoryId === 'ciencia-ficcion'));
  const detail = await api.get('books/' + list.data[0].id, isBook);
  assert.deepEqual(detail.data, list.data[0]);
});
test('categoría vacía devuelve 200 y [] en lugar de confundirse con un error', async () => {
  const result = await client().get('books?category=teatro', isBooks);
  assert.deepEqual(result.data, []);
});
test('categoría y libro inexistentes devuelven 404 con error identificable', async () => {
  for (const [path, validate] of [
    ['books?category=no-existe', isBooks],
    ['books/no-existe', isBook],
  ]) {
    await assert.rejects(
      client().get(path, validate),
      (error) => error.kind === 'not-found' && error.status === 404,
    );
  }
});
test('500, red y respuesta inválida producen mensajes diferenciados', async () => {
  for (const [scenario, kind] of [
    ['500', 'server'],
    ['network', 'network'],
    ['invalid', 'invalid'],
  ]) {
    await assert.rejects(
      client({ scenario }).get('categories', isCategories),
      (error) => error.kind === kind && error.message.length > 20,
    );
  }
});
test('JSON ilegible no se presenta como un catálogo vacío', async () => {
  server.use(
    http.get(
      baseUrl + 'categories',
      () => new HttpResponse('{broken', { headers: { 'Content-Type': 'application/json' } }),
    ),
  );
  await assert.rejects(
    client().get('categories', isCategories),
    (error) => error.kind === 'invalid',
  );
});
test('el servicio puede recuperarse tras un fallo y un reintento explícito', async () => {
  server.use(...createHandlers(baseUrl, { latency: 0 }));
  const api = client({ scenario: 'once' });
  await assert.rejects(api.get('categories', isCategories), (error) => error.status === 500);
  const result = await api.get('categories', isCategories);
  assert.equal(result.data.length, 6);
});
test('timeout cancela la petición y devuelve una explicación específica', async () => {
  const fetchImpl = (_, { signal }) =>
    new Promise((resolve, reject) =>
      signal.addEventListener('abort', () => reject(signal.reason), { once: true }),
    );
  await assert.rejects(
    client({ fetchImpl, timeoutMs: 10 }).get('categories', isCategories),
    (error) => error.kind === 'timeout',
  );
});
test('cancelación del consumidor se propaga sin convertirse en error de red', async () => {
  const controller = new AbortController();
  controller.abort(new DOMException('Cancelada', 'AbortError'));
  const fetchImpl = (_, { signal }) => Promise.reject(signal.reason);
  await assert.rejects(
    client({ fetchImpl }).get('categories', isCategories, controller.signal),
    (error) => error.name === 'AbortError',
  );
});
test('fallo de almacenamiento no impide mostrar una respuesta REST correcta', async () => {
  const result = await client({
    storage: {
      getItem: () => null,
      setItem: () => {
        throw new Error('sin espacio');
      },
    },
  }).get('categories', isCategories);
  assert.equal(result.data.length, 6);
  assert.equal(result.canSave, false);
});
test('última consulta correcta queda disponible y un error no la sobrescribe', async () => {
  const store = storage();
  const normal = client({ storage: store });
  const result = await normal.get('categories', isCategories);
  await assert.rejects(client({ storage: store, scenario: '500' }).get('categories', isCategories));
  assert.deepEqual(normal.saved('categories', isCategories).data, result.data);
});
test('consultas simultáneas e inmediatamente repetidas reutilizan una sola petición', async () => {
  let requests = 0;
  server.use(
    http.get(baseUrl + 'categories', () => {
      requests++;
      return HttpResponse.json({ data: [] });
    }),
  );
  const queryClient = createQueryClient();
  const options = catalogOptions(client(), 'categories', isCategories);
  const [first, second] = await Promise.all([
    queryClient.fetchQuery(options),
    queryClient.fetchQuery(options),
  ]);
  assert.deepEqual(first, second);
  await queryClient.fetchQuery(options);
  assert.equal(requests, 1);
  queryClient.clear();
});
test('claves distintas conservan resultados por categoría sin mezclar datos', async () => {
  const queryClient = createQueryClient();
  const api = client();
  const first = await queryClient.fetchQuery(
    catalogOptions(api, 'books?category=ficcion', isBooks),
  );
  const second = await queryClient.fetchQuery(
    catalogOptions(api, 'books?category=misterio', isBooks),
  );
  assert.ok(first.data.every((book) => book.categoryId === 'ficcion'));
  assert.ok(second.data.every((book) => book.categoryId === 'misterio'));
  queryClient.clear();
});
test('mock y cliente funcionan bajo una subcarpeta de publicación', async () => {
  const subBase = 'http://catalog.test/nuevo-repositorio/api/';
  server.use(...createHandlers(subBase, { latency: 0 }));
  const result = await createApiClient({ baseUrl: subBase }).get('categories', isCategories);
  assert.equal(result.data.length, 6);
});
