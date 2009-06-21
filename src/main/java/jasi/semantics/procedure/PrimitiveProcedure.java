package jasi.semantics.procedure;

import java.util.ArrayList;

import jasi.semantics.Environment;

public class PrimitiveProcedure extends Procedure {

    //todo: try to do read-eval first and that may change various design
    //decisions.
    //also do lambda
    //macro-expand will modify the AST

    //procecure Ids
    public final static int PLUS = 0;
    public final static int MINUS = PLUS + 1;
    public final static int MULTIPLICATION = MINUS + 1;
    public final static int DIVISION = MULTIPLICATION + 1;

    //procedure id of this PrimitiveProcedure
    private int id;

    public PrimitiveProcedure(int id) {
        this.id = id;
    }

    public Object apply(Object o, Environment env) {
        ArrayList<Object> args = null;
        if(o != null) {
            if(o instanceof ArrayList)
                args = (ArrayList)o;
            else
                throw new RuntimeException("arguments not ArrayList:" + o);
        }

        switch(id) {
        case PLUS:
            return applyPlus(args);
        /*case MINUS:
            return applyMinus(args);
        case MULTIPLICATION:
            return applyMultiplication(args);
        case DIVISION:
            return applyDivision(args);*/
        default:
            throw new RuntimeException("not a valid primitive procedure id:" + id);
        }
    }

    private Object applyPlus(ArrayList args) {
        double result = 0.0;
        if(args == null)
            return result;

        for(int i=0; i < args.size(); i++) {
            Object o = args.get(i);
            if(o instanceof Double) {
                double d = ((Double)o).doubleValue();
                result += d;
            }
            else {
                throw new RuntimeException("invalid argument, not a number:" + o);
            }
        }
        return result;
    }
}
