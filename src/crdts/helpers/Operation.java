package crdts.helpers;

/**
 * Stores information about the last operation made to a value of type T and the instance it occurred.
 * @param <T>
 */
public class Operation<T> {

    EnhancedValue<T> value;
    int type;

    //Operations allowed
    public final static int PUT = 1;
    public final static int REMOVE = -1;

    public Operation(T value, Long timestamp, int type) {
        this.value = new EnhancedValue<>(value, timestamp);
        this.type = type;
    }

    /**
     *
     * @return
     */
    public int getType() {
        return type;
    }

    /**
     * Retrieves the value stored.
     * @return stored value.
     */
    public T getValue() {
        return value.getValue();
    }

    /**
     * Restrives the timestamp associated with the last operation performed to the value.
     * @return the timestamp.
     */
    public Long getTimestamp() {
        return value.getTimestamp();
    }

    /**
     * Modifies the type of the last operation performed to te value.
     * @param type - the new operation.
     */
    public void setType(int type) {
        this.type = type;
    }

    /**
     * Modifies the value stored.
     * @param value - the new value to be stored.
     */
    public void setValue(T value) {
        this.value.setValue(value);
    }


    /**
     * Sets the new value for the timestamp.
     * @param timestamp - the new timestamp.
     */
    public void setTimestamp(long timestamp) {
        value.setTimestamp(timestamp);
    }

    /**
     * Indicates whether if the last operation performed over the value was a remove.
     * @return true if the last operation was a remove, false otherwise.
     */
    public boolean isDeleted(){
        return this.getType()==REMOVE;
    }
}
