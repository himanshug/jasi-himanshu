package jasi.parser;

public class Token {

    private final int type;
    private final String spelling;

    public Token(int type, String spelling) {
        this.type = type;
        this.spelling = spelling;
    }

    public int getType() {
        return type;
    }

    public String getSpelling() {
        return spelling;
    }

    public String toString() {
        return "Token: type :" + type + ":spelling :" + spelling +":";
      }
}
