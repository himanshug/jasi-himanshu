package jasi;

import jasi.parser.Reader;
import jasi.semantics.*;
import java.io.InputStream;
import java.util.logging.Logger;

public class Main {

    //todo:
    //macro-expand

    private final static Logger log = Logger.getLogger(Main.class.getName());
    public static Reader reader = new Reader(System.in);

    public static void main(String[] args) {

        Environment theGlobalEnv = Environment.getGlobalEnvironment();
        
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
        //print welcome message
        System.out.println("");
        System.out.println("#################### JASI ####################" );
        System.out.println("####### Just Another Scheme Interpreter ######");
        System.out.println("### http://code.google.com/p/jasi-himanshu/ ##");
        System.out.println("@Author: Himanshu Gupta <g.himanshu@gmail.com>");
        System.out.println("");
        
        //now the user prompt
        String prompt = ">";
        while(true) {
            System.out.print(prompt);
            System.out.flush();
            System.out.println(Scheme.eval(reader.read(), theGlobalEnv));
        }
    }
}
