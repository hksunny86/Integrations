package com.inov8.microbank.webapp.action.customermodule;

import org.extremecomponents.table.bean.Row;
import org.extremecomponents.table.core.TableModel;
import org.extremecomponents.table.interceptor.RowInterceptor;

import com.inov8.microbank.common.model.customermodule.BulkUpdateCustomerScreeningModel;

public class BulkScreeningPerformedInvalidRowInterceptor implements RowInterceptor{

	@Override
	public void addRowAttributes(TableModel arg0, Row arg1) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void modifyRowAttributes(TableModel arg0, Row arg1) {

		BulkUpdateCustomerScreeningModel bulkUpdateCustomerScreeningModel = (BulkUpdateCustomerScreeningModel) arg0.getCurrentRowBean();
		if( !bulkUpdateCustomerScreeningModel.getIsValid() )
		{
			arg1.setStyle("color:red;font-weight:bold;");  
		}
		else
		{
			arg1.setStyle("");
		}
	
		
	}

}
