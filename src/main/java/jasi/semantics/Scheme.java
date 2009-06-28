package jasi.semantics;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

import jasi.Constants;
import jasi.datatype.SBoolean;
import jasi.datatype.SChar;
import jasi.datatype.SEmptyList;
import jasi.datatype.SNumber;
import jasi.datatype.SPair;
import jasi.datatype.SString;
import jasi.datatype.SUndefined;
import jasi.datatype.SVariable;
import jasi.semantics.procedure.CompoundProcedure;
import jasi.semantics.procedure.Procedure;

public class Scheme {

    private final static Logger log = Logger.getLogger(Scheme.class.getName());

    public static Object eval(Object exp, Environment env) {
        log.fine("evaluating: " + exp);
        if(isSelfEvaluating(exp)){
            return selfEvaluateValue(exp);
        }
        else if(isVariable(exp)) {
            return getVariableValue(exp, env);
            }
        else if(isQuoted(exp)) {
            return evalQuoted(exp);
        }
        else if(isAssignment(exp)) {
            return evalAssignment(exp, env);
            }
        else if(isDefinition(exp)) {
            return evalDefinition(exp, env);
            }
        else if(isIf(exp)) {
            return evalIf(exp, env);
        }
        else if(isLambda(exp)) {
            return makeProcedure(lambdaParams(exp),lambdaBody(exp), env);
        }
        else if(isBegin(exp)) {
            return evalSequence(exp, env);
        }
        else if(isApplication(exp)) {
            return apply((operator(exp, env)), 
                    operands(exp, env), 
                    env);
        }
        else throw new RuntimeException("exp is not recognized: " + exp);
    }

    //self evaluating
    private static boolean isSelfEvaluating(Object exp) {
        return  ((exp instanceof SEmptyList) ||
                (exp instanceof SNumber) ||
                (exp instanceof SChar) ||
                (exp instanceof SString) ||
                (exp instanceof SBoolean));
    }

    private static Object selfEvaluateValue(Object exp) {
        return exp;
    }

    //variable
    private static boolean isVariable(Object exp) {
        return (exp instanceof SVariable);
    }

    private static Object getVariableValue(Object exp, Environment env) {
        SVariable sv = (SVariable)exp;
        Object o = env.getBinding(sv);
        if (o != null) return o;
        else
            throw new RuntimeException("variable " + sv.toString() + " is unbound.");
    }

    //quote
    private static boolean isQuoted(Object exp) {
        return isLanguageStmt(exp, Constants.KEYWORD_QUOTE);
    }

    private static Object evalQuoted(Object exp) {
        return Utils.rest(exp);
    }

    //assignment
    private static boolean isAssignment(Object exp) {
        return isLanguageStmt(exp, Constants.KEYWORD_ASSIGNMENT);
    }

    private static Object assignmentVariable(Object exp) {
        return Utils.cadr(exp);
    }

    private static Object assignmentValue(Object exp) {
        return Utils.caddr(exp);
    }

    private static Object evalAssignment(Object exp, Environment env) {
        env.setBinding((SVariable)assignmentVariable(exp), eval(assignmentValue(exp), env));
        return SUndefined.getInstance();
    }

    //definition
    private static boolean isDefinition(Object exp) {
        return isLanguageStmt(exp, Constants.KEYWORD_DEFINITION);
    }

    private static Object definitionVariable(Object exp) {
        return Utils.cadr(exp);
    }

    private static Object definitionValue(Object exp) {
        return Utils.caddr(exp);
    }

    private static Object evalDefinition(Object exp, Environment env) {
        env.putBinding((SVariable)definitionVariable(exp), eval(definitionVariable(exp), env));
        return SUndefined.getInstance();
    }

    //if
    private static boolean isIf(Object exp) {
        return isLanguageStmt(exp, Constants.KEYWORD_IF);
    }

    private static Object ifPredicate(Object ifExp) {
        return Utils.cadr(ifExp);
    }

    private static Object ifConsequent(Object ifExp) {
        return Utils.caddr(ifExp);
    }

    private static Object ifAlternative(Object ifExp) {
        return Utils.cadddr(ifExp);
    }

    private static Object evalIf(Object exp, Environment env) {
        Object tmp = eval(ifPredicate(exp), env);
        if(Utils.isTrue(tmp))
            return eval(ifConsequent(exp), env);
        else
            return eval(ifAlternative(exp), env);
    }

    //begin -- sequence
    private static boolean isBegin(Object exp) {
        return isLanguageStmt(exp, Constants.KEYWORD_BEGIN);
    }

    private static Object evalSequence(Object exp, Environment env) {
        Object result = null;
        Object o = Utils.rest(exp);
        while(!(o instanceof SEmptyList)) {
            if(o instanceof SPair) {
                SPair sp = (SPair)o;
                result = eval(sp.getCar(), env);
                o = sp.getCdr();
            }
            else
                throw new RuntimeException("begin expression has to be a valid scheme list,failed " +
                        "to evaluate sequence: " + exp);
        }
        return result;
    }

    //lambda
    private static boolean isLambda(Object exp) {
        return isLanguageStmt(exp, Constants.KEYWORD_LAMBDA);
    }

    //note: right now we only support fixed length argument
    //procedure
    private static List<SVariable> lambdaParams(Object exp) {
        log.fine("extracting params of lambda exp: " + exp );
        ArrayList<SVariable> params = null;
        Object tmp = Utils.first(Utils.rest(exp));
        while(!(tmp instanceof SEmptyList)) {
            Object o = Utils.first(tmp);
            Utils.validateType(o, SVariable.class);
            if(params == null) params = new ArrayList<SVariable>();
            params.add((SVariable)o);
            
            tmp = Utils.rest(o);
        }
        return params;
    }

    private static Object lambdaBody(Object exp) {
        log.fine("extracting body of lambda exp: " + exp );
        return Utils.rest(Utils.rest(exp));
    }

    private static Object makeProcedure(List<SVariable> params, Object body,
                                        Environment env) {
        return new CompoundProcedure(params, body, env);
    }

    //application
    private static boolean isApplication(Object exp) {
        log.fine("checking if :" + exp + ": is an application stmt.");
        return Utils.isSchemeList(exp);
    }

    private static Object operator(Object exp, Environment env) {
        Object o = Utils.first(exp);
        if(o instanceof SVariable) {
            return env.getBinding((SVariable)o);
        }
        else {
            throw new RuntimeException("can not apply :" + o.toString());
        }
    }

    private static Object operands(Object exp, Environment env) {
            ArrayList operands = null;
            Object o = Utils.rest(exp);
            while(!(o instanceof SEmptyList)) {
                if(operands == null) operands = new ArrayList();
                operands.add(eval(Utils.first(o), env));
                o = Utils.rest(o);
            }
            return operands;
    }

    private static Object apply(Object proc, Object args, Environment env) {
        if(proc instanceof Procedure) {
            return ((Procedure)proc).apply(args, env);
        }
        //else if macro..
        else
            throw new RuntimeException("not a procedure : " + proc);
    }

    //checks if given expression is a statement of the language
    //with given keyword
    //e.g. a definition statement exp will return true for isLanguageStmt(exp, "define")
    private static boolean isLanguageStmt(Object exp, String keyword) {
        log.fine("checking if :" + exp + ": is a scheme stmt with keyword: " + keyword);
        Object o = Utils.first(exp);
        return ((o instanceof SVariable) &&
                ((SVariable)o).getName().equalsIgnoreCase(keyword));
    }
}
