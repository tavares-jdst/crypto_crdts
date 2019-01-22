package crdts.simple;

import crdts.helpers.Operation;

import java.util.HashMap;
import java.util.Map;

public class OURMap<K, V> {

    Map<K, Operation<V>> state;

    public OURMap() {
        this.state = new HashMap<>();
    }

    /**
     * Inserts a pair (key, value) if there isnt already a value associated with the key,
     * otherwise it updates the value for the specified key applying the Add Wins policy when needed.
     * @param k - the key.
     * @param v - the value.
     * @param ts - the timestamp associated with this operation.
     */
    public void put(K k, V v, long ts) {

        Operation<V> oOp = this.state.get(k);

        if (oOp == null || (oOp != null && ts >= oOp.getTimestamp())) {
            this.state.put(k, new Operation(v, ts, Operation.PUT));
        }

    }

    /**
     * Removes the value associated with the specified key, if there is any while applying
     * the Add Wins policy when needed.
     *
     * @param k - the key that identifies the value to be removed
     * @param ts -  the timestamp associated with this operation.
     */
    public void remove(K k, long ts) {
        Operation<V> oOp = this.state.get(k);

        if (oOp != null && (oOp.getTimestamp() < ts || (oOp.getTimestamp() == ts && oOp.getType() != Operation.PUT))) { //TODO allows to delete with inferior ts
            this.state.put(k, new Operation(null, ts, Operation.REMOVE));
        }

    }

    /**
     * Retrieves the value associated with the specified key.
     * @param k - the key.
     * @return the value associated with the key or null if there is none.
     */
    public V get(K k) {
        Operation<V> e = this.state.get(k);
        if(e!=null && !e.isDeleted()){
            return e.getValue();
        }

        return null;
    }

    /**
     * Retrieves all of the associations currently present in the structure.
     *
     * @return the associations of the collection.
     */
    public Map<K, V> getAll() {

        Map<K, V> res = new HashMap<>();
        this.state.entrySet().stream().filter(e -> !e.getValue().isDeleted()).forEach(e -> res.put(e.getKey(), e.getValue().getValue()));

        return res;
    }

    /**
     * Returns the size of the structure.
     *
     * @return he size of the structure.
     */
    public int size(){
        return this.getAll().size();
    }

    /**
     * Checks wether there is an association for the specified key.
     * @param k - the key.
     * @return true if there is an association, false otherwise.
     */
    public boolean contains( K k){
        Operation<V> e = this.state.get(k);
        if(e!=null && !e.isDeleted()){
            return true;
        }

        return false;
    }

    /**
     * Retrieves the internal state of this datastructure.
     *
     * @return - internal state.
     */
    public Map<K, Operation<V>> getState(){
        return this.state;
    }

    /**
     * Merges this structure with the state of another structure of the same type applying the aplicable concurrency policy.
     *
     * @param state - the internal state of the set to be merged with.
     */
    public void merge(Map<K, Operation<V>> state) {


        state.entrySet().stream().forEach(e -> {

            Operation<V> op = e.getValue();
            switch (op.getType()) {
                case Operation.PUT:
                    this.put(e.getKey(), op.getValue(), op.getTimestamp());
                    break;

                case Operation.REMOVE:
                    this.remove(e.getKey(), op.getTimestamp());
                    break;
            }
        });
    }
}
