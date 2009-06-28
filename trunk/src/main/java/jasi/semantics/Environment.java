package jasi.semantics;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import jasi.datatype.SVariable;

public class Environment {

    Map<SVariable, Object> bindings = new HashMap<SVariable, Object>();

    //parent environment if any, created at the time of application of a compound prodedure
    //by using extendEnvironment method
    private Environment parent;

    public Environment() {}

    private Environment(Environment parent) {
        this.parent = parent;
    }

    //puts a new binding
    public void putBinding(SVariable sv, Object o) {
        bindings.put(sv, o);
    }

    //changes already defined binding
    public void setBinding(SVariable sv, Object o) {
        if(bindings.containsKey(sv))
            bindings.put(sv, o);
        else {
            if(parent != null)
                parent.setBinding(sv, o);
            else
                throw new RuntimeException("variable " + sv.toString() + " is unbound.");
        }
    }

    //gets a binding
    public Object getBinding(SVariable sv) {
        if(bindings.containsKey(sv))
            return bindings.get(sv);
        else {
            if(parent != null)
                return parent.getBinding(sv);
            else return null; //null means no binding is available.
        }
    }

    public Environment extendEnvironment(List<SVariable> variables, List<Object> values) {
        int size = variables.size();
        if(size != values.size())
            throw new RuntimeException("size mismatch between variables and values.");
        
        Environment result = new Environment(this);
        for(int i =0; i < size; i++) {
            result.putBinding(variables.get(i), values.get(i));
        }
        return result;
    }
}
