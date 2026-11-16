import React from 'react';
import { Link, NavLink } from 'react-router';
export default function Header() {
  return (
    <>
      <div className="border-b border-line bg-forest px-5 py-2 text-center text-[10px] tracking-[0.08em] text-paper">
        HISTORIAS PARA ENCONTRAR TU PRÓXIMO DESTINO
      </div>
      <header className="border-b border-line bg-paper">
        <div className="page-width flex flex-wrap items-center justify-between gap-4 py-5">
          <Link
            to="/"
            className="flex items-center gap-3 text-forest"
            aria-label="EntreLíneas, inicio"
          >
            <span aria-hidden="true" className="brand-icon">
              ≋
            </span>
            <span className="font-serif text-2xl tracking-tight sm:text-3xl">
              EntreLíneas
              <small className="mt-1 block font-sans text-[8px] tracking-[0.35em]">
                LIBRERÍA & DESCUBRIMIENTOS
              </small>
            </span>
          </Link>
          <nav
            aria-label="Navegación principal"
            className="flex items-center gap-5 text-xs sm:gap-8"
          >
            <NavLink
              to="/"
              end
              className={({ isActive }) => 'nav-link ' + (isActive ? 'nav-active' : '')}
            >
              Inicio
            </NavLink>
            <NavLink
              to="/categorias"
              className={({ isActive }) => 'nav-link ' + (isActive ? 'nav-active' : '')}
            >
              Categorías
            </NavLink>
            <Link
              to="/categorias"
              className="hidden rounded-full border border-forest px-4 py-2.5 text-forest sm:block"
            >
              Explorar catálogo ↗
            </Link>
          </nav>
        </div>
      </header>
    </>
  );
}
