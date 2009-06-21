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

    public String toString() {
        String result = "(" + SchemeReader.write(car);
        Object tmp = cdr;
        while(tmp != null && tmp instanceof Pair) {
            Pair p = (Pair)tmp;
            result += " " + SchemeReader.write(p.getCar());
            tmp = p.getCdr();
        }
        
        if(tmp == null) result += ")";
        else result += (" . " + SchemeReader.write(tmp) + ")");
        return result;
    }

    public boolean isList(Object o) {
        if(o == null)
            return true;
        
        if(o instanceof Pair) {
            return isList(((Pair)o).getCdr());
        }
        return false;
    }
}

