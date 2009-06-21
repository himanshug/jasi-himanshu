package jasi.datatype;

public class SChar {

    private char value;

    public SChar(String s) {
        //todo: put support for whitespace chars
        this.value = s.charAt(2);
    }

    public SChar(char c) {
        this.value = c;
    }

    public char getValue() {
        return value;
    }

    public String toString() {
        return "#\\" + value;
    }
}
