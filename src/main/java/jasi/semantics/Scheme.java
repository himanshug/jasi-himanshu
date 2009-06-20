package jasi.semantics;

import java.util.ArrayList;
import java.util.List;

import jasi.ast.*;
import jasi.semantics.procedure.PrimitiveProcedure;

public class Scheme {

    public Object eval(Object o, Environment env) {
        AST ast = null;
        if(o instanceof AST)
            ast = (AST)o;
        else
            throw new RuntimeException("not an AST :" + o);

        if(isSelfEvaluating(ast)){
            return selfEvaluateValue(ast);
        }
        else if(isVariable(ast)) {
            return env.getVariableValue(getVariableName(ast));
        }
        /*else if(isQuoted(exp)) {
            exp.text;
        }
        else if(isAssignment(exp)) {
            evalAssignment(exp, env);
            }
        else if(isDefinition(exp)) {
            (evalDefinition(exp, env))
            }
        else if(isIf(exp)) {
            (evalIf(exp, env))
            }
        else if(isLambda(exp)) {
            makeProcedure(lambdaParameters(exp),lambdaBody(exp), env)
            }
        else if(isBegin(exp)) {
            evalSequence(exp, env);
        }*/
        else if(isApplication(ast)) {
            return apply(eval(operator(ast),env), operands(ast, env), env);
        }
        else throw new RuntimeException("ast is not recognized: " + ast);
    }

    //self evaluating
    private boolean isSelfEvaluating(AST ast) {
        return ((ast instanceof CharAST) ||
                (ast instanceof NumberAST) ||
                (ast instanceof StringAST));
    }

    private Object selfEvaluateValue(AST ast) {
        if(ast instanceof CharAST) {
            return new Character(((CharAST)ast).getToken().getSpelling().charAt(2));
        }
        else if(ast instanceof StringAST) {
            return new StringBuffer(((StringAST)ast).getToken().getSpelling());
        }
        else if(ast instanceof NumberAST) {
            return new Double(((NumberAST)ast).getToken().getSpelling());
        }
        else throw new RuntimeException("not a self-evaluating ast:" + ast);
    }

    //variable
    private boolean isVariable(AST ast) {
        return (ast instanceof VariableAST);
    }

    private String getVariableName(AST ast) {
        if(ast instanceof VariableAST) {
            return ((VariableAST)ast).getToken().getSpelling();
        }
        else throw new RuntimeException("not a variable:" + ast);
    }

    
    
    
    
    //application
    private boolean isApplication(AST ast) {
        return (ast instanceof ApplicationAST);
    }

    private Object operator(AST ast) {
        if (ast instanceof ApplicationAST)
            return ((ApplicationAST)ast).getVariable();
        else
            throw new RuntimeException("not an ApplicationAST :" + ast);
    }

    private Object operands(AST ast, Environment env) {
        if(ast instanceof ApplicationAST) {
            ArrayList<Object> operands = null;
            List<AST> operandASTs = ((ApplicationAST)ast).getExpressions();
            if(operandASTs.size() > 0) {
                operands = new ArrayList<Object>();
            }
                for(AST a : operandASTs) {
                    operands.add(eval(a, env));
                }
            return operands;
        }
        else
            throw new RuntimeException("not an ApplicationAST :" + ast);
    }

    private Object apply(Object proc, Object args, Environment env) {
        if(proc instanceof PrimitiveProcedure) {
            return ((PrimitiveProcedure)proc).apply(args);
        }
        //else if compound..
        //..
        else
            throw new RuntimeException("not a procedure : " + proc);
    }
}
