package com.inov8.microbank.webapp.action.portal.transactiondetail;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.validator.GenericValidator;
import org.springframework.web.servlet.ModelAndView;

import com.inov8.framework.common.model.DateRangeHolderModel;
import com.inov8.framework.common.model.PagingHelperModel;
import com.inov8.framework.common.model.SortingOrder;
import com.inov8.framework.common.wrapper.BaseWrapper;
import com.inov8.framework.common.wrapper.BaseWrapperImpl;
import com.inov8.framework.common.wrapper.ReferenceDataWrapper;
import com.inov8.framework.common.wrapper.ReferenceDataWrapperImpl;
import com.inov8.framework.common.wrapper.SearchBaseWrapper;
import com.inov8.framework.common.wrapper.SearchBaseWrapperImpl;
import com.inov8.framework.server.service.common.ReferenceDataManager;
import com.inov8.framework.webapp.action.BaseFormSearchController;
import com.inov8.microbank.common.model.MiniTransactionModel;
import com.inov8.microbank.common.model.MiniTransactionStateModel;
import com.inov8.microbank.common.model.minitransactionmodule.MiniTransactionViewModel;
import com.inov8.microbank.common.util.CommandFieldConstants;
import com.inov8.microbank.common.util.EncryptionUtil;
import com.inov8.microbank.common.util.MiniTransactionStateConstant;
import com.inov8.microbank.server.service.portal.transactiondetail.TransactionDetailManager;


public class MiniTransactionViewSearchController extends BaseFormSearchController
{

	private TransactionDetailManager transactionDetailManager;
	private ReferenceDataManager referenceDataManager;

	public MiniTransactionViewSearchController()
	{
		super.setCommandName("miniTransactionViewModel");
		super.setCommandClass(MiniTransactionViewModel.class);
	}

	@Override
	protected Map<String,Object> loadReferenceData(HttpServletRequest request) throws Exception
	{
		Map<String,Object> referenceDataMap = new HashMap<>();
        try
        {
            String registered = request.getParameter("registered");
            if(registered != null && registered.trim().equals("true")){
                request.setAttribute("registered", "true");
            }
            
            String isHomeReferer = request.getParameter("home");
            if(isHomeReferer != null )
            {
                request.setAttribute("home", isHomeReferer);
            }

            MiniTransactionStateModel miniTransactionStateModel = new MiniTransactionStateModel();
            ReferenceDataWrapper refDataWrapper = new ReferenceDataWrapperImpl( miniTransactionStateModel, "name", SortingOrder.ASC );
            referenceDataManager.getReferenceData( refDataWrapper );
            referenceDataMap.put( "miniTransactionStateList", refDataWrapper.getReferenceDataList() );
        }
        catch( Exception ex )
        {
            log.error( "Error getting miniTransactionStateList data in loadReferenceData", ex );
        }
        return referenceDataMap; 
	}
	
	@Override
    protected Object formBackingObject( HttpServletRequest request ) throws Exception
    {
		MiniTransactionViewModel miniTransactionViewModel = new MiniTransactionViewModel( new DateRangeHolderModel() );
        return miniTransactionViewModel;
    }
	
	@Override
	protected ModelAndView onSearch(HttpServletRequest httpServletRequest,
			HttpServletResponse httpServletResponse,
			Object model,
			PagingHelperModel pagingHelperModel,
			LinkedHashMap<String,SortingOrder> sortingOrderMap) throws Exception {

		boolean searchOnAppUserId = false;
		String requestParam = null;
		String encryptedAppUserId = httpServletRequest.getParameter( "appUserId" );
		/*if( !GenericValidator.isBlankOrNull( encryptedAppUserId  ) )
		{
			requestParam = EncryptionUtil.decryptWithDES( encryptedAppUserId );
			searchOnAppUserId = true;
		}
		else
		{
			requestParam = httpServletRequest.getParameter("customerCnic");
		}*/

		if(GenericValidator.isBlankOrNull( requestParam )){

			MiniTransactionViewModel miniTransactionViewModel = (MiniTransactionViewModel) model;

			// Mark all previous CashWithdrawal transactions as expired........ start
			BaseWrapper bWrapper = new BaseWrapperImpl();
			MiniTransactionModel miniTransactionModel = new MiniTransactionModel();
			/*if( searchOnAppUserId )
			{
				Long appUserId = new Long(requestParam);
				miniTransactionViewModel.setAppUserId( appUserId );

				miniTransactionModel.setAppUserId( appUserId );
				miniTransactionModel.setCommandId(Long.parseLong(CommandFieldConstants.CMD_MINI_CASHOUT));
				miniTransactionModel.setMiniTransactionStateId( MiniTransactionStateConstant.PIN_SENT);
				bWrapper.setBasePersistableModel(miniTransactionModel);
				this.transactionDetailManager.modifyPINSentMiniTransToExpired(bWrapper);
			}
			else
			{
				miniTransactionViewModel.setCustomerCnic( requestParam );
			}*/
			// Mark all previous transactions as expired........ start
			
			SearchBaseWrapper searchBaseWrapper = new SearchBaseWrapperImpl();
			searchBaseWrapper.setPagingHelperModel(pagingHelperModel);
			searchBaseWrapper.setDateRangeHolderModel( new DateRangeHolderModel());
			
			searchBaseWrapper.setBasePersistableModel(miniTransactionViewModel);
	        if(sortingOrderMap.isEmpty()){
	        	sortingOrderMap.put("createdOn", SortingOrder.DESC );
	        }
	        DateRangeHolderModel dateRangeHolderModel = miniTransactionViewModel.getDateRangeHolderModel();
            dateRangeHolderModel.setDatePropertyName( "createdOn" );
            searchBaseWrapper.setDateRangeHolderModel( dateRangeHolderModel );
			searchBaseWrapper.setSortingOrderMap(sortingOrderMap);
			searchBaseWrapper = this.transactionDetailManager.searchMiniTransactionViewList(searchBaseWrapper);
			return new ModelAndView(super.getSuccessView(),
					"miniTransactionViewModelList", searchBaseWrapper.getCustomList().getResultsetList());
			
		}
		else
		{
			pagingHelperModel.setTotalRecordsCount(0);
			return new ModelAndView(super.getSuccessView(),"miniTransactionViewModelList",null);
		}
	}

	public void setTransactionDetailManager(TransactionDetailManager transactionDetailManager){
		this.transactionDetailManager = transactionDetailManager;
	}
	public void setReferenceDataManager(ReferenceDataManager referenceDataManager) {
		this.referenceDataManager = referenceDataManager;
	}
}