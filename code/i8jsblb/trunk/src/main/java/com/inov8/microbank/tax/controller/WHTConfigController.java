package com.inov8.microbank.tax.controller;

import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.codehaus.jackson.map.ObjectMapper;
import org.springframework.validation.BindException;
import org.springframework.web.bind.ServletRequestBindingException;
import org.springframework.web.bind.ServletRequestUtils;
import org.springframework.web.servlet.ModelAndView;

import com.inov8.framework.common.exception.FrameworkCheckedException;
import com.inov8.framework.common.wrapper.BaseWrapper;
import com.inov8.framework.common.wrapper.BaseWrapperImpl;
import com.inov8.framework.webapp.action.AdvanceFormController;
import com.inov8.microbank.common.util.ActionAuthorizationConstantsInterface;
import com.inov8.microbank.common.util.MessageUtil;
import com.inov8.microbank.common.util.PortalConstants;
import com.inov8.microbank.common.util.PortalDateUtils;
import com.inov8.microbank.server.facade.portal.authorizationmodule.ActionAuthorizationFacade;
import com.inov8.microbank.server.facade.taxregimemodule.TaxRegimeFacade;
import com.inov8.microbank.tax.vo.WHTConfigWrapper;

/**
 * Created by Malik on 7/1/2016.
 */
public class WHTConfigController extends AdvanceFormController
{
    private TaxRegimeFacade taxRegimeFacade;
    private ActionAuthorizationFacade actionAuthorizationFacade;

    public WHTConfigController(){
        setCommandName("whtConfigWrapper");
        setCommandClass(WHTConfigWrapper.class);
    }

    @Override
    protected Map loadReferenceData(HttpServletRequest httpServletRequest) throws Exception
    {
        return null;
    }

    @Override
    protected Object loadFormBackingObject(HttpServletRequest httpServletRequest) throws Exception
    {
        WHTConfigWrapper whtConfigWrapper=new WHTConfigWrapper();
        
		boolean isReSubmit = ServletRequestUtils.getBooleanParameter(httpServletRequest, "isReSubmit",false);
		boolean isReadOnly = ServletRequestUtils.getBooleanParameter(httpServletRequest, "isReadOnly",false);
        
      /// Added for Resubmit Authorization Request 
      		if(isReSubmit || isReadOnly){
      			ObjectMapper mapper = new ObjectMapper();
      			String modelJsonString = actionAuthorizationFacade.loadAuthorizationVOJson(httpServletRequest);		
      			whtConfigWrapper = mapper.readValue(modelJsonString,WHTConfigWrapper.class);

      		}
      		else
      		{
      			 whtConfigWrapper=taxRegimeFacade.loadAllActiveWHTConfigVo();
      		}
      		///End Added for Resubmit Authorization Request
        
        return whtConfigWrapper;
    }

    @Override
    protected ModelAndView onCreate(HttpServletRequest httpServletRequest, HttpServletResponse res, Object o, BindException e) throws Exception
    {
        return null;
    }

    @Override
    protected ModelAndView onUpdate(HttpServletRequest req, HttpServletResponse res, Object obj, BindException e) throws Exception
    {
        BaseWrapper baseWrapper=new BaseWrapperImpl();
        WHTConfigWrapper whtConfigWrapper = (WHTConfigWrapper) obj;
        try
        {
            baseWrapper.setBasePersistableModel(whtConfigWrapper);
            populateAuthenticationParams(baseWrapper,req,whtConfigWrapper);
            taxRegimeFacade.updateWHTConfigModelWithAuthorization(baseWrapper);

        }
        catch(Exception errors)
        {
            String msg=errors.getMessage();
            this.saveErrorMessage(req,msg);
            return super.showForm(req,res,e);
        }

        String msg = (String) baseWrapper.getObject(ActionAuthorizationConstantsInterface.KEY_AUTHORIZATION_MSG);
        if (null == msg) {
            msg = super.getText("whtConfig.update", req.getLocale());
        }
        this.saveMessage(req, msg);

        return new ModelAndView(getSuccessView());
    }


    @Override
    protected void populateAuthenticationParams(BaseWrapper baseWrapper, HttpServletRequest req, Object model) throws FrameworkCheckedException
    {

        WHTConfigWrapper whtConfigWrapper= (WHTConfigWrapper) model;
        ObjectMapper mapper = new ObjectMapper();
        Long actionAuthorizationId =null;
        try
        {
            actionAuthorizationId = ServletRequestUtils.getLongParameter(req,"actionAuthorizationId");
        } catch (ServletRequestBindingException e1) {
         //   e1.printStackTrace();
        }

        //Setting date format for Date Fields when parsed as String. Jakson parse only standard string value to date object
        DateFormat df = new SimpleDateFormat(PortalDateUtils.SHORT_DATE_FORMAT_2);
        mapper.setDateFormat(df);

        String modelJsonString =null;
        String initialModelJsonString =null;
        try
        {
            modelJsonString = mapper.writeValueAsString(whtConfigWrapper);
        } catch (IOException e)
        {
            e.printStackTrace();
            throw new FrameworkCheckedException(MessageUtil.getMessage(ActionAuthorizationConstantsInterface.REF_DATA_EXCEPTION_MSG));
        }
        WHTConfigWrapper initialModel = null;
        try {
            initialModel = (WHTConfigWrapper) this.loadFormBackingObject(req);
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
        baseWrapper.putObject(ActionAuthorizationConstantsInterface.KEY_METHODE_NAME,MessageUtil.getMessage("WHTConfigWrapperVo.updateMethodeName"));

        baseWrapper.putObject(ActionAuthorizationConstantsInterface.KEY_OLD_MODEL_STRING, initialModelJsonString);
        baseWrapper.putObject(ActionAuthorizationConstantsInterface.KEY_MODEL_CLASS,WHTConfigWrapper.class.getSimpleName());
        baseWrapper.putObject(ActionAuthorizationConstantsInterface.KEY_MODEL_CLASS_QUALIFIED_NAME,WHTConfigWrapper.class.getName());
        baseWrapper.putObject(ActionAuthorizationConstantsInterface.KEY_MODEL_STRING, modelJsonString);
        baseWrapper.putObject(ActionAuthorizationConstantsInterface.KEY_REQ_REF_ID,"1");

        baseWrapper.putObject(PortalConstants.KEY_ACTION_AUTH_ID,actionAuthorizationId);
        baseWrapper.putObject(PortalConstants.KEY_ACTION_ID, PortalConstants.ACTION_UPDATE);
        baseWrapper.putObject(PortalConstants.KEY_USECASE_ID, PortalConstants.WHT_CONFIG_USECASE_ID);
        baseWrapper.putObject(ActionAuthorizationConstantsInterface.KEY_MANAGER_BEAN_NAME,MessageUtil.getMessage("WHTConfigWrapperVo.Manager"));
        
        String[] ar = this.getFormView().split("/");
        String formview = ar[ar.length-1];
        
        baseWrapper.putObject(ActionAuthorizationConstantsInterface.KEY_FORM_NAME,formview);
    }

    public void setTaxRegimeFacade(TaxRegimeFacade taxRegimeFacade)
    {
        this.taxRegimeFacade = taxRegimeFacade;
    }

	public void setActionAuthorizationFacade(
			ActionAuthorizationFacade actionAuthorizationFacade) {
		this.actionAuthorizationFacade = actionAuthorizationFacade;
	}

}
