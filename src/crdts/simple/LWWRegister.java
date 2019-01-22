package crdts.simple;

import crdts.helpers.EnhancedValue;

public class LWWRegister<T> {

    EnhancedValue<T> state;

    public LWWRegister(T value, long ts) {
        this.state = new EnhancedValue<>(value,ts);
    }

    public T get() {
        return state.getValue();
    }

    public void put(T value, long ts) {
        if (this.applicable(ts))
            this.state = new EnhancedValue<>(value,ts);
    }

    public EnhancedValue<T> getState(){
        return this.state;
    }

    public void merge(EnhancedValue<T> state) {
        if (this.applicable(state.getTimestamp())) {
            this.state = state;
        }
    }

    private boolean applicable(long ts) {
        if ( Long.compare(this.state.getTimestamp(), ts) <= 0) {
            return true;
        } else return false;
    }

    @Override
    public int hashCode() {
        return this.state.hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        LWWRegister<?> rg = (LWWRegister<?>) obj;
        return this.state.equals(rg.state);
    }
}
