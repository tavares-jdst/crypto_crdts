package crdts.secure;

import crdts.simple.LWWSet;
import helpers.ComparableByteArray;
import helpers.LWWSetState;

import java.util.Set;

/**
 * A secure set with Last Writer Wins concurrency policy. Elements must be stored encrypted under deterministic encryption
 * to allow for equality operations to be performed correctly.
 */
public class SecureLWWSet {

    LWWSet<ComparableByteArray> set; //the internal state

    public SecureLWWSet() {
        this.set = new LWWSet<>();
    }

    /**
     * Adds an element to the set, applying the Last Writer Wins concurrency policy when needed.
     *
     * @param elem - the element to be added to the set.
     * @param ts   - the timestamp associated with this operation.
     */
    public void add(ComparableByteArray elem, long ts) {
        this.set.add(elem, ts);
    }

    /**
     * Removes an element from the set if it is present, applying the Last Writer Wins concurrency policy when needed.
     *
     * @param elem - the element to be removed from the set.
     * @param ts   - the timestamp associated with this operation.
     */
    public void remove(ComparableByteArray elem, long ts) {
        this.set.remove(elem, ts);
    }

    /**
     * Evaluates if an element is present within the collection.
     *
     * @param elem - the element to be found.
     * @return true if the element exists, false otherwise
     */
    public boolean contains(ComparableByteArray elem) {
        return this.set.contains(elem);
    }

    /**
     * Returns the size of the structure.
     *
     * @return he size of the structure.
     */
    public int size() {
        return this.set.size();
    }

    /**
     * Retrieves the internal state of this data structure.
     *
     * @return - internal state.
     */
    public LWWSetState<ComparableByteArray> getState() {
        return this.set.getState();
    }

    /**
     * Merges this structure with the state of another structure of the same type.
     * In this set, the merge behaves like set union.
     *
     * @param state - the internal state of the set to be merged with.
     */
    public void merge(LWWSetState<ComparableByteArray> state) {
        this.set.merge(state);
    }

    /**
     * Retrieves all of the elements currently present in the structure.
     *
     * @return the elements of the collection.
     */
    public Set<ComparableByteArray> getAll() {
        return this.set.lookup();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;

        if (o == null || getClass() != o.getClass()) return false;

        SecureLWWSet other = (SecureLWWSet) o;

        return this.set.equals(other);
    }

    @Override
    public int hashCode() {
        return this.set.hashCode();
    }
}
