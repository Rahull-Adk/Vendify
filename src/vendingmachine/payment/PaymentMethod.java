package vendingmachine.payment;

public interface PaymentMethod {
	public boolean pay(double amount);
}