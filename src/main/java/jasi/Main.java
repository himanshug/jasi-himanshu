package jasi;

import jasi.datatype.SVariable;
import jasi.parser.Reader;
import jasi.semantics.*;
import jasi.semantics.procedure.PrimitiveProcedure;

import java.util.logging.Logger;

public class Main {

    private final static Logger log = Logger.getLogger(Main.class.getName());

    public static void main(String[] args) {

        Environment theGlobalEnv = createGlobalEnv();
        String prompt = ">";
        while(true) {
            System.out.print(prompt);
            System.out.println(Scheme.eval(Reader.read(), theGlobalEnv));
        }
    }

    private static Environment createGlobalEnv() {
        Environment env = new Environment();
        
        env.putBinding(SVariable.getInstance("+"), new PrimitiveProcedure(PrimitiveProcedure.PLUS));
        env.putBinding(SVariable.getInstance("read"), new PrimitiveProcedure(PrimitiveProcedure.READ));
        return env;
    }
}
