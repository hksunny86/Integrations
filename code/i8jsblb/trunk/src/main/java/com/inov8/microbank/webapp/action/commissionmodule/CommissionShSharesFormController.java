package com.inov8.microbank.webapp.action.commissionmodule;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.propertyeditors.CustomNumberEditor;
import org.springframework.validation.BindException;
import org.springframework.web.bind.ServletRequestDataBinder;
import org.springframework.web.bind.ServletRequestUtils;
import org.springframework.web.servlet.ModelAndView;

import com.inov8.framework.common.model.PagingHelperModel;
import com.inov8.framework.common.model.SortingOrder;
import com.inov8.framework.common.wrapper.ReferenceDataWrapper;
import com.inov8.framework.common.wrapper.ReferenceDataWrapperImpl;
import com.inov8.framework.common.wrapper.SearchBaseWrapper;
import com.inov8.framework.common.wrapper.SearchBaseWrapperImpl;
import com.inov8.framework.server.service.common.ReferenceDataManager;
import com.inov8.framework.webapp.action.AdvanceFormController;
import com.inov8.microbank.common.model.CommissionShSharesModel;
import com.inov8.microbank.common.model.ProductModel;
import com.inov8.microbank.common.model.commissionmodule.CommissionShSharesViewModel;
import com.inov8.microbank.common.model.commissionmodule.ExtendedCommissionShSharesViewModel;
import com.inov8.microbank.common.util.CommissionConstantsInterface;
import com.inov8.microbank.common.util.UserUtils;
import com.inov8.microbank.server.facade.CommissionStakeholderFacade;
import com.inov8.microbank.server.facade.ProductFacade;
import com.inov8.microbank.server.service.productmodule.ProductManager;

/**
 * Created By    : Naseer Ullah <br>
 * Creation Date : May 15, 2013 7:01:44 PM<p>
 * Purpose       : <p>
 * Updated By    : <br>
 * Updated Date  : <br>
 * Comments      : <br>
 */
public class CommissionShSharesFormController extends AdvanceFormController
{
	private CommissionStakeholderFacade commissionStakeholderFacade;
    private ReferenceDataManager referenceDataManager;
    private ProductFacade productFacade;
	public CommissionShSharesFormController()
	{
        setCommandName( "commissionShSharesModel" );
        setCommandClass( ExtendedCommissionShSharesViewModel.class );
	}

    @SuppressWarnings( { "unchecked" } )
    @Override
	protected Map<String,Object> loadReferenceData( HttpServletRequest request ) throws Exception
	{
    	
    	Long productId = ServletRequestUtils.getLongParameter(request, "productId");
    	Map<String,Object> referenceDataMap = new HashMap<>();
    	ProductModel productModel = new ProductModel();
    	List<ProductModel> productModelList = null;
		
		if (null == productId) {//Create case
			
			SearchBaseWrapper searchBaseWrapper = new SearchBaseWrapperImpl();
			searchBaseWrapper.setBasePersistableModel(productModel);
			productModelList = productFacade.loadProductsWithNoStakeholderShares();
		
		}else{
			
			productModel.setActive(true);
			ReferenceDataWrapper referenceDataWrapper = new ReferenceDataWrapperImpl(productModel, "name", SortingOrder.ASC);
			referenceDataWrapper.setBasePersistableModel(productModel);
			try
			{
				referenceDataManager.getReferenceData(referenceDataWrapper);
			}
			catch (Exception e)
			{
				logger.error( e.getMessage(), e);
			}
			
			
			if (referenceDataWrapper.getReferenceDataList() != null)
			{
				productModelList = referenceDataWrapper.getReferenceDataList();
			}
			
		}
		
		
		
		referenceDataMap.put("productModelList", productModelList);
    	
		return referenceDataMap;
	}
    
	@Override
	protected ExtendedCommissionShSharesViewModel loadFormBackingObject(HttpServletRequest request)
			throws Exception {

		ExtendedCommissionShSharesViewModel model = new ExtendedCommissionShSharesViewModel();
		
		Long productId = ServletRequestUtils.getLongParameter(request, "productId");
		
		if (null == productId) {//Create case
			
			
			
		}else {//update case
			
			CommissionShSharesViewModel commShSharesModel = new CommissionShSharesViewModel();
			commShSharesModel.setProductId(productId);
			SearchBaseWrapper searchBaseWrapper = new SearchBaseWrapperImpl();
			searchBaseWrapper.setBasePersistableModel(commShSharesModel);
			searchBaseWrapper.setPagingHelperModel(new PagingHelperModel());
			
			List<CommissionShSharesViewModel> commShSharesList = commissionStakeholderFacade.loadCommissionStakeholderSharesViewList(searchBaseWrapper);
			
			if(commShSharesList != null && commShSharesList.size() > 0){
				CommissionShSharesViewModel commShSharesView = commShSharesList.get(0);
				
				model.setProductId(commShSharesView.getProductId());
				model.setBank(commShSharesView.getBank());
				
				model.setAgent1(commShSharesView.getAgent1());
				model.setAgent2(commShSharesView.getAgent2());
				model.setFranchise1(commShSharesView.getFranchise1());
				model.setFranchise2(commShSharesView.getFranchise2());
				
				if(commShSharesView.getFed() != null){
					model.setIsFedApplicable(true);
				}
				
				if(commShSharesView.getWht() != null){
					model.setIsWhtApplicable(true);
				}
			}
			
		}
		return model;
	}

	@Override
	protected ModelAndView onCreate(HttpServletRequest request,
			HttpServletResponse response, Object object, BindException arg3)
			throws Exception {
		ExtendedCommissionShSharesViewModel model = (ExtendedCommissionShSharesViewModel)object;
		
		List<CommissionShSharesModel> commShSharesList = new ArrayList<CommissionShSharesModel>();
		
		if(model.getBank() != null){
			commShSharesList.add(createCommissionShSharesModel(model.getProductId(), CommissionConstantsInterface.BANK_STAKE_HOLDER_ID, model.getBank()));
		}
		
		if(model.getAgent1() != null){
			commShSharesList.add(createCommissionShSharesModel(model.getProductId(), CommissionConstantsInterface.AGENT1_STAKE_HOLDER_ID, model.getAgent1()));
		}
		
		if(model.getAgent2() != null){
			commShSharesList.add(createCommissionShSharesModel(model.getProductId(), CommissionConstantsInterface.AGENT2_STAKE_HOLDER_ID, model.getAgent2()));
		}
		
		if(model.getFranchise1() != null){
			commShSharesList.add(createCommissionShSharesModel(model.getProductId(), CommissionConstantsInterface.FRANCHISE1_STAKE_HOLDER_ID, model.getFranchise1()));
		}
		
		if(model.getFranchise2() != null){
			commShSharesList.add(createCommissionShSharesModel(model.getProductId(), CommissionConstantsInterface.FRANCHISE2_STAKE_HOLDER_ID, model.getFranchise2()));
		}
		
		if(model.getIsFedApplicable()){
			commShSharesList.add(createCommissionShSharesModel(model.getProductId(), CommissionConstantsInterface.FED_STAKE_HOLDER_ID, CommissionConstantsInterface.DEFAULT_FED_VALUE));
		}
		
		if(model.getIsWhtApplicable()){
			commShSharesList.add(createCommissionShSharesModel(model.getProductId(), CommissionConstantsInterface.WHT_STAKE_HOLDER_ID, CommissionConstantsInterface.DEFAULT_WHT_VALUE));
		}
		
		commissionStakeholderFacade.saveCommissionShShares(commShSharesList);
		this.saveMessage(request, "Commission Stakeholder Shares saved successfully");
		ModelAndView modelAndView = new ModelAndView(this.getSuccessView());
		return modelAndView;
	}
	
	

	@Override
	protected ModelAndView onUpdate(HttpServletRequest request,
			HttpServletResponse response, Object object, BindException errors)
			throws Exception {
		
		ExtendedCommissionShSharesViewModel viewModel = (ExtendedCommissionShSharesViewModel)object;
		
		
		CommissionShSharesModel searchModel = new CommissionShSharesModel();
		searchModel.setProductId(viewModel.getProductId());
		SearchBaseWrapper wrapper = new SearchBaseWrapperImpl();
		wrapper.setBasePersistableModel(searchModel);
		
		List<CommissionShSharesModel> existingSharesList = commissionStakeholderFacade.loadCommissionShSharesList(wrapper);
		List<Long> removeSharesList = new ArrayList<Long>();
		
		if(viewModel.getBank() != null){
			updateCommissionShSharesModel(existingSharesList, viewModel.getBank(), CommissionConstantsInterface.BANK_STAKE_HOLDER_ID);
		}else{
			if(isExistingCommissionShShare(existingSharesList, CommissionConstantsInterface.BANK_STAKE_HOLDER_ID)){
				removeSharesList.add(getCommissionShShareIdByStakeholderId(existingSharesList, CommissionConstantsInterface.BANK_STAKE_HOLDER_ID));
			}
		}
				
		if(viewModel.getAgent1() != null){
			updateCommissionShSharesModel(existingSharesList, viewModel.getAgent1(), CommissionConstantsInterface.AGENT1_STAKE_HOLDER_ID);
		}else{
			if(isExistingCommissionShShare(existingSharesList, CommissionConstantsInterface.AGENT1_STAKE_HOLDER_ID)){
				removeSharesList.add(getCommissionShShareIdByStakeholderId(existingSharesList, CommissionConstantsInterface.AGENT1_STAKE_HOLDER_ID));
			}
		}
		
		if(viewModel.getAgent2() != null){
			updateCommissionShSharesModel(existingSharesList, viewModel.getAgent2(), CommissionConstantsInterface.AGENT2_STAKE_HOLDER_ID);
		}else{
			if(isExistingCommissionShShare(existingSharesList, CommissionConstantsInterface.AGENT2_STAKE_HOLDER_ID)){
				removeSharesList.add(getCommissionShShareIdByStakeholderId(existingSharesList, CommissionConstantsInterface.AGENT2_STAKE_HOLDER_ID));
			}
		}
		
		if(viewModel.getFranchise1() != null){
			updateCommissionShSharesModel(existingSharesList, viewModel.getFranchise1(), CommissionConstantsInterface.FRANCHISE1_STAKE_HOLDER_ID);
		}else{
			if(isExistingCommissionShShare(existingSharesList, CommissionConstantsInterface.FRANCHISE1_STAKE_HOLDER_ID)){
				removeSharesList.add(getCommissionShShareIdByStakeholderId(existingSharesList, CommissionConstantsInterface.FRANCHISE1_STAKE_HOLDER_ID));
			}
		}
		
		if(viewModel.getFranchise2() != null){
			updateCommissionShSharesModel(existingSharesList, viewModel.getFranchise2(), CommissionConstantsInterface.FRANCHISE2_STAKE_HOLDER_ID);
		}else{
			if(isExistingCommissionShShare(existingSharesList, CommissionConstantsInterface.FRANCHISE2_STAKE_HOLDER_ID)){
				removeSharesList.add(getCommissionShShareIdByStakeholderId(existingSharesList, CommissionConstantsInterface.FRANCHISE2_STAKE_HOLDER_ID));
			}
		}
		
		
		if(viewModel.getIsFedApplicable()){
			updateCommissionShSharesModel(existingSharesList, CommissionConstantsInterface.DEFAULT_FED_VALUE, CommissionConstantsInterface.FED_STAKE_HOLDER_ID);
		}else{
			if(isExistingCommissionShShare(existingSharesList, CommissionConstantsInterface.FED_STAKE_HOLDER_ID)){
				removeSharesList.add(getCommissionShShareIdByStakeholderId(existingSharesList, CommissionConstantsInterface.FED_STAKE_HOLDER_ID));
			}
		}
		
		if(viewModel.getIsWhtApplicable()){
			updateCommissionShSharesModel(existingSharesList, CommissionConstantsInterface.DEFAULT_WHT_VALUE, CommissionConstantsInterface.WHT_STAKE_HOLDER_ID);
		}else{
			if(isExistingCommissionShShare(existingSharesList, CommissionConstantsInterface.WHT_STAKE_HOLDER_ID)){
				removeSharesList.add(getCommissionShShareIdByStakeholderId(existingSharesList, CommissionConstantsInterface.WHT_STAKE_HOLDER_ID));
			}
		}
		
		boolean successView = false;
		boolean saved = successView =commissionStakeholderFacade.saveCommissionShShares(existingSharesList);
		
		if(saved){
			if(removeSharesList.size() > 0){
				commissionStakeholderFacade.removeCommissionShSharesByShShareIds(removeSharesList);
			}
		}
		
		ModelAndView modelAndView = null;
		
		if (successView) {
			modelAndView = new ModelAndView(this.getSuccessView());
			this.saveMessage(request, "Commission Stakeholder Shares updated successfully");
		}else{
			this.saveMessage(request, "Error in updating Commission Stakeholder Shares.");
			return super.showForm(request, response, errors);
		}
		
		return modelAndView;
		
	}

	private CommissionShSharesModel createCommissionShSharesModel(Long productId, Long commissionStakeholderId, Double share){
		CommissionShSharesModel bankShareModel = new CommissionShSharesModel();
		bankShareModel.setCommissionShare(share);
		bankShareModel.setCommissionStakeholderId(commissionStakeholderId);
		bankShareModel.setProductId(productId);
		bankShareModel.setCreatedBy(UserUtils.getCurrentUser().getAppUserId());
		bankShareModel.setUpdatedBy(UserUtils.getCurrentUser().getAppUserId());
		bankShareModel.setUpdatedOn(new Date());
		bankShareModel.setCreatedOn(new Date());
		
		return bankShareModel;
	}
	
	private void updateCommissionShSharesModel(List<CommissionShSharesModel> existingSharesList, Double share, Long commissionStakeholderId){
		boolean isShareFound = false;
		for(CommissionShSharesModel model : existingSharesList){
			if(model.getCommissionStakeholderId().longValue() == commissionStakeholderId.longValue()){
				model.setCommissionShare(share);
				model.setUpdatedBy(UserUtils.getCurrentUser().getAppUserId());
				model.setUpdatedOn(new Date());
				isShareFound = true;
				break;
			}
		}
		
		if( ! isShareFound){
			existingSharesList.add(createCommissionShSharesModel(existingSharesList.get(0).getProductId(), commissionStakeholderId, share));
		}
		
	}
	
	private boolean isExistingCommissionShShare(List<CommissionShSharesModel> existingSharesList, Long stekeholderId){
		boolean isShareFound = false;
		for(CommissionShSharesModel model : existingSharesList){
			if(model.getCommissionStakeholderId().longValue() == stekeholderId.longValue()){
				isShareFound = true;
				break;
			}
		}
		
		return isShareFound;
	}
	
	private Long getCommissionShShareIdByStakeholderId(List<CommissionShSharesModel> existingSharesList, Long stekeholderId){
		Long stakeholderShareId = null;
		for(CommissionShSharesModel model : existingSharesList){
			if(model.getCommissionStakeholderId().longValue() == stekeholderId.longValue()){
				stakeholderShareId = model.getCommissionShSharesId();
				break;
			}
		}
		
		return stakeholderShareId;
	}
	
   /* protected void initBinder(HttpServletRequest request, ServletRequestDataBinder binder) {
    	super.initBinder(request, binder);
        NumberFormat numFormat = new DecimalFormat("##");
        binder.registerCustomEditor(Double.class, new CustomNumberEditor(Double.class, numFormat, true));
    }*/

	public void setReferenceDataManager(ReferenceDataManager referenceDataManager) {
		this.referenceDataManager = referenceDataManager;
	}
	
	public void setCommissionStakeholderFacade( CommissionStakeholderFacade commissionStakeholderFacade )
    {
        this.commissionStakeholderFacade = commissionStakeholderFacade;
    }

	public void setProductFacade(ProductFacade productManager) {
		this.productFacade = productManager;
	}
}
