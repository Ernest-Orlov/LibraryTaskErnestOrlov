package by.javatr.orlov.service.impl;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class Hasher {
    private static Hasher hasher;

    private Hasher (){
    }

    public static Hasher getInstance (){
        if (hasher == null) {
            hasher = new Hasher();
        }
        return hasher;
    }

    public String getHashedPassword (String password){

        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-1");
            digest.update(password.getBytes(StandardCharsets.UTF_8));
            byte[] hash = digest.digest();
            StringBuilder stringBuilder = new StringBuilder();
            for (byte b :
                    hash) {
                stringBuilder.append(String.format("%02X", b));
            }
            return String.valueOf(stringBuilder);
        } catch (NoSuchAlgorithmException e) {
            System.out.println(e.getMessage());
        }
        return null;
    }
}