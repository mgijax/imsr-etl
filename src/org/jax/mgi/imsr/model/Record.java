package org.jax.mgi.imsr.model;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Set;

import org.jax.mgi.imsr.helpers.Constants;
import org.jax.mgi.imsr.helpers.URLHelper;
import org.jax.mgi.imsr.helpers.Utilities;

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
			System.out.println("ERROR: Mutation type is invalid [" + mutationType + "]");
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
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Record other = (Record) obj;
		if (alleleName == null) {
			if (other.alleleName != null)
				return false;
		} else if (!alleleName.equals(other.alleleName))
			return false;
		if (alleleSymbol == null) {
			if (other.alleleSymbol != null)
				return false;
		} else if (!alleleSymbol.equals(other.alleleSymbol))
			return false;
		if (chromosome == null) {
			if (other.chromosome != null)
				return false;
		} else if (!chromosome.equals(other.chromosome))
			return false;
		if (errors == null) {
			if (other.errors != null)
				return false;
		} else if (!errors.equals(other.errors))
			return false;
		if (geneName == null) {
			if (other.geneName != null)
				return false;
		} else if (!geneName.equals(other.geneName))
			return false;
		if (geneSymbol == null) {
			if (other.geneSymbol != null)
				return false;
		} else if (!geneSymbol.equals(other.geneSymbol))
			return false;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		if (mgiAlleleAccId == null) {
			if (other.mgiAlleleAccId != null)
				return false;
		} else if (!mgiAlleleAccId.equals(other.mgiAlleleAccId))
			return false;
		if (mgiGeneAccId == null) {
			if (other.mgiGeneAccId != null)
				return false;
		} else if (!mgiGeneAccId.equals(other.mgiGeneAccId))
			return false;
		if (mutationTypes == null) {
			if (other.mutationTypes != null)
				return false;
		} else if (!mutationTypes.equals(other.mutationTypes))
			return false;
		if (name == null) {
			if (other.name != null)
				return false;
		} else if (!name.equals(other.name))
			return false;
		if (states == null) {
			if (other.states != null)
				return false;
		} else if (!states.equals(other.states))
			return false;
		if (types == null) {
			if (other.types != null)
				return false;
		} else if (!types.equals(other.types))
			return false;
		if (url == null) {
			if (other.url != null)
				return false;
		} else if (!url.equals(other.url))
			return false;
		if (warnings == null) {
			if (other.warnings != null)
				return false;
		} else if (!warnings.equals(other.warnings))
			return false;
		return true;
	}

	
	@Override
	public String toString() {
		return "Record [id=" + id + ", name=" + name + ", types=" + types + ", states=" + states + ", url=" + url
				+ ", mgiAlleleAccId=" + mgiAlleleAccId + ", alleleSymbol=" + alleleSymbol + ", alleleName=" + alleleName
				+ ", mutationTypes=" + mutationTypes + ", chromosome=" + chromosome + ", mgiGeneAccId=" + mgiGeneAccId
				+ ", geneSymbol=" + geneSymbol + ", geneName=" + geneName + ", errors=" + errors + ", warnings="
				+ warnings + "]";
	}

	
	
	public static Record createRecord(String line, Boolean omitStrainUrl) {
		Record record = new Record();
		String values[] = line.split("\t", -1);
		
		for (int i = 0; i < values.length; i++) {
			values[i] = values[i].trim();
		}
		
		Integer urlOffset = (omitStrainUrl) ? 0 : 1;
		
		if (values.length > 0) {
			record.setId(values[0]);
		}
		
		if (values.length > 1) {
			record.setName(values[1]);
		}
		
		if (values.length > 2) {
			record.setTypes(Utilities.valuesIntoSet(values[2].toUpperCase()));
		}
		
		if (values.length > 3) {
			record.setStates(Utilities.valuesIntoSet(values[3].toUpperCase()));
		}
		
		if (values.length > 4 && !omitStrainUrl) {
			record.setUrl(values[4]);
		}
		
		if (values.length > 4 + urlOffset) {
			record.setMgiAlleleAccId(values[4 + urlOffset]);
		}
		
		if (values.length > 5 + urlOffset) {
			record.setAlleleSymbol(values[5 + urlOffset]);
		}
		
		if (values.length > 6 + urlOffset) {
			record.setAlleleName(values[6 + urlOffset]);
		}
		
		if (values.length > 7 + urlOffset) {
			record.setMutationTypes(Utilities.valuesIntoSet(values[7 +urlOffset].toUpperCase()));
		}
		
		if (values.length > 8 + urlOffset) {
			record.setChromosome(values[8 + urlOffset]);
		}
		
		if (values.length > 9 + urlOffset) {
			record.setMgiGeneAccId(values[9 + urlOffset]);
		}
		
		if (values.length > 10 + urlOffset) {
			record.setGeneSymbol(values[10 + urlOffset]);
		}
		
		if (values.length > 11 + urlOffset) {
			record.setGeneName(values[11 + urlOffset]);
		}
		
		return record;
	}

	
	
	/**
	 * Checks if gene accession id is a valid accession id.
	 * Adds a warning to the record, if invalid.
	 * 
	 * @param geneMap	- map of valid gene accession id's
	 */
	public void validateMgiGeneAccId(HashMap<String, MgiFeature> geneMap) {
		if (this.mgiGeneAccId != null && !this.mgiGeneAccId.isEmpty()) {
			if (!geneMap.containsKey(this.mgiGeneAccId)) {
				this.addWarning("Strain Gene Accession ID is invalid: [" + this.mgiGeneAccId + "]");
			}
		}
	}

	/**
	 * Checks if allele accession id is a valid accession id.
	 * Adds a warning to the record, if invalid.
	 * 
	 * @param alleleMap	- map of valid allele accession id's
	 */
	public void validateMgiAlleleAccId(HashMap<String, MgiFeature> alleleMap) {
		if (this.mgiAlleleAccId != null && !this.mgiAlleleAccId.isEmpty()) {
			if (!alleleMap.containsKey(mgiAlleleAccId)) {
				this.addWarning("Strain Allele Accession ID is invalid: [" + mgiAlleleAccId + "]");
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
		} else if (Constants.STRAIN_STATES.keySet().containsAll(getStates()) == false) {
			this.addError("Strain State: contains an invalid state: [" + getStates().toString() + "]");
		}
	}

	public void validateTypes() {
		if (this.types == null || this.types.isEmpty()) {
			this.addError("Strain Type: is a required field");
		} else if (Constants.STRAIN_TYPES.keySet().containsAll(getTypes()) == false) {
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

	/**
	 * Using allele accession id, replace symbol and name with mgi data and add recombinase mutation type (if needed).
	 * If invalid allele acc id, use allele symbol (lowercase) to find acc id.
	 * 
	 * @param alleleMgdMaps		- map of valid allele accession id's
	 * @param recombinaseAlleleList	- mgi known allele recombinase list 
	 */
	public void transformAllele(MgdMaps alleleMgdMaps, List<String> recombinaseAlleleList) {
		HashMap<String, MgiFeature> alleleMap = alleleMgdMaps.getFeatureMap();
		HashMap<String, String> alleleSymbolMap = alleleMgdMaps.getsymbolMap();

		if (mgiAlleleAccId != null && !mgiAlleleAccId.isEmpty() && alleleMap.containsKey(mgiAlleleAccId)) {
			// ignore repository information and use mgi data
		} else if (alleleSymbol != null && !alleleSymbol.isEmpty() && alleleSymbolMap.containsKey(alleleSymbol.toLowerCase())) {
			// find accession id, to create a valid link
			mgiAlleleAccId = alleleSymbolMap.get(alleleSymbol.toLowerCase());
		} else {
			// prevent link to invalid allele accid
			mgiAlleleAccId = null;
		}

		if (mgiAlleleAccId != null) {
			// ignore repository information and use mgi data
			alleleSymbol = alleleMap.get(mgiAlleleAccId).getSymbol();
			alleleName = alleleMap.get(mgiAlleleAccId).getName();
			
			// add recombinase information
			if (recombinaseAlleleList.contains(mgiAlleleAccId)) {
				addMutationType("REC");
			}
		}
	}

	/**
	 * Using gene accession id, replace symbol and name with mgi data or add withdrawn information (if needed).
	 * 
	 * @param geneMgdMaps		- map of valid gene accession id's
	 * @param withdrawnMarkersMap	- mgi list of withdrawn gene accession id's 
	 */
	public void transformGene(MgdMaps geneMgdMaps, HashMap<String, String> withdrawnMarkersMap) {
		HashMap<String, MgiFeature> geneMap = geneMgdMaps.getFeatureMap();
		HashMap<String, String> geneSymbolMap = geneMgdMaps.getsymbolMap();

		if (mgiGeneAccId != null && !mgiGeneAccId.isEmpty() && geneMap.containsKey(mgiGeneAccId)) {
			// ignore repository information and use mgi data
		} else if (geneSymbol != null && !geneSymbol.isEmpty() && geneSymbolMap.containsKey(geneSymbol)) {
			// find gene id, to create a valid link
			mgiGeneAccId = geneSymbolMap.get(geneSymbol);
		} else {
			// prevent link to invalid gene accid
			mgiGeneAccId = null;
		}

		if (mgiGeneAccId != null) {	
			// replace repository information with  mgi data
			geneSymbol = geneMap.get(mgiGeneAccId).getSymbol();
			geneName = geneMap.get(mgiGeneAccId).getName();
		} else if (geneSymbol != null && !geneSymbol.isEmpty() && withdrawnMarkersMap.containsKey(geneSymbol)) {
			// add withdrawn information	
			geneName = withdrawnMarkersMap.get(geneSymbol);
		}
	}

	/**
	 * Insert id in strain url
	 * 
	 * @param strainUrl	- url string for insertion of id
	 */
	public void transformUrl(String strainUrl) {
		if (strainUrl != null && !strainUrl.isEmpty()) {
			url = strainUrl.replace("###STRAINURL###", id);
		}
	}

	/**
	 * Check response status code of url. If code is a warning or error, add warning or error
	 * to respective lists.
	 */
	public void testUrlConnection() {
		int code;

		try {
			code = URLHelper.getHTTPResponseStatusCode(this.url);

			if (code >= 300 && code < 400) {
				this.addWarning("Strain URL: [" + this.url + "]" + " with a redirect response code = " + code);
			} else if (code != 200) {
				this.addError("Strain URL: [" + this.url + "]" + " with response code = " + code);
			}

		} catch (IOException e) {
			this.addError("EXCEPTION - Strain: " + this.id + " - URL: [" + this.url + "]" + " reports " + e.getMessage());
		}
	}

}
