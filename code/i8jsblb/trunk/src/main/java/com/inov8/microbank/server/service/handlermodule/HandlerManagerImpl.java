package com.inov8.microbank.server.service.handlermodule;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.inov8.framework.common.exception.FrameworkCheckedException;
import com.inov8.framework.common.model.ExampleConfigHolderModel;
import com.inov8.framework.common.util.CustomList;
import com.inov8.framework.common.wrapper.BaseWrapper;
import com.inov8.framework.common.wrapper.SearchBaseWrapper;
import com.inov8.microbank.common.model.HandlerModel;
import com.inov8.microbank.common.model.RetailerContactModel;
import com.inov8.microbank.common.model.retailermodule.HandlerSearchViewModel;
import com.inov8.microbank.server.dao.handlermodule.HandlerDAO;
import com.inov8.microbank.server.dao.handlermodule.HandlerSearchViewDAO;
import com.inov8.microbank.server.dao.retailermodule.RetailerDAO;
import com.inov8.microbank.server.service.actionlogmodule.ActionLogManager;
/**
 * <p>Title: Microbank</p>
 *
 * <p>Description: Backend Application for POS terminals.</p>
 *
 * <p>Copyright: Copyright (c) 2006</p>
 *
 * <p>Company: Inov8 Limited</p>
 *
 * @author Asad Hayat
 * @version 1.0
 */

public class HandlerManagerImpl implements HandlerManager
{

	private HandlerDAO handlerDAO;
	private HandlerSearchViewDAO handlerSearchViewDAO;
	private ActionLogManager actionLogManager;

	public SearchBaseWrapper loadHandler(SearchBaseWrapper searchBaseWrapper)
	{
		HandlerModel retailerModel = this.handlerDAO.findByPrimaryKey(
				searchBaseWrapper.getBasePersistableModel().
				getPrimaryKey());
		searchBaseWrapper.setBasePersistableModel(retailerModel);

		return searchBaseWrapper;
	}

	/**
	 * @param baseWrapper BaseWrapper
	 * @return BaseWrapper
	 */
	public BaseWrapper loadHandler(BaseWrapper baseWrapper)
	{
		HandlerModel retailerModel = this.handlerDAO.findByPrimaryKey(baseWrapper.
				getBasePersistableModel().
				getPrimaryKey());
		baseWrapper.setBasePersistableModel(retailerModel);
		return baseWrapper;
	}

	public SearchBaseWrapper searchHandler(SearchBaseWrapper searchBaseWrapper)
	{
		CustomList<HandlerModel>
		list = this.handlerDAO.findByExample( (HandlerModel)
				searchBaseWrapper.getBasePersistableModel(),
				searchBaseWrapper.getPagingHelperModel(),
				searchBaseWrapper.getSortingOrderMap());
		searchBaseWrapper.setCustomList(list);
		return searchBaseWrapper;
	}
	
	/**
	 * @author AtifHu
	 */
	public SearchBaseWrapper searchHandlerView(SearchBaseWrapper searchBaseWrapper)
	{
		HandlerSearchViewModel handlerSearchViewModel = (HandlerSearchViewModel) searchBaseWrapper
				.getBasePersistableModel();

		List<HandlerSearchViewModel> handlerSearchViewModelList = this.handlerSearchViewDAO.searchHandler(handlerSearchViewModel);
		
		searchBaseWrapper.setCustomList(new CustomList<>(handlerSearchViewModelList));
		return searchBaseWrapper;
	}
	public List<HandlerSearchViewModel> findHandlerViews(BaseWrapper baseWrapper)
	{
		
		List<HandlerSearchViewModel> list = new ArrayList<>();
		CustomList<HandlerSearchViewModel> customelist = this.handlerSearchViewDAO.findByExample((HandlerSearchViewModel) baseWrapper.getBasePersistableModel()); 
		if(null!=customelist.getResultsetList() && customelist.getResultsetList().size()>0 ){
			list = customelist.getResultsetList();
		}	
		return list;
	}

	
	 @Override
		public Boolean isAgentWebEnabled(Long handlerId) {

	    	HandlerModel model = handlerDAO.findByPrimaryKey(handlerId);
			Boolean isEnabled = false;
			 
			 if(model.getIsAgentWebEnabled() != null && model.getIsAgentWebEnabled() == true){
				 isEnabled = true;
			 }else{
				 isEnabled = false;
			 }
			 return isEnabled;
		}

		//Debit Card Fee Enabled
	@Override
	public Boolean isDebitCardFeeEnabled(Long handlerId) {
		HandlerModel model = handlerDAO.findByPrimaryKey(handlerId);
		Boolean isEnabled = false;

		if(model.getIsDebitCardFeeEnabled() != null && model.getIsDebitCardFeeEnabled() == true){
			isEnabled = true;
		}else{
			isEnabled = false;
		}
		return isEnabled;
	}


	@Override
	public int countByExample(HandlerModel handlerModel, ExampleConfigHolderModel exampleConfigHolderModel)
	{
		return handlerDAO.countByExample(handlerModel, exampleConfigHolderModel);
	}

	public List<Long> getRetalerDataMap(Long retailerContactId) {
				
		return handlerDAO.getRetalerDataMap(retailerContactId);
		
	}

	public BaseWrapper createOrUpdateHandler(BaseWrapper baseWrapper) throws FrameworkCheckedException
	{
		handlerDAO.saveOrUpdate((HandlerModel)baseWrapper.getBasePersistableModel());
		
		return baseWrapper;
	}

	public void setHandlerDAO(HandlerDAO handlerDAO)
	{
		this.handlerDAO = handlerDAO;
	}


	public void setActionLogManager(ActionLogManager actionLogManager)
	{
		this.actionLogManager = actionLogManager;
	}

	public void setHandlerSearchViewDAO(HandlerSearchViewDAO handlerSearchViewDAO) {
		this.handlerSearchViewDAO = handlerSearchViewDAO;
	}
}