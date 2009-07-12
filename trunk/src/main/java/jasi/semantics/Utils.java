package jasi.semantics;

import java.util.List;
import java.util.logging.Logger;

import jasi.datatype.SBoolean;
import jasi.datatype.SEmptyList;
import jasi.datatype.SPair;

public class Utils {

    private final static Logger log = Logger.getLogger(Utils.class.getName());

    //truth value
    //everything but SBoolean(value = false) is true
    public static boolean isTrue(Object o) {
        if(o instanceof SBoolean)
            return ((SBoolean)o).getValue();
        else
            return true;
    }

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

    //varioust list selector functions
    //returns the car(or first object) of a pair(list)
    public static Object first(Object o) {
        log.finer("getting first of list expression: " + o);
        validateType(o, SPair.class);
        return ((SPair)o).getCar();
    }

    //returns the cdr(or rest) of a pair(list)
    public static Object rest(Object o) {
        log.finer("getting rest of list expression: " + o);
        validateType(o, SPair.class);
        return ((SPair)o).getCdr();
    }

    //create a scheme list of given items
    public static Object list(List<Object> items) {
        int size = (items == null) ? 0 : items.size();
        if(size > 0) {
            SPair result = new SPair();
            SPair sp = result;
            for(int i = 0; i<size; i++) {
                sp.setCar(items.get(i));
                if(i == size-1) {
                    sp.setCdr(SEmptyList.getInstance());
                }
                else {
                    SPair tmp = new SPair();
                    sp.setCdr(tmp);
                    sp = tmp;
                }
            }
            return result;
        }
        else
            return SEmptyList.getInstance();
    }

    public static SPair cons(Object car, Object cdr) {
        SPair p = new SPair();
        p.setCar(car);
        p.setCdr(cdr);
        return p;
    }

    public static Object car(Object o) {
        return first(o);
    }

    public static Object cdr(Object o) {
        return rest(o);
    }

    public static Object cadr(Object o) {
        return car(cdr(o));
    }

    public static Object caddr(Object o) {
        return car(cdr(cdr(o)));
    }

    public static Object cadddr(Object o) {
        return car(cdr(cdr(cdr(o))));
    }

    public static Object caadr(Object o) {
        return car(car(cdr(o)));
    }

    public static Object cddr(Object o) {
        return cdr(cdr(o));
    }

    public static Object cdadr(Object o) {
        return cdr(car(cdr(o)));
    }

    public static Object cdddr(Object o) {
        return cdr(cdr(cdr(o)));
    }

    public static void validateType(Object o, Class c) {
        log.finer("validating type of :" + o + ": for class :" + c.getName());
        if(o == null)
            throw new RuntimeException("found null, expected type: " + c.getName());
        else if(!c.isInstance(o))
            throw new RuntimeException("Object " + o + " not of type " + c.getName());
    }
}
