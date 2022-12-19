package com.inov8.microbank.webapp.action.portal.manualadjustmentmodule;

import org.extremecomponents.table.bean.Row;
import org.extremecomponents.table.core.TableModel;
import org.extremecomponents.table.interceptor.RowInterceptor;

import com.inov8.microbank.common.model.BulkDisbursementsModel;
import com.inov8.microbank.common.model.portal.bulkdisbursements.BulkManualAdjustmentModel;

public class BulkManualAdjustmentErrorRowInterceptor implements RowInterceptor{

	@Override
	public void addRowAttributes(TableModel arg0, Row arg1) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void modifyRowAttributes(TableModel arg0, Row arg1) {
		BulkManualAdjustmentModel bulkManualAdjustmentModel = (BulkManualAdjustmentModel) arg0.getCurrentRowBean();
		if( !bulkManualAdjustmentModel.getIsValid() )
		{
			arg1.setStyle("color:red;font-weight:bold;");  
		}
		else
		{
			arg1.setStyle("");
		}
	}
		
	
}
