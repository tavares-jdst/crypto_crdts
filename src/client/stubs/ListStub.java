package client.stubs;

import client.Utils;
import helpers.ComparableByteArray;

import javax.crypto.SecretKey;

public class ListStub<E> {

    protected CryptoBean cryptobean;

    public ListStub(SecretKey k, byte[] iv) {
        cryptobean = new CryptoBean(null,null,k,iv);

    }

    protected ComparableByteArray encrypt(E elem) {
        return new ComparableByteArray(cryptobean.encRandom(elem));
    }

    protected E decrypt (ComparableByteArray elem){
        return (E) Utils.fromByte(cryptobean.decRandom(elem.getArray()));
    }
}
