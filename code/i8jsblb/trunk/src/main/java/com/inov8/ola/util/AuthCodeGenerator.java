package com.inov8.ola.util;

/** 	
 * @author 					Jawwad Farooq
 * Project Name: 			OLA
 * Creation Date: 			November 2008  			
 * Description:				
 */


import java.text.SimpleDateFormat;
import java.util.Date;

import org.apache.commons.lang.StringUtils;

import com.inov8.framework.server.dao.framework.jdbc.OracleSequenceGeneratorJdbcDAO;


public class AuthCodeGenerator
{
	
	private static OracleSequenceGeneratorJdbcDAO sequenceGen;
	
	
	public static String getAuthCode()
	{
		String today = new SimpleDateFormat("yyyyMMdd").format(new Date());
		
		String seqNumber = sequenceGen.nextLongValue().toString();
		
		seqNumber = StringUtils.leftPad(seqNumber, 8, "0");
		
		return today + seqNumber;
	}

	public void setSequenceGen(OracleSequenceGeneratorJdbcDAO sequenceGen)
	{
		this.sequenceGen = sequenceGen;
	}

}
