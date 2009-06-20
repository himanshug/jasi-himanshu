package jasi.semantics.procedure;

import jasi.semantics.Environment;

public abstract class Procedure {

    public abstract Object apply(Object args, Environment env);
}
