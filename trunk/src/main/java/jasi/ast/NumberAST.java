package jasi.ast;

import jasi.parser.Token;

public class NumberAST extends AST {
    private Token token;

    public NumberAST(Token t) {
        super();
        this.token = t;
    }

    public Token getToken() {
        return token;
    }

    public void setToken(Token token) {
        this.token = token;
    }

    public String toString() {
        return token.getSpelling();
    }
}
