//package tests.client;
//
//import client.stubs.LWWListStub;
//import crdts.secure.cryptoschemes.hj.mlib.HomoRandAESPkcs7;
//import org.junit.BeforeClass;
//import org.junit.Test;
//
//import javax.crypto.SecretKey;
//
//import static org.junit.Assert.assertFalse;
//import static org.junit.Assert.assertTrue;
//
//public class LWWListStubTests {
//
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
//        LWWListStub<Integer> stub = new LWWListStub<>(key, iv);
//        stub.insert(0, 0);
//        stub.insert(1, 1);
//        stub.insert(2, 2);
//        stub.insert(3, 3);
//        stub.insert(4, 4);
//
//        //Test 1
//        assertTrue(stub.contains(0));
//        assertTrue(stub.contains(1));
//        assertTrue(stub.contains(2));
//        assertTrue(stub.contains(3));
//        assertTrue(stub.contains(4));
//        assertTrue(stub.size() == 5);
//
//
//        stub.remove();
//        stub.remove(2);
//
//        //Test 2
//        assertTrue(stub.contains(0));
//        assertTrue(stub.contains(1));
//        assertFalse(stub.contains(2));
//        assertTrue(stub.contains(3));
//        assertFalse(stub.contains(4));
//        assertTrue(stub.size() == 3);
//    }
//}
