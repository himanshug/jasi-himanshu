package jasi.semantics;

import java.util.ArrayList;
import java.util.List;

import jasi.semantics.procedure.PrimitiveProcedure;

public class Scheme {/*

    public Object eval(Object exp, Environment env) {
        if(isSelfEvaluating(exp)){
            return selfEvaluateValue(exp);
        }
        else if(isVariable(exp)) {
            return env.getVariableValue(getVariableName(exp));
        }
        /else if(isQuoted(exp)) {
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
        }
        else if(isApplication(exp)) {
            return apply(eval(operator(exp),env), operands(exp, env), env);
        }
        else throw new RuntimeException("ast is not recognized: " + exp);
    }

    //self evaluating
    private boolean isSelfEvaluating(Object exp) {
        return ((ast instanceof CharAST) ||
                (ast instanceof NumberAST) ||
                (ast instanceof StringAST));
    }

    private Object selfEvaluateValue(Object exp) {
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
    private boolean isVariable(Object exp) {
        return (ast instanceof VariableAST);
    }

    private String getVariableName(Object exp) {

    }

    
    
    
    
    //application
    private boolean isApplication(Object exp) {

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
            return ((PrimitiveProcedure)proc).apply(args, env);
        }
        //else if compound..
        //..
        else
            throw new RuntimeException("not a procedure : " + proc);
    }*/
}
