package com.inov8.microbank.webapp.action.tickermodule;

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
import com.inov8.microbank.common.model.TickerModel;
import com.inov8.microbank.common.model.tickermodule.TickerListViewModel;
import com.inov8.microbank.server.service.tickermodule.TickerManager;


public class TickerFormController extends AdvanceFormController {

	
	private ReferenceDataManager referenceDataManager;
		private Long id ;
		private TickerManager tickerManager;
	
	public void setTickerManager(TickerManager tickerManager) {
			this.tickerManager = tickerManager;
		}

	public TickerFormController() {
		setCommandName("tickerListViewModel");
		setCommandClass(TickerListViewModel.class);
	}

	public void setReferenceDataManager(ReferenceDataManager referenceDataManager) {
		this.referenceDataManager = referenceDataManager;
	}

	@Override
	protected Object loadFormBackingObject(HttpServletRequest httpServletRequest) throws Exception {
		
		id = ServletRequestUtils.getLongParameter(httpServletRequest, "tickerId");
	    if (null != id)
	    {
	      if (log.isDebugEnabled())
	      {
	        log.debug("id is not null....retrieving object from DB");
	      }

	      SearchBaseWrapper searchBaseWrapper = new SearchBaseWrapperImpl();
	      TickerListViewModel tickerListViewModel = new TickerListViewModel();
	      tickerListViewModel.setTickerId(id);

	      searchBaseWrapper.setBasePersistableModel(tickerListViewModel);
	      searchBaseWrapper = this.tickerManager.loadTickerUser(
	          searchBaseWrapper);
	      tickerListViewModel = (TickerListViewModel) searchBaseWrapper.getBasePersistableModel();


	      if (log.isDebugEnabled())
	      {
	        log.debug("ticker String  is : " + tickerListViewModel.getTickerString());
	      }
	      return tickerListViewModel;
	    }
	    else
	    {
	      if (log.isDebugEnabled())
	      {
	        log.debug("id is null....creating new instance of Model");
	      }
	      long theDate = new Date().getTime();
	      TickerListViewModel tickerListViewModel = new TickerListViewModel();
	      tickerListViewModel.setCreatedOn(new Date(theDate));
	      return tickerListViewModel;
	    }
		
	}

	@Override
	protected Map loadReferenceData(HttpServletRequest arg0) throws Exception {
		
		 if (log.isDebugEnabled())
		    {
		      log.debug("Inside reference data");
		    }

		    /**
		     * code fragment to load reference data  for Supplier
		     *
		     */
         TickerModel tickerModel = new TickerModel();
         BaseWrapper baseWrapper = new BaseWrapperImpl();
         baseWrapper = this.tickerManager.loadDefaultTicker(baseWrapper);
         tickerModel = (TickerModel)baseWrapper.getBasePersistableModel();
         //arg0.setAttribute("defaultTickerString", tickerModel.getTickerString());
         
		 TickerListViewModel tickerListViewModel = new TickerListViewModel();
		 tickerListViewModel.setAccountEnabled(true);
		    ReferenceDataWrapper referenceDataWrapper = new ReferenceDataWrapperImpl(
		    		tickerListViewModel, "tickerId", SortingOrder.ASC);
		    referenceDataWrapper.setBasePersistableModel(tickerListViewModel);
		    referenceDataManager.getReferenceData(referenceDataWrapper);
		    List<TickerListViewModel> tickerModelList = null;
		    if (referenceDataWrapper.getReferenceDataList() != null)
		    {
		    	tickerModelList = referenceDataWrapper.getReferenceDataList();
		    }

		    Map referenceDataMap = new HashMap();
		    referenceDataMap.put("tickerModelList",
		    		tickerModelList);
		    


			
		    return referenceDataMap;

		
	}

	@Override
	protected ModelAndView onCreate(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object object, BindException bindException) throws Exception {
		// TODO Auto-generated method stub
		return this.createOrUpdate(httpServletRequest, httpServletResponse,
				object, bindException);
	}

	@Override
	protected ModelAndView onUpdate(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object object, BindException bindException) throws Exception {
		// TODO Auto-generated method stub
		return this.createOrUpdate(httpServletRequest, httpServletResponse,
				object, bindException);
	}
	
	
	private ModelAndView createOrUpdate(HttpServletRequest request,
			HttpServletResponse response, Object command, BindException errors)
			throws Exception {

		try {
			BaseWrapper baseWrapper = new BaseWrapperImpl();

			TickerListViewModel tickerListViewModel = (TickerListViewModel) command;
			

			if (null != id) {

				baseWrapper.setBasePersistableModel(tickerListViewModel);
				

				baseWrapper = this.tickerManager.updateTickerUser(

				baseWrapper);

			} else {

				
				baseWrapper.setBasePersistableModel(tickerListViewModel);
				
				baseWrapper = this.tickerManager.createTickerUser(baseWrapper);

				

			}

			this.saveMessage(request, "Record saved successfully");
			ModelAndView modelAndView = new ModelAndView(this.getSuccessView());
			return modelAndView;

		} catch (FrameworkCheckedException ex) {
			if (ExceptionErrorCodes.DATA_INTEGRITY_VIOLATION_EXCEPTION == ex
					.getErrorCode()) {
				super.saveMessage(request, ex.getMessage());
				return super.showForm(request, response, errors);
			}
			throw ex;
		}

	}


}
