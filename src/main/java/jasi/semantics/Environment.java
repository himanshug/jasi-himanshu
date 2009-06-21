package jasi.semantics;

import java.util.HashMap;
import java.util.Map;

import jasi.datatype.SVariable;

public class Environment {

    Map<SVariable, Object> bindings = new HashMap<SVariable, Object>();

    public void putBinding(SVariable sv, Object o) {
        bindings.put(sv, o);
    }

    public Object getBinding(SVariable sv) {
        if(bindings.containsKey(sv))
            return bindings.get(sv);
        else return null; //null means no binding is available.
    }
}
