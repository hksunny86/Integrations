package com.inov8.microbank.server.service.jms;

import java.io.Serializable;
import java.util.ArrayList;

import javax.jms.Destination;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.Session;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.jms.core.MessageCreator;

import com.inov8.microbank.common.model.messagemodule.SmsMessage;


public class JmsProducerImpl implements JmsProducer, ApplicationContextAware
{
	private final Log logger = LogFactory.getLog(this.getClass());
	private JmsTemplate jmsTemplate;
	private ApplicationContext ctx;		
	
	/* (non-Javadoc)
	 * @see com.inov8.microbank.common.jms.JmsPublisher#publish(java.io.Serializable, java.lang.String)
	 */
	public void produce(final Serializable obj, final String destinationName)
	{		
		if(null != obj)
	    {
	    	if(null != destinationName && !("".equals(destinationName)))
	    	{
	    		Destination destination = (Destination) ctx.getBean(destinationName);
	    		this.jmsTemplate.send(destination, new MessageCreator()
	    		{
	    			public Message createMessage(Session session) throws JMSException
	    			{
	    				Message message = null;
	    				String mobileNo = null;
	    				if(obj instanceof SmsMessage){
	    					mobileNo = ((SmsMessage)obj).getMobileNo();
	    				} else if(obj instanceof ArrayList<?>) {
	    				    ArrayList<SmsMessage> smsMessageList = (ArrayList<SmsMessage>) obj;
	    				    //mobileNo = smsMessageList.toString();
	    				}
	    				
	    				logger.info("Publishing the message to " + destinationName + " destination" + (mobileNo == null ? "" : (" Mobile No(s):" + mobileNo)));
	    				
	    				/** Commented for security reasons
	    				 *logger.debug("The message contents are: \n" + obj.toString());
 	    				 */
	    				if (obj instanceof String)
	    				{
							message = session.createTextMessage(obj.toString());
						}
	    				else
	    				{
	    					message = session.createObjectMessage(obj);
	    				}
	    				return message;
	    			}
	    		});
	    	}
	    	else
	    	{
	    		logger.fatal("Destination name cannot be null or empty String");
	    		throw new IllegalArgumentException("Destination name cannot be null or empty String.");
	    	}
	    }
	    else
	    {
	    	logger.warn("The object passed is null. Not publishing anything");	    	
	    }
	}

	public void setApplicationContext(ApplicationContext ctx) throws BeansException
	{
		this.ctx = ctx;
		
	}

	public void setJmsTemplate(JmsTemplate jmsTemplate)
	{
		this.jmsTemplate = jmsTemplate;
	}
	
}
