package jasi.datatype;

import jasi.Constants;

public class SChar {

    private char value;

    public SChar(String s) {
        if(s.length() == 3)
            this.value = s.charAt(2);
        else {
            String tmp = s.substring(2);
            if(tmp.equalsIgnoreCase(Constants.SCHEME_CHAR_NEWLINE)) {
                this.value = '\n';
            }
            else if(tmp.equalsIgnoreCase(Constants.SCHEME_CHAR_SPACE)) {
                this.value = ' ';
            }
            else
                throw new RuntimeException("Could not parse char value from: " + s);
        }
    }

    public SChar(char c) {
        this.value = c;
    }

    public char getValue() {
        return value;
    }

    public String toString() {
        if(Character.isWhitespace(value)) {
            String result = "#\\";
            switch(value) {
            case '\n':
                result += Constants.SCHEME_CHAR_NEWLINE;
                break;
            case ' ':
                result += Constants.SCHEME_CHAR_SPACE;
                break;
            }
            return result;
        }
        else
            return "#\\" + value;
    }
}
