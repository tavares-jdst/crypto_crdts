package client.stubs;

import crdts.secure.SecureCounter;
import crdts.secure.cryptoschemes.Paillier;
import helpers.CounterState;

import java.math.BigInteger;

public class CounterStub {

    private Paillier paillier;
    private SecureCounter sc;

    public CounterStub(int repid, Paillier p) {
        this.paillier = p;
        this.sc = new SecureCounter(repid, this.paillier.getNsquare());
    }

    public void inc(int value) {
        this.sc.inc(this.encrypt(value));
    }

    public void dec(int value) {
        this.sc.dec(this.encrypt(value));
    }

    public int get() {
        return this.decrypt(this.sc.get());
    }

    private BigInteger encrypt(int x) {
        return this.paillier.Encryption(BigInteger.valueOf(x));
    }

    private int decrypt(BigInteger x) {
        return this.paillier.Decryption(x).intValue();
    }

    public CounterState getState() {
        return this.sc.getState();
    }

    public Paillier getPailler() {
        return this.paillier;
    }
}
