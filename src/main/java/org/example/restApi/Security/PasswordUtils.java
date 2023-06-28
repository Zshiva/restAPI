package org.example.restApi.Security;

import org.mindrot.jbcrypt.BCrypt;

public class PasswordUtils {

    // Generate a salt and hash the password
    public static String hashPassword(String password) {
        try {
            String salt = BCrypt.gensalt();
            return BCrypt.hashpw(password, salt);
        } catch (Exception e) {
            // Handle any exception that may occur during the hashing process
            e.printStackTrace();
            return null;
        }
    }

    // Verify if the provided password matches the hashed password
    public static boolean verifyPassword(String password, String hashedPassword) {
        try {
            return BCrypt.checkpw(password, hashedPassword);
        } catch (Exception e) {
            // Handle any exception that may occur during the verification process
            e.printStackTrace();
            return false;
        }
    }
}
