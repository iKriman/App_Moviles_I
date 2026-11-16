import React from 'react';
import { Link } from 'react-router';
export default function Footer() {
  return (
    <footer className="mt-20 border-t border-line py-9">
      <div className="page-width flex flex-col justify-between gap-6 sm:flex-row">
        <div>
          <Link to="/" className="font-serif text-2xl text-forest">
            EntreLíneas
          </Link>
          <p className="mt-2 text-xs text-muted">Un lugar para descubrir tu próxima historia.</p>
        </div>
        <div className="text-xs leading-6 text-muted">
          <Link to="/categorias" className="text-forest underline underline-offset-4">
            Explorar categorías
          </Link>
          <p>Catálogo de demostración · Precios referenciales en CLP.</p>
        </div>
      </div>
    </footer>
  );
}
