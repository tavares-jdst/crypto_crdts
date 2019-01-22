//package tests.crdts.secure;
//
//import client.stubs.CryptoBean;
//import crdts.secure.SecureLWWSet;
//import crdts.secure.cryptoschemes.hj.mlib.HomoDet;
//import helpers.ComparableByteArray;
//import org.junit.BeforeClass;
//import org.junit.Test;
//
//import javax.crypto.SecretKey;
//
//import static org.junit.Assert.assertTrue;
//
//
//public class SecureLWWSetTests {
//
//    static SecretKey key;
//
//    @BeforeClass
//    public static void setup() {
//        key = HomoDet.generateKey();
//    }
//
//    @Test
//    public void testLookup() {
//        final SecureLWWSet lwwSet = new SecureLWWSet();
//
//        ComparableByteArray dog = CryptoBean.encDeterministic(key, "dog");
//        ComparableByteArray ape = CryptoBean.encDeterministic(key, "ape");
//        ComparableByteArray cat = CryptoBean.encDeterministic(key, "cat");
//        ComparableByteArray tiger = CryptoBean.encDeterministic(key, "tiger");
//
//        lwwSet.add(dog,1);
//        lwwSet.add(cat,1);
//        lwwSet.add(ape,1);
//        lwwSet.add(tiger,1);
//
//        lwwSet.remove(cat,2);
//        lwwSet.remove(dog,2);
//
//        // Actual test
//        assertTrue(lwwSet.size() == 2);
//        assertTrue(lwwSet.contains(ape));
//        assertTrue(lwwSet.contains(tiger));
//    }
//
//    @Test
//    public void testMerge() {
//        ComparableByteArray dog = CryptoBean.encDeterministic(key, "dog");
//        ComparableByteArray ape = CryptoBean.encDeterministic(key, "ape");
//        ComparableByteArray cat = CryptoBean.encDeterministic(key, "cat");
//        ComparableByteArray tiger = CryptoBean.encDeterministic(key, "tiger");
//
//        final SecureLWWSet firstLwwSet = new SecureLWWSet();
//        firstLwwSet.add(ape,3);
//        firstLwwSet.add(dog,1);
//        firstLwwSet.add(cat,1);
//        firstLwwSet.remove(cat,2);
//
//        final SecureLWWSet secondLwwSet = new SecureLWWSet();
//        secondLwwSet.add(ape,1);
//        secondLwwSet.add(tiger,1);
//        secondLwwSet.add(cat,1);
//        secondLwwSet.remove(ape,2);
//
//        // Actual test
//        firstLwwSet.merge(secondLwwSet.getState());
//        assertTrue(firstLwwSet.contains(ape));
//        assertTrue(firstLwwSet.contains(dog));
//        assertTrue(firstLwwSet.contains(tiger));
//        assertTrue(firstLwwSet.size()==3);
//
//    }
//
//}
