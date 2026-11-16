import test from 'node:test';
import assert from 'node:assert/strict';
import { isCategories, isBook, isBooks } from '../src/api/contracts.js';
import { books, categories } from '../src/mocks/catalog.js';
import { SNAPSHOT_KEY, readSnapshot, saveSnapshot } from '../src/lib/storage.js';
import { normalize } from '../src/lib/format.js';
function store(raw = null) {
  let value = raw;
  return {
    getItem: () => value,
    setItem: (_, next) => {
      value = next;
    },
  };
}
test('contratos rechazan duplicados, precios inválidos y campos ausentes', () => {
  assert.ok(isBooks(books));
  assert.ok(!isBooks([books[0], books[0]]));
  assert.ok(!isBook({ ...books[0], price: -1 }));
  assert.ok(!isBook({ ...books[0], title: null }));
  assert.ok(!isCategories(categories));
  assert.ok(isCategories([]));
});
test('una copia guardada conserva datos y fecha, y exige validación al leer', () => {
  const storage = store();
  assert.equal(saveSnapshot(storage, 'books', books, isBooks, 12345), true);
  assert.deepEqual(readSnapshot(storage, 'books', isBooks), { data: books, savedAt: 12345 });
  assert.equal(readSnapshot(storage, 'books', isCategories), null);
  assert.equal(readSnapshot(storage, 'missing', isBooks), null);
});
test('JSON corrupto y esquemas desconocidos no rompen la aplicación', () => {
  for (const value of ['{broken', 'null', '{}', JSON.stringify({ version: 2, entries: {} })])
    assert.equal(readSnapshot(store(value), 'books', isBooks), null);
});
test('datos inválidos no sobrescriben una copia correcta', () => {
  const storage = store();
  saveSnapshot(storage, 'books', books, isBooks);
  assert.equal(saveSnapshot(storage, 'books', null, isBooks), false);
  assert.equal(readSnapshot(storage, 'books', isBooks).data.length, books.length);
});
test('contrato getItem/setItem admite almacenamiento bloqueado sin propagar errores', () => {
  const storage = {
    getItem: () => {
      throw new Error('bloqueado');
    },
    setItem: () => {
      throw new Error('bloqueado');
    },
  };
  assert.equal(readSnapshot(storage, SNAPSHOT_KEY, isBooks), null);
  assert.equal(saveSnapshot(storage, 'books', books, isBooks), false);
});
test('búsqueda compartida normaliza tildes y mayúsculas', () => {
  assert.equal(normalize('FICCIÓN'), 'ficcion');
  assert.equal(normalize('Poesía'), 'poesia');
});
