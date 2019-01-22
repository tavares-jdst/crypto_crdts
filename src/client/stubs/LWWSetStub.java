package client.stubs;

import crdts.secure.SecureLWWSet;

import javax.crypto.SecretKey;
import java.util.HashSet;
import java.util.Set;

public class LWWSetStub<E> extends SetStub<E> {

    private SecureLWWSet set;

    public LWWSetStub(SecretKey k, byte[] iv) {
        super(k,iv);
        this.set = new SecureLWWSet();
    }

    public void add(E elem) {
        this.set.add(this.encrypt(elem), System.currentTimeMillis());
    }

    public void remove(E elem) {
        this.set.remove(this.encrypt(elem), System.currentTimeMillis());
    }

    public boolean contains(E elem) {
        return this.set.contains(this.encrypt(elem));
    }

    public int size() {
        return this.set.size();
    }

    public Set<E> getAll() {
        HashSet<E> res = new HashSet<>();
        this.set.getAll().forEach(e -> res.add(this.decrypt(e)));
        return res;
    }
}
