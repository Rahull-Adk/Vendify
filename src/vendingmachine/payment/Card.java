package vendingmachine.payment;

public class Card implements PaymentMethod {
    private String holderName;
    private String cardId;
    private double balance;

    public Card(String holderName, String cardId, double balance) {
        this.holderName = holderName;
        this.cardId = cardId;
        this.balance = balance;
    }

    @Override
    public boolean pay(double amount) {
        if (balance >= amount) {
            balance -= amount;
            return true;
        }
        return false;
    }


    public void withdraw(double amount) {
        if (amount > 0 && balance >= amount) {
            balance -= amount;
        }
    }

    public double getBalance() {
        return balance;
    }

    public void deposit(double amount) {
        if (amount > 0) {
            balance += amount;
        }
    }

    public String getHolderName() {
        return holderName;
    }

    public String getCardId() {
        return cardId;
    }

    public Card getCard() {
        return this;
    }
}
