package vendingmachine;

import vendingmachine.core.*;
import vendingmachine.payment.*;
import vendingmachine.users.*;
import vendingmachine.utils.*;
import javax.swing.*;
import java.awt.*;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Main extends JFrame {
	private VendingMachine vm;
    private Admin admin;
    private List<User> allUsers;

    public Main() {
        vm = new VendingMachine();
        admin = new Admin();
        allUsers = UserFileManager.loadAllUsers("cards.csv");

        if (vm.getProducts().isEmpty()) {
            initializeProducts();
        }

        setTitle("SmartVend - Vending Machine");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(500, 400);
        setLocationRelativeTo(null);

        JButton viewBtn = new JButton("View Products");
        JButton buyBtn = new JButton("Buy Products");
        JButton adminBtn = new JButton("Admin Login");
        JButton exitBtn = new JButton("Exit");

        viewBtn.addActionListener(e -> showProducts());
        buyBtn.addActionListener(e -> buyProducts());

        adminBtn.addActionListener(e -> {
            if (admin.login(this)) {   
                admin.showMenu(this, vm, allUsers);
            } else {
                JOptionPane.showMessageDialog(this, "Incorrect password!");
            }
        });

        exitBtn.addActionListener(e -> System.exit(0));

        JPanel panel = new JPanel(new GridLayout(4, 1, 10, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        panel.add(viewBtn);
        panel.add(buyBtn);
        panel.add(adminBtn);
        panel.add(exitBtn);

        add(panel);
    }

    private void initializeProducts() {
        vm.addProduct(new Food("F01", "Chips", 2.5, 20, "2025-12-01"));
        vm.addProduct(new Food("F02", "Chocolate", 3.0, 15, "2025-11-15"));
        vm.addProduct(new Food("F03", "Cookies", 2.0, 25, "2025-10-30"));
        vm.addProduct(new Food("F04", "Sandwich", 4.5, 10, "2025-09-30"));
        vm.addProduct(new Food("F05", "Candy", 1.0, 30, "2025-12-31"));
        vm.addProduct(new Food("F06", "Granola Bar", 1.5, 20, "2025-11-20"));
        vm.addProduct(new Food("F07", "Muffin", 2.2, 18, "2025-10-25"));
        vm.addProduct(new Food("F08", "Pretzels", 1.8, 25, "2025-12-05"));
        vm.addProduct(new Food("F09", "Popcorn", 2.0, 30, "2025-12-10"));
        vm.addProduct(new Food("F10", "Nuts Mix", 3.5, 12, "2025-11-30"));
        vm.addProduct(new Drink("D01", "Cola", 1.5, 20, "2025-10-01"));
        vm.addProduct(new Drink("D02", "Orange Juice", 2.0, 15, "2025-11-01"));
        vm.addProduct(new Drink("D03", "Water", 1.0, 50, "2026-01-01"));
        vm.addProduct(new Drink("D04", "Energy Drink", 2.5, 10, "2025-12-31"));
        vm.addProduct(new Drink("D05", "Milkshake", 3.0, 12, "2025-12-15"));
        vm.addProduct(new Drink("D06", "Lemonade", 2.0, 20, "2025-11-20"));
        vm.addProduct(new Drink("D07", "Iced Tea", 2.0, 18, "2025-11-25"));
        vm.addProduct(new Drink("D08", "Coffee", 2.5, 15, "2025-12-05"));
        vm.addProduct(new Drink("D09", "Green Tea", 2.0, 20, "2025-12-10"));
        vm.addProduct(new Drink("D10", "Smoothie", 3.0, 12, "2025-12-20"));

        ProductFileManager.saveProducts(vm.getProducts(), "products.csv");
    }


    private void showProducts() {
        StringBuilder sb = new StringBuilder("<html><body style='font-family:sans-serif'>");
        sb.append("<h2>🍽 Available Products</h2>");

        sb.append("<h3>Foods:</h3><ul>");
        for (Product p : vm.getProducts()) {
            if (p instanceof Food) {
                sb.append("<li><b>").append(p.getId()).append("</b> - ")
                        .append(p.getName()).append(" ($").append(p.getPrice())
                        .append(") | Stock: ").append(p.getQuantity()).append("</li>");
            }   
        }
        sb.append("</ul>");

        sb.append("<h3>Drinks:</h3><ul>");
        for (Product p : vm.getProducts()) {
            if (p instanceof Drink) {
                sb.append("<li><b>").append(p.getId()).append("</b> - ")
                        .append(p.getName()).append(" ($").append(p.getPrice())
                        .append(") | Stock: ").append(p.getQuantity()).append("</li>");
            }
        }
        sb.append("</ul></body></html>");

        JOptionPane.showMessageDialog(this, sb.toString(), "Products", JOptionPane.INFORMATION_MESSAGE);
    }

    private void buyProducts() {
        Map<Product, Integer> cart = new HashMap<>();

        while (true) {
            String prodId = JOptionPane.showInputDialog(this, "Enter Product ID to buy (or 'done'):");
            if (prodId == null || prodId.equalsIgnoreCase("done")) break;

            Product selected = vm.getProducts().stream()
                    .filter(p -> p.getId().equalsIgnoreCase(prodId))
                    .findFirst()
                    .orElse(null);

            if (selected == null) {
                JOptionPane.showMessageDialog(this, "Invalid product ID.");
                continue;
            }

            String qtyStr = JOptionPane.showInputDialog(this, "Enter quantity:");
            if (qtyStr == null) break;

            int qty;
            try {
                qty = Integer.parseInt(qtyStr);
            } catch (NumberFormatException e) {
                JOptionPane.showMessageDialog(this, "Invalid quantity!");
                continue;
            }

            if (qty <= 0 || qty > selected.getQuantity()) {
                JOptionPane.showMessageDialog(this, "Invalid quantity!");
                continue;
            }

            cart.put(selected, cart.getOrDefault(selected, 0) + qty);
        }

        if (cart.isEmpty()) {
            JOptionPane.showMessageDialog(this, "No products selected.");
            return;
        }

        double total = cart.entrySet().stream()
                .mapToDouble(e -> e.getKey().getPrice() * e.getValue())
                .sum();

        String[] options = {"Card", "Cash"};
        int payChoice = JOptionPane.showOptionDialog(this,
                "Total: $" + total + "\nChoose payment method:",
                "Payment",
                JOptionPane.DEFAULT_OPTION,
                JOptionPane.INFORMATION_MESSAGE,
                null, options, options[0]);

        PaymentMethod payment;
        User user = null;

        if (payChoice == 0) { 
            int hasCard = JOptionPane.showConfirmDialog(this,
                    "Do you already have a card?",
                    "Card Confirmation",
                    JOptionPane.YES_NO_OPTION);

            if (hasCard == JOptionPane.YES_OPTION) {
                String cardId = JOptionPane.showInputDialog(this, "Enter your card ID:");
                if (cardId == null) return;

                user = CardFileManager.findUserByCardId(cardId, "cards.csv");
                if (user == null) {
                    JOptionPane.showMessageDialog(this, "Card not found. Contact admin.");
                    return;
                }

            } else if (hasCard == JOptionPane.NO_OPTION) {
                JOptionPane.showMessageDialog(this, 
                    "You don’t have a card. Please contact the admin to create one.");
                return;
            } else {
                return; 
            }

            payment = user.getCard();

        } else if (payChoice == 1) { 
            String cashStr = JOptionPane.showInputDialog(this, "Insert cash amount:");
            if (cashStr == null) return;
            payment = new Cash(Double.parseDouble(cashStr));

        } else {
            return;
        }

        Transaction transaction = vm.processTransaction(user, cart, payment);

        if (transaction == null) {
            JOptionPane.showMessageDialog(this, "Payment failed! Check balance.");
            return;
        }

        ProductFileManager.saveProducts(vm.getProducts(), "products.csv");
        if (user != null) UserFileManager.saveOrUpdateUser(user, allUsers, "cards.csv");

        List<Transaction> transactions = TransactionFileManager.loadTransactions("transactions.csv", vm.getProducts());
        transactions.add(transaction);
        TransactionFileManager.saveTransactions(transactions, "transactions.csv");

        transaction.generateReceipt().showGUI(this);
    }




    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new Main().setVisible(true));
    }
}
