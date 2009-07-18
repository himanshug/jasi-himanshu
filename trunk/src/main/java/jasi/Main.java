package jasi;

import jasi.datatype.SVariable;
import jasi.parser.Reader;
import jasi.semantics.*;
import jasi.semantics.procedure.PrimitiveProcedure;

import java.io.FileInputStream;
import java.io.InputStream;
import java.util.logging.Logger;

public class Main {

    //todo:
    //macro-expand
    //eval

    private final static Logger log = Logger.getLogger(Main.class.getName());
    public static Reader reader = new Reader(System.in);

    public static void main(String[] args) {

        Environment theGlobalEnv = createGlobalEnv();
        
        //load the scheme lib
        String libFile = "preloadedscm.scm";
        InputStream in = Main.class.getClassLoader().getResourceAsStream(libFile);
        if(in == null) {
            throw new RuntimeException(libFile + " not readable.");
        }
        Reader tmpReader = new Reader(in);
        Object exp = tmpReader.read();
        while(exp != null) {
            Scheme.eval(exp, theGlobalEnv);
            exp = tmpReader.read();
        }
        //now the user prompt
        String prompt = ">";
        while(true) {
            System.out.print(prompt);
            System.out.flush();
            System.out.println(Scheme.eval(reader.read(), theGlobalEnv));
        }
    }

    private static Environment createGlobalEnv() {
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
        return env;
    }
}
