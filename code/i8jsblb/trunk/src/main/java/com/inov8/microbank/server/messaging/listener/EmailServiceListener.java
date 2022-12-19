package com.inov8.microbank.server.messaging.listener;

import java.util.Date;

import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.ObjectMessage;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.mail.SimpleMailMessage;

import com.inov8.framework.common.model.messagemodule.EmailMessage;
import com.inov8.framework.server.service.common.MailEngine;
import com.inov8.microbank.common.exception.EmailServiceSendFailureException;

public class EmailServiceListener implements MessageListener
{

	private MailEngine mailEngine;
	private SimpleMailMessage mailMessage;
	protected static Log logger	= LogFactory.getLog(EmailServiceListener.class);

	public void onMessage(Message message)
	{
		if(logger.isDebugEnabled())
		{
			logger.debug("Inside onMessage of EmailServiceListener..");
		}	
		
		if (message instanceof ObjectMessage)
		{
			
			try
			{
				Object obj = ((ObjectMessage) message).getObject();
				if (null != obj)
				{
					if (obj instanceof EmailMessage)
					{
						EmailMessage emailMessage = (EmailMessage) obj;
						
				        String recipientEmail = "";
				        
				        if ((emailMessage.getRecepients() != null) && (emailMessage.getRecepients().length > 0)) {
				        
				        	recipientEmail = emailMessage.getRecepients()[0];
				        }
				        
				        Long startTime = Long.valueOf(System.currentTimeMillis());
				        logger.info("Sending email start:\n[Message ID:" + startTime + ", To:" + recipientEmail + ", Subject:" + emailMessage.getSubject() + "]");							
						
						mailMessage.setTo(emailMessage.getRecepients());
						
						if(emailMessage.getCc() != null){	
							mailMessage.setCc(emailMessage.getCc());
						}
						
						if(emailMessage.getBcc() != null){
							mailMessage.setBcc(emailMessage.getBcc());
						}

						if(emailMessage.getSubject() != null){
							mailMessage.setSubject(emailMessage.getSubject());
						}
						
						mailMessage.setText(emailMessage.getText());
						mailMessage.setSentDate(new Date());
						
						mailEngine.send(mailMessage);
						
						Long endTime = System.currentTimeMillis();
					      logger.info("Sending email Complete:"+
					      "\n[Message ID(Start Time in millis):" + startTime +", To:"+recipientEmail+ ", Subject:" + emailMessage.getSubject() + 
					      ", Message Completed in (seconds):"+ ((endTime-startTime)/1000) + "]");
					}
				}
			}catch (Exception ex){
				logger.error("Exception caught while sending email...",ex);
				throw new EmailServiceSendFailureException(ex.getMessage(), ex);
			}
		}
	}

	/**
	 * @param mailEngine the mailEngine to set
	 */
	public void setMailEngine(MailEngine mailEngine) {
		this.mailEngine = mailEngine;
	}

	/**
	 * @param mailMessage the mailMessage to set
	 */
	public void setMailMessage(SimpleMailMessage mailMessage) {
		this.mailMessage = mailMessage;
	}

}
