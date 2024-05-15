package org.jax.informatics.imsr.model;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import org.apache.solr.client.solrj.beans.Field;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class Solr3Strain {
	
	@Field("provider")
	private String facility;
	
	@Field
	private String id;
	
	@Field("strain_id")
	private String strainId;
	
	@Field("strain_standard")
	private String standard;
	
	@Field("strain_name")
	private String designation;
	
	@Field("strain_synonyms")
	private Set<String> synonyms = new HashSet<String>();
	
	@Field("strain_states")
	private Set<String> states  = new HashSet<String>();
	
	@Field("strain_types")
	private Set<String> types = new HashSet<String>();
	
	@Field("strain_url")
	private String url;
	
	@Field("strain_orderLink")
	private String orderLink;
	
	@Field("gene_names")
	private Set<String> geneNames = new HashSet<String>();
	
	@Field("gene_symbols")
	private Set<String> geneSymbols = new HashSet<String>();
	
	@Field("gene_id")
	private Set<String> geneIds = new HashSet<String>();
	
	@Field("allele_names")
	private Set<String> alleleNames = new HashSet<String>();
	
	@Field("allele_symbols")
	private Set<String> alleleSymbols = new HashSet<String>();
	
	@Field("allele_id")
	private Set<String> alleleIds = new HashSet<String>();
	
	@Field("mutations")
	private Set<String> mutationTypes = new HashSet<String>();
	
	@Field("strain_associations")
	private String associations;
	
	@Field("timestamp")
	private Date date;

	
	public Solr3Strain(Strain strain, Repository repository) {
		JSONArray jsonMutations = new JSONArray();
		
		this.facility = repository.getId();
		this.id = strain.getId();
		this.strainId = strain.getPrefixId();
		this.standard = strain.getNomenclatureFlag().toString();
		this.designation = strain.getName();
		this.synonyms.addAll(strain.getSynonyms());
		this.states.addAll(strain.getStates());
		this.types.addAll(strain.getTypes());
		this.url = strain.getUrl();
		this.orderLink = strain.getOrderLink();
		this.date = repository.getFileLastModified();
		
		for (Mutation m : strain.getMutations()) {
			this.geneNames.add(m.getGene().getName());
			this.geneSymbols.add(m.getGene().getSymbol());
			this.geneIds.add(m.getGene().getMgiId());
			this.alleleNames.add(m.getAllele().getName());
			this.alleleSymbols.add(m.getAllele().getSymbol());
			this.alleleIds.add(m.getAllele().getMgiId());
			this.mutationTypes.addAll(m.getTypes());
			
			jsonMutations.put(createJsonMutation(m));			
		}
		
		this.associations = jsonMutations.toString();
	}
		
	
	private JSONObject createJsonMutation(Mutation m) {
		JSONObject jsonAllele = new JSONObject();
		JSONObject jsonMarker = new JSONObject();
		
		try {
			jsonMarker.put("name", m.getGene().getName());
			jsonMarker.put("symbol", m.getGene().getSymbol());
			jsonMarker.put("mgiId", m.getGene().getMgiId());
			
			jsonAllele.put("marker", jsonMarker);
			jsonAllele.put("mutationTypes", m.getTypes());
			jsonAllele.put("name", m.getAllele().getName());
			jsonAllele.put("symbol", m.getAllele().getSymbol());
			jsonAllele.put("mgiId", m.getAllele().getMgiId());
			
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return jsonAllele;
	}


	public String getFacility() {
		return facility;
	}

	public String getId() {
		return id;
	}

	public String getStrainId() {
		return strainId;
	}

	public String getStandard() {
		return standard;
	}

	public String getDesignation() {
		return designation;
	}

	public Set<String> getSynonyms() {
		return synonyms;
	}

	public Set<String> getStates() {
		return states;
	}

	public Set<String> getTypes() {
		return types;
	}

	public String getUrl() {
		return url;
	}

	public String getOrderLink() {
		return orderLink;
	}

	public Set<String> getGeneNames() {
		return geneNames;
	}

	public Set<String> getGeneSymbols() {
		return geneSymbols;
	}

	public Set<String> getGeneIds() {
		return geneIds;
	}

	public Set<String> getAlleleNames() {
		return alleleNames;
	}

	public Set<String> getAlleleSymbols() {
		return alleleSymbols;
	}

	public Set<String> getAlleleIds() {
		return alleleIds;
	}

	public Set<String> getMutationTypes() {
		return mutationTypes;
	}

	public String getAssociations() {
		return associations;
	}

	public Date getDate() {
		return date;
	}
	
}
