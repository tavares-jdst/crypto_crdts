package benchmarks.maps;

import benchmarks.helpers.Params;
import benchmarks.helpers.Utils;
import client.stubs.CryptoBean;
import crdts.secure.SecureLWWMap;
import helpers.ComparableByteArray;

import java.util.*;

public class SecureLWWMapBenchmark {
    public static final int PUT = 1;
    public static final int REMOVE = 2;
    public static final int GET = 3;
    public static final int CONTAINS = 4;
    public static final int GETALL = 5;
    public static final int MERGE = 6;

    static int op;
    private static CryptoBean cryptobean = new CryptoBean();

    public SecureLWWMapBenchmark(int opType) {
        op = opType;

    }

    public static void executeLWW(Params p) {
        if (op == MERGE)
            return;

        SecureLWWMap map = new SecureLWWMap();

        try {

            List<String> tmp_input = p.retrieveInput();
            float totalOps = tmp_input.size() * 1f;

            List<Long> encK = new ArrayList<>(tmp_input.size());
            List<Long> encV = new ArrayList<>(tmp_input.size());

            long eK_start, eK_finish, eV_start, eV_finish;
            Map<ComparableByteArray, ComparableByteArray> input = new HashMap<>(1000);

            for (String s : tmp_input) {
                eK_start = System.nanoTime();
                byte[] k = cryptobean.encDeterministic(s);
                eK_finish = System.nanoTime();

                ComparableByteArray key = new ComparableByteArray(k);

                eV_start = System.nanoTime();
                byte[] v = cryptobean.encRandom(s);
                eV_finish = System.nanoTime();

                ComparableByteArray value = new ComparableByteArray(v);

                encK.add(eK_finish - eK_start);
                encV.add(eV_finish - eV_start);

                input.put(key, value);
            }

            if (op != PUT) {
                preFillWithData(map, input, 0);
            }


            List<Long> times = new ArrayList<>(p.retrieveInput().size());
            long start, finish;

            for (ComparableByteArray k : input.keySet()) {
                switch (op) {
                    case PUT: {
                        long ts = System.nanoTime() + 1;
                        ComparableByteArray v = input.get(k);

                        start = System.nanoTime();
                        map.put(k, v, ts);
                        finish = System.nanoTime();
                        times.add(finish - start);
                    }
                    ;
                    break;
                    case REMOVE: {
                        long ts = System.nanoTime() + 1;
                        start = System.nanoTime();
                        map.remove(k, ts);
                        finish = System.nanoTime();
                        times.add(finish - start);
                    }
                    ;
                    break;
                    case GET: {
                        start = System.nanoTime();
                        map.get(k);
                        finish = System.nanoTime();
                        times.add(finish - start);
                    }
                    ;
                    break;
                    case CONTAINS: {
                        start = System.nanoTime();
                        map.contains(k);
                        finish = System.nanoTime();
                        times.add(finish - start);
                    }
                    ;
                    break;
                    case GETALL: {
                        start = System.nanoTime();
                        map.getAll().forEach((cba_k, cba_v) ->
                        {
                            cryptobean.decDeterministic(cba_k.getArray());
                            cryptobean.decRandom(cba_v.getArray());
                        });

                        finish = System.nanoTime();
                        times.add(finish - start);
                    }
                    ;
                    break;
                }

            }

            System.out.println(">>>>>>>>>>>>>>>>>> BENCHMARK STATS LWW MAP");
            System.out.println(p.toString());
            System.out.println("Operation: " + op);

            float tots = times.stream().reduce(0l, Long::sum) / totalOps;
            System.out.format("CrdtOp/microseconds: %f ", tots / 1000f);
            System.out.println();

            float encTimeK = encK.stream().reduce(0l, Long::sum) / totalOps;
            float encTimeV = encV.stream().reduce(0l, Long::sum) / totalOps;

            System.out.format("EncOp/microseconds for key: %f ", encTimeK / 1000f);
            System.out.println();

            System.out.format("EncOp/microseconds for value: %f ", encTimeV / 1000f);
            System.out.println();

            System.out.format("Total Op/microseconds: %f for key", (encTimeK + tots) / 1000f);
            System.out.println();

            System.out.format("Total Op/microseconds: %f for value", (encTimeV + tots) / 1000f);
            System.out.println();

            System.out.format("Total Op/microseconds: %f global", (encTimeK + encTimeV + tots) / 1000f);
            System.out.println();
        } catch (Exception e) {
            System.err.println("Unable to execute benchmark with params: \n" + p.toString());
            e.printStackTrace();

        }

    }

    public static void executeLWW(Params p, float concurrencyOpsPercentage) {

        if (op == MERGE)
            return;

        SecureLWWMap map = new SecureLWWMap();

        try {
            List<String> tmp_input = p.retrieveInput();
            float totalOps = tmp_input.size() * 1f;

            List<Long> encK = new ArrayList<>(tmp_input.size());
            List<Long> encV = new ArrayList<>(tmp_input.size());

            long eK_start, eK_finish, eV_start, eV_finish;
            Map<ComparableByteArray, ComparableByteArray> input = new HashMap<>(1000);

            for (String s : tmp_input) {
                eK_start = System.nanoTime();
                byte[] k = cryptobean.encDeterministic(s);
                eK_finish = System.nanoTime();

                ComparableByteArray key = new ComparableByteArray(k);

                eV_start = System.nanoTime();
                byte[] v = cryptobean.encRandom(s);
                eV_finish = System.nanoTime();

                ComparableByteArray value = new ComparableByteArray(v);

                encK.add(eK_finish - eK_start);
                encV.add(eV_finish - eV_start);

                input.put(key, value);
            }


            //inserir a priori os valores que vao gerar cocorrencia
            final int concOps = Math.round(concurrencyOpsPercentage * p.getNrOperations());
            final List<Long> tsForConcurrency = preFillWithData(map, input, concOps);


            List<Long> times = new ArrayList<>(p.retrieveInput().size());
            long start, finish;

            for (ComparableByteArray k : input.keySet()) {
                switch (op) {
                    case PUT: {
                        long ts = System.nanoTime() + 1;
                        ComparableByteArray v = input.get(k);

                        start = System.nanoTime();
                        map.put(k, v, ts);
                        finish = System.nanoTime();
                        times.add(finish - start);
                    }
                    ;
                    break;
                    case REMOVE: {
                        long ts = System.nanoTime() + 1;
                        start = System.nanoTime();
                        map.remove(k, ts);
                        finish = System.nanoTime();
                        times.add(finish - start);
                    }
                    ;
                    break;
                    default: {
                        System.err.println("Makes no sense");
                        return;
                    }
                }

            }

            System.out.println(">>>>>>>>>>>>>>>>>> BENCHMARK STATS LWW MAP");
            System.out.println(p.toString());
            System.out.println("Operation: " + op);

            float tots = times.stream().reduce(0l, Long::sum) / totalOps;
            System.out.format("CrdtOp/microseconds: %f ", tots / 1000f);
            System.out.println();

            float encTimeK = encK.stream().reduce(0l, Long::sum) / totalOps;
            float encTimeV = encV.stream().reduce(0l, Long::sum) / totalOps;

            System.out.format("EncOp/microseconds for key: %f ", encTimeK / 1000f);
            System.out.println();

            System.out.format("EncOp/microseconds for value: %f ", encTimeV / 1000f);
            System.out.println();

            System.out.format("Total Op/microseconds: %f for key", (encTimeK + tots) / 1000f);
            System.out.println();

            System.out.format("Total Op/microseconds: %f for value", (encTimeV + tots) / 1000f);
            System.out.println();

            System.out.format("Total Op/microseconds: %f global", (encTimeK + encTimeV + tots) / 1000f);
            System.out.println();
        } catch (Exception e) {
            System.err.println("Unable to execute benchmark with params: \n" + p.toString());
            e.printStackTrace();

        }


    }

    public void executeMergeLWW(Params[] common, Params firstStruct, Params secondStruct) {

        try {
            SortedMap<ComparableByteArray, ComparableByteArray> i1 = prepareInput(firstStruct.retrieveInput()); //unique data
            SortedMap<ComparableByteArray, ComparableByteArray> i2 = prepareInput(secondStruct.retrieveInput()); //unique data
            long start, finish;
            List<Long> times = new ArrayList<>(common.length);
            for (int i = 0; i < common.length; i++) {
                SecureLWWMap map1 = new SecureLWWMap(), map2 = new SecureLWWMap();

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

    private static List<Long> preFillWithData(SecureLWWMap map, Map<ComparableByteArray, ComparableByteArray> input, int nrTimestamps) {

        List<Long> tsCollection = new ArrayList<>(nrTimestamps);
        input.forEach((k, v) -> {
            long ts = System.nanoTime() + 1;
            if (tsCollection.size() < nrTimestamps)
                tsCollection.add(ts);
            map.put(k, v, ts);
        });

        return tsCollection;
    }

    private static SortedMap<ComparableByteArray, ComparableByteArray> prepareInput(List<String> strings) {
        SortedMap<ComparableByteArray, ComparableByteArray> r = new TreeMap<>();
        for (String s : strings) {
            ComparableByteArray key = new ComparableByteArray(cryptobean.encDeterministic(s));
            ComparableByteArray value = new ComparableByteArray(cryptobean.encRandom(s));
            r.put(key, value);
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
        float nrConcOps = 0;
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
        SecureLWWMapBenchmark benchie = new SecureLWWMapBenchmark(op);

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
            p2 = new Params(nOps / 2, base2);

            System.err.println("Done preparing input");

            benchie.executeMergeLWW(commons, p1, p2);

        }


    }
}