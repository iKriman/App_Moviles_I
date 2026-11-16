import React from 'react';
import { Link } from 'react-router';
import { dateTime } from '../lib/format';
export function LoadingCards({ label = 'Cargando catálogo…' }) {
  return (
    <div role="status" aria-live="polite">
      <p className="mb-5 flex items-center gap-2 text-sm text-muted">
        <span className="loader" aria-hidden="true" />
        {label}
      </p>
      <div aria-hidden="true" className="grid gap-4 sm:grid-cols-2 lg:grid-cols-3">
        {[1, 2, 3, 4, 5, 6].map((n) => (
          <div key={n} className="skeleton h-52 rounded-xl bg-stone-200/60" />
        ))}
      </div>
    </div>
  );
}
export default function QueryFeedback({ query, homeLink = true }) {
  return (
    <>
      {query.fetching && !query.pending && (
        <p role="status" className="mb-5 flex items-center gap-2 text-xs text-muted">
          <span className="loader" aria-hidden="true" />
          Actualizando catálogo…
        </p>
      )}
      {query.error && (
        <section role="alert" className="mb-6 rounded-xl border border-orange-200 bg-orange-50 p-5">
          <h2 className="font-semibold text-ink">
            {query.error.kind === 'not-found'
              ? 'Este contenido no está disponible'
              : 'No pudimos completar la consulta'}
          </h2>
          <p className="mt-2 text-sm leading-6 text-muted">{query.error.message}</p>
          {query.data && !query.usingSaved && (
            <p className="mt-2 text-xs text-muted">
              La última consulta disponible sigue visible mientras se recupera el servicio.
            </p>
          )}
          <div className="mt-4 flex flex-wrap gap-3">
            <button className="button-primary" onClick={query.retry} disabled={query.fetching}>
              {query.fetching ? 'Consultando…' : 'Volver a intentar'}
            </button>
            {query.saved && !query.usingSaved && !query.data && (
              <button className="button-secondary" onClick={query.useSaved}>
                Usar última consulta guardada
              </button>
            )}
            {homeLink && (
              <Link to="/categorias" className="button-secondary">
                Explorar otras categorías
              </Link>
            )}
          </div>
        </section>
      )}
      {query.usingSaved && (
        <p
          role="status"
          className="mb-6 rounded-lg border border-sage/40 bg-sage/10 px-4 py-3 text-sm text-forest"
        >
          Mostrando una copia guardada el {dateTime(query.savedAt)}. Puede estar desactualizada.
        </p>
      )}
      {query.data && query.canSave === false && !query.usingSaved && (
        <p className="mb-5 text-xs text-muted">
          No se pudo guardar una copia en este navegador. El catálogo actual sigue disponible.
        </p>
      )}
    </>
  );
}
