import React from 'react';
import { Link } from 'react-router';
import CategoryGrid from '../components/catalog/CategoryGrid';
export default function Home() {
  return (
    <>
      <section className="page-width grid items-center gap-8 pb-14 pt-10 lg:grid-cols-[1.1fr_1fr] lg:gap-16 lg:pb-16 lg:pt-14">
        <div>
          <p className="eyebrow">PARA LOS QUE SIEMPRE QUIEREN UNA PÁGINA MÁS</p>
          <h1 className="max-w-xl font-serif text-5xl leading-[1.12] tracking-tight text-ink sm:text-6xl">
            Cada libro,
            <br />
            un nuevo <em className="font-normal text-forest">comienzo.</em>
          </h1>
          <p className="mt-6 max-w-md text-sm leading-7 text-muted">
            Hay historias que te llevan lejos y otras que te acercan a ti. Encuentra la tuya entre
            nuestras categorías.
          </p>
          <Link className="button-primary mt-7" to="/categorias">
            Encuentra tu próxima lectura <span aria-hidden="true">↗</span>
          </Link>
          <p className="mt-6 text-[10px] tracking-wider text-muted">
            FICCIÓN · MISTERIO · FANTASÍA · Y MUCHO MÁS
          </p>
        </div>
        <div aria-hidden="true" className="hero-art">
          <span className="art-caption">
            UN PEQUEÑO REFUGIO
            <br />
            PARA GRANDES HISTORIAS
          </span>
          <span className="art-star">✳</span>
          <div className="hero-book hero-book-back">
            <span>
              EL ARTE DE
              <br />
              IMAGINAR
            </span>
            <small>UN MUNDO POR DESCUBRIR</small>
          </div>
          <div className="hero-book hero-book-front">
            <small>BIBLIOTECA ENTRE LÍNEAS</small>
            <span>
              Todo empieza
              <br />
              con una
              <br />
              <em>historia.</em>
            </span>
            <b>EL</b>
          </div>
          <span className="art-foot">LEE A TU PROPIO RITMO.</span>
        </div>
      </section>
      <section className="border-y border-line bg-paper-dark/60">
        <div className="page-width flex flex-col justify-between gap-3 py-5 text-xs text-muted sm:flex-row">
          <span>✧ &nbsp; Clásicos para volver a descubrir</span>
          <span>✧ &nbsp; Colecciones para cada curiosidad</span>
          <span>✧ &nbsp; Una lectura, muchas posibilidades</span>
        </div>
      </section>
      <section className="page-width pt-14">
        <div className="mb-8 flex flex-wrap items-end justify-between gap-4">
          <div>
            <p className="eyebrow">ELIGE TU PRÓXIMO DESTINO</p>
            <h2 className="font-serif text-3xl text-ink sm:text-4xl">Entre géneros e historias.</h2>
          </div>
          <Link
            className="text-xs font-semibold text-forest underline underline-offset-4"
            to="/categorias"
          >
            Ver todas las categorías ↗
          </Link>
        </div>
        <CategoryGrid />
      </section>
    </>
  );
}
