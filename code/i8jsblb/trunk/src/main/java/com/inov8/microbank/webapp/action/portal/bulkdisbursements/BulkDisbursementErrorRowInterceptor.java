/**
 * 
 */
package com.inov8.microbank.webapp.action.portal.bulkdisbursements;

import org.extremecomponents.table.bean.Row;
import org.extremecomponents.table.core.TableModel;
import org.extremecomponents.table.interceptor.RowInterceptor;

import com.inov8.microbank.common.model.BulkDisbursementsModel;

/**
 * @author NaseerUl
 *
 */
public class BulkDisbursementErrorRowInterceptor implements RowInterceptor
{

	@Override
	public void addRowAttributes(TableModel tableModel, Row row)
	{
		// TODO Auto-generated method stub
	}

	@Override
	public void modifyRowAttributes(TableModel tableModel, Row row)
	{
		BulkDisbursementsModel bulkDisbursementsModel = (BulkDisbursementsModel) tableModel.getCurrentRowBean();
		if( !bulkDisbursementsModel.getValidRecord() )
		{
			row.setStyle("color:red;font-weight:bold;");  
		}
		else
		{
			row.setStyle("");
		}
	}

}
