import jasi.ast.AST;
import jasi.parser.Parser;

public class Main {

	public static void main(String[] args) {
	    Parser p = new Parser();
        AST ast = p.parseExpr();
        System.out.println(ast);
        System.out.println(":" + ast.toString() + ":");
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
