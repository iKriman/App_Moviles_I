import { http, HttpResponse, delay } from 'msw';
import { categories, books } from './catalog.js';
export function createHandlers(apiBase, { latency = 650 } = {}) {
  let failedOnce = false;
  async function failure(request) {
    const scenario = request.headers.get('x-demo-scenario');
    await delay(scenario === 'slow' ? 3500 : latency);
    if (scenario === 'network') return HttpResponse.error();
    if (scenario === '500' || (scenario === 'once' && !failedOnce)) {
      failedOnce = true;
      return HttpResponse.json(
        { message: 'El catálogo no está disponible temporalmente.' },
        { status: 500 },
      );
    }
    if (scenario === '404')
      return HttpResponse.json({ message: 'Recurso no encontrado.' }, { status: 404 });
    if (scenario === 'invalid') return HttpResponse.json({ data: 'respuesta inválida' });
    return null;
  }
  function categoryData() {
    return categories.map((category) => ({
      ...category,
      bookCount: books.filter((book) => book.categoryId === category.id).length,
    }));
  }
  return [
    http.get(apiBase + 'categories', async ({ request }) => {
      const error = await failure(request);
      if (error) return error;
      return HttpResponse.json({
        data: request.headers.get('x-demo-scenario') === 'empty' ? [] : categoryData(),
      });
    }),
    http.get(apiBase + 'books', async ({ request }) => {
      const error = await failure(request);
      if (error) return error;
      const category = new URL(request.url).searchParams.get('category');
      if (category && !categories.some((item) => item.id === category))
        return HttpResponse.json({ message: 'Categoría no encontrada.' }, { status: 404 });
      return HttpResponse.json({
        data:
          request.headers.get('x-demo-scenario') === 'empty'
            ? []
            : books.filter((book) => !category || book.categoryId === category),
      });
    }),
    http.get(apiBase + 'books/:id', async ({ request, params }) => {
      const error = await failure(request);
      if (error) return error;
      const book = books.find((item) => item.id === params.id);
      return book
        ? HttpResponse.json({ data: book })
        : HttpResponse.json({ message: 'Libro no encontrado.' }, { status: 404 });
    }),
  ];
}
