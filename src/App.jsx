import React from 'react';
import { Route, Routes } from 'react-router';
import SiteLayout from './components/layout/SiteLayout';
import Home from './pages/Home';
import Categories from './pages/Categories';
import Category from './pages/Category';
import Book from './pages/Book';
import NotFound from './pages/NotFound';
export default function App() {
  return (
    <Routes>
      <Route element={<SiteLayout />}>
        <Route index element={<Home />} />
        <Route path="categorias" element={<Categories />} />
        <Route path="categorias/:categoryId" element={<Category />} />
        <Route path="libros/:bookId" element={<Book />} />
        <Route path="*" element={<NotFound />} />
      </Route>
    </Routes>
  );
}
