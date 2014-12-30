package org.jax.mgi.imsr.model;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name="contact")
public class Contact {
	
	private String contactName;
	private String contactEmail;
	private String contactPhone;
	private String contactFax;
	private String contactType;
	private Boolean isReportContact = false;
	
	public Contact() {
		// must have no-argument constructor
	}

	public Boolean isReportContact() {
		return isReportContact;
	}

	@XmlElement(name="contact-report")
	public void setIsReportcontact(Boolean isReportContact) {
		if (isReportContact != null) {
			this.isReportContact = isReportContact;
		}
	}

	public String getContactName() {
		return contactName;
	}

	@XmlElement(name="contact-name")
	public void setContactName(String name) {
		this.contactName = name;
	}

	public String getContactEmail() {
		return contactEmail;
	}

	@XmlElement(name="contact-email")
	public void setContactEmail(String contactEmail) {
		this.contactEmail = contactEmail;
	}

	public String getContactPhone() {
		return contactPhone;
	}

	@XmlElement(name="contact-phone")
	public void setContactPhone(String phone) {
		this.contactPhone = phone;
	}

	public String getContactFax() {
		return contactFax;
	}

	@XmlElement(name="contact-fax")
	public void setContactFax(String fax) {
		this.contactFax = fax;
	}
	
	public String getContactType() {
		return contactType;
	}

	@XmlElement(name="contact-type")
	public void setContactType(String type) {
		this.contactType = type;
	}
	
}
