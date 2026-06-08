import { useEffect, useState } from 'react';
import { Link, useParams } from 'react-router-dom';
import { fetchProduct } from '../api/client';
import type { ProductDetail } from '../api/types';

export default function ProductDetailPage() {
  const { id } = useParams<{ id: string }>();
  const [product, setProduct] = useState<ProductDetail | null>(null);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState<string | null>(null);

  useEffect(() => {
    if (!id) return;
    setLoading(true);
    fetchProduct(id)
      .then(setProduct)
      .catch((err: unknown) => setError(err instanceof Error ? err.message : 'Failed to load'))
      .finally(() => setLoading(false));
  }, [id]);

  if (loading) {
    return <p className="text-muted">Loading product…</p>;
  }

  if (error || !product) {
    return (
      <>
        <div className="alert alert-danger">Could not load product: {error ?? 'not found'}</div>
        <Link to="/" className="btn btn-link px-0">
          ← Back to products
        </Link>
      </>
    );
  }

  return (
    <>
      <Link to="/" className="btn btn-link px-0 mb-3">
        ← Back to products
      </Link>

      <h1 className="h3">{product.name}</h1>
      <p className="text-muted">{product.brand.name}</p>

      <div className="mb-3">
        {product.productType && (
          <span className="badge text-bg-secondary me-2">{product.productType.name}</span>
        )}
        {product.petTypes.map((pt) => (
          <span className="badge text-bg-info me-2" key={pt.petTypeId}>
            {pt.name}
          </span>
        ))}
      </div>

      {product.description && <p>{product.description}</p>}

      {product.caloriesPerCup != null && (
        <p className="text-muted">
          <strong>Calories:</strong> {product.caloriesPerCup} kcal/cup
        </p>
      )}

      <h2 className="h5 mt-4">Ingredients</h2>
      {product.ingredients.length === 0 ? (
        <p className="text-muted">No ingredients listed.</p>
      ) : (
        <table className="table table-sm table-striped">
          <thead>
            <tr>
              <th scope="col">#</th>
              <th scope="col">Ingredient</th>
              <th scope="col">Source</th>
              <th scope="col" className="text-end">
                %
              </th>
            </tr>
          </thead>
          <tbody>
            {product.ingredients.map((ing) => (
              <tr key={ing.ingredientId}>
                <td>{ing.labelPosition ?? '—'}</td>
                <td>{ing.name}</td>
                <td>{ing.source ?? '—'}</td>
                <td className="text-end">{ing.percentage != null ? `${ing.percentage}%` : '—'}</td>
              </tr>
            ))}
          </tbody>
        </table>
      )}
    </>
  );
}
