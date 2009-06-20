package jasi.semantics;

public class Utils {

    public static String stringify(Object arg) {
        if(arg instanceof Double) {
            return Integer.toString(((Double)arg).intValue());
        }
        //else ifs
        else
            throw new RuntimeException("could not stringify:" + arg);
        
    }
}
