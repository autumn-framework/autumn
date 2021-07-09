package com.dt.autumn.reporting.emailReporting;

import com.dt.autumn.reporting.ServerReporterPath;

import javax.mail.*;
import javax.mail.internet.*;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;

public class EmailSend {
	
	public static EmailSend emailSend = new EmailSend();
	public static String subject ="";
	private static String smtpHost;
	private static String smtpPort;
	private static String senderEmailId;
	private static String senderEmailPassword;
	private static String emailSendToGroup;

	public void setSenderEmailId(String senderEmailId) {
		EmailSend.senderEmailId = senderEmailId;
	}

	public void setSenderEmailPassword(String senderEmailPassword) {
		EmailSend.senderEmailPassword = senderEmailPassword;
	}

	public void setEmailSendToGroup(String emailSendToGroup) {
		EmailSend.emailSendToGroup = emailSendToGroup;
	}

	public void setSmtpHost(String smtpHost) {
		EmailSend.smtpHost = smtpHost;
	}

	public void setSmtpPort(String smtpPort) {
		EmailSend.smtpPort = smtpPort;
	}

	public static EmailSend getInstance() {
		return emailSend;
	}

	public void sendEmailAfterTest(String project, String environment){
		// FromEmailId

        final String username = senderEmailId;
        // FromPassword
        final String password = senderEmailPassword;
        String sendMailTo = emailSendToGroup;
        // Recipient List
        String[] sendMailToList = sendMailTo.split(",",-1);

		// Subject
		String subject = setSubject(project, environment);
		String host = smtpHost;
		String port = smtpPort;
		Properties properties = System.getProperties();
		System.setProperty("jdk.tls.client.protocols","TLSv1.2");
		properties.put("mail.smtp.auth", "true");
		properties.put("mail.smtp.starttls.enable", "true");
		properties.setProperty("mail.debug.auth", "true");
		properties.put("mail.transport.protocol", "smtp");
		properties.put("mail.smtp.host", host);
		properties.put("mail.smtp.port", port);
		Session session = Session.getInstance(properties,
				new Authenticator() {
					protected PasswordAuthentication getPasswordAuthentication() {
						return new PasswordAuthentication(username,password);
					}
				});

		MimeMessage message = new MimeMessage(session);
		try {
			message.setFrom(new InternetAddress(username));
			InternetAddress[] toAddress = new InternetAddress[sendMailToList.length];

			// To get the array of addresses
			for (int i = 0; i < sendMailToList.length; i++) {
				toAddress[i] = new InternetAddress(sendMailToList[i]);
			}

			for (int i = 0; i < toAddress.length; i++) {
				message.addRecipient(Message.RecipientType.TO, toAddress[i]);
			}

			message.setSubject(subject);
			// set the content
			BodyPart messageBodyPart1 = new MimeBodyPart();
			String content = "";
			try {
				BufferedReader in = new BufferedReader(new FileReader(EmailSummary.emailReportPath));
				String str;
				while ((str = in.readLine()) != null) {
					content += str;
				}
				in.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
			Transport transport = session.getTransport("smtp");
			messageBodyPart1.setContent(content, "text/html");
			Multipart multipart = new MimeMultipart();
			multipart.addBodyPart(messageBodyPart1);
			message.setContent(multipart);


			transport.connect(host, username, password);
			transport.sendMessage(message, message.getAllRecipients());
			transport.close();

//			File destFolderPath = new File(System.getProperty("user.dir") + "/EmailData");
	//		FileUtils.forceDelete(destFolderPath);

		} catch (AddressException ae) {
			ae.printStackTrace();
		} catch (MessagingException me) {
			me.printStackTrace();
		}
	}

	public String setSubject(String projectName, String environment) {
		subject = projectName + " | Automation Test Results | " + environment +" Environment | " + ServerReporterPath.getCurrentDateTime("MMM dd yyyy HH-mm-ss").replace(" ","_");
		return subject;
	}

}
