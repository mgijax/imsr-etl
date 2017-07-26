package org.jax.informatics.imsr.model;

public class ImsrEmailContact {
	private String email;
	private String name;
	
	public ImsrEmailContact(String email, String name) {
		this.email = email;
		this.name = name;
	}
	
	public String getEmail() {
		return email;
	}
	
	public String getName() {
		return name;
	}
}
