package jasi.semantics.procedure;

import java.util.ArrayList;

import jasi.datatype.SBoolean;
import jasi.datatype.SChar;
import jasi.datatype.SEmptyList;
import jasi.datatype.SNumber;
import jasi.datatype.SPair;
import jasi.datatype.SString;
import jasi.datatype.SUndefined;
import jasi.datatype.SVariable;
import jasi.parser.Reader;
import jasi.semantics.Environment;
import jasi.semantics.Utils;

public class PrimitiveProcedure extends Procedure {

    //procecure Ids
    public final static int PLUS = 0;
    public final static int MINUS = PLUS + 1;
    public final static int MULTIPLICATION = MINUS + 1;
    public final static int DIVISION = MULTIPLICATION + 1;

    public final static int NOT = DIVISION + 1;

    public final static int READ = 100;
    public final static int EVAL = READ + 1;
    public final static int DISPLAY = EVAL + 1;
    public final static int WRITE = DISPLAY + 1;
    public final static int NEWLINE = WRITE + 1;
    public final static int CONS = NEWLINE + 1;
    public final static int LIST = CONS + 1;
    
    public final static int PRED_EQ = LIST + 1;
    public final static int PRED_EQV = PRED_EQ + 1;
    public final static int PRED_EQUAL = PRED_EQV + 1;
    public final static int CAR = PRED_EQUAL + 1;
    public final static int CDR = CAR + 1;
    public final static int SET_CAR = CDR + 1;
    public final static int SET_CDR = SET_CAR + 1;
    public final static int PRED_PAIR = SET_CDR + 1;
    public final static int PRED_LIST = PRED_PAIR + 1;
    public final static int PRED_BOOLEAN = PRED_LIST + 1;
    public final static int PRED_CHAR = PRED_BOOLEAN + 1;
    public final static int PRED_STRING = PRED_CHAR + 1;
    public final static int PRED_SYMBOL = PRED_STRING + 1;
    public final static int PRED_NUMBER = PRED_SYMBOL + 1;
    public final static int PRED_PROCEDURE = PRED_NUMBER + 1;
    public final static int PRED_NULL = PRED_PROCEDURE + 1;

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
        case NOT:
            return applyNot(args, 1 ,1);
        case READ:
            return applyRead();
        //case EVAL:
            //return applyEval(args);
        case PRED_EQ:
            return applyPredEq(args , 2, 2);
        case PRED_EQV:
            return applyPredEqv(args, 2, 2);
        case PRED_EQUAL:
            return applyPredEqual(args, 2, 2);
        case DISPLAY:
            return applyDisplay(args, 1, 2);
        case WRITE:
            return applyWrite(args, 1, 2);
        case NEWLINE:
            return applyNewline();
        case CONS:
            return applyCons(args, 2 , 2);
        case CAR:
            return applyCar(args, 1, 1);
        case CDR:
            return applyCdr(args, 1, 1);
        case SET_CAR:
            return applySetCar(args, 2, 2);
        case SET_CDR:
            return applySetCdr(args, 2, 2);
        case PRED_PAIR:
            return applyPredPair(args, 1, 1);
        case PRED_LIST:
            return applyPredList(args, 1, 1);
        case LIST:
            return applyList(args, 0 , n);
        case PRED_BOOLEAN:
            return applyPredBoolean(args, 1 ,1);
        case PRED_CHAR:
            return applyPredChar(args, 1 ,1);
        case PRED_NULL:
            return applyPredNull(args, 1 ,1);
        case PRED_NUMBER:
            return applyPredNumber(args, 1 ,1);
        case PRED_PROCEDURE:
            return applyPredProcedure(args, 1 ,1);
        case PRED_STRING:
            return applyPredString(args, 1 ,1);
        case PRED_SYMBOL:
            return applyPredSymbol(args, 1 ,1);
        default:
            throw new RuntimeException("not a valid primitive procedure id:" + id);
        }
    }

    private Object applyPredEq(ArrayList args, int min, int max) {
        if (args == null) {
            throw new RuntimeException("must provide arguments");
        }
        // validate number of arguments
        validateArgsSize(args.size(), min, max);
        return args.get(0) == args.get(1);
    }

    private Object applyPredEqv(ArrayList args, int min, int max) {
        if (args == null) {
            throw new RuntimeException("must provide arguments");
        }
        // validate number of arguments
        validateArgsSize(args.size(), min, max);
        
        Object o1 = args.get(0);
        Object o2 = args.get(1);
        return SBoolean.getInstance(checkEqv(o1,o2));
    }

    private Object applyPredEqual(ArrayList args, int min, int max) {
        if (args == null) {
            throw new RuntimeException("must provide arguments");
        }
        // validate number of arguments
        validateArgsSize(args.size(), min, max);
        
        Object o1 = args.get(0);
        Object o2 = args.get(1);
        
        if(o1.getClass() == o2.getClass() && o1 instanceof SPair) {
            SPair sp1 = (SPair)o1;
            SPair sp2 = (SPair)o2;
            while(true) {
                o1 = sp1.getCar();
                o2 = sp2.getCar();
                if(!checkEqv(o1,o2)) return SBoolean.getInstance(false);
                
                o1 = sp1.getCdr();
                o2 = sp1.getCdr();
                if(!(o1 instanceof SPair && o2 instanceof SPair))
                    return SBoolean.getInstance(checkEqv(o1,o2));
                else {
                    sp1 = (SPair)o1;
                    sp2 = (SPair)o2;
                }
            }
        }
        else return SBoolean.getInstance(checkEqv(o1, o2));
    }

    //checks if two objects should return #t for eqv?
    //on o1 and o2
    private boolean checkEqv(Object o1, Object o2) {
      //this takes care of both being #t or #f or same symbol
        //or both being empty list
        if(o1 == o2) return true;
        
        //false if they're not of same type
        if(o1.getClass() != o2.getClass()) return false;
        
        //true if they're chars with same value
        if(o1 instanceof SChar)
            return ((SChar)o1).getValue() == ((SChar)o2).getValue();
        
        //true if they're numbers with same numeric value
        if(o1 instanceof SNumber)
            return ((SNumber)o1).getValue() == ((SNumber)o2).getValue();
        
        //true if they're both strings with same string value
        if(o1 instanceof SString)
            return ((SString)o1).getValue().equals(((SString)o2).getValue());
        
        //todo:
        //true if they're procedures or pairs having same location
        
        //if nothing then false
        return false;
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

    private Object applyNot(ArrayList args, int min, int max) {
        if (args == null) {
            throw new RuntimeException("must provide arguments");
        }
        // validate number of arguments
        validateArgsSize(args.size(), min, max);

        return SBoolean.getInstance(!Utils.isTrue(args.get(0)));
    }

    private Object applyRead() {
        return Reader.read();
    }

    //args should be array-list containing two elements, first is the object
    //to be printed and second is the port.
    //NOTE: port functionality is not supported, output goes automatically to
    //standard output
    private Object applyDisplay(ArrayList args, int min, int max) {
        if (args == null) {
            throw new RuntimeException("must provide arguments");
        }
        // validate number of arguments
        validateArgsSize(args.size(), min, max);
        System.out.print(args.get(0));
        return SUndefined.getInstance();
    }

    //args should be array-list containing two elements, first is the object
    //to be printed and second is the port.
    //NOTE: port functionality is not supported, output goes automatically to
    //standard output
    private Object applyWrite(ArrayList args, int min, int max) {
        if (args == null) {
            throw new RuntimeException("must provide arguments");
        }
        // validate number of arguments
        validateArgsSize(args.size(), min, max);
        
        String s = args.get(0).toString();
        //s = s.replaceAll("\\", "\\\\");
        //s = s.replaceAll("#\\", "#");
        //s = s.replaceAll("\"", "\\\"");
        System.out.print(s);
        return SUndefined.getInstance();
    }

    private Object applyNewline() {
        System.out.println("");
        return SUndefined.getInstance();
    }

    private Object applyCons(ArrayList args, int min, int max) {
        if (args == null) {
            throw new RuntimeException("must provide arguments");
        }
        // validate number of arguments
        validateArgsSize(args.size(), min, max);

        return Utils.cons(args.get(0), args.get(1));
    }

    private Object applyCar(ArrayList args, int min, int max) {
        if (args == null) {
            throw new RuntimeException("must provide arguments");
        }
        // validate number of arguments
        validateArgsSize(args.size(), min, max);

        return Utils.car(args.get(0));
    }

    private Object applyCdr(ArrayList args, int min, int max) {
        if (args == null) {
            throw new RuntimeException("must provide arguments");
        }
        // validate number of arguments
        validateArgsSize(args.size(), min, max);

        return Utils.cdr(args.get(0));
    }

    private Object applySetCar(ArrayList args, int min, int max) {
        if (args == null) {
            throw new RuntimeException("must provide arguments");
        }
        // validate number of arguments
        validateArgsSize(args.size(), min, max);

        Object oPair = args.get(0);
        Object oItem = args.get(1);
        Utils.validateType(oPair, SPair.class);
        SPair p = (SPair)oPair;
        p.setCar(oItem);
        return SUndefined.getInstance();
    }

    private Object applySetCdr(ArrayList args, int min, int max) {
        if (args == null) {
            throw new RuntimeException("must provide arguments");
        }
        // validate number of arguments
        validateArgsSize(args.size(), min, max);

        Object oPair = args.get(0);
        Object oItem = args.get(1);
        Utils.validateType(oPair, SPair.class);
        SPair p = (SPair)oPair;
        p.setCdr(oItem);
        return SUndefined.getInstance();
    }

    private Object applyList(ArrayList args, int min, int max) {

        if(args != null && args.size() > 0) {
            int len = args.size();
            SPair result = new SPair();
            SPair tmp = result;
            for(int i=0; i<len; i++) {
                tmp.setCar(args.get(i));
                if(i == len-1) {
                    tmp.setCdr(SEmptyList.getInstance());
                }
                else {
                    SPair sp = new SPair();
                    tmp.setCdr(sp);
                    tmp = sp;
                }
            }
            return result;
        }
        else return SEmptyList.getInstance();
    }

    private Object applyPredPair(ArrayList args, int min, int max) {
        if (args == null) {
            throw new RuntimeException("must provide arguments");
        }
        // validate number of arguments
        validateArgsSize(args.size(), min, max);

        return (args.get(0) instanceof SPair);
    }

    private Object applyPredBoolean(ArrayList args, int min, int max) {
        if (args == null) {
            throw new RuntimeException("must provide arguments");
        }
        // validate number of arguments
        validateArgsSize(args.size(), min, max);

        return (args.get(0) instanceof SBoolean);
    }

    private Object applyPredChar(ArrayList args, int min, int max) {
        if (args == null) {
            throw new RuntimeException("must provide arguments");
        }
        // validate number of arguments
        validateArgsSize(args.size(), min, max);

        return (args.get(0) instanceof SChar);
    }

    private Object applyPredString(ArrayList args, int min, int max) {
        if (args == null) {
            throw new RuntimeException("must provide arguments");
        }
        // validate number of arguments
        validateArgsSize(args.size(), min, max);

        return (args.get(0) instanceof SString);
    }

    private Object applyPredSymbol(ArrayList args, int min, int max) {
        if (args == null) {
            throw new RuntimeException("must provide arguments");
        }
        // validate number of arguments
        validateArgsSize(args.size(), min, max);

        return (args.get(0) instanceof SVariable);
    }

    private Object applyPredNumber(ArrayList args, int min, int max) {
        if (args == null) {
            throw new RuntimeException("must provide arguments");
        }
        // validate number of arguments
        validateArgsSize(args.size(), min, max);

        return (args.get(0) instanceof SNumber);
    }

    private Object applyPredProcedure(ArrayList args, int min, int max) {
        if (args == null) {
            throw new RuntimeException("must provide arguments");
        }
        // validate number of arguments
        validateArgsSize(args.size(), min, max);

        return (args.get(0) instanceof Procedure);
    }

    private Object applyPredList(ArrayList args, int min, int max) {
        if (args == null) {
            throw new RuntimeException("must provide arguments");
        }
        // validate number of arguments
        validateArgsSize(args.size(), min, max);

        return Utils.isSchemeList(args.get(0));
    }

    private Object applyPredNull(ArrayList args, int min, int max) {
        if (args == null) {
            throw new RuntimeException("must provide arguments");
        }
        // validate number of arguments
        validateArgsSize(args.size(), min, max);

        return SBoolean.getInstance((args.get(0) instanceof SEmptyList));
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
