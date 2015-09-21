package org.jax.mgi.imsr.model;

import java.util.HashMap;

public class MgdMaps {
	private HashMap<String, MgiFeature> featureMap;	
	private HashMap<String, String> symbolMap;	
	
	public MgdMaps(HashMap<String, MgiFeature> featureMap, HashMap<String, String> symbolMap) {
		this.featureMap = featureMap;
		this.symbolMap = symbolMap;
	}

	public HashMap<String, MgiFeature> getFeatureMap() {
		return featureMap;
	}

	public HashMap<String, String> getsymbolMap() {
		return symbolMap;
	}

}
