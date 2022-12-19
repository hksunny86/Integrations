package com.inov8.microbank.webapp.action.portal.usermanagementmodule;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.ajaxtags.helpers.AjaxXmlBuilder;
import org.springframework.web.bind.ServletRequestUtils;

import com.inov8.framework.common.model.SortingOrder;
import com.inov8.framework.common.wrapper.ReferenceDataWrapper;
import com.inov8.framework.common.wrapper.ReferenceDataWrapperImpl;
import com.inov8.framework.server.service.common.ReferenceDataManager;
import com.inov8.microbank.common.model.BankModel;
import com.inov8.microbank.common.model.DistributorModel;
import com.inov8.microbank.common.model.MnoModel;
import com.inov8.microbank.common.model.OperatorModel;
import com.inov8.microbank.common.model.RetailerModel;
import com.inov8.microbank.common.model.SupplierModel;
import com.inov8.microbank.common.util.BankConstantsInterface;
import com.inov8.microbank.common.util.UserTypeConstantsInterface;
import com.inov8.microbank.webapp.action.ajax.AjaxController;

public class ReferenceDataController extends AjaxController
{

	private ReferenceDataManager	referenceDataManager;

	@SuppressWarnings("unchecked")
	public String getResponseContent(HttpServletRequest request, HttpServletResponse response)
	{

		try
		{
			Long appUserTypeId = ServletRequestUtils.getRequiredLongParameter(request,"appUserTypeId");
			ReferenceDataWrapper referenceDataWrapper;

			AjaxXmlBuilder ajaxXmlBuilder = new AjaxXmlBuilder();
			//ajaxXmlBuilder.addItemAsCData(" ", "");
			String returnString = "";
			if (UserTypeConstantsInterface.INOV8.equals(appUserTypeId))
			{ // for i8 / csr
				OperatorModel operatorModel = new OperatorModel();
				List<OperatorModel> operatorModelList = null;
				referenceDataWrapper = new ReferenceDataWrapperImpl(operatorModel, "name", SortingOrder.ASC);
				this.referenceDataManager.getReferenceData(referenceDataWrapper);
				operatorModelList = referenceDataWrapper.getReferenceDataList();
				returnString = ajaxXmlBuilder.addItems(operatorModelList, "name", "operatorId").toString();
			}
			else if (UserTypeConstantsInterface.BANK.equals(appUserTypeId))
			{ // for Bank
				BankModel bankModel = new BankModel();
				List<BankModel> bankModelList = null;
				bankModel.setActive(true);
				referenceDataWrapper = new ReferenceDataWrapperImpl(bankModel, "name", SortingOrder.ASC);
				this.referenceDataManager.getReferenceData(referenceDataWrapper);
				bankModelList = referenceDataWrapper.getReferenceDataList();
				List<BankModel> banks = new ArrayList<BankModel>(0);
				
				if(null != bankModelList){
					Iterator<BankModel> i = bankModelList.iterator();
					while(i.hasNext()) {
						BankModel bm = (BankModel)i.next();
						if(BankConstantsInterface.ASKARI_BANK_ID.longValue() == bm.getBankId().longValue()){
							banks.add(bm);
							break;
						}	
					}
				}
				returnString = ajaxXmlBuilder.addItems(banks, "name", "bankId").toString();
			}
			else if (UserTypeConstantsInterface.MNO.equals(appUserTypeId))
			{ // for MNO
				MnoModel mnoModel = new MnoModel();
				List<MnoModel> mnoModelList = null;
				mnoModel.setActive(true);
				referenceDataWrapper = new ReferenceDataWrapperImpl(mnoModel, "name", SortingOrder.ASC);
				this.referenceDataManager.getReferenceData(referenceDataWrapper);
				mnoModelList = referenceDataWrapper.getReferenceDataList();
				returnString = ajaxXmlBuilder.addItems(mnoModelList, "name", "mnoId").toString();
			}
			else if(UserTypeConstantsInterface.SUPPLIER.equals(appUserTypeId))
			{ 
				// for Product Supplier or Payment Service
				SupplierModel supplierModel = new SupplierModel();
				List<SupplierModel> supplierModelList = null;
				supplierModel.setActive(true);
				referenceDataWrapper = new ReferenceDataWrapperImpl(supplierModel, "name", SortingOrder.ASC);
				this.referenceDataManager.getReferenceData(referenceDataWrapper);
				supplierModelList = referenceDataWrapper.getReferenceDataList();
				returnString = ajaxXmlBuilder.addItems(supplierModelList, "name", "supplierId").toString();
			}
			else if(UserTypeConstantsInterface.RETAILER.equals(appUserTypeId))
			{ 
				RetailerModel retailerModel = new RetailerModel();
				List<RetailerModel> retailerModelList = null;
				retailerModel.setActive(true);
				referenceDataWrapper = new ReferenceDataWrapperImpl(retailerModel, "name", SortingOrder.ASC);
				this.referenceDataManager.getReferenceData(referenceDataWrapper);
				retailerModelList = referenceDataWrapper.getReferenceDataList();
				returnString = ajaxXmlBuilder.addItems(retailerModelList, "name", "retailerId").toString();
			}
			else if(UserTypeConstantsInterface.DISTRIBUTOR.equals(appUserTypeId))
			{ 
				DistributorModel distributorModel = new DistributorModel();
				List<DistributorModel> distributorModelList = null;
				distributorModel.setActive(true);
				referenceDataWrapper = new ReferenceDataWrapperImpl(distributorModel, "name", SortingOrder.ASC);
				this.referenceDataManager.getReferenceData(referenceDataWrapper);
				distributorModelList = referenceDataWrapper.getReferenceDataList();
				returnString = ajaxXmlBuilder.addItems(distributorModelList, "name", "distributorId").toString();
			}
			else
			{
				returnString = ajaxXmlBuilder.addItemAsCData("", "").toString();
			}

			return returnString;

		}
		catch (Exception e)
		{
			e.printStackTrace();
			//String result = new AjaxXmlBuilder().addItemAsCData(" ", "").toString();
			//return result;
			return "";
		}

	}

	public void setReferenceDataManager(ReferenceDataManager referenceDataManager)
	{
		this.referenceDataManager = referenceDataManager;
	}

}
