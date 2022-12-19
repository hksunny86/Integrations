package com.inov8.microbank.webapp.action.portal.transactiondetaili8module;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.SortedMap;
import java.util.SortedSet;
import java.util.TreeMap;
import java.util.TreeSet;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.validator.GenericValidator;
import org.springframework.web.servlet.ModelAndView;

import com.inov8.framework.common.model.DateRangeHolderModel;
import com.inov8.framework.common.model.PagingHelperModel;
import com.inov8.framework.common.model.SortingOrder;
import com.inov8.framework.common.util.CustomList;
import com.inov8.framework.common.wrapper.ReferenceDataWrapper;
import com.inov8.framework.common.wrapper.ReferenceDataWrapperImpl;
import com.inov8.framework.common.wrapper.SearchBaseWrapper;
import com.inov8.framework.common.wrapper.SearchBaseWrapperImpl;
import com.inov8.framework.server.service.common.ReferenceDataManager;
import com.inov8.framework.webapp.action.BaseFormSearchController;
import com.inov8.microbank.common.model.ProductModel;
import com.inov8.microbank.common.model.portal.inovtransactiondetailmodule.ExtendedTransactionDetailPortalListModel;
import com.inov8.microbank.common.model.portal.inovtransactiondetailmodule.TransactionDetailPortalListModel;
import com.inov8.microbank.server.facade.portal.transactiondetaili8module.TransactionDetailI8Facade;

public class BillPaymentTransactionsSearchController extends BaseFormSearchController
{
    private static final Long SUPPLIER_BILL_PAYMENT_ID = 50063L;

	private ReferenceDataManager referenceDataManager;
	private TransactionDetailI8Facade transactionDetailI8Facade;

	public BillPaymentTransactionsSearchController()
	{
        super.setCommandName( "extendedTransactionDetailPortalListModel" );
        super.setCommandClass( ExtendedTransactionDetailPortalListModel.class );
	}

	protected Map<String,Object> loadReferenceData( HttpServletRequest request ) throws Exception
	{
	    if( log.isDebugEnabled() )
	    {
	        log.debug("Inside reference data");
	    }

	    Map<String,Object> referenceDataMap = new HashMap<String,Object>();
		ProductModel productModel = null;
		List<ProductModel> productModelList = null;

        productModel = new ProductModel();
        productModel.setSupplierId( SUPPLIER_BILL_PAYMENT_ID );
        productModel.setActive( true );
        ReferenceDataWrapper referenceDataWrapper = new ReferenceDataWrapperImpl( productModel, "name", SortingOrder.ASC );
        referenceDataManager.getReferenceData( referenceDataWrapper );
        if( referenceDataWrapper.getReferenceDataList() != null )
        {
            productModelList = referenceDataWrapper.getReferenceDataList();

            SortedSet<String> billTypeSet = new TreeSet<String>();
            SortedSet<String> billCompanySet = new TreeSet<String>();
            SortedMap<String, SortedSet<String>> billTypeAndCompaniesMap = new TreeMap<String, SortedSet<String>>();

			for (ProductModel product : productModelList) {
				String billType = product.getBillType();
				if (!GenericValidator.isBlankOrNull(billType)) {
					billTypeSet.add(billType);

					if (!billTypeAndCompaniesMap.containsKey(billType)) {
						billTypeAndCompaniesMap.put(billType, new TreeSet<String>());
					}

					String billCompany = product.getName();
					billCompanySet.add(billCompany);

					SortedSet<String> billCompanies = billTypeAndCompaniesMap.get(billType);
					if (null != billCompanies) {
						billCompanies.add(billCompany);
					}
                }
            }

            StringBuilder billTypeAndCompanyCsvBuilder = new StringBuilder(); //just like query string seperated by comma(,) instead of ampersand(&)  
            for( String billType : billTypeAndCompaniesMap.keySet() )
            {
                for( String company : billTypeAndCompaniesMap.get( billType ) )
                {
                    billTypeAndCompanyCsvBuilder.append( billType ).append( '=' ).append( company ).append( ',' );
                }
            }
            int lastCharIdx = billTypeAndCompanyCsvBuilder.length() -1;
            if( billTypeAndCompanyCsvBuilder.charAt( lastCharIdx ) == ',' )
            {
                billTypeAndCompanyCsvBuilder.deleteCharAt( lastCharIdx ); //delete trailing/extra comma
            }

            referenceDataMap.put( "billTypeAndCompanyCsv", billTypeAndCompanyCsvBuilder.toString() );
            referenceDataMap.put( "billCompanySet", billCompanySet );
            referenceDataMap.put( "billTypeSet", billTypeSet );
        }
		referenceDataMap.put("productModelList", productModelList);

	    return referenceDataMap;
	}

	protected ModelAndView onSearch( HttpServletRequest request, HttpServletResponse response,
			Object model, PagingHelperModel pagingHelperModel, LinkedHashMap<String,SortingOrder> sortingOrderMap ) throws Exception
	{
		SearchBaseWrapper searchBaseWrapper = new SearchBaseWrapperImpl();
        searchBaseWrapper.setSortingOrderMap( sortingOrderMap );
        searchBaseWrapper.setPagingHelperModel( pagingHelperModel );

		ExtendedTransactionDetailPortalListModel extendedTransactionDetailPortalListModel = (ExtendedTransactionDetailPortalListModel) model;
		extendedTransactionDetailPortalListModel.setSupplierId( SUPPLIER_BILL_PAYMENT_ID );
		
		DateRangeHolderModel dateRangeHolderModel = new DateRangeHolderModel( "createdOn",
		    extendedTransactionDetailPortalListModel.getStartDate(), extendedTransactionDetailPortalListModel.getEndDate() );

		searchBaseWrapper.setBasePersistableModel( (TransactionDetailPortalListModel)extendedTransactionDetailPortalListModel );
		searchBaseWrapper.setDateRangeHolderModel( dateRangeHolderModel );

		//sorting order 
		if( sortingOrderMap.isEmpty() )
		{
			sortingOrderMap.put( "createdOn", SortingOrder.DESC );
		}

		searchBaseWrapper = this.transactionDetailI8Facade.searchTransactionDetailForI8( searchBaseWrapper );
		CustomList<TransactionDetailPortalListModel> customList = searchBaseWrapper.getCustomList();

		List<TransactionDetailPortalListModel> list = null;
        if( customList != null )
		{
			list = customList.getResultsetList();
		}
		return new ModelAndView( getSuccessView(), "transactionDetailPortalList", list );
	}

	public void setReferenceDataManager( ReferenceDataManager referenceDataManager )
	{
		this.referenceDataManager = referenceDataManager;
	}

	public void setTransactionDetailI8Facade( TransactionDetailI8Facade transactionDetailI8Facade )
    {
        this.transactionDetailI8Facade = transactionDetailI8Facade;
    }

}
