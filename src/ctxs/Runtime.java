package ctxs;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import exprs.Expr;
import exprs.Location;
import regions.RegionConst;

public class Runtime {

	private Map<Integer, Expr> store;
	
	private Map<String, Expr> varMapping;

	private Map<RegionConst, List<Location>> regions;
	
	private static int LocationCounter = 0;
	private static int RegionCounter = 0;
	
	public Runtime() {
		this.store = new HashMap<>();
		this.varMapping = new HashMap<>();
		this.regions = new HashMap<>();
	}
	
	private Runtime(Runtime other) {
		this.store = other.store;
		this.varMapping = new HashMap<>();
		this.regions = new HashMap<>();
		for (String key : other.varMapping.keySet()) {
			this.varMapping.put(key, other.varMapping.get(key));
		}
	}
	
	public boolean hasBinding(String name) {
		return this.varMapping.containsKey(name);
	}
	
	public Expr lookupVariable(String name) {
		return this.varMapping.get(name);
	}
	
	public Runtime extend(String varName, Expr value) {
		Runtime rtm2 = new Runtime(this);
		for (String name : varMapping.keySet()) {
			rtm2.varMapping.put(name, varMapping.get(name));
		}
		for (RegionConst r : regions.keySet()) {
			List<Location> locations = new ArrayList<>();
			for (Location l : regions.get(r)) {
				locations.add(l);
			}
			rtm2.regions.put(r, locations);			
		}
		rtm2.varMapping.put(varName, value);
		return rtm2;
	}

	public Runtime extend(String[] varNames, Expr[] bindingsReduced) {
		if (varNames.length != bindingsReduced.length)
			throw new RuntimeException("Must give a binding for each variable name.");
		Runtime rtm2 = new Runtime();
		rtm2.store = this.store;
		for (String varName : varMapping.keySet()) {
			rtm2.varMapping.put(varName, varMapping.get(varName));
		}
		for (RegionConst r : regions.keySet()) {
			List<Location> locations = new ArrayList<>();
			for (Location l : regions.get(r)) {
				locations.add(l);
			}
			rtm2.regions.put(r, locations);			
		}
		for (int i = 0; i < varNames.length; i++) {
			rtm2.varMapping.put(varNames[i], bindingsReduced[i]);
		}
		return rtm2;
	}
	
	public Expr dereference(Location loc) {
		if (!store.containsKey(loc.getLocation()))
			throw new RuntimeException("Dereferencing non-existent location.");
		return store.get(loc.getLocation());
	}
	
	public Location allocate(RegionConst region, Expr init) {
		Location location = new Location(region, Runtime.LocationCounter++);
		if (!this.regions.containsKey(region)) {
			this.regions.put(region, new ArrayList<>());
		}
		this.regions.get(region).add(location);
		this.store.put(location.getLocation(), init);
		return location;
	}
	
	public void memWrite(Location loc, Expr value) {
		if (!store.containsKey(loc.getLocation()))
			throw new RuntimeException("Writing to non-existent location.");
		this.store.put(loc.getLocation(), value);
	}

	public RegionConst makeRegion() {
		RegionConst newRegion = new RegionConst(Runtime.RegionCounter++);
		regions.put(newRegion, new ArrayList<>());
		return newRegion;
	}
	
}
