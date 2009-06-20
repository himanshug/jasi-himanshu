package jasi.semantics;

import jasi.semantics.procedure.PlusPrimitiveProcedure;

public class Environment {

    public Object getVariableValue(String vName) {
        //right now just returns plus primitive procedure.
        return new PlusPrimitiveProcedure();
    }
}
