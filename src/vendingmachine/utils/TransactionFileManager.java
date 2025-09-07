package vendingmachine.utils;

import java.io.*;
import java.util.*;
import vendingmachine.core.*;
import vendingmachine.payment.*;

public class TransactionFileManager {

    public static void saveTransactions(List<Transaction> transactions, String filename) {
        try (PrintWriter pw = new PrintWriter(new FileWriter(filename))) {
            for (Transaction t : transactions) {
                StringBuilder productData = new StringBuilder();

                for (Map.Entry<Product, Integer> entry : t.getProducts().entrySet()) {
                    productData.append(entry.getKey().getId())
                               .append(":").append(entry.getValue()).append(";");
                }

                if (productData.length() > 0) {
                    productData.setLength(productData.length() - 1); // remove trailing ;
                }

                String paymentMethod = (t.getPaymentMethod() != null)
                        ? t.getPaymentMethod().getClass().getSimpleName()
                        : "Unknown";

                pw.println(productData + "," + t.getTotalAmount() + "," +
                           t.getTimestampString() + "," + paymentMethod);
            }
        } catch (IOException e) {
            System.out.println("Error saving transactions: " + e.getMessage());
        }
    }

    public static List<Transaction> loadTransactions(String filename, List<Product> products) {
        List<Transaction> transactions = new ArrayList<>();
        try (Scanner sc = new Scanner(new File(filename))) {
            while (sc.hasNextLine()) {
                String line = sc.nextLine();
                if (line.trim().isEmpty()) continue;

                String[] parts = line.split(",", 4);
                if (parts.length < 3) continue;

                String productData = parts[0];
                double total = Double.parseDouble(parts[1]);
                String timestampStr = parts[2];
                String paymentType = (parts.length > 3) ? parts[3] : "Unknown";

                Map<Product, Integer> cart = new HashMap<>();
                for (String item : productData.split(";")) {
                    String[] p = item.split(":");
                    Product prod = ProductFileManager.findProductById(p[0], products);
                    if (prod != null) {
                        cart.put(prod, Integer.parseInt(p[1]));
                    }
                }

                PaymentMethod payment = "Card".equalsIgnoreCase(paymentType)
                        ? new Card("Unknown", "Unknown", total)
                        : new Cash(total);

                Transaction t = new Transaction(cart, total, payment);
                t.setTimestamp(timestampStr);
                transactions.add(t);
            }
        } catch (FileNotFoundException e) {
            System.out.println("Transactions file not found, starting fresh.");
        }
        return transactions;
    }
}
