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
        else if(isIf(exp)) {
            return evalIf(exp, env);
        }
        //cond is a derived expression
        else if(isCond(exp)) {
            return eval(condToIf(exp), env);
        }
        else if(isBegin(exp)) {
            return evalSequence(exp, env);
        }
        //let is a derived expression
        else if(isLet(exp)) {
            return eval(letToLambda(exp), env);
        }
        else if(isLambda(exp)) {
            return makeProcedure(lambdaParams(exp),lambdaBody(exp), env);
        }
        else if(isDefinition(exp)) {
            return evalDefinition(exp, env);
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
                (exp instanceof SBoolean) ||
                (exp instanceof SUndefined));
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
        return Utils.cadr(exp);
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
        Object o = Utils.cdddr(ifExp);
        if(o instanceof SEmptyList)
            return SUndefined.getInstance();
        else return Utils.car(o);
    }

    private static Object evalIf(Object exp, Environment env) {
        Object tmp = eval(ifPredicate(exp), env);
        if(Utils.isTrue(tmp))
            return eval(ifConsequent(exp), env);
        else
            return eval(ifAlternative(exp), env);
    }

    //cond
    private static boolean isCond(Object exp) {
        return isLanguageStmt(exp, Constants.KEYWORD_COND);
    }

    private static Object condToIf(Object exp) {
        /*
         * (cond  ((pred1) <work1>)
         *        ((pred2) <work2>)
         *         ....
         *        ((predn-1) <workn-1>)
         *        (else <workn>))
         *  ==>>
         *  (if (pred1) <work1>
         *      (if (pred2) <work2>
         *          .......
         *      (if (predn-1) <workn-1> <workn>))...) 
         */
        log.fine("converting cond exp to if ...");
        log.fine(exp.toString());
        Object clauses = Utils.cdr(exp);
        Object result = condToIfRecur(clauses);
        log.fine(result.toString());
        log.fine("converted cond exp to if ...");
        return result;
    }

    private static Object condToIfRecur(Object clauses) {
        if(clauses instanceof SEmptyList) {
            return SUndefined.getInstance();
        }
        else {
            Object aClause = Utils.car(clauses);
            if(isLanguageStmt(aClause, Constants.KEYWORD_ELSE)) {
                //its an else clause
                SVariable beginVar = SVariable.getInstance(Constants.KEYWORD_BEGIN);
                return Utils.cons(beginVar, Utils.cdr(aClause));
            }
            else {
                /*
                 * (list 'if (car aClause) (cons 'begin (cdr aClause)) --recurse--)
                 */
                SVariable ifVar = SVariable.getInstance(Constants.KEYWORD_IF);
                SVariable beginVar = SVariable.getInstance(Constants.KEYWORD_BEGIN);
                ArrayList arr = new ArrayList();
                arr.add(ifVar);
                arr.add(Utils.car(aClause));
                arr.add(Utils.cons(beginVar, Utils.cdr(aClause)));
                arr.add(condToIfRecur(Utils.cdr(clauses)));
                return Utils.list(arr);
            }
        }
    }

    //begin -- sequence
    private static boolean isBegin(Object exp) {
        return isLanguageStmt(exp, Constants.KEYWORD_BEGIN);
    }

    private static Object evalSequence(Object exp, Environment env) {
        Object result = SUndefined.getInstance();
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

    private static Object lambdaParams(Object exp) {
        log.fine("extracting params of lambda exp: " + exp );
        return Utils.cadr(exp);
    }

    private static Object lambdaBody(Object exp) {
        log.fine("extracting body of lambda exp: " + exp );
        return Utils.cddr(exp);
    }

    private static Object makeProcedure(Object params, Object body,
                                        Environment env) {
        return new CompoundProcedure(params, body, env);
    }

    //let
    private static boolean isLet(Object exp) {
        return isLanguageStmt(exp, Constants.KEYWORD_LET);
    }

    private static Object letToLambda(Object exp) {
        /*
         * (let ((var1 val1) (var2 val2) ... (varn valn))
         *      <body>)
         * ==>
         * ((lambda (var1 var2 ... varn) <body>) val1 val2 ... valn)
         */
        log.fine("converting let to lambda...");
        log.fine(exp.toString());
        Object vars_vals = Utils.cadr(exp);
        ArrayList vars = null;
        ArrayList vals = null;
        while(!(vars_vals instanceof SEmptyList)) {
            Object var_val = Utils.car(vars_vals);
            if(vars == null) {
                vars = new ArrayList();
                vals = new ArrayList();
            }
            vars.add(Utils.car(var_val));
            vals.add(Utils.cadr(var_val));
            vars_vals = Utils.cdr(vars_vals);
        }
        
        SVariable sv = SVariable.getInstance(Constants.KEYWORD_LAMBDA);
        //(cons (cons 'lambda (cons (list var1 var2 .. varn) <body>)) (list val1 val2 ... valn))
        Object result = Utils.cons(Utils.cons(sv, Utils.cons(Utils.list(vars), Utils.cddr(exp))),
                    Utils.list(vals));
        log.fine(result.toString());
        log.fine("converted let to lambda...");
        return result;
    }

    //definition
    private static boolean isDefinition(Object exp) {
        return isLanguageStmt(exp, Constants.KEYWORD_DEFINITION);
    }

    private static boolean isVariableDefinition(Object exp) {
        log.fine("checking if " + exp + " is a variable definition");
        return Utils.cadr(exp) instanceof SVariable;
    }

    private static Object variableDefinitionVariable(Object exp) {
        return Utils.cadr(exp);
    }

    private static Object variableDefinitionValue(Object exp) {
        return Utils.caddr(exp);
    }

    private static Object evalDefinition(Object exp, Environment env) {
        if(isVariableDefinition(exp))
            return evalVariableDefinition(exp, env);
        else
            return evalProcedureDefinition(exp, env);
    }

    private static Object evalVariableDefinition(Object exp, Environment env) {
        env.putBinding((SVariable)variableDefinitionVariable(exp), 
                eval(variableDefinitionValue(exp), env));
        return SUndefined.getInstance();
    }

    private static Object procedureDefinitionVariable(Object exp) {
        log.fine("extracting variable from procedure definition: " + exp);
        return Utils.caadr(exp);
    }

    private static Object procedureDefinitionParams(Object exp) {
        log.fine("extracting params from procedure definition: " + exp);
        return Utils.cdadr(exp);
    }

    private static Object procedureDefinitionBody(Object exp) {
        log.fine("extracting body from procedure definition: " + exp);
        return Utils.cddr(exp);
    }

    private static Object evalProcedureDefinition(Object exp, Environment env) {
        env.putBinding((SVariable)procedureDefinitionVariable(exp),
                makeProcedure(procedureDefinitionParams(exp),
                        procedureDefinitionBody(exp), env));
        return SUndefined.getInstance();
    }

    //application
    private static boolean isApplication(Object exp) {
        log.fine("checking if :" + exp + ": is an application stmt.");
        return Utils.isSchemeList(exp);
    }

    private static Object operator(Object exp, Environment env) {
        Object o = eval(Utils.first(exp),env);
        if(o instanceof Procedure) {
            return o;
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
