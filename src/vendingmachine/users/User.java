package vendingmachine.users;
import vendingmachine.payment.Card;

public class User {
    private String name;
    private Card card;

    public User(String name, Card card) {
        this.name = name;
        this.card = card;
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
