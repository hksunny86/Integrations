package com.inov8.microbank.webapp.action.commissionmodule;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.inov8.microbank.common.util.UserTypeConstantsInterface;
import com.inov8.microbank.common.util.UserUtils;
import org.apache.commons.validator.GenericValidator;
import org.springframework.web.servlet.ModelAndView;

import com.inov8.framework.common.model.PagingHelperModel;
import com.inov8.framework.common.model.SortingOrder;
import com.inov8.framework.common.wrapper.ReferenceDataWrapper;
import com.inov8.framework.common.wrapper.ReferenceDataWrapperImpl;
import com.inov8.framework.common.wrapper.SearchBaseWrapper;
import com.inov8.framework.common.wrapper.SearchBaseWrapperImpl;
import com.inov8.framework.webapp.action.BaseFormSearchController;
import com.inov8.microbank.common.model.ProductModel;
import com.inov8.microbank.common.model.SupplierModel;
import com.inov8.microbank.common.model.commissionmodule.CommissionShSharesViewModel;
import com.inov8.microbank.server.facade.CommissionStakeholderFacade;
import com.inov8.microbank.server.facade.CommonFacade;

/**
 * Created By    : Naseer Ullah <br>
 * Creation Date : May 15, 2013 7:01:44 PM<p>
 * Purpose       : <p>
 * Updated By    : <br>
 * Updated Date  : <br>
 * Comments      : <br>
 */
public class CommissionShSharesSearchController extends BaseFormSearchController
{
	private CommissionStakeholderFacade commissionStakeholderFacade;
    private CommonFacade commonFacade;

	public CommissionShSharesSearchController()
	{
        setCommandName( "commissionShSharesViewModel" );
        setCommandClass( CommissionShSharesViewModel.class );
	}

    @SuppressWarnings( "unchecked" )
    @Override
	protected Map<String,Object> loadReferenceData( HttpServletRequest request ) throws Exception
	{
        Map<String,Object> referenceDataMap = new HashMap<String,Object>(2);
        List<SupplierModel> supplierModelList = null;
        List<ProductModel> productModelList = null;

        String strSupplierId = request.getParameter("supplierId");

        SupplierModel supplierModel = new SupplierModel();
        supplierModel.setActive(true);
        ReferenceDataWrapper referenceDataWrapper = new ReferenceDataWrapperImpl( supplierModel, "name", SortingOrder.ASC );

        try
        {
            commonFacade.getReferenceData( referenceDataWrapper );

            if( referenceDataWrapper.getReferenceDataList() != null )
            {
                supplierModelList = referenceDataWrapper.getReferenceDataList();
            }

            if( !GenericValidator.isBlankOrNull( strSupplierId ) )
            {
                Long supplierId = Long.parseLong( strSupplierId );
                ProductModel productModel = new ProductModel();
                productModel.setSupplierId( supplierId );
                productModel.setActive( true );
                referenceDataWrapper = new ReferenceDataWrapperImpl( productModel, "name", SortingOrder.ASC );
                commonFacade.getReferenceData( referenceDataWrapper );
                if ( referenceDataWrapper.getReferenceDataList() != null )
                {
                    productModelList = referenceDataWrapper.getReferenceDataList();
                }
            }
        }
        catch(Exception e)
        {
            log.error( e.getMessage(), e );
        }
        referenceDataMap.put( "supplierModelList", supplierModelList );
        referenceDataMap.put( "productModelList", productModelList );

    	return referenceDataMap;
	}
    
    @Override
	protected ModelAndView onSearch(HttpServletRequest req, HttpServletResponse res, Object model, PagingHelperModel pagingHelperModel,
			                         LinkedHashMap<String, SortingOrder> sortingOrderMap) throws Exception 
    {
		SearchBaseWrapper searchBaseWrapper = new SearchBaseWrapperImpl();
		searchBaseWrapper.setPagingHelperModel( pagingHelperModel );
		searchBaseWrapper.setSortingOrderMap( sortingOrderMap );

		CommissionShSharesViewModel commissionShSharesViewModel = (CommissionShSharesViewModel) model;
		if(UserUtils.getCurrentUser().getAppUserTypeId().equals(UserTypeConstantsInterface.MNO))
		    commissionShSharesViewModel.setMnoId(50028L);
        searchBaseWrapper.setBasePersistableModel( commissionShSharesViewModel );

        List<CommissionShSharesViewModel> commShSharesList = commissionStakeholderFacade.loadCommissionStakeholderSharesViewList(searchBaseWrapper);;
		return new ModelAndView( getSuccessView(), "commShSharesViewModelList", commShSharesList );
	}

	public void setCommissionStakeholderFacade( CommissionStakeholderFacade commissionStakeholderFacade )
    {
        this.commissionStakeholderFacade = commissionStakeholderFacade;
    }

	public void setCommonFacade( CommonFacade commonFacade )
    {
        this.commonFacade = commonFacade;
    }
	
}
