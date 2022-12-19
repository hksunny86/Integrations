package com.inov8.microbank.common.util;

public class ValidationErrors 
{
	  private StringBuilder strBuilder;
	  

	  public ValidationErrors()
	  {
		  this.strBuilder = new StringBuilder();
	  }

	  	  
	  public StringBuilder getStringBuilder()
	  {
	    return this.strBuilder;
	  }
	  
	  public String getErrors()
	  {
		  return this.strBuilder.toString();
	  }

	  public boolean hasValidationErrors()
	  {
		  if(getStringBuilder().length() > 0)
		  {
			  return true;
		  }
		  else
		  {
			  return false;
		  }
	  }
}
