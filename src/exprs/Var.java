package exprs;

import ctxs.Runtime;
import ctxs.TypeContext;
import types.Type;

public class Var implements Expr {

	private String name;
	
	public Var(String name) {
		this.name = name;
	}
	
	@Override
	public Expr reduce(Runtime ctx) {
		if (ctx.hasBinding(name))
			throw new RuntimeException("Variable " + name + " is undefined");
		return ctx.lookupVariable(name);
	}

	@Override
	public Type typeCheck(TypeContext ctx) {
		if (ctx.hasBinding(name)) return ctx.lookupType(name);
		else throw new RuntimeException("Typechecking variable that isn't defined.");
	}

	public String getName() {
		return this.name;
	}

}
