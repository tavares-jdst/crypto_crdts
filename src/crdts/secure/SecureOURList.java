package crdts.secure;


import crdts.simple.OURList;

/**
 * Secure list with the Add Wins concurrency policy.
 */
public class SecureOURList extends SecureList {

    public SecureOURList() {
        super();
        this.state = new OURList<>();
    }
}
