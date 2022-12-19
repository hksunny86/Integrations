package com.inov8.microbank.webapp.action.portal.escalateinov8;

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
import com.inov8.microbank.common.model.IssueModel;
import com.inov8.microbank.common.model.portal.escalateinov8module.I8EscalateModel;
import com.inov8.microbank.common.util.EncryptionUtil;
import com.inov8.microbank.server.service.portal.escalateinov8module.EscalateInov8Manager;


public class I8EscalateFormController extends AdvanceFormController {

	private EscalateInov8Manager escalateInov8Manager;
	private ReferenceDataManager referenceDataManager;
	
	public I8EscalateFormController(){
		setCommandName("i8EscalateModel");
	    setCommandClass(I8EscalateModel.class);
	}
	
	@Override
	protected Object loadFormBackingObject(HttpServletRequest httpServletRequest)
			throws Exception {
		// TODO Auto-generated method stub
		
		String issueId = EncryptionUtil.decryptWithDES(ServletRequestUtils.getStringParameter(httpServletRequest, "issueId"));
		String transactionCode = ServletRequestUtils.getStringParameter(httpServletRequest, "transactionCode");
		
		BaseWrapper baseWrapper = new BaseWrapperImpl();
		baseWrapper.putObject("issueId", issueId);
		
		baseWrapper = escalateInov8Manager.retrieveI8EscalateForm(baseWrapper);
		I8EscalateModel i8EscalateModel =(I8EscalateModel)baseWrapper.getObject("i8EscalateModel");
		i8EscalateModel.setTransactionCode(transactionCode);

		return i8EscalateModel;
	}

	@Override
	protected Map loadReferenceData(HttpServletRequest httpServletRequest)
			throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	protected ModelAndView onCreate(HttpServletRequest httpServletRequest,
			HttpServletResponse httpServletResponse, Object object,
			BindException bindException) throws Exception {
		// TODO Auto-generated method stub

		I8EscalateModel i8EscalateModel	= (I8EscalateModel)object;
		i8EscalateModel.setIssueId(new Long(EncryptionUtil.decryptWithDES(ServletRequestUtils.getStringParameter(httpServletRequest, "issueId"))));
		String enTransactionId = ServletRequestUtils.getStringParameter(httpServletRequest, "transactionId");
		if(enTransactionId != null && enTransactionId.trim().length() > 0){		
			i8EscalateModel.setTransactionId(new Long(EncryptionUtil.decryptWithDES(enTransactionId)));
		}
		
		BaseWrapper bWrapper = new BaseWrapperImpl();
		
		bWrapper.putObject("i8EscalateModel", i8EscalateModel);
		BaseWrapper baseWrapper = escalateInov8Manager.makeResolveDispute(bWrapper);
		IssueModel issueModel = (IssueModel)baseWrapper.getBasePersistableModel();
	
		String error = (String)baseWrapper.getObject("error");
		
		if (error != null)
		{
			this.saveMessage(httpServletRequest, super.getText("escalate.inov8.escalate.user.not.exist.error",
					httpServletRequest.getLocale()));
		}
		else
		{
			this.saveMessage(httpServletRequest, super.getText("escalate.inov8.escalate.success",
					new Object[] { issueModel.getIssueCode() }, httpServletRequest.getLocale()));
		}

		ModelAndView modelAndView = new ModelAndView(this.getSuccessView());
		    
		return modelAndView;
		
	}

	@Override
	protected ModelAndView onUpdate(HttpServletRequest httpServletRequest,
			HttpServletResponse httpServletResponse, Object object,
			BindException bindException) throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

	/**
	 * @return the escalateInov8Manager
	 */
	public EscalateInov8Manager getEscalateInov8Manager() {
		return escalateInov8Manager;
	}

	/**
	 * @param escalateInov8Manager the escalateInov8Manager to set
	 */
	public void setEscalateInov8Manager(EscalateInov8Manager escalateInov8Manager) {
		this.escalateInov8Manager = escalateInov8Manager;
	}

	/**
	 * @return the referenceDataManager
	 */
	public ReferenceDataManager getReferenceDataManager() {
		return referenceDataManager;
	}

	/**
	 * @param referenceDataManager the referenceDataManager to set
	 */
	public void setReferenceDataManager(ReferenceDataManager referenceDataManager) {
		this.referenceDataManager = referenceDataManager;
	}

}
