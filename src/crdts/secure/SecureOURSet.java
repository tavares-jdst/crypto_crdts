package crdts.secure;

import crdts.simple.OURSet;
import helpers.ComparableByteArray;

import java.util.Set;

/**
 * A secure set with Adds Wins concurrency policy. Elements must be stored encrypted under deterministic encryption
 * to allow for equality operations to be performed correctly.
 */
public class SecureOURSet {

    OURSet<ComparableByteArray> set; //the internal state


    public SecureOURSet() {
        this.set = new OURSet<>();
    }

    /**
     * Adds an element to the set, applying the Add Wins concurrency policy when needed.
     *
     * @param elem - the element to be added to the set.
     * @param ts   - the timestamp associated with this operation.
     */
    public void add(long ts, ComparableByteArray elem) {
        this.set.add(ts, elem);

    }

    /**
     * Removes an element from the set if it is present, applying the Last Writer Wins concurrency policy when needed.
     *
     * @param elem - the element to be removed from the set.
     * @param ts   - the timestamp associated with this operation.
     */
    public void remove(long ts, ComparableByteArray elem) {
        this.set.remove(ts, elem);
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
     * Retrieves the internal state of this data structure.
     *
     * @return - internal state.
     */
    public OURSet<ComparableByteArray> getState() {
        return this.set;
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
     * Retrieves all of the elements currently present in the structure.
     *
     * @return the elements of the collection.
     */
    public Set<ComparableByteArray> getAll() {
        return this.set.lookup();
    }

    /**
     * Merges this structure with the state of another structure of the same type.
     * In this set, the merge behaves like set union.
     *
     * @param state - the internal state of the set to be merged with.
     */
    public void merge(OURSet<ComparableByteArray> state) {
        this.set.merge(state.getState());

    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;

        if (o == null || getClass() != o.getClass()) return false;

        SecureOURSet other = (SecureOURSet) o;

        return this.set.equals(other);
    }

    @Override
    public int hashCode() {
        return this.set.hashCode();
    }
}
