package com.inov8.microbank.webapp.action.customermodule;

import java.util.Comparator;

import com.inov8.microbank.common.model.customermodule.BulkUpdateCustomerScreeningModel;

public class ScreeningPerformedInvalidRecordsComparator implements Comparator<BulkUpdateCustomerScreeningModel> {

	@Override
	public int compare(BulkUpdateCustomerScreeningModel o1,
			BulkUpdateCustomerScreeningModel o2) {
		return o1.getIsValid().compareTo(o2.getIsValid());
	}

}
