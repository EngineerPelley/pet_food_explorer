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

export function fetchProducts(): Promise<ProductSummary[]> {
  return getJson<ProductSummary[]>('/products');
}

export function fetchProduct(id: number | string): Promise<ProductDetail> {
  return getJson<ProductDetail>(`/products/${id}`);
}
