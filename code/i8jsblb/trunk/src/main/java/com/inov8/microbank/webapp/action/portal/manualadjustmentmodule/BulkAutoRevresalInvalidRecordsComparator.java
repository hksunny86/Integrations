package com.inov8.microbank.webapp.action.portal.manualadjustmentmodule;

import com.inov8.microbank.common.model.portal.bulkdisbursements.BulkAutoReversalModel;
import com.inov8.microbank.common.model.portal.bulkdisbursements.BulkManualAdjustmentModel;

import java.util.Comparator;

public class BulkAutoRevresalInvalidRecordsComparator implements Comparator<BulkAutoReversalModel>{

	@Override
	public int compare(BulkAutoReversalModel model1,
					   BulkAutoReversalModel model2) {
		return model1.getIsValid().compareTo(model2.getIsValid());
	}

}
