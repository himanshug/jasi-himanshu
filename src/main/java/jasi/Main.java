package jasi;

import jasi.parser.Reader;
import jasi.semantics.*;

import java.util.logging.Logger;

public class Main {

    private final static Logger log = Logger.getLogger(Main.class.getName());

    public static void main(String[] args) {
        Scheme s = new Scheme();
        Environment env = new Environment();
        log.severe(":" + (s.eval(Reader.read(), env)).toString() + ":");
    }
}
