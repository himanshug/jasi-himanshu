package jasi.semantics;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import jasi.datatype.SVariable;
import jasi.semantics.procedure.PrimitiveProcedure;

public class Environment {

    //the interactive environment that use types in the
    //commands
    private static Environment theGlobalEnv = null;
    
    //prepare the global environment at load time
    static {
        initGlobalEnv();
    }

    Map<SVariable, Object> bindings = new HashMap<SVariable, Object>();

    //parent environment if any, created at the time of application of a compound prodedure
    //by using extendEnvironment method
    private Environment parent;

    public Environment() {}

    private Environment(Environment parent) {
        this.parent = parent;
    }

    //puts a new binding
    public void putBinding(SVariable sv, Object o) {
        bindings.put(sv, o);
    }

    //changes already defined binding
    public void setBinding(SVariable sv, Object o) {
        if(bindings.containsKey(sv))
            bindings.put(sv, o);
        else {
            if(parent != null)
                parent.setBinding(sv, o);
            else
                throw new RuntimeException("variable " + sv.toString() + " is unbound.");
        }
    }

    //gets a binding
    public Object getBinding(SVariable sv) {
        if(bindings.containsKey(sv))
            return bindings.get(sv);
        else {
            if(parent != null)
                return parent.getBinding(sv);
            else return null; //null means no binding is available.
        }
    }

    public Environment extendEnvironment() {
        return new Environment(this);
    }

    public Environment extendEnvironment(List<SVariable> variables, List<Object> values) {
        int size = variables.size();
        if(size != values.size())
            throw new RuntimeException("size mismatch between variables and values.");
        
        Environment result = new Environment(this);
        for(int i =0; i < size; i++) {
            result.putBinding(variables.get(i), values.get(i));
        }
        return result;
    }

    public static Environment getGlobalEnvironment() {
        return theGlobalEnv;
    }

    private static void initGlobalEnv() {
        Environment env = new Environment();
        
        env.putBinding(SVariable.getInstance("+"), new PrimitiveProcedure(PrimitiveProcedure.PLUS));
        env.putBinding(SVariable.getInstance("-"), new PrimitiveProcedure(PrimitiveProcedure.MINUS));
        env.putBinding(SVariable.getInstance("*"), new PrimitiveProcedure(PrimitiveProcedure.MULTIPLICATION));
        env.putBinding(SVariable.getInstance("/"), new PrimitiveProcedure(PrimitiveProcedure.DIVISION));
        
        //logical operators
        env.putBinding(SVariable.getInstance("not"), new PrimitiveProcedure(PrimitiveProcedure.NOT));
        env.putBinding(SVariable.getInstance(">"), new PrimitiveProcedure(PrimitiveProcedure.GT));
        env.putBinding(SVariable.getInstance("<"), new PrimitiveProcedure(PrimitiveProcedure.LT));
        env.putBinding(SVariable.getInstance(">="), new PrimitiveProcedure(PrimitiveProcedure.GT_EQ));
        env.putBinding(SVariable.getInstance("<="), new PrimitiveProcedure(PrimitiveProcedure.LT_EQ));
        
        env.putBinding(SVariable.getInstance("make-string"), new PrimitiveProcedure(PrimitiveProcedure.MAKE_STRING));
        env.putBinding(SVariable.getInstance("symbol->string"), new PrimitiveProcedure(PrimitiveProcedure.SYMBOL_TO_STRING));
        env.putBinding(SVariable.getInstance("string->symbol"), new PrimitiveProcedure(PrimitiveProcedure.STRING_TO_SYMBOL));
        env.putBinding(SVariable.getInstance("string-length"), new PrimitiveProcedure(PrimitiveProcedure.STRING_LENGTH));
        env.putBinding(SVariable.getInstance("string-ref"), new PrimitiveProcedure(PrimitiveProcedure.STRING_REF));
        env.putBinding(SVariable.getInstance("string-set!"), new PrimitiveProcedure(PrimitiveProcedure.STRING_SET));
        env.putBinding(SVariable.getInstance("number->string"), new PrimitiveProcedure(PrimitiveProcedure.NUMBER_TO_STRING));
        env.putBinding(SVariable.getInstance("string->number"), new PrimitiveProcedure(PrimitiveProcedure.STRING_TO_NUMBER));
        
        env.putBinding(SVariable.getInstance("char-upcase"), new PrimitiveProcedure(PrimitiveProcedure.CHAR_UPCASE));
        env.putBinding(SVariable.getInstance("char-downcase"), new PrimitiveProcedure(PrimitiveProcedure.CHAR_DOWNCASE));
        env.putBinding(SVariable.getInstance("char<?"), new PrimitiveProcedure(PrimitiveProcedure.CHAR_LT));
        env.putBinding(SVariable.getInstance("char-alphabetic?"), new PrimitiveProcedure(PrimitiveProcedure.CHAR_PRED_ALPHABETIC));
        env.putBinding(SVariable.getInstance("char-numeric?"), new PrimitiveProcedure(PrimitiveProcedure.CHAR_PRED_NUMERIC));
        env.putBinding(SVariable.getInstance("char-whitespace?"), new PrimitiveProcedure(PrimitiveProcedure.CHAR_PRED_WHITESPACE));
        env.putBinding(SVariable.getInstance("char-lower-case?"), new PrimitiveProcedure(PrimitiveProcedure.CHAR_PRED_LOWERCASE));
        env.putBinding(SVariable.getInstance("char-upper-case?"), new PrimitiveProcedure(PrimitiveProcedure.CHAR_PRED_UPPERCASE));
        
        //predicates
        env.putBinding(SVariable.getInstance("eq?"), new PrimitiveProcedure(PrimitiveProcedure.PRED_EQ));
        env.putBinding(SVariable.getInstance("eqv?"), new PrimitiveProcedure(PrimitiveProcedure.PRED_EQV));
        env.putBinding(SVariable.getInstance("equal?"), new PrimitiveProcedure(PrimitiveProcedure.PRED_EQUAL));
        env.putBinding(SVariable.getInstance("boolean?"), new PrimitiveProcedure(PrimitiveProcedure.PRED_BOOLEAN));
        env.putBinding(SVariable.getInstance("char?"), new PrimitiveProcedure(PrimitiveProcedure.PRED_CHAR));
        env.putBinding(SVariable.getInstance("string?"), new PrimitiveProcedure(PrimitiveProcedure.PRED_STRING));
        env.putBinding(SVariable.getInstance("number?"), new PrimitiveProcedure(PrimitiveProcedure.PRED_NUMBER));
        env.putBinding(SVariable.getInstance("procedure?"), new PrimitiveProcedure(PrimitiveProcedure.PRED_PROCEDURE));
        env.putBinding(SVariable.getInstance("pair?"), new PrimitiveProcedure(PrimitiveProcedure.PRED_PAIR));
        env.putBinding(SVariable.getInstance("list?"), new PrimitiveProcedure(PrimitiveProcedure.PRED_LIST));
        env.putBinding(SVariable.getInstance("null?"), new PrimitiveProcedure(PrimitiveProcedure.PRED_NULL));
        env.putBinding(SVariable.getInstance("symbol?"), new PrimitiveProcedure(PrimitiveProcedure.PRED_SYMBOL));
        
        //pair/list related
        env.putBinding(SVariable.getInstance("cons"), new PrimitiveProcedure(PrimitiveProcedure.CONS));
        env.putBinding(SVariable.getInstance("car"), new PrimitiveProcedure(PrimitiveProcedure.CAR));
        env.putBinding(SVariable.getInstance("cdr"), new PrimitiveProcedure(PrimitiveProcedure.CDR));
        env.putBinding(SVariable.getInstance("set-car!"), new PrimitiveProcedure(PrimitiveProcedure.SET_CAR));
        env.putBinding(SVariable.getInstance("set-cdr!"), new PrimitiveProcedure(PrimitiveProcedure.SET_CDR));
        env.putBinding(SVariable.getInstance("list"), new PrimitiveProcedure(PrimitiveProcedure.LIST));
        
        //output related
        env.putBinding(SVariable.getInstance("write"), new PrimitiveProcedure(PrimitiveProcedure.WRITE));
        env.putBinding(SVariable.getInstance("display"), new PrimitiveProcedure(PrimitiveProcedure.DISPLAY));
        env.putBinding(SVariable.getInstance("newline"), new PrimitiveProcedure(PrimitiveProcedure.NEWLINE));
        
        //read/eval/apply
        env.putBinding(SVariable.getInstance("read"), new PrimitiveProcedure(PrimitiveProcedure.READ));
        env.putBinding(SVariable.getInstance("apply"), new PrimitiveProcedure(PrimitiveProcedure.APPLY));
        //todo: eval
        env.putBinding(SVariable.getInstance("eval"), new PrimitiveProcedure(PrimitiveProcedure.EVAL));
        
        env.putBinding(SVariable.getInstance("error"), new PrimitiveProcedure(PrimitiveProcedure.ERROR));
        env.putBinding(SVariable.getInstance("interaction-environment"), new PrimitiveProcedure(PrimitiveProcedure.INTERACTION_ENVIRONMENT));
        theGlobalEnv = env;
    }
}
