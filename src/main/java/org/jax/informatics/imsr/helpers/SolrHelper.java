package org.jax.informatics.imsr.helpers;

import java.io.IOException;
import java.util.List;

import org.apache.solr.client.solrj.SolrQuery;
import org.apache.solr.client.solrj.SolrServerException;
import org.apache.solr.client.solrj.impl.HttpSolrClient;

import org.jax.informatics.imsr.model.Solr3Strain;

public class SolrHelper {
	
	private HttpSolrClient solrClient = null;
	
	public SolrHelper(String solrClientType) {
		if (Constants.SOLR_SERVERS.containsKey(solrClientType)) {
			String url = Constants.SOLR_SERVERS.get(solrClientType);
			this.solrClient = new HttpSolrClient.Builder(url).build();
		}
	}
	
	
	/**
	 * Deletes all solr documents associated with the repositoryId.
	 * 
	 * @param repositoryId	- specifies repository to delete from solr index
	 * @see solrClient
	 */
	public void dropRepositoryFromSolr(String repositoryId) {
		System.out.println(repositoryId + ": Deleting old records.");
		
		try {			
			solrClient.deleteByQuery(String.format("provider:%s", repositoryId));
			solrClient.commit();
		} catch (SolrServerException | IOException e) {
			e.printStackTrace();
		}
	}

	
	/**
	 * Returns a count of solr documents for a given repository.
	 * 
	 * @param repositoryId	- repository to count strains
	 * @return 				a count of strains from a specified repository
	 * @see solrClient
	 * @see SolrQuery
	 */
	public long getRepositoryStrainCountFromSolr(String repositoryId) {		
		// http://stackoverflow.com/questions/5050746/solr-solrj-how-can-i-determine-the-total-number-of-documents-in-an-index
		long strainCount = 0;
		try {			
			SolrQuery q = new SolrQuery("provider:" + repositoryId);
			q.setRows(0);
			strainCount = solrClient.query(q).getResults().getNumFound();
		} catch (SolrServerException | IOException e) {
			e.printStackTrace();
		}
		return strainCount;
	}


	/**
	 * Adds a list of strains to the solr index, as documents.
	 * 
	 * @param solrStrains 	- a list of strains (in JSON format) to be added to the index
	 * @see solrClient
	 */
	public void writeStrainsToSolr(List<Solr3Strain> solrStrains) {
		System.out.println("Writing " + solrStrains.size() + " strains.");
				
		try {			
			solrClient.addBeans(solrStrains);
			solrClient.optimize();
			solrClient.commit();
		} catch (SolrServerException | IOException e) {
			e.printStackTrace();
		}
	}
	

//	/**
//	 * Commits any solr changes
//	 */
//	public void solrCommit() {
//		try {
//			solrClient.commit();
//		} catch (SolrServerException | IOException e) {
//			e.printStackTrace();
//		}
//	}
	
}
