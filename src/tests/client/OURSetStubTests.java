//package tests.client;
//
//import client.stubs.OURSetStub;
//import crdts.secure.cryptoschemes.hj.mlib.HomoDet;
//import org.junit.BeforeClass;
//import org.junit.Test;
//
//import javax.crypto.SecretKey;
//import java.util.UUID;
//
//import static org.junit.Assert.assertEquals;
//import static org.junit.Assert.assertTrue;
//
//public class OURSetStubTests {
//
//    static SecretKey key;
//
//    @BeforeClass
//    public static void setup() {
//
//        key = HomoDet.generateKey();
//    }
//
//    @Test
//    public void lookup(){
//        OURSetStub<String> stub = new OURSetStub<>(key);
//
//        stub.add("ape");
//        stub.add("dog");
//        stub.add("cat");
//
//        try {
//            Thread.sleep(1000);
//        } catch (InterruptedException e) {
//            e.printStackTrace();
//        }
//
//        stub.remove("tiger");
//        stub.remove("cat");
//
//        // Actual test
//        assertEquals(stub.size(), 2);
//        assertTrue(stub.contains("dog"));
//        assertTrue(stub.contains("ape"));
//    }
//
//
//}
