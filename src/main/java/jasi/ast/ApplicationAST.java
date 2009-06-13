package jasi.ast;

import java.util.ArrayList;
import java.util.List;

public class ApplicationAST extends AST {
    private VariableAST variable;
    private List<AST> expressions;

    public ApplicationAST() {
        super();
        expressions = new ArrayList<AST>();
    }

    public VariableAST getVariable() {
        return variable;
    }

    public void setVariable(VariableAST variable) {
        this.variable = variable;
    }

    public List<AST> getExpressions() {
        return expressions;
    }

    public void addExpr(AST ast) {
        expressions.add(ast);
    }
    
    public String toString() {
        String result = "(" + variable.toString();
        for(AST ast : expressions) {
            result += (" " + ast.toString());
        }
        result += ")";
        return result;
    }
}
