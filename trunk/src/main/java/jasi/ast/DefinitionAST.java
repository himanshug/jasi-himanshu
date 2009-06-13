package jasi.ast;

import jasi.parser.Token;

public class DefinitionAST extends AST {

    private Token token;
    private VariableAST variable;
    private AST expr;

    public DefinitionAST(Token t) {
        this.token = t;
    }

    public VariableAST getVariable() {
        return variable;
    }

    public void setVariable(VariableAST variable) {
        this.variable = variable;
    }

    public AST getExpr() {
        return expr;
    }

    public void setExpr(AST expr) {
        this.expr = expr;
    }

    public String toString() {
        return "(" + token.getSpelling() + " " + variable.toString() + " " +
            expr.toString() + ")";
    }
}
