package org.jax.mgi.imsr.helpers;

import java.io.File;
import java.util.List;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;

import org.jax.mgi.imsr.model.Repositories;
import org.jax.mgi.imsr.model.Repository;;

public class JAXBXMLHandler {
	// Importer
	public static List<Repository> unmarshal(File importFile) throws JAXBException {
		Repositories repositories = new Repositories();
		
		JAXBContext context = JAXBContext.newInstance(Repositories.class);
		Unmarshaller um = context.createUnmarshaller();
		repositories = (Repositories) um.unmarshal(importFile);
		
		return repositories.getRepositories();
	}
}


