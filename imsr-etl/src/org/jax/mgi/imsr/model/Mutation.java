package org.jax.mgi.imsr.model;

public class Mutation {
	
	private String type;
	private MgiFeature allele;
	private MgiFeature gene;

	public Mutation(String string, MgiFeature allele, MgiFeature gene) {
		this.type = string;
		this.allele = allele;
		this.gene = gene;
	}

	public String getType() {
		return type;
	}

	public MgiFeature getAllele() {
		return allele;
	}

	public MgiFeature getGene() {
		return gene;
	}
	
	
}
