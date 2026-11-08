# StockPilot

A grocery inventory management application.

## Technology Stack

- React with TypeScript
- Java with Spring Boot
- PostgreSQL

## Project Structure

- frontend: React user interface
- backend: Spring Boot API
- docs: Project documentation

## Project Overview

StockPilot helps a small grocery shop manage products, purchases,
sales and stock records in one application.

This is an individual portfolio project developed using sample data.
The initial version supports one shop and one administrator.

## Requirements and Planned Features

### Administrator Access
- Sign in using an email and password.
- Access protected features after authentication.
- Log out of the application.

### Product Management
- Add, edit, list and search products.
- Record a unique SKU, name, category, unit, selling price and reorder level.
- Prevent duplicate product codes.

### Suppliers and Purchases
- Provide sample suppliers for purchase recording.
- Record multiple products, quantities and unit costs in a purchase.
- Calculate purchase totals on the backend.
- Increase stock when goods are received.

### Sales
- Record sales containing multiple products.
- Calculate sale totals on the backend.
- Reduce stock after a successful sale.
- Reject a sale when any requested quantity exceeds available stock.

### Inventory
- Display current stock quantities and units.
- Maintain a history of stock increases and decreases.
- Display low-stock alerts when quantity is at or below the reorder level.
- Keep stock balances and transaction records consistent.

### Small AI Feature
- Estimate the next seven days of demand for one sample product.
- Use simple linear regression implemented in Java.
- Compare predictions with an average-based baseline.
- Evaluate prediction errors using mean absolute error (MAE).
- Clearly label simulated data and explain forecast limitations.

## Development Progress

- [x] Frontend and backend setup
- [x] Frontend-to-backend connection
- [x] PostgreSQL connection
- [x] Flyway migration for categories and products
- [ ] Administrator authentication
- [ ] Product management
- [ ] Purchase recording
- [ ] Sales recording
- [ ] Inventory views and low-stock alerts
- [ ] Demand forecasting
- [ ] Complete workflow testing