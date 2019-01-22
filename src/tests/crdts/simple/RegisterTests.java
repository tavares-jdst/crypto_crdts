package tests.crdts.simple;

import crdts.simple.LWWRegister;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class RegisterTests {

    @Test
    public void testLookup() {
        LWWRegister<String> sgs = new LWWRegister<>("dog", 1);

        sgs.put("ape", 2);
        sgs.put("cat", 2);
        sgs.put("tiger", 2);

        assertTrue(sgs.get() == "tiger");
    }

    @Test
    public void merge() {
        LWWRegister<String> sgs = new LWWRegister<>("dog", 1);
        LWWRegister<String> sgs1 = new LWWRegister<>("zebra",8);

        sgs.put("tiger", 2);

        sgs1.put("dog",7);

        sgs.merge(sgs1.getState());
        assertEquals("zebra", sgs.get());
    }
}
