package vendingmachine.core;

public class Drink extends Product {
	public Drink(String id, String name, double price, int quantity, String expiryDate) {
        super(id, name, price, quantity, expiryDate);
    }

    @Override
    public String getInfo() {
        return "[Drink] " + getName() + " - $" + getPrice() + " (" + getQuantity() + " left)";
    }
}
