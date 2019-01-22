package client.stubs;

import helpers.ComparableByteArray;
import crdts.secure.SecureOURList;

import javax.crypto.SecretKey;
import java.util.ArrayList;
import java.util.List;

public class OURListStub<T> extends ListStub<T>{

    private SecureOURList list;

    public OURListStub(SecretKey k, byte[] iv) {
        super(k, iv);
        this.list = new SecureOURList();
    }

    public void insert(T elem) {
        this.list.insert(this.encrypt(elem), System.currentTimeMillis());
    }

    public void insert(int pos, T elem) {
        this.list.insert(pos, this.encrypt(elem), System.currentTimeMillis());
    }

    public void remove() {
        this.list.remove(System.currentTimeMillis());
    }

    public void remove(int pos) {
        this.list.remove(pos, System.currentTimeMillis());
    }

    public T get(int pos) {
        return this.decrypt( this.list.get(pos));
    }

    public boolean contains(T elem) {
        return this.list.contains(this.encrypt(elem));
    }

    public int size() {
        return this.list.size();
    }

    public List<T> getAll() {
        List<ComparableByteArray> l = this.list.getAll();
        List<T> res = new ArrayList<>(l.size());

        for(int i = 0; i< l.size(); i++)
            res.add(i,this.decrypt(l.get(i)));

        return res;
    }
}
