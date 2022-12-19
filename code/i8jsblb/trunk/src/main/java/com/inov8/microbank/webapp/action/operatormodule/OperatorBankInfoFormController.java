package com.inov8.microbank.webapp.action.operatormodule;

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
import com.inov8.microbank.common.model.OperatorModel;
import com.inov8.microbank.common.model.PaymentModeModel;
import com.inov8.microbank.common.model.operatorbankmodule.OperatorBankInfoListViewModel;
import com.inov8.microbank.common.util.MessageUtil;
import com.inov8.microbank.server.service.operatormodule.OperatorBankInfoManager;

public class OperatorBankInfoFormController extends AdvanceFormController {

	private OperatorBankInfoManager operatorBankInfoManager ;
	private ReferenceDataManager referenceDataManager;
	private Long id ;
	public void setOperatorBankInfoManager(
			OperatorBankInfoManager operatorBankInfoManager) {
		this.operatorBankInfoManager = operatorBankInfoManager;
	}
	public OperatorBankInfoFormController() {
		setCommandName("operatorBankInfoListViewModel");
		setCommandClass(OperatorBankInfoListViewModel.class);
	}
	@Override
	protected Object loadFormBackingObject(HttpServletRequest httpServletRequest) throws Exception {
		id = ServletRequestUtils.getLongParameter(httpServletRequest, "operatorBankInfoId");
	    if (null != id)
	    {
	      if (log.isDebugEnabled())
	      {
	        log.debug("id is not null....retrieving object from DB");
	      }

	      SearchBaseWrapper searchBaseWrapper = new SearchBaseWrapperImpl();
	      OperatorBankInfoListViewModel operatorBankInfoListViewModel = new OperatorBankInfoListViewModel();
	      operatorBankInfoListViewModel.setOperatorBankInfoId(id);

	      searchBaseWrapper.setBasePersistableModel(operatorBankInfoListViewModel);
	      searchBaseWrapper = this.operatorBankInfoManager.loadOperatorBankInfo(
	          searchBaseWrapper);
	      operatorBankInfoListViewModel = (OperatorBankInfoListViewModel) searchBaseWrapper.getBasePersistableModel();


	      if (log.isDebugEnabled())
	      {
	        log.debug("ticker String  is : " + operatorBankInfoListViewModel.getName());
	      }
	      return operatorBankInfoListViewModel;
	    }
	    else
	    {
	      if (log.isDebugEnabled())
	      {
	        log.debug("id is null....creating new instance of Model");
	      }
	      long theDate = new Date().getTime();
	      OperatorBankInfoListViewModel operatorBankInfoListViewModel = new OperatorBankInfoListViewModel();
	      operatorBankInfoListViewModel.setCreatedOn(new Date(theDate));
	      return operatorBankInfoListViewModel;
	    }
		

	}

	@Override
	protected Map loadReferenceData(HttpServletRequest arg0) throws Exception {
		// TODO Auto-generated method stub
		 OperatorModel operatorModel = new OperatorModel();
		    //operatorModel.setActive(true);
		    ReferenceDataWrapper referenceDataWrapper = new ReferenceDataWrapperImpl(
		        operatorModel, "name", SortingOrder.ASC);

		    try
		    {
		      referenceDataManager.getReferenceData(referenceDataWrapper);
		    }
		    catch (FrameworkCheckedException ex1)
		    {
		    }
		    List<OperatorModel> operatorModelList = null;
		    if (referenceDataWrapper.getReferenceDataList() != null)
		    {
		      operatorModelList = referenceDataWrapper.getReferenceDataList();
		    }

		    Map referenceDataMap = new HashMap();
		    referenceDataMap.put("operatorModelList", operatorModelList);
		    
		    
			 PaymentModeModel paymentModeModel = new PaymentModeModel();
			  //  operatorModel.setActive(true);
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

			OperatorBankInfoListViewModel operatorBankInfoListViewModel = (OperatorBankInfoListViewModel) command;
			

			if (null != id) {

				baseWrapper.setBasePersistableModel(operatorBankInfoListViewModel);

				baseWrapper = this.operatorBankInfoManager.updateOperatorBankInfo(baseWrapper);
				System.out.println("update case ");

			} else {

				
				baseWrapper.setBasePersistableModel(operatorBankInfoListViewModel);
				baseWrapper = this.operatorBankInfoManager.createOperatorBankInfo(baseWrapper);
				System.out.println("create case ");
				
				//baseWrapper = this.operatorBankInfoManager.createTickerUser(baseWrapper);
			}

			this.saveMessage(request, "Record saved successfully");
			ModelAndView modelAndView = new ModelAndView(this.getSuccessView());
			return modelAndView;

		} catch (FrameworkCheckedException ex) {
			if( ex.getMessage().equalsIgnoreCase("NameUniqueException") )
			{
				super.saveMessage(request, " Operator Bank with the same Operator Bank Name already exists.");
				return super.showForm(request, response, errors);				
			}			
			
			else if( ex.getMessage().equalsIgnoreCase("OperatorPaymentBankUniqueException") )
			{
				super.saveMessage(request, " Operator Bank with the same Operator ,Bank and Payment Mode already exists.");
				return super.showForm(request, response, errors);				
			}	
			else if (ExceptionErrorCodes.DATA_INTEGRITY_VIOLATION_EXCEPTION == ex
					.getErrorCode()) {
				super.saveMessage(request, "Operator Bank with the same Operator and Payment Mode already exists.");
				return super.showForm(request, response, errors);
			}
			throw ex;
		}
		catch (Exception ex) {
			super.saveMessage(request,MessageUtil.getMessage("6075"));
			return super.showForm(request, response, errors);
		}

	}
	public void setReferenceDataManager(ReferenceDataManager referenceDataManager) {
		this.referenceDataManager = referenceDataManager;
	}


}
