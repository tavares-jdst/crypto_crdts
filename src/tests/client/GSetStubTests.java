//package tests.client;
//
//import client.stubs.GSetStub;
//import crdts.secure.cryptoschemes.hj.mlib.HomoDet;
//import org.junit.BeforeClass;
//import org.junit.Test;
//
//import javax.crypto.SecretKey;
//
//import static org.junit.Assert.assertTrue;
//
//public class GSetStubTests {
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
//        GSetStub<String> stub = new GSetStub<>(key);
//
//        stub.add("dog");
//        stub.add("ape");
//        stub.add("cat");
//
//        assertTrue(stub.size() == 3);
//        assertTrue(stub.contains("dog"));
//        assertTrue(stub.contains("ape"));
//        assertTrue(stub.contains("ape"));
//    }
//}
//
//
