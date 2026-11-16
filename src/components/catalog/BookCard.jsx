import React from 'react';
import { Link } from 'react-router';
import { currency } from '../../lib/format';
import BookCover from './BookCover';
export default function BookCard({ book }) {
  return (
    <Link to={'/libros/' + book.id} className="group rounded-lg focus-visible:outline-offset-8">
      <div className="flex min-h-72 items-center justify-center rounded-xl bg-paper-dark px-8 py-8 transition-colors group-hover:bg-stone-200/60">
        <BookCover book={book} />
      </div>
      <p className="mt-5 text-[10px] uppercase tracking-widest text-muted">{book.author}</p>
      <h3 className="mt-2 font-serif text-xl leading-7 text-ink group-hover:text-forest">
        {book.title}
      </h3>
      <p className="mt-3 text-sm font-semibold text-forest">
        {currency(book.price)}{' '}
        <span className="ml-2 text-xs font-normal text-muted">Ver libro ↗</span>
      </p>
    </Link>
  );
}
