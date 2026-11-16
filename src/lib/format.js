export const normalize = (value) =>
  value
    .normalize('NFD')
    .replace(/[\u0300-\u036f]/g, '')
    .toLowerCase();
export const currency = (value) =>
  new Intl.NumberFormat('es-CL', {
    style: 'currency',
    currency: 'CLP',
    maximumFractionDigits: 0,
  }).format(value);
export const dateTime = (value) =>
  new Intl.DateTimeFormat('es-CL', { dateStyle: 'short', timeStyle: 'short' }).format(
    new Date(value),
  );
