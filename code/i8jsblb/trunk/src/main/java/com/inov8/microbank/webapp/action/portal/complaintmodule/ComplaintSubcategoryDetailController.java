package com.inov8.microbank.webapp.action.portal.complaintmodule;


import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.validation.BindException;
import org.springframework.web.bind.ServletRequestUtils;
import org.springframework.web.servlet.ModelAndView;

import com.inov8.framework.common.model.BasePersistableModel;
import com.inov8.framework.common.util.CustomList;
import com.inov8.framework.common.wrapper.SearchBaseWrapper;
import com.inov8.framework.common.wrapper.SearchBaseWrapperImpl;
import com.inov8.framework.webapp.action.AdvanceFormController;
import com.inov8.microbank.common.model.ComplaintSubcategoryViewModel;
import com.inov8.microbank.server.service.complaintmodule.ComplaintManager;

public class ComplaintSubcategoryDetailController extends AdvanceFormController{

	private ComplaintManager complaintManager;
    
	public ComplaintSubcategoryDetailController() {
		setCommandName("complaintSubcategoryViewModel");
		setCommandClass(ComplaintSubcategoryViewModel.class);
	}

	public ComplaintManager getComplaintManager() {
		return complaintManager;
	}

	public void setComplaintManager(ComplaintManager complaintManager) {
		this.complaintManager = complaintManager;
	}


	@Override
	protected Object loadFormBackingObject(HttpServletRequest req) throws Exception {
		String subcategoryId = ServletRequestUtils.getStringParameter(req, "subcategoryId");
		
		SearchBaseWrapper baseWrapper = new SearchBaseWrapperImpl();
		ComplaintSubcategoryViewModel model = new ComplaintSubcategoryViewModel();
		model.setComplaintSubcategoryId(Long.valueOf(subcategoryId));
		baseWrapper.setBasePersistableModel(model);

		List<ComplaintSubcategoryViewModel> list = this.complaintManager.searchComplaintSubcategoryList(baseWrapper);

	    return (ComplaintSubcategoryViewModel) list.get(0);
		
	}

	@Override
	protected Map loadReferenceData(HttpServletRequest req) throws Exception {
		return null;
	}
    
	@Override
	protected ModelAndView onCreate(HttpServletRequest req, HttpServletResponse res, Object obj, BindException errors) throws Exception {
		ModelAndView modelAndView = new ModelAndView(this.getSuccessView());
		return modelAndView;
	}

	@Override
	protected ModelAndView onUpdate(HttpServletRequest req, HttpServletResponse res, Object obj, BindException errors) throws Exception {
		ModelAndView modelAndView = new ModelAndView(getSuccessView() );
		    
		return modelAndView;
	}

}
