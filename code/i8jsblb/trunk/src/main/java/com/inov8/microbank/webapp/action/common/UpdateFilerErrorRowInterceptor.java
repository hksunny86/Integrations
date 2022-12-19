package com.inov8.microbank.webapp.action.common;

import org.extremecomponents.table.bean.Row;
import org.extremecomponents.table.core.TableModel;
import org.extremecomponents.table.interceptor.RowInterceptor;

import com.inov8.microbank.common.vo.BulkFilerNonFilerVO;

public class UpdateFilerErrorRowInterceptor implements RowInterceptor {

	@Override
	public void addRowAttributes(TableModel tableModel, Row row)
	{
		// TODO Auto-generated method stub
	}

	@Override
	public void modifyRowAttributes(TableModel tableModel, Row row)
	{
		BulkFilerNonFilerVO bulkFilerNonFilerVO = (BulkFilerNonFilerVO) tableModel.getCurrentRowBean();
		if( !bulkFilerNonFilerVO.getIsValid() )
		{
			row.setStyle("color:red;font-weight:bold;");  
		}
		else
		{
			row.setStyle("");
		}
	}
}
