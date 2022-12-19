package com.inov8.microbank.webapp.action.OLAAccountModule;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.validation.BindException;
import org.springframework.web.bind.ServletRequestUtils;
import org.springframework.web.servlet.ModelAndView;

import com.inov8.framework.common.wrapper.BaseWrapper;
import com.inov8.framework.common.wrapper.BaseWrapperImpl;
import com.inov8.framework.server.service.common.ReferenceDataManager;
import com.inov8.framework.webapp.action.AdvanceFormController;
import com.inov8.microbank.common.util.UserUtils;
import com.inov8.microbank.common.util.WorkFlowErrorCodeConstants;
import com.inov8.microbank.common.wrapper.switchmodule.SwitchWrapper;
import com.inov8.microbank.common.wrapper.switchmodule.SwitchWrapperImpl;
import com.inov8.microbank.server.service.financialintegrationmodule.FinancialInstitution;
import com.inov8.microbank.server.service.olamodule.OLAManager;
import com.inov8.ola.integration.vo.OLAVO;

public class OLAAccountFormController extends AdvanceFormController {
	 private ReferenceDataManager referenceDataManager;
	 private OLAManager olaManager;
	FinancialInstitution olaVeriflyFinancialInstitution;
		
	  public OLAAccountFormController()
	  {
	    setCommandName("olaVo");
	    setCommandClass(OLAVO.class);
	  }

	
	@Override
	protected Object loadFormBackingObject(HttpServletRequest httpServletRequest)
			throws Exception {
		Long id = ServletRequestUtils.getLongParameter(httpServletRequest, "accountId");
		if (id != null){
			OLAVO olaVo = new OLAVO ();
			SwitchWrapper sWrapper = new SwitchWrapperImpl ();
			olaVo.setAccountId(id);
			sWrapper.setBankId(UserUtils.getCurrentUser().getBankUserIdBankUserModel()
					.getBankId());
			sWrapper.setOlavo(olaVo);
			try
			{
			 sWrapper= olaVeriflyFinancialInstitution.getAccountInfo(sWrapper);
			 return sWrapper.getOlavo();
			}
			
			catch (Exception e) {
			
			httpServletRequest.setAttribute("isSaved", true);
			e.printStackTrace();
			if( e.getMessage().equalsIgnoreCase(WorkFlowErrorCodeConstants.SWITCH_INACTIVE) )
			{
				super.saveMessage(httpServletRequest, WorkFlowErrorCodeConstants.PHOENIX_SERVICE_DOWN_MSG);
				
				//return super.showForm(request, response, errors);				
			}
			
			return new OLAVO();
			}

		
		
			
		}else{
			return new OLAVO();
		}				
	}

	@Override
	protected Map loadReferenceData(HttpServletRequest httpServletRequest) throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ModelAndView onCreate(HttpServletRequest httpServletRequest,
			HttpServletResponse httpServletResponse, Object object, BindException bindException)
			throws Exception {		
		BaseWrapper baseWrapper = new BaseWrapperImpl ();
		baseWrapper.putObject("olaVo", (OLAVO)object);
		
		ModelAndView modelAndView = new ModelAndView(this.getSuccessView());
		try {
			baseWrapper = olaManager.createAccount(baseWrapper,httpServletRequest);		
			OLAVO olaVo = (OLAVO)baseWrapper.getObject("olaVo");
		if ("06".equals(olaVo.getResponseCode())){
			this.saveMessage(httpServletRequest,
	        "Record could not be saved");
		}else{		
		this.saveMessage(httpServletRequest,
        "Account ( "+olaVo.getPayingAccNo() +" ) has been created successfully");
		httpServletRequest.setAttribute("olaAccountNo", olaVo.getPayingAccNo());
		}		
		httpServletRequest.setAttribute("isSaved", true);				
		httpServletRequest.setAttribute("olaVo", olaVo);		
        return modelAndView;
		} catch (Exception e) {
			
			httpServletRequest.setAttribute("olaVo", new OLAVO());
			e.printStackTrace();
			if( e.getMessage().equalsIgnoreCase(WorkFlowErrorCodeConstants.PHOENIX_SERVICE_DOWN_MSG) )
			{
				super.saveMessage(httpServletRequest, WorkFlowErrorCodeConstants.PHOENIX_SERVICE_DOWN_MSG);
				
				//return super.showForm(request, response, errors);				
			}
			else
			{

			
			
			this.saveMessage(httpServletRequest,
	        "Record could not be saved");
			}
			
		}
		return modelAndView;		
	}

	@Override
	public ModelAndView onUpdate(HttpServletRequest httpServletRequest,
			HttpServletResponse httpServletResponse, Object object, BindException bindException)
			throws Exception {
		ModelAndView modelAndView = new ModelAndView(this.getSuccessView());
		BaseWrapper baseWrapper = new BaseWrapperImpl ();
		OLAVO olavo= (OLAVO)object;
		baseWrapper.putObject("olaVo", olavo);
		try {
		baseWrapper = olaManager.updateAccount(baseWrapper,httpServletRequest);
		OLAVO olaVo = (OLAVO)baseWrapper.getObject("olaVo");
		if ("06".equals(olaVo.getResponseCode())){
			this.saveMessage(httpServletRequest,
	        "Record could not be saved");
		}else{
		this.saveMessage(httpServletRequest,
        "Record saved successfully");
		}						
		httpServletRequest.setAttribute("olaVo",  new OLAVO());
		httpServletRequest.setAttribute("isSaved", true);
        return modelAndView;
		} catch (Exception e) {
			httpServletRequest.setAttribute("olaVo",  new OLAVO());
			e.printStackTrace();
			if( e.getMessage().equalsIgnoreCase(WorkFlowErrorCodeConstants.PHOENIX_SERVICE_DOWN_MSG) )
			{
				super.saveMessage(httpServletRequest, WorkFlowErrorCodeConstants.PHOENIX_SERVICE_DOWN_MSG);
				
				//return super.showForm(request, response, errors);				
			}
			else
			{
			
			this.saveMessage(httpServletRequest,
	        "Record could not be saved");
			}
			
		}
		return modelAndView;
	}


	public FinancialInstitution getOlaVeriflyFinancialInstitution() {
		return olaVeriflyFinancialInstitution;
	}


	public void setOlaVeriflyFinancialInstitution(
			FinancialInstitution olaVeriflyFinancialInstitution) {
		this.olaVeriflyFinancialInstitution = olaVeriflyFinancialInstitution;
	}


	public OLAManager getOlaManager() {
		return olaManager;
	}


	public void setOlaManager(OLAManager olaManager) {
		this.olaManager = olaManager;
	}


	public ReferenceDataManager getReferenceDataManager() {
		return referenceDataManager;
	}


	public void setReferenceDataManager(ReferenceDataManager referenceDataManager) {
		this.referenceDataManager = referenceDataManager;
	}

}
