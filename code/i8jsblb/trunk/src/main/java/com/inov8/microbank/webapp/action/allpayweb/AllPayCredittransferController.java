package com.inov8.microbank.webapp.action.allpayweb;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.validation.BindException;
import org.springframework.web.bind.ServletRequestUtils;
import org.springframework.web.servlet.ModelAndView;

import com.inov8.framework.webapp.action.AdvanceFormController;
import com.inov8.microbank.common.model.AppUserModel;
import com.inov8.microbank.common.util.CommandFieldConstants;
import com.inov8.microbank.common.util.ThreadLocalAppUser;
import com.inov8.microbank.common.util.UserTypeConstantsInterface;
import com.inov8.microbank.common.util.ValidationErrors;
import com.inov8.microbank.common.util.ValidatorWrapper;
import com.inov8.microbank.mfsweb.MfsWebManager;
import com.inov8.microbank.mfsweb.MfsWebResponseDataPopulator;

public class AllPayCredittransferController extends AdvanceFormController{
	private MfsWebManager mfsWebController;
	private MfsWebResponseDataPopulator mfsWebResponseDataPopulator; 
	
	public AllPayCredittransferController (){
		setCommandName("object");
	    setCommandClass(Object.class);
	}
	@Override
	protected Object loadFormBackingObject(HttpServletRequest arg0) throws Exception {
//		System.out.println("hello");
		return new Object();
	}

	@Override
	protected Map loadReferenceData(HttpServletRequest arg0) throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	protected ModelAndView onCreate(HttpServletRequest request, HttpServletResponse response, Object model, BindException exception) throws Exception 
	{
		ValidationErrors vErrors = new ValidationErrors();
		String amount = ServletRequestUtils.getStringParameter(request, CommandFieldConstants.KEY_TX_AMOUNT);
		String allPayId = ServletRequestUtils.getStringParameter(request, CommandFieldConstants.KEY_ALLPAY_ID);
		request.setAttribute("amount", amount);
		request.setAttribute("allPayId", allPayId);
		vErrors = ValidatorWrapper.doRequired(amount, vErrors, "amount");
		AppUserModel appUserModel = ThreadLocalAppUser.getAppUserModel();
		String fieldName = "";
		if(null != appUserModel && appUserModel.getAppUserTypeId().longValue() == UserTypeConstantsInterface.DISTRIBUTOR.longValue())
		{
			fieldName = "Distributor ID";
		}
		else
		{
			fieldName = "Retailer ID";
		}
		
		if(!vErrors.hasValidationErrors())
		{
			vErrors = ValidatorWrapper.doInteger(amount, vErrors, "amount");
			if(!vErrors.hasValidationErrors())
			{
				vErrors = ValidatorWrapper.doRange("1", "999999", amount, "amount", vErrors);
				
			if(!vErrors.hasValidationErrors())
			{
				
				vErrors = ValidatorWrapper.doRequired(allPayId, vErrors, fieldName);
				if(!vErrors.hasValidationErrors())
				{
					vErrors = ValidatorWrapper.doNumeric(allPayId, vErrors, fieldName);
					if(!vErrors.hasValidationErrors())
					{
						vErrors = ValidatorWrapper.checkLength(allPayId,12, vErrors, fieldName);
						if(!vErrors.hasValidationErrors())
						{
							return new ModelAndView(this.getSuccessView());
						}
						else
						{
							request.setAttribute("errors", vErrors.getErrors());
							return new ModelAndView (this.getFormView()) ;

						}
					}
					else
					{
						request.setAttribute("errors", fieldName+" is not numeric.");
						return new ModelAndView (this.getFormView()) ;
					}
				}
				else
				{
					request.setAttribute("errors", vErrors.getErrors());
					return new ModelAndView (this.getFormView()) ;
				}
			}
			else
			{
				request.setAttribute("errors", "Amount is invalid. Please enter a valid amount between 1 to 999999.");
				return new ModelAndView (this.getFormView()) ;
			}
		}
			else
			{
				request.setAttribute("errors", "Amount is invalid. Please enter a valid amount between 1 to 999999.");
				return new ModelAndView (this.getFormView()) ;
			}
		}
		else
		{
			request.setAttribute("errors", vErrors.getErrors());
			return new ModelAndView (this.getFormView()) ;
		}
		
	}	
	@Override
	protected ModelAndView onUpdate(HttpServletRequest request, HttpServletResponse response, Object model, BindException exception) throws Exception {
		return onCreate(request, response, model, exception);
	}

	public MfsWebManager getMfsWebController() {
		return mfsWebController;
	}

	public void setMfsWebController(MfsWebManager mfsWebController) {
		this.mfsWebController = mfsWebController;
	}
	public MfsWebResponseDataPopulator getMfsWebResponseDataPopulator() {
		return mfsWebResponseDataPopulator;
	}
	public void setMfsWebResponseDataPopulator(
			MfsWebResponseDataPopulator mfsWebResponseDataPopulator) {
		this.mfsWebResponseDataPopulator = mfsWebResponseDataPopulator;
	}

	

}
