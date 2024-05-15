package org.jax.informatics.imsr.model;


import java.io.File;
import java.util.ArrayList;
import java.util.List;

import jakarta.xml.bind.JAXBException;
import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlRootElement;

import org.jax.informatics.imsr.helpers.JAXBXMLHandler;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name = "Repositories")
public class Repositories {
	
	
	@XmlElement(name = "Repository", type = Repository.class)
	private List<Repository> repositories = new ArrayList<Repository>();

	public Repositories() {
	}
	
	public Repositories(List<Repository> repositories) {
		this.repositories = repositories;
	}
	
	public List<Repository> getRepositories() {
		return this.repositories;
	}
	
	public void setRepositories(List<Repository> repositories) {
		this.repositories = repositories;
	}
	
	public void add(Repository repository) {
		this.repositories.add(repository);
	}
	
	public void importRepositories(File file) {
		try {
			this.repositories = JAXBXMLHandler.unmarshal(file);
		} catch (JAXBException e) {
			e.printStackTrace();
		}
	}
	
	public Repository findRepository(String id) {
		for (Repository r : this.repositories) {
            if (r.getId().equals(id)) {
            	return r;
            } else if (r.getIdAlias() != null && r.getIdAlias().equals(id)) {
            	return r;
            }
        }		
		return null;
	}

	public List<Integer> getAllLogicalDBs() {
		List<Integer> resulstList = new ArrayList<Integer>();
		
		for (Repository r : this.repositories) {
			resulstList.add(r.getLogicalDB());
        }		
		
		return resulstList;
	}
	
}
