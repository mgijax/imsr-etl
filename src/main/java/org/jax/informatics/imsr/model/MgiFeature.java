package org.jax.informatics.imsr.model;

public class MgiFeature {
	private String symbol;	
	private String name;	
	private String mgiId;
	
	public MgiFeature(String symbol, String name) {
		this.symbol = symbol;
		this.name = name;
	}

	public MgiFeature(String id, String symbol, String name) {
		this.mgiId = id;
		this.symbol = symbol;
		this.name = name;
	}

	public String getSymbol() {
		return symbol;
	}

	public String getName() {
		return name;
	}

	public String getMgiId() {
		return mgiId;
	}	
	
}
