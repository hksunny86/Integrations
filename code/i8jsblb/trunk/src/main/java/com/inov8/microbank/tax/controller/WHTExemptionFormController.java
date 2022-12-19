/**
 * Created by Zeeshan Naeem on 6/27/2016.
 */
package com.inov8.microbank.tax.controller;


import java.io.IOException;
import java.sql.Date;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.collections.map.HashedMap;
import org.codehaus.jackson.map.ObjectMapper;
import org.springframework.validation.BindException;
import org.springframework.web.bind.ServletRequestBindingException;
import org.springframework.web.bind.ServletRequestDataBinder;
import org.springframework.web.bind.ServletRequestUtils;
import org.springframework.web.servlet.ModelAndView;

import com.inov8.framework.common.exception.FrameworkCheckedException;
import com.inov8.framework.common.wrapper.BaseWrapper;
import com.inov8.framework.common.wrapper.BaseWrapperImpl;
import com.inov8.framework.webapp.action.AdvanceFormController;
import com.inov8.microbank.common.util.ActionAuthorizationConstantsInterface;
import com.inov8.microbank.common.util.CommonUtils;
import com.inov8.microbank.common.util.MessageUtil;
import com.inov8.microbank.common.util.PortalConstants;
import com.inov8.microbank.common.util.PortalDateUtils;
import com.inov8.microbank.server.facade.portal.authorizationmodule.ActionAuthorizationFacade;
import com.inov8.microbank.server.facade.taxregimemodule.TaxRegimeFacade;
import com.inov8.microbank.server.service.mfsmodule.CommonCommandManager;
import com.inov8.microbank.tax.vo.WHTConfigWrapper;
import com.inov8.microbank.tax.vo.WHTExemptionVO;


public class WHTExemptionFormController extends AdvanceFormController {

        private TaxRegimeFacade taxRegimeFacade;
        private CommonCommandManager commonCommandManager;
        private ActionAuthorizationFacade actionAuthorizationFacade;


        public WHTExemptionFormController() {
            setCommandName("wHTExemptionVO");
            setCommandClass(WHTExemptionVO.class);
        }
        
    	@Override
    	public void initBinder(HttpServletRequest request, ServletRequestDataBinder binder)
    	{
    		super.initBinder(request, binder);
    		CommonUtils.bindCustomDateEditor(binder);
    	}


        @Override
        protected Object loadFormBackingObject(HttpServletRequest req) throws Exception {
            req.setAttribute("saveAllow" , Boolean.FALSE);
            
            WHTExemptionVO whtExemptionVO = new WHTExemptionVO();
            
    		boolean isReSubmit = ServletRequestUtils.getBooleanParameter(req, "isReSubmit",false);
    		boolean isReadOnly = ServletRequestUtils.getBooleanParameter(req, "isReadOnly",false);
            
          /// Added for Resubmit Authorization Request 
          		if(isReSubmit || isReadOnly){
          			ObjectMapper mapper = new ObjectMapper();
          			String modelJsonString = actionAuthorizationFacade.loadAuthorizationVOJson(req);		
          			whtExemptionVO = mapper.readValue(modelJsonString,WHTExemptionVO.class);
          			return whtExemptionVO;

          		}
          		return whtExemptionVO;
        }

        @SuppressWarnings({ "rawtypes", "unchecked" })
        @Override
        protected Map loadReferenceData(HttpServletRequest req) throws Exception {
            req.setAttribute("saveAllow" , Boolean.FALSE);
         return new HashedMap();
        }

        @Override
        protected ModelAndView onCreate(HttpServletRequest req, HttpServletResponse res, Object obj, BindException errors) throws Exception {

            WHTExemptionVO whtExemptionVO = (WHTExemptionVO) obj;
            
            Calendar calendar=Calendar.getInstance();
            calendar.setTime(whtExemptionVO.getEndDate());
            calendar.set(Calendar.HOUR_OF_DAY, 23);
            calendar.set(Calendar.MINUTE, 59);
            calendar.set(Calendar.SECOND, 59);
            java.util.Date date=calendar.getTime();
            
            whtExemptionVO.setEndDate(date);
            BaseWrapper baseWrapper= new BaseWrapperImpl();
            baseWrapper.setBasePersistableModel(whtExemptionVO);

            try {

                baseWrapper = taxRegimeFacade.loadAndValidateWhtExemption(baseWrapper);
                baseWrapper.setBasePersistableModel(whtExemptionVO);

                if(whtExemptionVO.getWhtExemptionId()!=null){
                	baseWrapper.putObject(PortalConstants.KEY_ACTION_ID, PortalConstants.ACTION_UPDATE);
                }else{
                	baseWrapper.putObject(PortalConstants.KEY_ACTION_ID, PortalConstants.ACTION_CREATE);
                }
                populateAuthenticationParams(baseWrapper, req, whtExemptionVO);
                taxRegimeFacade.saveUpdateWhtExemptionModelsWithAuthorization(baseWrapper);
            } catch(Exception exception) {
                String msg=exception.getMessage();
                this.saveMessage(req,msg);
                return new ModelAndView(getSuccessView());
            }


            String msg = (String) baseWrapper.getObject(ActionAuthorizationConstantsInterface.KEY_AUTHORIZATION_MSG);
            if (null == msg) {
                msg = super.getText("whtExemption.saved", req.getLocale());
            }
            this.saveMessage(req, msg);
            return new ModelAndView(getSuccessView());
        }


        @Override
        protected ModelAndView onUpdate(HttpServletRequest req,
                                        HttpServletResponse res, Object obj, BindException errors)
                throws Exception {
	
            return null;
        }

    @Override
    protected void populateAuthenticationParams(BaseWrapper baseWrapper, HttpServletRequest req,Object model) throws FrameworkCheckedException
    {

        WHTExemptionVO whtExemptionVO= (WHTExemptionVO) model;
        ObjectMapper mapper = new ObjectMapper();
        Long actionAuthorizationId =null;
        try
        {
            actionAuthorizationId = ServletRequestUtils.getLongParameter(req,"actionAuthorizationId");
        } catch (ServletRequestBindingException e1) {
            //e1.printStackTrace();
        }

        //Setting date format for Date Fields when parsed as String. Jakson parse only standard string value to date object
        DateFormat df = new SimpleDateFormat(PortalDateUtils.FULL_DATE_TIME_FORMAT_JASONSTD);
        mapper.setDateFormat(df);

        String modelJsonString =null;
        String initialModelJsonString =null;
        try
        {
            modelJsonString = mapper.writeValueAsString(whtExemptionVO);
        } catch (IOException e)
        {
            e.printStackTrace();
            throw new FrameworkCheckedException(MessageUtil.getMessage(ActionAuthorizationConstantsInterface.REF_DATA_EXCEPTION_MSG));
        }

        WHTExemptionVO initialModel = null;
        try {
            initialModel = (WHTExemptionVO) this.loadFormBackingObject(req);
        } catch(Exception e) {
            e.printStackTrace();
        }

        try
        {
            initialModelJsonString = mapper.writeValueAsString(initialModel);

        } catch (IOException e) {
            e.printStackTrace();
            throw new FrameworkCheckedException(MessageUtil.getMessage(ActionAuthorizationConstantsInterface.REF_DATA_EXCEPTION_MSG));
        }
        baseWrapper.putObject(ActionAuthorizationConstantsInterface.KEY_METHODE_NAME,MessageUtil.getMessage("WHTExemptionVO.methodeName"));

        baseWrapper.putObject(ActionAuthorizationConstantsInterface.KEY_OLD_MODEL_STRING, initialModelJsonString);
        baseWrapper.putObject(ActionAuthorizationConstantsInterface.KEY_MODEL_CLASS,WHTExemptionVO.class.getSimpleName());
        baseWrapper.putObject(ActionAuthorizationConstantsInterface.KEY_MODEL_CLASS_QUALIFIED_NAME,WHTExemptionVO.class.getName());
        baseWrapper.putObject(ActionAuthorizationConstantsInterface.KEY_MODEL_STRING, modelJsonString);
        baseWrapper.putObject(ActionAuthorizationConstantsInterface.KEY_REQ_REF_ID,whtExemptionVO.getUserId());

        baseWrapper.putObject(PortalConstants.KEY_ACTION_AUTH_ID,actionAuthorizationId);
       // baseWrapper.putObject(PortalConstants.KEY_ACTION_ID, PortalConstants.ACTION_UPDATE);
        baseWrapper.putObject(PortalConstants.KEY_USECASE_ID, PortalConstants.WHT_EXEMPTION_USECASE_ID);
        baseWrapper.putObject(ActionAuthorizationConstantsInterface.KEY_MANAGER_BEAN_NAME,MessageUtil.getMessage("WHTExemptionVO.Manager"));
        
        String[] ar = this.getFormView().split("/");
        String formview = ar[ar.length-1];
        
        baseWrapper.putObject(ActionAuthorizationConstantsInterface.KEY_FORM_NAME,formview);
    }

        public void setTaxRegimeFacade(TaxRegimeFacade taxRegimeFacade) {
            this.taxRegimeFacade = taxRegimeFacade;
        }


        public void setCommonCommandManager(CommonCommandManager commonCommandManager) {
            this.commonCommandManager = commonCommandManager;
        }


		public void setActionAuthorizationFacade(
				ActionAuthorizationFacade actionAuthorizationFacade) {
			this.actionAuthorizationFacade = actionAuthorizationFacade;
		}
    }




