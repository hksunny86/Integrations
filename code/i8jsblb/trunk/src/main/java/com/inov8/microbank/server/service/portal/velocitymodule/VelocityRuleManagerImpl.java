package com.inov8.microbank.server.service.portal.velocitymodule;

import java.util.List;

import com.inov8.framework.common.exception.FrameworkCheckedException;
import com.inov8.framework.common.util.CustomList;
import com.inov8.framework.common.wrapper.BaseWrapper;
import com.inov8.framework.common.wrapper.SearchBaseWrapper;
import com.inov8.framework.common.wrapper.SearchBaseWrapperImpl;
import com.inov8.microbank.common.model.ActionLogModel;
import com.inov8.microbank.common.model.ProductModel;
import com.inov8.microbank.common.model.VelocityRuleModel;
import com.inov8.microbank.common.util.ThreadLocalActionLog;
import com.inov8.microbank.server.dao.velocitymodule.VelocityRuleModelDAO;
import com.inov8.microbank.server.service.actionlogmodule.ActionLogManager;
import com.inov8.microbank.server.service.productmodule.ProductManager;

public class VelocityRuleManagerImpl implements VelocityRuleManager {

	private VelocityRuleModelDAO velocityRuleModelDAO;
	private ActionLogManager actionLogManager;
	private ProductManager productManager;
	
	@Override
	public BaseWrapper saveOrUpdate(BaseWrapper baseWrapper)
			throws FrameworkCheckedException {
		ActionLogModel actionLogModel = this.actionLogManager.createActionLogRequiresNewTransaction(baseWrapper);
		ThreadLocalActionLog.setActionLogId(actionLogModel.getActionLogId());
		VelocityRuleModel velocityRuleModel= velocityRuleModelDAO.saveOrUpdate((VelocityRuleModel) baseWrapper.getBasePersistableModel());
		baseWrapper.setBasePersistableModel(velocityRuleModel);

//******commented product loading as it is not mandatory on the screen so throwing nullPointerException
//		SearchBaseWrapper searchBaseWrapper = new SearchBaseWrapperImpl();
//		searchBaseWrapper.setBasePersistableModel(velocityRuleModel.getProductIdProductModel());
//		searchBaseWrapper = productManager.loadProduct(searchBaseWrapper);
//		ProductModel productModel = (ProductModel) searchBaseWrapper.getBasePersistableModel();
	
//		actionLogModel.setCustomField11(productModel.getName());
	    this.actionLogManager.completeActionLog(actionLogModel);
		return baseWrapper;
	}

	@Override
	public VelocityRuleModel loadVelocityRuleModel(Long velocityRuleId)
			throws FrameworkCheckedException {
		
		return velocityRuleModelDAO.findByPrimaryKey(velocityRuleId);
	}

	@Override
	public SearchBaseWrapper searchVelocityRule(SearchBaseWrapper searchBaseWrapper) throws FrameworkCheckedException {
		CustomList<VelocityRuleModel> customList = this.velocityRuleModelDAO.findByExample( (VelocityRuleModel)
			 	searchBaseWrapper.getBasePersistableModel(),
		        searchBaseWrapper.getPagingHelperModel(),
		        searchBaseWrapper.getSortingOrderMap(),searchBaseWrapper.getDateRangeHolderModel());
	searchBaseWrapper.setCustomList(customList);
	return searchBaseWrapper;
	}
	@Override
	public List<VelocityRuleModel> findByCriteria(VelocityRuleModel velocityRuleModel) throws FrameworkCheckedException {
		
		return velocityRuleModelDAO.findByCriteria(velocityRuleModel) ;
	}

	
	public void setVelocityRuleModelDAO(VelocityRuleModelDAO velocityRuleModelDAO) {
		this.velocityRuleModelDAO = velocityRuleModelDAO;
	}

	public void setActionLogManager(ActionLogManager actionLogManager) {
		this.actionLogManager = actionLogManager;
	}

	public void setProductManager(ProductManager productManager) {
		this.productManager = productManager;
	}

	
}
