package org.jax.mgi.imsr.model;

import java.util.HashMap;

public class MgdAlleleMaps {
	private HashMap<String, MgiFeature> featureMap;	
	private HashMap<String, String> symbolMap;	
	
	public MgdAlleleMaps(HashMap<String, MgiFeature> featureMap, HashMap<String, String> symbolMap) {
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
