package benchmarks.sets;

import benchmarks.helpers.Params;
import crdts.simple.GSet;

import java.util.ArrayList;
import java.util.List;

public class GOnlySetBenchmark {
    public static final int ADD = 1;
    public static final int REMOVE = 2;
    public static final int CONTAINS = 3;
    public static final int GETALL = 4;
    public static final int MERGE = 5;

    static int op;

    public GOnlySetBenchmark(int opType) {
        op = opType;

    }

    public static void executeGONLY(Params p) {
        if (op == MERGE || op == REMOVE)
            return;

        GSet<String> set = new GSet<>();

        try {
            List<String> input = p.retrieveInput();
            float totalOps = input.size()*1f;


            if (op != ADD)
                preFillWithData(set, input);

            List<Long> times = new ArrayList<>(p.retrieveInput().size());
            long start, finish;

            for(String e : input) {
                switch (op) {
                    case ADD: {
                        start = System.nanoTime();
                        set.add(e);
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

            }


            System.out.println(">>>>>>>>>>>>>>>>>> BENCHMARK STATS GONLY");
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

    public void executeMergeGONLY( Params[] common, Params firstStruct, Params secondStruct) {

        try {
            List<String> i1 = firstStruct.retrieveInput(); //unique data
            List<String> i2 = secondStruct.retrieveInput(); //unique data

            long start, finish;
            List<Long> times = new ArrayList<>(common.length);
            for (int i = 0; i < common.length; i++) {
                GSet<String> s1 = new GSet<>(), s2 = new GSet<>();

                preFillWithData(s1, common[i].retrieveInput());
                preFillWithData(s1, i1);

                preFillWithData(s2, common[i].retrieveInput());
                preFillWithData(s2, i2);

                start = System.nanoTime();
                s1.merge(s2.getState());
                finish = System.nanoTime();

                times.add(finish - start);

            }

            System.err.println(common.length);
            float totalOps = common.length*1f;
            float sum = times.stream().reduce(0l, Long::sum) / totalOps;


            System.out.println(">>>>>>>>>>>>>>>>>> BENCHMARK STATS GONLY");
            System.out.println("Operation: " + op);
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

    private static void preFillWithData(GSet<String> set, List<String> input) {
        input.forEach(s -> set.add(s));
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
        } else if (args.length == 5) {
            nrConcOps = Float.parseFloat(args[4]);
            if (nrConcOps != 0) {
                conc = true;
            } else conc = false;
        }

        Params p, p1, p2;
        GOnlySetBenchmark benchie = new GOnlySetBenchmark(op);

        if (op != MERGE) {

            p = new Params(nOps, base);

            if (!conc) {
                benchie.executeGONLY(p);
            }

        } else if (op == MERGE) {
            System.err.println("Preparing input");
            p = new Params(nOps, base); //gerar 1000 base strings

            Params[] commons = new Params[nOps];
            List<String> bases = p.retrieveInput();

            for (int i = 0; i < commons.length; i++) //gerar 1000 inputs distintos onde cada um tem 500 strings
                commons[i] = new Params(nOps / 2, bases.get(i));

            p1 = new Params(nOps / 2, base1);
            p2 = new Params(nOps / 2, base2);

            System.err.println("Done preparing input");

            //para cada um dos 1000 inputs distintos onde cada tem 500 strings diferentes, vamos juntar as 500 strings que vao ser
            //comuns a cada conjunto para que o merge tenha overlap. ao total vamos ter 1000 inputs com 500 strings diferentes e 500 iguais
            benchie.executeMergeGONLY(commons, p1, p2);

        }


    }
}
