import React from 'react';
export default function BookCover({ book, large = false }) {
  return (
    <div
      aria-hidden="true"
      className={'book-cover cover-' + book.color + (large ? ' book-cover-large' : '')}
    >
      <span className="book-edition">BIBLIOTECA ENTRE LÍNEAS</span>
      <span className="book-ornament">✳</span>
      <strong>{book.title}</strong>
      <span className="book-author">{book.author}</span>
      <span className="book-imprint">EL</span>
    </div>
  );
}
