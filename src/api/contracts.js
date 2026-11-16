const text = (value) => typeof value === 'string' && value.trim().length > 0;
const slug = (value) => typeof value === 'string' && /^[a-z0-9]+(?:-[a-z0-9]+)*$/.test(value);
const unique = (array) => new Set(array.map((item) => item.id)).size === array.length;
const colors = ['terracotta', 'sage', 'slate', 'ochre', 'rose', 'sand'];
export const isCategory = (item) =>
  Boolean(
    item &&
    slug(item.id) &&
    text(item.name) &&
    text(item.description) &&
    text(item.symbol) &&
    colors.includes(item.color) &&
    Number.isSafeInteger(item.bookCount) &&
    item.bookCount >= 0,
  );
export const isBook = (item) =>
  Boolean(
    item &&
    slug(item.id) &&
    text(item.title) &&
    text(item.author) &&
    slug(item.categoryId) &&
    text(item.description) &&
    colors.includes(item.color) &&
    Number.isSafeInteger(item.price) &&
    item.price > 0 &&
    Number.isSafeInteger(item.year) &&
    item.year > 0,
  );
export const isCategories = (value) =>
  Array.isArray(value) && value.every(isCategory) && unique(value);
export const isBooks = (value) => Array.isArray(value) && value.every(isBook) && unique(value);
