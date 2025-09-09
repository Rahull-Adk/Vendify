package vendingmachine.users;

import vendingmachine.core.Product;
import vendingmachine.core.Transaction;
import vendingmachine.core.VendingMachine;
import vendingmachine.payment.Card;
import vendingmachine.utils.*;
import javax.swing.*;
import java.awt.Dimension;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.List;
import java.util.Properties;

public class Admin {

    private final String adminPassword;

    public Admin() {
        this.adminPassword = loadAdminPassword();
    }

    private String loadAdminPassword() {
        Properties props = new Properties();
        try (FileInputStream fis = new FileInputStream(".env")) {
            props.load(fis);
        } catch (IOException e) {
            JOptionPane.showMessageDialog(null, "Failed to load admin password from .env");
            return null; 
        }
        return props.getProperty("ADMIN_PASSWORD");
    }


    public boolean login(JFrame parent) {
        String password = JOptionPane.showInputDialog(parent, "Enter admin password:");
        return adminPassword.equals(password);
    }

    public void showMenu(JFrame parent, VendingMachine vm, List<User> allUsers) {
        boolean back = false;
        while (!back) {
            String[] options = {"View Sales Report", "Restock Product", "Manage Cards", "Back"};
            int choice = JOptionPane.showOptionDialog(parent, "Admin Menu", "Admin",
                    JOptionPane.DEFAULT_OPTION, JOptionPane.INFORMATION_MESSAGE, null, options, options[0]);

            switch (choice) {
                case 0 -> viewReportGUI(parent, vm);
                case 1 -> restockProductGUI(parent, vm);
                case 2 -> manageCardsGUI(parent, allUsers);
                default -> back = true;
            }
        }
    }

    public void viewReportGUI(JFrame parent, VendingMachine vm) {
        List<Transaction> transactions = TransactionFileManager.loadTransactions("transactions.csv", vm.getProducts());

        if (transactions.isEmpty()) {
            JOptionPane.showMessageDialog(parent, "No transactions yet.", "Sales Report", JOptionPane.INFORMATION_MESSAGE);
            return;
        }

        String[] columns = {"Transaction #", "Items", "Total Amount", "Date", "Payment Method"};
        Object[][] data = new Object[transactions.size()][columns.length];

        for (int i = 0; i < transactions.size(); i++) {
            Transaction t = transactions.get(i);

            StringBuilder items = new StringBuilder();
            t.getProducts().forEach((product, qty) ->
                    items.append(product.getName()).append(" x").append(qty).append(", "));
            if (items.length() > 0) items.setLength(items.length() - 2);

            data[i][0] = i + 1; 
            data[i][1] = items.toString();
            data[i][2] = "$" + t.getTotalAmount();
            data[i][3] = t.getTimestampString();
            data[i][4] = t.getPaymentMethod().getClass().getSimpleName();
        }

        JTable table = new JTable(data, columns);
        table.setEnabled(false);
        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.setPreferredSize(new Dimension(600, 300));

        JOptionPane.showMessageDialog(parent, scrollPane, "Sales Report", JOptionPane.INFORMATION_MESSAGE);
    }


    public void restockProductGUI(JFrame parent, VendingMachine vm) {
        String prodId = JOptionPane.showInputDialog(parent, "Enter Product ID to restock:");
        if (prodId == null) return;


        Product prod = ProductFileManager.findProductById(prodId, vm.getProducts());

        if (prod == null) {
            JOptionPane.showMessageDialog(parent, "Invalid product ID.");
            return;
        }

        String qtyStr = JOptionPane.showInputDialog(parent, "Enter quantity to add:");
        int addQty = Integer.parseInt(qtyStr);

        if (addQty > 0) {
            prod.setQuantity(prod.getQuantity() + addQty);
            ProductFileManager.saveProducts(vm.getProducts(), "products.csv");
            JOptionPane.showMessageDialog(parent, prod.getName() + " restocked. New stock: " + prod.getQuantity());
        } else {
            JOptionPane.showMessageDialog(parent, "Quantity must be positive.");
        }
    }

    public void manageCardsGUI(JFrame parent, List<User> allUsers) {
        String[] options = {"Create new card", "Add balance", "Back"};
        int choice = JOptionPane.showOptionDialog(parent, "Card Management", "Admin",
                JOptionPane.DEFAULT_OPTION, JOptionPane.INFORMATION_MESSAGE, null, options, options[0]);

        if (choice == 0) {
            createNewCardGUI(allUsers, "cards.csv");
        } else if (choice == 1) {
            String cardId = JOptionPane.showInputDialog(parent, "Enter card ID:");
            User target = allUsers.stream()
                    .filter(u -> u.getCard().getCardId().equals(cardId))
                    .findFirst().orElse(null);

            if (target == null) {
                JOptionPane.showMessageDialog(parent, "Card not found!");
                return;
            }

            String amountStr = JOptionPane.showInputDialog(parent, "Enter amount to add:");
            double amount = Double.parseDouble(amountStr);
            if (amount > 0) {
                target.getCard().deposit(amount);
                UserFileManager.saveAllCards(allUsers, "cards.csv");
                JOptionPane.showMessageDialog(parent,
                        "Balance updated! New balance: $" + target.getCard().getBalance());
            } else {
                JOptionPane.showMessageDialog(parent, "Amount must be positive.");
            }
        }
    }

    public Card createNewCardGUI(List<User> allUsers, String filename) {
        String name = JOptionPane.showInputDialog(null, "Enter user name:");

        String cardId;
        boolean exists;
        do {
            int randomNum = 100000 + (int)(Math.random() * 900000);
            cardId =  String.valueOf(randomNum);

            exists = false;
            for (User u : allUsers) {
                if (u.getCard().getCardId().equals(cardId)) {
                    exists = true;
                    break;
                }
            }
        } while (exists);

        Card card = new Card(name, cardId, 0.0);
        allUsers.add(new User(name, card));
        UserFileManager.saveAllCards(allUsers, filename);
        JOptionPane.showMessageDialog(null, "New card created: " + cardId);
        return card;
    }


}
