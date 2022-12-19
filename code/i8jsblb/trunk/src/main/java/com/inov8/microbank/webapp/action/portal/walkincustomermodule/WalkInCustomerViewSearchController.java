package com.inov8.microbank.webapp.action.portal.walkincustomermodule;

import com.inov8.framework.common.model.DateRangeHolderModel;
import com.inov8.framework.common.model.PagingHelperModel;
import com.inov8.framework.common.model.SortingOrder;
import com.inov8.framework.common.util.CustomList;
import com.inov8.framework.common.wrapper.BaseWrapper;
import com.inov8.framework.common.wrapper.BaseWrapperImpl;
import com.inov8.framework.common.wrapper.SearchBaseWrapper;
import com.inov8.framework.common.wrapper.SearchBaseWrapperImpl;
import com.inov8.framework.webapp.action.BaseFormSearchController;
import com.inov8.integration.common.model.AccountHolderModel;
import com.inov8.integration.common.model.AccountModel;
import com.inov8.integration.common.model.LimitModel;
import com.inov8.microbank.common.model.portal.walkincustomermodule.ExtendedWalkInCustomerViewModel;
import com.inov8.microbank.common.model.portal.walkincustomermodule.WalkInCustomerViewModel;
import com.inov8.microbank.common.util.PortalDateUtils;
import com.inov8.microbank.server.facade.portal.walkincustomermodule.WalkInCustomerViewFacade;
import com.inov8.microbank.server.service.securitymodule.AppUserManager;
import com.inov8.ola.server.dao.ledger.LedgerDAO;
import com.inov8.ola.server.service.account.AccountManager;
import com.inov8.ola.server.service.limit.LimitManager;
import com.inov8.ola.util.LimitTypeConstants;
import com.inov8.ola.util.TransactionTypeConstants;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.*;

/** 
 * Created By    : Naseer Ullah <br>
 * Creation Date : Aug 1, 2012 6:40:46 PM<p>
 * Purpose       : <p>
 * Updated By    : <br>
 * Updated Date  : <br>
 * Comments      : <br>
 */
public class WalkInCustomerViewSearchController extends BaseFormSearchController
{
    private WalkInCustomerViewFacade walkInCustomerViewFacade;
    private LimitManager limitManager;
    private LedgerDAO ledgerDAO;
    private AccountManager accountManager;
    private AppUserManager appUserManager;


    public WalkInCustomerViewSearchController()
    {
        setCommandName( "extendedWalkInCustomerViewModel" );
        setCommandClass( ExtendedWalkInCustomerViewModel.class );
    }

    @Override
    protected Map<String,Object> loadReferenceData( HttpServletRequest req ) throws Exception
    {
        return null;
    }

    @Override
    protected ModelAndView onSearch( HttpServletRequest req, HttpServletResponse resp, Object model,
                                     PagingHelperModel pagingHelperModel,
                                     LinkedHashMap<String, SortingOrder> sortingOrderMap ) throws Exception
    {
        SearchBaseWrapper wrapper = new SearchBaseWrapperImpl();
        ExtendedWalkInCustomerViewModel extendedModel = (ExtendedWalkInCustomerViewModel) model;

        DateRangeHolderModel dateRangeHolderModel = new DateRangeHolderModel( "createdOn", extendedModel.getStartDate(), extendedModel.getEndDate() );
        wrapper.setDateRangeHolderModel( dateRangeHolderModel );

        wrapper.setBasePersistableModel( (WalkInCustomerViewModel) extendedModel );
        wrapper.setPagingHelperModel( pagingHelperModel );

        if( sortingOrderMap.isEmpty() )
        {
            sortingOrderMap.put("createdOn", SortingOrder.DESC );
        }
        wrapper.setSortingOrderMap( sortingOrderMap );
        Date transactionDateTime=new Date();
       /* ReTagMobileCnicHistoryModel reTagMobileCnicHistoryModel=new ReTagMobileCnicHistoryModel();
        reTagMobileCnicHistoryModel.setCurrentMobileNo(extendedModel.getMobileNo());
        SearchBaseWrapper searchBaseWrapper=new SearchBaseWrapperImpl();
        searchBaseWrapper.setBasePersistableModel(reTagMobileCnicHistoryModel);
        searchBaseWrapper=appUserManager.loadRetagMobileCnicHistory(searchBaseWrapper);
        CustomList<ReTagMobileCnicHistoryModel> hislist =searchBaseWrapper.getCustomList();
        if(null!=hislist && hislist.getResultsetList().size()>0){

        }*/

        //Calculate Debit and Credit limit
        CustomList<WalkInCustomerViewModel> list = walkInCustomerViewFacade.searchWalkInCustomerView( wrapper );
        List<WalkInCustomerViewModel> l1=list.getResultsetList();
        List<ExtendedWalkInCustomerViewModel> l2=new ArrayList<>();
        LimitModel nonBvsCreditLimitModel =limitManager.getLimitByTransactionType(TransactionTypeConstants.CREDIT, LimitTypeConstants.THROUGHPUT, 9L);
        LimitModel nonBvsdebitLimitModel =limitManager.getLimitByTransactionType(TransactionTypeConstants.DEBIT, LimitTypeConstants.THROUGHPUT, 9L);
        LimitModel bvsCreditLimitModel =limitManager.getLimitByTransactionType(TransactionTypeConstants.CREDIT, LimitTypeConstants.BVS_THROUGHPUT, 9L);
        LimitModel bvsdebitLimitModel =limitManager.getLimitByTransactionType(TransactionTypeConstants.DEBIT, LimitTypeConstants.BVS_THROUGHPUT, 9L);
        Double nonBvsCredit,nonBvsDebit;
        if(nonBvsdebitLimitModel.getMaximum()!=null){
            nonBvsDebit=nonBvsdebitLimitModel.getMaximum();
        }
        else{
            nonBvsDebit=0.0;
        }
        if(nonBvsCreditLimitModel.getMaximum()!=null){
            nonBvsCredit=nonBvsCreditLimitModel.getMaximum();
        }
        else{
            nonBvsCredit=0.0;
        }


        for (WalkInCustomerViewModel vm:l1
             ) {
            ExtendedWalkInCustomerViewModel ex=new ExtendedWalkInCustomerViewModel();
            ex.setAppUserId(vm.getAppUserId());
            ex.setCnic(vm.getCnic());
            ex.setCreatedOn(vm.getCreatedOn());
            ex.setMobileNo(vm.getMobileNo());
            ex.setLastTransactionDate(vm.getLastTransactionDate());
            ex.setNoOfTransactions(vm.getNoOfTransactions());
            ex.setRegisteredBy(vm.getRegisteredBy());
            ex.setTransactionsAsSender(vm.getTransactionsAsSender());
            ex.setWalkInCustomerId(vm.getWalkInCustomerId());
            ex.setTransactionsAsReceiver(vm.getTransactionsAsReceiver());
            Double creditBalance=0.0,debitBalance=0.0;

            AccountModel accountModel = new AccountModel() ;
            AccountHolderModel accountHolderModel = new AccountHolderModel() ;
            accountHolderModel.setCnic(vm.getCnic()+"-W") ;
            accountModel.setAccountHolderIdAccountHolderModel(accountHolderModel) ;
            BaseWrapper baseWrapper = new BaseWrapperImpl() ;
            baseWrapper.setBasePersistableModel(accountModel) ;
            baseWrapper = accountManager.loadAccount(baseWrapper) ;
            if(baseWrapper.getBasePersistableModel()!=null) {
                accountModel = (AccountModel) baseWrapper.getBasePersistableModel();

                Calendar startCalendar = Calendar.getInstance();
                startCalendar.setTime(transactionDateTime);
                startCalendar.set(Calendar.DAY_OF_MONTH, startCalendar.getActualMinimum(Calendar.DAY_OF_MONTH));
                PortalDateUtils.resetTime(startCalendar);
                Date startDate = startCalendar.getTime();
                Double creditConsumedBalance = ledgerDAO.getWalkinCustomerConsumedBalanceByDateRange(accountModel.getAccountId(), TransactionTypeConstants.CREDIT, startDate, transactionDateTime, false);
                if (nonBvsCreditLimitModel != null) {
                    creditBalance = nonBvsCredit+bvsCreditLimitModel.getMaximum() - creditConsumedBalance;
                    if(creditBalance<0.0){
                        creditBalance=0.0;
                    }
                }
                Double debitConsumedBalance = ledgerDAO.getWalkinCustomerConsumedBalanceByDateRange(accountModel.getAccountId(), TransactionTypeConstants.DEBIT, startDate, transactionDateTime, false);
                if (nonBvsdebitLimitModel != null) {
                    debitBalance = nonBvsDebit+bvsdebitLimitModel.getMaximum() - debitConsumedBalance;
                    if(debitBalance<0.0){
                        debitBalance=0.0;
                    }
                }
            }
            else {

                creditBalance=nonBvsCredit+bvsCreditLimitModel.getMaximum();
                debitBalance=nonBvsDebit+bvsdebitLimitModel.getMaximum();
            }
            ex.setCreditLimit(creditBalance);
            ex.setDebitLimit(debitBalance);
            l2.add(ex);
        }

        return new ModelAndView( getFormView(), "walkInCustomerViewModelList", l2 );
    }

    public void setWalkInCustomerViewFacade( WalkInCustomerViewFacade walkInCustomerViewFacade )
    {
        this.walkInCustomerViewFacade = walkInCustomerViewFacade;
    }
    public void setLimitManager( LimitManager limitManager )
    {
        this.limitManager = limitManager;
    }

    public void setLedgerDAO( LedgerDAO ledgerDAO )
    {
        this.ledgerDAO = ledgerDAO;
    }


    public void setAccountManager(AccountManager accountManager) {
        this.accountManager = accountManager;
    }
}
