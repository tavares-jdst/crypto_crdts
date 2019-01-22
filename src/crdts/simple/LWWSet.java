/*
 * The MIT License (MIT)
 *
 * Copyright (c) 2016 Dmitry Ivanov
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

package crdts.simple;

import crdts.helpers.EnhancedValue;
import crdts.helpers.Operations;
import helpers.LWWSetState;

import java.util.Set;

/**
 * A set with Last Writer Wins concurrency policy.
 *
 * @param <E> - the type of the elements to be stored in the structure.
 */
public class LWWSet<E> {

    /**
     * Adds an element to the set, applying the Last Writer Wins concurrency policy when needed.
     *
     * @param elem - the element to be added to the set.
     * @param ts   - the timestamp associated with this operation.
     */
    private GSet<EnhancedValue<E>> addSet, removeSet; //the internal state
    private transient Set<E> elems; //aux structure

    public LWWSet() {
        this.elems = null;
        this.addSet = new GSet<>();
        this.removeSet = new GSet<>();
    }


    /**
     * Adds an element to the set, applying the Last Writer Wins concurrency policy when needed.
     *
     * @param elem - the element to be added to the set.
     * @param ts   - the timestamp associated with this operation.
     */
    public void add(E elem, long ts) {

        addSet.add(new EnhancedValue<>(elem, ts));
        this.getOrCompute().add(elem);
    }

    /**
     * Removes an element from the set if it is present.
     *
     * @param elem - the element to be removed from the set.
     * @param ts   - the timestamp associated with this operation.
     */
    public void remove(E elem, long ts) {
        this.elems = null;
        removeSet.add(new EnhancedValue<>(elem, ts));
    }

    /**
     * Retrieves the internal state of this data structure.
     *
     * @return - internal state.
     */
    public LWWSetState<E> getState() {
        return new LWWSetState<>(this.addSet, this.removeSet);
    }

    /**
     * Merges this structure with the state of another structure of the same type.
     * In this set, the merge behaves like set union.
     *
     * @param state - the internal state of the set to be merged with.
     */
    public void merge(LWWSetState<E> state) {
        this.addSet.merge(state.getAddSet().getState());
        this.removeSet.merge(state.getRemoveSet().getState());
    }

    /**
     * Evaluates if an element is present within the collection.
     *
     * @param elem - the element to be found.
     * @return true if the element exists, false otherwise
     */
    public boolean contains(E elem) {
        return this.getOrCompute().contains(elem);
    }

    /**
     * Returns the size of the structure.
     *
     * @return he size of the structure.
     */
    public int size() {
        return this.getOrCompute().size();
    }

    /**
     * Retrieves all of the elements currently present in the structure.
     *
     * @return the elements of the collection.
     */
    public Set<E> lookup() {
        return this.getOrCompute();
    }

    /**
     * Retrieves all of the elements currently present in the structure that are present in the
     * add set and not in the remove set.
     *
     * @return the elements of the collection.
     */
    private Set<E> getOrCompute() {

        if (this.elems == null) {
            this.elems = Operations.filteredAndMapped(addSet.lookup(), new Operations.Predicate<EnhancedValue<E>>() {
                @Override
                public boolean call(EnhancedValue<E> element) {
                    return nonRemoved(element);
                }
            }, new Operations.Mapper<EnhancedValue<E>, E>() {
                @Override
                public E call(EnhancedValue<E> element) {
                    return element.getValue();
                }
            });
        }

        return this.elems;
    }

    /**
     * Eveluates wheter a state element is within the removed set. If it is, then it checks if the queried element is
     * more recent then the element already existing in the removed set.
     *
     * @param addState - the element to be checked.
     * @return true if the state element is more recent and in the removed set or false otherwise.
     */
    private boolean nonRemoved(final EnhancedValue<E> addState) {
        Set<EnhancedValue<E>> removes = Operations.filtered(removeSet.lookup(),
                new Operations.Predicate<EnhancedValue<E>>() {
                    @Override
                    public boolean call(EnhancedValue<E> element) {
                        return element.getValue().equals(addState.getValue())
                                && element.getTimestamp() > addState.getTimestamp();
                    }
                });
        return removes.isEmpty();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        LWWSet<?> lwwSet = (LWWSet<?>) o;

        return addSet.equals(lwwSet.addSet) && removeSet.equals(lwwSet.removeSet);
    }

    @Override
    public int hashCode() {
        int result = addSet.hashCode();
        result = 31 * result + removeSet.hashCode();
        return result;
    }
}
