package jasi.semantics;

import jasi.Pair;

public class Utils {

    //empty list
    public static boolean isEmptyList(Object o) {
        return (o == null);
    }

    public static String writeEmptyList(Object o) {
        return "()";
    }

    //list
    public static boolean isSchemeList(Object o) {
        if(o == null)
            return true;
        
        if(o instanceof Pair) {
            return isSchemeList(((Pair)o).getCdr());
        }
        return false;
    }

    public static void validateType(Object o, Class c) {
        if(!c.isInstance(o))
            throw new RuntimeException("Object " + o + " not of type " + c.getName());
    }
}
