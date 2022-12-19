/**
 * 
 */
package com.inov8.microbank.webapp.action.portal.bulkdisbursements;

import java.util.Comparator;

import com.inov8.microbank.common.model.BulkDisbursementsModel;

/**
 * @author NaseerUl
 *
 */
public class BulkDisbursementInvalidRecordsComparator implements Comparator<BulkDisbursementsModel>
{
	@Override
	public int compare(BulkDisbursementsModel model1, BulkDisbursementsModel model2) {
		return model1.getValidRecord().compareTo(model2.getValidRecord());
	}
	
}
