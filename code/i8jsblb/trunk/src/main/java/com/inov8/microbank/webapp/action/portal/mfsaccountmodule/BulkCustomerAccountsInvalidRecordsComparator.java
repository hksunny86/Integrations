/**
 * 
 */
package com.inov8.microbank.webapp.action.portal.mfsaccountmodule;

import java.util.Comparator;

import com.inov8.microbank.common.vo.account.BulkCustomerAccountVo;


/**
 * @author NaseerUl
 *
 */
public class BulkCustomerAccountsInvalidRecordsComparator implements Comparator<BulkCustomerAccountVo>
{
	@Override
	public int compare(BulkCustomerAccountVo model1, BulkCustomerAccountVo model2)
	{
		return model1.getValidRecord().compareTo(model2.getValidRecord());
	}
	
}
