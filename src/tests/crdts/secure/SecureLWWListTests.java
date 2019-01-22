//package tests.crdts.secure;
//
//import client.Utils;
//import client.stubs.CryptoBean;
//import crdts.secure.SecureLWWList;
//import crdts.secure.SecureList;
//import crdts.secure.cryptoschemes.hj.mlib.HomoDet;
//import helpers.ComparableByteArray;
//import org.junit.BeforeClass;
//import org.junit.Test;
//
//import javax.crypto.SecretKey;
//
//import static org.junit.Assert.assertFalse;
//import static org.junit.Assert.assertTrue;
//
//public class SecureLWWListTests {
//
//    static SecretKey key;
//    //static byte[] iv;
//
//    @BeforeClass
//    public static void setup() {
//        key = HomoDet.generateKey();
//        //key = HomoRandAESPkcs7.generateKey();
//        //iv = HomoRandAESPkcs7.generateIV();
//    }
//
//    @Test
//    public void testLookup() {
//
//        ComparableByteArray zero = CryptoBean.encDeterministic(key, Utils.toByte(0));
//        ComparableByteArray one = CryptoBean.encDeterministic(key, Utils.toByte(1));
//        ComparableByteArray two = CryptoBean.encDeterministic(key, Utils.toByte(2));
//        ComparableByteArray three = CryptoBean.encDeterministic(key, Utils.toByte(3));
//        ComparableByteArray four = CryptoBean.encDeterministic(key, Utils.toByte(4));
//
//        SecureLWWList ints = new SecureLWWList();
//        ints.insert(0, zero, 1);
//        ints.insert(1, one, 2);
//        ints.insert(2, two, 3);
//        ints.insert(3, three, 3);
//        ints.insert(4, four, 10);
//
//        //Test 1
//        assertTrue(ints.contains(zero));
//        assertTrue(ints.contains(one));
//        assertTrue(ints.contains(two));
//        assertTrue(ints.contains(three));
//        assertTrue(ints.contains(four));
//        assertTrue(ints.size() == 5);
//
//
//        ints.remove(10);
//        ints.remove(2, 4);
//
//        //Test 2
//        assertTrue(ints.contains(zero));
//        assertTrue(ints.contains(one));
//        assertFalse(ints.contains(two));
//        assertTrue(ints.contains(three));
//        assertFalse(ints.contains(four));
//        assertTrue(ints.size() == 3);
//
//    }
//
//    @Test
//    public void testMerge() {
//
//        ComparableByteArray zero = CryptoBean.encDeterministic(key, Utils.toByte(0));
//        ComparableByteArray one = CryptoBean.encDeterministic(key, Utils.toByte(1));
//        ComparableByteArray two = CryptoBean.encDeterministic(key, Utils.toByte(2));
//        ComparableByteArray three = CryptoBean.encDeterministic(key, Utils.toByte(3));
//        ComparableByteArray four = CryptoBean.encDeterministic(key, Utils.toByte(4));
//        ComparableByteArray five = CryptoBean.encDeterministic(key, Utils.toByte(5));
//        ComparableByteArray eight = CryptoBean.encDeterministic(key, Utils.toByte(8));
//        ComparableByteArray ten = CryptoBean.encDeterministic(key,  Utils.toByte(10));
//
//        SecureLWWList ints = new SecureLWWList();
//        ints.insert(0, zero, 1);
//        ints.insert(1, one, 2);
//        ints.insert(2, two, 3);
//        ints.insert(3, three, 3);
//        ints.insert(4, three, 19);
//
//        SecureLWWList intsC = new SecureLWWList();
//        intsC.insert(five, 5);
//        intsC.insert(eight, 10);
//        intsC.insert(ten, 9);
//
//        ints.merge(intsC.getState());
//        assertTrue(ints.contains(zero));
//        assertTrue(ints.contains(one));
//        assertTrue(ints.contains(two));
//        assertTrue(ints.contains(three));
//        assertTrue(ints.contains(five));
//        assertTrue(ints.contains(eight));
//        assertTrue(ints.contains(ten));
//
//        assertTrue(ints.size() == 8);
//    }
//}
