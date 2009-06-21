package jasi.datatype;

public class SBoolean {

    private boolean value;

    public SBoolean(String s) {
        char c = s.charAt(1);
        if(c == 't') value = true;
        else if(c == 'f') value = false;
        else
            throw new RuntimeException("not a scheme boolean: " + s);
    }

    public SBoolean(boolean b) {
        this.value = b;
    }

    public boolean getValue() {
        return value;
    }

    public String toString() {
        if(value) return "#t";
        else return "#f";
    }
}
