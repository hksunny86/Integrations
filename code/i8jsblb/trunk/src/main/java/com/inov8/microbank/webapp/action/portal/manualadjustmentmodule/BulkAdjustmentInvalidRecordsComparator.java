package com.inov8.microbank.webapp.action.portal.manualadjustmentmodule;

import java.util.Comparator;

import com.inov8.microbank.common.model.BulkDisbursementsModel;
import com.inov8.microbank.common.model.portal.bulkdisbursements.BulkManualAdjustmentModel;

public class BulkAdjustmentInvalidRecordsComparator implements Comparator<BulkManualAdjustmentModel>{

	@Override
	public int compare(BulkManualAdjustmentModel model1,
			BulkManualAdjustmentModel model2) {
		return model1.getIsValid().compareTo(model2.getIsValid());
	}

}
