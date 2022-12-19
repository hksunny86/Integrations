package com.inov8.microbank.webapp.action.portal.reports;

import com.inov8.framework.common.model.DateRangeHolderModel;
import com.inov8.framework.common.model.PagingHelperModel;
import com.inov8.framework.common.model.SortingOrder;
import com.inov8.framework.common.util.CustomList;
import com.inov8.framework.common.wrapper.ReferenceDataWrapperImpl;
import com.inov8.framework.common.wrapper.SearchBaseWrapper;
import com.inov8.framework.common.wrapper.SearchBaseWrapperImpl;
import com.inov8.framework.server.service.common.ReferenceDataManager;
import com.inov8.framework.webapp.action.BaseFormSearchController;
import com.inov8.microbank.common.model.SegmentModel;
import com.inov8.microbank.common.model.portal.bbcustomers.BbCustomerAccountsViewModel;
import com.inov8.microbank.common.util.CryptographyType;
import com.inov8.microbank.common.util.ReportConstants;
import com.inov8.microbank.server.facade.portal.bbcustomersmodule.BbCustomerAccountsViewFacade;
import com.inov8.microbank.webapp.action.portal.bigreports.ReportCriteriaSessionObject;
import com.inov8.ola.util.AccountConstants;
import com.inov8.ola.util.EncryptionUtil;
import org.apache.commons.validator.GenericValidator;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/** 
 * Created By    : Naseer Ullah <br>
 * Creation Date : Feb 1, 2013 7:29:27 PM<p>
 * Purpose       : <p>
 * Updated By    : <br>
 * Updated Date  : <br>
 * Comments      : <br>
 */
public class BbCustomerAccountsViewController extends BaseFormSearchController
{
    //Autowired
    private BbCustomerAccountsViewFacade bbCustomerAccountsViewFacade;
    private ReferenceDataManager referenceDataManager;

    public BbCustomerAccountsViewController()
    {
        setCommandClass( BbCustomerAccountsViewModel.class );
        setCommandName( "bbCustomerAccountsViewModel" );
    }

    @Override
    protected Map<String,Object> loadReferenceData( HttpServletRequest request ) throws Exception
    {
		/*Integer totalRecordsCount = (Integer) request.getAttribute("totalRecordsCount");
		
		if(totalRecordsCount == null) {

			request.getSession().removeAttribute(ReportCriteriaSessionObject.REPORT_CRITERIA_SESSION_OBJ_BB_CUS_ACC);
		}    	
		
		request.setAttribute(ReportConstants.REPORT_ID, ReportConstants.REPORT_BB_CUSTOMER_REPORT);*/

        Map<String,Object> referenceDataMap = new HashMap<String,Object>();

        SegmentModel segmentModel = new SegmentModel();
        List<SegmentModel> segmentModelList = null;

        segmentModel.setIsActive(true);
        ReferenceDataWrapperImpl referenceDataWrapper = new ReferenceDataWrapperImpl(
                segmentModel, "name", SortingOrder.ASC);
        referenceDataManager.getReferenceData(referenceDataWrapper);
        {
            segmentModelList = referenceDataWrapper.getReferenceDataList();
        }

        referenceDataMap.put("segmentModelList",segmentModelList);

        return referenceDataMap;
    }

    @SuppressWarnings( "unchecked" )
    @Override
    protected ModelAndView onSearch( HttpServletRequest request, HttpServletResponse response, Object model,
                                     PagingHelperModel pagingHelperModel, LinkedHashMap<String, SortingOrder> sortingOrderMap ) throws Exception
    {
		long start = System.currentTimeMillis();
        SearchBaseWrapper wrapper = new SearchBaseWrapperImpl();
        BbCustomerAccountsViewModel modelToSearch = (BbCustomerAccountsViewModel) model;// ).clone();
        DateRangeHolderModel dateRangeHolderModel = new DateRangeHolderModel(
                "accountOpenDate", modelToSearch.getStartDate(),
                modelToSearch.getEndDate());
        wrapper.setBasePersistableModel( modelToSearch );
        wrapper.setPagingHelperModel( pagingHelperModel );
        wrapper.setDateRangeHolderModel( dateRangeHolderModel );

        if( sortingOrderMap.isEmpty() )
        {
            sortingOrderMap.put( "firstName", SortingOrder.ASC );
        }
        wrapper.setSortingOrderMap( sortingOrderMap );

        /*String accountNumber = modelToSearch.getAccountNumber();
        if( !GenericValidator.isBlankOrNull(accountNumber) && !AccountConstants.BULK_PAYMENT_SUNDRY_OLA_ACCOUNT_NUMBER.equals(accountNumber) )
        {
            modelToSearch.setAccountNumber( EncryptionUtil.encryptAccountNo( modelToSearch.getAccountNumber() ) );
        }*/
        //EncryptionUtil.docryptFields( CryptographyType.ENCRYPT, modelToSearch, "cnic", "dob" );
        modelToSearch.setIsDeleted(false);
        wrapper = bbCustomerAccountsViewFacade.searchBbCustomerAccountsView( wrapper );

        CustomList<BbCustomerAccountsViewModel> customList = wrapper.getCustomList();
        List<BbCustomerAccountsViewModel> bbCustomerAccountsViewModelList = null;
        if( customList != null )
        {
            bbCustomerAccountsViewModelList = customList.getResultsetList();
            //decryptBbCustomerAccountsViewModels( bbCustomerAccountsViewModelList );
            //removeDuplicateLastName( bbCustomerAccountsViewModelList );
        }

        //request.getSession().setAttribute(ReportCriteriaSessionObject.REPORT_CRITERIA_SESSION_OBJ_BB_CUS_ACC, new ReportCriteriaSessionObject(wrapper));
		
		Integer totalRecordsCount = 0;
		
		if(pagingHelperModel != null) {
			
			totalRecordsCount = pagingHelperModel.getTotalRecordsCount();
		}		
		
		request.setAttribute("totalRecordsCount", totalRecordsCount);	        
		
		logger.info("\n\n:- File Created "+ ((System.currentTimeMillis() - start)/1000) + ".s\n");
        
        return new ModelAndView( getFormView(), "bbCustomerAccountsViewModelList", bbCustomerAccountsViewModelList );
    }

    private void removeDuplicateLastName(List<BbCustomerAccountsViewModel> bbCAccountsViewModels)
    {
        for(BbCustomerAccountsViewModel model : bbCAccountsViewModels)
        {            
            String firstName = model.getFirstName();
            String lastName = model.getLastName();
            
            // set last name as blank if first name & last name are same
            if(firstName != null && lastName != null)
            {
                if(firstName.trim().equalsIgnoreCase(lastName.trim()))
                {
                    model.setLastName(null);
                }   
            } 
        }   
    }

    private void decryptBbCustomerAccountsViewModels(List<BbCustomerAccountsViewModel> bbCustomerAccountsViewModels)
    {
        for( BbCustomerAccountsViewModel bbCustomerAccountsViewModel : bbCustomerAccountsViewModels )
        {
            try
            {
            	/*if(AccountConstants.BULK_PAYMENT_SUNDRY_OLA_ACCOUNT_ID != bbCustomerAccountsViewModel.getAccountId().longValue())
            	{
            		EncryptionUtil.docryptFields( CryptographyType.DECRYPT, bbCustomerAccountsViewModel, "balance", "cnic", "dob" );
				}*/

                String accountNumber = bbCustomerAccountsViewModel.getAccountNumber();
                if( !GenericValidator.isBlankOrNull(accountNumber) && !AccountConstants.BULK_PAYMENT_SUNDRY_OLA_ACCOUNT_NUMBER.equals(accountNumber) )
                {
                    bbCustomerAccountsViewModel.setAccountNumber( EncryptionUtil.decryptAccountNo( accountNumber ) ) ;
                }
            }
            catch( Exception e )
            {
                log.error( e.getMessage(), e );
            }
        }
    }

    public void setReferenceDataManager(ReferenceDataManager referenceDataManager) {
        this.referenceDataManager = referenceDataManager;
    }
    public void setBbCustomerAccountsViewFacade( BbCustomerAccountsViewFacade bbCustomerAccountsViewFacade )
    {
        this.bbCustomerAccountsViewFacade = bbCustomerAccountsViewFacade;
    }
}
