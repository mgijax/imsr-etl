package org.jax.informatics.imsr.etl;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.GnuParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.apache.commons.io.FilenameUtils;

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
	private static List<File> files;
	private static String[] cliFileNames = null;
	private static String cliSolrServerType = null;
	private static Boolean cliSkipUrlTest = false;
	private static Boolean cliForce = false;

	public Etl() {
	}

	public static void main(String[] args) throws Exception {
		
		if (parseCommandLine(args)) {
			collectCommonData();
			markReposForUpdate();
			updateRepos(cliSolrServerType, cliSkipUrlTest, cliForce);

			System.out.println("Completed indexing all repositories.");
		}
	}

	private static boolean parseCommandLine(String[] args) {
		Options options = new Options();
		options.addOption("h", false, "print this message");
		options.addOption("s", true, "solr server to write to [dev,test,public]");
		options.addOption("noUrlTesting", false, "skip url validation testing");
		options.addOption("d", true, "directory of files, ");
		options.addOption("force", false, "always load file - regardless of errors");
		
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
				
		if (line.hasOption("h")) {
			String header = "";
			String footer = "\n Note: -d and -f can not be combined, must choose only one.";
			HelpFormatter helpFormatter = new HelpFormatter();
			helpFormatter.printHelp("imsr-etl", header, options, footer);

			return false;
		}
		
		if (line.hasOption("s")) {
			cliSolrServerType = line.getOptionValue("s").toUpperCase();
			if (!Constants.SOLR_SERVERS.containsKey(cliSolrServerType)) {
				return commandLineErrorMessage(options, "Error: " + cliSolrServerType + " - is invalid server type.");
			}
		} else {
			return commandLineErrorMessage(options, "No solr server argument provided - no action taken.");
		}
		
		if (line.hasOption("d")) {
			String cliDirectoryName = line.getOptionValue("d");
			File cliDirectory = new File(cliDirectoryName);
			
			if (cliDirectory.isDirectory()) {
				File[] filelist = cliDirectory.listFiles();
				files = new ArrayList<File>(Arrays.asList(filelist));
			}		
		} else if (line.hasOption("f")) {
			cliFileNames = line.getOptionValues("f");
			if (cliFileNames.length > 0) {
				files = createFileList(cliFileNames);
			} else {
				return commandLineErrorMessage(options, "No files provided - no action taken.");
			}
		} else {
			return commandLineErrorMessage(options, "No files provided - no action taken.");
		}
		
		
		cliSkipUrlTest = line.hasOption("noUrlTesting");
		cliForce = line.hasOption("force");
		if (cliForce) {
			cliSkipUrlTest = true;
		}

		return true;
	}

	private static boolean commandLineErrorMessage(Options options, String message) {
		HelpFormatter formatter = new HelpFormatter();
		
		System.out.println(message);
		formatter.printHelp( "imsretl", options );
		return false;
	}
	
	private static List<File> createFileList(String[] fileNames) {
		List<File> fileList = new ArrayList<File> ();
		
		for (String fileName : fileNames) {
			File file = new File(fileName);
			if (file.isFile()) {
				fileList.add(file);
			}
		}
		return fileList;
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
		List<String> validFileNameExtensions = Arrays.asList("DAT", "TXT");
		Repository repo = null;
		
		for (File file : files) {
			// expected filename convention: <repo>_<date>.dat
			String fileName = file.getName();
			String fileNamePrefix = fileName.toUpperCase().split("_")[0];
			
			String ext = FilenameUtils.getExtension(fileName).toUpperCase();

			if (validFileNameExtensions.contains(ext)) {
				repo = repos.findRepository(fileNamePrefix);		
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
	}
	
	private static void updateRepos(String solrServerType, Boolean skipUrlTest, Boolean cliForce) throws IOException {
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

				if (repository.isValidForSolr() || cliForce) {
					repoNomenclatureMap = MGDConnection.getRepoNomenclature(repository.getLogicalDB());

					repository.aggregateStrains();
					if (repository.meetsStrainCountThreshold() || cliForce) {
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
