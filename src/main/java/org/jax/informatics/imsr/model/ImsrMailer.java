package org.jax.informatics.imsr.model;

import java.util.ArrayList;
import java.util.List;

import org.jax.informatics.imsr.helpers.EmailSender;

public class ImsrMailer {
	private String subjet = "";
	private String message = "";
	private List<ImsrEmailContact> toContacts = new ArrayList<ImsrEmailContact>();
	private String from = "";
	private List <ImsrEmailContact> ccContacts = new ArrayList<ImsrEmailContact>();	
	private List<ImsrEmailAttachment> attachments = new ArrayList<ImsrEmailAttachment>();
	
	
	public String getSubjet() {
		return subjet;
	}

	public void setSubjet(String subjet) {
		this.subjet = subjet;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public List<ImsrEmailContact> getTo() {
		return toContacts;
	}

	public void addTo(ImsrEmailContact toContact) {
		this.toContacts.add(toContact);
	}

	public List<ImsrEmailContact> getCc() {
		return ccContacts;
	}

	public void addCc(ImsrEmailContact ccContact) {
		this.ccContacts.add(ccContact);
	}

	public List<ImsrEmailAttachment> getAttachments() {
		return attachments;
	}

	public void addAttachment(ImsrEmailAttachment attachment) {
		this.attachments.add(attachment);
	}
	
	public void send() {
		if (toContacts.isEmpty() || from.isEmpty()) {
			System.out.println("Error sending email: To or From email address is empty.");
		} else {
			EmailSender.send(this);
		}
	}

	public String getFrom() {
		return this.from;
	}	
	
	public void setFrom(String from) {
		this.from = from;
	}	
}



