package jasi.ast;

import jasi.parser.Token;

public class IfAST extends AST {
    private Token token;
    private AST testExpr;
    private AST thenExpr;
    private AST elseExpr;
    
    public IfAST(Token t) {
        super();
        this.token = t;
    }
    
    public AST getTestExpr() {
        return testExpr;
    }
    
    public void setTestExpr(AST testExpr) {
        this.testExpr = testExpr;
    }
    public AST getThenExpr() {
        return thenExpr;
    }
    
    public void setThenExpr(AST thenExpr) {
        this.thenExpr = thenExpr;
    }
    
    public AST getElseExpr() {
        return elseExpr;
    }
    
    public void setElseExpr(AST elseExpr) {
        this.elseExpr = elseExpr;
    }

    public String toString() {
        String result = "(" + token.getSpelling() + " " + testExpr.toString() + " " +
            thenExpr.toString();
        if(elseExpr != null) {
            result += (" " + elseExpr.toString() + ")");
        }
        else result += ")";
        return result;
    }
}
