import React, { cloneElement } from 'react';
export default function FormField({ id, label, error, children }) {
  return (
    <div>
      <label htmlFor={id} className="mb-2 block text-xs font-semibold text-ink">
        {label}
      </label>
      {cloneElement(children, {
        id,
        'aria-invalid': Boolean(error),
        'aria-describedby': error ? id + '-error' : undefined,
      })}
      {error && (
        <p id={id + '-error'} className="mt-2 text-xs text-red-800">
          {error}
        </p>
      )}
    </div>
  );
}
