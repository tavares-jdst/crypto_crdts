package client;

import java.io.*;

public class Utils {

    public static byte[] toByte(Object obj) {

        try {
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            ObjectOutputStream oos = new ObjectOutputStream(baos);

            oos.writeObject(obj);
            oos.close();

            byte[] data = baos.toByteArray();
            baos.close();

            return data;

        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static Object fromByte(byte[] obj) {

        try {
            ByteArrayInputStream bais = new ByteArrayInputStream(obj);
            ObjectInputStream ois = new ObjectInputStream(bais);

            Object o = ois.readObject();

            bais.close();
            ois.close();

            return o;

        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }

    }
}
