package com.inov8.microbank.webapp.action.suppliermodule;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.validation.BindException;
import org.springframework.web.bind.ServletRequestUtils;
import org.springframework.web.servlet.ModelAndView;

import com.inov8.framework.common.exception.ExceptionErrorCodes;
import com.inov8.framework.common.exception.FrameworkCheckedException;
import com.inov8.framework.common.model.SortingOrder;
import com.inov8.framework.common.wrapper.BaseWrapper;
import com.inov8.framework.common.wrapper.BaseWrapperImpl;
import com.inov8.framework.common.wrapper.ReferenceDataWrapper;
import com.inov8.framework.common.wrapper.ReferenceDataWrapperImpl;
import com.inov8.framework.common.wrapper.SearchBaseWrapper;
import com.inov8.framework.common.wrapper.SearchBaseWrapperImpl;
import com.inov8.framework.server.service.common.ReferenceDataManager;
import com.inov8.framework.webapp.action.AdvanceFormController;
import com.inov8.microbank.common.model.BankModel;
import com.inov8.microbank.common.model.PaymentModeModel;
import com.inov8.microbank.common.model.SupplierModel;
import com.inov8.microbank.common.model.suppliermodule.SupplierBankInfoListViewModel;
import com.inov8.microbank.server.service.suppliermodule.SupplierBankInfoManager;

public class SupplierBankInfoFormController extends AdvanceFormController {

	private SupplierBankInfoManager supplierBankInfoManager ;
	private ReferenceDataManager referenceDataManager;
	private Long id ;
	public void setSupplierBankInfoManager(
			SupplierBankInfoManager supplierBankInfoManager) {
		this.supplierBankInfoManager = supplierBankInfoManager;
	}
	public SupplierBankInfoFormController() {
		setCommandName("supplierBankInfoListViewModel");
		setCommandClass(SupplierBankInfoListViewModel.class);
	}
	@Override
	protected Object loadFormBackingObject(HttpServletRequest httpServletRequest) throws Exception {
		id = ServletRequestUtils.getLongParameter(httpServletRequest, "supplierBankInfoId");
	    if (null != id)
	    {
	      if (log.isDebugEnabled())
	      {
	        log.debug("id is not null....retrieving object from DB");
	      }

	      SearchBaseWrapper searchBaseWrapper = new SearchBaseWrapperImpl();
	      SupplierBankInfoListViewModel supplierBankInfoListViewModel = new SupplierBankInfoListViewModel();
	      supplierBankInfoListViewModel.setSupplierBankInfoId(id);

	      searchBaseWrapper.setBasePersistableModel(supplierBankInfoListViewModel);
	      searchBaseWrapper = this.supplierBankInfoManager.loadSupplierBankInfo(
	          searchBaseWrapper);
	      supplierBankInfoListViewModel = (SupplierBankInfoListViewModel) searchBaseWrapper.getBasePersistableModel();


	      if (log.isDebugEnabled())
	      {
	        log.debug("ticker String  is : " + supplierBankInfoListViewModel.getName());
	      }
	      return supplierBankInfoListViewModel;
	    }
	    else
	    {
	      if (log.isDebugEnabled())
	      {
	        log.debug("id is null....creating new instance of Model");
	      }
	      long theDate = new Date().getTime();
	      SupplierBankInfoListViewModel supplierBankInfoListViewModel = new SupplierBankInfoListViewModel();
	      supplierBankInfoListViewModel.setCreatedOn(new Date(theDate));
	      return supplierBankInfoListViewModel;
	    }
		

	}

	@Override
	protected Map loadReferenceData(HttpServletRequest arg0) throws Exception {
		// TODO Auto-generated method stub
		 SupplierModel supplierModel = new SupplierModel();
		    supplierModel.setActive(true);
		    ReferenceDataWrapper referenceDataWrapper = new ReferenceDataWrapperImpl(
		        supplierModel, "name", SortingOrder.ASC);

		    try
		    {
		      referenceDataManager.getReferenceData(referenceDataWrapper);
		    }
		    catch (FrameworkCheckedException ex1)
		    {
		    }
		    List<SupplierModel> supplierModelList = null;
		    if (referenceDataWrapper.getReferenceDataList() != null)
		    {
		      supplierModelList = referenceDataWrapper.getReferenceDataList();
		    }

		    Map referenceDataMap = new HashMap();
		    referenceDataMap.put("supplierModelList", supplierModelList);
		    
		    
			 PaymentModeModel paymentModeModel = new PaymentModeModel();
			    supplierModel.setActive(true);
			    referenceDataWrapper = new ReferenceDataWrapperImpl(
			        paymentModeModel, "name", SortingOrder.ASC);

			    try
			    {
			      referenceDataManager.getReferenceData(referenceDataWrapper);
			    }
			    catch (FrameworkCheckedException ex1)
			    {
			    }
			    List<PaymentModeModel> paymentModeModelList = null;
			    if (referenceDataWrapper.getReferenceDataList() != null)
			    {
			      paymentModeModelList = referenceDataWrapper.getReferenceDataList();
			    }

			    
			    referenceDataMap.put("paymentModeModelList", paymentModeModelList);
			    
			    
			    BankModel bankModel = new BankModel();
			    bankModel.setActive(true);
			    referenceDataWrapper = new ReferenceDataWrapperImpl(
			        bankModel, "name", SortingOrder.ASC);

			    try
			    {
			      referenceDataManager.getReferenceData(referenceDataWrapper);
			    }
			    catch (FrameworkCheckedException ex1)
			    {
			    }
			    List<BankModel> bankModelList = null;
			    if (referenceDataWrapper.getReferenceDataList() != null)
			    {
			      bankModelList = referenceDataWrapper.getReferenceDataList();
			    }

			    
			    referenceDataMap.put("bankModelList", bankModelList);
		    return referenceDataMap;
		
	}

	@Override
	protected ModelAndView onCreate(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object object, BindException bindException) throws Exception {
		return this.createOrUpdate(httpServletRequest, httpServletResponse,
				object, bindException);
	}

	@Override
	protected ModelAndView onUpdate(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object object, BindException bindException) throws Exception {
		return this.createOrUpdate(httpServletRequest, httpServletResponse,
				object, bindException);
	}
	
	private ModelAndView createOrUpdate(HttpServletRequest request,
			HttpServletResponse response, Object command, BindException errors)
			throws Exception {

		try {
			BaseWrapper baseWrapper = new BaseWrapperImpl();

			SupplierBankInfoListViewModel supplierBankInfoListViewModel = (SupplierBankInfoListViewModel) command;
			

			if (null != id) {

				baseWrapper.setBasePersistableModel(supplierBankInfoListViewModel);

				baseWrapper = this.supplierBankInfoManager.updateSupplierBankInfo(baseWrapper);
				System.out.println("update case ");

			} else {

				
				baseWrapper.setBasePersistableModel(supplierBankInfoListViewModel);
				baseWrapper = this.supplierBankInfoManager.createSupplierBankInfo(baseWrapper);
				System.out.println("create case ");
				
				//baseWrapper = this.supplierBankInfoManager.createTickerUser(baseWrapper);
			}

			this.saveMessage(request, "Record saved successfully");
			ModelAndView modelAndView = new ModelAndView(this.getSuccessView());
			return modelAndView;

		} catch (FrameworkCheckedException ex) {
			if( ex.getMessage().equalsIgnoreCase("NameUniqueException") )
			{
				super.saveMessage(request, " Supplier Bank with the same Supplier Bank Name already exists.");
				return super.showForm(request, response, errors);				
			}			
			
			else if (ExceptionErrorCodes.DATA_INTEGRITY_VIOLATION_EXCEPTION == ex
					.getErrorCode()) {
				super.saveMessage(request, "Supplier Bank with the same Supplier and Payment Mode already exists.");
				return super.showForm(request, response, errors);
			}
			throw ex;
		}

	}
	public void setReferenceDataManager(ReferenceDataManager referenceDataManager) {
		this.referenceDataManager = referenceDataManager;
	}


}
