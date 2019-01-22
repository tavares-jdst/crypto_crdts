package crdts.secure.cryptoschemes;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.spec.IvParameterSpec;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;

public class Probabilistic {

    private static final int KEYSIZE = 128;
    private static final int IVSIZE = 16;
    private static final String ALGORITHM = "AES/CTR/PKCS5PADDING";

    IvParameterSpec iv;
    Cipher enc_cipher, dec_cipher;
    SecretKey key;

    public Probabilistic() {
        init( generateKey(), generateIV() );
    }

    public Probabilistic(SecretKey KEY, byte[] IV) {
        init(KEY,IV);
    }

    private void init(SecretKey KEY, byte[] IV) {

        try {
            iv = new IvParameterSpec(IV);
            key = KEY;

            enc_cipher = Cipher.getInstance(ALGORITHM);
            enc_cipher.init(Cipher.ENCRYPT_MODE, key, iv);

            dec_cipher = Cipher.getInstance(ALGORITHM);
            dec_cipher.init(Cipher.DECRYPT_MODE, key, iv);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static SecretKey generateKey() {
        return generateKey(KEYSIZE);
    }

    public static SecretKey generateKey(int size) {
        KeyGenerator keyGen = null;
        try {
            keyGen = KeyGenerator.getInstance("AES");
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        keyGen.init(size);
        return keyGen.generateKey();
    }

    public static byte[] generateIV() {
        byte[] randomBytes = new byte[IVSIZE];
        SecureRandom rnd = new SecureRandom();
        rnd.nextBytes(randomBytes);
        return randomBytes;
    }

    public byte[] encrypt(byte[] value) {
        try {

            return enc_cipher.doFinal(value);

        } catch (Exception e) {
            e.printStackTrace();
            return new byte[0];
        }
    }

    public byte[] decrypt(byte[] value) {
        try {

            return dec_cipher.doFinal(value);

        } catch (Exception e) {
            e.printStackTrace();
            return new byte[0];
        }
    }
}
