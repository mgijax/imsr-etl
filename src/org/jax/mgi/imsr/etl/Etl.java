package org.jax.mgi.imsr.etl;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.jax.mgi.imsr.helpers.Constants;
import org.jax.mgi.imsr.helpers.MGDConnection;
import org.jax.mgi.imsr.helpers.Utilities;
import org.jax.mgi.imsr.model.MgdAlleleMaps;
import org.jax.mgi.imsr.model.MgiFeature;
import org.jax.mgi.imsr.model.Repositories;
import org.jax.mgi.imsr.model.Repository;

public class Etl {

	private static Repositories repos = new Repositories();
	private static MgdAlleleMaps alleleFeaturesMap;
	private static HashMap<String, MgiFeature> alleleMap;
	private static List<String> recombinaseAlleleList;
	private static HashMap<String, MgiFeature> geneMap;
	private static HashMap<String, List<String>> synonymsMap;
	private static HashMap<String, String> inverseSynonymsMap;
	private static HashMap<String, String> withdrawnMarkersMap;
	private static List<String> allNomenclaturesList;
	private static List<File> files = new ArrayList<File>();

	public Etl() {
	}

	public static void main(String[] args) throws Exception {
		if (args.length == 0) {
			System.out.println("No action taken - no files specified in args.");
		} else {
			
			createFileList(args);
			collectCommonData();
			markReposForUpdate();
			updateRepos();

			System.out.println("Completed indexing all repositories.");
		}
	}

	
	private static void createFileList(String[] args) {
		for (String fileName : args) {
			files.add(new File(fileName));
		}
	}

	private static void collectCommonData() {
		// import repository meta data
		repos.importRepositories(new File(Constants.REPOSITORY_META_DATA_FILENAME));

		// get allele detail list
		alleleFeaturesMap = MGDConnection.getAlleleMaps();
		alleleMap = alleleFeaturesMap.getFeatureMap();
		System.out.println("Collected alleles: " + alleleMap.size());

		recombinaseAlleleList = MGDConnection.getRecombinaseAlleles();
		System.out.println("Collected recombinase alleles: " + recombinaseAlleleList.size());

		// get gene detail list
		geneMap = MGDConnection.getGenes();
		System.out.println("Collected genes: " + geneMap.size());

		synonymsMap = MGDConnection.getSynonyms();
		System.out.println("Collected synonyms: " + synonymsMap.size());

		inverseSynonymsMap = MGDConnection.getInverseSynonyms();
		System.out.println("Collected inverse synonyms: " + inverseSynonymsMap.size());

		withdrawnMarkersMap = MGDConnection.getWithdrawnMarkers();
		System.out.println("Collected withdrawn markers: " + withdrawnMarkersMap.size());

		// get all nomenclatures list
		List<Integer> allLogicalDBs = repos.getAllLogicalDBs();
		allNomenclaturesList = MGDConnection.getAllNomenclatures(allLogicalDBs);
		System.out.println("Collected strain nomenclatures: " + allNomenclaturesList.size());
	}

	private static void markReposForUpdate() {		
		for (File file : files) {
			// expected filename convention: <repo>_<date>.dat
			String fileNamePrefix = file.getName().toUpperCase().split("_")[0];
			Repository repo = repos.findRepository(fileNamePrefix);
			
			if (repo != null) {
				// update using the latest repo file
				file = Utilities.newerFile(file, repo.getFile());
				repo.setFile(file);
				repo.setNeedsUpdate(true);
			} else {
				throw new IllegalArgumentException("No repository with id: " + fileNamePrefix);
			}
		}
	}
	
	private static void updateRepos() throws IOException {
		HashMap<String, String> repoNomenclatureMap;
		
		for (Repository repository : repos.getRepositories()) {
			if (repository.getNeedsUpdate()) {
				repository.importRecords();
				repository.validateRecords(alleleMap, geneMap);
				repository.transformRecords(alleleFeaturesMap, geneMap, recombinaseAlleleList, withdrawnMarkersMap);
				repository.testStrainUrls();

				if (repository.isValidForSolr()) {
					repoNomenclatureMap = MGDConnection.getRepoNomenclature(repository.getLogicalDB());

					repository.aggregateStrains();
					if (repository.meetsStrainCountThreshold()) {
						repository.decorateStrains(synonymsMap, inverseSynonymsMap, allNomenclaturesList, repoNomenclatureMap);
						repository.loadStrainsIntoSolr();
					}
				}

				repository.emailUploadStatusReport();
				repository.emailImsrCuratorReport();
			}
		}
	}

}
