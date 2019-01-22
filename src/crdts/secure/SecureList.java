package crdts.secure;

import crdts.simple.AbstractList;
import helpers.ComparableByteArray;

import java.util.List;

/**
 * Abstract class for a secure list where the elements are first sorted by the position
 * they were inserted in and then by an disambiguator in case if more than one element has been
 * inserted in the same position.
 * <p>
 * The elements stored under this structure must store elements encrypted under deterministic encryption
 * to allow for equality operations to be performed correctly.
 */
public abstract class SecureList {
    AbstractList<ComparableByteArray> state; //internal state representation

    /**
     * Inserts an element at the tail of the list applying the aplicable concurrency policy when needed.
     *
     * @param elem -  the element to be inserted.
     * @param ts   - the timestamp associated with this operation.
     */
    public void insert(ComparableByteArray elem, long ts) {
        this.state.insert(elem, ts);
    }

    /**
     * Inserts an element at the specified position in the list applying the aplicable concurrency policy when needed.
     *
     * @param pos  - the position to insert the element.
     * @param elem -  the element to be inserted.
     * @param ts   - the timestamp associated with this operation.
     */
    public void insert(int pos, ComparableByteArray elem, long ts) {
        this.state.insert(pos, elem, ts);
    }

    /**
     * Removes the element stored in the tail of the list applying the aplicable concurrency policy when needed.
     *
     * @param ts - the timestamp associated with this operation.
     */
    public void remove(long ts) {
        this.state.remove(ts);
    }

    /**
     * Removes the element stored at the specified position in the list, applying the aplicable concurrency policy when needed.
     *
     * @param pos - the position of the element to be removed.
     * @param ts  - the timestamp associated with this operation.
     */
    public void remove(int pos, long ts) {
        this.state.remove(pos, ts);
    }

    /**
     * Retrieves the element stored at the specified position.
     *
     * @param pos - the position of the element.
     * @return the element stored at the specified position.
     */
    public ComparableByteArray get(int pos) {
        return this.state.get(pos);
    }

    /**
     * Evaluates if an element is present within the collection.
     *
     * @param elem - the element to be found.
     * @return true upon the first match of the element, false if no match is found.
     */
    public boolean contains(ComparableByteArray elem) {
        return this.state.contains(elem);
    }

    /**
     * Returns the size of the structure.
     *
     * @return he size of the structure.
     */
    public int size() {
        return this.state.size();
    }

    /**
     * Retrieves all of the elements present in the structure.
     *
     * @return the elements of the collection.
     */
    public List<ComparableByteArray> getAll() {
        return this.state.getAll();
    }

    /**
     * Retrieves the internal state of this datastructure.
     *
     * @return - internal state.
     */
    public AbstractList<ComparableByteArray> getState() {
        return this.state;
    }

    /**
     * Merges this structure with the state of another structure of the same type applying the aplicable concurrency policy.
     *
     * @param state - the internal state of the set to be merged with.
     */
    public void merge(AbstractList<ComparableByteArray> state) {
        this.state.merge(state.getState());

    }

}
