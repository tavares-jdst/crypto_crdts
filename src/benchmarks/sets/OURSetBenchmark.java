package benchmarks.sets;

import benchmarks.helpers.Params;
import crdts.simple.OURSet;

import java.util.ArrayList;
import java.util.List;

public class OURSetBenchmark {
    public static final int ADD = 1;
    public static final int REMOVE = 2;
    public static final int CONTAINS = 3;
    public static final int GETALL = 4;
    public static final int MERGE = 5;

    static int op;

    public OURSetBenchmark(int opType) {
        op = opType;

    }

    public static void executeOUR(Params p) {
        if (op == MERGE)
            return;

        OURSet<String> set = new OURSet<>();

        try {
            List<String> input = p.retrieveInput();
            float totalOps = input.size()*1f;


            if (op != ADD)
                preFillWithData(set, input, 0);

            List<Long> times = new ArrayList<>(p.retrieveInput().size());
            long start, finish;

            for( String e: input) {
                switch (op) {
                    case ADD: {
                        start = System.nanoTime();
                        set.add(start+1, e);
                        finish =  System.nanoTime();
                        times.add(finish-start);
                    }
                    ;
                    break;
                    case REMOVE: {
                        start = System.nanoTime();
                        set.remove(start+1, e);
                        finish =  System.nanoTime();
                        times.add(finish-start);
                    }
                    ;
                    break;
                    case CONTAINS: {
                        start = System.nanoTime();
                        set.contains(e);
                        finish =  System.nanoTime();
                        times.add(finish-start);
                    }
                    ;
                    break;
                    case GETALL: {
                        start = System.nanoTime();
                        set.lookup();
                        finish =  System.nanoTime();
                        times.add(finish-start);
                    }
                    ;
                    break;
                }

            };

            System.out.println(">>>>>>>>>>>>>>>>>> BENCHMARK STATS OUR");
            System.out.println(p.toString());
            System.out.println("Operation: " + op);
            float tots = times.stream().reduce(0l, Long::sum)/totalOps;
            System.out.format("Op/microsegundos: %f", tots / 1000f);
            System.out.println();

        } catch (Exception e) {
            System.err.println("Unable to execute benchmark with params: \n" + p.toString());
            e.printStackTrace();

        }
    }

    public static void executeOUR(Params p, float concurrencyOpsPercentage) {
        if (op == MERGE)
            return;

        OURSet<String> set = new OURSet<>();

        try {
            List<String> input = p.retrieveInput();
            float totalOps = input.size()*1f;

            //inserir a priori os valores que vao gerar cocorrencia
            final int concOps = Math.round(concurrencyOpsPercentage * p.getNrOperations());
            final List<Long> tsForConcurrency = preFillWithData(set, input, concOps);



            if (op != ADD)
                preFillWithData(set, input, 0);

            List<Long> times = new ArrayList<>(p.retrieveInput().size());
            long start, finish;

            for(String e : input ) {
                switch (op) {
                    case ADD: {
                        long ts;
                        if (tsForConcurrency.isEmpty()) {
                            ts = System.nanoTime() + 1;
                        } else {
                            int index = tsForConcurrency.size() - 1;
                            ts = tsForConcurrency.get(index);
                            tsForConcurrency.remove(index);
                        }

                        start = System.nanoTime();
                        set.add(ts,e);
                        finish =  System.nanoTime();
                        times.add(finish-start);
                    }
                    ;
                    break;
                    case REMOVE: {
                        long ts;
                        if (tsForConcurrency.isEmpty()) {
                            ts = System.nanoTime() + 1;
                        } else {
                            int index = tsForConcurrency.size() - 1;
                            ts = tsForConcurrency.get(index);
                            tsForConcurrency.remove(index);
                        }
                        start = System.nanoTime();
                        set.remove(ts,e);
                        finish =  System.nanoTime();
                        times.add(finish-start);
                    }
                    break;
                }

            }


            System.out.println(">>>>>>>>>>>>>>>>>> BENCHMARK STATS OUR CONC");
            System.out.println(p.toString());
            System.out.println("Operation: " + op);
            float tots = times.stream().reduce(0l, Long::sum)/totalOps;
            System.out.format("Op/microsegundos: %f", tots / 1000f);
            System.out.println();

        } catch (Exception e) {
            System.err.println("Unable to execute benchmark with params: \n" + p.toString());
            e.printStackTrace();

        }
    }

    public void executeMergeOUR(Params[] common, Params firstStruct, Params secondStruct) {


        try {
            List<String> i1 = firstStruct.retrieveInput(); //unique data
            List<String> i2 = secondStruct.retrieveInput(); //unique data

            long start, finish;
            List<Long> times = new ArrayList<>(common.length);
            for (int i = 0; i < common.length; i++) {
                //TODO structs
                OURSet<String> s1 = new OURSet<>(), s2 = new OURSet<>();

                preFillWithData(s1, common[i].retrieveInput(), 0);
                preFillWithData(s1, i1, 0);

                preFillWithData(s2, common[i].retrieveInput(), 0);
                preFillWithData(s2, i2, 0);

                start = System.nanoTime();
                s1.merge(s2.getState());
                finish = System.nanoTime();

                times.add(finish - start);

            }

            float sum = times.stream().reduce(0l, Long::sum) / (common.length*1f);


            System.out.println(">>>>>>>>>>>>>>>>>> BENCHMARK STATS OUR");
            //from nano to seconds is 10^3 but then we divide by 10^3 to get op/s so in fact we must divide it all by 10^6
            System.out.format("Op/microseconds: %f", sum / 1000f);
            System.out.println();

        } catch (Exception e) {
            System.err.println("Unable to execute benchmark with params:");
            System.out.println("Shared " + common.toString());
            System.out.println("Struct 1 " + firstStruct.toString());
            System.out.println("Struct 2 " + secondStruct.toString());
            e.printStackTrace();

        }
    }

    private static List<Long> preFillWithData(OURSet<String> set, List<String> input, int nrTimestamps) {
        List<Long> tsCollection = new ArrayList<>(nrTimestamps);
        input.forEach(s -> {
            long ts = System.nanoTime() + 1;
            if (tsCollection.size() < nrTimestamps)
                tsCollection.add(ts);

            set.add(ts, s);
        });

        return tsCollection;
    }

    public static void main(String[] args) {

        if (args.length < 5 || args.length > 8) {
            System.out.println("Usage : op nrOps nrUniqueElemenstsInInput baseStringForInput [baseStringForInput1] [baseStringForInput2} nrConcurrentOps");
            System.exit(1);
        }

        int op = Integer.parseInt(args[0]);
        int nOps = Integer.parseInt(args[1]);
        int uniqueElems = Integer.parseInt(args[2]);
        String base = args[3];
        float nrConcOps = 0f;
        boolean conc = false;

        String base1 = "", base2 = "";
        if (args.length == 7) {
            base1 = args[4];
            base2 = args[5];
            nrConcOps = Float.parseFloat(args[6]);
            System.out.println(nrConcOps);
        } else if (args.length == 5) {
            nrConcOps = Float.parseFloat(args[4]);
            if (nrConcOps != 0) {
                conc = true;
            } else conc = false;
        }

        Params p, p1, p2;
        OURSetBenchmark benchie = new OURSetBenchmark(op);

        if (op != MERGE) {

            p = new Params(nOps,base);

            if (!conc) {
                benchie.executeOUR(p);

            } else {
                benchie.executeOUR(p, nrConcOps);
            }

        } else if (op == MERGE) {
            System.err.println("Preparing input");
            p = new Params(nOps, base);

            Params[] commons = new Params[nOps];
            List<String> bases = p.retrieveInput();
            for (int i = 0; i < commons.length; i++)
                commons[i] = new Params(nOps / 2, bases.get(i));

            p1 = new Params(nOps / 2, base1);
            p2 = new Params(nOps / 2,  base2);

            System.err.println("Done preparing input");
            benchie.executeMergeOUR(commons, p1, p2);

        }


    }
}
