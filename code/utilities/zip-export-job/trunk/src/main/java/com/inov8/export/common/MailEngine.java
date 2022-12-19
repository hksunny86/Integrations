package com.inov8.export.common;

import java.util.Properties;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import org.apache.log4j.Logger;

public class MailEngine {
	
	static Logger log = Logger.getLogger(MailEngine.class);
	
	public MailEngine() {
		super();
	}	
	
	
	public Boolean sendEmail(String msg,String to) {
		
		String from = MessageSource.getPoperties("mail.default.from");
		String host= MessageSource.getPoperties("mail.host");
		
		 //Get the session object  
		 Properties props = new Properties();  
		 props.put("mail.smtp.host",host);  
		 props.put("mail.smtp.auth", "true");  
		 
		 Session session = Session.getDefaultInstance(props,  new javax.mail.Authenticator() {  
				      protected PasswordAuthentication getPasswordAuthentication() {  
				    	  	String username = MessageSource.getPoperties("mail.username");
				  			String password = MessageSource.getPoperties("mail.password");
				    	  	return new PasswordAuthentication(username,password);  
				      }  
				    });  
	
		//Compose the message  
		    try {  
		     MimeMessage message = new MimeMessage(session);  
		     message.setFrom(new InternetAddress(from));  
		     message.addRecipient(Message.RecipientType.TO,new InternetAddress(to));  
		     message.setSubject("<Intimation>ZIP Export File ready to download");  
		     message.setText(msg);  
		       
		    //send the message  
		     Transport.send(message);
		     log.info("Email sent successfully to "+to);
		  		   
		     } catch (MessagingException e) {log.error(e.getMessage(), e);} 
		
		return true;
	}
}
