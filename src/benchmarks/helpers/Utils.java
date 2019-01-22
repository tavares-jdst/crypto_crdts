package benchmarks.helpers;

import java.security.MessageDigest;

public class Utils {

    private static String	digits = "0123456789abcdef";


    public static String digestAndString(String s) throws Exception{
        return toHex(digest(s));
    }

    public static byte[] digest(String s) throws Exception{
        MessageDigest hashF = MessageDigest.getInstance("SHA-1");
        hashF.update(s.getBytes());
        return hashF.digest();

    }

    public static String bytesToHexa(byte[] bytes) {
        StringBuilder sb = new StringBuilder();
        for (byte b : bytes) {
            sb.append(String.format("0x%02X ", b));
        }
        return sb.toString();
    }

    public static String toHex(byte[] data)
    {
        StringBuffer	buf = new StringBuffer();

        for (int i = 0; i != data.length; i++)
        {
            int	v = data[i] & 0xff;

            buf.append(digits.charAt(v >> 4));
            buf.append(digits.charAt(v & 0xf));
        }

        return buf.toString();
    }
}
