import { useEffect, useState } from 'react';
import { Link } from 'react-router-dom';
import { fetchProducts } from '../api/client';
import type { ProductSummary } from '../api/types';

export default function ProductListPage() {
  const [products, setProducts] = useState<ProductSummary[]>([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState<string | null>(null);

  useEffect(() => {
    fetchProducts()
      .then(setProducts)
      .catch((err: unknown) => setError(err instanceof Error ? err.message : 'Failed to load'))
      .finally(() => setLoading(false));
  }, []);

  if (loading) {
    return <p className="text-muted">Loading products…</p>;
  }

  if (error) {
    return <div className="alert alert-danger">Could not load products: {error}</div>;
  }

  return (
    <>
      <h1 className="h3 mb-4">Products</h1>
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
    </>
  );
}
