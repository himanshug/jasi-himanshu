package jasi.datatype;

//a class to represent undefined values
//as in the result of a definition statement
public class SUndefined {

    private final static SUndefined instance = new SUndefined();

    private SUndefined() {}

    public static SUndefined getInstance() {
        return instance;
    }

    public String toString() {
        return "";
    }
}
