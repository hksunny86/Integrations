package com.inov8.microbank.webapp.action.portal.opconcernsmodule;

import java.util.Date;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.validation.BindException;
import org.springframework.web.bind.ServletRequestUtils;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.RedirectView;

import com.inov8.framework.common.exception.ExceptionErrorCodes;
import com.inov8.framework.common.exception.FrameworkCheckedException;
import com.inov8.framework.common.util.CustomList;
import com.inov8.framework.common.wrapper.BaseWrapper;
import com.inov8.framework.common.wrapper.BaseWrapperImpl;
import com.inov8.framework.webapp.action.AdvanceFormController;
import com.inov8.microbank.common.model.ConcernCategoryModel;
import com.inov8.microbank.common.util.MessageUtil;
import com.inov8.microbank.common.util.UserUtils;
import com.inov8.microbank.server.service.portal.concernmodule.ConcernManager;

public class OpConcernCategoryFormController extends AdvanceFormController{
    private ConcernManager concernManager;

	public OpConcernCategoryFormController() {
		setCommandName("concernCategoryModel");
	    setCommandClass(ConcernCategoryModel.class);
	}
	@Override
	protected Object loadFormBackingObject(HttpServletRequest req) throws Exception {
		Long id = ServletRequestUtils.getLongParameter(req, "concernCategoryId");
	    if (null != id)
	    {
	      if (log.isDebugEnabled())
	      {
	        log.debug("id is not null....retrieving object from DB");
	      }

	      BaseWrapper baseWrapper = new BaseWrapperImpl();
	      ConcernCategoryModel concernCategoryModel = new ConcernCategoryModel();
	      concernCategoryModel.setPrimaryKey(id);
	      concernCategoryModel.setConcernCategoryId(id);
	      baseWrapper.setBasePersistableModel(concernCategoryModel);
	      baseWrapper = this.concernManager.searchConcernCategoryByPrimaryKey(baseWrapper);
	      
	      return (ConcernCategoryModel)baseWrapper.getBasePersistableModel();
	    }
	    else
	    {
	      if (log.isDebugEnabled())
	      {
	        log.debug("id is null....creating new instance of Model");
	      }
	      return new ConcernCategoryModel();
	    }
	}
	@Override
	protected Map loadReferenceData(HttpServletRequest req) throws Exception {
		// TODO Auto-generated method stub
		return null;
	}
	@Override
	protected ModelAndView onCreate(HttpServletRequest req, HttpServletResponse res, Object model, BindException errors) throws Exception {
		Date nowDate = new Date();
		ConcernCategoryModel concernCategoryModel = (ConcernCategoryModel)model;
		ConcernCategoryModel tempConcernCategoryModel = new ConcernCategoryModel();
		tempConcernCategoryModel.setName(concernCategoryModel.getName());
        BaseWrapper tempBaseWrapper = new BaseWrapperImpl();
        tempBaseWrapper.setBasePersistableModel(tempConcernCategoryModel);
        tempBaseWrapper = this.concernManager.searchConcernCategoryByName(tempBaseWrapper);
        CustomList<ConcernCategoryModel> list = (CustomList<ConcernCategoryModel>)tempBaseWrapper.getObject("list");
		if(list.getResultsetList().size() > 0){
			ConcernCategoryModel ccModel = list.getResultsetList().get(0);
			if(concernCategoryModel.getName().equalsIgnoreCase(ccModel.getName())){
				super.saveMessage(req, "Concern Category with the same name already exists.");
				return super.showForm(req, res, errors);
			}
		}
		
		concernCategoryModel.setActive(true);
		concernCategoryModel.setCreatedByAppUserModel(UserUtils.getCurrentUser());
		concernCategoryModel.setCreatedOn(nowDate);
		concernCategoryModel.setUpdatedByAppUserModel(UserUtils.getCurrentUser());
		concernCategoryModel.setUpdatesOn(nowDate);
		BaseWrapper baseWrapper = new BaseWrapperImpl();
		baseWrapper.setBasePersistableModel(concernCategoryModel);
		try{
		   baseWrapper = this.concernManager.createConcernCategory(baseWrapper);
		   this.saveMessage(req, "Record saved successfully.");
		}catch(FrameworkCheckedException fce){
		   if(ExceptionErrorCodes.DATA_INTEGRITY_VIOLATION_EXCEPTION == fce.getErrorCode())
			  super.saveMessage(req, "Concern Category with the same name already exists.");
		   else
			   super.saveMessage(req, "Record could not be saved.");
		   return super.showForm(req, res, errors);
		}catch(Exception fce){
			  
			super.saveMessage(req,MessageUtil.getMessage("6075"));
			return super.showForm(req, res, errors);
		}
		ModelAndView modelAndView = new ModelAndView(new RedirectView("p_opconcerncatdefmanagement.html?actionId=2&_formSubmit=true"));
		return modelAndView;
	}
	@Override
	protected ModelAndView onUpdate(HttpServletRequest req, HttpServletResponse res, Object model, BindException errors) throws Exception {
		Date nowDate = new Date();
		BaseWrapper baseWrapper = new BaseWrapperImpl();
		ConcernCategoryModel tempConcernCategoryModel = (ConcernCategoryModel)model;
		
		ConcernCategoryModel concernCategoryModel = new ConcernCategoryModel();
		concernCategoryModel.setConcernCategoryId(tempConcernCategoryModel.getConcernCategoryId());
		baseWrapper.setBasePersistableModel(concernCategoryModel);
		baseWrapper = this.concernManager.searchConcernCategoryByPrimaryKey(baseWrapper);
		concernCategoryModel = (ConcernCategoryModel)baseWrapper.getBasePersistableModel();
		
		if(!concernCategoryModel.getName().equalsIgnoreCase(tempConcernCategoryModel.getName())){
			ConcernCategoryModel tConcernCategoryModel = new ConcernCategoryModel();
			tConcernCategoryModel.setName(tempConcernCategoryModel.getName());
	        BaseWrapper tempBaseWrapper = new BaseWrapperImpl();
	        tempBaseWrapper.setBasePersistableModel(tConcernCategoryModel);
	        tempBaseWrapper = this.concernManager.searchConcernCategoryByName(tempBaseWrapper);
	        CustomList<ConcernCategoryModel> list = (CustomList<ConcernCategoryModel>)tempBaseWrapper.getObject("list");
			if(list.getResultsetList().size() > 0){
				ConcernCategoryModel ccModel = list.getResultsetList().get(0);
				if(tempConcernCategoryModel.getName().equalsIgnoreCase(ccModel.getName())){
					super.saveMessage(req, "Concern Category with the same name already exists.");
					return super.showForm(req, res, errors);
				}
			}
		}
		
		concernCategoryModel.setName(tempConcernCategoryModel.getName());
		concernCategoryModel.setComments(tempConcernCategoryModel.getComments());
		concernCategoryModel.setDescription(tempConcernCategoryModel.getDescription());
		
		concernCategoryModel.setUpdatedByAppUserModel(UserUtils.getCurrentUser());
		concernCategoryModel.setUpdatesOn(nowDate);
		baseWrapper.setBasePersistableModel(concernCategoryModel);
		try{
		    baseWrapper = this.concernManager.updateConcernCategory(baseWrapper);
		    this.saveMessage(req, "Record saved successfully.");
		}catch(FrameworkCheckedException fce){
			super.saveMessage(req, "Record could not be saved.");
			fce.printStackTrace();
			super.showForm(req, res, errors);
		}catch(Exception fce){
			fce.printStackTrace();  
			super.saveMessage(req,MessageUtil.getMessage("6075"));
			return super.showForm(req, res, errors);
		}
		
		ModelAndView modelAndView = new ModelAndView(new RedirectView("p_opconcerncatdefmanagement.html?actionId=2&_formSubmit=true"));
		return modelAndView;
	}
	public void setConcernManager(ConcernManager concernManager) {
		this.concernManager = concernManager;
	}
}
