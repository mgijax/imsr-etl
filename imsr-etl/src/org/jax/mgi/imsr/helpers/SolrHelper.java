package org.jax.mgi.imsr.helpers;

import java.io.IOException;
import java.util.List;

import org.apache.solr.client.solrj.SolrQuery;
import org.apache.solr.client.solrj.SolrServer;
import org.apache.solr.client.solrj.SolrServerException;
import org.apache.solr.client.solrj.impl.HttpSolrServer;
import org.jax.mgi.imsr.model.Solr3Strain;

public class SolrHelper {
	
	//	 solr_servers = {LOCAL, TEST, PUBLIC}
	private static SolrServer solrServer = new HttpSolrServer(Constants.SOLR_SERVERS.get("TEST"));
	
	
	public static void dropRepositoryFromSolr(String repositoryId) {
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


	public static long getRepositoryStrainCountFromSolr(String repositoryId) {		
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


	public static void writeStrainsToSolr(List<Solr3Strain> solrStrains) {
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
	
	
}
