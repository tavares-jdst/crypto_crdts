package crdts.helpers;

/**
 * Unique identifier for the List implementation. Based on the TreeDoc strucuture.
 */
public class PosID implements Comparable<PosID>{

    SID id; //desambiguator
    int pos; //position

    public PosID(SID id, int pos) {
        this.id = id;
        this.pos = pos;
    }

    @Override
    public int compareTo(PosID o) {
        int r = this.pos-o.pos;
        return r!=0 ? r : id.compareTo(o.id);
    }

    /**
     * Returns the disambiguator associated with this identifier.
     * @return the disambiguator.
     */
    public SID getId() {
        return id;
    }

    /**
     * Returns the positon associated with this identifier.
     * @return the disambiguator.
     */
    public int getPos() {
        return pos;
    }
}
