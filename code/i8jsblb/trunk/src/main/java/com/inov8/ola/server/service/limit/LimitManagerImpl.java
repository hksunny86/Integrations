package com.inov8.ola.server.service.limit;

/** 	
 * @author 					Usman Ashraf
 * Project Name: 			OLA
 * Creation Date: 			April 2012  			
 * Description:				
 */



import java.util.Collection;
import java.util.List;

import com.inov8.framework.common.exception.FrameworkCheckedException;
import com.inov8.framework.common.util.CustomList;
import com.inov8.framework.common.wrapper.BaseWrapper;
import com.inov8.framework.common.wrapper.SearchBaseWrapper;
import com.inov8.framework.common.wrapper.SearchBaseWrapperImpl;
import com.inov8.integration.common.model.BlinkDefaultLimitModel;
import com.inov8.integration.common.model.LimitModel;
import com.inov8.microbank.common.model.ActionLogModel;
import com.inov8.microbank.common.model.BlinkCustomerLimitModel;
import com.inov8.microbank.common.model.LimitRuleModel;
import com.inov8.microbank.common.model.ProductModel;
import com.inov8.microbank.common.util.ThreadLocalActionLog;
import com.inov8.microbank.server.service.actionlogmodule.ActionLogManager;
import com.inov8.microbank.server.service.productmodule.ProductManager;
import com.inov8.ola.server.dao.blinkcustomerlimit.BlinkCustomerDAO;
import com.inov8.ola.server.dao.limit.BlinkDefaultLimitDAO;
import com.inov8.ola.server.dao.limit.LimitDAO;
import com.inov8.ola.server.dao.limit.LimitRuleDAO;


public class LimitManagerImpl implements LimitManager 
{
	private LimitDAO limitDAO;
	private BlinkDefaultLimitDAO blinkDefaultLimitDAO;
	private BlinkCustomerDAO blinkCustomerDAO;
	private LimitRuleDAO limitRuleDAO;
	private ProductManager productManager;
	private ActionLogManager actionLogManager;
	
	/**
	 * Input should be of TransactionTypeModel.
	 */
	public LimitModel getLimitByTransactionType(Long transactionTypeId, Long limitTypeId,Long customerAccountTypeId)throws FrameworkCheckedException {
		LimitModel resultModel = null;
		try{
			LimitModel limitModel = new LimitModel();
			limitModel.setTransactionTypeId(transactionTypeId);
			limitModel.setLimitTypeId(limitTypeId);
			limitModel.setCustomerAccountTypeId(customerAccountTypeId);
			List<LimitModel> searchedResults = this.limitDAO.findByExample(limitModel).getResultsetList();
			if(searchedResults != null && searchedResults.size() > 0){
				resultModel = searchedResults.get(0);

			}
		}catch(Exception exception){
			exception.printStackTrace();
		}

		return resultModel;
	}

	@Override
	public BlinkDefaultLimitModel getBlinkDefaultLimitByTransactionType(Long transactionTypeId, Long limitTypeId, Long customerAccountTypeId) throws Exception {
		BlinkDefaultLimitModel resultModel = null;
		try{
			BlinkDefaultLimitModel blinkDefaultLimitModel = new BlinkDefaultLimitModel();
			blinkDefaultLimitModel.setTransactionTypeId(transactionTypeId);
			blinkDefaultLimitModel.setLimitTypeId(limitTypeId);
			blinkDefaultLimitModel.setCustomerAccountTypeId(customerAccountTypeId);
			List<BlinkDefaultLimitModel> searchedResults = this.blinkDefaultLimitDAO.findByExample(blinkDefaultLimitModel).getResultsetList();
			if(searchedResults != null && searchedResults.size() > 0){
				resultModel = searchedResults.get(0);

			}
		}catch(Exception exception){
			exception.printStackTrace();
		}

		return resultModel;
	}

	@Override
	public BlinkCustomerLimitModel getBlinkCustomerLimitByTransactionType(Long transactionTypeId, Long limitTypeId, Long customerAccountTypeId, Long accountId) throws Exception {
		BlinkCustomerLimitModel resultModel = null;
		try{
			BlinkCustomerLimitModel limitModel = new BlinkCustomerLimitModel();
			limitModel.setTransactionType(transactionTypeId);
			limitModel.setLimitType(limitTypeId);
			limitModel.setCustomerAccTypeId(customerAccountTypeId);
			limitModel.setAccountId(accountId);
			List<BlinkCustomerLimitModel> searchedResults = this.blinkCustomerDAO.findByExample(limitModel).getResultsetList();
			if(searchedResults != null && searchedResults.size() > 0){
				resultModel = searchedResults.get(0);

			}
		}catch(Exception exception){
			exception.printStackTrace();
		}

		return resultModel;
	}


	@Override
	public List<BlinkCustomerLimitModel> getBlinkCustomerLimitByTransactionTypeByCustomerId(Long customerId) throws FrameworkCheckedException {
		List<BlinkCustomerLimitModel> searchedResults = null;
		try{
			BlinkCustomerLimitModel limitModel = new BlinkCustomerLimitModel();
			limitModel.setCustomerId(customerId);
			searchedResults = this.blinkCustomerDAO.findByExample(limitModel).getResultsetList();
		}catch(Exception exception){
			exception.printStackTrace();
		}
		return searchedResults;
	}

	public List<LimitModel> getLimitsByCustomerAccountType(Long customerAccountTypeId)throws FrameworkCheckedException {
		List<LimitModel> searchedResults = null;
		try{
			LimitModel limitModel = new LimitModel();
			limitModel.setCustomerAccountTypeId(customerAccountTypeId);
			searchedResults = this.limitDAO.getLimitsByCustomerAccountType(customerAccountTypeId);
		}catch(Exception exception){
			exception.printStackTrace();
		}
		return searchedResults;
	}
	
	public boolean updateLimitsList(List<LimitModel> limitsList) throws FrameworkCheckedException{
			
		Collection<LimitModel> updatedList = this.limitDAO.saveOrUpdateCollection(limitsList);
		
	    return updatedList.size() > 0;
	}
	public void setLimitDAO(LimitDAO limitDAO) {
		this.limitDAO = limitDAO;
	}
	
	@Override
	public BaseWrapper saveOrUpdateLimitRule(BaseWrapper baseWrapper ) throws FrameworkCheckedException{
		ActionLogModel actionLogModel = this.actionLogManager.createActionLogRequiresNewTransaction(baseWrapper);
		ThreadLocalActionLog.setActionLogId(actionLogModel.getActionLogId());
		LimitRuleModel limitRuleModel = (LimitRuleModel) baseWrapper.getBasePersistableModel();	
		LimitRuleModel limitRuleModelTemp = new LimitRuleModel();
		
		limitRuleModelTemp.setProductId(limitRuleModel.getProductId());
		limitRuleModelTemp.setSegmentId(limitRuleModel.getSegmentId());
		limitRuleModelTemp.setAccountTypeId(limitRuleModel.getAccountTypeId());	
	
		int numberOfRecords = limitRuleDAO.findByCriteria(limitRuleModelTemp);
		
		if(numberOfRecords>1 || (null==limitRuleModel.getLimitRuleId() && numberOfRecords!=0))
			throw new FrameworkCheckedException("Same record already exists");	
		
//******commented product loading as it is not mandatory on the screen so throwing nullPointerException
//		SearchBaseWrapper searchBaseWrapper = new SearchBaseWrapperImpl();
//		searchBaseWrapper.setBasePersistableModel(limitRuleModel.getProductIdProductModel());
//		searchBaseWrapper = productManager.loadProduct(searchBaseWrapper);
//		ProductModel productModel = (ProductModel) searchBaseWrapper.getBasePersistableModel();
		
		limitRuleModel= limitRuleDAO.saveOrUpdate((LimitRuleModel) baseWrapper.getBasePersistableModel());
		baseWrapper.setBasePersistableModel(limitRuleModel);
//		actionLogModel.setCustomField11(productModel.getName());
	    this.actionLogManager.completeActionLog(actionLogModel);
		return baseWrapper;
	} 
	/// Limit Rule 
	@Override
	public LimitRuleModel loadLimitRuleModel(Long limitRuleId ) throws FrameworkCheckedException {
		return limitRuleDAO.findByPrimaryKey(limitRuleId);	
	}
	
	@Override
	public SearchBaseWrapper searchLimitRule(SearchBaseWrapper searchBaseWrapper) throws FrameworkCheckedException {
		
		CustomList<LimitRuleModel> customList = this.limitRuleDAO.findByExample( (LimitRuleModel)
				 	searchBaseWrapper.getBasePersistableModel(),
			        searchBaseWrapper.getPagingHelperModel(),
			        searchBaseWrapper.getSortingOrderMap(),searchBaseWrapper.getDateRangeHolderModel());
		searchBaseWrapper.setCustomList(customList);
		return searchBaseWrapper;
	}

	public void setLimitRuleDAO(LimitRuleDAO limitRuleDAO) {
		this.limitRuleDAO = limitRuleDAO;
	}

	public void setActionLogManager(ActionLogManager actionLogManager) {
		this.actionLogManager = actionLogManager;
	}

	public void setProductManager(ProductManager productManager) {
		this.productManager = productManager;
	}

	public void setBlinkCustomerDAO(BlinkCustomerDAO blinkCustomerDAO) {


		this.blinkCustomerDAO = blinkCustomerDAO;
	}

	public void setBlinkDefaultLimitDAO(BlinkDefaultLimitDAO blinkDefaultLimitDAO) {
		this.blinkDefaultLimitDAO = blinkDefaultLimitDAO;
	}
}


