package helpers;

import crdts.helpers.EnhancedValue;
import crdts.simple.GSet;

public class LWWSetState<T> {
    GSet<EnhancedValue<T>> addSet, removeSet;

    public LWWSetState(GSet<EnhancedValue<T>> addSet, GSet<EnhancedValue<T>> removeSet) {
        this.addSet = addSet;
        this.removeSet = removeSet;
    }

    public GSet<EnhancedValue<T>> getAddSet() {
        return addSet;
    }

    public GSet<EnhancedValue<T>> getRemoveSet() {
        return removeSet;
    }
}
