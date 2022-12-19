package com.inov8.microbank.server.service.portal.operatinghoursmodule;


import com.inov8.framework.common.exception.FrameworkCheckedException;
import com.inov8.framework.common.util.CustomList;
import com.inov8.framework.common.wrapper.BaseWrapper;
import com.inov8.framework.common.wrapper.SearchBaseWrapper;
import com.inov8.microbank.common.model.ActionLogModel;
import com.inov8.microbank.common.model.OperatingHoursRuleModel;


import com.inov8.microbank.common.util.ThreadLocalActionLog;
import com.inov8.microbank.server.dao.operatinghoursmodule.OperatingHoursRuleModelDAO;
import com.inov8.microbank.server.service.actionlogmodule.ActionLogManager;
import com.inov8.microbank.server.service.productmodule.ProductManager;

import java.util.List;

public class OperatingHoursRuleManagerImpl implements OperatingHoursRuleManager {
    private OperatingHoursRuleModelDAO operatingHoursRuleModelDAO;
    private ActionLogManager actionLogManager;
    private ProductManager productManager;

    @Override
    public BaseWrapper saveOrUpdate(BaseWrapper baseWrapper) throws FrameworkCheckedException {
        ActionLogModel actionLogModel = this.actionLogManager.createActionLogRequiresNewTransaction(baseWrapper);
        ThreadLocalActionLog.setActionLogId(actionLogModel.getActionLogId());
        OperatingHoursRuleModel operatingHoursRuleModel= operatingHoursRuleModelDAO.saveOrUpdate((OperatingHoursRuleModel) baseWrapper.getBasePersistableModel());
        baseWrapper.setBasePersistableModel(operatingHoursRuleModel);

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
    public OperatingHoursRuleModel loadOperatingHoursRuleModel(Long operatingHoursRuleId) throws FrameworkCheckedException {
        return operatingHoursRuleModelDAO.findByPrimaryKey(operatingHoursRuleId);
    }

    @Override
    public SearchBaseWrapper searchOperatingHoursRule(SearchBaseWrapper searchBaseWrapper) throws FrameworkCheckedException {
        CustomList<OperatingHoursRuleModel> customList = this.operatingHoursRuleModelDAO.findByExample( (OperatingHoursRuleModel)
                        searchBaseWrapper.getBasePersistableModel(),
                searchBaseWrapper.getPagingHelperModel(),
                searchBaseWrapper.getSortingOrderMap(),searchBaseWrapper.getDateRangeHolderModel());
        searchBaseWrapper.setCustomList(customList);
        return searchBaseWrapper;
    }

    @Override
    public List<OperatingHoursRuleModel> findByCriteria(OperatingHoursRuleModel operatingHoursRuleModeltemp) throws FrameworkCheckedException {
        return operatingHoursRuleModelDAO.findByCriteria(operatingHoursRuleModeltemp) ;
    }

    public void setOperatingHoursRuleModelDAO(OperatingHoursRuleModelDAO operatingHoursRuleModelDAO) {
        this.operatingHoursRuleModelDAO = operatingHoursRuleModelDAO;
    }

    public void setActionLogManager(ActionLogManager actionLogManager) {
        this.actionLogManager = actionLogManager;
    }

    public void setProductManager(ProductManager productManager) {
        this.productManager = productManager;
    }

}
