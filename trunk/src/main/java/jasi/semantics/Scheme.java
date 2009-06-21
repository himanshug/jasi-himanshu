package jasi.semantics;

import java.util.ArrayList;

import jasi.datatype.SNumber;
import jasi.datatype.SPair;
import jasi.datatype.SVariable;
import jasi.semantics.procedure.PrimitiveProcedure;

public class Scheme {

    public Object eval(Object exp, Environment env) {
        if(isSelfEvaluating(exp)){
            return selfEvaluateValue(exp);
        }
        else if(isVariable(exp)) {
            return getVariableValue(exp, env);
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
        else if(isApplication(exp)) {
            return apply((operator(exp, env)), 
                    operands(exp, env), 
                    env);
        }
        else throw new RuntimeException("exp is not recognized: " + exp);
    }

    //self evaluating
    private boolean isSelfEvaluating(Object exp) {
        //todo: support booleans
//        return (Utils.isSchemeChar(exp) ||
//                Utils.isSchemeNumber(exp) ||
//                Utils.isSchemeString(exp) ||
//                Utils.isSchemeBoolean(exp) ||
//                Utils.isEmptyList(exp));
        return (exp instanceof SNumber);
    }

    private Object selfEvaluateValue(Object exp) {
        return exp;
    }

    //variable
    private boolean isVariable(Object exp) {
        return (exp instanceof SVariable);
    }

    private Object getVariableValue(Object exp, Environment env) {
        return env.getVariableValue((SVariable)exp);
    }

    
    
    
    
    //application
    private boolean isApplication(Object exp) {
        return Utils.isSchemeList(exp);
    }

    private Object operator(Object exp, Environment env) {
        SPair p = (SPair)exp;
        Object o = p.getCar();
        if(o instanceof SVariable) {
            return env.getVariableValue((SVariable)o);
        }
        else {
            throw new RuntimeException("can not apply :" + o.toString());
        }
    }

    private Object operands(Object exp, Environment env) {
            ArrayList operands = null;
            SPair p = (SPair)((SPair)exp).getCdr();
            while(!Utils.isEmptyList(p)) {
                if(operands == null) operands = new ArrayList();
                operands.add(eval(p.getCar(), env));
                p = ((SPair)p.getCdr());
            }
            return operands;
    }

    private Object apply(Object proc, Object args, Environment env) {
        if(proc instanceof PrimitiveProcedure) {
            return ((PrimitiveProcedure)proc).apply(args, env);
        }
        //else if compound procedure..
        //else if macro..
        else
            throw new RuntimeException("not a procedure : " + proc);
    }
}
