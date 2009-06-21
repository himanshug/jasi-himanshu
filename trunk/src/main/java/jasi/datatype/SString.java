package jasi.datatype;

public class SString {

    private StringBuffer value;

    public SString(String s) {
        this.value = new StringBuffer(s);
    }

    public SString(StringBuffer s) {
        this.value = s;
    }

    public StringBuffer getValue() {
        return value;
    }

    public String toString() {
        return value.toString();
    }
}
