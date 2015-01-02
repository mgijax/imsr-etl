package org.jax.mgi.imsr.model;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class Strain {
	
	private String id;
	private String name;
	private Set<String> types = new HashSet<String>();
	private Set<String> states = new HashSet<String>();
	private String url;
	private Set<Mutation> mutations = new HashSet<Mutation>();
	private String chromosome;
	
	private String prefixId;
	private Character nomenclatureFlag;
	private String imsrMessage = null;
	private Set<String> synonyms = new HashSet<String>();

	public Strain() {
		// must have no-argument constructor
	}
	
	public Strain(Record r) {
		this.id = r.getId();
		this.name = r.getName();
		this.url = r.getUrl();
		this.chromosome = r.getChromosome();

		this.addRecord(r);
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

	public Set<Mutation> getMutations() {
		return mutations;
	}

	public String getChromosome() {
		return chromosome;
	}

	public String getPrefixId() {
		return prefixId;
	}

	public String getImsrMessage() {
		return imsrMessage;
	}

	public Character getNomenclatureFlag() {
		return nomenclatureFlag;
	}

	public Set<String> getSynonyms() {
		return synonyms;
	}

	public boolean hasImsrMessage(){
		return imsrMessage != null;
	}
	
	public void addNomenclatureFlag(List<String> allNomenclaturesList, HashMap<String, String> repoNomenclatureMap, String repoId) {		
		// Prerequisite: call the 'addSynonyms() method first
		
		Boolean foundSynonymInNomenclature = false;

		for (String s : synonyms) {
			foundSynonymInNomenclature |= allNomenclaturesList.contains(s);
		}

		if (allNomenclaturesList.contains(name)) {
			// strain name uses approved nomenclature
			nomenclatureFlag = '+';
			
			if (!repoNomenclatureMap.containsValue(name)) {
				imsrMessage = "with strain name: [" + name + "] is approved nomenclature, ";
				imsrMessage += "however is not annotated to the " + repoId + " repository.";
			}
		} else if (foundSynonymInNomenclature) {
				// strain name does not use approved nomenclature
				nomenclatureFlag = '-';
		} else {
			// mgi doesn't know strain name
			nomenclatureFlag = '?';
		}			
	}


	public void addSynonyms(HashMap<String, List<String>> synonymsMap, HashMap<String, String> inverseSynonymsMap, HashMap<String, String> nomenclatureMap) {
		String mgiNomenName;
		String mgiStrainName;

		// add mgi nomenclature name, if different than strain name
		if (nomenclatureMap.containsValue(name)) {
			mgiNomenName = nomenclatureMap.get(id);
			if (mgiNomenName != null && !mgiNomenName.equals(name)) {
				synonyms.add(mgiNomenName);
			}
		}
		
		if (synonymsMap.containsKey(name)) {
			synonyms.addAll(synonymsMap.get(name));
		}
		
		if (inverseSynonymsMap.containsKey(name)) {
			mgiStrainName = inverseSynonymsMap.get(name);
			synonyms.add(mgiStrainName);
			synonyms.addAll(synonymsMap.get(mgiStrainName));
			
			synonyms.remove(name);
		}
		
	}

	
	public void addPrefixId(String strainidprefix) {
		if (strainidprefix == null) {
			strainidprefix = "";
		}
		
		this.prefixId = strainidprefix + ":" + this.id;		
	}


	public void addRecord(Record r) {
		this.types.addAll(r.getTypes());
		this.states.addAll(r.getStates());
		
		MgiFeature allele = new MgiFeature(r.getMgiAlleleAccId(), r.getAlleleSymbol(), r.getAlleleName());
		MgiFeature gene = new MgiFeature(r.getMgiGeneAccId(), r.getGeneSymbol(), r.getGeneName());		
		this.mutations.add(new Mutation(r.getMutationType(), allele, gene));	
	}

	

}
