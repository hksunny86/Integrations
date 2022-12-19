/**
 * 
 */
package com.inov8.microbank.webapp.action.portal.mfsaccountmodule;

import org.extremecomponents.table.bean.Row;
import org.extremecomponents.table.core.TableModel;
import org.extremecomponents.table.interceptor.RowInterceptor;

import com.inov8.microbank.common.vo.account.BulkCustomerAccountVo;

/**
 * @author NaseerUl
 *
 */
public class BulkCustomersErrorRowInterceptor implements RowInterceptor
{

	@Override
	public void addRowAttributes(TableModel tableModel, Row row)
	{
	}

	@Override
	public void modifyRowAttributes(TableModel tableModel, Row row)
	{
		BulkCustomerAccountVo bulkCustomerAccountVo = (BulkCustomerAccountVo) tableModel.getCurrentRowBean();
		if( !bulkCustomerAccountVo.getValidRecord() )
		{
			row.setStyle("color:red;font-weight:bold;");  
		}
		else
		{
			row.setStyle("");
		}
	}

}
