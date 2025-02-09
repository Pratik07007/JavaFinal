package app;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.nio.charset.StandardCharsets;

/**
 * This class provides methods for hashing passwords using SHA-256 and verifying if a given password matches the stored hashed password.
 */
public class PasswordHash {

    /**
     * Verifies if the input password matches the stored hashed password.
     *
     * @param inputPassword The input password entered by the user.
     * @param storedHashedPassword The stored hashed password (e.g., from the database).
     * @return {@code true} if the input password matches the stored hashed password, {@code false} otherwise.
     */
    public static boolean verifyPassword(String inputPassword, String storedHashedPassword) {
        return hashPassword(inputPassword).equals(storedHashedPassword);
    }

    /**
     * Hashes the given password using SHA-256.
     *
     * @param password The password to be hashed.
     * @return A string representing the hashed password in hexadecimal format.
     * @throws RuntimeException If there is an error during the hashing process (e.g., unsupported algorithm).
     */
    public static String hashPassword(String password) {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256"); // Initialize SHA-256 MessageDigest
            // Use UTF-8 encoding to convert the password into bytes consistently
            byte[] hashBytes = digest.digest(password.getBytes(StandardCharsets.UTF_8));
            StringBuilder hexString = new StringBuilder();
            // Convert each byte to its hexadecimal representation
            for (byte b : hashBytes) {
                hexString.append(String.format("%02x", b)); 
            }
            return hexString.toString(); // Return the hashed password in hexadecimal format
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("Hashing error: " + e.getMessage()); // Handle error if SHA-256 is not available
        }
    }
}
