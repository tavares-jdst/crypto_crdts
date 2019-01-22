package crdts.secure;


import crdts.simple.LWWList;

/**
 * Secure list with the Last Writer Wins concurrency policy.
 */
public class SecureLWWList extends SecureList {

    public SecureLWWList() {
        super();
        this.state = new LWWList<>();
    }
}
