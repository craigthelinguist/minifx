package exprs;

import ctxs.Runtime;
import ctxs.TypeContext;
import types.Ref;
import types.Type;

public class Get implements Expr {

	private Expr refToGet;
	
	public Get(Expr ref) {
		this.refToGet = ref;
	}

	@Override
	public Expr reduce(Runtime rtm) {
		Expr loc = refToGet.reduce(rtm);
		if (!(loc instanceof Location))
			throw new RuntimeException("Can only dereference a location.");
		Location location = (Location) loc;
		return rtm.dereference(location);
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((refToGet == null) ? 0 : refToGet.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Get other = (Get) obj;
		if (refToGet == null) {
			if (other.refToGet != null)
				return false;
		} else if (!refToGet.equals(other.refToGet))
			return false;
		return true;
	}

	@Override
	public Type typeCheck(TypeContext ctx) {
		Type refToGetType = refToGet.typeCheck(ctx);
		if (!(refToGetType instanceof Ref))
			throw new RuntimeException("Can only dereference a reference type.");
		Ref reference = (Ref) refToGetType;
		return reference.componentType();
	}

}