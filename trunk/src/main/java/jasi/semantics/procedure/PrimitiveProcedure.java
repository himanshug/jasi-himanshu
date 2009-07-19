package jasi.semantics.procedure;

import java.util.ArrayList;

import jasi.Main;
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
import jasi.semantics.Scheme;
import jasi.semantics.Utils;

public class PrimitiveProcedure extends Procedure {

    //procecure Ids
    public final static int PLUS = 0;
    public final static int MINUS = PLUS + 1;
    public final static int MULTIPLICATION = MINUS + 1;
    public final static int DIVISION = MULTIPLICATION + 1;

    public final static int NOT = DIVISION + 1;
    public final static int GT = NOT + 1;
    public final static int LT = GT + 1;
    public final static int GT_EQ = LT + 1;
    public final static int LT_EQ = GT_EQ + 1;

    public final static int MAKE_STRING = LT_EQ + 1;
    public final static int SYMBOL_TO_STRING = MAKE_STRING + 1;
    public final static int STRING_TO_SYMBOL = SYMBOL_TO_STRING + 1;
    public final static int STRING_LENGTH = STRING_TO_SYMBOL + 1;
    public final static int STRING_REF = STRING_LENGTH + 1;
    public final static int STRING_SET = STRING_REF + 1;
    public final static int NUMBER_TO_STRING = STRING_SET + 1;
    public final static int STRING_TO_NUMBER = NUMBER_TO_STRING + 1;

    public final static int CHAR_UPCASE = STRING_TO_NUMBER + 1;
    public final static int CHAR_DOWNCASE = CHAR_UPCASE + 1;
    public final static int CHAR_LT = CHAR_DOWNCASE + 1;
    public final static int CHAR_PRED_ALPHABETIC = CHAR_LT + 1;
    public final static int CHAR_PRED_NUMERIC = CHAR_PRED_ALPHABETIC + 1;
    public final static int CHAR_PRED_WHITESPACE = CHAR_PRED_NUMERIC + 1;
    public final static int CHAR_PRED_LOWERCASE = CHAR_PRED_WHITESPACE + 1;
    public final static int CHAR_PRED_UPPERCASE = CHAR_PRED_LOWERCASE + 1;

    public final static int READ = 100;
    public final static int EVAL = READ + 1;
    public final static int APPLY = EVAL + 1;
    public final static int ERROR = APPLY + 1;
    public final static int INTERACTION_ENVIRONMENT = ERROR + 1;
    public final static int DISPLAY = INTERACTION_ENVIRONMENT + 1;
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
        case MINUS:
            return applyMinus(args, 1, n);
        case MULTIPLICATION:
            return applyMultiplication(args, 0 ,n);
        case DIVISION:
            return applyDivision(args, 1, n);
        case NOT:
            return applyNot(args, 1 ,1);
        case GT:
            return applyGreaterThan(args, 0, n);
        case LT:
            return applyLessThan(args, 0 ,n);
        case GT_EQ:
            return applyGreaterThanEqual(args, 0 , n);
        case LT_EQ:
            return applyLessThanEqual(args, 0 , n);
        case MAKE_STRING:
            return applyMakeString(args, 1, 2);
        case SYMBOL_TO_STRING:
            return applySymbolToString(args, 1, 1);
        case STRING_TO_SYMBOL:
            return applyStringToSymbol(args, 1, 1);
        case STRING_LENGTH:
            return applyStringLength(args, 1, 1);
        case STRING_REF:
            return applyStringRef(args, 2, 2);
        case STRING_SET:
            return applyStringSet(args, 3, 3);
        case NUMBER_TO_STRING:
            return applyNumberToString(args, 1, 1);
        case STRING_TO_NUMBER:
            return applyStringToNumber(args, 1, 1);
        case CHAR_UPCASE:
            return applyCharUpCase(args, 1, 1);
        case CHAR_DOWNCASE:
            return applyCharDownCase(args, 1, 1);
        case CHAR_LT:
            return applyCharLess(args, 2, 2);
        case CHAR_PRED_ALPHABETIC:
            return applyCharPredAlphabetic(args, 1, 1);
        case CHAR_PRED_NUMERIC:
            return applyCharPredNumeric(args, 1, 1);
        case CHAR_PRED_WHITESPACE:
            return applyCharPredWhitespace(args, 1, 1);
        case CHAR_PRED_LOWERCASE:
            return applyCharPredLowerCase(args, 1, 1);
        case CHAR_PRED_UPPERCASE:
            return applyCharPredUpperCase(args, 1, 1);
        case READ:
            return applyRead();
        case EVAL:
            return applyEval(args, 1 ,2, env);
        case ERROR:
            return applyError(args, 1, 1);
        case APPLY:
            return applyApply(args, 2 ,2, env);
        case INTERACTION_ENVIRONMENT:
            return applyInteractionEnvironment(args, 0 ,0);
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

    //min-max is the number of minimum and maximum arguments this procedure
    //can take.
    private Object applyPlus(ArrayList args, int min, int max) {
        int result = 0;
        if(args == null)
            return new SNumber(result);

        //validate number of arguments
        validateArgsSize(args.size(), min, max);

        for(int i=0; i < args.size(); i++) {
            Object o = args.get(i);
            if(o instanceof SNumber) {
                double d = ((SNumber)o).getValue();
                result += Double.valueOf(d).intValue();
            }
            else {
                throw new RuntimeException("invalid argument, not a number:" + o);
            }
        }
        return new SNumber(result);
    }

    private Object applyMinus(ArrayList args, int min, int max) {
        if (args == null) {
            throw new RuntimeException("must provide arguments");
        }

        //validate number of arguments
        validateArgsSize(args.size(), min, max);

        int result = 0;
        for(int i=0; i < args.size(); i++) {
            Object o = args.get(i);
            Utils.validateType(o, SNumber.class);
            double d = ((SNumber)o).getValue();
            if(i == 0 && args.size() > 1) {
                result = Double.valueOf(d).intValue();
            }
            else result -= Double.valueOf(d).intValue();
        }
        return new SNumber(result);
    }

    private Object applyMultiplication(ArrayList args, int min, int max) {
        int result = 1;
        if(args == null)
            return new SNumber(result);

        //validate number of arguments
        validateArgsSize(args.size(), min, max);

        for(int i=0; i < args.size(); i++) {
            Object o = args.get(i);
            if(o instanceof SNumber) {
                double d = ((SNumber)o).getValue();
                result *= Double.valueOf(d).intValue();
            }
            else {
                throw new RuntimeException("invalid argument, not a number:" + o);
            }
        }
        return new SNumber(result);
    }

    private Object applyDivision(ArrayList args, int min, int max) {
        int result = 1;
        if(args == null)
            throw new RuntimeException("must provide arguments");

        //validate number of arguments
        validateArgsSize(args.size(), min, max);

        for(int i=0; i < args.size(); i++) {
            Object o = args.get(i);
            if(o instanceof SNumber) {
                double d = ((SNumber)o).getValue();
                if(i == 0 && args.size() > 1)
                    result = Double.valueOf(d).intValue();
                else
                    result /= Double.valueOf(d).intValue();
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

    
    private Object applyGreaterThan(ArrayList args, int min, int max) {
        if (args == null || args.size() == 1) {
            return SBoolean.getInstance(true);
        }
        // validate number of arguments
        validateArgsSize(args.size(), min, max);

        Object o = args.get(0);
        Utils.validateType(o, SNumber.class);
        double d = ((SNumber)o).getValue();
        for(int i = 1; i < args.size(); i++) {
            Object o2 = args.get(i);
            Utils.validateType(o2, SNumber.class);
            double d2 = ((SNumber)o2).getValue();
            if(d2 < d)
                d = d2;
            else
                return SBoolean.getInstance(false);
        }

        return SBoolean.getInstance(true);
    }

    private Object applyGreaterThanEqual(ArrayList args, int min, int max) {
        if (args == null || args.size() == 1) {
            return SBoolean.getInstance(true);
        }
        // validate number of arguments
        validateArgsSize(args.size(), min, max);

        Object o = args.get(0);
        Utils.validateType(o, SNumber.class);
        double d = ((SNumber)o).getValue();
        for(int i = 1; i < args.size(); i++) {
            Object o2 = args.get(i);
            Utils.validateType(o2, SNumber.class);
            double d2 = ((SNumber)o2).getValue();
            if(d2 <= d)
                d = d2;
            else
                return SBoolean.getInstance(false);
        }

        return SBoolean.getInstance(true);
    }

    private Object applyLessThan(ArrayList args, int min, int max) {
        if (args == null || args.size() == 1) {
            return SBoolean.getInstance(true);
        }
        // validate number of arguments
        validateArgsSize(args.size(), min, max);

        Object o = args.get(0);
        Utils.validateType(o, SNumber.class);
        double d = ((SNumber)o).getValue();
        for(int i = 1; i < args.size(); i++) {
            Object o2 = args.get(i);
            Utils.validateType(o2, SNumber.class);
            double d2 = ((SNumber)o2).getValue();
            if(d2 > d)
                d = d2;
            else
                return SBoolean.getInstance(false);
        }

        return SBoolean.getInstance(true);
    }

    private Object applyLessThanEqual(ArrayList args, int min, int max) {
        if (args == null || args.size() == 1) {
            return SBoolean.getInstance(true);
        }
        // validate number of arguments
        validateArgsSize(args.size(), min, max);

        Object o = args.get(0);
        Utils.validateType(o, SNumber.class);
        double d = ((SNumber)o).getValue();
        for(int i = 1; i < args.size(); i++) {
            Object o2 = args.get(i);
            Utils.validateType(o2, SNumber.class);
            double d2 = ((SNumber)o2).getValue();
            if(d2 >= d)
                d = d2;
            else
                return SBoolean.getInstance(false);
        }

        return SBoolean.getInstance(true);
    }

    private Object applyMakeString(ArrayList args, int min, int max) {
        if (args == null) {
            throw new RuntimeException("must provide arguments");
        }
        // validate number of arguments
        validateArgsSize(args.size(), min, max);

        Object o = args.get(0);
        Utils.validateType(o, SNumber.class);
        int len = (int)((SNumber)o).getValue();
        
        StringBuffer sb = new StringBuffer(len);
        char ch = ' ';
        if(args.size() > 1) {
            o = args.get(1);
            Utils.validateType(o, SChar.class);
            ch = ((SChar)o).getValue();
        }
        
        while(len-- > 0) {
            sb.append(ch);
        }
        return new SString(sb);
    }

    public Object applySymbolToString(ArrayList args, int min, int max) {
        if (args == null) {
            throw new RuntimeException("must provide arguments");
        }
        // validate number of arguments
        validateArgsSize(args.size(), min, max);
        
        Object o = args.get(0);
        Utils.validateType(o, SVariable.class);
        
        return new SString(((SVariable)o).getName());
    }

    public Object applyStringToSymbol(ArrayList args, int min, int max) {
        if (args == null) {
            throw new RuntimeException("must provide arguments");
        }
        // validate number of arguments
        validateArgsSize(args.size(), min, max);
        
        Object o = args.get(0);
        Utils.validateType(o, SString.class);
        
        return SVariable.getInstance(((SString)o).getValue().toString());
    }

    public Object applyStringLength(ArrayList args, int min, int max) {
        if (args == null) {
            throw new RuntimeException("must provide arguments");
        }
        // validate number of arguments
        validateArgsSize(args.size(), min, max);
        
        Object o = args.get(0);
        Utils.validateType(o, SString.class);
        
        return new SNumber(((SString)o).getValue().length());
    }

    public Object applyStringRef(ArrayList args, int min, int max) {
        if (args == null) {
            throw new RuntimeException("must provide arguments");
        }
        // validate number of arguments
        validateArgsSize(args.size(), min, max);
        
        Object o1 = args.get(0);
        Object o2  = args.get(1);
        Utils.validateType(o1, SString.class);
        Utils.validateType(o2, SNumber.class);
        return new SChar(((SString)o1).getValue().
                charAt(Double.valueOf(((SNumber)o2).getValue()).intValue()));
    }

    public Object applyStringSet(ArrayList args, int min, int max) {
        if (args == null) {
            throw new RuntimeException("must provide arguments");
        }
        // validate number of arguments
        validateArgsSize(args.size(), min, max);
        
        Object o1 = args.get(0);
        Object o2 = args.get(1);
        Object o3 = args.get(2);
        Utils.validateType(o1, SString.class);
        Utils.validateType(o2, SNumber.class);
        Utils.validateType(o3, SChar.class);
        
        StringBuffer sb = ((SString)o1).getValue();
        int i = Double.valueOf(((SNumber)o2).getValue()).intValue();
        char c = ((SChar)o3).getValue();
        //set c at i in sb
        sb.setCharAt(i, c);
        return SUndefined.getInstance();
    }

    public Object applyNumberToString(ArrayList args, int min, int max) {
        if (args == null) {
            throw new RuntimeException("must provide arguments");
        }
        // validate number of arguments
        validateArgsSize(args.size(), min, max);
        
        Object o = args.get(0);
        Utils.validateType(o, SNumber.class);
        
        double d = ((SNumber)o).getValue();
        return new SString(Integer.toString(Double.valueOf(d).intValue()));
    }

    public Object applyStringToNumber(ArrayList args, int min, int max) {
        if (args == null) {
            throw new RuntimeException("must provide arguments");
        }
        // validate number of arguments
        validateArgsSize(args.size(), min, max);
        
        Object o = args.get(0);
        Utils.validateType(o, SString.class);
        
        String s = ((SString)o).getValue().toString();
        
        try {
            return new SNumber(Integer.parseInt(s));
        }
        catch(NumberFormatException ex) {
            return SBoolean.getInstance(false);
        }
    }

    public Object applyCharUpCase(ArrayList args, int min, int max) {
        if (args == null) {
            throw new RuntimeException("must provide arguments");
        }
        // validate number of arguments
        validateArgsSize(args.size(), min, max);
        
        Object o = args.get(0);
        Utils.validateType(o, SChar.class);
        
        char c = ((SChar)o).getValue();
        if(Character.isLowerCase(c))
            return new SChar(Character.toUpperCase(c));
        else return o;
    }

    public Object applyCharDownCase(ArrayList args, int min, int max) {
        if (args == null) {
            throw new RuntimeException("must provide arguments");
        }
        // validate number of arguments
        validateArgsSize(args.size(), min, max);
        
        Object o = args.get(0);
        Utils.validateType(o, SChar.class);
        
        char c = ((SChar)o).getValue();
        if(Character.isUpperCase(c))
            return new SChar(Character.toLowerCase(c));
        else return o;
    }

    public Object applyCharLess(ArrayList args, int min, int max) {
        if (args == null) {
            throw new RuntimeException("must provide arguments");
        }
        // validate number of arguments
        validateArgsSize(args.size(), min, max);
        
        Object o1 = args.get(0);
        Object o2 = args.get(1);
        Utils.validateType(o1, SChar.class);
        Utils.validateType(o2, SChar.class);
        
        Character c1 = Character.valueOf(((SChar)o1).getValue());
        Character c2 = Character.valueOf(((SChar)o2).getValue());
        return SBoolean.getInstance(c1.compareTo(c2) < 0);
    }

    public Object applyCharPredAlphabetic(ArrayList args, int min, int max) {
        if (args == null) {
            throw new RuntimeException("must provide arguments");
        }
        // validate number of arguments
        validateArgsSize(args.size(), min, max);
        
        Object o = args.get(0);
        Utils.validateType(o, SChar.class);
        
        char c = ((SChar)o).getValue();
        return SBoolean.getInstance(Character.isLetter(c));
    }

    public Object applyCharPredNumeric(ArrayList args, int min, int max) {
        if (args == null) {
            throw new RuntimeException("must provide arguments");
        }
        // validate number of arguments
        validateArgsSize(args.size(), min, max);
        
        Object o = args.get(0);
        Utils.validateType(o, SChar.class);
        
        char c = ((SChar)o).getValue();
        return SBoolean.getInstance(Character.isDigit(c));
    }

    public Object applyCharPredWhitespace(ArrayList args, int min, int max) {
        if (args == null) {
            throw new RuntimeException("must provide arguments");
        }
        // validate number of arguments
        validateArgsSize(args.size(), min, max);
        
        Object o = args.get(0);
        Utils.validateType(o, SChar.class);
        
        char c = ((SChar)o).getValue();
        return SBoolean.getInstance(Character.isWhitespace(c));
    }

    public Object applyCharPredLowerCase(ArrayList args, int min, int max) {
        if (args == null) {
            throw new RuntimeException("must provide arguments");
        }
        // validate number of arguments
        validateArgsSize(args.size(), min, max);
        
        Object o = args.get(0);
        Utils.validateType(o, SChar.class);
        
        char c = ((SChar)o).getValue();
        return SBoolean.getInstance(Character.isLowerCase(c));
    }

    public Object applyCharPredUpperCase(ArrayList args, int min, int max) {
        if (args == null) {
            throw new RuntimeException("must provide arguments");
        }
        // validate number of arguments
        validateArgsSize(args.size(), min, max);
        
        Object o = args.get(0);
        Utils.validateType(o, SChar.class);
        
        char c = ((SChar)o).getValue();
        return SBoolean.getInstance(Character.isUpperCase(c));
    }

    private Object applyPredEq(ArrayList args, int min, int max) {
        if (args == null) {
            throw new RuntimeException("must provide arguments");
        }
        // validate number of arguments
        validateArgsSize(args.size(), min, max);
        return SBoolean.getInstance(args.get(0) == args.get(1));
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
        
        return SBoolean.getInstance(checkEqual(o1, o2));
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
            return ((SString)o1).getValue().toString().equals(((SString)o2).getValue().toString());
        
        //todo:
        //true if they're procedures or pairs having same location
        
        //if nothing then false
        return false;
    }

    //checks if two objects should return #t for equal?
    //on o1 and o2
    private boolean checkEqual(Object o1, Object o2) {
        if(o1.getClass() == o2.getClass() && o1 instanceof SPair) {
            SPair sp1 = (SPair)o1;
            SPair sp2 = (SPair)o2;
            while(true) {
                o1 = sp1.getCar();
                o2 = sp2.getCar();
                if(!checkEqual(o1,o2)) return false;
                
                o1 = sp1.getCdr();
                o2 = sp1.getCdr();
                if(!(o1 instanceof SPair && o2 instanceof SPair))
                    return checkEqv(o1,o2);
                else {
                    sp1 = (SPair)o1;
                    sp2 = (SPair)o2;
                }
            }
        }
        else return checkEqv(o1, o2);
    }

    private Object applyRead() {
        //todo: may be we should get reader set for this class
        return Main.reader.read();
    }

    private Object applyEval(ArrayList args, int min, int max, Environment env) {
        if (args == null) {
            throw new RuntimeException("must provide arguments");
        }
        // validate number of arguments
        validateArgsSize(args.size(), min, max);
        
        if(args.size() > 1)
            env = (Environment)args.get(1);
        
        return Scheme.eval(args.get(0), env);
    }

    private Object applyApply(ArrayList args, int min, int max, Environment env) {
        if (args == null) {
            throw new RuntimeException("must provide arguments");
        }
        // validate number of arguments
        validateArgsSize(args.size(), min, max);
        
        Object proc = args.get(0);
        Utils.validateType(proc, Procedure.class);
        
        if(args.size() > 0) {
            Object argsList = args.get(1);
            if(argsList instanceof SPair) {
                ArrayList arr = new ArrayList();
                while(argsList instanceof SPair) {
                    arr.add(Utils.car(argsList));
                    argsList = Utils.cdr(argsList);
                }
                return ((Procedure)proc).apply(arr, env);
            }
        }
        
        return ((Procedure)proc).apply(null, env);
    }

    private Object applyError(ArrayList args, int min, int max) {
        if (args == null) {
            throw new RuntimeException("must provide arguments");
        }
        // validate number of arguments
        validateArgsSize(args.size(), min, max);
        
        throw new RuntimeException(args.get(0).toString());
    }

    private Object applyInteractionEnvironment(ArrayList args, int min, int max) {
        if(args != null) {
            // validate number of arguments
            validateArgsSize(args.size(), min, max);
        }
        
        return Environment.getGlobalEnvironment();
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

        return SBoolean.getInstance((args.get(0) instanceof SPair));
    }

    private Object applyPredBoolean(ArrayList args, int min, int max) {
        if (args == null) {
            throw new RuntimeException("must provide arguments");
        }
        // validate number of arguments
        validateArgsSize(args.size(), min, max);

        return SBoolean.getInstance((args.get(0) instanceof SBoolean));
    }

    private Object applyPredChar(ArrayList args, int min, int max) {
        if (args == null) {
            throw new RuntimeException("must provide arguments");
        }
        // validate number of arguments
        validateArgsSize(args.size(), min, max);

        return SBoolean.getInstance((args.get(0) instanceof SChar));
    }

    private Object applyPredString(ArrayList args, int min, int max) {
        if (args == null) {
            throw new RuntimeException("must provide arguments");
        }
        // validate number of arguments
        validateArgsSize(args.size(), min, max);

        return SBoolean.getInstance((args.get(0) instanceof SString));
    }

    private Object applyPredSymbol(ArrayList args, int min, int max) {
        if (args == null) {
            throw new RuntimeException("must provide arguments");
        }
        // validate number of arguments
        validateArgsSize(args.size(), min, max);

        return SBoolean.getInstance((args.get(0) instanceof SVariable));
    }

    private Object applyPredNumber(ArrayList args, int min, int max) {
        if (args == null) {
            throw new RuntimeException("must provide arguments");
        }
        // validate number of arguments
        validateArgsSize(args.size(), min, max);

        return SBoolean.getInstance((args.get(0) instanceof SNumber));
    }

    private Object applyPredProcedure(ArrayList args, int min, int max) {
        if (args == null) {
            throw new RuntimeException("must provide arguments");
        }
        // validate number of arguments
        validateArgsSize(args.size(), min, max);

        return SBoolean.getInstance((args.get(0) instanceof Procedure));
    }

    private Object applyPredList(ArrayList args, int min, int max) {
        if (args == null) {
            throw new RuntimeException("must provide arguments");
        }
        // validate number of arguments
        validateArgsSize(args.size(), min, max);

        return SBoolean.getInstance(Utils.isSchemeList(args.get(0)));
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
