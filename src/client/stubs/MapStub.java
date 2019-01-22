package client.stubs;

import client.Utils;
import helpers.ComparableByteArray;

import javax.crypto.SecretKey;

public class MapStub<K,V> {


    private final CryptoBean cryptobean;

    public MapStub(SecretKey kD, byte[] ivD, SecretKey kP, byte[] ivP) {
        cryptobean = new CryptoBean(kD,ivD,kP,ivP);
    }

    protected ComparableByteArray encryptKey(K elem){
        return new ComparableByteArray(cryptobean.encDeterministic(elem));
        //return new ComparableByteArray(HomoDet.encrypt(this.kD, Utils.toByte(elem)));
    }

    protected K decryptKey(ComparableByteArray elem) {
        return (K) Utils.fromByte(cryptobean.decDeterministic(elem.getArray()));
        // /return (K) Utils.fromByte( HomoDet.decrypt(this.kD,elem.getArray()) );
    }

    protected ComparableByteArray encryptValue( V elem){
        return new ComparableByteArray(cryptobean.encRandom(elem));
        //return new ComparableByteArray(HomoRandAESPkcs7.encrypt(this.kR, this.iv, Utils.toByte(elem)));
    }

    protected V decryptValue(ComparableByteArray elem){
        if(elem==null)
            return null;

        return  (V) Utils.fromByte(cryptobean.decRandom(elem.getArray()));
    }
}
