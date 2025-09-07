package vendingmachine.utils;

import java.io.*;
import java.util.*;
import vendingmachine.core.*;

public class ProductFileManager {

    public static void saveProducts(List<Product> products, String filename) {
        try (PrintWriter pw = new PrintWriter(new FileWriter(filename))) {
            for (Product p : products) {
                pw.println(p.getClass().getSimpleName() + "," + p.getId() + "," + p.getName() + "," + 
                           p.getPrice() + "," + p.getQuantity() + "," + p.getExpiryDate());
            }
        } catch (IOException e) {
            System.out.println("Error saving products: " + e.getMessage());
        }
    }

    public static List<Product> loadProducts(String filename) {
        List<Product> products = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader(filename))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts.length == 6) {
                    String type = parts[0];
                    String id = parts[1];
                    String name = parts[2];
                    double price = Double.parseDouble(parts[3]);
                    int qty = Integer.parseInt(parts[4]);
                    String expiry = parts[5];

                    if ("Food".equals(type)) {
                        products.add(new Food(id, name, price, qty, expiry));
                    } else if ("Drink".equals(type)) {
                        products.add(new Drink(id, name, price, qty, expiry));
                    }
                }
            }
        } catch (IOException e) {
            System.out.println("Error loading products: " + e.getMessage());
        }
        return products;
    }

    public static Product findProductById(String id, List<Product> products) {
        return products.stream()
                .filter(p -> p.getId().equalsIgnoreCase(id))
                .findFirst()
                .orElse(null);
    }
}
