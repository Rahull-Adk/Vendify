package vendingmachine.utils;

import java.io.*;
import java.util.*;
import vendingmachine.payment.Card;
import vendingmachine.users.User;

public class UserFileManager {

    public static void saveOrUpdateUser(User user, List<User> allUsers, String filename) {
        allUsers.removeIf(u -> u.getName().equalsIgnoreCase(user.getName()));
        allUsers.add(user);
        saveAllCards(allUsers, filename);
    }

    public static boolean usernameExists(String username, String filename) {
        List<User> users = loadAllUsers(filename);
        for (User u : users) {
            if (u.getName().equalsIgnoreCase(username)) {
                return true;
            }
        }
        return false;
    }

    public static void saveAllCards(List<User> users, String filename) {
        try (FileWriter fw = new FileWriter(filename)) {
            for (User user : users) {
                Card card = user.getCard();
                fw.write(card.getCardId() + "," + card.getHolderName() + "," + card.getBalance() + "\n");
            }
        } catch (IOException e) {
            System.out.println("Error saving cards: " + e.getMessage());
        }
    }

    public static List<User> loadAllUsers(String filename) {
        List<User> users = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader(filename))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts.length >= 3) {
                    String cardId = parts[0].trim();
                    String holderName = parts[1].trim();
                    double balance = Double.parseDouble(parts[2].trim());
                    Card card = new Card(holderName, cardId, balance);
                    users.add(new User(holderName, card));
                }
            }
        } catch (FileNotFoundException e) {
        } catch (Exception e) {
            e.printStackTrace();
        }
        return users;
    }
}
