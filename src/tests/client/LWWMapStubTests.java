//package tests.client;
//
//import client.stubs.OURMapStub;
//import crdts.secure.cryptoschemes.hj.mlib.HomoDet;
//import crdts.secure.cryptoschemes.hj.mlib.HomoRandAESPkcs7;
//import org.junit.BeforeClass;
//import org.junit.Test;
//
//import javax.crypto.SecretKey;
//
//import static org.junit.Assert.*;
//import static org.junit.Assert.assertNull;
//import static org.junit.Assert.assertTrue;
//
//public class LWWMapStubTests {
//
//    static SecretKey kD, kR;
//    static byte[] iv;
//
//    @BeforeClass
//    public static void setup() {
//        kD = HomoDet.generateKey();
//        kR = HomoRandAESPkcs7.generateKey();
//        iv = HomoRandAESPkcs7.generateIV();
//    }
//
//    @Test
//    public void lookup() {
//        OURMapStub<String,String> stub = new OURMapStub<>(kD,kR,iv);
//        stub.put("Zé", "CS");
//        stub.put("Asdrúbal", "IT");
//        stub.put("João", "Finance");
//
//        try {
//            Thread.sleep(500);
//        } catch (InterruptedException e) {
//            e.printStackTrace();
//        }
//
//        stub.remove("João");
//        stub.remove("Asdrúbal");
//
//        //Test
//        assertTrue(stub.contains("Zé"));
//        assertFalse(stub.contains("Asdrúbal"));
//        assertFalse(stub.contains("João"));
//
//        assertNotNull(stub.get("Zé"));
//        assertNull(stub.get("João"));
//        assertNull(stub.get("Asdrúbal"));
//
//        assertTrue(stub.get("Zé").equals("CS"));
//
//        assertTrue(stub.size()==1);
//
//    }
//}
