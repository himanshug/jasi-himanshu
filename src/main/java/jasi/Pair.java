package jasi;

public class Pair {
    private Object car;
    private Object cdr;

    public Pair() {}

    public Pair(Object car, Object cdr) {
        this.car = car;
        this.cdr = cdr;
    }

    public Object getCar() {
        return car;
    }

    public void setCar(Object car) {
        this.car = car;
    }

    public Object getCdr() {
        return cdr;
    }

    public void setCdr(Object cdr) {
        this.cdr = cdr;
    }

    public static boolean isList(Object o) {
        if(o == null)
            return true;
        
        if(o instanceof Pair) {
            return isList(((Pair)o).getCdr());
        }
        return false;
    }
}

