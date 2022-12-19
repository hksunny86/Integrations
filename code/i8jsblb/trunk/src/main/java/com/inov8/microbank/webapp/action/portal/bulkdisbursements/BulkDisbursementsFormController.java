package com.inov8.microbank.webapp.action.portal.bulkdisbursements;

import java.util.Date;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.validator.GenericValidator;
import org.springframework.validation.BindException;
import org.springframework.web.bind.ServletRequestDataBinder;
import org.springframework.web.bind.ServletRequestUtils;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.RedirectView;

import com.inov8.framework.common.exception.FrameworkCheckedException;
import com.inov8.framework.common.wrapper.BaseWrapper;
import com.inov8.framework.common.wrapper.BaseWrapperImpl;
import com.inov8.framework.common.wrapper.SearchBaseWrapper;
import com.inov8.framework.common.wrapper.SearchBaseWrapperImpl;
import com.inov8.framework.server.service.common.ReferenceDataManager;
import com.inov8.framework.webapp.action.AdvanceFormController;
import com.inov8.microbank.common.model.BulkDisbursementsModel;
import com.inov8.microbank.common.util.CommonUtils;
import com.inov8.microbank.common.util.EncryptionUtil;
import com.inov8.microbank.common.util.MessageUtil;
import com.inov8.microbank.common.util.PortalConstants;
import com.inov8.microbank.common.util.UserUtils;
import com.inov8.microbank.disbursement.service.BulkDisbursementsManager;

public class BulkDisbursementsFormController extends AdvanceFormController{
    
	private BulkDisbursementsManager bulkDisbursementsManager;
    private ReferenceDataManager referenceDataManager;
    
	public BulkDisbursementsFormController() {
		super.setCommandName("bulkDisbursementsModel");
		super.setCommandClass(BulkDisbursementsModel.class);
	}
	
	@Override
	protected Map<String, Object> loadReferenceData(HttpServletRequest req) throws Exception {
	    return null;
	}

	@Override
	protected Object loadFormBackingObject(HttpServletRequest req) throws Exception {
		String bulkDisbursementsId = ServletRequestUtils.getStringParameter(req, "bulkDisbursementsId");
		bulkDisbursementsId = EncryptionUtil.decryptWithDES(bulkDisbursementsId);
		BulkDisbursementsModel model = new BulkDisbursementsModel();
		if ( !GenericValidator.isBlankOrNull(bulkDisbursementsId) ) {
			SearchBaseWrapper wrapper = new SearchBaseWrapperImpl();
			model.setBulkDisbursementsId(new Long(bulkDisbursementsId));
			wrapper.setBasePersistableModel(model);
			wrapper = this.bulkDisbursementsManager.loadBulkDisbursement(wrapper);
			model = (BulkDisbursementsModel) wrapper.getBasePersistableModel();
		}
		return model;
	}

	@Override
	protected ModelAndView onCreate(HttpServletRequest req, HttpServletResponse res, Object model, BindException errors) throws Exception {
		ModelAndView modelAndView = new ModelAndView(new RedirectView("home.html"));
		return modelAndView;
	}

	@Override
	protected ModelAndView onUpdate(HttpServletRequest req, HttpServletResponse res, Object obj, BindException errors) throws Exception {
		BaseWrapper baseWrapper = new BaseWrapperImpl();
		Date nowDate = new Date();
		BulkDisbursementsModel bulkDisbursementsModel = null;
		BulkDisbursementsModel model = null;
		try{
			Long bulkDisbursementsId = new Long(EncryptionUtil.decryptWithDES(ServletRequestUtils.getStringParameter(req, "bulkDisbursementsId")));			
			model = new BulkDisbursementsModel();
			bulkDisbursementsModel = new BulkDisbursementsModel();
			
			SearchBaseWrapper wrapper = new SearchBaseWrapperImpl();
			bulkDisbursementsModel.setBulkDisbursementsId(bulkDisbursementsId);
			wrapper.setBasePersistableModel(bulkDisbursementsModel);
			wrapper = this.bulkDisbursementsManager.loadBulkDisbursement(wrapper);
			bulkDisbursementsModel = (BulkDisbursementsModel) wrapper.getBasePersistableModel();
			
			model = (BulkDisbursementsModel) obj;
			bulkDisbursementsModel.setBulkDisbursementsId(bulkDisbursementsId);
			if(bulkDisbursementsModel.getPosted()==false){
				bulkDisbursementsModel.setPaymentDate(model.getPaymentDate());				
				bulkDisbursementsModel.setDescription(model.getDescription());
			}
			bulkDisbursementsModel.setLimitApplicable(model.getLimitApplicable());
			bulkDisbursementsModel.setPayCashViaCnic(model.getPayCashViaCnic());
			bulkDisbursementsModel.setUpdatedByAppUserModel(UserUtils.getCurrentUser());
			bulkDisbursementsModel.setUpdatedOn(nowDate);
			baseWrapper.setBasePersistableModel(bulkDisbursementsModel);
			baseWrapper.putObject(PortalConstants.KEY_ACTION_ID, PortalConstants.ACTION_UPDATE);
			baseWrapper.putObject(PortalConstants.KEY_USECASE_ID, new Long(PortalConstants.UPDATE_BULK_DISBURSEMENT_USECASE_ID));
//		    baseWrapper = this.bulkDisbursementsManager.updateBulkDisbursement(baseWrapper);
		    bulkDisbursementsModel = (BulkDisbursementsModel)baseWrapper.getBasePersistableModel();
		   
		    String msg = super.getText("bulkdisbersement.update.success", req.getLocale());
			this.saveMessage(req, msg);
		}catch(FrameworkCheckedException exception){
			exception.printStackTrace();
			super.saveMessage(req, super.getText("bulkdisbersement.update.failure", req.getLocale()));
		} 
		catch (Exception fce)
		{	
			fce.printStackTrace();
			this.saveMessage(req, MessageUtil.getMessage("6075"));
		}
		
		ModelAndView modelAndView = new ModelAndView(new RedirectView("p_bulkdisbursementsearch.html?actionId=2"));
		return modelAndView;
	}
	
	@Override
	public void initBinder(HttpServletRequest request, ServletRequestDataBinder binder)
	{
		super.initBinder(request, binder);
		CommonUtils.bindCustomDateEditor(binder);
	}

	public ReferenceDataManager getReferenceDataManager() {
		return referenceDataManager;
	}

	public void setReferenceDataManager(ReferenceDataManager referenceDataManager) {
		this.referenceDataManager = referenceDataManager;
	}

	public BulkDisbursementsManager getBulkDisbursementsManager() {
		return bulkDisbursementsManager;
	}

	public void setBulkDisbursementsManager(BulkDisbursementsManager bulkDisbursementsManager) {
		this.bulkDisbursementsManager = bulkDisbursementsManager;
	}

}