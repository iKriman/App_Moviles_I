import React from 'react';
import { Link } from 'react-router';
export default function NotFound() {
  return (
    <section className="page-width py-24 text-center">
      <p className="eyebrow">PÁGINA 404</p>
      <h1 className="font-serif text-4xl text-ink">Esta página se salió del libro.</h1>
      <p className="mt-5 text-sm text-muted">
        La dirección que buscas no existe. Podemos empezar por otra historia.
      </p>
      <Link className="button-primary mt-7" to="/categorias">
        Volver al catálogo
      </Link>
    </section>
  );
}
