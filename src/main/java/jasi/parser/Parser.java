package jasi.parser;

import jasi.ast.AST;
import jasi.ast.ApplicationAST;
import jasi.ast.CharAST;
import jasi.ast.DefinitionAST;
import jasi.ast.IfAST;
import jasi.ast.LambdaAST;
import jasi.ast.NumberAST;
import jasi.ast.StringAST;
import jasi.ast.VariableAST;

/**
 * This is a Recursive descent parser, where we have one parse method
 * for each production rule(each one is given a label) in EBNF representation
 * of the grammar. And each parse method creates its respective AST.
 * It is essential to remove left recursion and left factorize the grammar for
 * it to be implementable using recursive descent algorhythm.
 * 
 * @author himanshu<g.himanshu@gmail.com>
 *
 */
public class Parser {

    public AST parseExpr() {
        Token currentToken = Tokenizer.getNextToken();
        switch(currentToken.getType()) {
            case Constants.TOKEN_TYPE_CHAR:
                return parseCharExpr(currentToken);
            case Constants.TOKEN_TYPE_NUMBER:
                return parseNumberExpr(currentToken);
            case Constants.TOKEN_TYPE_STRING:
                return parseStringExpr(currentToken);
            case Constants.TOKEN_TYPE_VARIABLE:
                return parseVariableExpr(currentToken);
            case Constants.TOKEN_TYPE_LPAREN:
                currentToken = Tokenizer.getNextToken();
                switch(currentToken.getType()) {
                    case Constants.TOKEN_TYPE_DEFINE:
                        return parseDefinitionExpr(currentToken);
                    case Constants.TOKEN_TYPE_IF:
                        return parseIfExpr(currentToken);
                    case Constants.TOKEN_TYPE_LAMBDA:
                        return parseLambdaExpr(currentToken);
                    case Constants.TOKEN_TYPE_VARIABLE:
                        return parseApplicationExpr(currentToken);
                    default:
                        throw new RuntimeException("Could not parse:" + currentToken.getType() +
                                ":" + currentToken.getSpelling());
                }
            default:
                throw new RuntimeException("Could not parse at:" + currentToken.getType() +
                        ":" + currentToken.getSpelling());
        }
    }

    public CharAST parseCharExpr(Token currentToken) {
        return new CharAST(currentToken);
    }

    public NumberAST parseNumberExpr(Token currentToken) {
        return new NumberAST(currentToken);
    }

    public StringAST parseStringExpr(Token currentToken) {
        return new StringAST(currentToken);
    }

    public VariableAST parseVariableExpr(Token currentToken) {
        return new VariableAST(currentToken);
    }

    public DefinitionAST parseDefinitionExpr(Token currentToken) {
        DefinitionAST ast = new DefinitionAST(currentToken);
        Token t = Tokenizer.getNextToken();
        if(t.getType() == Constants.TOKEN_TYPE_VARIABLE) {
            ast.setVariable(parseVariableExpr(t));
            ast.setExpr(parseExpr());
            t = Tokenizer.getNextToken();
            if(t.getType() != Constants.TOKEN_TYPE_RPAREN)
                throw new RuntimeException("could not parse definition expr.");
            
            return ast;
        }
        else
            throw new RuntimeException("could not parse definition expr.");
    }

    public IfAST parseIfExpr(Token currentToken) {
        IfAST ast = new IfAST(currentToken);
        ast.setTestExpr(parseExpr());
        ast.setThenExpr(parseExpr());
        Token t = Tokenizer.peekNextToken();
        if(t.getType() != Constants.TOKEN_TYPE_RPAREN)
            ast.setElseExpr(parseExpr());
        t = Tokenizer.getNextToken();
        if(t.getType() != Constants.TOKEN_TYPE_RPAREN)
            throw new RuntimeException("could not parse if expression.");
        return ast;
    }

    public LambdaAST parseLambdaExpr(Token currentToken) {
        LambdaAST ast = new LambdaAST(currentToken);
        Token t = Tokenizer.getNextToken();
        if(t.getType() != Constants.TOKEN_TYPE_LPAREN)
            throw new RuntimeException("could not parse lambda params");
        
        t = Tokenizer.peekNextToken();
        while(t.getType() == Constants.TOKEN_TYPE_VARIABLE) {
            Tokenizer.getNextToken();
            ast.addParam(parseVariableExpr(t));
            t = Tokenizer.peekNextToken();
        }
        
        if(t.getType() != Constants.TOKEN_TYPE_RPAREN)
            throw new RuntimeException("could not parse lambda params");
        
        Tokenizer.getNextToken();
        
        ast.addBodyStatement(parseExpr());
        t = Tokenizer.peekNextToken();
        while(t.getType() != Constants.TOKEN_TYPE_RPAREN) {
            ast.addBodyStatement(parseExpr());
            t = Tokenizer.peekNextToken();
        }
        Tokenizer.getNextToken();
        return ast;
    }

    public ApplicationAST parseApplicationExpr(Token currentToken) {
        ApplicationAST ast = new ApplicationAST();
        ast.setVariable(parseVariableExpr(currentToken));
        Token t = Tokenizer.peekNextToken();
        while(t.getType() != Constants.TOKEN_TYPE_RPAREN) {
            ast.addExpr(parseExpr());
            t = Tokenizer.peekNextToken();
        }
        Tokenizer.getNextToken();
        return ast;
    }
}
