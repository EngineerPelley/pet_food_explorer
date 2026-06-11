import type { ProductDetail, ProductSummary } from './types';

// Base URL is configurable via a Vite env variable; falls back to the local
// backend default.
const BASE_URL = import.meta.env.VITE_API_BASE_URL ?? 'http://localhost:8080/api';

async function getJson<T>(path: string): Promise<T> {
  const response = await fetch(`${BASE_URL}${path}`);
  if (!response.ok) {
    throw new Error(`Request failed (${response.status}) for ${path}`);
  }
  return response.json() as Promise<T>;
}

// Lists products, optionally filtered by ingredients to include (`wanted`) and
// exclude (`unwanted`). Each term becomes a repeated query param, e.g.
// /products?wanted=chicken&wanted=rice&unwanted=beef
export function fetchProducts(
  wanted: string[] = [],
  unwanted: string[] = [],
): Promise<ProductSummary[]> {
  const params = new URLSearchParams();
  wanted.forEach((w) => params.append('wanted', w));
  unwanted.forEach((u) => params.append('unwanted', u));
  const query = params.toString();
  return getJson<ProductSummary[]>(`/products${query ? `?${query}` : ''}`);
}

export function fetchProduct(id: number | string): Promise<ProductDetail> {
  return getJson<ProductDetail>(`/products/${id}`);
}
