# Vendify (Smart Vending Machine)

A Java-based Smart Vending Machine application that allows users to browse products, make purchases using card or cash, and generate receipts. The system also handles user management, transaction tracking, and product inventory.

## Features

* Browse available products (Food & Drink)
* Add multiple products to cart
* Card and cash payment support
* New card creation for users
* Transaction history tracking
* Automatic receipt generation
* Admin functions for managing users and products

## Installation

1. Clone the repository:

```bash
git clone https://github.com/username/SmartVendingMachine.git
cd SmartVendingMachine
```

2. Open the project in your favorite Java IDE (Eclipse, IntelliJ, etc.)
3. Ensure you have Java 8+ installed.
4. Build and run the project.

## Usage

* Launch the application.
* Select products by entering their IDs and quantities.
* Choose a payment method (Card or Cash).
* Complete the transaction and view the receipt.

## File and Folder Structure

```
Vendify/
 ├── src/
 │    ├── vendingmachine/
 │    │    └── Main.java
 │    ├── vendingmachine/core/
 │    │    ├── Drink.java
 │    │    ├── Food.java
 │    │    ├── Product.java
 │    │    ├── Receipt.java
 │    │    ├── Transaction.java
 │    │    └── VendingMachine.java
 │    ├── vendingmachine/payment/
 │    │    ├── Card.java
 │    │    ├── Cash.java
 │    │    └── PaymentMethod.java
 │    ├── vendingmachine/users/
 │    │    ├── Admin.java
 │    │    └── User.java
 │    ├── vendingmachine/utils/
 │    │    ├── CardFileManager.java
 │    │    ├── ProductFileManager.java
 │    │    ├── TransactionFileManager.java
 │    │    └── UserFileManager.java
 │    └── module-info.java

```

## Notes

* `.env` and CSV data files are ignored for security and privacy.
* Use the admin interface to manage products and user cards.
* Ensure your card data is not shared publicly.

## License

This project is licensed under the MIT License.
