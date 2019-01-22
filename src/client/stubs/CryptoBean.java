package client.stubs;

import client.Utils;
import crdts.secure.cryptoschemes.Deterministic;
import crdts.secure.cryptoschemes.Probabilistic;

import javax.crypto.SecretKey;

public class CryptoBean {

    private final Probabilistic rnd;
    private final Deterministic det;

    public CryptoBean() {
        rnd = new Probabilistic();
        det = new Deterministic();
    }

    public CryptoBean(SecretKey kD, byte[] IVD, SecretKey kP, byte[] IVR) {
        rnd = new Probabilistic(kP,IVR);
        det = new Deterministic(kD, IVD);
    }

    public byte[] encRandom(Object o) {
        return rnd.encrypt(Utils.toByte(o));
    }

    public byte[] decRandom(byte[] cba) {
        return rnd.decrypt(cba);
    }

    public byte[] encDeterministic(Object o) {
        return det.encrypt(Utils.toByte(o));
    }

    public byte[] decDeterministic(byte[] cba) {
        return det.decrypt(cba);
    }
}
