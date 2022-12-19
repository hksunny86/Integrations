package com.inov8.microbank.account.controller;

import com.inov8.microbank.account.vo.BlacklistMarkingBulkUploadVo;
import com.inov8.microbank.account.vo.CustomerACNatureMarkingUploadVo;
import org.extremecomponents.table.bean.Row;
import org.extremecomponents.table.core.TableModel;
import org.extremecomponents.table.interceptor.RowInterceptor;

public class CustomerACNatureMarkingErrorRowInterceptor implements RowInterceptor {
    @Override
    public void addRowAttributes(TableModel tableModel, Row row)
    {
    }

    @Override
    public void modifyRowAttributes(TableModel tableModel, Row row)
    {
        CustomerACNatureMarkingUploadVo customerACNatureMarkingUploadVo = (CustomerACNatureMarkingUploadVo) tableModel.getCurrentRowBean();
        if( !customerACNatureMarkingUploadVo.getValidRecord() )
        {
            row.setStyle("color:red;font-weight:bold;");
        }
        else
        {
            row.setStyle("");
        }
    }
}
