/**
 * 
 */
package com.inov8.microbank.server.dao.portal.transactiondetaili8module.hibernate;

import java.io.File;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

import org.apache.log4j.Logger;

/**
 * @author KashifBa
 *
 */
public class TransactionDataDeleteThread implements Runnable {

	private final Logger logger = Logger.getLogger(TransactionDataDeleteThread.class);

	public TransactionDataDeleteThread(String path) {
		
		this.path = path;
	}

	private String path = null;
	
	@Override
	public void run() {

		Calendar calendar = new GregorianCalendar();
		calendar.set(GregorianCalendar.HOUR, 0);
		calendar.set(GregorianCalendar.MINUTE, 0);
		calendar.set(GregorianCalendar.SECOND, 0);
		
		Date cutoffDate = calendar.getTime();
		
		logger.debug("Delete.. If Exists Report File(s) Before "+cutoffDate);
		
		File directory = new File(path);
		
		File[] files = directory.listFiles();

		for (File file : files) {
			
			Date lastMod = new Date(file.lastModified());
						
			if(cutoffDate.after(lastMod)) {

				logger.debug("\nDeleting File: " + file.getName() + ", Created on Date: " + lastMod + "");
				file.delete();
			}
		}
	}

	public static void main(String d[]) {	
		new Thread(new TransactionDataDeleteThread("C:/java/jboss-as-7.1.1.Final/standalone/deployments/i8MicrobankJS.war/zip_reports/")).start();
	}
}