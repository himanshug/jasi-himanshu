package jasi;
public class Constants {

    //token type constants
    public final static int TOKEN_TYPE_CHAR = 0;
    public final static int TOKEN_TYPE_NUMBER = TOKEN_TYPE_CHAR + 1;
    public final static int TOKEN_TYPE_STRING = TOKEN_TYPE_NUMBER + 1;
    public final static int TOKEN_TYPE_BOOLEAN = TOKEN_TYPE_STRING + 1;
    public final static int TOKEN_TYPE_LPAREN = TOKEN_TYPE_BOOLEAN + 1;
    public final static int TOKEN_TYPE_RPAREN = TOKEN_TYPE_LPAREN + 1;
    public final static int TOKEN_TYPE_VARIABLE = TOKEN_TYPE_RPAREN + 1;

    //various language key-words
    public final static String KEYWORD_QUOTE = "quote";
    public final static String KEYWORD_ASSIGNMENT = "set!";
    public final static String KEYWORD_DEFINITION = "define";
    public final static String KEYWORD_BEGIN = "begin";
    public final static String KEYWORD_LAMBDA = "lambda";
    public final static String KEYWORD_IF = "if";
}
