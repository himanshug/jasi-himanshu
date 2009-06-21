package jasi.semantics;

import jasi.Pair;

public class Utils {

    //empty list
    public static boolean isEmptyList(Object o) {
        return (o == null);
    }

    public static boolean isSchemeList(Object o) {
        if(o == null)
            return true;
        
        if(o instanceof Pair) {
            return isSchemeList(((Pair)o).getCdr());
        }
        return false;
    }

    public static String writeEmptyList(Object o) {
        return "()";
    }

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

    //pair
    public static boolean isSchemePair(Object o) {
        return (o instanceof Pair);
    }

    public static  String writeSchemePair(Object o) {
        validateType(o, Pair.class);
        Pair p = (Pair)o;

        String result = "(" + write(p.getCar());
        Object tmp = p.getCdr();
        while(tmp != null && tmp instanceof Pair) {
            p = (Pair)tmp;
            result += " " + write(p.getCar());
            tmp = p.getCdr();
        }
        
        if(tmp == null) result += ")";
        else result += (" . " + write(tmp) + ")");

        return result;
    }

    //generic scheme expression writer
    public static String write(Object o) {
        //todo: support booleans, pair
        if(isSchemeChar(o))
            return writeSchemeChar(o);
        else if(isSchemeNumber(o))
            return writeSchemeNumber(o);
        else if(isSchemeString(o))
            return writeSchemeString(o);
        else if(isSchemeVariable(o))
            return writeSchemeVariable(o);
        else if(isSchemePair(o))
            return writeSchemePair(o);
        else if(isEmptyList(o)) //empty list
            return writeEmptyList(o);
        else
            throw new RuntimeException("could not write:" + o);
    }

    public static void validateType(Object o, Class c) {
        if(!c.isInstance(o))
            throw new RuntimeException("Object " + o + " not of type " + c.getName());
    }
}
