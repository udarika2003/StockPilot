import { useState } from 'react'

function App() {
  const [message, setMessage] = useState('Connection not checked')
  const [isLoading, setIsLoading] = useState(false)

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
    </main>
  )
}

export default App