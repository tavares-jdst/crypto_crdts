package tests.crdts.simple;

import crdts.simple.AbstractList;
import crdts.simple.OURList;
import org.junit.Test;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class OURListTest {
    @Test
    public void testLookup() {
        OURList<Integer> ints = new OURList<>();
        ints.insert(0, 0, 1);
        ints.insert(1, 1, 2);
        ints.insert(2, 2, 3);
        ints.insert(3, 3, 3);
        ints.insert(4, 4, 10);

        //Test 1
        assertTrue(ints.contains(0));
        assertTrue(ints.contains(1));
        assertTrue(ints.contains(2));
        assertTrue(ints.contains(3));
        assertTrue(ints.contains(4));
        assertTrue(ints.size() == 5);


        ints.remove(10);
        ints.remove(2, 4);

        //Test 2
        assertTrue(ints.contains(0));
        assertTrue(ints.contains(1));
        assertFalse(ints.contains(2));
        assertTrue(ints.contains(3));
        assertTrue(ints.contains(4));
        assertTrue(ints.size() == 4);
    }

    @Test
    public void testMerge() {

        OURList<Integer> ints = new OURList<>();
        ints.insert(0, 0, 1);
        ints.insert(1, 1, 2);
        ints.insert(2, 2, 3);
        ints.remove(11);

        OURList<Integer> intsC = new OURList<>();
        intsC.insert(5,5);
        intsC.insert(8,10);
        intsC.insert(10,9);

        ints.merge(intsC.getState());
        assertTrue(ints.contains(0));
        assertTrue(ints.contains(1));
        assertFalse(ints.contains(2));
        assertTrue(ints.contains(5));
        assertTrue(ints.contains(8));
        assertTrue(ints.contains(10));
        assertTrue(ints.size()==5);
    }
}
