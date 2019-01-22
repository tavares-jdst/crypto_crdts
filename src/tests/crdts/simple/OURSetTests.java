package tests.crdts.simple;

import crdts.simple.OURSet;
import org.junit.Test;

import static org.junit.Assert.*;

public class OURSetTests {

    @Test
    public void testLookup() {
        OURSet<String> ourSet = new OURSet<>();

        ourSet.add( System.currentTimeMillis(), "ape");
        ourSet.add( System.currentTimeMillis(), "dog");
        ourSet.add( System.currentTimeMillis(), "cat");

        try {
            Thread.sleep(500);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        ourSet.add( System.currentTimeMillis(), "cat");

        long ts = System.currentTimeMillis();
        ourSet.remove(System.currentTimeMillis(), "tiger");
        ourSet.remove(ts + 1, "cat");

        // Actual test
        assertEquals(2,ourSet.size() );
        assertTrue(ourSet.contains("dog"));
        assertTrue(ourSet.contains("ape"));
        assertFalse(ourSet.contains("cat"));
    }

    @Test
    public void testMerge() {
        OURSet<String> firstOURSet = new OURSet<>();

        firstOURSet.add( System.currentTimeMillis(), "ape");
        firstOURSet.add( System.currentTimeMillis(), "dog");
        firstOURSet.add( System.currentTimeMillis(), "cat");

        try {
            Thread.sleep(500);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        OURSet<String> secondOURSet = new OURSet<>();
        secondOURSet.add( System.currentTimeMillis(), "cat");
        secondOURSet.add( System.currentTimeMillis(), "tiger");
        secondOURSet.remove( System.currentTimeMillis()+1, "cat");

        // Actual test
        firstOURSet.merge(secondOURSet.getState());

        firstOURSet.getState();

        assertEquals(3, firstOURSet.size());
        assertTrue(firstOURSet.contains("ape"));
        assertTrue(firstOURSet.contains("dog"));
        assertTrue(firstOURSet.contains("tiger"));
        assertFalse(firstOURSet.contains("cat"));
    }


}
