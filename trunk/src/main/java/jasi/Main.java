package jasi;

import jasi.semantics.*;

import java.util.logging.Logger;

import jasi.ast.AST;
import jasi.parser.Parser;

public class Main {

    private final static Logger log = Logger.getLogger(Main.class.getName());

    public static void main(String[] args) {
        
        log.severe(":" + SchemeReader.write(SchemeReader.read()) + ":");
    }
}
