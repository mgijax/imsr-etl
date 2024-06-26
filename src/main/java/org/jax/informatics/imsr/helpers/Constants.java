package org.jax.informatics.imsr.helpers;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.jax.informatics.imsr.model.ImsrEmailContact;

public class Constants {
	
	// change has to be less than 7 percent, when submitting less strains for a repository 
	public static final double STRAIN_PERCENT_THRESHOLD = 0.93;
	
	// number of invalid records has to be less than 2 percent for passing as a valid repository submission
	public static final double VALID_SUBMISSION_THRESHOLD = 0.02;
	
	
	public static final String REPOSITORY_META_DATA_FILENAME = "repos.xml";
	public static final Integer MAX_STRAINS_TO_WRITE_TO_SOLR_AT_ONCE = 50000;
	public static final Integer MAX_RECORDS_TO_TEST = 250;
	
	public static final String IMSR_EMAIL_CONTACT = "imsr-admin@jax.org";
	public static final String IMSR_EMAIL_NAME = "IMSR Administrator";
	
	// the 'EI' does not add the repo as a prefix for the strain ids (this is legacy behavior)
	public static final List<String> NO_PREFIX_STRAIN_IDS = Collections.unmodifiableList(Arrays.asList("CARD", "JAX"));
	
	public static final ImsrEmailContact[] IMSR_CURATORS_LIST = {
		new ImsrEmailContact("Cynthia.Smith@jax.org", "Cynthia Smith"), 
		new ImsrEmailContact("Michelle.Perry@jax.org", "Michelle Perry"),
		new ImsrEmailContact("Howie.Motenko@jax.org", "Howie Motenko")
	};
	
	public static final Map<String,String> STRAIN_STATES;
	static
	{
		Map<String,String> ssMap = new HashMap<String,String>();
		ssMap.put("CA", "archived");
		ssMap.put("EM", "embryo");
		ssMap.put("ES", "ES Cell");
		ssMap.put("LM", "live");
		ssMap.put("OV", "ovaries");
		ssMap.put("SP", "sperm");
		STRAIN_STATES = Collections.unmodifiableMap(ssMap);
	}

	public static final Map<String,String> STRAIN_TYPES;
	static
	{
		Map<String,String> stMap = new HashMap<String,String>();
		stMap.put("CCO", "closed colony");
		stMap.put("COI", "coisogenic strain");
		stMap.put("CON", "congenic strain");
		stMap.put("CSS", "consomic or chromosome substitution strain");
		stMap.put("IS", "inbred strain");		
		stMap.put("HY", "hybrid");		
		stMap.put("MAH", "major histocompatibility congenic");		
		stMap.put("MIH", "minor histocompatibility congenic");		
		stMap.put("MSK", "mutant stock");		
		stMap.put("MSR", "mutant strain");		
		stMap.put("NON", "noninbred stock");		
		stMap.put("OUT", "outbred stock");		
		stMap.put("RC", "recombinant congenic");		
		stMap.put("RI", "recombinant inbred");		
		stMap.put("SEG", "segregating inbred");		
		stMap.put("UN", "unclassified");
		stMap.put("WDS", "wild-derived inbred strain");
		STRAIN_TYPES = Collections.unmodifiableMap(stMap);
	}

	public static final Map<String,String> MUTATION_TYPES;
	static
	{
		Map<String,String> mtMap = new HashMap<String,String>();
		mtMap.put("CH", "chromosomal aberration");
		mtMap.put("CI", "chemically induced mutation");
		mtMap.put("DEL", "deletion");
		mtMap.put("DP", "duplication");
		mtMap.put("EMM", "endonuclease mediated mutation");
		mtMap.put("GT", "gene trap");
		mtMap.put("INS", "insertion");
		mtMap.put("INV", "inversion");
		mtMap.put("OTH", "other");
		mtMap.put("RAD", "radiation induced mutation");
		mtMap.put("REC", "recombinase(cre/flp)");
		mtMap.put("RB", "Robertsonian translocation");
		mtMap.put("SM", "spontaneous mutation");
		mtMap.put("TG", "transgenic");
		mtMap.put("TL", "reciprocal translocation");
		mtMap.put("TM", "targeted mutation");
		mtMap.put("TP", "transposition");
		MUTATION_TYPES = Collections.unmodifiableMap(mtMap);
	}
	
	public static final Set<String> FAUX_SYMBOLS;
	static
	{
		Set<String> fsSet = new HashSet<String>();
		fsSet.add("CAG");		// CMV-IE enhancer/chicken beta-actin/rabbit beta-globin hybrid promoter
		fsSet.add("CFP");		// Cyan Fluorescent Protein
		fsSet.add("CRE");		// cre recombinase
		fsSet.add("EGFP");		// enhanced green fluorescent protein
		fsSet.add("FLP");		// FLP recombinase
		fsSet.add("GFP");		// Green Fluorescent Protein
		fsSet.add("RFP");		// Red Fluorescent Protein
		fsSet.add("VENUS");		// Green Fluorescent Protein, F46L
		fsSet.add("YFP");		// Yellow Fluorescent Protein
		FAUX_SYMBOLS = Collections.unmodifiableSet(fsSet);
	}
	
	public static final Map<String,String> SOLR_SERVERS;
	static
	{
		Map<String,String> ssMap = new HashMap<String,String>();
		ssMap.put("DEV", "http://localhost:48983/solr/imsr");
		ssMap.put("TEST", "http://bhmgiimsrtest-01LT.jax.org:48983/solr/imsr");
		ssMap.put("TEST2", "http://bhmgiimsrtst02lt.jax.org:48983/solr/imsr");
		ssMap.put("PUBLIC", "http://bhmgiimsr01.jax.org:48983/solr/imsr");
		SOLR_SERVERS = Collections.unmodifiableMap(ssMap);
	}
		
}
