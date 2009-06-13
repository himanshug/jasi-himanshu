package jasi.ast;

import jasi.parser.Token;

public class CharAST extends AST {
    private Token token;

    public CharAST(Token t) {
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
