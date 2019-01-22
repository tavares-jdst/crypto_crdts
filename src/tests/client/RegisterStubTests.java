//package tests.client;
//
//import client.stubs.RegisterStub;
//import crdts.secure.cryptoschemes.hj.mlib.HomoRandAESPkcs7;
//import org.junit.BeforeClass;
//import org.junit.Test;
//
//import javax.crypto.SecretKey;
//
//import static org.junit.Assert.assertEquals;
//
//public class RegisterStubTests {
//    static SecretKey key;
//    static byte[] iv;
//
//    @BeforeClass
//    public static void setup() {
//
//        key = HomoRandAESPkcs7.generateKey();
//        iv = HomoRandAESPkcs7.generateIV();
//    }
//
//    @Test
//    public void testLookup() {
//        RegisterStub<String> sgs = new RegisterStub<>(key, iv, "dog");
//
//        sgs.put("ape");
//        sgs.put("cat");
//        sgs.put("tiger");
//
//        assertEquals("tiger", sgs.get());
//    }
//}
