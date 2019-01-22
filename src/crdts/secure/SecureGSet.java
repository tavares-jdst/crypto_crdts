package crdts.secure;

import crdts.simple.GSet;
import helpers.ComparableByteArray;

import java.util.Set;

/**
 * A secure grow only set that must store elements encrypted under deterministic encryption
 * to allow for equality operations to be performed correctly.
 */
public class SecureGSet {

    GSet<ComparableByteArray> set;


    public SecureGSet() {
        this.set = new GSet<>();
    }

    /**
     * Adds an element to the set.
     *
     * @param elem the element to be added to the set.
     */
    public void add(ComparableByteArray elem) {
        this.set.add(elem);
    }

    /**
     * Retrieves all of the elements present in the structure.
     *
     * @return the elements of the collection.
     */
    public Set<ComparableByteArray> getAll() {
        return this.set.lookup();
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
     * Merges this structure with the state of another structure of the same type.
     * In this set, the merge behaves like set union.
     *
     * @param state - the internal state of the set to be merged with.
     */
    public void merge(GSet<ComparableByteArray> state) {
        this.set.merge(state.getState());
    }

    /**
     * Retrieves the internal state of this data structure.
     *
     * @return - internal state.
     */
    public GSet<ComparableByteArray> getState() {
        return this.set;
    }

    @Override
    public boolean equals(Object o) {

        if (this == o) return true;

        if (o == null || getClass() != o.getClass()) return false;

        SecureGSet other = (SecureGSet) o;

        return this.set.equals(other);
    }

    @Override
    public int hashCode() {
        return this.set.hashCode();
    }
}
