import java.io.IOException;
import java.io.InputStream;
import java.util.logging.LogManager;
import java.util.logging.Logger;

import jasi.ast.AST;
import jasi.parser.Parser;
import jasi.parser.Tokenizer;

public class Main {

    private final static Logger log = Logger.getLogger(Tokenizer.class.getName());

	public static void main(String[] args) {
	    Parser p = new Parser();
        AST ast = p.parseExpr();
        log.severe(":" + ast.toString() + ":");
    }

	/*private void eval(Expression exp, Environment env) {
        if(isSelfEvaluating(exp){
            evalValue(exp);
        }
        else if(isVariable(exp)) {
            env.getVariableValue(exp);
        }
        else if(isQuoted(exp)) {
            exp.text;
        }
        else if(isAssignment(exp)) {
            evalAssignment(exp, env);
        }
        else if(isDefinition(exp)) {
            (evalDefinition(exp, env))
        }
        else if(isIf(exp)) {
            (evalIf(exp, env))
        }
        else if(isLambda(exp)) {
            makeProcedure(lambdaParameters(exp),lambdaBody(exp), env)
        }
        else if(isBegin(exp)) {
            evalSequence(exp, env);
        }
        else if(isApplication(exp)) {
            apply(eval(operator(exp),env), listOfValues(operands(exp), env))
        }
        else throw RuntimeException("expression is not recognized: " + exp.toString());
    }*/
}
