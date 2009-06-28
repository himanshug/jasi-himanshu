package jasi.semantics;

import java.util.logging.Logger;

import jasi.datatype.SEmptyList;
import jasi.datatype.SPair;

public class Utils {

    private final static Logger log = Logger.getLogger(Utils.class.getName());

    //list
    public static boolean isSchemeList(Object o) {
        if(o instanceof SEmptyList)
            return true;
        
        if(o instanceof SPair) {
            return isSchemeList(((SPair)o).getCdr());
        }
        log.finer("this expression :" + o + ": found not to be a list");
        return false;
    }

    //list-length
    public static int getSchemeListLength(Object o) {
        if(o == null)
            throw new RuntimeException("not a valid list:" + o);
        
        if(o instanceof SEmptyList) return 0;
        else
            return 1 + getSchemeListLength(rest(o));
    }

    //returns the car(or first object) of a pair(list)
    public static Object first(Object o) {
        log.fine("getting first of list expression: " + o);
        validateType(o, SPair.class);
        return ((SPair)o).getCar();
    }

    //returns the cdr(or rest) of a pair(list)
    public static Object rest(Object o) {
        log.fine("getting rest of list expression: " + o);
        validateType(o, SPair.class);
        return ((SPair)o).getCdr();
    }

    public static void validateType(Object o, Class c) {
        log.fine("validating type of :" + o + ": for class :" + c.getName());
        if(o == null)
            throw new RuntimeException("found null, expected type: " + c.getName());
        else if(!c.isInstance(o))
            throw new RuntimeException("Object " + o + " not of type " + c.getName());
    }
}