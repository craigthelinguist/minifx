package exprs;

import java.util.HashSet;
import java.util.Set;

import ctxs.Runtime;
import ctxs.TypeContext;
import fx.Effect;
import types.Type;
import types.Types;

public class Nil implements Expr {

	@Override
	public Expr reduce(Runtime ctx) {
		return this;
	}

	@Override
	public Type typeCheck(TypeContext ctx) {
		return Types.UnitType();
	}

	@Override
	public Set<Effect> effectCheck(TypeContext ctx) {
		return new HashSet<>();
	}
	
	@Override
	public String toString() {
		return "nil";
	}
	
}
