package org.jax.mgi.imsr.etl;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.GnuParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.jax.mgi.imsr.helpers.Constants;
import org.jax.mgi.imsr.helpers.MGDConnection;
import org.jax.mgi.imsr.helpers.SolrHelper;
import org.jax.mgi.imsr.helpers.Utilities;
import org.jax.mgi.imsr.model.MgdMaps;
import org.jax.mgi.imsr.model.MgiFeature;
import org.jax.mgi.imsr.model.Repositories;
import org.jax.mgi.imsr.model.Repository;

public class Etl {

	private static Repositories repos = new Repositories();
	private static MgdMaps alleleMgdMaps;
	private static HashMap<String, MgiFeature> alleleMap;
	private static List<String> recombinaseAlleleList;
	private static MgdMaps geneMgdMaps;
	private static HashMap<String, MgiFeature> geneMap;
	private static HashMap<String, List<String>> synonymsMap;
	private static HashMap<String, String> inverseSynonymsMap;
	private static HashMap<String, String> withdrawnMarkersMap;
	private static List<String> allNomenclaturesList;
	private static List<File> files = new ArrayList<File>();
	private static String[] cliFileNames = null;
	private static String cliSolrServerType = null;
	private static Boolean cliSkipUrlTest = false;

	public Etl() {
	}

	public static void main(String[] args) throws Exception {
		
		if (parseCommandLine(args)) {
			createFileList(cliFileNames);
			collectCommonData();
			markReposForUpdate();
			updateRepos(cliSolrServerType, cliSkipUrlTest);

			System.out.println("Completed indexing all repositories.");
		}
	}

	private static boolean parseCommandLine(String[] args) {
		Options options = new Options();
		options.addOption("s", true, "solr server to write to [dev,test,public]");
		options.addOption("noUrlTesting", false, "skip url validation testing");
		
		Option fileListOption = new Option("f", true, "list of files");
		fileListOption.setArgs(Option.UNLIMITED_VALUES);
		options.addOption(fileListOption);
		
		CommandLineParser parser = new GnuParser();
		CommandLine line = null;
				
		try {
			line = parser.parse(options, args);
		} catch (ParseException exp) {
			return commandLineErrorMessage(options, "Parsing failed.  Reason: " + exp.getMessage());
		}
				
		if (line.hasOption("s")) {
			cliSolrServerType = line.getOptionValue("s").toUpperCase();
			if (!Constants.SOLR_SERVERS.containsKey(cliSolrServerType)) {
				return commandLineErrorMessage(options, "Error: " + cliSolrServerType + " - is invalid server type.");
			}
		} else {
			return commandLineErrorMessage(options, "No solr server argument provided - no action taken.");
		}
		
		if (line.hasOption("f")) {
			cliFileNames = line.getOptionValues("f");
			if (cliFileNames.length == 0) {
				return commandLineErrorMessage(options, "No files provided - no action taken.");
			}
		} else {
			return commandLineErrorMessage(options, "No files provided - no action taken.");
		}
		
		cliSkipUrlTest = line.hasOption("noUrlTesting");

		return true;
	}

	private static boolean commandLineErrorMessage(Options options, String message) {
		HelpFormatter formatter = new HelpFormatter();
		
		System.out.println(message);
		formatter.printHelp( "imsretl", options );
		return false;
	}
	
	private static void createFileList(String[] fileNames) {
		for (String fileName : fileNames) {
			files.add(new File(fileName));
		}
	}

	private static void collectCommonData() {
		// import repository meta data
		repos.importRepositories(new File(Constants.REPOSITORY_META_DATA_FILENAME));

		// get allele detail list
		alleleMgdMaps = MGDConnection.getAlleleMaps();
		alleleMap = alleleMgdMaps.getFeatureMap();
		System.out.println("Collected alleles: " + alleleMap.size());

		recombinaseAlleleList = MGDConnection.getRecombinaseAlleles();
		System.out.println("Collected recombinase alleles: " + recombinaseAlleleList.size());

		// get gene detail list
		geneMgdMaps = MGDConnection.getGeneMaps();
		geneMap = geneMgdMaps.getFeatureMap();
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
	
	private static void updateRepos(String solrServerType, Boolean skipUrlTest) throws IOException {
		HashMap<String, String> repoNomenclatureMap;
		SolrHelper solrHelper = new SolrHelper(solrServerType);
		
		for (Repository repository : repos.getRepositories()) {
			if (repository.getNeedsUpdate()) {
				repository.setSolrHelper(solrHelper);
				repository.importRecords();
				repository.validateRecords(alleleMap, geneMap);
				repository.transformRecords(alleleMgdMaps, geneMgdMaps, recombinaseAlleleList, withdrawnMarkersMap);
				
				if (!skipUrlTest) {
					repository.testStrainUrls();
				}

				if (repository.isValidForSolr()) {
					repoNomenclatureMap = MGDConnection.getRepoNomenclature(repository.getLogicalDB());

					repository.aggregateStrains();
					if (repository.meetsStrainCountThreshold()) {
						repository.decorateStrains(synonymsMap, inverseSynonymsMap, allNomenclaturesList, repoNomenclatureMap);
						repository.loadStrainsIntoSolr();
					}
				}

				Boolean sendPublicEmail = cliSolrServerType.equals("PUBLIC");
				repository.emailUploadStatusReport(sendPublicEmail);
				repository.emailImsrCuratorReport(sendPublicEmail);
			}
		}
	}

}
