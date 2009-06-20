package jasi;

import jasi.semantics.*;

import java.util.logging.Logger;

import jasi.ast.AST;
import jasi.parser.Parser;

public class Main {

    private final static Logger log = Logger.getLogger(Main.class.getName());

    public static void main(String[] args) {
        Parser p = new Parser();
        AST ast = p.parseExpr();
        Scheme interpreter = new Scheme();
        Environment env = new Environment();
        log.severe(":" + ast.toString() + ":");
        log.severe(":value is:");
        log.severe(Utils.stringify(interpreter.eval(ast, env)));
    }
}
