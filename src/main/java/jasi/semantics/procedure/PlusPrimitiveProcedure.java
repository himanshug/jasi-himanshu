package jasi.semantics.procedure;

import java.util.ArrayList;

public class PlusPrimitiveProcedure extends PrimitiveProcedure {

    public Object apply(Object args) {
        double result = 0.0;
        if(args == null)
            return result;

        if(args instanceof ArrayList) {
            ArrayList arrL = (ArrayList)args;
            for(int i=0; i < arrL.size(); i++) {
                Object o = arrL.get(i);
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
        else {
            throw new RuntimeException("invalid args, not  ArrayList");
        }
    }
}
