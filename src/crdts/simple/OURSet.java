package crdts.simple;

import crdts.helpers.Operation;

import java.util.*;

/**
 * A set with Add Wins concurrency policy.
 *
 * @param <E> - the type of the elements to be stored in the structure.
 */
public class OURSet<E extends Comparable<E>> {

    private Map<UUID, Operation<E>> elems;
    private Map<E, Set<UUID>> mapper;

    public OURSet() {

        elems = new HashMap<>();
        mapper = new HashMap<>();
    }

    /**
     * Adds an element to the set and automatically generates the ID to be associated with this element.
     * Applies the Add Wins concurrency policy when needed.
     *
     * @param elem - the element to be added to the set.
     * @param ts   - the timestamp associated with this operation.
     */
    public void add(long ts, E elem) {
        UUID id = UUID.randomUUID();
        this.add(id, ts, elem);

    }

    /**
     * Adds an element that is uniquely identified to the set, applying the Add Wins concurrency policy when needed.
     *
     * @param id - the identifier of the element.
     * @param timestamp - the timestamp associated with this operation.
     * @param element - the element to be added to the set.
     */
    private void add(UUID id, long timestamp, E element) {
        Set<UUID> idSet;

        if (!this.elems.containsKey(id)) { //id nunca antes visto
            this.elems.put(id, new Operation<>(element, timestamp, Operation.PUT));

            if (!this.mapper.containsKey(element)) {
                idSet = new HashSet<>();
                this.mapper.put(element, idSet);
            } else idSet = this.mapper.get(element);

            idSet.add(id);

        } else { //id já observado (pode ou nao estar associado a este elemento)
            Operation<E> op = this.elems.get(id);

            if (op.getValue().equals(element)) { //caso de estar associado ao mesmo elemento
                if (Long.compare(op.getTimestamp(), timestamp) <= 0) { //validar que podemos efectivamente adicionar
                    op.setTimestamp(timestamp);
                    op.setType(Operation.PUT);
                }

            }

        }
    }

    /**
     * Removes an element from the set if it is present, applying the Last Writer Wins concurrency policy when needed.
     *
     * @param elem - the element to be removed from the set.
     * @param ts   - the timestamp associated with this operation.
     */
    public void remove(long ts, E elem) {
        Set<UUID> idSet = this.mapper.get(elem);

        if (idSet == null)
            return;

        idSet.forEach(id -> {
            Operation<E> op = this.elems.get(id);
            //set as removed all of the ids for which the timestamp is bellow the timestamp received as a paramenter
            if (Long.compare(op.getTimestamp(), ts) < 0) {
                op.setType(Operation.REMOVE);
                op.setTimestamp(ts);
            }
        });
    }

    /**
     * Retrieves the internal state of this data structure.
     *
     * @return - internal state.
     */
    public Map<UUID, Operation<E>> getState() {
        return this.elems;
    }

    /**
     * Merges this structure with the state of another structure of the same type.
     * In this set, the merge behaves like set union.
     *
     * @param state - the internal state of the set to be merged with.
     */
    public void merge(Map<UUID, Operation<E>> state) {
        state.forEach((k, v) -> {
            if (v.getType() == Operation.PUT) {
                this.add(k, v.getTimestamp(), v.getValue());

            } else if (v.getType() == Operation.REMOVE) {
                this.remove(v.getTimestamp(), v.getValue());
            }
        });
    }

    /**
     * Evaluates if an element is present within the collection.
     *
     * @param elem - the element to be found.
     * @return true if the element exists, false otherwise
     */
    public boolean contains(E elem) {

        Set<UUID> idSet = this.mapper.get(elem);

        if (idSet == null)
            return false;

        for (UUID id : idSet) {
            Operation<E> op = this.elems.get(id);
            if (op != null && !op.isDeleted())
                return true;
        }

        return false;
    }

    /**
     * Returns the size of the structure.
     *
     * @return he size of the structure.
     */
    public int size() {
        return this.lookup().size();
    }

    /**
     * Retrieves all of the elements currently present in the structure.
     *
     * @return the elements of the collection.
     */
    public Set<E> lookup() {
        Set<E> res = new HashSet<>();
        this.elems.forEach((k, v) -> {
            if (!v.isDeleted() && !res.contains(v.getValue()))
                res.add(v.getValue());
        });
        return res;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        OURSet<?> ourSet = (OURSet<?>) o;

        return elems.equals(ourSet.elems);
    }

    @Override
    public int hashCode() {
        return elems.hashCode();
    }
}
