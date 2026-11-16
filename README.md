# EntreLíneas

Librería web para explorar categorías y descubrir libros. El catálogo se consulta mediante una API REST simulada con Mock Service Worker (MSW), con indicadores de carga, manejo de errores, caché y recuperación de consultas guardadas.

## Funcionalidades

- Inicio con categorías obtenidas desde la API.
- Búsqueda de categorías sin distinguir mayúsculas ni tildes.
- Listado de libros por categoría.
- Búsqueda por título o autor y orden por título o precio.
- Detalle de libro con autor, descripción, primera publicación y precio.
- Navegación con React Router y página no encontrada.
- Indicadores de carga inicial y actualización.
- Mensajes específicos para errores HTTP, red, tiempo de espera y respuestas inválidas.
- Reintento manual y acceso a la última consulta guardada cuando está disponible.
- Diseño adaptable a móviles, navegación por teclado y movimiento reducido.

## Tecnologías

| Tecnología          | Uso                                        |
| ------------------- | ------------------------------------------ |
| React 19            | Componentes e interfaz                     |
| React Router 7      | Rutas y navegación                         |
| MSW 2               | Mock REST en navegador y pruebas           |
| TanStack Query 5    | Caché, estado de consultas y deduplicación |
| Tailwind CSS 4      | Tema visual y diseño responsive            |
| Vite 7              | Desarrollo y compilación                   |
| Prettier            | Formato consistente                        |
| Node.js Test Runner | Pruebas automatizadas                      |



## Comandos

| Comando                     | Descripción                        |
| --------------------------- | ---------------------------------- |
| `npm run dev` o `npm start` | Inicia el servidor de desarrollo   |
| `npm test`                  | Ejecuta las pruebas                |
| `npm run format`            | Aplica Prettier                    |
| `npm run format:check`      | Comprueba el formato               |
| `npm run build`             | Compila en `dist/`                 |
| `npm run preview`           | Sirve la compilación               |
| `npm run mock:init`         | Regenera el archivo público de MSW |

`public/mockServiceWorker.js` ya está incluido. Solo hace falta regenerarlo si se actualiza MSW o se elimina ese archivo.

## API REST mock

El prefijo de la API es `api/` dentro de la base de la aplicación. En local es `/api/`; bajo una subcarpeta, por ejemplo, es `/mi-repositorio/api/`.

| Método | Endpoint                      | Respuesta                                                                              |
| ------ | ----------------------------- | -------------------------------------------------------------------------------------- |
| GET    | `/api/categories`             | Categorías con identificador, nombre, descripción, símbolo, color y cantidad de libros |
| GET    | `/api/books?category=ficcion` | Libros de la categoría                                                                 |
| GET    | `/api/books`                  | Catálogo completo                                                                      |
| GET    | `/api/books/:id`              | Detalle del libro                                                                      |

Respuesta exitosa:

```json
{
  "data": [
    {
      "id": "ficcion",
      "name": "Ficción",
      "description": "Vidas, encuentros y otras formas de mirar el mundo.",
      "symbol": "✳",
      "color": "terracotta",
      "bookCount": 3
    }
  ]
}
```

Una categoría o libro inexistente devuelve HTTP 404. Una categoría existente sin libros devuelve HTTP 200 con `data: []`.

Los datos de demostración están en `src/mocks/catalog.js` y los endpoints en `src/mocks/handlers.js`. Las páginas no importan esos datos: utilizan el cliente HTTP.

## Recorrido de una consulta

```text
Página → useCatalog → TanStack Query → cliente fetch
                                          ↓
                                  Handler REST de MSW
                                          ↓
                       Validación de la respuesta JSON
                                          ↓
                          Caché y copia local disponible
                                          ↓
                              Estado de carga o resultado
```

El arranque espera la activación del service worker antes de renderizar la aplicación que consulta el catálogo. Si no puede iniciarse, muestra un mensaje con la opción de volver a cargar.

## Carga, errores y recuperación

`useCatalog` concentra el estado de las peticiones. `QueryFeedback` presenta sus avisos y acciones.

- **Carga inicial:** indicador y tarjetas de espera.
- **Actualización:** indicador discreto que mantiene los datos ya visibles.
- **HTTP 404:** explica que el contenido no está disponible y ofrece explorar otras categorías.
- **HTTP 500 y otros errores del servidor:** explica la indisponibilidad y permite reintentar.
- **Error de red:** recomienda revisar la conexión.
- **Tiempo de espera:** cancela la petición a los 8 segundos y permite repetirla.
- **JSON o esquema inválido:** muestra un error de datos; no lo confunde con una colección vacía.
- **Sin resultados:** muestra un estado vacío con una acción para continuar.
- **Datos guardados:** si una petición falla sin datos visibles, permite abrir la última respuesta correcta guardada para esa URL.
- **Almacenamiento bloqueado:** informa que no se guardó una copia; la respuesta actual sigue disponible.

La copia guardada se muestra únicamente por elección del usuario y con fecha y aviso de posible desactualización. No sustituye silenciosamente una respuesta del servicio.

## Caché y eficiencia

- Los resultados permanecen frescos durante 5 minutos.
- Las consultas inactivas se conservan en memoria durante 30 minutos.
- Las consultas simultáneas con la misma clave comparten una petición.
- Volver inmediatamente a una categoría reutiliza su respuesta.
- No se vuelve a consultar automáticamente al enfocar la ventana o recuperar conexión.
- No hay reintentos automáticos: el usuario decide cuándo repetir una petición fallida.
- Las categorías y cada listado tienen claves diferentes.
- La búsqueda y el orden se aplican sobre los datos recibidos, sin generar una petición por cada pulsación.
- El catálogo de libros se solicita al abrir su categoría.
- La señal de cancelación se propaga desde la consulta hasta `fetch`, con limpieza del temporizador.

La copia local utiliza la clave `entrelineas-catalogo-v1`, con versión, entradas por URL completa y fecha de guardado. Se valida tanto al guardar como al leer. Es una alternativa de recuperación; no precarga la caché como si fuera una respuesta actual.

El acceso al almacenamiento mantiene el mismo contrato `getItem/setItem` en lectura, escritura y pruebas.

## Rutas

| Ruta                      | Página                  |
| ------------------------- | ----------------------- |
| `/`                       | Inicio                  |
| `/categorias`             | Todas las categorías    |
| `/categorias/:categoryId` | Libros de una categoría |
| `/libros/:bookId`         | Detalle del libro       |
| Cualquier otra ruta       | Página no encontrada    |

Se utiliza `HashRouter` para permitir enlaces directos y recargas en GitHub Pages. Ejemplo: `/mi-repositorio/#/categorias/ficcion`.

## Organización

```text
public/
└── mockServiceWorker.js
src/
├── api/
│   ├── ApiProvider.jsx
│   ├── client.js
│   ├── contracts.js
│   └── queries.js
├── components/
│   ├── catalog/
│   ├── layout/
│   ├── FormField.jsx
│   └── QueryFeedback.jsx
├── hooks/
│   └── useCatalog.js
├── lib/
│   ├── format.js
│   └── storage.js
├── mocks/
│   ├── browser.js
│   ├── catalog.js
│   └── handlers.js
├── pages/
├── App.jsx
├── main.jsx
└── styles.css
tests/
├── api.test.js
└── contracts-storage.test.js
.github/workflows/deploy.yml
```

La normalización de búsquedas se comparte desde `lib/format.js`. Las reglas de respuesta se concentran en `api/contracts.js`. `FormField` uniforma las etiquetas y la asociación accesible de errores. El layout contiene únicamente elementos de navegación generales; las herramientas de búsqueda pertenecen a sus páginas.

## Pruebas

```bash
npm test
npm run format:check
npm run build
```

Las pruebas usan los mismos handlers REST con `setupServer` de MSW en Node. Comprueban respuestas correctas, relaciones entre recursos, categorías vacías, errores 404 y 500, red, JSON inválido, timeout, cancelación, recuperación, almacenamiento bloqueado, caché, deduplicación y rutas bajo subcarpetas.

### Escenarios manuales de servicio

Para simular un escenario, agrega `escenario` antes del fragmento de ruta. Por ejemplo:

```text
http://localhost:5173/?escenario=once#/categorias
```

| Valor     | Comportamiento                                                          |
| --------- | ----------------------------------------------------------------------- |
| `once`    | La primera petición falla con 500; al reintentar responde correctamente |
| `500`     | Error del servidor                                                      |
| `404`     | Recurso no encontrado                                                   |
| `network` | Error de conexión                                                       |
| `invalid` | Respuesta JSON con estructura incorrecta                                |
| `slow`    | Retraso de 3,5 segundos para observar la carga                          |
| `empty`   | Categorías o listados vacíos                                            |

Para probar la alternativa guardada, carga primero el catálogo normalmente. Después abre `?escenario=500#/categorias` y selecciona **Usar última consulta guardada**. Sin una consulta exitosa previa, esa opción no aparece.

Quita el parámetro para volver al servicio normal. Los escenarios solo se envían en modo mock.

## Configuración

`.env.example` documenta las variables disponibles.

| Variable         | Valor por defecto                      | Uso                                               |
| ---------------- | -------------------------------------- | ------------------------------------------------- |
| `VITE_BASE_PATH` | `/`                                    | Ruta pública de la aplicación                     |
| `VITE_API_MODE`  | `mock`                                 | `mock` para MSW o `real` para un servidor externo |
| `VITE_API_URL`   | API dentro de la base de la aplicación | URL de un backend REST externo                    |

El mock está activado tanto en desarrollo como en la compilación de demostración para que GitHub Pages pueda mostrar el catálogo sin un backend independiente. Para un backend real, configura `VITE_API_MODE=real` y una URL que respete el contrato de respuesta.

Las variables se aplican al iniciar Vite o compilar, no después de publicar.

## GitHub Pages

1. Sube el contenido del proyecto a la raíz del repositorio, incluyendo `public/`, `.github/`, `.prettierrc.json` y `.prettierignore`.
2. En **Settings > Pages > Source**, selecciona **GitHub Actions**.
3. Al subir cambios a `main`, el flujo instala, comprueba formato, ejecuta pruebas, compila y publica.
4. Espera a que **Publicar en GitHub Pages** termine en verde.

El flujo obtiene la base desde Pages y la pasa a Vite. El service worker y los endpoints se construyen con esa misma base, sin fijar el nombre del repositorio.

Para una compilación manual bajo una subcarpeta:

```bash
npm run build -- --base=/mi-repositorio/
npm run preview -- --base=/mi-repositorio/
```

