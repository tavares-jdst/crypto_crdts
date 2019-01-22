package crdts.simple;

import helpers.CounterState;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

public class Counter {

    Map<Integer, Integer> positives, negatives; //estruturas principais
    Map<Integer, Long> aux_positives, aux_negatives; //estruturas auxiliares
    Integer my_id;

    //private int inc = Math.abs(new Random().nextInt()); //incremento dos contadores auxiliares

    public Counter(Integer id) {
        this.my_id = id;

        this.negatives = new HashMap<>();
        this.aux_negatives = new HashMap<>();

        this.positives = new HashMap<>();
        this.aux_positives = new HashMap<>();

        this.negatives.put(this.my_id, 0);
        this.aux_negatives.put(this.my_id, 0L);

        this.positives.put(this.my_id, 0);
        this.aux_positives.put(this.my_id, 0L);
    }

    public void inc(int value) {
        if (value < 0)
            return;

        int v = this.positives.get(this.my_id);
        this.positives.put(this.my_id, v += value);

        this.aux_positives.put(this.my_id, System.nanoTime());
    }

    public void dec(int value) {

        if (value < 0)
            return;

        int v = this.negatives.get(this.my_id);
        this.negatives.put(this.my_id, v += value);

        this.aux_negatives.put(this.my_id, System.nanoTime());
    }

    public int get() {
        int p = this.positives.values().stream().reduce(0, (a, b) -> a + b);
        int n = this.negatives.values().stream().reduce(0, (a, b) -> a + b);
        return p - n;
    }

    public CounterState<Integer, Integer, Integer, Long> getState() {
        return new CounterState<>(positives, negatives, aux_positives, aux_negatives);
    }

    public void merge(CounterState<Integer, Integer, Integer, Long> s) {

        //seleccionar as entradas que irao permancecer com base nos contadores auxiliares
        s.getAuxPositives().forEach(
                (k, v) -> {
                    long myV;
                    Map<Integer, Integer> pos = s.getPositives();
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
                    Map<Integer, Integer> negs = s.getNegatives();
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

