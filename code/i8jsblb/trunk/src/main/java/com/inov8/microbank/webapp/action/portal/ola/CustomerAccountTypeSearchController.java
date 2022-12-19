package com.inov8.microbank.webapp.action.portal.ola;

import java.util.LinkedHashMap;
import java.util.concurrent.CopyOnWriteArrayList;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.collections.CollectionUtils;
import org.springframework.web.servlet.ModelAndView;

import com.inov8.framework.common.model.PagingHelperModel;
import com.inov8.framework.common.model.SortingOrder;
import com.inov8.framework.common.wrapper.SearchBaseWrapper;
import com.inov8.framework.common.wrapper.SearchBaseWrapperImpl;
import com.inov8.framework.webapp.action.BaseSearchController;
import com.inov8.integration.common.model.AccountTypeViewModel;
import com.inov8.ola.server.facade.AccountFacade;
import com.inov8.ola.util.CustomerAccountTypeConstants;

/** 
 * Created By    : Naseer Ullah <br>
 * Creation Date : Feb 1, 2013 7:29:27 PM<p>
 * Purpose       : <p>
 * Updated By    : <br>
 * Updated Date  : <br>
 * Comments      : <br>
 */
public class CustomerAccountTypeSearchController extends BaseSearchController
{
    //Autowired
    private AccountFacade accountFacade;

    public CustomerAccountTypeSearchController()
    {
        setFilterSearchCommandClass( AccountTypeViewModel.class );
    }

    @Override
    protected ModelAndView onSearch( PagingHelperModel pagingHelperModel, Object model, HttpServletRequest request,
                                         LinkedHashMap<String, SortingOrder> sortingOrderMap ) throws Exception
    {

    	CopyOnWriteArrayList<AccountTypeViewModel> accountTypeViewModelList = null;

        try
        {
            SearchBaseWrapper searchBaseWrapper = new SearchBaseWrapperImpl();
            AccountTypeViewModel accountTypeViewModel = (AccountTypeViewModel) model;
            searchBaseWrapper.setBasePersistableModel( accountTypeViewModel );
            searchBaseWrapper.setPagingHelperModel( pagingHelperModel );
            searchBaseWrapper.setSortingOrderMap( sortingOrderMap );

            searchBaseWrapper = accountFacade.searchAccountTypeView( searchBaseWrapper );
            if( searchBaseWrapper.getCustomList() != null )
            {
            	
            	accountTypeViewModelList = new CopyOnWriteArrayList<AccountTypeViewModel>(searchBaseWrapper.getCustomList().getResultsetList());
            }
        }
        catch( Exception e )
        {
            log.error( e.getMessage(), e );
        }

        return new ModelAndView( getSearchView(), "olaCustomerAccountTypeModelList", accountTypeViewModelList );
    }

    public void setAccountFacade( AccountFacade accountFacade )
    {
        this.accountFacade = accountFacade;
    }

}
