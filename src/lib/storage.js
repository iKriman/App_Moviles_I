export const SNAPSHOT_KEY = 'entrelineas-catalogo-v1';
export function browserStorage() {
  return {
    getItem: (key) => window.localStorage.getItem(key),
    setItem: (key, value) => window.localStorage.setItem(key, value),
  };
}
function readEntries(storage) {
  const raw = storage.getItem(SNAPSHOT_KEY);
  if (raw === null) return {};
  const parsed = JSON.parse(raw);
  return parsed?.version === 1 &&
    parsed.entries &&
    typeof parsed.entries === 'object' &&
    !Array.isArray(parsed.entries)
    ? parsed.entries
    : {};
}
export function readSnapshot(storage, key, validate) {
  try {
    const entry = readEntries(storage)[key];
    if (!entry || !Number.isFinite(entry.savedAt) || !validate(entry.data)) return null;
    return entry;
  } catch {
    return null;
  }
}
export function saveSnapshot(storage, key, data, validate, savedAt = Date.now()) {
  if (!validate(data)) return false;
  try {
    let entries;
    try {
      entries = readEntries(storage);
    } catch {
      entries = {};
    }
    storage.setItem(
      SNAPSHOT_KEY,
      JSON.stringify({ version: 1, entries: { ...entries, [key]: { data, savedAt } } }),
    );
    return true;
  } catch {
    return false;
  }
}
