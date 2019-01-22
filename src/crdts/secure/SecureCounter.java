package crdts.secure;

import helpers.CounterState;

import java.math.BigInteger;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;


public class SecureCounter {

    Integer my_id;
    Map<Integer, BigInteger> positives, negatives; //estruturas principais
    Map<Integer, Long> aux_positives, aux_negatives; //estruturas auxiliares

    private int inc = Math.abs(new Random().nextInt());
    private BigInteger nsquare; //numero publico de paillier

    public SecureCounter(int id, BigInteger nsquare) {
        this.my_id = id;
        this.nsquare = nsquare;

        this.positives = new HashMap<>();
        this.aux_positives = new HashMap<>();

        this.aux_positives.put(this.my_id, 0L);

        this.negatives = new HashMap<>();
        this.aux_negatives = new HashMap<>();
        this.aux_negatives.put(this.my_id, 0L);

    }

    public void inc(BigInteger value) {
        BigInteger previous = this.positives.get(this.my_id);
        BigInteger following;
        if (previous != null) {
            following = this.sum(previous, value);
        } else following = value;

        this.positives.put(this.my_id, following);

        long ctr = this.aux_positives.get(this.my_id);
        this.aux_positives.put(this.my_id, ctr += this.inc);
    }

    public void dec(BigInteger value) {
        BigInteger previous = this.negatives.get(this.my_id);
        BigInteger following;

        if (previous != null) {
            following = this.sum(previous, value);
        } else following = value;

        this.negatives.put(this.my_id, following);

        long ctr = this.aux_negatives.get(this.my_id);
        this.aux_negatives.put(this.my_id, ctr += this.inc);
    }

    public BigInteger get() {

        BigInteger p = this.positives.values().stream().reduce(BigInteger.ONE, (a, b) -> this.sum(a, b));
        BigInteger sumN = this.negatives.values().stream().reduce(BigInteger.ONE, (a, b) -> this.sum(a, b));

        /**
         * It is only possible to calculate the multiplicative inverse modulo n^2 of sumN in certain conditions:
         *
         * - https://crypto.stackexchange.com/questions/2076/division-in-paillier-cryptosystem
         * - https://crypto.stackexchange.com/questions/19457/how-can-i-do-minus-on-plaintexts-in-the-paillier-cryptosystem
         *
         * In other words, the counter can't be negative.
         */

        BigInteger n = sumN.modPow(BigInteger.valueOf(-1), this.nsquare);

        return this.sum(p, n);
    }

    public CounterState<Integer, BigInteger, Integer, Long> getState() {
        return new CounterState<>(positives, negatives, aux_positives, aux_negatives);
    }

    private BigInteger sum(BigInteger x, BigInteger y) {
        return x.multiply(y).mod(this.nsquare);
        //return this.paillier.eAdd(x, y);
    }

   //TODO giving errors
   public void merge (CounterState<Integer, BigInteger, Integer, Long> s){
       //seleccionar as entradas que irao permancecer com base nos contadores auxiliares
       s.getAuxPositives().forEach(
               (k, v) -> {
                   long myV;
                   Map<Integer, BigInteger> pos = s.getPositives();
                   if (this.aux_positives.containsKey(k)) {
                       myV = this.aux_positives.get(k);
                       int cmp = Long.compare(myV, v);

                       if (cmp < 0) { //so actualizo se o valor do outro contador for maior que o meu
                           this.aux_positives.put(k, v);
                           this.positives.put(k, pos.get(k));
                       }

                   } else {
                       this.aux_positives.put(k, v);
                       this.positives.put(k, pos.get(k));
                   }
               }
       );

       s.getAuxNegatives().forEach(
               (k, v) -> {
                   long myV;
                   Map<Integer, BigInteger> negs = s.getNegatives();
                   if (this.aux_negatives.containsKey(k)) {
                       myV = this.aux_negatives.get(k);
                       int cmp = Long.compare(myV, v);

                       if (cmp < 0) { //so actualizo se o valor do outro contador for maior que o meu
                           this.aux_negatives.put(k, v);
                           this.negatives.put(k, negs.get(k));
                       }

                   } else {
                       this.aux_negatives.put(k, v);
                       this.negatives.put(k, negs.get(k));
                   }
               }
       );

   }
}
