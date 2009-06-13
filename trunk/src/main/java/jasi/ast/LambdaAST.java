package jasi.ast;

import java.util.ArrayList;
import java.util.List;

import jasi.parser.Token;

public class LambdaAST extends AST {

    private Token token;
    private List<VariableAST> params;
    private List<AST> body;

    public LambdaAST(Token t) {
        super();
        this.token = t;
        params = new ArrayList<VariableAST>();
        body = new ArrayList<AST>();
    }

    public List<VariableAST> getParams() {
        return params;
    }

    public List<AST> getBody() {
        return body;
    }

    public void addParam(VariableAST param) {
        params.add(param);
    }

    public void addBodyStatement(AST stmt) {
        body.add(stmt);
    }

    public String toString() {
        String result = "(" + token.getSpelling() + " (";
        for(VariableAST param : params) {
            result += (param.toString() + " ");
        }
        result += (")");
        for(AST stmt : body) {
            result += (" " + stmt.toString());
        }
        result += (")");
        return result;
    }
}
