package jasi.semantics.procedure;

import java.util.ArrayList;
import java.util.List;

import jasi.Constants;
import jasi.datatype.SEmptyList;
import jasi.datatype.SPair;
import jasi.datatype.SVariable;
import jasi.semantics.Environment;
import jasi.semantics.Scheme;
import jasi.semantics.Utils;

public class CompoundProcedure extends Procedure {

    //environment at the time of creation of this compound
    //procedure
    //we keep a handle to it to provide lexically scoped
    //free variables
    private Environment creationEnv;

    //argument list
    private List<SVariable> argVars; //fixed variables
    //list of all args appearing after fixed args
    //kept null if procedure has fixed length arguments
    private SVariable restArgs;

    //body
    private SPair body; //this will be a valid begin expression.

    public CompoundProcedure(Object paramArgs, Object bodyArg,
                                Environment env) {
        ArrayList<SVariable> params = null;
        Object tmp = paramArgs;
        if(paramArgs instanceof SVariable) {
            restArgs = (SVariable)paramArgs;
            this.argVars = new ArrayList<SVariable>();
        }
        else {
            while(!(tmp instanceof SEmptyList)) {
                Object o = Utils.first(tmp);
                Utils.validateType(o, SVariable.class);
                if(params == null) params = new ArrayList<SVariable>();
                params.add((SVariable)o);
                
                tmp = Utils.cdr(tmp);
                if(tmp instanceof SVariable) {
                    restArgs = (SVariable)tmp;
                    break;
                }
            }
            this.argVars = params;
        }

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
        
        if(restArgs == null)  {
            if(lenArgVars != lenInputArgs)
                throw new RuntimeException("expecing " + lenArgVars + " arguments, but" +
                        " received " + lenInputArgs + ".");
        }
        else {
            if(lenArgVars > lenInputArgs)
                throw new RuntimeException("expecing atleast " + lenArgVars + " arguments, but" +
                        " received " + lenInputArgs + " only.");
        }

        if(lenInputArgs == 0) {
            env = creationEnv.extendEnvironment();
            if(restArgs != null)
                env.putBinding(restArgs, SEmptyList.getInstance());
        }
        else if(restArgs == null){
            env = creationEnv.extendEnvironment(argVars, args);
        }
        else {
            Object inputRestArgs = SEmptyList.getInstance();
            if(lenInputArgs > lenArgVars) {
                for(int i= args.size()-1; args.size() > lenArgVars; i--)
                    inputRestArgs = Utils.cons(args.remove(i), inputRestArgs);
            }
            env = creationEnv.extendEnvironment(argVars, args);
            env.putBinding(restArgs, inputRestArgs);
        }
        return Scheme.eval(body, env);
    }

    public String toString() {
        return "#<Compound-Procedure>#";
    }
}
