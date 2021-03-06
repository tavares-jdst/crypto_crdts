/*****************************************************************************
 * Copyright 2011-2012 INRIA
 * Copyright 2011-2012 Universidade Nova de Lisboa
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *****************************************************************************/

package crdts.helpers;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

public class SID implements Comparable<SID> {

    static final int TOP = 1 << 30;

    static final int INCREMENT = 1 << 9;
    static final Random rg = new Random(1L);
    public static SID FIRST = new SID(new int[]{increment(0), SiteID.get()});

    int[] coords;

    protected SID(int[] coords) {
        this.coords = coords;
    }

    public SID between(SID other) {
        return new SID(between(this, other));
    }

    // TODO deal with underoverflow of first coordinate...
    static public SID smallerThan(SID x) {
        return new SID(new int[]{decrement(x.coord(0)), SiteID.get()});
    }

    // TODO deal with overflow of first coordinate...
    static public SID greaterThan(SID x) {
        return new SID(new int[]{increment(x.coord(0)), SiteID.get()});
    }

    @Override
    public int compareTo(SID other) {
        int dims = Math.max(dims(), other.dims());
        for (int i = 0; i < dims; i++) {
            int signum = coord(i) - other.coord(i);
            if (signum != 0)
                return signum;
        }
        return 0;
    }

    final int coord(int d) {
        return d < coords.length ? coords[d] : 0;
    }

    static private int[] between(SID lo, SID hi) {

        int dims = Math.max(lo.dims(), hi.dims());

        int[] inc = Arrays.copyOf(lo.coords, dims);
        int[] res = Arrays.copyOf(lo.coords, dims);

        for (int i = 0; i < dims - 1; i += 2) {
            inc[i] = Math.min(2 * INCREMENT, ((hi.coord(i) - lo.coord(i)) + TOP) % TOP);
            res[i] = (lo.coord(i) + (inc[i] >> 1));
            if (res[i] > TOP) {
                res[i] %= TOP;
                res[i - 2] += 1;
            }
        }

        for (int i = 0; i < dims - 1; i += 2) {
            if (inc[i] > 1) {
                res[i + 1] = SiteID.get();
                res = Arrays.copyOf(res, i + 2);
                return res;
            }
        }

        // we got a collision, increase #dims by 2.
        res = Arrays.copyOf(res, dims + 2);
        res[dims] = INCREMENT;
        res[dims + 1] = SiteID.get();

        return res;
    }

    static List<Integer> INT(int[] a) {
        Integer[] res = new Integer[a.length / 2];
        int j = 0;
        for (int i = 0; i < a.length; i++) {
            if ((i & 1) == 0)
                res[j++] = a[i];
        }

        return Arrays.asList(res);
    }

    public int hashCode() {
        int res = 0;

        for (int i : coords)
            res ^= i;

        return res;
    }

    public boolean equals(Object other) {
        return (other instanceof SID) && compareTo((SID) other) == 0;
    }

    public String toString() {
        List<Integer> tmp = new ArrayList<>();
        int j = 0;
        for (int i : coords)
            if ((j++ & 1) == 0)
                tmp.add(i);

        return String.format("%s", tmp);
    }

    static private int increment(int base) {
        final int jitter = (INCREMENT >> 2) + rg.nextInt(INCREMENT >> 1);
        return base + jitter;
    }

    static private int decrement(int base) {
        final int jitter = (INCREMENT >> 2) + rg.nextInt(INCREMENT >> 1);
        return base - jitter;
    }

    final private int dims() {
        return coords.length;
    }

}
