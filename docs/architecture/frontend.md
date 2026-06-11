# Frontend architecture

React 18 + TypeScript, built and served by Vite, styled with Bootstrap 5.
Source root: `frontend/src/`.

## Structure

```
src/
├── main.tsx        entry point: mounts the React app into index.html and
│                   imports Bootstrap's CSS globally (once, for the whole app)
├── App.tsx         React Router setup: which URL shows which page
├── api/
│   ├── types.ts    TypeScript shapes that mirror the backend's Java records
│   │               (ProductSummary, ProductDetail, ...). Kept in sync by hand.
│   └── client.ts   the only place HTTP happens: a small typed fetch wrapper
│                   with one function per endpoint (fetchProducts, fetchProduct)
└── pages/
    ├── ProductListPage.tsx     list + ingredient filter UI  →  route "/"
    └── ProductDetailPage.tsx   one product                  →  route "/products/:id"
```

## Conventions

- **All backend communication goes through `api/client.ts`.** Pages never call
  `fetch` directly. The base URL comes from `VITE_API_BASE_URL` in
  `frontend/.env` (default `http://localhost:8080/api`).
- **`api/types.ts` mirrors the backend `view`/`dto` records.** When the
  backend's response shape changes, this file must change with it — nothing
  checks this automatically.
- The ingredient filter is sent as repeated query parameters:
  `/products?wanted=chicken&wanted=rice&unwanted=beef`.
- **Bootstrap via plain class names only** — the `react-bootstrap` wrapper
  library is deliberately not used; keep dependencies minimal. All versions in
  `package.json` are pinned exactly.
- It's a **single-page app**: the browser loads one HTML page and
  `react-router-dom` swaps page components as the URL changes — navigation
  never reloads the page.

## Build & tooling

- `npm run dev` — Vite dev server on port 5173 with hot reload.
- `npm run build` — TypeScript type-check (`tsc -b`) then production bundle.
- `npm run lint` / `npm run format` — ESLint / Prettier.

## Related

- Architecture: [[overview]] · [[backend]]
- Features served: [[product-list]] · [[product-detail]] · [[ingredient-filtering]]
- Knowledge: [[react|React]] · [[typescript|TypeScript]] · [[node-npm-vite|Node, npm & Vite]] · [[html-css-bootstrap|HTML, CSS & Bootstrap]] · [[http-and-rest|HTTP & REST]]
