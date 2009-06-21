package jasi.parser;

import jasi.Constants;
import jasi.datatype.SBoolean;
import jasi.datatype.SChar;
import jasi.datatype.SNumber;
import jasi.datatype.SString;
import jasi.datatype.SVariable;

public class Reader {

    public static Object read() {
        Token currentToken = Tokenizer.getNextToken();
        switch(currentToken.getType()) {
            //todo: support booleans, pair
            case Constants.TOKEN_TYPE_CHAR:
                return new SChar(currentToken.getSpelling());
            case Constants.TOKEN_TYPE_BOOLEAN:
                return new SBoolean(currentToken.getSpelling());
            case Constants.TOKEN_TYPE_NUMBER:
                return new SNumber(currentToken.getSpelling());
            case Constants.TOKEN_TYPE_STRING:
                return new SString(currentToken.getSpelling());
            case Constants.TOKEN_TYPE_VARIABLE:
                return SVariable.getInstance(currentToken.getSpelling());
            case Constants.TOKEN_TYPE_LPAREN:
                return readSPair();
            default:
                throw new RuntimeException("could not recognize token: " + currentToken.toString());
        }
    }

    //by the time we reach here, left paren has been read.
    private static Object readSPair() {
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
                        p.setCdr(readSPair());
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
