package com.inov8.microbank.server.facade.postedtransactionreportmodule;

import java.util.List;

import com.inov8.framework.common.exception.FrameworkCheckedException;
import com.inov8.framework.common.exception.FrameworkExceptionTranslator;
import com.inov8.framework.common.model.SortingOrder;
import com.inov8.framework.common.util.CustomList;
import com.inov8.framework.common.wrapper.BaseWrapper;
import com.inov8.framework.common.wrapper.SearchBaseWrapper;
import com.inov8.microbank.common.model.postedrransactionreportmodule.IntgTransactionTypeModel;
import com.inov8.microbank.common.model.postedrransactionreportmodule.PostedTransactionNadraViewModel;
import com.inov8.microbank.common.model.postedrransactionreportmodule.PostedTransactionViewModel;
import com.inov8.microbank.server.service.postedtransactionmodule.PostedTransactionReportManager;

public class PostedTransactionReportFacadeImpl implements PostedTransactionReportFacade 
{
	private PostedTransactionReportManager postedTransactionReportManager;
	private FrameworkExceptionTranslator frameworkExceptionTranslator;
	
	
	public void setFrameworkExceptionTranslator(
			FrameworkExceptionTranslator frameworkExceptionTranslator) {
		this.frameworkExceptionTranslator = frameworkExceptionTranslator;
	}


	public void setPostedTransactionReportManager ( PostedTransactionReportManager postedTransactionReportManager) 
	{
		this.postedTransactionReportManager = postedTransactionReportManager;
	}

	public BaseWrapper createOrUpdatePostedTransactionRequiresNewTransaction(BaseWrapper baseWrapper) throws FrameworkCheckedException 
	{
		try
	    {
	      this.postedTransactionReportManager.createOrUpdatePostedTransactionRequiresNewTransaction(baseWrapper);
	    }
	    catch (Exception ex)
	    {
	      throw this.frameworkExceptionTranslator.translate(ex, FrameworkExceptionTranslator.INSERT_ACTION);
	    }
	    return baseWrapper;
	}

	public CustomList<PostedTransactionViewModel> searchPostedTransactionView( SearchBaseWrapper searchBaseWrapper ) throws FrameworkCheckedException
	{
	    CustomList<PostedTransactionViewModel> customList = null;
	    try
        {
            customList = this.postedTransactionReportManager.searchPostedTransactionView( searchBaseWrapper );
        }
        catch (Exception ex)
        {
            throw this.frameworkExceptionTranslator.translate( ex, FrameworkExceptionTranslator.FIND_ACTION );
        }
        return customList;
	}

	@Override
	public List<PostedTransactionNadraViewModel> searchPostedTransactionNadraView( SearchBaseWrapper searchBaseWrapper ) throws FrameworkCheckedException
	{
	    List<PostedTransactionNadraViewModel> postedTransactionNadraViewModelList = null;
	    try
        {
	        postedTransactionNadraViewModelList = this.postedTransactionReportManager.searchPostedTransactionNadraView( searchBaseWrapper );
        }
        catch( Exception ex )
        {
            throw this.frameworkExceptionTranslator.translate( ex, FrameworkExceptionTranslator.FIND_ACTION );
        }
	    return postedTransactionNadraViewModelList;
	}

	@Override
	public List<IntgTransactionTypeModel> fetchIntgTransactionTypes(java.util.List<IntgTransactionTypeModel> intgTransactionTypeModel,
	                                                                 String propertyToSortBy, SortingOrder sortingOrder, Long[] intgTransactionTypeIds ) throws FrameworkCheckedException
	{
	    List<IntgTransactionTypeModel> intgTransactionTypeModels = null;
	    try
        {
            intgTransactionTypeModels = this.postedTransactionReportManager.fetchIntgTransactionTypes( intgTransactionTypeModel, propertyToSortBy, sortingOrder, intgTransactionTypeIds );
        }
        catch( Exception ex )
        {
            throw this.frameworkExceptionTranslator.translate( ex, FrameworkExceptionTranslator.FIND_ACTION );
        }
	    return intgTransactionTypeModels;
	}


	@Override
	public CustomList<PostedTransactionViewModel> searchPostedTransactionView(
			PostedTransactionViewModel postedTransactionViewModel)
			throws FrameworkCheckedException {
		
		return postedTransactionReportManager.searchPostedTransactionView(postedTransactionViewModel);
	}
}
