package jasi.datatype;

public class SPair {
    private Object car;
    private Object cdr;

    public SPair() {}

    public SPair(Object car, Object cdr) {
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
        String result = "(" + car.toString();
        Object tmp = cdr;
        while(tmp != null && tmp instanceof SPair) {
            SPair p = (SPair)tmp;
            result += " " + p.getCar().toString();
            tmp = p.getCdr();
        }
        
        if(tmp == null) result += ")";
        else result += (" . " + tmp.toString() + ")");

        return result;
    }
}
