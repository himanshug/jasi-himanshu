package jasi;

/**
 * Each token is one of terminal from the grammar
 * 
 * @author himanshu <g.himanshu@gmail.com>
 */
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
