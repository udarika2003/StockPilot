import { useState } from 'react'
import ProductsTable from './ProductsTable'
import AddProductForm from './AddProductForm'

function App() {
  const [message, setMessage] = useState('Connection not checked')
  const [isLoading, setIsLoading] = useState(false)
  // Change the table key after a successful save so it fetches products again.
  const [productsTableVersion, setProductsTableVersion] = useState(0)

  async function checkBackend() {
    setIsLoading(true)
    setMessage('Connecting...')

    try {
      const response = await fetch('/api/health')

      if (!response.ok) {
        throw new Error('Backend returned an error')
      }

      const text = await response.text()
      setMessage(text)
    } catch {
      setMessage('Connection failed. Check that the backend is running.')
    } finally {
      setIsLoading(false)
    }
  }

  return (
    
    <main>
      <h1>StockPilot</h1>
      <p>Grocery Inventory Management System</p>

      <button onClick={checkBackend} disabled={isLoading}>
        Check backend connection
      </button>

      <p role="status">{message}</p>

      <AddProductForm
        onProductAdded={() => setProductsTableVersion((version) => version + 1)}
      />
      <ProductsTable key={productsTableVersion} />



    </main>
  )
}

export default App