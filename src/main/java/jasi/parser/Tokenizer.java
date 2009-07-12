package jasi.parser;


import jasi.Constants;

import java.io.PushbackInputStream;
import java.util.logging.Logger;

/**
 * This reads characters from input stream and returns the tokens(terminals),
 * each token has a type associated with it.
 * Here we are implementing the lexer using a finite state machine.
 * 
 * @author hgupta <g.himanshu@gmail.com>
 *
 */
public class Tokenizer {

    private final static Logger log = Logger.getLogger(Tokenizer.class.getName());

    //Tokenizer has just been called to read a token
    private final static int ZERO_STATE = 0;
    //From ZERO_STATE we read a '#'
    private final static int HASH_ZERO_STATE = ZERO_STATE + 1;
    //From HASH_ZERO_STATE we read a '\' right after a '#'
    private final static int CHAR_ZERO_STATE = HASH_ZERO_STATE + 1;
    //Afte CHAR_ZERO_STATE we read any non-whitespace char
    private final static int CHAR_ONE_STATE = CHAR_ZERO_STATE + 1;
    //From ZERO_STATE we read a '"'
    private final static int STRING_ZERO_STATE = CHAR_ONE_STATE + 1;
    //From ZERO_STATE we read a digit
    private final static int NUMBER_ZERO_STATE = STRING_ZERO_STATE + 1;
    //start of a comment
    private final static int COMMENT_ZERO_STATE = NUMBER_ZERO_STATE + 1;
    //From ZERO_STATE, when we read any non-whitespace char
    //other than the ones described above.
    private final static int VARIABLE_ZERO_STATE = COMMENT_ZERO_STATE + 1;

    private static PushbackInputStream in = new PushbackInputStream(System.in);

    private static Token peekedToken;

    public static Token getNextToken() {
        Token t = null;
        if(peekedToken != null) {
            t = peekedToken;
            peekedToken = null;
        }
        else t = nextToken();
        log.finest("fetched token:" + t.toString());
        return t;
    }

    public static Token peekNextToken() {
        if(peekedToken == null)
            peekedToken = nextToken();
        log.finest("peeked token:" + peekedToken.toString());
        return peekedToken;
    }

    //reads next token from System input stream
    private static Token nextToken() {
        try {
            int i = -1;
            int state = ZERO_STATE;
            StringBuilder tokenBuilder = new StringBuilder();
            while((i = in.read()) != -1) {
                char c = (char)i;
                log.finest("found character :" + c + ": in state :" + state);
                switch(state) {
                    case ZERO_STATE:
                        if(c == '(') {
                            return new Token(Constants.TOKEN_TYPE_LPAREN,"(");
                        }
                        else if(c == ')') {
                            return new Token(Constants.TOKEN_TYPE_RPAREN,")");
                        }
                        else if(c == '\'') {
                            return new Token(Constants.TOKEN_TYPE_QUOTE,"'");
                        }
                        else if(c == ';') {
                            state = COMMENT_ZERO_STATE;
                            break;
                        }
                        else if(!isWhiteSpace(c)) {
                            switch(c) {
                                case '#':
                                    state = HASH_ZERO_STATE;
                                    break;
                                case '"':
                                    state = STRING_ZERO_STATE;
                                    break;
                                case '0':
                                case '1':
                                case '2':
                                case '3':
                                case '4':
                                case '5':
                                case '6':
                                case '7':
                                case '8':
                                case '9':
                                    state = NUMBER_ZERO_STATE;
                                    break;
                                default:
                                    state = VARIABLE_ZERO_STATE;
                            }
                            tokenBuilder.append(c);
                        }
                        break;
                    case COMMENT_ZERO_STATE:
                        if(isNewlineChar(c)) {
                            state = ZERO_STATE;
                        }
                        break;
                    case HASH_ZERO_STATE:
                        if(c == '\\') {
                            tokenBuilder.append(c);
                            state = CHAR_ZERO_STATE;
                            break;
                        }
                        else {
                            if(!isWhiteSpace(c))
                                tokenBuilder.append(c);
                            else
                                throw new RuntimeException("not valid # token.");
                            return new Token(Constants.TOKEN_TYPE_BOOLEAN, tokenBuilder.toString());
                        }
                    case CHAR_ZERO_STATE:
                        if(!isWhiteSpace(c)) {
                            tokenBuilder.append(c);
                            state = CHAR_ONE_STATE;
                        }
                        else {
                            if(isNewlineChar(c))
                                tokenBuilder.append(Constants.SCHEME_CHAR_NEWLINE);
                            else tokenBuilder.append(Constants.SCHEME_CHAR_SPACE);
                            return new Token(Constants.TOKEN_TYPE_CHAR, tokenBuilder.toString());
                        }
                        break;
                    case CHAR_ONE_STATE:
                        if(isWhiteSpace(c) || c == ')' || c == '(') {
                            in.unread(c);
                            return new Token(Constants.TOKEN_TYPE_CHAR, tokenBuilder.toString());
                        }
                        else 
                            tokenBuilder.append(c);
                        break;
                    case STRING_ZERO_STATE:
                        tokenBuilder.append(c);
                        if(c == '"')
                            return new Token(Constants.TOKEN_TYPE_STRING, tokenBuilder.toString());
                        break;
                    case NUMBER_ZERO_STATE:
                        if(isDigit(c))
                            tokenBuilder.append(c);
                        else if(isWhiteSpace(c) || c == ')') {
                            if(c == ')') in.unread(i);
                            return new Token(Constants.TOKEN_TYPE_NUMBER, tokenBuilder.toString());
                        }
                        else
                            throw new RuntimeException("expected a digit/)/whitespace char");
                        break;
                    case VARIABLE_ZERO_STATE:
                        if(isWhiteSpace(c) || c == ')') {
                            if(c == ')') in.unread(i);
                            return new Token(Constants.TOKEN_TYPE_VARIABLE, tokenBuilder.toString());
                        }
                        else tokenBuilder.append(c);
                        break;
                    default:
                        throw new RuntimeException("could not create token with:" + c +":");
                }
            }
            throw new RuntimeException("premature end of input");
        }
        catch(Exception ex) {
            throw new RuntimeException(ex);
        }
    }

    private static boolean isWhiteSpace(char c) {
        return Character.isWhitespace(c);
    }

    private static boolean isDigit(char c) {
        return Character.isDigit(c);
    }

    private static boolean isNewlineChar(char c) {
        String newline = System.getProperty("line.separator");
        return newline.indexOf(c) >= 0;
    }
}
