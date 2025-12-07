package com.example.currencyconverter;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;

public class SecurityUtils {

    public static String hashPassword(String password) {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] hash = digest.digest(password.getBytes(StandardCharsets.UTF_8));

            StringBuilder hexString = new StringBuilder();
            for (byte b : hash) {
                hexString.append(String.format("%02x", b));
            }

            return hexString.toString();
        } catch (Exception e) {
            return null;
        }
    }
}
