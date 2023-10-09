package io.github.tanguygab.spygotsecurity.utils;

import io.github.tanguygab.spygotsecurity.SpyGotSecurity;
import org.bukkit.Bukkit;

import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.spec.KeySpec;
import java.util.function.Consumer;

public class PasswordUtils {

    private static final SecureRandom SECURE_RANDOM = new SecureRandom();
    private static final SecretKeyFactory factory;

    static {
        try {factory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA1");}
        catch (NoSuchAlgorithmException e) {throw new RuntimeException(e);}
    }

    public static byte[] newSalt() {
        byte[] salt = new byte[16];
        SECURE_RANDOM.nextBytes(salt);
        return salt;
    }

    public static byte[] hash(String password, byte[] salt) {
        KeySpec spec = new PBEKeySpec(password.toCharArray(), salt, 65536, 128);
        try {return factory.generateSecret(spec).getEncoded();}
        catch (Exception e) {throw new RuntimeException(e);}
    }

    public static void asyncHash(String password, byte[] salt, Consumer<byte[]> consumer) {
        Bukkit.getServer().getScheduler().runTaskAsynchronously(SpyGotSecurity.getInstance(),()->{
            byte[] hashed = hash(password,salt);
            consumer.accept(hashed);
        });
    }


}
