import { useEffect, useState } from 'react'
import type { FormEvent } from 'react'

// Match the id and name returned by the category API.
type Category = {
  id: number
  name: string
}

export default function AddProductForm({
  onProductAdded,
}: {
  onProductAdded: () => void //inform to App.jsx about product is succsessfully save  
}) {
  const [categories, setCategories] = useState<Category[]>([])
  const [categoryId, setCategoryId] = useState('')
  const [isLoading, setIsLoading] = useState(true)
  const [error, setError] = useState('')
  const [isSaving, setIsSaving] = useState(false)
  const [saveError, setSaveError] = useState('')
  const [successMessage, setSuccessMessage] = useState('')

  useEffect(() => {
    const controller = new AbortController()

    async function loadCategories() {
      try {
        // Load the available choices from Spring Boot through the Vite proxy.
        const response = await fetch('/api/categories', {
          signal: controller.signal,
        })

        if (!response.ok) {
          throw new Error(`Could not load categories (HTTP ${response.status}).`)
        }

        const data: Category[] = await response.json()
        setCategories(data)
      } catch (error) {
        if (!controller.signal.aborted) {
          setError(
            error instanceof Error ? error.message : 'Could not load categories.',
          )
        }
      } finally {
        if (!controller.signal.aborted) {
          setIsLoading(false)
        }
      }
    }

    void loadCategories()

    // Cancel the request if React removes this component.
    return () => controller.abort()
  }, [])

  async function handleSubmit(event: FormEvent<HTMLFormElement>) {
  // Keep the page open and read the inputs by their name attributes.
  event.preventDefault()
  const form = event.currentTarget
  const formData = new FormData(form)

  // Match the fields and types expected by CreateProductRequest.java.
  const product = {
    sku: String(formData.get('sku')).trim(),
    name: String(formData.get('name')).trim(),
    categoryId: Number(categoryId),
    unit: String(formData.get('unit')),
    sellingPrice: Number(formData.get('sellingPrice')),
    reorderLevel: Number(formData.get('reorderLevel')),
  }

  setIsSaving(true)
  setSaveError('')
  setSuccessMessage('')

  try {
    const response = await fetch('/api/products', {
      method: 'POST',
      headers: { 'Content-Type': 'application/json' },
      body: JSON.stringify(product),
    })

    if (response.status === 409) {
      throw new Error('This SKU already exists. Enter a different SKU.')
    }

    if (response.status === 400) {
      throw new Error(
        'Check the product details. PIECE and PACK require a whole-number reorder level.',
      )
    }

    if (!response.ok) {
      throw new Error(`Could not save the product (HTTP ${response.status}).`)
    }

    // Clear the form and tell App to reload the products table.
    form.reset()
    setCategoryId('')
    setSuccessMessage(`${product.name} was added.`)
    onProductAdded()
  } catch (error) {
    setSaveError(
      error instanceof Error ? error.message : 'Could not save the product.',
    )
  } finally {
    setIsSaving(false)
  }
}

return (
  <section aria-labelledby="add-product-heading">
    <h2 id="add-product-heading">Add Product</h2>

    {isLoading && <p role="status">Loading categories…</p>}
    {error && <p role="alert">{error}</p>}
    {!isLoading && !error && categories.length === 0 && (
      <p>No categories available.</p>
    )}

    <form onSubmit={handleSubmit}>
      {/* Disable the fields while saving or while categories are unavailable. */}
      <fieldset disabled={isSaving || isLoading || categories.length === 0}>
        <legend>Product details</legend>

        <p>
          <label htmlFor="product-sku">SKU</label>{' '}
          <input id="product-sku" name="sku" required maxLength={40} />
        </p>

        <p>
          <label htmlFor="product-name">Product name</label>{' '}
          <input id="product-name" name="name" required maxLength={120} />
        </p>

        <p>
          <label htmlFor="product-category">Category</label>{' '}
          <select
            id="product-category"
            value={categoryId}
            onChange={(event) => setCategoryId(event.target.value)}
            required
          >
            <option value="" disabled>Select a category</option>
            {categories.map((category) => (
              <option key={category.id} value={category.id}>
                {category.name}
              </option>
            ))}
          </select>
        </p>

        <p>
          <label htmlFor="product-unit">Unit</label>{' '}
          <select id="product-unit" name="unit" defaultValue="" required>
            <option value="" disabled>Select a unit</option>
            <option value="PIECE">Piece</option>
            <option value="PACK">Pack</option>
            <option value="KG">Kilogram</option>
            <option value="LITRE">Litre</option>
          </select>
        </p>

        <p>
          <label htmlFor="product-price">Selling price</label>{' '}
          <input
            id="product-price"
            name="sellingPrice"
            type="number"
            min="0.01"
            step="0.01"
            required
          />
        </p>

        <p>
          <label htmlFor="product-reorder">Reorder level</label>{' '}
          <input
            id="product-reorder"
            name="reorderLevel"
            type="number"
            min="0"
            step="0.001"
            required
          />
        </p>

        <button type="submit">
          {isSaving ? 'Saving…' : 'Save Product'}
        </button>
      </fieldset>
    </form>

    {saveError && <p role="alert">{saveError}</p>}
    {successMessage && <p role="status">{successMessage}</p>}
  </section>
)
}