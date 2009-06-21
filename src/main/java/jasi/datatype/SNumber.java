package jasi.datatype;

public class SNumber {

    private double value;
    
    public SNumber(String s) {
        this.value = Double.parseDouble(s);
    }

    public SNumber(double d) {
        this.value = d;
    }

    public double getValue() {
        return value;
    }

    public String toString() {
        return Integer.toString(Double.valueOf(value).intValue());
    }
}

