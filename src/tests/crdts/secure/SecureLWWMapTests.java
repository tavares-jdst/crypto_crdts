//package tests.crdts.secure;
//
//import client.stubs.CryptoBean;
//import crdts.secure.SecureLWWMap;
//import crdts.secure.cryptoschemes.hj.mlib.HomoDet;
//import crdts.secure.cryptoschemes.hj.mlib.HomoRandAESPkcs7;
//import helpers.ComparableByteArray;
//import org.junit.BeforeClass;
//import org.junit.Test;
//
//import javax.crypto.SecretKey;
//
//import static org.junit.Assert.*;
//
//public class SecureLWWMapTests {
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
//    public void testLookup() {
//        ComparableByteArray Joao = CryptoBean.encDeterministic(kD, "João");
//        ComparableByteArray Ze = CryptoBean.encDeterministic(kD, "Zé");
//        ComparableByteArray Asdrubal = CryptoBean.encDeterministic(kD, "Asdrúbal");
//
//        ComparableByteArray CS = CryptoBean.encRandom(kR, iv, "CS");
//        ComparableByteArray IT = CryptoBean.encRandom(kR, iv, "IT");
//        ComparableByteArray Finance = CryptoBean.encRandom(kR, iv, "Finance");
//
//        SecureLWWMap map = new SecureLWWMap();
//        map.put(Ze, CS, 0);
//        map.put(Asdrubal, IT, 1);
//        map.put(Joao, Finance, 3);
//
//        map.remove(Joao,3);
//        map.remove(Joao, 1);
//        map.remove(Asdrubal,3);
//
//        //Test
//        assertTrue(map.contains(Ze));
//        assertFalse(map.contains(Asdrubal));
//        assertFalse(map.contains(Joao));
//
//        assertNotNull(map.get(Ze));
//        assertNull(map.get(Joao));
//        assertNull(map.get(Asdrubal));
//
//        assertTrue(map.get(Ze).equals(CS));
//
//        assertTrue(map.size()==1);
//    }
//
//    @Test
//    public void testMerge(){
//        ComparableByteArray Joao = CryptoBean.encDeterministic(kD, "João");
//        ComparableByteArray Ze = CryptoBean.encDeterministic(kD, "Zé");
//        ComparableByteArray Asdrubal = CryptoBean.encDeterministic(kD, "Asdrúbal");
//        ComparableByteArray Kimbe = CryptoBean.encDeterministic(kD, "Kimbé");
//
//        ComparableByteArray CS = CryptoBean.encRandom(kR, iv,"CS");
//        ComparableByteArray IT = CryptoBean.encRandom(kR, iv, "IT");
//        ComparableByteArray Finance = CryptoBean.encRandom(kR, iv, "Finance");
//        ComparableByteArray COD = CryptoBean.encRandom(kR, iv, "COD Player");
//        ComparableByteArray Calceteiro = CryptoBean.encRandom(kR, iv, "Calceteiro Maritimo");
//        ComparableByteArray Fisherman = CryptoBean.encRandom(kR, iv, "Fisherman");
//
//        SecureLWWMap map = new SecureLWWMap();
//        SecureLWWMap map1 = new SecureLWWMap();
//
//        map.put(Ze, CS, 0);
//        map.put(Asdrubal, IT, 1);
//        map.put(Joao, Finance, 3);
//        map.remove(Asdrubal,3);
//
//        map1.put(Asdrubal, COD, 3);
//        map1.put(Ze, Calceteiro, 3);
//        map1.put(Kimbe,Fisherman, 5);
//
//        map.merge(map1.getState());
//
//        //Test
//        assertTrue(map.contains(Ze));
//        assertTrue(map.contains(Asdrubal));
//        assertTrue(map.contains(Joao));
//        assertTrue(map.contains(Kimbe));
//
//        assertNotNull(map.get(Ze));
//        assertNotNull(map.get(Asdrubal));
//        assertNotNull(map.get(Kimbe));
//        assertNotNull(map.get(Joao));
//
//        assertEquals(COD, map.get(Asdrubal));
//        assertEquals(Calceteiro,map.get(Ze));
//        assertEquals(Fisherman,map.get(Kimbe));
//        assertEquals(Finance,map.get(Joao));
//
//        assertTrue(map.size()==4);
//    }
//}
