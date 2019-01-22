package tests.crdts.simple;

import crdts.simple.Counter;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class CounterTests {

    @Test
    public void testLookup() {
        Counter ctr = new Counter(1);
        ctr.inc(2);
        ctr.inc(3);
        ctr.inc(2);

        ctr.dec(3);
        ctr.dec(3);

        assertEquals(1, ctr.get());
    }

    @Test
    public void testMerge() {
        Counter ctr = new Counter(1), ctr1 = new Counter(2);

        ctr.inc(2);
        ctr.inc(3);
        ctr.inc(2);

        ctr.dec(3);
        ctr.dec(3);

        ctr1.inc(1);
        ctr1.dec(10);

        ctr.merge(ctr1.getState());

        assertEquals(-8, ctr.get());

    }
}
