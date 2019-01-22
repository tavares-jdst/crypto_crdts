package crdts.simple;

import crdts.helpers.Operation;
import crdts.helpers.PosID;

/**
 * List with the Add Wins concurrency policy.
 *
 * @param <T> - type of the element to be stored within the list.
 */
public class OURList<T> extends AbstractList<T> {


    public OURList() {
        super();
    }

    /**
     * Inserts an element at the specified position in the list, applying the Add Wins concurrency policy.
     *
     * @param pos  - the position to insert the element.
     * @param elem -  the element to be inserted.
     * @param ts   - the timestamp associated with this operation.
     */
    @Override
    public void insert(int pos, T elem, long ts) {
        PosID id = this.createPos(pos);
        Operation<T> op = this.state.get(id);

        if (op == null || (op != null && ts >= op.getTimestamp())) {
            this.getAtoms().put(id, elem);
            this.getIds().add(id);
            this.state.put(id, new Operation<>(elem, ts, Operation.PUT));
        }

    }

    /**
     * Removes the element stored at the specified position in the list, applying the Last Writer Wins concurrency policy.
     *
     * @param pos - the position of the element to be removed.
     * @param ts  - the timestamp associated with this operation.
     */
    @Override
    public T remove(int pos, long ts) {
        PosID id = getIdsArray().get(pos);
        Operation<T> op = this.state.get(id);

        if (op != null && (op.getTimestamp() < ts || (op.getTimestamp() == ts && op.getType() != Operation.PUT))) { //(op.ts < ts || (op.ts == ts && op.OpID != Operation.PUT))
            T v = this.getAtoms().get(id); //no need to remove as we are going to delinearize
            this.state.put(id, new Operation<>(null, ts, Operation.REMOVE));
            this.delinearize();

            return v;
        }

        return null;

    }

}
