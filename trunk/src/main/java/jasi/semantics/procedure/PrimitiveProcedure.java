package jasi.semantics.procedure;

import java.util.ArrayList;

import jasi.datatype.SNumber;
import jasi.parser.Reader;
import jasi.semantics.Environment;

public class PrimitiveProcedure extends Procedure {

    //procecure Ids
    public final static int PLUS = 0;
    public final static int MINUS = PLUS + 1;
    public final static int MULTIPLICATION = MINUS + 1;
    public final static int DIVISION = MULTIPLICATION + 1;

    public final static int READ = 100;
    public final static int EVAL = READ + 1;
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

        //for all the procedures that take a limited number of arguments, we pass
        //min, max integers specifying the number of minimum and maximum arguments
        //they can take. For a procedure that can take any arguments we pass a -ve
        //value for max.
        final int n = -1;
        
        switch(id) {
        case PLUS:
            return applyPlus(args, 0, n);
        /*case MINUS:
            return applyMinus(args);
        case MULTIPLICATION:
            return applyMultiplication(args);
        case DIVISION:
            return applyDivision(args);*/
        case READ:
            return applyRead();
        //case EVAL:
            //return applyEval(args);
        default:
            throw new RuntimeException("not a valid primitive procedure id:" + id);
        }
    }

    //min-max is the number of minimum and maximum arguments this procedure
    //can take.
    private Object applyPlus(ArrayList args, int min, int max) {
        double result = 0.0;
        if(args == null)
            return result;

        //validate number of arguments
        validateArgsSize(args.size(), min, max);

        for(int i=0; i < args.size(); i++) {
            Object o = args.get(i);
            if(o instanceof SNumber) {
                double d = ((SNumber)o).getValue();
                result += d;
            }
            else {
                throw new RuntimeException("invalid argument, not a number:" + o);
            }
        }
        return new SNumber(result);
    }

    private Object applyRead() {
        return Reader.read();
    }

    //validates if min <= size <= max.
    //max < 0 is treated as infinity.
    private void validateArgsSize(int size, int min, int max) {
        if(size < min || (size > max && max >= 0))
            throw new RuntimeException("argument list size is not valid.");
    }

    public String toString() {
        return "#<Primitive-Procedure>#";
    }
}
