package utils;

import java.security.MessageDigest;

public final class Hashing
{
    private Hashing() {}

    public static String getHashPassword(String password) {
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            byte[] array = md.digest(password.getBytes());

            StringBuilder sb = new StringBuilder();
            for (byte b : array) sb.append(String.format("%02x", b));

            return sb.toString();
        }
        catch (Exception e) {
            return null;
        }
    }
}