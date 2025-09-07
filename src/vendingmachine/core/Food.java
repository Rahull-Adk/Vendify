package vendingmachine.core;

public class Food extends Product {
    public Food(String id, String name, double price, int quantity, String expiryDate) {
        super(id, name, price, quantity, expiryDate);
    }

    @Override
    public String getInfo() {
        return "[Food] " + getName() + " - $" + getPrice() + " (" + getQuantity() + " left)";
    }
}
