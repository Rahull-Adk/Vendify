package vendingmachine.users;

import java.util.Map;
import vendingmachine.core.Product;
import vendingmachine.core.Transaction;
import vendingmachine.payment.Card;

public class User {
    private String name;
    private Card card;

    public User(String name, Card card) {
        this.name = name;
        this.card = card;
    }

    public Transaction purchase(Map<Product, Integer> products) {
        double totalCost = products.entrySet().stream()
                                   .mapToDouble(e -> e.getKey().getPrice() * e.getValue())
                                   .sum();
  
        if (!card.pay(totalCost)) {
            System.out.println("Payment failed. Insufficient balance.");
            return null;
        }
        for (Map.Entry<Product, Integer> entry : products.entrySet()) {
            Product product = entry.getKey();
            int quantity = entry.getValue();
            product.setQuantity(product.getQuantity() - quantity);
        }
        Transaction transaction = new Transaction(products, totalCost, card);

        return transaction;
    }


    public void addBalance(double amount) {
        card.deposit(amount);
    }

    public double getBalance() {
        return card.getBalance();
    }

    public Card getCard() {
        return card.getCard();
    }

    public String getName() {
        return name;
    }
}
