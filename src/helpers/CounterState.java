package helpers;

import java.util.Map;

/**
 * Represents the inner state of the counter crdt. Each counter has 4 maps as their inner state:
 *  - 2 represent the positive and negative values of the local values of the counter for each replica (P and N)
 *  - 2 store meta data that allow, for each of the previously mentioned maps, to determine which entry is
 *  bigger than the other (auxP and auxN)
 * @param <T> Type of the key for maps P and N
 * @param <S> Type of the value for maps P and N
 * @param <U> Type of the key for maps auxP and auxN
 * @param <V> Type of the value for maps auxP and auxN
 */
public class CounterState<T,S,U,V> {
    Map<T,S> p, n;
    Map<U,V> auxP, auxN;

    public CounterState(Map<T, S> p, Map<T, S> n, Map<U, V> auxP, Map<U, V> auxN) {
        this.p = p;
        this.n = n;
        this.auxP = auxP;
        this.auxN = auxN;
    }

    /**
     * Retrieves the map that contains the the sum of values added to the counter for a replica.
     * @return map that contains the the sum of values added to the counter for a replica.
     */
    public Map<T, S> getPositives() {
        return p;
    }

    /**
     * Retrieves the map that contains the the sum of values subtracted to the counter for a replica.
     * @return map that contains the the sum of values subtracted to the counter for a replica.
     */
    public Map<T, S> getNegatives() {
        return n;
    }

    /**
     * Retrieves the map that contains metadata regarding the values added to the counter (enables merge).
     * @return map that contains metadata regarding the values added to the counter.
     */
    public Map<U, V> getAuxPositives() {
        return auxP;
    }

    /**
     * Retrieves the map that contains metadata regarding the values subtracted to the counter (enables merge).
     * @return map that contains metadata regarding the values subtracted to the counter.
     */
    public Map<U, V> getAuxNegatives() {
        return auxN;
    }
}
