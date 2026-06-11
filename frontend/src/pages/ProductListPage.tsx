import { useEffect, useState } from 'react';
import { Link } from 'react-router-dom';
import { fetchProducts } from '../api/client';
import type { ProductSummary } from '../api/types';

// Split a raw search box value ("chicken, rice") into clean terms.
function parseTerms(raw: string): string[] {
  return raw
    .split(',')
    .map((t) => t.trim())
    .filter((t) => t.length > 0);
}

export default function ProductListPage() {
  const [wantedText, setWantedText] = useState('');
  const [unwantedText, setUnwantedText] = useState('');

  const [products, setProducts] = useState<ProductSummary[]>([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState<string | null>(null);

  // Re-query whenever the filters change. A short debounce keeps us from firing
  // a request on every keystroke.
  useEffect(() => {
    const handle = setTimeout(() => {
      const wanted = parseTerms(wantedText);
      const unwanted = parseTerms(unwantedText);
      setError(null);
      fetchProducts(wanted, unwanted)
        .then(setProducts)
        .catch((err: unknown) => setError(err instanceof Error ? err.message : 'Failed to load'))
        .finally(() => setLoading(false));
    }, 300);

    return () => clearTimeout(handle);
  }, [wantedText, unwantedText]);

  return (
    <>
      {/* Ingredient filters: two search boxes centered near the top. */}
      <div className="row justify-content-center mb-4">
        <div className="col-12 col-lg-9">
          <div className="row g-3">
            <div className="col-12 col-md-6">
              <label htmlFor="wanted" className="form-label fw-semibold text-center w-100">
                Wanted Ingredients
              </label>
              <input
                id="wanted"
                type="search"
                className="form-control"
                placeholder="e.g. chicken, rice"
                value={wantedText}
                onChange={(e) => setWantedText(e.target.value)}
              />
            </div>
            <div className="col-12 col-md-6">
              <label htmlFor="unwanted" className="form-label fw-semibold text-center w-100">
                Unwanted Ingredients
              </label>
              <input
                id="unwanted"
                type="search"
                className="form-control"
                placeholder="e.g. beef, peas"
                value={unwantedText}
                onChange={(e) => setUnwantedText(e.target.value)}
              />
            </div>
          </div>
          <p className="text-muted small text-center mt-2 mb-0">
            Separate multiple ingredients with commas. Foods must contain every wanted ingredient
            and none of the unwanted ones.
          </p>
        </div>
      </div>

      <h1 className="h3 mb-4">Products</h1>

      {error ? (
        <div className="alert alert-danger">Could not load products: {error}</div>
      ) : loading ? (
        <p className="text-muted">Loading products…</p>
      ) : products.length === 0 ? (
        <p className="text-muted">No products match your ingredient filters.</p>
      ) : (
        <div className="row g-3">
          {products.map((p) => (
            <div className="col-12 col-md-6 col-lg-4" key={p.foodId}>
              <div className="card h-100 shadow-sm">
                <div className="card-body d-flex flex-column">
                  <h2 className="h5 card-title">{p.name}</h2>
                  <h3 className="h6 card-subtitle mb-2 text-muted">{p.brandName}</h3>
                  <p className="card-text mb-3">
                    {p.productTypeName && (
                      <span className="badge text-bg-secondary me-2">{p.productTypeName}</span>
                    )}
                    {p.caloriesPerCup != null && (
                      <span className="text-muted small">{p.caloriesPerCup} kcal/cup</span>
                    )}
                  </p>
                  <Link to={`/products/${p.foodId}`} className="btn btn-outline-primary mt-auto">
                    View details
                  </Link>
                </div>
              </div>
            </div>
          ))}
        </div>
      )}
    </>
  );
}
