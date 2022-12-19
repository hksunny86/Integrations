package com.inov8.microbank.account.service;

import com.inov8.framework.common.exception.FrameworkCheckedException;
import com.inov8.framework.common.wrapper.BaseWrapper;
import com.inov8.framework.common.wrapper.SearchBaseWrapper;

import java.util.List;

/**
 * Created by Malik on 8/17/2016.
 */
public interface AccountControlManager
{
    SearchBaseWrapper searchBlacklistMarkingViewModel(SearchBaseWrapper searchBaseWrapper) throws FrameworkCheckedException;

    SearchBaseWrapper searchBlacklistedCNICViewModel(SearchBaseWrapper wrapper) throws FrameworkCheckedException;

    void saveBlacklistMarkingViewModel(BaseWrapper wrapper) throws FrameworkCheckedException;
    void saveBlacklistMarkingViewModelWithAuthorization(BaseWrapper wrapper) throws FrameworkCheckedException;
    void saveCustomerACNatureMarkingWithAuthorization(BaseWrapper wrapper) throws FrameworkCheckedException;
    BaseWrapper markUnmarkBlacklistedWithAuthorization(BaseWrapper wrapper) throws FrameworkCheckedException;

    Boolean isCnicBlacklisted(String cnic);
    Boolean isAgentWebEnabled(Long retailerContactId);
    Boolean isAgentUSSDEnabled(Long retailerContactId);
    //Is Debit Card Fee Enable
    Boolean isDebitCardFeeEnabled(Long retailerContactId);


    Boolean isUserNameAlreadyExist(String username);

    List<String> loadBlacklistedCNICList();
    
    BaseWrapper markUnmarkBlacklistedCNIC(BaseWrapper baseWrapper) throws FrameworkCheckedException;
    
    SearchBaseWrapper loadBlacklistMarkUnmarkHistory( SearchBaseWrapper searchBaseWrapper ) throws FrameworkCheckedException;

    SearchBaseWrapper loadBlackListedNICHistory(SearchBaseWrapper searchBaseWrapper) throws FrameworkCheckedException;

    SearchBaseWrapper loadWalkInBlackListedNics(SearchBaseWrapper searchBaseWrapper) throws FrameworkCheckedException;

    SearchBaseWrapper loadExpiredNics(SearchBaseWrapper searchBaseWrapper) throws FrameworkCheckedException;
}
