package benchmarks.helpers;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Params {
    private int operations;
    private String base;
    private List<String> input;
    private static final String[] factors = {"A", "B", "C", "D", "E", "F"};

    /**
     * Encapsula os parametros e o input de teste. O input e gerado no instante em que o objecto e inicializado.
     *
     * @param nOps        - total de operaçoes a serem efectuadas
     * @param base        - string usada como base da geracao do input.
     */
    public Params(int nOps, String base) {
        this.operations = nOps;
        this.base = base;
        input = new ArrayList<>(operations);
        this.generateInput();
    }

    /**
     * Gera o input de teste
     */
    private void generateInput() {

        for (int i = 0; i < this.operations; i++)
            this.input.add(this.base + i);

    }

    /**
     * Permite obter o nr de operacoes do benchmark
     *
     * @return o numero de operacoes a ser efectuadas neste benchmark
     */
    public int getNrOperations() {
        return operations;
    }

    /**
     * Obter o input de teste
     *
     * @return - o input para este teste gerado automaticamente no momento de inicializacao do objecto.
     */
    public List<String> retrieveInput() {
        return this.input;
    }


    @Override
    public String toString() {
        return "Parameters = { Operations=" + operations +
                "; Base String=" + base + "; }";
    }
}
