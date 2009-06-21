package jasi;

import java.util.ArrayList;

import jasi.semantics.Utils;

public class SchemeReader {

    public static Object read() {
        Token currentToken = Tokenizer.getNextToken();
        switch(currentToken.getType()) {
            //todo: support booleans, pair
            case Constants.TOKEN_TYPE_CHAR:
                return Utils.readSchemeChar(currentToken.getSpelling());
            case Constants.TOKEN_TYPE_NUMBER:
                return Utils.readSchemeNumber(currentToken.getSpelling());
            case Constants.TOKEN_TYPE_STRING:
                return Utils.readSchemeString(currentToken.getSpelling());
            case Constants.TOKEN_TYPE_VARIABLE:
                return Utils.readSchemeVariable(currentToken.getSpelling());
            case Constants.TOKEN_TYPE_LPAREN:
                return readPair();
            default:
                throw new RuntimeException("could not recognize token: " + currentToken.toString());
        }
    }

    //by the time we reach here, left paren has been read.
    private static Object readPair() {
        System.out.println("came to read pair");
        Pair p = null;
        Token token = Tokenizer.peekNextToken();
        if(token.getType() != Constants.TOKEN_TYPE_RPAREN) {
            if(token.getSpelling().equals("."))
                throw new RuntimeException("ill formed dotted list");
            else {
                p = new Pair(read(), null);
                token = Tokenizer.peekNextToken();
                if(token.getType() != Constants.TOKEN_TYPE_RPAREN) {
                    if(token.getSpelling().equals(".")) {
                        //consume the dot
                        Tokenizer.getNextToken();
                        p.setCdr(read());
                        if(Tokenizer.getNextToken().getType() != Constants.TOKEN_TYPE_RPAREN)
                            throw new RuntimeException("ill formed dotted list");
                    }
                    else
                        p.setCdr(readPair());
                }
                else //just consume the right paren
                    Tokenizer.getNextToken();
            }
        }
        else //consume the right paren and return empty list
            Tokenizer.getNextToken();

        return p;
    }

}
