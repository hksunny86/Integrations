package com.inov8.microbank.server.service.transactionmodule;

import com.inov8.common.util.RandomUtils;

/**
 * <p>Title: </p>
 *
 * <p>Description: </p>
 *
 * <p>Copyright: Copyright (c) 2006</p>
 *
 * <p>Company: </p>
 *
 * @author not attributable
 * @version 1.0
 */
public class TransactionCodeGeneratorImpl
    implements TransactionCodeGenerator
{
  public TransactionCodeGeneratorImpl()
  {
  }

  /**
   * This methid generated a random Alpha numeric number of 10 characters using a utility class.
   * @return String generated number.
   */
  public String getTransactionCode()
  {    
    String orgChars = "TX" ;
    String alphaChars = RandomUtils.generateRandom(8, true, true).toUpperCase() ;
//    String numChars = randomUtils.generateRandom(4, false, true);

    return orgChars + alphaChars ;

  }


  // Method added for generating transaction codes for AllPay 
  // @author Jawwad Farooq
  public String getAllPayTransactionCode()
  {     
	  //Maqsood Shahzad - Changing transaction code length to 12 for USSD application
	  String alphaChars = RandomUtils.generateRandom(12, false, true).toUpperCase() ;
	  return alphaChars ;
  }
  
  public static void main(String [] a)
  {
	  System.out.println(RandomUtils.generateRandom(8, false, true).toUpperCase());
  }
  
}
