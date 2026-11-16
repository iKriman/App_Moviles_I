import React from 'react';
import { Link } from 'react-router';
export default function CategoryCard({ category, index }) {
  return (
    <Link to={'/categorias/' + category.id} className={'category-card category-' + category.color}>
      <div className="flex items-start justify-between">
        <span className="text-[10px] font-medium tracking-widest opacity-65">
          {String(index + 1).padStart(2, '0')} / COLECCIÓN
        </span>
        <span aria-hidden="true" className="text-4xl">
          {category.symbol}
        </span>
      </div>
      <h3 className="mt-6 font-serif text-2xl">{category.name}</h3>
      <p className="mt-2 max-w-xs text-xs leading-6 opacity-80">{category.description}</p>
      <div className="mt-6 flex items-center justify-between border-t border-current/15 pt-4 text-xs">
        <span>
          {category.bookCount} {category.bookCount === 1 ? 'libro' : 'libros'}
        </span>
        <span aria-hidden="true">Explorar ↗</span>
      </div>
    </Link>
  );
}
