package com.inov8.microbank.tax.service;

import java.util.Date;
import java.util.List;
import java.util.Map;

import com.inov8.framework.common.exception.FrameworkCheckedException;
import com.inov8.framework.common.util.CustomList;
import com.inov8.framework.common.wrapper.BaseWrapper;
import com.inov8.framework.common.wrapper.SearchBaseWrapper;
import com.inov8.microbank.common.model.FilerRateConfigModel;
import com.inov8.microbank.common.model.TaxRegimeModel;
import com.inov8.microbank.common.wrapper.workflow.WorkFlowWrapper;
import com.inov8.microbank.tax.model.DailyWhtDeductionModel;
import com.inov8.microbank.tax.model.DateWiseUserWHTAmountViewModel;
import com.inov8.microbank.tax.model.FEDRuleModel;
import com.inov8.microbank.tax.model.FEDRuleViewModel;
import com.inov8.microbank.tax.model.WHTConfigModel;
import com.inov8.microbank.tax.model.WHTDeductionSchedularStatusModel;
import com.inov8.microbank.tax.model.WHTExemptionModel;
import com.inov8.microbank.tax.vo.WHTConfigWrapper;

public interface TaxManager {

	public abstract FEDRuleViewModel loadFEDRuleViewModel(WorkFlowWrapper workFlowWrapper) throws FrameworkCheckedException;

	public abstract WHTConfigModel loadCommissionWHTConfigModel(Long appUserId);

	public abstract WHTConfigModel loadWHTConfigModel(Long whtConfigModelId);

	public abstract void saveOrUpdateWhtExemption(WHTExemptionModel whtExemptionModel);

	public abstract WHTExemptionModel loadValidWHTExemption(Long appUserId);

	public abstract List<WHTExemptionModel> loadWHTExemptionByAppUserId(Long appUserId);

	public abstract WHTExemptionModel loadWhtExemptionModelByExemptionId(Long exemptionId);

	public abstract SearchBaseWrapper searchWhtExemptionModel(SearchBaseWrapper searchBaseWrapper);

	public SearchBaseWrapper searchTaxRegimeDefaultFEDRates(SearchBaseWrapper searchBaseWrapper) throws FrameworkCheckedException;

	public TaxRegimeModel searchTaxRegimeById(Long taxRegimeId)	throws FrameworkCheckedException;

	public BaseWrapper createTaxRegimeFEDRatesWithAuthorization(BaseWrapper baseWrapper) throws FrameworkCheckedException;

	public List<FEDRuleModel> loadFEDRuleModelList() throws FrameworkCheckedException;

	public BaseWrapper saveUpdateFedRules(BaseWrapper baseWrapper)	throws FrameworkCheckedException;

	WorkFlowWrapper makeDebitForWHT(DailyWhtDeductionModel model) throws Exception;
//	void saveDailyWHTEntries(String mobileNo, Double principalAmount, WHTConfigModel whtConfigModel) throws Exception;
	public List<WorkFlowWrapper> prepareDailyWHTEntries(List<DateWiseUserWHTAmountViewModel> modelList, Map<Long, WHTConfigModel> configModelMap) throws Exception;
	public BaseWrapper saveUpdateWhtExemptionModelsWithAuthorization(BaseWrapper wrapper) throws Exception;
	List<DailyWhtDeductionModel> loadUnsettledWithholdingDeductionList(Date toDate) throws Exception;

	public WHTConfigWrapper loadAllActiveWHTConfigVo() ;
	public BaseWrapper updateWHTConfigModelWithAuthorization(BaseWrapper baseWrapper) throws FrameworkCheckedException;
	//BaseWrapper saveDailyWhtDeduction(BaseWrapper baseWrapper) throws Exception;

	public Boolean isTaxRegimeNameUnique(String taxRegimeName, Long taxRegimeId) throws FrameworkCheckedException;

    public BaseWrapper loadAndValidateWhtExemption(BaseWrapper baseWrapper) throws Exception;

	int getAssociatedUsersWithTaxRegime(Long taxRegimeId)		throws FrameworkCheckedException;

//	public CustomList<TaxRegimeModel> searchActiveTaxRegimeDefaultFEDRates() throws FrameworkCheckedException;
	
	public void saveOrUpdateWHTDeductionSchedularStatus(WHTDeductionSchedularStatusModel wHTDeductionSchedularStatusModel ) throws Exception;
	public List<WHTDeductionSchedularStatusModel> findWHTDeductionMissedEntries(WHTDeductionSchedularStatusModel wHTDeductionSchedularStatusModel) throws Exception;
	public List<WHTDeductionSchedularStatusModel>  findWHTDeductionSchedularStatusEntries() throws Exception;
	
	public void updateWhtDeductionSchedulerStatus() throws Exception;
	public void saveDailyWHTDeductionModels(List<WorkFlowWrapper> saveDailyWHTEntriesList) throws Exception;

	public abstract FilerRateConfigModel loadFilerRateConfigModelByFiler(Long filer);

}