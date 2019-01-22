//package tests.crdts.secure;
//
//import crdts.secure.SecureCounter;
//import crdts.secure.cryptoschemes.Paillier;
//import org.junit.BeforeClass;
//import org.junit.Test;
//
//import java.math.BigInteger;
//
//import static org.junit.Assert.assertEquals;
//
//public class SecureCounterTests {
//
//    static BigInteger nsquare;
//    static Paillier p;
//
//    @BeforeClass
//    public static void setup() {
//
//        p = new Paillier();
//        nsquare = p.getNsquare();
//    }
//
//    @Test
//    public void testLookup() {
//        SecureCounter ctr = new SecureCounter(1,nsquare);
//        BigInteger one = p.Encryption(this.convert(1));
//        BigInteger two = p.Encryption(this.convert(2));
//        BigInteger three = p.Encryption(this.convert(3));
//
//        ctr.inc(two);
//        ctr.inc(three);
//        ctr.inc(two);
//
//        ctr.dec(three);
//        ctr.dec(three);
//
//        BigInteger vCtr = ctr.get();
//        assertEquals(1, p.Decryption(vCtr).intValue());
//
//        SecureCounter ctr1 = new SecureCounter(2,nsquare);
//        ctr1.inc(three);
//        ctr1.dec(one);
//
//        BigInteger vCtr1 = ctr1.get();
//        assertEquals(2, p.Decryption(vCtr1).intValue());
//
//    }
//
//    @Test
//    public void testMerge(){
//        BigInteger one = p.Encryption(this.convert(1));
//        BigInteger two = p.Encryption(this.convert(2));
//        SecureCounter ctr1 = new SecureCounter(1,nsquare);
//        SecureCounter ctr2 = new SecureCounter(2,nsquare);
//
//        ctr1.inc(two);
//        ctr1.dec(two);
//
//        ctr2.inc(one);
//
//        ctr1.merge(ctr2.getState());
//        BigInteger vCtr = ctr1.get();
//        assertEquals(1, p.Decryption(vCtr).intValue());
//    }
//
//    private BigInteger convert(int x){
//        return BigInteger.valueOf(x);
//    }
//}
