package crdts.simple;

import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

public class LWWMap <K,V> {
    Map<K, LWWRegister<V>> state;

    public LWWMap(){
        this.state=new HashMap<>();
    }

    /**
     * Inserts a pair (key, value) if there isnt already a value associated with the key,
     * otherwise it updates the value for the specified key applying the Last Writer Wins policy when needed.
     * @param k - the key.
     * @param v - the value.
     * @param ts - the timestamp associated with this operation.
     */
    public void put(K k, V v, long ts){
        LWWRegister<V> entry = this.state.get(k);
        LWWRegister<V> nEntry =  new LWWRegister<>(v,ts);

        if (entry == null) {
            this.state.put(k,nEntry );
        } else{
            entry.merge(nEntry.getState());
            this.state.put(k, entry ) ;
        }

    }

    /**
     * Removes the value associated with the specified key, if there is any while applying
     * the Last Writer Wins policy when needed.
     *
     * @param k - the key that identifies the value to be removed
     * @param ts -  the timestamp associated with this operation.
     */
    public void remove(K k, long ts){
        LWWRegister<V> entry = this.state.get(k);

        if (entry != null) {
           entry.put(null, ts);
        }

    }

    /**
     * Retrieves the value associated with the specified key.
     * @param k - the key.
     * @return the value associated with the key or null if there is none.
     */
    public V get(K k){
        LWWRegister<V> entry = this.state.get(k);

        if(entry!=null)
            return entry.get();

        return null;
    }

    /**
     * Checks wether there is an association for the specified key.
     * @param k - the key.
     * @return true if there is an association, false otherwise.
     */
    public boolean contains (K k){
        if(this.get(k)!=null)
            return true;

        return false;
    }

    /**
     * Returns the size of the structure.
     *
     * @return he size of the structure.
     */
    public int size(){
        return this.filtered().size();

    }

    /**
     * Retrieves all of the associations currently present in the structure.
     *
     * @return the associations of the collection.
     */
    public Map<K,V> getAll(){
        return this.filtered();
    }

    /**
     * Retrieves all of the associations in this map's state for which the value is not removed (i.e. null).
     * @return associatios present in the map.
     */
    private Map<K,V> filtered (){
        return this.state.entrySet().stream()
                                    .filter( e -> e.getValue().get()!=null)
                                    .collect(Collectors.toMap(
                                            e -> e.getKey(),
                                            e -> e.getValue().get()
                                    ));
    }

    /**
     * Retrieves the internal state of this datastructure.
     *
     * @return - internal state.
     */
    public Map<K, LWWRegister<V>> getState(){
        return this.state;
    }

    /**
     * Merges this structure with the state of another structure of the same type applying the aplicable concurrency policy.
     *
     * @param state - the internal state of the set to be merged with.
     */
    public void merge(Map<K, LWWRegister<V>> state){
        state.forEach((k,v) ->{
            LWWRegister<V> tmp = this.state.get(k);
            if(tmp==null){
                this.state.put(k,v);
            }else tmp.merge(v.getState());
        });
    }


}
