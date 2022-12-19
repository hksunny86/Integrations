package com.inov8.microbank.account.controller;

import com.inov8.microbank.account.vo.BlacklistMarkingBulkUploadVo;
import org.extremecomponents.table.bean.Row;
import org.extremecomponents.table.core.TableModel;
import org.extremecomponents.table.interceptor.RowInterceptor;

/**
 * Created by Malik on 8/24/2016.
 */
public class BlacklistMarkingErrorRowInterceptor implements RowInterceptor
{

        @Override
        public void addRowAttributes(TableModel tableModel, Row row)
        {
        }

        @Override
        public void modifyRowAttributes(TableModel tableModel, Row row)
        {
            BlacklistMarkingBulkUploadVo blacklistMarkingBulkUploadVo = (BlacklistMarkingBulkUploadVo) tableModel.getCurrentRowBean();
            if( !blacklistMarkingBulkUploadVo.getValidRecord() )
            {
                row.setStyle("color:red;font-weight:bold;");
            }
            else
            {
                row.setStyle("");
            }
        }

}
