//package tests.crdts.secure;
//
//import client.stubs.CryptoBean;
//import crdts.secure.SecureOURSet;
//import crdts.secure.cryptoschemes.hj.mlib.HomoDet;
//import crdts.secure.cryptoschemes.hj.mlib.HomoRandAESPkcs7;
//import crdts.simple.OURSet;
//import helpers.ComparableByteArray;
//import org.junit.BeforeClass;
//import org.junit.Test;
//
//import javax.crypto.SecretKey;
//import java.util.UUID;
//
//import static org.junit.Assert.*;
//
//public class SecureOURSetTests {
//
//    static SecretKey key;
//    static byte[] iv;
//
//    @BeforeClass
//    public static void setup() {
//
//        //key = HomoDet.generateKey();
//        key = HomoRandAESPkcs7.generateKey();
//        iv = HomoRandAESPkcs7.generateIV();
//
//    }
//
//    @Test
//    public void testLookup() {
//
//        ComparableByteArray d = CryptoBean.encRandom(key,iv, "dog");
//        ComparableByteArray a = CryptoBean.encRandom(key,iv, "ape");
//        ComparableByteArray c = CryptoBean.encRandom(key,iv, "cat");
//        ComparableByteArray t = CryptoBean.encRandom(key,iv, "tiger");
//
//
//        SecureOURSet ourSet = new SecureOURSet();
//
//        ourSet.add(System.currentTimeMillis(), a);
//        ourSet.add(System.currentTimeMillis(), d);
//        ourSet.add(System.currentTimeMillis(), c);
//
//        ourSet.remove(System.currentTimeMillis(), t);
//        ourSet.remove(System.currentTimeMillis() + 1, c);
//
//        // Actual test
//        assertEquals(ourSet.size(), 2);
//        assertTrue(ourSet.contains(d));
//        assertTrue(ourSet.contains(a));
//    }
//
//    @Test
//    public void testMerge() {
//
//        /*ComparableByteArray d = CryptoBean.encDeterministic(key, "dog");
//        ComparableByteArray a = CryptoBean.encDeterministic(key, "ape");
//        ComparableByteArray c = CryptoBean.encDeterministic(key, "cat");
//        ComparableByteArray t= CryptoBean.encDeterministic(key, "tiger");*/
//
//        ComparableByteArray d = CryptoBean.encRandom(key,iv, "dog");
//        ComparableByteArray a = CryptoBean.encRandom(key,iv, "ape");
//        ComparableByteArray c = CryptoBean.encRandom(key,iv, "cat");
//        ComparableByteArray t = CryptoBean.encRandom(key,iv, "tiger");
//
//        SecureOURSet firstOURSet = new SecureOURSet();
//
//        firstOURSet.add(System.currentTimeMillis(), a);
//        firstOURSet.add(System.currentTimeMillis(), d);
//        firstOURSet.add(System.currentTimeMillis(), c);
//
//        SecureOURSet secondOURSet = new SecureOURSet();
//        secondOURSet.add(System.currentTimeMillis(), c);
//        secondOURSet.add(System.currentTimeMillis(), t);
//
//        // Actual test
//        firstOURSet.merge(secondOURSet.getState());
//
//        firstOURSet.remove(System.currentTimeMillis()+3, c);
//
//        assertEquals(firstOURSet.size(), 3);
//        assertTrue(firstOURSet.contains(a));
//        assertTrue(firstOURSet.contains(d));
//        assertTrue(firstOURSet.contains(t));
//        assertFalse(firstOURSet.contains(c));
//
//        /*OURSet<String> reverseMergeResult = secondOURSet.merge(firstOURSet);
//        assertEquals("'merge' should be symmetrical", mergeResult, reverseMergeResult);*/
//    }
//
//
//}
