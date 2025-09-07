package vendingmachine.utils;

import java.io.*;
import vendingmachine.payment.Card;
import vendingmachine.users.User;

public class CardFileManager {

    public static boolean cardExists(String cardId, String filename) {
        try (BufferedReader br = new BufferedReader(new FileReader(filename))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts.length >= 1 && parts[0].equals(cardId)) {
                    return true;
                }
            }
        } catch (IOException e) {
            return false;
        }
        return false;
    }

    public static User findUserByCardId(String cardId, String filename) {
        try (BufferedReader br = new BufferedReader(new FileReader(filename))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts[0].equals(cardId)) {
                    String holderName = parts[1];
                    double balance = Double.parseDouble(parts[2]);
                    Card card = new Card(holderName, cardId, balance);
                    return new User(holderName, card);
                }
            }
        } catch (IOException e) {
            System.out.println("Error reading cards file: " + e.getMessage());
        }
        return null;
    }
}
