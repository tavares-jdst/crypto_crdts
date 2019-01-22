import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.spec.IvParameterSpec;

public class TestDrive {
    public static void main(String[] args) {
        try {
            Cipher cipher = Cipher.getInstance("AES/CTR/PKCS5PADDING");
            KeyGenerator keyGen = KeyGenerator.getInstance("AES");
            keyGen.init(128); // for example
            SecretKey k =  keyGen.generateKey();
            byte [] ivBytes =  "0000000000000000".getBytes("UTF-8");
            IvParameterSpec iv = new IvParameterSpec(ivBytes);
            cipher.init(Cipher.ENCRYPT_MODE, k, iv);

            String texto = "Olá miguitos";
            byte[] c = cipher.doFinal(texto.getBytes());
            System.out.println(c);


            cipher.init(Cipher.DECRYPT_MODE, k, iv);
            byte [] p = cipher.doFinal(c);
            System.out.println( new String(p));


        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
