package jasi.parser;

import java.io.InputStream;
import java.util.logging.Logger;

import jasi.Constants;
import jasi.datatype.SBoolean;
import jasi.datatype.SChar;
import jasi.datatype.SEmptyList;
import jasi.datatype.SNumber;
import jasi.datatype.SPair;
import jasi.datatype.SString;
import jasi.datatype.SVariable;
import jasi.semantics.Utils;

public class Reader {

    private final static Logger log = Logger.getLogger(Reader.class.getName());
    private Tokenizer tokenizer;

    public Reader(InputStream in) {
        tokenizer = new Tokenizer(in);
    }

    public Object read() {
        Object result = null;
        Token currentToken = tokenizer.getNextToken();
        if(currentToken == null) return null;
        switch(currentToken.getType()) {
            case Constants.TOKEN_TYPE_CHAR:
                result = new SChar(currentToken.getSpelling());
                log.finer("read scheme character:" + result);
                break;
            case Constants.TOKEN_TYPE_BOOLEAN:
                result = SBoolean.getInstance((currentToken.getSpelling()));
                log.finer("read scheme boolean:" + result);
                break;
            case Constants.TOKEN_TYPE_NUMBER:
                result = new SNumber(currentToken.getSpelling());
                log.finer("read scheme number:" + result);
                break;
            case Constants.TOKEN_TYPE_STRING:
                //remove leading and trailing double quotes from the token
                //spelling
                String tmp = currentToken.getSpelling();
                tmp = tmp.substring(1, tmp.length()-1);
                result = new SString(tmp);
                log.finer("read scheme string:" + result);
                return result;
            case Constants.TOKEN_TYPE_VARIABLE:
                result = SVariable.getInstance(currentToken.getSpelling());
                log.finer("read scheme variable:" + result);
                break;
            case Constants.TOKEN_TYPE_QUOTE:
                log.finer("start reading quoted...");
                SPair tmp1 = new SPair();
                tmp1.setCar(SVariable.getInstance(Constants.KEYWORD_QUOTE));
                SPair tmp2 = new SPair();
                tmp1.setCdr(tmp2);
                tmp2.setCar(read());
                tmp2.setCdr(SEmptyList.getInstance());
                result = tmp1;
                log.finer("finished reading quoted: " + result);
                return result;
            case Constants.TOKEN_TYPE_LPAREN:
                log.finer("start reading pair...");
                result = readSPair();
                log.finer("finished reading pair: " + result);
                break;
            default:
                throw new RuntimeException("could not recognize token: " + currentToken.toString());
        }
        return result;
    }

    //by the time we reach here, left paren has been read.
    private Object readSPair() {
        SPair p = null;
        Token token = tokenizer.peekNextToken();
        if(token.getType() != Constants.TOKEN_TYPE_RPAREN) {
            if(token.getSpelling().equals("."))
                throw new RuntimeException("ill formed dotted list");
            else {
                p = new SPair(read(), SEmptyList.getInstance());
                token = tokenizer.peekNextToken();
                if(token.getType() != Constants.TOKEN_TYPE_RPAREN) {
                    if(token.getSpelling().equals(".")) {
                        //consume the dot
                        tokenizer.getNextToken();
                        p.setCdr(read());
                        if(tokenizer.getNextToken().getType() != Constants.TOKEN_TYPE_RPAREN)
                            throw new RuntimeException("ill formed dotted list");
                    }
                    else
                        p.setCdr(readSPair());
                }
                else //just consume the right paren
                    tokenizer.getNextToken();
            }
        }
        else {
            //consume the right paren and return empty list
            tokenizer.getNextToken();
            return SEmptyList.getInstance();
        }

        return p;
    }

}
