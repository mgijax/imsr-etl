package org.jax.informatics.imsr.model;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.jax.informatics.imsr.helpers.Constants;

public class Strain {
	
	private String id;
	private String name;
	private Set<String> types = new HashSet<String>();
	private Set<String> states = new HashSet<String>();
	private String url;
	private String orderLink;
	private Set<Mutation> mutations = new HashSet<Mutation>();
	private String chromosome;
	
	private String prefixId;
	private String mgdLookupId;
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
		this.orderLink = r.getOrderLink();
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

	public String getOrderLink() {
		return orderLink;
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

	public String getMgdLookupId() {
		return mgdLookupId;
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
		// Note: We should be searching for the prefixId instead of the mgdLookupId,
		//       but currently the 'EI' has not added prefixes to CARD and JAX strains.
		//       That is a larger conversation, because MGI has added the 'JAX:' prefix 
		//       in their code to compensate for this situation.
		
		String mgdMappedStrainName = repoNomenclatureMap.get(mgdLookupId);
		Boolean foundStrainNameInMgd = allNomenclaturesList.contains(name);

		if ((mgdMappedStrainName != null) && mgdMappedStrainName.equals(name)) {
			// id maps to correct name in repo data (from mgd)
			nomenclatureFlag = '+';
		} else if (repoNomenclatureMap.containsKey(mgdLookupId) || foundStrainNameInMgd) {
			// mgd has some information, but not a direct mapping
			nomenclatureFlag = '-';
			
			if (foundStrainNameInMgd && !repoNomenclatureMap.containsValue(name)) {
				imsrMessage = "with strain name: [" + name + "] is approved nomenclature, ";
				imsrMessage += "however is not annotated to the " + repoId + " repository.";
			}
		} else {
			// mgd doesn't contain strain name or strain id
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
		String prefix = "";
		
		if (strainidprefix != null) {
			prefix = strainidprefix + ":";
		}
		
		this.prefixId = prefix + this.id;		
	}


	public void addMgdLookupId(String strainidprefix) {
		String prefix = "";
		
		if ((strainidprefix != null) && (!Constants.NO_PREFIX_STRAIN_IDS.contains(strainidprefix))) {
			prefix = strainidprefix + ":";
		}
		
		this.mgdLookupId = prefix + this.id;		
	}


	public void addRecord(Record r) {
		this.types.addAll(r.getTypes());
		this.states.addAll(r.getStates());
		
		MgiFeature allele = new MgiFeature(r.getMgiAlleleAccId(), r.getAlleleSymbol(), r.getAlleleName());
		MgiFeature gene = new MgiFeature(r.getMgiGeneAccId(), r.getGeneSymbol(), r.getGeneName());		
		this.mutations.add(new Mutation(r.getMutationTypes(), allele, gene));	
	}

	

}
