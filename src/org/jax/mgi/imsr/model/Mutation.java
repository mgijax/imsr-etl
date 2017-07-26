package org.jax.mgi.imsr.model;

import java.util.Set;

public class Mutation {
	
	private Set<String> types;
	private MgiFeature allele;
	private MgiFeature gene;

	public Mutation(Set<String> types, MgiFeature allele, MgiFeature gene) {
		this.types = types;
		this.allele = allele;
		this.gene = gene;
	}

	public Set<String> getTypes() {
		return types;
	}

	public MgiFeature getAllele() {
		return allele;
	}

	public MgiFeature getGene() {
		return gene;
	}
	
	
}
