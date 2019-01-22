//package tests.crdts.secure;
//
//import client.stubs.CryptoBean;
//import crdts.secure.SecureLWWRegister;
//import crdts.secure.cryptoschemes.hj.mlib.HomoDet;
//import crdts.secure.cryptoschemes.hj.mlib.HomoRandAESPkcs7;
//import crdts.simple.LWWRegister;
//import helpers.ComparableByteArray;
//import org.junit.BeforeClass;
//import org.junit.Test;
//
//import javax.crypto.SecretKey;
//
//import static org.junit.Assert.assertEquals;
//import static org.junit.Assert.assertTrue;
//
//public class SecureRegisterTests {
//
//    static SecretKey k;
//    static byte[] iv;
//
//    @BeforeClass
//    public static void setup() {
//        k = HomoRandAESPkcs7.generateKey();
//        iv = HomoRandAESPkcs7.generateIV();
//    }
//
//    @Test
//    public void testLookup() {
//        ComparableByteArray d = CryptoBean.encRandom(k, iv, "dog");
//        ComparableByteArray a = CryptoBean.encRandom(k, iv, "ape");
//        ComparableByteArray c = CryptoBean.encRandom(k, iv, "cat");
//        ComparableByteArray t = CryptoBean.encRandom(k, iv, "tiger");
//
//        SecureLWWRegister sgs = new SecureLWWRegister(d, 1);
//
//        sgs.put(a, 2);
//        sgs.put(c, 2);
//        sgs.put(t, 2);
//
//        assertEquals(t,sgs.get());
//    }
//
//    @Test
//    public void merge() {
//
//        ComparableByteArray d = CryptoBean.encRandom(k, iv, "dog");
//        ComparableByteArray z = CryptoBean.encRandom(k, iv, "zebra");
//        ComparableByteArray t = CryptoBean.encRandom(k, iv, "tiger");
//
//        SecureLWWRegister sgs = new SecureLWWRegister(d, 1);
//        SecureLWWRegister sgs1 = new SecureLWWRegister(z,8);
//
//        sgs.put(t, 2);
//
//        sgs1.put(d,7);
//        sgs.merge(sgs1.getState());
//
//        assertEquals(z, sgs.get());
//    }
//}
