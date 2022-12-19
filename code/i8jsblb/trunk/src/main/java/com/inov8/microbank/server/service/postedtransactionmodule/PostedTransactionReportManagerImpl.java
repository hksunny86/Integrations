package com.inov8.microbank.server.service.postedtransactionmodule;

import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.List;

import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.Restrictions;

import com.inov8.framework.common.exception.FrameworkCheckedException;
import com.inov8.framework.common.model.DateRangeHolderModel;
import com.inov8.framework.common.model.PagingHelperModel;
import com.inov8.framework.common.model.SortingOrder;
import com.inov8.framework.common.util.CustomList;
import com.inov8.framework.common.wrapper.BaseWrapper;
import com.inov8.framework.common.wrapper.SearchBaseWrapper;
import com.inov8.microbank.common.model.AppUserModel;
import com.inov8.microbank.common.model.OperatorUserModel;
import com.inov8.microbank.common.model.postedrransactionreportmodule.IntgTransactionTypeModel;
import com.inov8.microbank.common.model.postedrransactionreportmodule.PostedTransactionNadraViewModel;
import com.inov8.microbank.common.model.postedrransactionreportmodule.PostedTransactionReportModel;
import com.inov8.microbank.common.model.postedrransactionreportmodule.PostedTransactionViewModel;
import com.inov8.microbank.common.model.usergroupmodule.CustomUserPermissionViewModel;
import com.inov8.microbank.common.util.AccessLevelConstants;
import com.inov8.microbank.common.util.IntgTransactionTypeConstantsInterface;
import com.inov8.microbank.common.util.PortalConstants;
import com.inov8.microbank.common.util.UserUtils;
import com.inov8.microbank.server.dao.postedtransactionreportmodule.PostedTransactionNadraViewDao;
import com.inov8.microbank.server.dao.postedtransactionreportmodule.PostedTransactionReportDAO;
import com.inov8.microbank.server.dao.postedtransactionreportmodule.PostedTransactionViewDao;

public class PostedTransactionReportManagerImpl implements PostedTransactionReportManager {

	private PostedTransactionReportDAO postedTransactionReportDAO;
	//Autowired
	private PostedTransactionViewDao postedTransactionViewDao;

	//Autowired
    private PostedTransactionNadraViewDao postedTransactionNadraViewDao;

	public PostedTransactionReportManagerImpl() {
		
	}

	public BaseWrapper createOrUpdatePostedTransactionRequiresNewTransaction(BaseWrapper baseWrapper) throws FrameworkCheckedException 
	{
		try
		{	
		PostedTransactionReportModel postedTransactionReportModel = (PostedTransactionReportModel)baseWrapper.getBasePersistableModel();
		this.postedTransactionReportDAO.saveOrUpdate(postedTransactionReportModel);
		baseWrapper.setBasePersistableModel(postedTransactionReportModel);
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
		return baseWrapper;
	}

	public CustomList<PostedTransactionViewModel> searchPostedTransactionView( SearchBaseWrapper searchBaseWrapper ) throws FrameworkCheckedException
	{
		Criterion criterion=null;  
	    CustomList<PostedTransactionViewModel> customList=null;
	    AppUserModel appUserModel = UserUtils.getCurrentUser();
		OperatorUserModel operatorUser= appUserModel.getOperatorUserIdOperatorUserModel();
		Collection<CustomUserPermissionViewModel> userPermissionList = appUserModel.getUserPermissionList();
	    
		PostedTransactionViewModel exampleInstance = (PostedTransactionViewModel) searchBaseWrapper.getBasePersistableModel();
	    PagingHelperModel pagingHelperModel = searchBaseWrapper.getPagingHelperModel();
	    LinkedHashMap<String, SortingOrder> sortingOrderMap = searchBaseWrapper.getSortingOrderMap();
	    DateRangeHolderModel dateRangeHolderModel = searchBaseWrapper.getDateRangeHolderModel();

	    if(exampleInstance.getIntgTransactionTypeId()==null){
	    	if(operatorUser!=null && !UserUtils.isInPermissionList(userPermissionList,AccessLevelConstants.ADMIN_GP_USER) ){  	
		    	criterion = Restrictions.in("intgTransactionTypeId",IntgTransactionTypeConstantsInterface.CSR_VIEW_TRANS_TYPE_ID);
		    	String excludeProperty=null;
		    	customList =postedTransactionViewDao.findBillPaymentTransactions(criterion,exampleInstance, pagingHelperModel, sortingOrderMap, dateRangeHolderModel, null,excludeProperty);	       
		    }
		    else{	    		    	
		    	customList =postedTransactionViewDao.findByExample(exampleInstance, pagingHelperModel, sortingOrderMap, dateRangeHolderModel );    	
	        }
	    }
	    else{	    	
	    		customList =postedTransactionViewDao.findByExample(exampleInstance, pagingHelperModel, sortingOrderMap, dateRangeHolderModel );	    	
	    }    
	    return customList;
	}
	@Override
	public CustomList<PostedTransactionViewModel> searchPostedTransactionView(PostedTransactionViewModel postedTransactionViewModel) throws FrameworkCheckedException
	{
		Criterion criterion=null;  
	    CustomList<PostedTransactionViewModel> customList=null;
	    AppUserModel appUserModel = UserUtils.getCurrentUser();
		OperatorUserModel operatorUser= appUserModel.getOperatorUserIdOperatorUserModel();
		Collection<CustomUserPermissionViewModel> userPermissionList = appUserModel.getUserPermissionList();
	    
		PostedTransactionViewModel exampleInstance =postedTransactionViewModel;
	   
	    if(exampleInstance.getIntgTransactionTypeId()==null){
	    	if(operatorUser!=null && !UserUtils.isInPermissionList(userPermissionList,AccessLevelConstants.ADMIN_GP_USER) ){  	
		    	criterion = Restrictions.in("intgTransactionTypeId",IntgTransactionTypeConstantsInterface.CSR_VIEW_TRANS_TYPE_ID);
		    	String excludeProperty=null;
		    	customList =postedTransactionViewDao.findBillPaymentTransactions(criterion,exampleInstance, null,null,null, null,excludeProperty);	       
		    }
		    else{	    		    	
		    	customList =postedTransactionViewDao.findByExample(exampleInstance,null,null,PortalConstants.EXACT_CONFIG_HOLDER_MODEL);    	
	        }
	    }
	    else{	    	
	    		customList =postedTransactionViewDao.findByExample(exampleInstance,null,null,PortalConstants.EXACT_CONFIG_HOLDER_MODEL);	    	
	    }    
	    return customList;
	}

	@Override
	public List<PostedTransactionNadraViewModel> searchPostedTransactionNadraView( SearchBaseWrapper searchBaseWrapper ) throws FrameworkCheckedException
	{
	    List<PostedTransactionNadraViewModel> postedTransactionViewModelList = null;

	    PostedTransactionNadraViewModel exampleInstance = (PostedTransactionNadraViewModel) searchBaseWrapper.getBasePersistableModel();
        PagingHelperModel pagingHelperModel = searchBaseWrapper.getPagingHelperModel();
        LinkedHashMap<String, SortingOrder> sortingOrderMap = searchBaseWrapper.getSortingOrderMap();
        DateRangeHolderModel dateRangeHolderModel = searchBaseWrapper.getDateRangeHolderModel();
        CustomList<PostedTransactionNadraViewModel> customList = postedTransactionNadraViewDao.findByExample( exampleInstance, pagingHelperModel, sortingOrderMap, dateRangeHolderModel );
        if( customList != null )
        {
            searchBaseWrapper.setCustomList( customList );
            postedTransactionViewModelList = customList.getResultsetList();
        }
        return postedTransactionViewModelList;
	}

	@Override
	public List<IntgTransactionTypeModel> fetchIntgTransactionTypes( java.util.List<IntgTransactionTypeModel> intgTransactionTypeModel,
	                                                                 String propertyToSortBy, SortingOrder sortingOrder, Long... intgTransactionTypeIds ) throws FrameworkCheckedException
	{
	    return postedTransactionReportDAO.fetchIntgTransactionTypes( intgTransactionTypeModel, propertyToSortBy, sortingOrder, intgTransactionTypeIds );
	}

    public void setPostedTransactionReportDAO( PostedTransactionReportDAO postedTransactionReportDAO )
    {
        this.postedTransactionReportDAO = postedTransactionReportDAO;
    }

	public void setPostedTransactionViewDao( PostedTransactionViewDao postedTransactionViewDao )
    {
        this.postedTransactionViewDao = postedTransactionViewDao;
    }

	public void setPostedTransactionNadraViewDao( PostedTransactionNadraViewDao postedTransactionNadraViewDao )
    {
        this.postedTransactionNadraViewDao = postedTransactionNadraViewDao;
    }

}
