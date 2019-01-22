package benchmarks.lists;

import benchmarks.helpers.Params;
import crdts.simple.LWWList;

import java.util.ArrayList;
import java.util.List;

public class LWWListBenchmark {
    public static final int ADD = 1;
    public static final int REMOVE = 2;
    public static final int GET = 3;
    public static final int CONTAINS = 4;
    public static final int GETALL = 5;
    public static final int MERGE = 6;

    static int op;

    public LWWListBenchmark(int opType) {
        op = opType;

    }

    public static void executeLWW(Params p) {
        if (op == MERGE)
            return;

        LWWList<String> list = new LWWList<>();

        try {
            List<String> input = p.retrieveInput();
            float totalOps = input.size()*1f;


            if (op != ADD)
                preFillWithData(list, input, 0);

            List<Long> times = new ArrayList<>(p.retrieveInput().size());
            long start, finish;

            for (int i = 0; i < input.size(); i++) {
                switch (op) {
                    case ADD: {
                        start = System.nanoTime();
                        list.insert(input.get(i), start+1);
                        finish =  System.nanoTime();
                        times.add(finish-start);
                    }
                    ;
                    break;
                    case REMOVE: {
                        start = System.nanoTime();
                        long ts = System.nanoTime() + 1;
                        list.remove(ts);
                        finish =  System.nanoTime();
                        times.add(finish-start);
                    }
                    ;
                    break;
                    case GET: {
                        start = System.nanoTime();
                        list.get(i);
                        finish =  System.nanoTime();
                        times.add(finish-start);
                    }
                    break;
                    case CONTAINS: {
                        start = System.nanoTime();
                        list.contains(input.get(i));
                        finish =  System.nanoTime();
                        times.add(finish-start);
                    }
                    ;
                    break;
                    case GETALL: {
                        start = System.nanoTime();
                        list.getAll();
                        finish =  System.nanoTime();
                        times.add(finish-start);
                    }
                    ;
                    break;
                }
            }

            System.out.println(">>>>>>>>>>>>>>>>>> BENCHMARK STATS LWW");
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

    public static void executeLWW(Params p, float concurrencyOpsPercentage) {

        if (op == MERGE)
            return;

        LWWList<String> list = new LWWList<>();

        try {
            List<String> input = p.retrieveInput();
            float totalOps = input.size()*1f;

            //inserir a priori os valores que vao gerar cocorrencia
            final int concOps = Math.round(concurrencyOpsPercentage * p.getNrOperations());
            final List<Long> tsForConcurrency = preFillWithData(list, input, concOps);


            if (op != ADD)
                preFillWithData(list, input, 0);

            List<Long> times = new ArrayList<>(p.retrieveInput().size());
            long start, finish;

            for (int i = 0; i < input.size(); i++) {
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
                        list.insert(input.get(i), ts);
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
                        list.remove(ts);
                        finish =  System.nanoTime();
                        times.add(finish-start);
                    }
                    ;
                }
            }

            System.out.println(">>>>>>>>>>>>>>>>>> BENCHMARK STATS LWW");
            System.out.println(p.toString());
            System.out.println("Operation: " + op);
            System.out.println("Concurrent operations: " + concOps);

            float tots = times.stream().reduce(0l, Long::sum)/totalOps;
            System.out.format("Op/microsegundos: %f", tots / 1000f);
            System.out.println();

        } catch (Exception e) {
            System.err.println("Unable to execute benchmark with params: \n" + p.toString());
            e.printStackTrace();

        }


    }

    public void executeMergeLWW(Params[] common, Params firstStruct, Params secondStruct) {

        try {
            List<String> i1 = firstStruct.retrieveInput(); //unique data
            List<String> i2 = secondStruct.retrieveInput(); //unique data

            long start, finish;
            List<Long> times = new ArrayList<>(common.length);
            for (int i = 0; i < common.length; i++) {
                //TODO structs
                LWWList<String> l1 = new LWWList<>(), l2 = new LWWList<>();

                preFillWithData(l1, common[i].retrieveInput(), 0);
                preFillWithData(l1, i1, 0);

                preFillWithData(l2, common[i].retrieveInput(), 0);
                preFillWithData(l2, i2, 0);

                start = System.nanoTime();
                l1.merge(l2.getState());
                finish = System.nanoTime();

                times.add(finish - start);

            }

            long sum = times.stream().reduce(0l, Long::sum)/common.length;


            System.out.println(">>>>>>>>>>>>>>>>>> BENCHMARK STATS LWW");
            System.out.println("Operation: " + op);
            //from nano to seconds is 10^3 but then we divide by 10^3 to get op/s so in fact we must divide it all by 10^6
            System.out.format("CrdtOp/microseconds: %f ", sum / 1000f);
            System.out.println();

        } catch (Exception e) {
            System.err.println("Unable to execute benchmark with params:");
            System.out.println("Shared " + common.toString());
            System.out.println("Struct 1 " + firstStruct.toString());
            System.out.println("Struct 2 " + secondStruct.toString());
            e.printStackTrace();

        }

    }

    private static List<Long> preFillWithData(LWWList<String> list, List<String> input, int nrTimestamps) {
        List<Long> tsCollection = new ArrayList<>(nrTimestamps);
        input.forEach(s -> {
            long ts = System.nanoTime() + 1;
            if (tsCollection.size() < nrTimestamps)
                tsCollection.add(ts);

            list.insert(s, ts);
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
        LWWListBenchmark benchie = new LWWListBenchmark(op);

        if (op != MERGE) {

            p = new Params(nOps, base);

            if (!conc) {
                benchie.executeLWW(p);

            } else {
                benchie.executeLWW(p, nrConcOps);
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
            benchie.executeMergeLWW(commons, p1, p2);

        }


    }
}
