package jasi.datatype;

import java.util.HashMap;
import java.util.Map;

//scheme variable
public class SVariable {

    private String value;

    private static Map<String, SVariable> instances = new HashMap<String, SVariable>();

    private SVariable(String s) {
        this.value = s;
    }

    public static SVariable getInstance(String s) {
        SVariable sv = instances.get(s);
        if(sv == null) {
            sv = new SVariable(s);
            instances.put(s, sv);
        }
        return sv;
    }

    public String getValue() {
        return value;
    }

    public String toString() {
        return value;
    }
}

