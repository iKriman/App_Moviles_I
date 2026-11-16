import React, { useEffect, useRef } from 'react';
import { Outlet, useLocation } from 'react-router';
import Header from './Header';
import Footer from './Footer';
export default function SiteLayout() {
  const main = useRef(null);
  const { pathname } = useLocation();
  useEffect(() => {
    document.title =
      'EntreLíneas · ' +
      (pathname === '/'
        ? 'Librería'
        : pathname === '/categorias'
          ? 'Categorías'
          : pathname.startsWith('/libros/')
            ? 'Detalle del libro'
            : pathname.startsWith('/categorias/')
              ? 'Colección'
              : 'Página no encontrada');
    main.current?.focus();
    window.scrollTo(0, 0);
  }, [pathname]);
  return (
    <>
      <a
        className="skip-link"
        href="#contenido"
        onClick={(event) => {
          event.preventDefault();
          main.current?.focus();
        }}
      >
        Saltar al contenido
      </a>
      <Header />
      <main ref={main} id="contenido" tabIndex={-1} className="outline-none">
        <Outlet />
      </main>
      <Footer />
    </>
  );
}
