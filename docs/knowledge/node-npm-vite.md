# Node, npm & Vite

**What it is:** The frontend toolchain. **Node.js** runs JavaScript outside a
browser (used here only for build tools, not for our server). **npm** is its
package manager — like Gradle's dependency handling, but for JavaScript.
**Vite** is the development server and bundler that serves the frontend while
you work and packages it for production.

**David's level: new**

## What David knows

(nothing recorded yet)

## Next things to learn

- `npm install` — reads `package.json`, downloads dependencies into
  `node_modules/` (which is huge and never committed to git).
- `package.json` — dependencies and **scripts** (`npm run dev`, `npm run
  build`, `npm run lint`); compare with `build.gradle.kts` on the backend.
- `package-lock.json` — records exact versions so installs are reproducible.
- **Vite dev server** (`npm run dev`) — serves the app on port 5173 with hot
  reload (saving a file updates the browser instantly, no restart).
- Environment variables via `.env` (`VITE_API_BASE_URL`) — compare with the
  backend's `${DB_HOST:localhost}` pattern.
- What `npm run build` produces and why a production site is just static
  files.

## Related

- Knowledge: [[typescript|TypeScript]] · [[react|React]] (what this toolchain builds) · [[gradle|Gradle]] (the backend's counterpart — compare `package.json` with `build.gradle.kts`) · [[git|Git]] (`node_modules/` is gitignored)
- Architecture: [[frontend]]

## Notes

(none yet)
