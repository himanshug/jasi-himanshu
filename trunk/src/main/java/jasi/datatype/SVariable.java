package jasi.datatype;

import java.util.HashMap;
import java.util.Map;

//scheme variable
public class SVariable {

    private String name;

    //this is actually the symbol table also
    private static Map<String, SVariable> instances = new HashMap<String, SVariable>();

    private SVariable(String s) {
        this.name = s;
    }

    public static SVariable getInstance(String s) {
        s = s.toLowerCase();
        SVariable sv = instances.get(s);
        if(sv == null) {
            sv = new SVariable(s);
            instances.put(s, sv);
        }
        return sv;
    }

    public String getName() {
        return name;
    }

    public String toString() {
        return name;
    }
}

