package jasi.datatype;

public class SBoolean {

    private boolean value;

    private final static SBoolean trueInstance = new SBoolean(true);
    private final static SBoolean falseInstance = new SBoolean(false);

    private SBoolean(boolean b) {
        this.value = b;
    }

    public static SBoolean getInstance(boolean b) {
        if(b) return trueInstance;
        else return falseInstance;
    }

    public static SBoolean getInstance(String s) {
        return getInstance(parseSchemeStringToBoolean(s));
    }

    public boolean getValue() {
        return value;
    }

    public String toString() {
        if(value) return "#t";
        else return "#f";
    }

    private static boolean parseSchemeStringToBoolean(String s) {
        char c = s.charAt(1);
        if(c == 't') return true;
        else if(c == 'f') return false;
        else
            throw new RuntimeException("not a scheme boolean: " + s);
    }
}
