package client.stubs;

import crdts.secure.SecureOURMap;

import javax.crypto.SecretKey;
import java.util.HashMap;
import java.util.Map;

public class OURMapStub<K,V> extends MapStub<K,V> {

    private SecureOURMap map;

    public OURMapStub(SecretKey kD, byte[] ivD, SecretKey kP, byte[] ivP) {
        super(kD, ivD, kP, ivP);
        this.map = new SecureOURMap();
    }

    public void put(K k, V v) {
        this.map.put(this.encryptKey(k), this.encryptValue(v), System.currentTimeMillis());
    }

    public void remove(K k) {
        this.map.remove(this.encryptKey(k), System.currentTimeMillis());
    }

    public V get(K k) {
        return this.decryptValue( this.map.get(this.encryptKey(k)) );
    }

    public Map<K, V> getAll() {
        HashMap<K,V> res = new HashMap<>();
        this.map.getAll().forEach( (k,v)-> res.put(this.decryptKey(k), this.decryptValue(v)) );
        return res;
    }

    public boolean contains(K k) {
        return this.map.contains(this.encryptKey(k));
    }

    public int size() {
        return this.map.size();
    }
}
