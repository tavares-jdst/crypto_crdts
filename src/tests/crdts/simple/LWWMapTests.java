package tests.crdts.simple;

import crdts.simple.LWWMap;
import org.junit.Test;

import static org.junit.Assert.*;

public class LWWMapTests {

    @Test
    public void testLookup() {
        LWWMap<String, String> map = new LWWMap<>();
        map.put("Zé", "CS", 0);
        map.put("Asdrúbal", "IT", 1);
        map.put("João", "Finance", 3);

        map.remove("João",3);
        map.remove("João", 1);
        map.remove("Asdrúbal",3);

        //Test
        assertTrue(map.contains("Zé"));
        assertFalse(map.contains("Asdrúbal"));
        assertFalse(map.contains("João"));

        assertNotNull(map.get("Zé"));
        assertNull(map.get("João"));
        assertNull(map.get("Asdrúbal"));

        assertTrue(map.get("Zé").equals("CS"));

        assertTrue(map.size()==1);
    }

    @Test
    public void testMerge(){
        LWWMap<String,String> map = new LWWMap<>();
        LWWMap<String,String> map1 = new LWWMap<>();

        map.put("Zé", "CS", 0);
        map.put("Asdrúbal", "IT", 1);
        map.put("João", "Finance", 3);
        map.remove("Asdrúbal",3);

        map1.put("Asdrúbal", "COD Player", 3);
        map1.put("Zé", "Calceteiro Maritimo", 3);
        map1.put("Kimbé","Fisherman", 5);

        map.merge(map1.getState());

        //Test
        assertTrue(map.contains("Asdrúbal"));
        assertTrue(map.contains("Zé"));
        assertTrue(map.contains("Kimbé"));
        assertTrue(map.contains("João"));

        assertNotNull(map.get("Zé"));
        assertNotNull(map.get("Asdrúbal"));
        assertNotNull(map.get("Kimbé"));
        assertNotNull(map.get("João"));

        assertEquals("COD Player", map.get("Asdrúbal"));
        assertEquals("Calceteiro Maritimo",map.get("Zé"));
        assertEquals("Fisherman",map.get("Kimbé"));
        assertEquals("Finance",map.get("João"));

        assertTrue(map.size()==4);
    }
}
