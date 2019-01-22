///*
// * The MIT License (MIT)
// *
// * Copyright (c) 2016 Dmitry Ivanov
// *
// * Permission is hereby granted, free of charge, to any person obtaining a copy
// * of this software and associated documentation files (the "Software"), to deal
// * in the Software without restriction, including without limitation the rights
// * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
// * copies of the Software, and to permit persons to whom the Software is
// * furnished to do so, subject to the following conditions:
// *
// * The above copyright notice and this permission notice shall be included in all
// * copies or substantial portions of the Software.
// *
// * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
// * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
// * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
// * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
// * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
// * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
// * SOFTWARE.
// */
//
//
//package tests.crdts.secure;
//
//import client.stubs.CryptoBean;
//import crdts.secure.SecureGSet;
//import crdts.secure.cryptoschemes.hj.mlib.HomoDet;
//import helpers.ComparableByteArray;
//import org.junit.BeforeClass;
//import org.junit.Test;
//
//import javax.crypto.SecretKey;
//import java.util.Set;
//
//import static org.junit.Assert.assertEquals;
//import static org.junit.Assert.assertTrue;
//
//public class SecureGSetTests {
//
//    static SecretKey key;
//
//    @BeforeClass
//    public static void setup() {
//        key = HomoDet.generateKey();
//    }
//
//    @Test
//    public void testLookup() {
//        SecureGSet gSet = new SecureGSet();
//
//        ComparableByteArray dog = CryptoBean.encDeterministic(key, "dog");
//        ComparableByteArray ape = CryptoBean.encDeterministic(key, "ape");
//        ComparableByteArray cat = CryptoBean.encDeterministic(key, "cat");
//
//        gSet.add(dog);
//        gSet.add(ape);
//        gSet.add(cat);
//
//        // Actual test
//        Set<ComparableByteArray> result = gSet.getAll();
//
//        assertTrue(result.size() == 3);
//        assertTrue(result.contains(dog));
//        assertTrue(result.contains(ape));
//        assertTrue(result.contains(cat));
//    }
//
//    @Test
//    public void testMerge() {
//        SecureGSet firstGSet = new SecureGSet();
//
//        ComparableByteArray dog = CryptoBean.encDeterministic(key, "dog");
//        ComparableByteArray ape = CryptoBean.encDeterministic(key, "ape");
//        ComparableByteArray cat = CryptoBean.encDeterministic(key, "cat");
//
//        firstGSet.add(dog);
//        firstGSet.add(ape);
//
//        SecureGSet secondGSet = new SecureGSet();
//        secondGSet.add(cat);
//        secondGSet.add(dog);
//
//        // Actual test
//       firstGSet.merge(secondGSet.getState());
//
//        assertTrue(firstGSet.size() == 3);
//        assertTrue(firstGSet.contains(dog));
//        assertTrue(firstGSet.contains(cat));
//        assertTrue(firstGSet.contains(ape));
//
//    }
//}
