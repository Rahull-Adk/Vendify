package vendingmachine.core;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Map;
import vendingmachine.payment.PaymentMethod;

public class Transaction {
    private Map<Product, Integer> products; 
    private double totalAmount;
    private PaymentMethod paymentMethod;
    private LocalDateTime timestamp;
    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ISO_LOCAL_DATE_TIME;
    public Transaction(Map<Product, Integer> products, double totalAmount, PaymentMethod paymentMethod) {
        this.products = products;
        this.totalAmount = totalAmount;
        this.paymentMethod = paymentMethod;
        this.timestamp = LocalDateTime.now();
    }

    public Receipt generateReceipt() {
        return new Receipt(this);
    }

    public Map<Product, Integer> getProducts() {
        return products;
    }

    public double getTotalAmount() {
        return totalAmount;
    }

    public PaymentMethod getPaymentMethod() {
        return paymentMethod;
    }

    public String getTimestampString() {
        return this.timestamp.format(FORMATTER);
    }

    public void setTimestamp(String timestampStr) {
        this.timestamp = LocalDateTime.parse(timestampStr, FORMATTER);
    }
    
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("🕒 ").append(timestamp.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"))).append("\n");
        sb.append("---------------------------\n");

        for (Map.Entry<Product, Integer> entry : products.entrySet()) {
            Product product = entry.getKey();
            int qty = entry.getValue();
            double lineTotal = product.getPrice() * qty;
            sb.append(String.format("%-15s x%-3d $%.2f\n", product.getName(), qty, lineTotal));
        }

        sb.append("---------------------------\n");
        sb.append(String.format("TOTAL: $%.2f\n", totalAmount));
        sb.append("Payment: ").append(paymentMethod.getClass().getSimpleName()).append("\n");
        sb.append("===========================\n");

        return sb.toString();
    }

}
