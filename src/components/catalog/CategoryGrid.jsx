import React, { useState } from 'react';
import { isCategories } from '../../api/contracts';
import { useCatalog } from '../../hooks/useCatalog';
import { normalize } from '../../lib/format';
import FormField from '../FormField';
import QueryFeedback, { LoadingCards } from '../QueryFeedback';
import CategoryCard from './CategoryCard';
export default function CategoryGrid({ filters = false }) {
  const query = useCatalog('categories', isCategories);
  const [search, setSearch] = useState('');
  const categories = query.data?.filter((category) =>
    normalize(category.name).includes(normalize(search)),
  );
  return (
    <div>
      <QueryFeedback query={query} homeLink={false} />
      {query.pending && <LoadingCards label="Buscando categorías para ti…" />}
      {query.data && (
        <>
          {filters && (
            <div className="mb-7 max-w-md">
              <FormField id="category-search" label="Buscar una categoría">
                <input
                  type="search"
                  placeholder="Ficción, misterio, poesía…"
                  value={search}
                  onChange={(event) => setSearch(event.target.value)}
                />
              </FormField>
            </div>
          )}
          {categories.length ? (
            <div className="grid gap-4 sm:grid-cols-2 lg:grid-cols-3">
              {categories.map((category, index) => (
                <CategoryCard key={category.id} category={category} index={index} />
              ))}
            </div>
          ) : (
            <section className="empty-state">
              <h3 className="font-serif text-2xl">No hay categorías para mostrar</h3>
              <p className="mt-3 text-sm text-muted">
                {search
                  ? 'Prueba con otro nombre o vuelve a ver todas las categorías.'
                  : 'El catálogo todavía no tiene categorías publicadas. Puedes volver a consultar en unos momentos.'}
              </p>
              <button
                className="button-secondary mt-5"
                onClick={() => {
                  if (search) setSearch('');
                  else query.retry();
                }}
              >
                {search ? 'Limpiar búsqueda' : 'Volver a consultar'}
              </button>
            </section>
          )}
        </>
      )}
    </div>
  );
}
