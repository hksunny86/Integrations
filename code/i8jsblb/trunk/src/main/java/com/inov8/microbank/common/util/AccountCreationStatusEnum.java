/**
 * 
 */
package com.inov8.microbank.common.util;

/**
 * @author NaseerUl
 *
 */
public enum AccountCreationStatusEnum
{
	INITIATED
	{
		@Override
		public String toString()
		{
			return "Initiated";
		}
	},
	SUCCESSFUL
	{
		@Override
		public String toString()
		{
			return "Successful";
		}
	},
	FAILED
	{
		@Override
		public String toString()
		{
			return "Failed";
		}
	}
}
