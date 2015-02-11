package org.jax.mgi.imsr.model;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Set;

import org.jax.mgi.imsr.helpers.Constants;
import org.jax.mgi.imsr.helpers.URLHelper;

public class Record {

	private String id;
	private String name;
	private Set<String> types;
	private Set<String> states;
	private String url;
	private String mgiAlleleAccId;
	private String alleleSymbol;
	private String alleleName;
	private Set<String> mutationTypes;
	private String chromosome;
	private String mgiGeneAccId;
	private String geneSymbol;
	private String geneName;
	private List<String> errors = new ArrayList<String>();
	private List<String> warnings = new ArrayList<String>();
	
	
	public Record() {
		// must have no-argument constructor
	}
	
	public String getId() {
		return id;
	}
	
	public String getName() {
		return name;
	}
	
	public Set<String> getTypes() {
		return types;
	}
		
	public Set<String> getStates() {
		return states;
	}
	
	public String getUrl() {
		return url;
	}
	
	public String getMgiAlleleAccId() {
		return mgiAlleleAccId;
	}
	
	public String getAlleleSymbol() {
		return alleleSymbol;
	}
	
	public String getAlleleName() {
		return alleleName;
	}
	
	public Set<String> getMutationTypes() {
		return mutationTypes;
	}
	
	public String getChromosome() {
		return chromosome;
	}
	
	public String getMgiGeneAccId() {
		return mgiGeneAccId;
	}
	
	public String getGeneSymbol() {
		return geneSymbol;
	}
	
	public String getGeneName() {
		return geneName;
	}
	
	public boolean isValid() {
		return errors.isEmpty();
	}
	
	public List<String> getErrors() {
		return errors;
	}
	
	public void addError(String error) {
		if (error != null) {
			errors.add(error);
		}
	}
		
	public boolean hasWarning() {
		return !warnings.isEmpty();
	}

	public List<String> getWarnings() {
		return warnings;
	}
	
	public void addWarning(String warning) {
		if (warning != null) {
			warnings.add(warning);
		}
	}
		
	public void setId(String id) {
		this.id = id;
	}

	public void setName(String name) {
		this.name = name;
	}

	public void setTypes(Set<String> types) {
		this.types = types;
	}

	public void setStates(Set<String> states) {
		this.states = states;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public void setMgiAlleleAccId(String mgiAlleleAccId) {
		this.mgiAlleleAccId = mgiAlleleAccId;
	}

	public void setAlleleSymbol(String alleleSymbol) {
		this.alleleSymbol = alleleSymbol;
	}

	public void setAlleleName(String alleleName) {
		this.alleleName = alleleName;
	}

	public void addMutationType(String mutationType) {
		if (mutationType != null && Constants.MUTATION_TYPES.containsKey(mutationType)) {
			mutationTypes.add(mutationType);
		} else {
			System.out.println("ERROR: Mutation ype is invalid [" +  mutationType);
		}
	}

	public void setMutationTypes(Set<String> mutationTypes) {
		this.mutationTypes = mutationTypes;
	}

	public void setChromosome(String chromosome) {
		this.chromosome = chromosome;
	}

	public void setMgiGeneAccId(String mgiGeneAccId) {
		this.mgiGeneAccId = mgiGeneAccId;
	}

	public void setGeneSymbol(String geneSymbol) {
		this.geneSymbol = geneSymbol;
	}

	public void setGeneName(String geneName) {
		this.geneName = geneName;
	}

	@Override
	public String toString() {
		return "Strain [id=" + id + ", name=" + name + ", types=" + types.toString()
				+ ", states=" + states.toString() + ", url=" + url + ", mgiAlleleAccId="
				+ mgiAlleleAccId + ", alleleSymbol=" + alleleSymbol
				+ ", alleleName=" + alleleName + ", mutations="
				+ mutationTypes.toString() + ", chromosome=" + chromosome
				+ ", mgiGeneAccId=" + mgiGeneAccId + ", geneSymbol="
				+ geneSymbol + ", geneName=" + geneName + ", isValid=" + isValid() + "]";
	}
	

	public void validateMgiGeneAccId(HashMap<String, MgiFeature> geneMap) {
		if (this.mgiGeneAccId != null && !this.mgiGeneAccId.isEmpty()) {
			if (!geneMap.containsKey(this.mgiGeneAccId)) {
				if (this.geneSymbol == null || this.geneSymbol.isEmpty()) {
					this.addWarning("Strain Gene Accession ID is invalid: [" + this.mgiGeneAccId + "]");
				}
			}
		}
	}


	public void validateMgiAlleleAccId(HashMap<String,MgiFeature> alleleMap) {
		if (this.mgiAlleleAccId != null && !this.mgiAlleleAccId.isEmpty()) {
			if (!alleleMap.containsKey(mgiAlleleAccId)) {
				if (this.alleleSymbol == null || this.alleleSymbol.isEmpty()) {
					this.addWarning("Strain Allele Accession ID is invalid: [" + mgiAlleleAccId + "]");
				}
			}
		}
	}


	public void validateId() {
		if (this.id == null || this.id.isEmpty()) {
			this.addError("Strain ID: is a required field");
		}
	}

	
	public void validateUrl() {
		if (this.url == null || this.url.isEmpty()) {
			this.addError("Strain URL: is a required field");
		}		
	}

	
	public void validateStates() {
		if (this.states == null || this.states.isEmpty()) {
			this.addError("Strain State: is a required field");
		}
		
		if (Constants.STRAIN_STATES.keySet().containsAll(getStates()) == false) {
			this.addError("Strain State: contains an invalid state: [" + getStates().toString() + "]");
		}	
	}


	public void validateTypes() {
		if (this.types == null || this.types.isEmpty()) {
			this.addError("Strain Type: is a required field");
		}
		
		if (Constants.STRAIN_TYPES.keySet().containsAll(getTypes()) == false) {
			this.addError("Strain Type: contains an invalid type: [" + getTypes().toString() + "]");
		}	
	}


	public void validateName() {
		if (this.id == null || this.id.isEmpty()) {
			this.addError("Strain Name: is a required field");
		}
	}

	
	public void validateMutationType() {
		if (this.mutationTypes != null && !this.mutationTypes.isEmpty()) {
			if (Constants.MUTATION_TYPES.keySet().containsAll(getMutationTypes()) == false) {
				this.addWarning("Strain Mutation Type: contains an invalid mutation type: [" + getMutationTypes().toString() + "]");
			}	
		}
	}


	public void transformAllele(MgdAlleleMaps alleleFeaturesMap, List<String> recombinaseAlleleList) {		
		HashMap<String, MgiFeature> alleleMap = alleleFeaturesMap.getFeatureMap();
		HashMap<String, String> alleleSymbolMap = alleleFeaturesMap.getsymbolMap();

		if (mgiAlleleAccId != null && !mgiAlleleAccId.isEmpty() && alleleMap.containsKey(mgiAlleleAccId)) {			
			// ignore repository information and use mgi data
		} else if (alleleSymbol != null && !alleleSymbol.isEmpty() && alleleSymbolMap.containsKey(alleleSymbol)) {
			// find accession id, to create a valid link
			mgiAlleleAccId = alleleSymbolMap.get(alleleSymbol);			
		} else {
			// prevent link to invalid allele accid
			mgiAlleleAccId = null;
		}
		
		if (mgiAlleleAccId != null) {
			// ignore repository information and use mgi data
			alleleSymbol = alleleMap.get(mgiAlleleAccId).getSymbol();
			alleleName = alleleMap.get(mgiAlleleAccId).getName();
			
			if (recombinaseAlleleList.contains(mgiAlleleAccId)) {
				addMutationType("REC");
			}
		}
	}


	public void transformGene(HashMap<String, MgiFeature> geneMap, HashMap<String, String> withdrawnMarkersMap) {
		if (mgiGeneAccId != null && !mgiGeneAccId.isEmpty() && geneMap.containsKey(mgiGeneAccId)) {
			// ignore repository information and use mgi data
			geneSymbol = geneMap.get(mgiGeneAccId).getSymbol();
			geneName = geneMap.get(mgiGeneAccId).getName();						
		} else {
			//prevent link to invalid gene accid
			mgiGeneAccId = null;
			
			//add withdrawn information
			if (geneSymbol != null && !geneSymbol.isEmpty() && withdrawnMarkersMap.containsKey(geneSymbol)) {
				geneName = withdrawnMarkersMap.get(geneSymbol);
			}
		}		
	}


	public void transformUrl(String strainUrl) {
		if (strainUrl != null && !strainUrl.isEmpty()) {
			url = strainUrl.replace("###STRAINURL###", id);
		}
	}

	
	public void testUrlConnection() {
		int code;
		
		try {
			code = URLHelper.getHTTPResponseStatusCode(this.url);
			
			if (code >= 300 && code < 400) {
				this.addWarning("Strain URL: [" + this.url + "]" + " with a redirect response code = " + code);
			}
			else if (code != 200) {
				this.addError("Strain URL: [" + this.url + "]" + " with response code = " + code);
			}
			
		} catch (IOException e) {
			this.addError("EXCEPTION - Strain: " + this.id + " - URL: [" + this.url + "]" + " reports " + e.getMessage());
		}
	}

}

