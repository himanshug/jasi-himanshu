package jasi.semantics;

public class Utils {

    //characters
    public static boolean isSchemeChar(Object o) {
        return (o instanceof Character);
    }

    public static char readSchemeChar(String s) {
        //todo: support invisible chars like newline etc
        return s.charAt(2);
    }

    public static String writeSchemeChar(Object o) {
        validateType(o, Character.class);
        //todo: support invisible chars like newline etc
        return "#\\" + ((Character)o).toString();
    }

    //numbers
    public static boolean isSchemeNumber(Object o) {
        return (o instanceof Double);
    }
    
    public static double readSchemeNumber(String s) {
      //todo: support inexact numbers
        return Double.parseDouble(s);
    }

    public static String writeSchemeNumber(Object o) {
        validateType(o, Double.class);
        //todo: support inexact, real numbers
        return Integer.toString(((Double)o).intValue());
    }

    //strings
    public static boolean isSchemeString(Object o) {
        return (o instanceof StringBuffer);
    }

    public static StringBuffer readSchemeString(String s) {
        return new StringBuffer(s);
    }

    public static String writeSchemeString(Object o) {
        validateType(o, StringBuffer.class);
        return ((StringBuffer)o).toString();
    }

    //variable
    public static boolean isSchemeVariable(Object o) {
        return (o instanceof String);
    }
    
    public static String readSchemeVariable(String s) {
        return s;
    }

    public static String writeSchemeVariable(Object o) {
        validateType(o, String.class);
        return (String)o;
    }

    public static void validateType(Object o, Class c) {
        if(!c.isInstance(o))
            throw new RuntimeException("Object " + o + " not of type " + c.getName());
    }

    public static String stringify(Object arg) {
        if(arg instanceof Double) {
            return Integer.toString(((Double)arg).intValue());
        }
        //else ifs
        else
            throw new RuntimeException("could not stringify:" + arg);
        
    }
}
