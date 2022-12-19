package com.inov8.ola.util;

/** 	
 * @author 					Jawwad Farooq
 * Project Name: 			OLA
 * Creation Date: 			November 2008  			
 * Description:				
 */


import com.inov8.framework.server.dao.framework.jdbc.OracleSequenceGeneratorJdbcDAO;


public class AccountNumberGenerator
{
	
	private static OracleSequenceGeneratorJdbcDAO sequenceGen;
	
	
	public static String getAccountNumber()
	{
		String accountNumber = sequenceGen.nextLongValue().toString() ;
		
		while( accountNumber.length() != 10 )
		{
			accountNumber = "0" + accountNumber ;
		}
		
		return accountNumber ;
	}

	
	public void setSequenceGen(OracleSequenceGeneratorJdbcDAO seq)
	{
		this.sequenceGen = seq;
	}
	
}
