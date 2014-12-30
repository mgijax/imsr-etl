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
    
    public static HashMap<String, String> getRepoNomenclature(Integer logicalDB) {
		HashMap<String, String> resultHashMap = new HashMap<String, String>();
    	String query = "SELECT a.accid, s.strain " +
		               "FROM ACC_Accession a, PRB_Strain s " +
    			       "WHERE a._logicaldb_key = " + logicalDB + " " +
    			       "AND a._object_key = s._strain_key";
    	
    	ResultSet results = executeQuery(query);
    	
    	try {
			while (results.next()) {
				resultHashMap.put(results.getString("accid"), results.getString("strain"));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
    	
    	return resultHashMap;
    }

    public static List<String> getAllNomenclatures(List<Integer> logicalDBs) {
		List<String> resultList = new ArrayList<String>();
    	String query = "SELECT distinct s.strain " +
		               "FROM ACC_Accession a, PRB_Strain s " +
    			       "WHERE a._logicaldb_key IN (" + StringUtils.join(logicalDBs, ',') + ") " +
    			       "AND a._object_key = s._strain_key";
    	
    	ResultSet results = executeQuery(query);
    	
    	try {
			while (results.next()) {	
				resultList.add(results.getString("strain"));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
    	
    	return resultList;
    }

    public static HashMap<String, List<String>> getSynonyms() {
		HashMap<String, List<String>> resultHashMap = new HashMap<String, List<String>> ();
    	String query = "SELECT syn.synonym, str.strain " +
		               "FROM MGI_Synonym syn, PRB_Strain str " +
    			       "WHERE syn._mgitype_key = 10 " +
    			       "AND syn._object_key = str._strain_key";
    	
    	ResultSet results = executeQuery(query);
    	
    	try {
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

    public static HashMap<String, String> getInverseSynonyms() {
		HashMap<String, String> resultHashMap = new HashMap<String, String> ();
    	String query = "SELECT syn.synonym, str.strain " +
		               "FROM MGI_Synonym syn, PRB_Strain str " +
    			       "WHERE syn._mgitype_key = 10 " +
    			       "AND syn._object_key = str._strain_key";
    	
    	ResultSet results = executeQuery(query);
    	
    	try {
			while (results.next()) {
				resultHashMap.put(results.getString("synonym"), results.getString("strain"));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
    	
    	return resultHashMap;
    }

	public static MgdAlleleMaps getAlleleMaps() {
		HashMap<String, MgiFeature> resultFeatureMap = new HashMap<String, MgiFeature>();
		HashMap<String, String> resultSymbolMap = new HashMap<String, String>();
    	String query = "SELECT acc.accid, a.symbol, a.name " +
		               "FROM ACC_Accession acc, ALL_Allele a " +
    			       "WHERE acc._mgitype_key = 11 " +
    			       "AND acc._object_key = a._allele_key " +
    			       "AND acc.private = 0";
    	
    	ResultSet results = executeQuery(query);
    	
    	try {
			while (results.next()) {	
				MgiFeature alleleDetail = new MgiFeature(results.getString("symbol"), results.getString("name"));
				resultFeatureMap.put(results.getString("accid"), alleleDetail);
				resultSymbolMap.put(results.getString("symbol"), results.getString("accid"));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
    	
		MgdAlleleMaps mgdAlleleMaps = new MgdAlleleMaps(resultFeatureMap, resultSymbolMap);
		return mgdAlleleMaps;
	}

	public static List<String> getRecombinaseAlleles() {
		List<String> resultList = new ArrayList<String>();
    	String query = "SELECT acc.accid " +
    				   "FROM ACC_Accession acc, ALL_Allele a, VOC_Term t, VOC_Annot v " +
    				   "WHERE acc._mgitype_key = 11 " +
    				   "AND acc.private = 0 " +
    				   "AND v._AnnotType_key = 1014 " +
    				   "AND acc._object_key = a._allele_key " +
    				   "AND v._object_key = a._allele_key " +
    				   "AND  v._Term_key = t._Term_key " +
    				   "AND t.term = 'Recombinase'";

    	ResultSet results = executeQuery(query);
    	
    	try {
			while (results.next()) {	
				resultList.add(results.getString("accid"));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
    	
    	return resultList;
	}

	public static HashMap<String, MgiFeature> getGenes() {
		HashMap<String, MgiFeature> resultFeatureMap = new HashMap<String, MgiFeature>();
    	String query = "SELECT acc.accid, m.symbol, m.name " +
		               "FROM ACC_Accession acc, MRK_Marker m " +
    			       "WHERE acc._mgitype_key = 2 " +
    			       "AND acc._object_key = m._marker_key " +
    			       "AND acc.private = 0";
    	
    	ResultSet results = executeQuery(query);
    	
    	try {
			while (results.next()) {
				MgiFeature geneDetails = new MgiFeature(results.getString("symbol"), results.getString("name"));
				resultFeatureMap.put(results.getString("accid"), geneDetails);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
    	
    	return resultFeatureMap;
	}

	public static HashMap<String, String> getWithdrawnMarkers() {
		HashMap<String, String> resultHashMap = new HashMap<String, String>();
    	String query = "SELECT m.symbol, m.name " +
		               "FROM MRK_Marker m " +
    			       "WHERE m._marker_status_key = 2";
    	
    	ResultSet results = executeQuery(query);
    	
    	try {
			while (results.next()) {
				resultHashMap.put(results.getString("symbol"), results.getString("name"));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
    	
    	return resultHashMap;
    }

}
