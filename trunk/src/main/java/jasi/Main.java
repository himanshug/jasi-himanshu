package jasi;

import jasi.datatype.SVariable;
import jasi.parser.Reader;
import jasi.semantics.*;
import jasi.semantics.procedure.PrimitiveProcedure;

import java.util.logging.Logger;

public class Main {

    //todo:
    //macro-expand
    //eval

    private final static Logger log = Logger.getLogger(Main.class.getName());

    public static void main(String[] args) {

        Environment theGlobalEnv = createGlobalEnv();
        String prompt = ">";
        while(true) {
            System.out.print(prompt);
            System.out.flush();
            System.out.println(Scheme.eval(Reader.read(), theGlobalEnv));
        }
    }

    private static Environment createGlobalEnv() {
        Environment env = new Environment();
        
        env.putBinding(SVariable.getInstance("+"), new PrimitiveProcedure(PrimitiveProcedure.PLUS));
        
        //logical operators
        env.putBinding(SVariable.getInstance("not"), new PrimitiveProcedure(PrimitiveProcedure.NOT));
        
        env.putBinding(SVariable.getInstance("make-string"), new PrimitiveProcedure(PrimitiveProcedure.MAKE_STRING));
        
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
        
        return env;
    }
}
