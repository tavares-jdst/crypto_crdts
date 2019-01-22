package client.stubs;

import client.Utils;
import helpers.ComparableByteArray;

import javax.crypto.SecretKey;

public class SetStub<E> {


    private final CryptoBean cryptobean;

    public SetStub(SecretKey k, byte[] iv) {

        cryptobean = new CryptoBean(k,iv,null,null);
    }

    protected ComparableByteArray encrypt(E elem){
        return new ComparableByteArray(cryptobean.encDeterministic(elem));
    }

    protected E decrypt(ComparableByteArray elem){
        return (E) Utils.fromByte(cryptobean.decDeterministic(elem.getArray()));
    }

}
