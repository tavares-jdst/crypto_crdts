package client.stubs;

import crdts.secure.SecureOURSet;

import javax.crypto.SecretKey;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

/**
 * TODO
 * Susbstituir o set com um target para um srv do lado dos CRDTs
 */
public class OURSetStub<E> extends SetStub<E> {

    private final SecureOURSet set;
    private Map<UUID, E> mapper;

    public OURSetStub(SecretKey k, byte[] iv) {
        super(k,iv);
        this.set = new SecureOURSet();
    }

    public void add (E elem){

        this.set.add(System.currentTimeMillis(), this.encrypt(elem));
    }

    public void remove(E elem){
        this.set.remove(System.currentTimeMillis(), this.encrypt(elem));
    }

    public boolean contains(E elem) {
        return this.set.contains(this.encrypt(elem));
    }

    public int size() {
        return this.set.size();
    }

    public Set<E> getAll() {
        HashSet<E> res = new HashSet<>();
        this.set.getAll().forEach( e -> res.add(this.decrypt(e)) );
        return res;
    }
}
