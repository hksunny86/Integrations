package com.inov8.microbank.server.service.retailermodule;

import java.util.List;

import com.inov8.framework.common.exception.FrameworkCheckedException;
import com.inov8.framework.common.model.ExampleConfigHolderModel;
import com.inov8.framework.common.wrapper.BaseWrapper;
import com.inov8.framework.common.wrapper.SearchBaseWrapper;
import com.inov8.microbank.common.model.AppUserPartnerGroupModel;
import com.inov8.microbank.common.model.RetailerContactModel;
import com.inov8.microbank.common.model.retailermodule.RetailerContactSearchViewModel;
import com.inov8.microbank.common.vo.RetailerContactDetailVO;

/**
 * <p>Title: Microbank</p>
 *
 * <p>Description: Backend Application for POS terminals.</p>
 *
 * <p>Copyright: Copyright (c) 2006</p>
 *
 * <p>Company: Inov8 Limited</p>
 *
 * @author Asad Hayat
 * @version 1.0
 */
public interface RetailerContactManager
{

  SearchBaseWrapper loadRetailerContact(SearchBaseWrapper searchBaseWrapper) throws
      FrameworkCheckedException;

  BaseWrapper loadRetailerContact(BaseWrapper baseWrapper) throws
      FrameworkCheckedException;

  SearchBaseWrapper searchRetailerContact(SearchBaseWrapper searchBaseWrapper) throws
      FrameworkCheckedException;
  
  public BaseWrapper createOrUpdateRetailerContact(BaseWrapper baseWrapper)throws
  FrameworkCheckedException;
  
  public  Long getAppUserPartnerGroupId(Long appUserId)throws FrameworkCheckedException;
  
  public  AppUserPartnerGroupModel getAppUserPartnetGroupByAppUserId(Long appUserId)throws FrameworkCheckedException;
	 
  
  BaseWrapper createRetailerContact(BaseWrapper baseWrapper) throws
      FrameworkCheckedException;

  public SearchBaseWrapper searchRetailerContactByMobileNo(SearchBaseWrapper
      searchBaseWrapper) throws
      FrameworkCheckedException;

  public RetailerContactModel findRetailerContactByMobileNumber(
      SearchBaseWrapper searchBaseWrapper) throws
      FrameworkCheckedException;

  public BaseWrapper loadRetailerContactByAppUser(BaseWrapper baseWrapper)throws
      FrameworkCheckedException;

  public RetailerContactModel findRetailerHeadContact(SearchBaseWrapper
              searchBaseWrapper)throws FrameworkCheckedException;
  
  
  
public BaseWrapper updateRetailerContact(BaseWrapper  baseWrapper) throws
  FrameworkCheckedException;

public BaseWrapper createAppUserForRetailerContact(BaseWrapper baseWrapper) throws
  FrameworkCheckedException;

SearchBaseWrapper loadRetailerContactListView(SearchBaseWrapper searchBaseWrapper) throws FrameworkCheckedException;

SearchBaseWrapper findExactRetailerContactListViewModel(SearchBaseWrapper searchBaseWrapper) throws
FrameworkCheckedException;

public BaseWrapper loadHeadRetailerContactAppUser( Long retailerId ) throws FrameworkCheckedException, Exception;

public RetailerContactModel findHeadRetailerContactModelByRetailerId( Long retailerId ) throws FrameworkCheckedException;

public int countByExample(RetailerContactModel agentModel, ExampleConfigHolderModel exampleConfigHolder);

SearchBaseWrapper loadRetailerByInitialAppFormNo(SearchBaseWrapper searchBaseWrapper) throws FrameworkCheckedException;

SearchBaseWrapper loadRetailerContactAddresses(SearchBaseWrapper searchBaseWrapper) throws FrameworkCheckedException;

BaseWrapper loadRetailerContactByIAF(String initialAppFormNo) throws FrameworkCheckedException;

List<RetailerContactDetailVO> findRetailerContactModelList() throws FrameworkCheckedException;



public RetailerContactModel loadRetailerContactByRetailerContactId( Long retailerContactId ) throws FrameworkCheckedException;

public RetailerContactModel saveOrUpdateRetailerContactModel(RetailerContactModel mmodel) throws FrameworkCheckedException;


}
