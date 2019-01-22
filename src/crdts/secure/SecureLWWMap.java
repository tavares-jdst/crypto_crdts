package crdts.secure;

import crdts.simple.LWWMap;
import helpers.ComparableByteArray;

import java.util.Map;

/**
 *
 * Secure map with Last Writter Wins concurrency policy. It's an composed structure in the sense that it has another CRDT
 * (LWW Register) underlying it. The keys must be stored under deterministic encryption to allow for equality operations
 * to be performed correctly. Values can be stored under conventional forms of encryption.
 *
 */
public class SecureLWWMap {

    LWWMap<ComparableByteArray, ComparableByteArray> map; //internal state representation

    public SecureLWWMap() { this.map = new LWWMap<>(); }

    /**
     * Inserts a pair (key, value) if there isnt already a value associated with the key,
     * otherwise it updates the value for the specified key applying the Last Writer Wins policy when needed.
     * @param k - the key.
     * @param v - the value.
     * @param ts - the timestamp associated with this operation.
     */
    public void put(ComparableByteArray k, ComparableByteArray v, long ts) { this.map.put(k, v, ts); }

    /**
     * Removes the value associated with the specified key, if there is any while applying
     * the Last Writer Wins policy when needed.
     *
     * @param k - the key that identifies the value to be removed
     * @param ts -  the timestamp associated with this operation.
     */
    public void remove(ComparableByteArray k, long ts) { this.map.remove(k, ts); }

    /**
     * Retrieves the value associated with the specified key.
     * @param k - the key.
     * @return the value associated with the key or null if there is none.
     */
    public ComparableByteArray get(ComparableByteArray k) { return this.map.get(k); }

    /**
     * Retrieves all of the associations present in the structure.
     *
     * @return the associations of the collection.
     */
    public Map<ComparableByteArray, ComparableByteArray> getAll() { return this.map.getAll(); }

    /**
     * Checks wether there is an association for the specified key.
     * @param k - the key.
     * @return true if there is an association, false otherwise.
     */
    public boolean contains(ComparableByteArray k) { return this.map.contains(k); }

    /**
     * Returns the size of the structure.
     *
     * @return he size of the structure.
     */
    public int size() { return this.map.size(); }

    /**
     * Retrieves the internal state of this datastructure.
     *
     * @return - internal state.
     */
    public LWWMap<ComparableByteArray, ComparableByteArray> getState() { return this.map; }

    /**
     * Merges this structure with the state of another structure of the same type applying the aplicable concurrency policy.
     *
     * @param state - the internal state of the set to be merged with.
     */
    public void merge(LWWMap<ComparableByteArray, ComparableByteArray> state) { this.map.merge(state.getState()); }

}
