package org.jax.informatics.imsr.helpers;

import java.io.File;
import java.util.List;

import jakarta.xml.bind.JAXBContext;
import jakarta.xml.bind.JAXBException;
import jakarta.xml.bind.Unmarshaller;

import org.jax.informatics.imsr.model.Repositories;
import org.jax.informatics.imsr.model.Repository;;

public class JAXBXMLHandler {
	/**
	 * Imports a list of repositories by unmarshalling repositories from an XML file based on the repository object. 
	 * 
	 * @param importFile	- an XML document containing repository information
	 * @return				a list of repositories from the importFile
	 * @throws JAXBException
	 */
	public static List<Repository> unmarshal(File importFile) throws JAXBException {
		Repositories repositories = new Repositories();
		
		JAXBContext context = JAXBContext.newInstance(Repositories.class);
		Unmarshaller um = context.createUnmarshaller();
		repositories = (Repositories) um.unmarshal(importFile);
		
		return repositories.getRepositories();
	}
}


