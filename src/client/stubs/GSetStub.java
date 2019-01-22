package client.stubs;

import crdts.secure.SecureGSet;

import javax.crypto.SecretKey;
import java.util.HashSet;
import java.util.Set;

public class GSetStub<E> extends SetStub<E> {

    private SecureGSet set;

    public GSetStub(SecretKey k, byte[] iv) {
        super(k,iv);
        this.set = new SecureGSet();
    }

    public void add(E elem) {
        this.set.add(this.encrypt(elem));
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
