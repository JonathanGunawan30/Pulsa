package controllers;
import models.User;
import database.DBConnection;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
public class UserController {
    public boolean registerUser(User user) {
        try (Connection conn = DBConnection.getConnection()) {
            String hashedPassword = hashPassword(user.getPassword());
            String query = "INSERT INTO users (username, password, phone_number, role) VALUES (?, ?, ?, ?)";
            PreparedStatement stmt = conn.prepareStatement(query);
            stmt.setString(1, user.getUsername());
            stmt.setString(2, hashedPassword);
            stmt.setString(3, user.getPhoneNumber());
            stmt.setString(4, user.getRole());

            int rowsAffected = stmt.executeUpdate();
            return rowsAffected > 0;
        } catch (SQLException ex) {
            System.out.println("Error: " + ex.getMessage());
            return false;
        }
    }

    public boolean login(String username, String password) {
        try (Connection conn = DBConnection.getConnection()) {
            String hashedPassword = hashPassword(password);
            String query = "SELECT COUNT(*) FROM users WHERE username = ? AND password = ?";
            PreparedStatement stmt = conn.prepareStatement(query);
            stmt.setString(1, username);
            stmt.setString(2, hashedPassword);

            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return rs.getInt(1) > 0;
            }
            return false;
        } catch (SQLException ex) {
            System.out.println("Error: " + ex.getMessage());
            return false;
        }
    }
    
    private String hashPassword(String password) {
     try {
         MessageDigest digest = MessageDigest.getInstance("SHA-256");
         byte[] hash = digest.digest(password.getBytes());
         StringBuilder hexString = new StringBuilder();
         for (byte b : hash) {
             String hex = Integer.toHexString(0xff & b);
             if (hex.length() == 1) hexString.append('0');
             hexString.append(hex);
         }
         return hexString.toString();
     } catch (NoSuchAlgorithmException e) {
         throw new RuntimeException("Error hashing password", e);
     }
}
}
