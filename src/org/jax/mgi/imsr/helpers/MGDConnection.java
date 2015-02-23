package org.jax.mgi.imsr.helpers;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.jax.mgi.imsr.model.MgdAlleleMaps;
import org.jax.mgi.imsr.model.MgiFeature;



public class MGDConnection {

	private static String url = "jdbc:postgresql://adhoc.informatics.jax.org/mgd";
//	private static String url = "jdbc:postgresql://mgi-adhoc.jax.org/mgd";
	
    /**
     * Runs a sql query on server (adhoc) and returns a result set.
     * 
     * @param query	- sql query string to run on server
     * @return		the result set from running the query on the server
     */
    public static ResultSet executeQuery(String query) {
    	ResultSet results = null;
    	
    	try {
    		Class.forName("org.postgresql.Driver");
    		
			Connection connection = DriverManager.getConnection(url, "mgd_public", "mgdpub");
			PreparedStatement stmt = connection.prepareStatement(query);
	    	
			results = stmt.executeQuery();
	    	connection.close();

    	} catch (SQLException e) {
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
    	
    	return results;
    }
    
    
	/**
	 * Run a db sql query and populate a hash map with two columns. 
	 * This is a direct population, without any logic.
	 * 
	 * @param query			- db sql query to run
	 * @param colunmName1	- column name for the first tuple of the hash map
	 * @param colunmName2	- column name for the second tuple of the hash map
	 * @return				a hash map populated with results from the sql query
	 */
	public static HashMap<String, String> simpleHashMapQuery(String query, String colunmName1, String colunmName2) {
		HashMap<String, String> resultHashMap = new HashMap<String, String>();
    	    	
    	try {
    		ResultSet results = executeQuery(query);
			while (results.next()) {
				resultHashMap.put(results.getString(colunmName1), results.getString(colunmName2));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
    	
    	return resultHashMap;
    }


	/**
	 * Run a db sql query and populate a list with one column. 
	 * This is a direct population, without any logic.
	 * 
	 * @param query			- db sql query to run
	 * @param colunmName	- column name to populate the list with
	 * @return				a list populated with results from the sql query
	 */
	public static List<String> simpleListQuery(String query, String colunmName) {
		List<String> resultList = new ArrayList<String>();
    	
    	try {
    		ResultSet results = executeQuery(query);
			while (results.next()) {	
				resultList.add(results.getString(colunmName));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
    	
    	return resultList;
    }


    /**
     * Returns a hash map of nomenclature for all strains from a specific repository.
     * 
     * @param logicalDB	- logical DB key of repository for nomenclature lookup
     * @return			a hash map of <accession id, strain name> that lists all nomenclature within a logical DB 
     */
    public static HashMap<String, String> getRepoNomenclature(Integer logicalDB) {
    	String query = "SELECT a.accid, s.strain " +
		               "FROM ACC_Accession a, PRB_Strain s " +
    			       "WHERE a._logicaldb_key = " + logicalDB + " " +
    			       "AND a._object_key = s._strain_key";
    	    	
    	return simpleHashMapQuery(query, "accid", "strain");
    }

 
    /**
     * Returns a hash map of all synonyms and the strain associated with the synonym.
     * 
     * @return	a hash map of <synonym, strain name> that lists all synonyms and the strain associated with the synonym
     */
    public static HashMap<String, String> getInverseSynonyms() {
    	String query = "SELECT syn.synonym, str.strain " +
		               "FROM MGI_Synonym syn, PRB_Strain str " +
    			       "WHERE syn._mgitype_key = 10 " +
    			       "AND syn._object_key = str._strain_key";
    	
    	return simpleHashMapQuery(query, "synonym", "strain");
    }


	/**
	 * Returns a hash map of all withdrawn markers.
	 * 
	 * @return	a hash map of <symbol, name> that lists all withdrawn markers
	 */
	public static HashMap<String, String> getWithdrawnMarkers() {
    	String query = "SELECT m.symbol, m.name " +
		               "FROM MRK_Marker m " +
    			       "WHERE m._marker_status_key = 2";
    	
    	return simpleHashMapQuery(query, "symbol", "name");
    }


	/**
	 * Returns a list of strain names for all repositories provided.
	 * 
	 * @param logicalDBs	- list of repository logical DB's to gather strain names from
	 * @return				- a lists all nomenclature within the specified logical DBs
	 */
	public static List<String> getAllNomenclatures(List<Integer> logicalDBs) {
    	String query = "SELECT distinct s.strain " +
		               "FROM ACC_Accession a, PRB_Strain s " +
    			       "WHERE a._logicaldb_key IN (" + StringUtils.join(logicalDBs, ',') + ") " +
    			       "AND a._object_key = s._strain_key";
    	
    	return simpleListQuery(query, "strain");
    }

	/**
	 * Returns a list of accession ids of all recombinase alleles.
	 * 
	 * @return	a list of accession ids of all recombinase alleles
	 */
	public static List<String> getRecombinaseAlleles() {
    	String query = "SELECT acc.accid " +
    				   "FROM ACC_Accession acc, ALL_Allele a, VOC_Term t, VOC_Annot v " +
    				   "WHERE acc._mgitype_key = 11 " +
    				   "AND acc.private = 0 " +
    				   "AND v._AnnotType_key = 1014 " +
    				   "AND acc._object_key = a._allele_key " +
    				   "AND v._object_key = a._allele_key " +
    				   "AND  v._Term_key = t._Term_key " +
    				   "AND t.term = 'Recombinase'";

    	return simpleListQuery(query, "accid");
	}

    /**
     * Returns a hash map of all strains and their associated synonyms.
     * 
     * @return	a hash map of <strain, list of synonyms> for all strains
     */
    public static HashMap<String, List<String>> getSynonyms() {
		HashMap<String, List<String>> resultHashMap = new HashMap<String, List<String>> ();
    	String query = "SELECT syn.synonym, str.strain " +
		               "FROM MGI_Synonym syn, PRB_Strain str " +
    			       "WHERE syn._mgitype_key = 10 " +
    			       "AND syn._object_key = str._strain_key";
    	  	
    	try {
    		ResultSet results = executeQuery(query);
			while (results.next()) {
				String strain = results.getString("strain");
				if (!resultHashMap.containsKey(strain)) {
					resultHashMap.put(strain, new ArrayList<String>());
				}
				List<String> synonyms = resultHashMap.get(strain);
				synonyms.add(results.getString("synonym"));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
    	
    	return resultHashMap;
    }

	/**
	 * Returns two hash maps of alleles and associated information:
	 * 	(1) alleleFeatureMap - a hash map of <accid, <allele symbol, allele name>> for all alleles
	 * 	(2) alleleSymbolMap  - a hash map of <allele symbol, accid> for all alleles
	 * 
	 * @return	a hash map of <alleleFeatureMap, alleleSymbolMap> for all alleles
	 */
	public static MgdAlleleMaps getAlleleMaps() {
		HashMap<String, MgiFeature> alleleFeatureMap = new HashMap<String, MgiFeature>();
		HashMap<String, String> alleleSymbolMap = new HashMap<String, String>();
    	String query = "SELECT acc.accid, a.symbol, a.name " +
		               "FROM ACC_Accession acc, ALL_Allele a " +
    			       "WHERE acc._mgitype_key = 11 " +
    			       "AND acc._object_key = a._allele_key " +
    			       "AND acc.private = 0";
    	
    	try {
    		ResultSet results = executeQuery(query);
			while (results.next()) {	
				MgiFeature alleleDetail = new MgiFeature(results.getString("symbol"), results.getString("name"));
				alleleFeatureMap.put(results.getString("accid"), alleleDetail);
				alleleSymbolMap.put(results.getString("symbol"), results.getString("accid"));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
    	
		MgdAlleleMaps mgdAlleleMaps = new MgdAlleleMaps(alleleFeatureMap, alleleSymbolMap);
		return mgdAlleleMaps;
	}

	/**
	 * Returns a hash map of all accession ids and their associated genes.
	 * 
	 * @return	a hash map of <accid, <gene symbol, gene name>> of all genes
	 */
	public static HashMap<String, MgiFeature> getGenes() {
		HashMap<String, MgiFeature> resultFeatureMap = new HashMap<String, MgiFeature>();
    	String query = "SELECT acc.accid, m.symbol, m.name " +
		               "FROM ACC_Accession acc, MRK_Marker m " +
    			       "WHERE acc._mgitype_key = 2 " +
    			       "AND acc._object_key = m._marker_key " +
    			       "AND acc.private = 0";
    	    	
    	try {
        	ResultSet results = executeQuery(query);
			while (results.next()) {
				MgiFeature geneDetails = new MgiFeature(results.getString("symbol"), results.getString("name"));
				resultFeatureMap.put(results.getString("accid"), geneDetails);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
    	
    	return resultFeatureMap;
	}


}
