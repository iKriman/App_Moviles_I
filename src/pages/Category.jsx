import React, { useState } from 'react';
import { Link, useParams } from 'react-router';
import { isBooks, isCategories } from '../api/contracts';
import { useCatalog } from '../hooks/useCatalog';
import { normalize } from '../lib/format';
import QueryFeedback, { LoadingCards } from '../components/QueryFeedback';
import FormField from '../components/FormField';
import BookCard from '../components/catalog/BookCard';
export default function Category() {
  const { categoryId } = useParams();
  const categories = useCatalog('categories', isCategories);
  const query = useCatalog('books?category=' + encodeURIComponent(categoryId), isBooks);
  const [search, setSearch] = useState('');
  const [sort, setSort] = useState('title');
  const category = categories.data?.find((item) => item.id === categoryId);
  const books = query.data
    ?.filter((book) => normalize(book.title + ' ' + book.author).includes(normalize(search)))
    .toSorted((a, b) =>
      sort === 'price' ? a.price - b.price : a.title.localeCompare(b.title, 'es'),
    );
  return (
    <section className="page-width pt-10">
      <Link className="back-link" to="/categorias">
        ← Todas las categorías
      </Link>
      <p className="eyebrow mt-8">COLECCIÓN ENTRE LÍNEAS</p>
      <h1 className="font-serif text-4xl text-ink sm:text-5xl">
        {category?.name || 'Libros de la colección'}
      </h1>
      <p className="mb-8 mt-4 text-sm leading-7 text-muted">
        {category?.description || 'Descubre los títulos disponibles en esta categoría.'}
      </p>
      <QueryFeedback query={categories} />
      <QueryFeedback query={query} />
      {query.pending && <LoadingCards label="Abriendo esta estantería…" />}
      {query.data && (
        <>
          <div className="mb-8 flex flex-col items-start justify-between gap-5 border-y border-line py-5 sm:flex-row sm:items-end">
            <div className="w-full sm:max-w-sm">
              <FormField id="book-search" label="Buscar título o autor">
                <input
                  type="search"
                  placeholder="Escribe un título o autor…"
                  value={search}
                  onChange={(event) => setSearch(event.target.value)}
                />
              </FormField>
            </div>
            <div className="flex w-full items-end justify-between gap-6 sm:w-auto">
              <span role="status" className="whitespace-nowrap py-3 text-xs text-muted">
                {books.length} {books.length === 1 ? 'libro' : 'libros'}
              </span>
              <FormField id="book-sort" label="Ordenar por">
                <select value={sort} onChange={(event) => setSort(event.target.value)}>
                  <option value="title">Título A–Z</option>
                  <option value="price">Menor precio</option>
                </select>
              </FormField>
            </div>
          </div>
          {books.length ? (
            <div className="grid gap-x-6 gap-y-10 sm:grid-cols-2 lg:grid-cols-3 xl:grid-cols-4">
              {books.map((book) => (
                <BookCard key={book.id} book={book} />
              ))}
            </div>
          ) : (
            <section className="empty-state">
              <span aria-hidden="true" className="text-4xl text-forest">
                ≋
              </span>
              <h2 className="mt-4 font-serif text-2xl">
                {search ? 'No encontramos esa lectura' : 'Esta estantería está por llenarse'}
              </h2>
              <p className="mx-auto mt-3 max-w-md text-sm leading-7 text-muted">
                {search
                  ? 'Prueba con otro título o autor.'
                  : 'Aún no hay libros disponibles en esta categoría. Mientras tanto, puedes explorar otras colecciones.'}
              </p>
              {search ? (
                <button className="button-secondary mt-5" onClick={() => setSearch('')}>
                  Limpiar búsqueda
                </button>
              ) : (
                <Link className="button-primary mt-5" to="/categorias">
                  Explorar otras categorías
                </Link>
              )}
            </section>
          )}
        </>
      )}
    </section>
  );
}
