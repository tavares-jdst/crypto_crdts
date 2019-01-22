package tests.crdts.simple;

import crdts.simple.LWWSet;
import org.junit.Test;

import static org.junit.Assert.assertTrue;


public class LWWSetTests {

    @Test
    public void testLookup() {
        final LWWSet<String> lwwSet = new LWWSet<>();

        lwwSet.add("dog",1);
        lwwSet.add("cat",1);
        lwwSet.add("ape",1);
        lwwSet.add("tiger",1);

        lwwSet.remove("cat",2);
        lwwSet.remove("dog",2);

        // Actual test
        assertTrue(lwwSet.size() == 2);
        assertTrue(lwwSet.contains("ape"));
        assertTrue(lwwSet.contains("tiger"));
    }

    @Test
    public void testMerge() {
        final LWWSet<String> firstLwwSet = new LWWSet<>();
        firstLwwSet.add("ape",3);
        firstLwwSet.add("dog",1);
        firstLwwSet.add("cat",1);
        firstLwwSet.remove("cat",2);

        final LWWSet<String> secondLwwSet = new LWWSet<>();
        secondLwwSet.add("ape",1);
        secondLwwSet.add("tiger",1);
        secondLwwSet.add("cat",1);
        secondLwwSet.remove("ape",2);

        // Actual test
        firstLwwSet.merge(secondLwwSet.getState());
        assertTrue(firstLwwSet.contains("ape"));
        assertTrue(firstLwwSet.contains("dog"));
        assertTrue(firstLwwSet.contains("tiger"));
        assertTrue(firstLwwSet.size()==3);

    }

}
