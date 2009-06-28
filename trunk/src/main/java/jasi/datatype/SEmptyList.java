package jasi.datatype;

/**
 * An instance of this class represents scheme empty list. This implements
 * singeton so as to control instance creation to just one.
 * @author himanshu
 *
 */
public class SEmptyList {

    private final static SEmptyList instance = new SEmptyList();

    private SEmptyList() {}

    public static SEmptyList getInstance() {
        return instance;
    }

    public String toString() {
        return "()";
    }
}
