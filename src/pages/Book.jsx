import React from 'react';
import { Link, useParams } from 'react-router';
import { isBook } from '../api/contracts';
import { useCatalog } from '../hooks/useCatalog';
import { currency } from '../lib/format';
import QueryFeedback, { LoadingCards } from '../components/QueryFeedback';
import BookCover from '../components/catalog/BookCover';
export default function Book() {
  const { bookId } = useParams();
  const query = useCatalog('books/' + encodeURIComponent(bookId), isBook);
  const book = query.data;
  return (
    <section className="page-width pt-10">
      <Link className="back-link" to={book ? '/categorias/' + book.categoryId : '/categorias'}>
        ← Volver a la colección
      </Link>
      <div className="mt-8">
        {!book && <h1 className="sr-only">Detalle del libro</h1>}
        <QueryFeedback query={query} />
        {query.pending && <LoadingCards label="Buscando el libro…" />}
        {book && (
          <article className="grid items-start gap-10 md:grid-cols-2 lg:gap-20">
            <div className="flex min-h-96 items-center justify-center rounded-xl bg-paper-dark py-12">
              <BookCover book={book} large />
            </div>
            <div className="pt-4">
              <p className="eyebrow">UNA HISTORIA PARA TU ESTANTERÍA</p>
              <h1 className="font-serif text-4xl leading-tight text-ink sm:text-5xl">
                {book.title}
              </h1>
              <p className="mt-5 text-sm text-muted">{book.author}</p>
              <p className="mt-8 text-2xl font-semibold text-forest">{currency(book.price)}</p>
              <p className="mt-1 text-xs text-muted">
                Precio referencial de esta edición de demostración.
              </p>
              <h2 className="mt-9 border-t border-line pt-7 font-serif text-2xl">
                Entre sus páginas
              </h2>
              <p className="mt-4 text-sm leading-8 text-muted">{book.description}</p>
              <dl className="mt-7 border-y border-line py-5 text-xs">
                <div className="flex justify-between gap-5">
                  <dt className="text-muted">Primera publicación</dt>
                  <dd>{book.year}</dd>
                </div>
              </dl>
              <Link className="button-primary mt-7" to={'/categorias/' + book.categoryId}>
                Más libros de esta colección ↗
              </Link>
            </div>
          </article>
        )}
      </div>
    </section>
  );
}
