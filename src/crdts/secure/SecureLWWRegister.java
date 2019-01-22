package crdts.secure;


import helpers.ComparableByteArray;
import crdts.simple.LWWRegister;

public class SecureLWWRegister {

    LWWRegister<ComparableByteArray> rg;

    public SecureLWWRegister(ComparableByteArray value, long ts) {

        this.rg = new LWWRegister<>(value, ts);
    }

    public ComparableByteArray get() {
        return this.rg.get();

    }

    public LWWRegister<ComparableByteArray> getState() {
        return this.rg;
    }

    public void put(ComparableByteArray value, long ts) {
        this.rg.put(value, ts);
    }

    public void merge( LWWRegister<ComparableByteArray> state) {
       this.rg.merge(state.getState());
    }
}
