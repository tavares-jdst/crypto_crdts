package benchmarks.registers;

import benchmarks.helpers.Params;
import client.stubs.CryptoBean;
import crdts.secure.SecureLWWRegister;
import helpers.ComparableByteArray;

import java.util.ArrayList;
import java.util.List;

public class SecureRegisterBenchmark {
    public static final int PUT = 1;
    public static final int GET = 2;
    public static final int MERGE = 3;

    static int op;
    private static CryptoBean cryptobean =  new CryptoBean();

    public SecureRegisterBenchmark(int opType) {
        op = opType;
    }

    public static void executeLWW(Params p) {
        if (op == MERGE)
            return;

        try {
            List<Long> enctimes = new ArrayList<>(p.retrieveInput().size());
            List<ComparableByteArray> input = new ArrayList<>(p.retrieveInput().size());
            long startEnc;
            for (String s : p.retrieveInput()) {
                startEnc = System.nanoTime();
                byte[] e = cryptobean.encRandom(s);
                long endEnc = System.nanoTime() - startEnc;

                enctimes.add(endEnc);
                input.add(new ComparableByteArray(e));
            }

            float totalOps = input.size() * 1f;

            List<Long> optimes = new ArrayList<>(p.retrieveInput().size());
            long start, finish;
            SecureLWWRegister rg = new SecureLWWRegister(null, -1);

            if (op == GET) {
                rg = new SecureLWWRegister(input.get(0), System.nanoTime());
            }

            for (ComparableByteArray e : input) {
                switch (op) {
                    case PUT: {
                        long ts = System.nanoTime() + 1;
                        start = System.nanoTime();
                        rg.put(e, ts);
                        finish =  System.nanoTime();

                        optimes.add(finish-start);
                    }
                    ;
                    break;
                    case GET: {
                        start = System.nanoTime();
                        cryptobean.decRandom(rg.get().getArray());
                        finish =  System.nanoTime();

                        optimes.add(finish-start);
                    }
                    ;
                    break;
                }
            }

            System.out.println(">>>>>>>>>>>>>>>>>> BENCHMARK STATS LWW");
            System.out.println(p.toString());
            System.out.println("Operation: " + op);
            float tots = optimes.stream().reduce(0l, Long::sum) / totalOps;
            System.out.format("CrdtOp/microsegundos: %f ", tots / 1000f);
            System.out.println();


            if (op == PUT) {

                float encTime = enctimes.stream().reduce(0l, Long::sum) / totalOps;
                System.out.format("EncOp/microsegundos: %f ", encTime / 1000f);
                System.out.println();
                System.out.format("Total (Op + EncOp)/microsegundos: %f", (encTime + tots) / 1000f);
                System.out.println();

            }

        } catch (Exception e) {
            System.err.println("Unable to execute benchmark with params: \n" + p.toString());
            e.printStackTrace();

        }

    }

    public static void executeLWW(Params p, float concurrencyOpsPercentage) {

        if (op == MERGE)
            return;

        try {

            List<Long> enctimes = new ArrayList<>(p.retrieveInput().size());
            List<ComparableByteArray> input = new ArrayList<>(p.retrieveInput().size());
            long startEnc;
            for (String s : p.retrieveInput()) {
                startEnc = System.nanoTime();
                byte[] e = cryptobean.encRandom(s);
                long endEnc = System.nanoTime() - startEnc;

                enctimes.add(endEnc);
                input.add(new ComparableByteArray(e));
            }

            //inserir a priori os valores que vao gerar cocorrencia
            final int concOps = Math.round(concurrencyOpsPercentage * p.getNrOperations());

            long concTs = System.nanoTime();
            int ctr = 0;

            List<Long> optimes = new ArrayList<>(p.retrieveInput().size());
            long start, finish;
            SecureLWWRegister rg = new SecureLWWRegister(null, -1);

            if (op == GET) {
                rg = new SecureLWWRegister(input.get(0), System.nanoTime());
            }

            for (ComparableByteArray e : input) {
                switch (op) {
                    case PUT: {
                        long ts;
                        if (ctr < concOps) {
                            ts = concTs;
                            ctr++;

                        } else {
                            ts = System.nanoTime() + 1;

                        }
                        start = System.nanoTime();
                        rg.put(e, ts);
                        finish =  System.nanoTime();

                        optimes.add(finish-start);
                    }
                    ;
                    break;
                    default: {
                        System.err.println("Makes no sense");
                        return;
                    }
                }

            }

            float totalOps = input.size() * 1f;

            System.out.println(">>>>>>>>>>>>>>>>>> BENCHMARK STATS LWW ");
            System.out.println(p.toString());
            System.out.println("Operation: " + op);
            System.out.println("Concurrent operations: " + concOps);

            float tots = optimes.stream().reduce(0l, Long::sum) / totalOps;
            System.out.format("CrdtOp/microsegundos: %f ", tots / 1000f);
            System.out.println();


            if (op == PUT) {

                float encTime = enctimes.stream().reduce(0l, Long::sum) / totalOps;
                System.out.format("EncOp/microsegundos: %f ", encTime / 1000f);
                System.out.println();
                System.out.format("Total (Op + EncOp)/microsegundos: %f", (encTime + tots) / 1000f);
                System.out.println();

            }


        } catch (Exception e) {
            System.err.println("Unable to execute benchmark with params: \n" + p.toString());
            e.printStackTrace();

        }


    }

    public void executeMergeLWW(Params a, Params b) {

        try {

            //lets see if it looks good, should be false
            //System.out.println(Collections.disjoint(map1.getAll().values(), map2.getAll().values()));

            List<ComparableByteArray> in1 = convertInput(a.retrieveInput());
            List<ComparableByteArray> in2 = convertInput(b.retrieveInput());

            SecureLWWRegister rg1, rg2;
            List<Long> times = new ArrayList<>(in1.size());

            for (int i = 0; i < a.getNrOperations(); i++) {
                rg1 = new SecureLWWRegister(in1.get(i), System.nanoTime());
                rg2 = new SecureLWWRegister(in2.get(i), System.nanoTime());

                long start = System.nanoTime();

                rg1.merge(rg2.getState());

                long finish = System.nanoTime();

                times.add(finish - start);
            }

            long sum = times.stream().reduce(0l, Long::sum);


            System.out.println(">>>>>>>>>>>>>>>>>> BENCHMARK STATS LWW");
            System.out.println("Operation: " + op);
            System.out.format("CrdtOp/microseconds: %f ", sum / 1000000f);
            System.out.println();

        } catch (Exception e) {
            System.err.println("Unable to execute benchmark with params:");
            e.printStackTrace();

        }

    }

    private static List<ComparableByteArray> convertInput(List<String> input) {
        List<ComparableByteArray> r = new ArrayList<>(input.size());
        input.forEach(s -> r.add( new ComparableByteArray(cryptobean.encRandom(s))));
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
        SecureRegisterBenchmark benchie = new SecureRegisterBenchmark(op);

        if (op != MERGE) {

            p = new Params(nOps,base);

            if (!conc) {
                benchie.executeLWW(p);


            } else {
                benchie.executeLWW(p, nrConcOps);

            }

        } else if (op == MERGE) {
            System.err.println("Preparing input");

            p1 = new Params(nOps, base1);
            p2 = new Params(nOps, base2);

            System.err.println("Done preparing input");


            benchie.executeMergeLWW(p1, p2);

        }


    }
}
