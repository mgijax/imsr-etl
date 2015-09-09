package org.jax.mgi.imsr.model;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import org.jax.mgi.imsr.helpers.Constants;
import org.jax.mgi.imsr.helpers.SolrHelper;
import org.jax.mgi.imsr.helpers.Utilities;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name = "Repository")
public class Repository {
	
	@XmlAttribute
	private String id;
	private String idalias;	
	private Integer logicaldb;
	private String strainurl;
	private String orderurl;
	private String homepage;
	private String name;
	private String address1;
	private String address2;
	private String address3;
	private String city;
	private String state;
	private String postalcode;
	private String country;
	private String email;
	private String phone;
	private String fax;
	private Repository.Contacts contacts;
	private Map<String, Strain> strains = new HashMap<String, Strain>();
	private List<Record> records = new ArrayList<Record>();
	private List<Record> invalidRecords = new ArrayList<Record>();
	private Boolean needsUpdate = false;
	private File file;
	private String strainidprefix;
	private SolrHelper solrHelper;

	public Repository() {
		// must have no-argument constructor
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getIdAlias() {
		return idalias;
	}

	public void setIdAlias(String idalias) {
		this.idalias = idalias;
	}

	public Integer getLogicalDB() {
		return logicaldb;
	}

	public void setId(Integer logicaldb) {
		this.logicaldb = logicaldb;
	}

	public String getStrainUrl() {
		return strainurl;
	}

	public void setStrainurl(String strainurl) {
		this.strainurl = strainurl;
	}

	public String getOrderurl() {
		return orderurl;
	}

	public void setOrderurl(String orderurl) {
		this.orderurl = orderurl;
	}

	public String getHomepage() {
		return homepage;
	}

	public void setHomepage(String homepage) {
		this.homepage = homepage;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getAddress1() {
		return address1;
	}

	public void setAddress1(String address1) {
		this.address1 = address1;
	}

	public String getAddress2() {
		return address2;
	}

	public void setAddress2(String address2) {
		this.address2 = address2;
	}

	public String getAddress3() {
		return address3;
	}

	public void setAddress3(String address3) {
		this.address3 = address3;
	}

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public String getState() {
		return state;
	}

	public void setState(String state) {
		this.state = state;
	}

	public String getPostalcode() {
		return postalcode;
	}

	public void setPostalcode(String postalcode) {
		this.postalcode = postalcode;
	}

	public String getCountry() {
		return country;
	}

	public void setCountry(String country) {
		this.country = country;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public String getFax() {
		return fax;
	}

	public void setFax(String fax) {
		this.fax = fax;
	}

	public Boolean getNeedsUpdate() {
		return needsUpdate;
	}

	public void setNeedsUpdate(Boolean needsUpdate) {
		this.needsUpdate = needsUpdate;
	}

	public File getFile() {
		return file;
	}

	public void setFile(File file) {
		this.file = file;
	}

	public Date getFileLastModified() {
		return  new Date(this.file.lastModified());
	}

	public String getStrainidprefix() {
		return strainidprefix;
	}

	public void setStrainidprefix(String strainidprefix) {
		this.strainidprefix = strainidprefix;
	}

	public Repository.Contacts getContacts() {
		return contacts;
	}
	
	public void setContacts(Repository.Contacts contacts) {
		this.contacts = contacts;
	}
	
	public void setSolrHelper(SolrHelper solrHelper) {
		this.solrHelper = solrHelper;
	}

	public static class Contacts {
		// nested child elements
		// link: http://www.coderanch.com/t/595215/XML/Unmarshalling-nested-child-elements-jaxb
		
		@XmlElement (name="contact")
		private List<Contact> contact;
		
		public List<Contact> getContact() {
			if (contact == null) {
				contact = new ArrayList<Contact> ();
			}
			return this.contact;
		}
	}
	
	public List<Record> getStrains() {
		return records;
	}

	public void addRecord(Record record) {
		records.add(record);
	}

	public void validateRecords(HashMap<String, MgiFeature> alleleMap, HashMap<String, MgiFeature> geneMap) {
		for (Record r : records) {
			r.validateId();
			r.validateName();
			r.validateTypes();
			r.validateStates();
			r.validateMgiAlleleAccId(alleleMap);
			r.validateMutationType();
			r.validateMgiGeneAccId(geneMap);
			
			if (strainurl == null) {
				r.validateUrl();
			}
			
			if (! r.isValid()) {
				invalidRecords.add(r);
			}
		}
		records.removeAll(invalidRecords);
	}
	
	public boolean areRecordsValid() {
		return invalidRecords.isEmpty();
	}
	
	public List<Record> getInvalidRecords() {
		return invalidRecords;
	}

	private List<Record> getRecordsWithWarnings() {
		List<Record> recordsWithWarnings = new ArrayList<Record>();
		for (Record r : records) {
			if (r.hasWarning()) {
				recordsWithWarnings.add(r);
			}
		}
		
		return recordsWithWarnings;
	}

	public void importRecords() throws IOException {
		// open file 
		BufferedReader bReader = new BufferedReader(new FileReader(file));
		
		// populate strain object and add to repository
		String line;
		while ((line = bReader.readLine()) != null) {
			if (!line.trim().isEmpty()) {
				this.addRecord(createRecord(line));
			}
		}
		
		bReader.close();
	}

	private Record createRecord(String line) {
		Record record = new Record();
		String values[] = line.replaceAll("\"", "").split("\t", -1);
		
		for (int i = 0; i < values.length; i++) {
			values[i] = values[i].trim();
		}
		
		Integer urlOffset = ((this.strainurl == null) || this.strainurl.isEmpty()) ? 1 : 0;
		
		if (values.length > 0) {
			record.setId(values[0]);
		}
		
		if (values.length > 1) {
			record.setName(values[1]);
		}
		
		if (values.length > 2) {
			record.setTypes(valuesIntoSet(values[2].toUpperCase()));
		}
		
		if (values.length > 3) {
			record.setStates(valuesIntoSet(values[3].toUpperCase()));
		}
		
		if (values.length > 4 && urlOffset == 1) {
			record.setUrl(values[4]);
		}
		
		if (values.length > 4 + urlOffset) {
			record.setMgiAlleleAccId(values[4 + urlOffset]);
		}
		
		if (values.length > 5 + urlOffset) {
			record.setAlleleSymbol(values[5 + urlOffset]);
		}
		
		if (values.length > 6 + urlOffset) {
			record.setAlleleName(values[6 + urlOffset]);
		}
		
		if (values.length > 7 + urlOffset) {
			record.setMutationTypes(valuesIntoSet(values[7 +urlOffset].toUpperCase()));
		}
		
		if (values.length > 8 + urlOffset) {
			record.setChromosome(values[8 + urlOffset]);
		}
		
		if (values.length > 9 + urlOffset) {
			record.setMgiGeneAccId(values[9 + urlOffset]);
		}
		
		if (values.length > 10 + urlOffset) {
			record.setGeneSymbol(values[10 + urlOffset]);
		}
		
		if (values.length > 11 + urlOffset) {
			record.setGeneName(values[11 + urlOffset]);
		}
		
		return record;
	}
	
	private Set<String> valuesIntoSet(String values) {
		Set<String> valueSet = new HashSet<String>();

		for (String t : values.split(",|\\s")) {
			if (!t.isEmpty()) {
				valueSet.add(t);
			}
		}
 		 
		return valueSet;
	}

	
	public void transformRecords(MgdAlleleMaps alleleFeaturesMap, HashMap<String, MgiFeature> geneMap, List<String> recombinaseAlleleList, HashMap<String, String> withdrawnMarkersMap) {		
		for (Record r : records) {
			r.transformUrl(this.strainurl);
			r.transformAllele(alleleFeaturesMap, recombinaseAlleleList);
			r.transformGene(geneMap, withdrawnMarkersMap);
			
			r.setMutationTypes(expandAbbreviations(Constants.MUTATION_TYPES, r.getMutationTypes()));
			r.setStates(expandAbbreviations(Constants.STRAIN_STATES, r.getStates()));
			r.setTypes(expandAbbreviations(Constants.STRAIN_TYPES, r.getTypes()));
		}		
	}
	
	
	private Set<String> expandAbbreviations(Map<String, String> abbreviationMap, Set<String> abbreviations) {
		Set<String> result = new HashSet<String>();

		for (String a : abbreviations) {
			result.add(abbreviationMap.get(a));
		}
		
		return result;
	}

	
	public void aggregateStrains() {
		for (Record r : records) {
			String recordId = r.getId();
			
			if (!strains.containsKey(recordId)) {
				// create new strain
				Strain s = new Strain(r);
				strains.put(s.getId(), s);
			} else {
				Strain existingStrain = strains.get(recordId);
				if (r.getName().equals(existingStrain.getName())) {
					// add record to existing strain
					existingStrain.addRecord(r);
				} else {
					r.addError("has differing strain names: " + r.getName() + " and " + existingStrain.getName());
					invalidRecords.add(r);
				}
			}			
		}
		records.removeAll(invalidRecords);
	}

	
	public void decorateStrains(HashMap<String, List<String>> synonymsMap, HashMap<String, String> inverseSynonymsMap, List<String> allNomenclaturesList, HashMap<String, String> repoNomenclatureMap) {		
		for (Entry<String, Strain> entry : strains.entrySet()) {
			Strain s = entry.getValue();
			
			s.addSynonyms(synonymsMap, inverseSynonymsMap, repoNomenclatureMap);
			s.addNomenclatureFlag(allNomenclaturesList, repoNomenclatureMap, this.id);
			s.addPrefixId(this.strainidprefix);
		}
	}
	

	private List<Strain> getStrainsWithImsrMessages() {
		List<Strain> strainsWithImsrMessages = new ArrayList<Strain>();
		for (Entry<String, Strain> entry : strains.entrySet()) {
			Strain s = entry.getValue();
			
			if (s.hasImsrMessage()) {
				strainsWithImsrMessages.add(s);
			}
		}
		return strainsWithImsrMessages;
	}

	
	@Override
	public String toString() {
		return "Repository [id=" + id + ", logicaldb=" + logicaldb
				+ ", strainurl=" + strainurl + ", orderurl=" + orderurl
				+ ", homepage=" + homepage + ", name=" + name + ", address1="
				+ address1 + ", address2=" + address2 + ", address3="
				+ address3 + ", city=" + city + ", state=" + state
				+ ", postalcode=" + postalcode + ", country=" + country
				+ ", email=" + email + ", phone=" + phone + ", fax=" + fax
				+ "]";
	}

	public void testStrainUrls() {
		Collections.shuffle(records);
		Integer limit = Math.min(Constants.MAX_RECORDS_TO_TEST, Math.max(records.size(), records.size()/10));
		
		int count = 0;
		System.out.print("testing strains: ");
		
		for (Record r : records.subList(0, limit)) {
			r.testUrlConnection();
	
			if (! r.isValid()) {
				invalidRecords.add(r);
			}
			
			count++;
			if (count%10 == 0) {
				System.out.print("*");
			}
		}
		
		records.removeAll(invalidRecords);
		System.out.println(".");
	}

	public boolean isValidForSolr() {
		System.out.println(id + ": " + invalidRecords.size() + " invalid entries" + ".");
		
		Integer limit = Math.min(Constants.MAX_RECORDS_TO_TEST, Math.max(records.size(), records.size()/10));
		return invalidRecords.size() < (limit * Constants.VALID_SUBMISSION_THRESHOLD);
	}
	
	
	private void removeOldStrainsFromSolr() {
		solrHelper.dropRepositoryFromSolr(this.id);
	}

	
	private void addStrainsToSolr() {
		List<Solr3Strain> solrStrains = new ArrayList<Solr3Strain>();
		
		for (Entry<String, Strain> entry : strains.entrySet()) {
			Strain s = entry.getValue();
			
			solrStrains.add(new Solr3Strain(s, this));
			
			if (solrStrains.size() > Constants.MAX_STRAINS_TO_WRITE_TO_SOLR_AT_ONCE) {
				solrHelper.writeStrainsToSolr(solrStrains);
				solrStrains.clear();
			}
		}
		
		solrHelper.writeStrainsToSolr(solrStrains);
	}

	
	public void loadStrainsIntoSolr() {		
		removeOldStrainsFromSolr();
		addStrainsToSolr();
	}

	
	public void emailUploadStatusReport(Boolean sendPublicEmail) {
		ImsrEmailContact imsrEmailAddress = new ImsrEmailContact(Constants.IMSR_EMAIL_CONTACT, Constants.IMSR_EMAIL_NAME);
		
		ImsrMailer email = new ImsrMailer();
		email.setFrom(imsrEmailAddress.getEmail());
		email.addCc(imsrEmailAddress);
		
		if (sendPublicEmail) {
			Repository.Contacts contacts = this.getContacts();
			for (Contact c : contacts.getContact()) {
				if (c.isReportContact()) {
					email.addTo(new ImsrEmailContact(c.getContactEmail(), c.getContactName()));
				}
			}
		} 
		
		Boolean meetsThreshold = meetsStrainCountThreshold();
		Boolean validDataFile = isValidForSolr() && meetsThreshold;
		String subjectStatus = validDataFile ? "Succeeded" : "FAILED";
		email.setSubjet(subjectStatus + " - IMSR file submission: " + this.file.getName());

		List<Record> warningRecords = this.getRecordsWithWarnings();
		
		String message = "This is an auto-generated message.\n\n";
		
		message += "Your submission file: " + this.file.getName() + " has " + subjectStatus.toLowerCase() + " uploading to the IMSR website.\n";

		if (!meetsThreshold) {
			message += "Your submission file resulted in less strains than you currently have listed with IMSR, which may indicate an incomplete submission.\n";
			message += "If you believe your submission is correct, please contact the IMSR staff to manaully override this warning.\n";
			message += "Additional errors and warnings are listed below.\n\n";
		} else {	
			message += strains.size() + Utilities.pluralSuffix(strains.size(), " strain has", " strains have") + " been uploaded.\n\n";
		}
		
		if (warningRecords.size() > 0 || invalidRecords.size() > 0) {
			message += "This submission contained " + warningRecords.size() + Utilities.pluralSuffix(warningRecords.size(), " warning", " warnings");
			message += " and " + invalidRecords.size() + Utilities.pluralSuffix(invalidRecords.size(), " entry has", " entries have") + " been rejected due to errors.\n";
		}
		
		if (invalidRecords.size() > 0) {
			message += "\n" + "Attached is the IMSR file format guide for your reference.\n\nPlease resolve the " + Utilities.pluralSuffix(invalidRecords.size(), "error", "errors") + " listed below and resubmit your submission file:\n";
			
			try {
				ImsrEmailAttachment attachment = new ImsrEmailAttachment();
				attachment.setUrl(new URL("http://www.findmice.org/assets/etc/IMSRFileFormat.pdf"));
				attachment.setDescription("IMSR_File_Format_Guide.pdf");
				attachment.setName("IMSR_File_Format_Guide.pdf");
				
				email.addAttachment(attachment);
			} catch (MalformedURLException e) {
				e.printStackTrace();
			}
		}
		
		for (Record r : this.getInvalidRecords()) {
			for (String e : r.getErrors()) {
				message += "ERROR - Strain Id: " + r.getId() + " - " + e + "\n";
			}
		}
				
		if (warningRecords.size() > 0) {
			message += "\n" + "Please resolve the " + Utilities.pluralSuffix(warningRecords.size(), "warning", "warnings") + " listed below and resubmit your submission file:\n";
		}
		
		for (Record r : warningRecords) {
			for (String w : r.getWarnings()) {
				message += "Warning - Strain Id: " + r.getId() + " - " + w + "\n";
			}
		}
		
		if (validDataFile) {
			message += "\n" + "The latest IMSR data for the " + this.name + " repository is available at:\n";
			message += "http://www.findmice.org/summary?query=&states=Any&_states=1&types=Any&_types=1&repositories=" + this.id + "&_repositories=1&_mutations=on \n"; 
			message += "\n" + "Thank you for updating IMSR and helping ensure IMSR is accurate and relevant.\n";
		} else {
			message += "\n" + "There were too many errors in your submission file. No new data has been uploaded to IMSR. \n";	
		}
		
		message += "\n" + "Please email with any questions or concerns. Our goal is to help you submit the most current and accurate data to IMSR.\n";
		message += "\n\n" + "Gratefully,\n";
		message += "Howie Motenko\n";
						
		if (sendPublicEmail) {
			email.setMessage(message);
			email.send();
		} else {
			System.out.println(message);
		}
	}	
		
	public void emailImsrCuratorReport(Boolean sendPublicEmail) {
		if (sendPublicEmail) {		
			ImsrEmailContact imsrEmailAddress = new ImsrEmailContact(Constants.IMSR_EMAIL_CONTACT, Constants.IMSR_EMAIL_NAME);
			ImsrEmailContact[] imsrCurators = Constants.IMSR_CURATORS_LIST;
			List<Strain> strainsWithImsrMessages = this.getStrainsWithImsrMessages();
			
			if (strainsWithImsrMessages.size() > 0 && isValidForSolr()) {
				ImsrMailer email = new ImsrMailer();
				email.setFrom(imsrEmailAddress.getEmail());
				email.addCc(imsrEmailAddress);
				
				email.setSubjet("IMSR file submission: " + this.file.getName());
	
				for (ImsrEmailContact c : imsrCurators) {
					email.addTo(c);
				}
				
				String message = "";
				message += "When uploading the submission file: " + this.file.getName() + " there " + Utilities.pluralSuffix(strainsWithImsrMessages.size(), "is ", "are ");
				message += strainsWithImsrMessages.size() + Utilities.pluralSuffix(strainsWithImsrMessages.size(), " strain", " strains") + " listed below for your review.\n\n";
		
				for (Strain s : strainsWithImsrMessages) {
					message += "Strain ID: " + s.getId() + " " + s.getImsrMessage() + "\n";
				}
				
				email.setMessage(message);
				email.send();
			}
		}
	}

	public boolean meetsStrainCountThreshold() {
		Double threshold = Constants.STRAIN_PERCENT_THRESHOLD;
		Boolean meetsThreshold;
		Integer newStrainCount = this.strains.size();
		Long currentStrainCount = solrHelper.getRepositoryStrainCountFromSolr(this.id);
		
		if (currentStrainCount == 0) {
			// new repository - no strains in solr
			meetsThreshold = true;
		} else {
			meetsThreshold = ((double)newStrainCount / (double)currentStrainCount) > threshold;		
		}
		
		return meetsThreshold;
	}


}
