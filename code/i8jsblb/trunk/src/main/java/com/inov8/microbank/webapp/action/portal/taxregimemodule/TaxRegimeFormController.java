package com.inov8.microbank.webapp.action.portal.taxregimemodule;

import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.codehaus.jackson.map.ObjectMapper;
import org.springframework.validation.BindException;
import org.springframework.web.bind.ServletRequestBindingException;
import org.springframework.web.bind.ServletRequestDataBinder;
import org.springframework.web.bind.ServletRequestUtils;
import org.springframework.web.servlet.ModelAndView;

import com.inov8.framework.common.exception.FrameworkCheckedException;
import com.inov8.framework.common.wrapper.BaseWrapper;
import com.inov8.framework.common.wrapper.BaseWrapperImpl;
import com.inov8.framework.server.service.common.ReferenceDataManager;
import com.inov8.framework.webapp.action.AdvanceFormController;
import com.inov8.microbank.common.model.TaxRegimeModel;
import com.inov8.microbank.common.model.portal.authorizationreferencedata.TaxRegimeModelVO;
import com.inov8.microbank.common.model.portal.usermanagementmodule.UserManagementModel;
import com.inov8.microbank.common.util.ActionAuthorizationConstantsInterface;
import com.inov8.microbank.common.util.CommonUtils;
import com.inov8.microbank.common.util.MessageUtil;
import com.inov8.microbank.common.util.PortalConstants;
import com.inov8.microbank.common.util.PortalDateUtils;
import com.inov8.microbank.common.util.UserUtils;
import com.inov8.microbank.server.facade.portal.authorizationmodule.ActionAuthorizationFacade;
import com.inov8.microbank.server.facade.taxregimemodule.TaxRegimeFacade;


/**
 * @author Abu Turab
 * 
 */
public class TaxRegimeFormController extends AdvanceFormController {
	private static final Logger LOGGER = Logger
			.getLogger(TaxRegimeFormController.class);
	private ReferenceDataManager referenceDataManager;
	private TaxRegimeFacade taxRegimeFacade;

	private ActionAuthorizationFacade actionAuthorizationFacade;

	public TaxRegimeFormController() {
		setCommandName("taxRegimeModel");
		setCommandClass(TaxRegimeModel.class);
	}

	@Override
	public void initBinder(HttpServletRequest request,
			ServletRequestDataBinder binder) {
		super.initBinder(request, binder);
		CommonUtils.bindCustomDateEditor(binder);
	}

	@SuppressWarnings("unchecked")
	@Override
	protected Object loadFormBackingObject(HttpServletRequest req)
			throws Exception {

		String enId = ServletRequestUtils.getStringParameter(req, "taxRegimeId");
		boolean isReSubmit = ServletRequestUtils.getBooleanParameter(req, "isReSubmit",false);
		
		
		/// Added for Resubmit Authorization Request 
		if(isReSubmit){
			ObjectMapper mapper = new ObjectMapper();
			String modelJsonString = actionAuthorizationFacade.loadAuthorizationVOJson(req);		
			TaxRegimeModelVO taxRegimeModelVO = mapper.readValue(modelJsonString,TaxRegimeModelVO.class);
			return getTaxRegimeModel(taxRegimeModelVO);
		}
		///End Added for Resubmit Authorization Request
		
		
		
		if (null != enId && enId.trim().length() > 0) {
			TaxRegimeModel taxRegimeModel = this.taxRegimeFacade.searchTaxRegimeById(Long.parseLong(enId));
			return taxRegimeModel;
		} else {
			return new TaxRegimeModel();
		}

	}

	@SuppressWarnings("unchecked")
	@Override
	protected Map loadReferenceData(HttpServletRequest req) throws Exception {
		Map referenceDataMap = new HashMap();
		return referenceDataMap;
	}

	@Override
	protected ModelAndView onCreate(HttpServletRequest req,
			HttpServletResponse res, Object model, BindException errors)
			throws Exception {

		BaseWrapper baseWrapper = new BaseWrapperImpl();
		TaxRegimeModel taxRegimeModel = (TaxRegimeModel) model;
		taxRegimeModel.setCreatedBy(UserUtils.getCurrentUser().getAppUserId());
		taxRegimeModel.setCreatedOn(new Date());
		taxRegimeModel.setUpdatedBy(UserUtils.getCurrentUser().getAppUserId());
		taxRegimeModel.setUpdatedOn(new Date());
		
		baseWrapper.putObject(PortalConstants.KEY_ACTION_ID, PortalConstants.ACTION_CREATE);
		baseWrapper.putObject(PortalConstants.KEY_USECASE_ID, new Long( PortalConstants.KEY_CREATE_UPDATE_TAX_REGIME_USECASE_ID ) );
		TaxRegimeModelVO taxRegimeModelVO = new TaxRegimeModelVO(taxRegimeModel);	
		baseWrapper.setBasePersistableModel(taxRegimeModelVO);

		try {
			validateTaxRegime(taxRegimeModel.getName(), null);
			populateAuthenticationParams(baseWrapper,req,taxRegimeModelVO);
			
			baseWrapper = this.taxRegimeFacade.createTaxRegimeFEDRatesWithAuthorization(baseWrapper);
		} catch (FrameworkCheckedException fce) {

			this.saveMessage(req, fce.getMessage());

			return super.showForm(req, res, errors);
		} catch (Exception fce) {

			this.saveMessage(req, MessageUtil.getMessage("6075"));
			return super.showForm(req, res, errors);
		}

		String msg = (String) baseWrapper
				.getObject(ActionAuthorizationConstantsInterface.KEY_AUTHORIZATION_MSG);
		if (null == msg) {
			msg = "Tax Regime Default FED Rate created";
		}

		this.saveMessage(req, msg);

		ModelAndView modelAndView = new ModelAndView(this.getSuccessView());
		return modelAndView;
	}

	@Override
	protected ModelAndView onUpdate(HttpServletRequest req,
			HttpServletResponse res, Object model, BindException errors)
			throws Exception {
		BaseWrapper baseWrapper = new BaseWrapperImpl();
		TaxRegimeModel taxRegimeModel = (TaxRegimeModel) model;
		taxRegimeModel.setUpdatedBy(UserUtils.getCurrentUser().getAppUserId());
		taxRegimeModel.setUpdatedOn(new Date());
		String taxRegimeId = ServletRequestUtils.getStringParameter(req,
				"taxRegimeId");

		if (taxRegimeId != null && taxRegimeId.trim().length() > 0) {
			taxRegimeModel.setTaxRegimeId(Long.valueOf(taxRegimeId));
			if(!taxRegimeModel.getIsActive()){
				int count = taxRegimeFacade.getAssociatedUsersWithTaxRegime(Long.valueOf(taxRegimeId));
				if(count > 0){
					super.saveMessage(req, "You can not deactivate this Tax Regime, there are " + count + " users attached with this Tax Regime");
					taxRegimeModel.setIsActive(Boolean.TRUE);
					return super.showForm(req, res, errors);
				}
			}
			
		}
		
		baseWrapper.putObject(PortalConstants.KEY_ACTION_ID, PortalConstants.ACTION_UPDATE);
		baseWrapper.putObject(PortalConstants.KEY_USECASE_ID, new Long( PortalConstants.KEY_CREATE_UPDATE_TAX_REGIME_USECASE_ID ) );
		TaxRegimeModelVO taxRegimeModelVO = new TaxRegimeModelVO(taxRegimeModel);	
		baseWrapper.setBasePersistableModel(taxRegimeModelVO);

		try {
			validateTaxRegime(taxRegimeModel.getName(), taxRegimeModel.getTaxRegimeId());
			populateAuthenticationParams(baseWrapper,req,taxRegimeModelVO);
			baseWrapper = this.taxRegimeFacade.createTaxRegimeFEDRatesWithAuthorization(baseWrapper); // save
																			// or
																			// update
		} catch (FrameworkCheckedException fce) {

			this.saveMessage(req, fce.getMessage());

			return super.showForm(req, res, errors);
		} catch (Exception fce) {

			this.saveMessage(req, MessageUtil.getMessage("6075"));
			return super.showForm(req, res, errors);
		}

		String msg = (String) baseWrapper
				.getObject(ActionAuthorizationConstantsInterface.KEY_AUTHORIZATION_MSG);
		if (null == msg) {
			msg = "Tax Regime Default FED Rate updated";
		}

		this.saveMessage(req, msg);
		ModelAndView modelAndView = new ModelAndView(this.getSuccessView());
		return modelAndView;
	}
	
	@Override 
	protected void populateAuthenticationParams(BaseWrapper baseWrapper, HttpServletRequest req,Object model) throws FrameworkCheckedException
	    {

			TaxRegimeModelVO taxRegimeModelVO= (TaxRegimeModelVO) model;
		 	Long actionId = (Long) baseWrapper.getObject(PortalConstants.KEY_ACTION_ID);
		 	
		 	
	        ObjectMapper mapper = new ObjectMapper();
	        Long actionAuthorizationId =null;
	  
	        try
	        {
	        	String strActionAuthorizationId = ServletRequestUtils.getStringParameter(req,"actionAuthorizationId");
	            if(null != strActionAuthorizationId && !"".equals(strActionAuthorizationId)){
	            	actionAuthorizationId = Long.parseLong(strActionAuthorizationId);
	            }
	        } catch (ServletRequestBindingException e1) {
	        	System.out.println("New request will be created as actionAuthorizationId donot exists");	
	        }

	        //Setting date format for Date Fields when parsed as String. Jakson parse only standard string value to date object
	        DateFormat df = new SimpleDateFormat(PortalDateUtils.SHORT_DATE_FORMAT_2);
	        mapper.setDateFormat(df);

	        String modelJsonString =null;
	        String initialModelJsonString =null;
	        try
	        {
	            modelJsonString = mapper.writeValueAsString(taxRegimeModelVO);
	            
	            if(actionId.equals(PortalConstants.ACTION_UPDATE)){
	            	TaxRegimeModel initialModel = (TaxRegimeModel) this.loadFormBackingObject(req);
	            	TaxRegimeModelVO  initialModelVO = new TaxRegimeModelVO(initialModel);
	            	 
	            	initialModelJsonString = mapper.writeValueAsString(initialModelVO);
	            }
	        } catch (Exception e)
	        {
	            e.printStackTrace();
	            throw new FrameworkCheckedException(MessageUtil.getMessage(ActionAuthorizationConstantsInterface.REF_DATA_EXCEPTION_MSG));
	        }

	 
	        baseWrapper.putObject(ActionAuthorizationConstantsInterface.KEY_METHODE_NAME,MessageUtil.getMessage("TaxRegimeModelVO.methodeName"));

	        baseWrapper.putObject(ActionAuthorizationConstantsInterface.KEY_OLD_MODEL_STRING, initialModelJsonString);
	        baseWrapper.putObject(ActionAuthorizationConstantsInterface.KEY_MODEL_CLASS,TaxRegimeModelVO.class.getSimpleName());
	        baseWrapper.putObject(ActionAuthorizationConstantsInterface.KEY_MODEL_CLASS_QUALIFIED_NAME,TaxRegimeModelVO.class.getName());
	        baseWrapper.putObject(ActionAuthorizationConstantsInterface.KEY_MODEL_STRING, modelJsonString);
	        baseWrapper.putObject(ActionAuthorizationConstantsInterface.KEY_REQ_REF_ID,taxRegimeModelVO.getName());

	        baseWrapper.putObject(PortalConstants.KEY_ACTION_AUTH_ID,actionAuthorizationId);
//	        baseWrapper.putObject(PortalConstants.KEY_ACTION_ID, PortalConstants.ACTION_UPDATE);
	        baseWrapper.putObject(PortalConstants.KEY_USECASE_ID, PortalConstants.KEY_CREATE_UPDATE_TAX_REGIME_USECASE_ID);
	        baseWrapper.putObject(ActionAuthorizationConstantsInterface.KEY_MANAGER_BEAN_NAME,MessageUtil.getMessage("TaxRegimeModelVO.Manager"));
	        baseWrapper.putObject(ActionAuthorizationConstantsInterface.KEY_FORM_NAME,this.getFormView());
	    }
	
	private TaxRegimeModel getTaxRegimeModel(TaxRegimeModelVO taxRegimeModelVO){
		TaxRegimeModel taxRegimeModel = new TaxRegimeModel();
		taxRegimeModel.setTaxRegimeId(taxRegimeModelVO.getTaxRegimeId());
		taxRegimeModel.setName(taxRegimeModelVO.getName());
		taxRegimeModel.setDescription(taxRegimeModelVO.getDescription());
		taxRegimeModel.setFed(taxRegimeModelVO.getFed());
		taxRegimeModel.setCreatedBy(taxRegimeModelVO.getCreatedByAppUserModelId());
		taxRegimeModel.setCreatedOn(taxRegimeModelVO.getCreatedOn());
		taxRegimeModel.setUpdatedBy(taxRegimeModelVO.getUpdatedByAppUserModelId());
		taxRegimeModel.setUpdatedOn(taxRegimeModelVO.getUpdatedOn());
		taxRegimeModel.setVersionNo(taxRegimeModelVO.getVersionNo());
		taxRegimeModel.setIsActive(taxRegimeModelVO.getIsActive());
		
		return taxRegimeModel;
	}
	
	private void validateTaxRegime(String name, Long taxRegimeId) throws FrameworkCheckedException{
		
			 if(!taxRegimeFacade.isTaxRegimeNameUnique(name, taxRegimeId)){
				 throw new FrameworkCheckedException("FED Default Rates already defined with name \'" + name + "\' in the system.");
			 }	
		
	}

	public void setReferenceDataManager(
			ReferenceDataManager referenceDataManager) {
		this.referenceDataManager = referenceDataManager;
	}

	public void setActionAuthorizationFacade(
			ActionAuthorizationFacade actionAuthorizationFacade) {
		this.actionAuthorizationFacade = actionAuthorizationFacade;
	}

	public void setTaxRegimeFacade(TaxRegimeFacade taxRegimeFacade) {
		this.taxRegimeFacade = taxRegimeFacade;
	}

}
