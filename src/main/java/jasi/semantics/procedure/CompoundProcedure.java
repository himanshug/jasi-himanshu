package jasi.semantics.procedure;

import java.util.ArrayList;
import java.util.List;

import jasi.Constants;
import jasi.datatype.SPair;
import jasi.datatype.SVariable;
import jasi.semantics.Environment;
import jasi.semantics.Scheme;

public class CompoundProcedure extends Procedure {

    //environment at the time of creation of this compound
    //procedure
    //we keep a handle to it to provide lexically scoped
    //free variables
    private Environment creationEnv;

    //argument list
    //Note: right now we support fixed length argument list only
    private List<SVariable> argVars;
    //body
    private SPair body; //this will be a valid begin expression.

    public CompoundProcedure(List<SVariable> argVars, Object bodyArg,
                                Environment env) {
        this.argVars = argVars;
        this.body = new SPair(SVariable.getInstance(Constants.KEYWORD_BEGIN), bodyArg);
        this.creationEnv = env;
    }

    //NOTE: this implementation doesn't have tail recursion optimization 
    public Object apply(Object o, Environment env) {
        //the environment coming in the input is ignored, rather the one
        //at the time of creation of this procedure is used for all purposes
        //this is to provide lexically scoped free variables or else the impl
        //will become that of dynamically scoped free variables.
        
        ArrayList<Object> args = null;
        if(o != null) {
            if(o instanceof ArrayList)
                args = (ArrayList)o;
            else
                throw new RuntimeException("arguments not ArrayList:" + o);
        }

        //check length of provided arguments
        int lenInputArgs = (args == null)? 0 : args.size();
        int lenArgVars = (argVars == null)? 0 : argVars.size();
        
        if(lenArgVars != lenInputArgs)
            throw new RuntimeException("expecing " + lenArgVars + " arguments, but" +
                    " received " + lenInputArgs + ".");
        
        if(lenInputArgs == 0)
            env = creationEnv;
        else {
            env = creationEnv.extendEnvironment(argVars, args);
        }
        return Scheme.eval(body, env);
    }

    public String toString() {
        return "#<Compound-Procedure>#";
    }
}
