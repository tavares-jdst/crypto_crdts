package benchmarks.maps;

import benchmarks.helpers.Params;
import benchmarks.helpers.Utils;
import crdts.simple.LWWMap;

import java.util.*;

public class LWWMapBenchmark {
    public static final int PUT = 1;
    public static final int REMOVE = 2;
    public static final int GET = 3;
    public static final int CONTAINS = 4;
    public static final int GETALL = 5;
    public static final int MERGE = 6;

    static int op;

    public LWWMapBenchmark(int opType) {
        op = opType;

    }

    public static void executeLWW(Params p) {
        if (op == MERGE)
            return;

        LWWMap<String, String> map = new LWWMap<>();

        try {
            Map<String, String> input = prepareInput(p.retrieveInput());
            float totalOps = input.size()*1f;


            if (op != PUT)
                preFillWithData(map, input, 0);

            List<Long> times = new ArrayList<>(p.retrieveInput().size());
            long start, finish;

            for(String k  : input.keySet()) {
                switch (op) {
                    case PUT: {
                        long ts = System.nanoTime() + 1;
                        String v = input.get(k);

                        start = System.nanoTime();
                        map.put(k, v, ts);
                        finish =  System.nanoTime();
                        times.add(finish-start);
                    }
                    ;
                    break;
                    case REMOVE: {
                        long ts = System.nanoTime() + 1;
                        start = System.nanoTime();
                        map.remove(k, ts);
                        finish =  System.nanoTime();
                        times.add(finish-start);
                    }
                    ;
                    break;
                    case GET: {
                        start = System.nanoTime();
                        map.get(k);
                        finish =  System.nanoTime();
                        times.add(finish-start);
                    }
                    ;
                    break;
                    case CONTAINS: {
                        start = System.nanoTime();
                        map.contains(k);
                        finish =  System.nanoTime();
                        times.add(finish-start);
                    }
                    ;
                    break;
                    case GETALL: {
                        start = System.nanoTime();
                        map.getAll();
                        finish =  System.nanoTime();
                        times.add(finish-start);
                    }
                    ;
                    break;
                }

            };

            System.out.println(">>>>>>>>>>>>>>>>>> BENCHMARK STATS LWW MAP");
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

        LWWMap<String, String> map = new LWWMap<>();

        try {
            SortedMap<String, String> input = prepareInput(p.retrieveInput());
            float totalOps = input.size()*1f;


            //inserir a priori os valores que vao gerar cocorrencia
            final int concOps = Math.round(concurrencyOpsPercentage * p.getNrOperations());
            final List<Long> tsForConcurrency = preFillWithData(map, input, concOps);


            List<Long> times = new ArrayList<>(p.retrieveInput().size());
            long start, finish;

            for(String k  : input.keySet()) {
                switch (op) {
                    case PUT: {
                        long ts;
                        if (tsForConcurrency.isEmpty()) {
                            ts = System.nanoTime() + 1;

                        } else {
                            int index = tsForConcurrency.size() - 1;
                            ts = tsForConcurrency.get(index);
                            tsForConcurrency.remove(index);

                        }

                        String v = input.get(k);

                        start = System.nanoTime();
                        map.put(k, v, ts);
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
                        map.remove(k, ts);
                        finish =  System.nanoTime();
                        times.add(finish-start);
                    }
                    ;
                    break;
                    default: {
                        System.err.println("Makes no sense");
                        return;
                    }
                }

            };


            System.out.println(">>>>>>>>>>>>>>>>>> BENCHMARK STATS LWW MAP");
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
            Map<String, String> i1 = prepareInput(firstStruct.retrieveInput()); //unique data
            Map<String, String> i2 = prepareInput(secondStruct.retrieveInput()); //unique data

            long start, finish;
            List<Long> times = new ArrayList<>(common.length);
            for (int i = 0; i < common.length; i++) {
                LWWMap<String, String> map1 = new LWWMap<>(), map2 = new LWWMap<>();

                preFillWithData(map1, prepareInput(common[i].retrieveInput()), 0);
                preFillWithData(map1, i1, 0);

                preFillWithData(map2, prepareInput(common[i].retrieveInput()), 0);
                preFillWithData(map2, i2, 0);

                start = System.nanoTime();
                map1.merge(map2.getState());
                finish = System.nanoTime();

                times.add(finish - start);

            }

            long sum = times.stream().reduce(0l, Long::sum)/common.length;


            System.out.println(">>>>>>>>>>>>>>>>>> BENCHMARK STATS LWW MAP");
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

    private static List<Long> preFillWithData(LWWMap map, Map<String, String> input, int nrTimestamps) {

        List<Long> tsCollection = new ArrayList<>(nrTimestamps);
        input.forEach((k, v) -> {
            long ts = System.nanoTime() + 1;
            if (tsCollection.size() < nrTimestamps)
                tsCollection.add(ts);
            map.put(k, v, ts);
        });

        return tsCollection;
    }

    private static SortedMap<String, String> prepareInput(List<String> strings) throws Exception {
        SortedMap<String, String> r = new TreeMap<>();
        for (String s : strings) {
            r.put(Utils.digestAndString(s), s);
        }

        return r;
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

        String base1 = "scrdtsrock", base2 = "inforumrocks";
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
        LWWMapBenchmark benchie = new LWWMapBenchmark(op);

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
