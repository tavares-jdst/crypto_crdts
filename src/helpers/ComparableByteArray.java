package helpers;

import java.util.Arrays;

/**
 * Class that encapsulates an byte array. Allows for comparison of said array.
 */
public class ComparableByteArray implements Comparable<ComparableByteArray> {

    byte[] array;
    int hash;

    public ComparableByteArray(byte[] array){
        this.array = array;
        this.hash = Arrays.hashCode(this.array);
    }

    /**
     * Compares this internal byte array with another byte array through Array.equals().
     * @param o - object to be equalled to.
     * @return true if the other object is of the same class and if the internal array contents are the same as this one's.
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ComparableByteArray that = (ComparableByteArray) o;

        if(this.hash!= that.hashCode()){
            return false;
        }

        return Arrays.equals(array, that.array);
    }

    @Override
    public int hashCode() {
        return this.hash;
    }


    /**
     * Compares lexicographically this internal array with the array of the other object of the same class.
     * @param o - object to be compared to.
     * @return a negative value if this array is lexicographically smaller then o's array, 0 if they are equal
     * and a positive value if this array is lexicographically bigger then o's array.
     */
    @Override
    public int compareTo(ComparableByteArray o) {
        System.out.println("comparing");

        int m = Math.min(this.array.length, o.array.length);
        int r;
        for (int i = 0; i < m; i++) {
            r = Byte.compare(this.array[i], o.array[i]);
            if (r != 0)
                return r;
        }

        return this.array.length - o.array.length;
    }

    /**
     * Retrieves the encapsulated byte array.
     * @return the byte array.
     */
    public byte[] getArray() {
        return array;
    }
}
