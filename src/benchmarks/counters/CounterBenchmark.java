package benchmarks.counters;

import benchmarks.helpers.CounterParams;
import crdts.simple.Counter;

import java.util.ArrayList;
import java.util.List;

public class CounterBenchmark {
    public static final int INC = 1;
    public static final int DEC = 2;
    public static final int GET = 3;
    public static final int MERGE = 4;

    static int op;

    public CounterBenchmark(int opType) {
        op = opType;

    }

    public static void execute(CounterParams p) {
        if (op == MERGE)
            return;

        try {
            Counter ctr = new Counter(0);
            List<Integer> input = p.retrieveInput();



            if(op!= INC){
                makeOps(ctr,p.retrieveInput());
            }

            long start = System.nanoTime();
            for(int i = 0; i< input.size(); i++){
                switch (op) {
                    case INC: {
                        ctr.inc(input.get(i));
                    }
                    ;
                    break;
                    case DEC: {
                        ctr.dec(input.get(i));
                    }
                    ;
                    break;
                    case GET: {
                        ctr.get();
                    }
                    ;
                }

            }


            long finish = System.nanoTime();

            System.out.println(">>>>>>>>>>>>>>>>>> BENCHMARK STATS LWW");
            System.out.println(p.toString());
            System.out.println("Operation: " + op);
            float tots = (finish - start);
            //from nano to seconds is 10^3 but then we divide by 10^3 to get op/s so in fact we must divide it all by 10^6
            System.out.format("CrdtOp/microseconds: %f ", tots/1000000f);
            System.out.println();

        } catch (Exception e) {
            System.err.println("Unable to execute benchmark with params: \n" + p.toString());
            e.printStackTrace();

        }

    }

    public void executeMerge( int totalops, CounterParams[] common ) {

        try {

            //lets see if it looks good, should be false
            //System.out.println(Collections.disjoint(map1.getAll().values(), map2.getAll().values()));

            Counter ctr = new Counter(0);
            Counter ctr1 = new Counter(1);


            List<Long> times = new ArrayList<>(common.length);
            for(int i = 0; i<totalops; i++){

                makeOps(ctr,common[i].retrieveInput());
                makeOps(ctr1,common[i].retrieveInput());

                long start = System.nanoTime();
                ctr.merge(ctr1.getState());
                long finish = System.nanoTime();

                times.add(finish-start);
            }

            long sum = times.stream().reduce(0l, Long::sum);


            System.out.println(">>>>>>>>>>>>>>>>>> BENCHMARK STATS LWW");
            System.out.println("Operation: " + op);
            //from nano to seconds is 10^3 but then we divide by 10^3 to get op/s so in fact we must divide it all by 10^6
            System.out.format("CrdtOp/microseconds: %f ", sum / 1000000f);
            System.out.println();

        } catch (Exception e) {
            System.err.println("Unable to execute benchmark with params:");
            e.printStackTrace();

        }

    }

    private static void makeOps(Counter ctr, List<Integer> input){
        input.forEach( i ->ctr.inc(i));
    }


    public static void main(String[] args) {

        if (args.length < 5 || args.length > 8) {
            System.out.println("Usage : op nrOps nrUniqueElemenstsInInput baseStringForInput [baseStringForInput1] [baseStringForInput2} nrConcurrentOps");
            System.exit(1);
        }

        int op = Integer.parseInt(args[0]);
        int nOps = Integer.parseInt(args[1]);
        int uniqueElems = Integer.parseInt(args[2]);
        int base = Integer.parseInt(args[3]);
        float nrConcOps=0;
        boolean conc = false;

        int base1 = 0, base2 = 0;
        if (args.length == 7) {
            base1 = Integer.parseInt(args[4]);
            base2 = Integer.parseInt(args[5]);
            nrConcOps = Float.parseFloat(args[6]);
            System.out.println(nrConcOps);
        } else if (args.length == 5) {
            nrConcOps = Float.parseFloat(args[4]);
            if (nrConcOps != 0) {
                conc = true;
            } else conc = false;
        }

        CounterParams p, p1, p2;
        CounterBenchmark benchie = new CounterBenchmark(op);

        if (op != MERGE) {

             p = new CounterParams(nOps, base);
             benchie.execute(p);


        } else if (op == MERGE) {
            System.err.println("Preparing input");
            p = new CounterParams(nOps, base);

            CounterParams[] commons = new CounterParams[nOps];
            List<Integer> bases = p.retrieveInput();
            for(int i=0; i<commons.length; i++)
                commons[i] = new CounterParams(nOps, bases.get(i));

            benchie.executeMerge(nOps,commons);

        }


    }
}
