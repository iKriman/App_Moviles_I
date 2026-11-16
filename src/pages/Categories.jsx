import React from 'react';
import CategoryGrid from '../components/catalog/CategoryGrid';
export default function Categories() {
  return (
    <section className="page-width pt-12">
      <p className="eyebrow">TU PRÓXIMA LECTURA EMPIEZA AQUÍ</p>
      <h1 className="font-serif text-4xl text-ink sm:text-5xl">Un mundo por estantería.</h1>
      <p className="mb-9 mt-5 max-w-xl text-sm leading-7 text-muted">
        Explora las categorías y deja que tu curiosidad elija el camino. Cada colección tiene algo
        distinto para contar.
      </p>
      <CategoryGrid filters />
    </section>
  );
}
