# TypeScript

**What it is:** JavaScript with a type system added on top. Browsers only run
JavaScript, so TypeScript is checked and converted ("compiled") to JavaScript
by the build. Coming from Java, the types will feel familiar — but the
underlying JavaScript behaves differently from Java in important ways.

**David's level: new**

## What David knows

(nothing recorded yet)

## Next things to learn

- JavaScript basics first (TypeScript is JavaScript plus types):
  `const`/`let`, functions and arrow functions (`(x) => x + 1`), objects and
  arrays as literals, **async/await** and Promises (how `fetch` works).
- Type annotations: `function f(id: number): Promise<ProductDetail>` —
  compare with Java signatures.
- **Interfaces / type aliases** as data shapes — see
  `frontend/src/api/types.ts`, which mirrors the backend's Java records.
- **Generics** like `getJson<T>` in `client.ts` — same idea as Java generics.
- Key difference from Java: TypeScript types are erased at runtime — the
  browser never checks them; they only catch mistakes at build time.
- `null` vs `undefined`, and optional fields (`source: string | null`).

## Related

- Knowledge: [[java|Java]] (the familiar reference point — similar types, different runtime) · [[react|React]] (what it's used to build here) · [[node-npm-vite|Node, npm & Vite]] (what compiles and serves it)
- Architecture: [[frontend]] (`api/types.ts` mirrors the backend's records)

## Notes

(none yet)
