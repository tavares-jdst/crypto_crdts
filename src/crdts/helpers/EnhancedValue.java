package crdts.helpers;

/**
 * Class that simulates the pair (value, timestamp). The timestamp referst to the instance in time where the value was last modified.
 *
 * @param <T> - type of the value to be stored.
 */
public class EnhancedValue<T> {

    T value;
    long timestamp;

    public EnhancedValue(T value, long timestamp) {
        this.value = value;
        this.timestamp = timestamp;
    }

    /**
     * Retrieves the value stored.
     * @return stored value.
     */
    public T getValue() {
        return value;
    }

    /**
     * Modifies the value stored.
     * @param value - the new value to be stored.
     */
    public void setValue(T value) {
        this.value = value;
    }

    /**
     * Restrives the timestamp associated with the value.
     * @return the timestamp.
     */
    public long getTimestamp() {
        return timestamp;
    }

    /**
     * Sets the new value for the timestamp.
     * @param timestamp - the new timestamp.
     */
    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        EnhancedValue<?> that = (EnhancedValue<?>) o;

        return timestamp == that.timestamp && !(value != null ? !value.equals(that.value) : that.value != null);
    }

    @Override
    public int hashCode() {
        int result = (int) (timestamp ^ (timestamp >>> 32));
        result = 31 * result + (value != null ? value.hashCode() : 0);
        return result;
    }
}
