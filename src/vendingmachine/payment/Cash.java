package vendingmachine.payment;

public class Cash implements PaymentMethod {
    private double insertedAmount;

    public Cash(double insertedAmount) {
        this.insertedAmount = insertedAmount;
    }

    public double getInsertedAmount() {
        return insertedAmount;
    }

    @Override
    public boolean pay(double amount) {
        if (insertedAmount >= amount) {
            return true;
        }
        return false;
    }
}
