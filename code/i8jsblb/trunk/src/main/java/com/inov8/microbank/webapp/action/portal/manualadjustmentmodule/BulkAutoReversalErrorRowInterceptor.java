package com.inov8.microbank.webapp.action.portal.manualadjustmentmodule;

import com.inov8.microbank.common.model.portal.bulkdisbursements.BulkAutoReversalModel;
import org.extremecomponents.table.bean.Row;
import org.extremecomponents.table.core.TableModel;
import org.extremecomponents.table.interceptor.RowInterceptor;

public class BulkAutoReversalErrorRowInterceptor implements RowInterceptor {
    @Override
    public void addRowAttributes(TableModel tableModel, Row row) {

    }

    @Override
    public void modifyRowAttributes(TableModel arg0, Row arg1) {
        BulkAutoReversalModel bulkAutoReversalModel = (BulkAutoReversalModel) arg0.getCurrentRowBean();
        if( !bulkAutoReversalModel.getIsValid() )
        {
            arg1.setStyle("color:red;font-weight:bold;");
        }
        else
        {
            arg1.setStyle("");
        }
    }

}
