import { useEffect, useState } from 'react'

// Match the fields this table reads from the backend's ProductResponse.
type Product = {
  id: number
  sku: string
  name: string
  categoryName: string
  unit: string
  sellingPrice: number
  reorderLevel: number
}

export default function ProductsTable() {
  const [products, setProducts] = useState<Product[]>([])
  const [isLoading, setIsLoading] = useState(true)
  const [error, setError] = useState('')

  useEffect(() => {
    const controller = new AbortController()

    async function loadProducts() {
      try {
        // Vite forwards this request to Spring Boot during development.
        const response = await fetch('/api/products', {
          signal: controller.signal,
        })

        if (!response.ok) {
          throw new Error(`Could not load products (HTTP ${response.status}).`)
        }

        const data: Product[] = await response.json()
        setProducts(data)
      } catch (error) {
        if (!controller.signal.aborted) {
          setError(
            error instanceof Error ? error.message : 'Could not load products.',
          )
        }
      } finally {
        if (!controller.signal.aborted) {
          setIsLoading(false)
        }
      }
    }

    void loadProducts()

    // Cancel the request if React removes this component from the screen.
    return () => controller.abort()
  }, [])

  return (
    <section aria-labelledby="products-heading">
      <h2 id="products-heading">Products</h2>

      {isLoading && <p role="status">Loading products…</p>}
      {error && <p role="alert">{error}</p>}
      {!isLoading && !error && products.length === 0 && <p>No products yet.</p>}

      {!isLoading && !error && products.length > 0 && (
        <table aria-labelledby="products-heading">
          <thead>
            <tr>
              <th scope="col">SKU</th>
              <th scope="col">Name</th>
              <th scope="col">Category</th>
              <th scope="col">Unit</th>
              <th scope="col">Selling price</th>
              <th scope="col">Reorder level</th>
            </tr>
          </thead>
          <tbody>
            {/* Render one table row per product, using its database ID as the key. */}
            {products.map((product) => (
              <tr key={product.id}>
                <td>{product.sku}</td>
                <td>{product.name}</td>
                <td>{product.categoryName}</td>
                <td>{product.unit}</td>
                <td>{product.sellingPrice.toFixed(2)}</td>
                <td>{product.reorderLevel}</td>
              </tr>
            ))}
          </tbody>
        </table>
      )}
    </section>
  )
}