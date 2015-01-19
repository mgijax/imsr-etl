package org.jax.mgi.imsr.helpers;

import java.io.IOException;
import java.util.List;

import org.apache.solr.client.solrj.SolrQuery;
import org.apache.solr.client.solrj.SolrServer;
import org.apache.solr.client.solrj.SolrServerException;
import org.apache.solr.client.solrj.impl.HttpSolrServer;
import org.jax.mgi.imsr.model.Solr3Strain;

public class SolrHelper {
	
	private SolrServer solrServer = null;
	
	public SolrHelper(String solrServerType) {
		if (Constants.SOLR_SERVERS.containsKey(solrServerType)) {
			String url = Constants.SOLR_SERVERS.get(solrServerType);
			this.solrServer = new HttpSolrServer(url);
		}
	}
	
	
	/**
	 * Deletes all solr documents associated with the repositoryId.
	 * 
	 * @param repositoryId	- specifies repository to delete from solr index
	 * @see SolrServer
	 */
	public void dropRepositoryFromSolr(String repositoryId) {
		System.out.println(repositoryId + ": Deleting old records.");
		
		try {			
			solrServer.deleteByQuery(String.format("provider:%s", repositoryId));
			solrServer.commit();
		} catch (SolrServerException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	
	/**
	 * Returns a count of solr documents for a given repository.
	 * 
	 * @param repositoryId	- repository to count strains
	 * @return 				a count of strains from a specified repository
	 * @see SolrServer
	 * @see SolrQuery
	 */
	public long getRepositoryStrainCountFromSolr(String repositoryId) {		
		// http://stackoverflow.com/questions/5050746/solr-solrj-how-can-i-determine-the-total-number-of-documents-in-an-index
		long strainCount = 0;
		try {			
			SolrQuery q = new SolrQuery("provider:" + repositoryId);
			q.setRows(0);
			strainCount = solrServer.query(q).getResults().getNumFound();
		} catch (SolrServerException e) {
			e.printStackTrace();
		}
		return strainCount;
	}


	/**
	 * Adds a list of strains to the solr index, as documents.
	 * 
	 * @param solrStrains 	- a list of strains (in JSON format) to be added to the index
	 * @see SolrServer
	 */
	public void writeStrainsToSolr(List<Solr3Strain> solrStrains) {
		System.out.println("Writing " + solrStrains.size() + " strains.");
				
		try {			
			solrServer.addBeans(solrStrains);
			solrServer.optimize();
			solrServer.commit();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (SolrServerException e) {
			e.printStackTrace();
		}
	}
	
//	/**
//	 * Commits any solr changes
//	 */
//	public void solrCommit() {
//		try {
//			solrServer.commit();
//		} catch (SolrServerException e) {
//			e.printStackTrace();
//		} catch (IOException e) {
//			e.printStackTrace();
//		}
//	}
	
}
