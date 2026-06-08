import { Link, Route, Routes } from 'react-router-dom';
import ProductListPage from './pages/ProductListPage';
import ProductDetailPage from './pages/ProductDetailPage';

export default function App() {
  return (
    <>
      <nav className="navbar navbar-expand navbar-dark bg-dark">
        <div className="container">
          <Link className="navbar-brand" to="/">
            🐾 Pet Food Explorer
          </Link>
        </div>
      </nav>

      <main className="container py-4">
        <Routes>
          <Route path="/" element={<ProductListPage />} />
          <Route path="/products/:id" element={<ProductDetailPage />} />
        </Routes>
      </main>
    </>
  );
}
