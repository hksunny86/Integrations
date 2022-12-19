package com.inov8.microbank.server.facade.taxregimemodule;

import java.util.Date;
import java.util.List;
import java.util.Map;

import com.inov8.framework.common.exception.FrameworkCheckedException;
import com.inov8.framework.common.exception.FrameworkExceptionTranslator;
import com.inov8.framework.common.util.CustomList;
import com.inov8.framework.common.wrapper.BaseWrapper;
import com.inov8.framework.common.wrapper.SearchBaseWrapper;
import com.inov8.microbank.common.model.FilerRateConfigModel;
import com.inov8.microbank.common.model.TaxRegimeModel;
import com.inov8.microbank.common.wrapper.workflow.WorkFlowWrapper;
import com.inov8.microbank.server.facade.CreditAccountQueingPreProcessor;
import com.inov8.microbank.tax.model.*;
import com.inov8.microbank.tax.service.TaxManager;
/*import com.inov8.microbank.tax.vo.FedRuleManagementVO;
import com.inov8.microbank.tax.vo.WHTConfigWrapper;*/
import com.inov8.microbank.tax.vo.FedRuleManagementVO;
import com.inov8.microbank.tax.vo.WHTConfigWrapper;

public class TaxRegimeFacadeImpl implements TaxRegimeFacade {

	private FrameworkExceptionTranslator frameworkExceptionTranslator;
	private TaxManager taxManager;
	 private CreditAccountQueingPreProcessor creditAccountQueingPreProcessor;


	@Override
	public FEDRuleViewModel loadFEDRuleViewModel(WorkFlowWrapper workFlowWrapper)
			throws FrameworkCheckedException {
		try{
			return taxManager.loadFEDRuleViewModel(workFlowWrapper);
		}catch (Exception ex){
			throw frameworkExceptionTranslator.translate(ex,FrameworkExceptionTranslator.FIND_ACTION);
		}
	}

	@Override
	public WHTConfigModel loadCommissionWHTConfigModel(Long appUserId) {
		try{
			return taxManager.loadCommissionWHTConfigModel(appUserId);
		}catch (Exception ex){
			ex.printStackTrace();
		}
		return null;
	}

	@Override
	public WHTConfigModel loadWHTConfigModel(Long whtConfigModelId) {
		try{
			return taxManager.loadWHTConfigModel(whtConfigModelId);
		}catch (Exception ex){
			ex.printStackTrace();
		}

		return null;
	}

	@Override
	public void saveOrUpdateWhtExemption(WHTExemptionModel whtExemptionModel) {
		try{
			taxManager.saveOrUpdateWhtExemption(whtExemptionModel);
		}catch (Exception ex){
			ex.printStackTrace();
		}
	}

	@Override
	public WHTExemptionModel loadValidWHTExemption(Long appUserId) {
		try{
			return taxManager.loadValidWHTExemption(appUserId);
		}catch (Exception ex){
			ex.printStackTrace();
		}	

		return null;
	}

	@Override
	public List<WHTExemptionModel> loadWHTExemptionByAppUserId(Long appUserId) {
		try{
			return taxManager.loadWHTExemptionByAppUserId(appUserId);
		}catch (Exception ex){
			ex.printStackTrace();
		}

		return null;
	}

	@Override
	public WHTExemptionModel loadWhtExemptionModelByExemptionId(Long exemptionId) {
		try{
			return taxManager.loadWhtExemptionModelByExemptionId(exemptionId);
		}catch (Exception ex){
			ex.printStackTrace();
		}

		return null;
	}

	@Override
	public SearchBaseWrapper searchWhtExemptionModel(SearchBaseWrapper searchBaseWrapper) {
		try{
			return taxManager.searchWhtExemptionModel(searchBaseWrapper);
		}catch (Exception ex){
			ex.printStackTrace();
		}

		return null;
	}

	@Override
	public SearchBaseWrapper searchTaxRegimeDefaultFEDRates( SearchBaseWrapper searchBaseWrapper) throws FrameworkCheckedException
	{
	    try
        {
	        return taxManager.searchTaxRegimeDefaultFEDRates( searchBaseWrapper);
        }
        catch( Exception e )
        {
            throw frameworkExceptionTranslator.translate( e, FrameworkExceptionTranslator.FIND_ACTION );
        }
	}

	@Override
	public TaxRegimeModel searchTaxRegimeById( Long taxRegimeId) throws FrameworkCheckedException
	{
	    try
        {
	        return taxManager.searchTaxRegimeById( taxRegimeId);
        }
        catch( Exception e )
        {
            throw frameworkExceptionTranslator.translate( e, FrameworkExceptionTranslator.FIND_ACTION );
        }
	}

	@Override
	public BaseWrapper createTaxRegimeFEDRatesWithAuthorization( BaseWrapper baseWrapper) throws FrameworkCheckedException
	{
	    try
        {
	        taxManager.createTaxRegimeFEDRatesWithAuthorization( baseWrapper);
        }
        catch( Exception e )
        {
            throw frameworkExceptionTranslator.translate( e, FrameworkExceptionTranslator.INSERT_ACTION );
        }
		return baseWrapper;
	}

	@Override
	public BaseWrapper saveUpdateFedRules( BaseWrapper baseWrapper) throws FrameworkCheckedException
	{
	    try
        {
	    	FedRuleManagementVO vo = (FedRuleManagementVO) baseWrapper.getBasePersistableModel();
	        taxManager.saveUpdateFedRules(baseWrapper);
        }
        catch( Exception e )
        {
            throw frameworkExceptionTranslator.translate( e, FrameworkExceptionTranslator.INSERT_ACTION );
        }
		return baseWrapper;
	}

	@Override
	public BaseWrapper saveUpdateWhtExemptionModelsWithAuthorization(BaseWrapper wrapper) throws FrameworkCheckedException{
		try {
			return taxManager.saveUpdateWhtExemptionModelsWithAuthorization(wrapper);
		} catch (Exception e) {
			throw frameworkExceptionTranslator.translate( e, FrameworkExceptionTranslator.INSERT_ACTION );
		}
	}

	@Override
	public WHTConfigWrapper loadAllActiveWHTConfigVo()
	{
		return taxManager.loadAllActiveWHTConfigVo();
	}

	@Override
	public BaseWrapper updateWHTConfigModelWithAuthorization(BaseWrapper baseWrapper) throws FrameworkCheckedException
	{
		try
		{
			return taxManager.updateWHTConfigModelWithAuthorization( baseWrapper);
		}
		catch( Exception e )
		{
			throw frameworkExceptionTranslator.translate( e, FrameworkExceptionTranslator.INSERT_ACTION );
		}
	}

	@Override
	public List<DailyWhtDeductionModel> loadUnsettledWithholdingDeductionList(Date toDate) throws Exception{
		try
		{
			return taxManager.loadUnsettledWithholdingDeductionList(toDate);
		}
		catch( Exception e )
		{
			throw frameworkExceptionTranslator.translate( e, FrameworkExceptionTranslator.FIND_ACTION );
		}
	}

/*	@Override
	public BaseWrapper saveDailyWhtDeduction(BaseWrapper baseWrapper) throws Exception {
		try {
			return taxManager.saveDailyWhtDeduction(baseWrapper);
		} catch (Exception e) {
			throw frameworkExceptionTranslator.translate( e, FrameworkExceptionTranslator.INSERT_ACTION );
		}
	}*/

	@Override
	public List<FEDRuleModel> loadFEDRuleModelList ()
			throws FrameworkCheckedException {
		try{
			return taxManager.loadFEDRuleModelList();
		}catch (Exception ex){
			throw frameworkExceptionTranslator.translate(ex,FrameworkExceptionTranslator.FIND_ACTION);
		}
	}

	public WorkFlowWrapper makeDebitForWHT(DailyWhtDeductionModel model) throws Exception{
		try{
			   WorkFlowWrapper workFlowWrapper = taxManager.makeDebitForWHT(model);
			   creditAccountQueingPreProcessor.startProcessing(workFlowWrapper);
			   return workFlowWrapper;
		}catch (Exception ex){
			throw frameworkExceptionTranslator.translate(ex,FrameworkExceptionTranslator.UPDATE_ACTION);
		}
	}

	public List<WorkFlowWrapper> prepareDailyWHTEntries(List<DateWiseUserWHTAmountViewModel> modelList, Map<Long, WHTConfigModel> configModelMap) throws Exception{
		try{
			return taxManager.prepareDailyWHTEntries(modelList, configModelMap);
		}catch (Exception ex){
			throw ex;
		}
	}
	
	@Override
	public Boolean isTaxRegimeNameUnique( String taxRegimeName, Long taxRegimeId ) throws FrameworkCheckedException
	{
	    try
        {
	        return taxManager.isTaxRegimeNameUnique(taxRegimeName, taxRegimeId);
        }
        catch( Exception e )
        {
            throw frameworkExceptionTranslator.translate( e, FrameworkExceptionTranslator.INSERT_ACTION );
        }
	}

	@Override
	public BaseWrapper loadAndValidateWhtExemption(BaseWrapper baseWrapper) throws Exception {
		try{
			return taxManager.loadAndValidateWhtExemption(baseWrapper);
		}catch (Exception ex){
			throw frameworkExceptionTranslator.translate(ex,FrameworkExceptionTranslator.FIND_ACTION);
		}
	}

	public void setFrameworkExceptionTranslator(
			FrameworkExceptionTranslator frameworkExceptionTranslator) {
		this.frameworkExceptionTranslator = frameworkExceptionTranslator;
	}

	public void setTaxManager(TaxManager taxManager) {
		this.taxManager = taxManager;
	}

	@Override
	public int getAssociatedUsersWithTaxRegime(Long taxRegimeId) throws FrameworkCheckedException {
		try{
			return this.taxManager.getAssociatedUsersWithTaxRegime(taxRegimeId);
		}catch (Exception ex){
			throw frameworkExceptionTranslator.translate(ex,FrameworkExceptionTranslator.FIND_ACTION);
		}
	}

	/*@Override
	public CustomList<TaxRegimeModel> searchActiveTaxRegimeDefaultFEDRates() throws FrameworkCheckedException {
		try{
			return this.taxManager.searchActiveTaxRegimeDefaultFEDRates();
		}catch (Exception ex){
			throw frameworkExceptionTranslator.translate(ex,FrameworkExceptionTranslator.FIND_ACTION);
		}
	}*/
	
	 public void setCreditAccountQueingPreProcessor(
			   CreditAccountQueingPreProcessor creditAccountQueingPreProcessor) {
			  this.creditAccountQueingPreProcessor = creditAccountQueingPreProcessor;
			 }

	@Override
	public void saveOrUpdateWHTDeductionSchedularStatus(
			WHTDeductionSchedularStatusModel wHTDeductionSchedularStatusModel)
			throws Exception {
		try{
			taxManager.saveOrUpdateWHTDeductionSchedularStatus(wHTDeductionSchedularStatusModel);
		}catch (Exception ex){
			throw frameworkExceptionTranslator.translate(ex,FrameworkExceptionTranslator.FIND_ACTION);
		
		}
	}

	@Override
	public List<WHTDeductionSchedularStatusModel> findWHTDeductionMissedEntries(WHTDeductionSchedularStatusModel wHTDeductionSchedularStatusModel)
			throws Exception {
		try{
			return this.taxManager.findWHTDeductionMissedEntries(wHTDeductionSchedularStatusModel);
		}catch (Exception ex){
			throw frameworkExceptionTranslator.translate(ex,FrameworkExceptionTranslator.FIND_ACTION);
		}
	}

	@Override
	public List<WHTDeductionSchedularStatusModel> findWHTDeductionSchedularStatusEntries()
			throws Exception {
		try{
			return this.taxManager.findWHTDeductionSchedularStatusEntries();
		}catch (Exception ex){
			throw frameworkExceptionTranslator.translate(ex,FrameworkExceptionTranslator.FIND_ACTION);
		}
	}

	@Override
	public void updateWhtDeductionSchedulerStatus() throws Exception {
		// TODO Auto-generated method stub
		this.taxManager.updateWhtDeductionSchedulerStatus();
		
	}

	@Override
	public void saveDailyWHTDeductionModels(
			List<WorkFlowWrapper> saveDailyWHTEntriesList) throws Exception {
		try{
			 this.taxManager.saveDailyWHTDeductionModels(saveDailyWHTEntriesList);
		}catch (Exception ex){
			throw ex;
		}
		
	}

	@Override
	public FilerRateConfigModel loadFilerRateConfigModelByFiler(Long filer) {
		try{
			return taxManager.loadFilerRateConfigModelByFiler(filer);
		}catch (Exception ex){
			ex.printStackTrace();
		}

		return null;
	}

}
