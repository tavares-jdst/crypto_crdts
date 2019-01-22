//package tests.client;
//
//import client.stubs.LWWSetStub;
//import crdts.secure.cryptoschemes.hj.mlib.HomoDet;
//import org.junit.BeforeClass;
//import org.junit.Test;
//
//import javax.crypto.SecretKey;
//
//import static org.junit.Assert.assertTrue;
//
//public class LWWSetStubTests {
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
//        LWWSetStub<String> stub = new LWWSetStub<>(key);
//
//        stub.add("dog");
//        stub.add("cat");
//        stub.add("ape");
//        stub.add("tiger");
//
//        try {
//            Thread.sleep(500);
//        } catch (InterruptedException e) {
//            e.printStackTrace();
//        }
//
//        stub.remove("cat");
//        stub.remove("dog");
//
//        // Actual test
//        assertTrue(stub.size() == 2);
//        assertTrue(stub.contains("ape"));
//        assertTrue(stub.contains("tiger"));
//    }
//}
