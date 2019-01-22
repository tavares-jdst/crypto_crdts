package client.stubs;

import client.Utils;
import crdts.secure.SecureLWWRegister;
import helpers.ComparableByteArray;

import javax.crypto.SecretKey;

public class RegisterStub<T> {

    private final CryptoBean cryptobean;

    public RegisterStub(SecretKey k, byte[] iv) {
        cryptobean = new CryptoBean(null, null, k,iv);
    }

    private ComparableByteArray encrypt(T elem) {
        return new ComparableByteArray(cryptobean.encRandom( elem));
    }

    private T decrypt(ComparableByteArray elem){
        return (T) Utils.fromByte(cryptobean.decRandom(elem.getArray()));
    }

}
