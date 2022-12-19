package com.inov8.microbank.server.service.portal.paymentmodesmodule;

import java.util.LinkedHashMap;

import com.inov8.framework.common.exception.FrameworkCheckedException;
import com.inov8.framework.common.model.SortingOrder;
import com.inov8.framework.common.util.CustomList;
import com.inov8.framework.common.wrapper.SearchBaseWrapper;
import com.inov8.microbank.common.model.PaymentModeModel;
import com.inov8.microbank.server.dao.mfsmodule.PaymentModeDAO;


public class ViewPaymentModeManagerImp implements  ViewPaymentModeManager
{

	
	private PaymentModeDAO paymentModeDAO ; 
		
	
	public SearchBaseWrapper viewAvailablePaymentModeList(SearchBaseWrapper searchBaseWrapper) throws FrameworkCheckedException 
	{
		
		try{
			LinkedHashMap<String,SortingOrder> sortingOrderMap = new LinkedHashMap();
			sortingOrderMap.put("name", SortingOrder.ASC);
			CustomList<PaymentModeModel> list = this.paymentModeDAO.findAll(sortingOrderMap);
			searchBaseWrapper.setCustomList(list);
			return searchBaseWrapper;
		}
		catch(Exception ex)
		{
			ex.printStackTrace();
			return searchBaseWrapper;
		}
		
		
	}


	public void setPaymentModeDAO(PaymentModeDAO paymentModeDAO) {
		this.paymentModeDAO = paymentModeDAO;
	}


}
