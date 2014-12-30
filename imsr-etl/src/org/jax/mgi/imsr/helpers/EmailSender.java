package org.jax.mgi.imsr.helpers;

import org.apache.commons.mail.EmailAttachment;
import org.apache.commons.mail.EmailException;
import org.apache.commons.mail.MultiPartEmail;
import org.jax.mgi.imsr.model.ImsrEmailAttachment;
import org.jax.mgi.imsr.model.ImsrEmailContact;
import org.jax.mgi.imsr.model.ImsrMailer;

public class EmailSender {
	
	public static String hostName = "smtp.jax.org";
	public static Integer smtpPort = 25;
	public static Boolean SSLOnConnect = false;
	
	
	public static void send(ImsrMailer imsrMail) {
		MultiPartEmail email = new MultiPartEmail();

		email.setHostName(hostName);
		email.setSmtpPort(smtpPort);
		email.setSSLOnConnect(SSLOnConnect);

		try {
			for (ImsrEmailAttachment a : imsrMail.getAttachments()) {
				EmailAttachment attachment = new EmailAttachment();

				attachment.setURL(a.getUrl());
				attachment.setDisposition(EmailAttachment.ATTACHMENT);
				attachment.setDescription(a.getDescription());
				attachment.setName(a.getName());

				email.attach(attachment);
			}

			for (ImsrEmailContact to : imsrMail.getTo()) {
				email.addTo(to.getEmail(), to.getName());
			}
			
			for (ImsrEmailContact cc : imsrMail.getCc()) {
				email.addCc(cc.getEmail(), cc.getName());
			}
			
			email.setFrom(imsrMail.getFrom());
			email.setSubject(imsrMail.getSubjet());
			email.setMsg(imsrMail.getMessage());

			email.send();
		} catch (EmailException e) {
			e.printStackTrace();
		}
	}
	
}
