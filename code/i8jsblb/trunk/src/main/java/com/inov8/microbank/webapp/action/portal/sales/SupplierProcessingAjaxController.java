package com.inov8.microbank.webapp.action.portal.sales;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.bind.ServletRequestUtils;

import com.inov8.framework.common.wrapper.BaseWrapper;
import com.inov8.framework.common.wrapper.BaseWrapperImpl;
import com.inov8.microbank.common.model.TransactionModel;
import com.inov8.microbank.common.util.EncryptionUtil;
import com.inov8.microbank.common.util.PortalConstants;
import com.inov8.microbank.server.service.portal.sales.SalesManager;
import com.inov8.microbank.webapp.action.ajax.AjaxController;

public class SupplierProcessingAjaxController extends AjaxController
{

	private SalesManager salesManager;

	@Override
	public String getResponseContent(HttpServletRequest request, HttpServletResponse response) throws Exception
	{
		StringBuffer buffer = new StringBuffer();
		Long processingStatus = ServletRequestUtils.getRequiredLongParameter(request, "processingStatus");
		Long transactionId = new Long(EncryptionUtil.decryptWithDES(ServletRequestUtils.getStringParameter(request, "transactionId")));
	
		BaseWrapper baseWrapper = new BaseWrapperImpl();
		baseWrapper.putObject(PortalConstants.KEY_ACTION_ID, PortalConstants.ACTION_UPDATE);
		baseWrapper.putObject(PortalConstants.KEY_USECASE_ID, PortalConstants.MNO_VIEW_SALES_USECASE_ID);
		
		TransactionModel transactionModel = new TransactionModel();
		transactionModel.setTransactionId(transactionId);
		transactionModel.setSupProcessingStatusId(processingStatus);		
		baseWrapper.setBasePersistableModel(transactionModel);
		
		baseWrapper = salesManager.updateTransactionSupStatus(baseWrapper);
		
		String success = (String)baseWrapper.getObject("success");
		
		String msg = "";
		if(null!=success && success.equals("true"))
		{
			buffer.append(getMessageSourceAccessor().getMessage("viewsale.status.success",request.getLocale()));
		}

		return buffer.toString();
	}

	/**
	 * @param salesManager the salesManager to set
	 */
	public void setSalesManager(SalesManager salesManager) {
		this.salesManager = salesManager;
	}


	
}
