package vendingmachine.core;

import javax.swing.*;
import java.awt.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Map;

public class Receipt {
    private Transaction transaction;

    public Receipt(Transaction t) {
        this.transaction = t;
    }

    public void showGUI(Component parent) {
        LocalDateTime ts = LocalDateTime.parse(transaction.getTimestampString(), DateTimeFormatter.ISO_LOCAL_DATE_TIME);
        String formatted = ts.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));

        StringBuilder sb = new StringBuilder();
        sb.append("===== RECEIPT =====\n");
        sb.append("Timestamp: ").append(formatted).append("\n");
        sb.append("-------------------\n");

        for (Map.Entry<Product, Integer> entry : transaction.getProducts().entrySet()) {
            Product product = entry.getKey();
            int quantity = entry.getValue();
            double lineTotal = product.getPrice() * quantity;
            sb.append(String.format("%s x%d - $%.2f\n", product.getName(), quantity, lineTotal));
        }

        sb.append("-------------------\n");
        sb.append(String.format("TOTAL: $%.2f\n", transaction.getTotalAmount()));
        sb.append("Payment Method: ").append(
                transaction.getPaymentMethod() != null ? transaction.getPaymentMethod().getClass().getSimpleName() : "Unknown"
        ).append("\n");
        sb.append("===================\n");

        JTextArea textArea = new JTextArea(sb.toString());
        textArea.setEditable(false);
        textArea.setFont(new Font("Monospaced", Font.PLAIN, 14));
        JScrollPane scrollPane = new JScrollPane(textArea);
        scrollPane.setPreferredSize(new Dimension(400, 300));

        JOptionPane.showMessageDialog(parent, scrollPane, "Receipt", JOptionPane.INFORMATION_MESSAGE);
    }
}
