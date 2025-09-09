package vendingmachine.core;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import vendingmachine.payment.Card;
import vendingmachine.payment.Cash;
import vendingmachine.payment.PaymentMethod;
import vendingmachine.users.User;
import vendingmachine.utils.*;

public class VendingMachine {
	private List<Product> products;
	private List<Transaction> transactions;

	private final String PRODUCT_FILE = "products.csv";

	public VendingMachine() {
		products = ProductFileManager.loadProducts(PRODUCT_FILE);
		transactions = new ArrayList<>();
	}

	public void addProduct(Product product) {
		products.add(product);
		ProductFileManager.saveProducts(products, PRODUCT_FILE);
	}

	public List<Product> getProducts() {
		return products;
	}

	public List<Transaction> getTransactions() {
		return transactions;
	}

	public void displayItems() {
		for (Product product : products) {
			System.out.println(product.getInfo() + " | Stock: " + product.getQuantity());
		}
	}

	public Transaction processTransaction(User user, Map<Product, Integer> cart, PaymentMethod payment) {
		double total = cart.entrySet().stream().mapToDouble(e -> e.getKey().getPrice() * e.getValue()).sum();

		boolean paymentSuccess = false;
		if (payment instanceof Card cardPayment) {
			paymentSuccess = cardPayment.pay(total);
		} else if (payment instanceof Cash cashPayment) {
			paymentSuccess = cashPayment.pay(total);
		}

		if (!paymentSuccess) {
			return null;
		}
		
		for (Map.Entry<Product, Integer> entry : cart.entrySet()) {
			Product p = entry.getKey();
			int qty = entry.getValue();
			p.setQuantity(p.getQuantity() - qty);
		}

		Transaction transaction = new Transaction(cart, total, payment);
		transactions.add(transaction);

		return transaction;
	}
}
