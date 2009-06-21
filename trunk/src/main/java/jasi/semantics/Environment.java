package jasi.semantics;

import jasi.datatype.SVariable;
import jasi.semantics.procedure.PrimitiveProcedure;

public class Environment {

    public Object getVariableValue(SVariable vName) {
        //right now just returns plus primitive procedure.
        return new PrimitiveProcedure(PrimitiveProcedure.PLUS);
    }
}
