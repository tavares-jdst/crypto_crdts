package crdts.simple;

import crdts.helpers.Operation;
import crdts.helpers.PosID;
import crdts.helpers.SID;

import java.util.*;
import java.util.stream.Collectors;

/**
 * Abstract class for a secure list where the elements are first sorted by the position
 * they were inserted in and then by an disambiguator in case if more than one element has been
 * inserted in the same position.
 *
 * @param <T> - type of the element to be stored within the list.
 */
public abstract class AbstractList<T> {

    SortedMap<PosID, Operation<T>> state; //internal state representation
    transient SortedMap<PosID, T> atoms; //aux data structure, not serialized
    transient SortedSet<PosID> ids; //aux data structure, not serialized

    public AbstractList() {
        this.state = new TreeMap<>();
    }

    /**
     * Inserts an element at the tail of the list applying the aplicable concurrency policy when needed.
     *
     * @param elem -  the element to be inserted.
     * @param ts   - the timestamp associated with this operation.
     */
    public void insert(T elem, long ts) {
        this.insert(this.size(), elem, ts);
    }

    /**
     * Inserts an element at the specified position in the list applying the aplicable concurrency policy when needed.
     *
     * @param pos  - the position to insert the element.
     * @param elem -  the element to be inserted.
     * @param ts   - the timestamp associated with this operation.
     */
    public abstract void insert(int pos, T elem, long ts);

    /**
     * Removes the element stored in the tail of the list applying the aplicable concurrency policy when needed.
     *
     * @param ts - the timestamp associated with this operation.
     */
    public T remove(long ts) {
        return this.remove(this.size() - 1, ts);
    }

    /**
     * Removes the element stored at the specified position in the list, applying the aplicable concurrency policy when needed.
     *
     * @param pos - the position of the element to be removed.
     * @param ts  - the timestamp associated with this operation.
     */
    public abstract T remove(int pos, long ts);

    /**
     * Retrieves the element stored at the specified position.
     *
     * @param pos - the position of the element.
     * @return the element stored at the specified position.
     */
    public T get(int pos) {
        return this.getAtoms().get(this.getIdsArray().get(pos));
    }

    /**
     * Evaluates if an element is present within the collection.
     *
     * @param elem - the element to be found.
     * @return true upon the first match of the element, false if no match is found.
     */
    public boolean contains(T elem) {
        return this.getAtoms().containsValue(elem);
    }

    /**
     * Returns the size of the structure.
     *
     * @return he size of the structure.
     */
    public int size() {
        return this.getAtoms().size();
    }

    /**
     * Retrieves all of the elements present in the structure.
     *
     * @return the elements of the collection.
     */
    public List<T> getAll() {
        return this.getAtoms().values().stream().collect(Collectors.toList());
    }

    /**
     * Retrieves the internal state of this datastructure.
     *
     * @return - internal state.
     */
    public SortedMap<PosID, Operation<T>> getState() {
        return this.state;
    }

    /**
     * Merges this structure with the state of another structure of the same type applying the aplicable concurrency policy.
     *
     * @param state - the internal state of the set to be merged with.
     */
    public void merge(SortedMap<PosID, Operation<T>> state) {
        state.forEach((k, v) -> {
            int pos = k.getPos();

            switch (v.getType()) {
                case Operation.PUT:
                    this.insert(pos, v.getValue(), v.getTimestamp());
                    break;

                case Operation.REMOVE:
                    this.remove(pos, v.getTimestamp());
                    break;
            }
        });
    }

    /**
     * Creates a new unique identifier for the position pos having the disambiguator
     * obey the property d1 < d < d2, where d1 and d2 are the disambiguators of the
     * elements at pos-1 and pos of the list and d is the new disambiguator.
     *
     * @param pos - the position to be associated with the new identifier.
     * @return a new identifier for pos.
     */
    protected PosID createPos(int pos) {
        SID id = null;
        int size = size();

        List<PosID> ids = getIdsArray();

        if (pos == 0) { //first
            id = size == 0 ? SID.FIRST : SID.smallerThan(ids.get(pos).getId());
            return new PosID(id, pos);
        }

        if (pos == size) {
            id = SID.greaterThan(ids.get(pos - 1).getId());
            return new PosID(id, pos);
        }


        SID lowerBound = ids.get(pos - 1).getId();
        SID upperBound = ids.get(pos).getId();
        id = lowerBound.between(upperBound);
        return new PosID(id, pos);
    }

    /**
     * Retrieves all of the current position id's present within the structure as a list, excluding the id's that already have been eliminated .
     * @return current id's of the list.
     */
    protected List<PosID> getIdsArray() {
        List<PosID> res = this.getIds().stream().collect(Collectors.toList());
        return res;
    }

    /**
     * Retrieves all of the current pairs of (PosID, elem) present within the structure, excluding the pairs for which
     * the element has already been eliminated .
     * @return
     */
    protected SortedMap<PosID, T> getAtoms() {
        if (this.atoms == null) {
            this.linearize();
        }

        return this.atoms;
    }

    /**
     * Retrieves all of the current position id's present within the structure as a set, excluding the id's that already have been eliminated .
     * @return current id's of the list.
     */
    protected SortedSet<PosID> getIds() {
        if (this.ids == null) {
            this.linearize();
        }

        return this.ids;
    }

    /**
     * Given the whole internal state, this method builds the auxiliary structures that
     * help to execute operations of this structure by reducing the number of searches required to the
     * treemap that represents the internal state. These auxiliary structures do not
     * take into account the elements and positions that have already been removed.
     */
    protected void linearize() {

        this.atoms = new TreeMap<>();
        this.ids = new TreeSet<>();

        this.state.entrySet().stream().forEach(elem -> {
            Operation<T> op = elem.getValue();
            PosID id;
            if (!op.isDeleted()) {
                id = elem.getKey();
                //atoms
                this.atoms.put(id, op.getValue());
                this.ids.add(id);

            }
        });
    }

    /**
     * Invalidates the auxiliary data strucutres.
     */
    protected void delinearize() {
        this.atoms = null;
        this.ids = null;
    }
}
