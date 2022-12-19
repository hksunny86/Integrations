package com.inov8.microbank.server.facade;

import com.inov8.framework.common.exception.FrameworkCheckedException;
import com.inov8.framework.common.exception.FrameworkExceptionTranslator;
import com.inov8.framework.common.wrapper.BaseWrapper;
import com.inov8.framework.common.wrapper.SearchBaseWrapper;
import com.inov8.microbank.common.wrapper.workflow.WorkFlowWrapper;
import com.inov8.microbank.server.service.creditmodule.CreditInfoManager;
import com.inov8.microbank.server.service.creditmodule.DistDistCreditManager;
import com.inov8.microbank.server.service.creditmodule.DistRetCreditManager;
import com.inov8.microbank.server.service.creditmodule.Inov8CustomerCreditManager;
import com.inov8.microbank.server.service.creditmodule.Inov8DistributorCreditManager;
import com.inov8.microbank.server.service.creditmodule.RetCustomerCreditManager;
import com.inov8.microbank.server.service.creditmodule.RetHeadRetCreditManager;
import com.inov8.microbank.server.service.creditmodule.RetRetCreditManager;

/**
 *
 * <p>Title: Microbank</p>
 *
 * <p>Description: Backened application for POS terminal</p>
 *
 * <p>Copyright: Copyright (c) 2006</p>
 *
 * <p>Company: Inov8 Ltd</p>
 *
 * @author Ahmad Iqbal
 * @version 1.0
 *
 */

public class CreditFacadeImpl
    implements CreditFacade
{
  private RetHeadRetCreditManager retHeadRetCreditManager;
  private RetRetCreditManager retRetCreditManager;
  private Inov8DistributorCreditManager inov8DistributorCreditManager;

  private FrameworkExceptionTranslator frameworkExceptionTranslator;
  private DistDistCreditManager distDistCreditManager;
  private DistRetCreditManager distRetCreditManager;
  private RetCustomerCreditManager retCustomerCreditManager;
  private Inov8CustomerCreditManager inov8CustomerCreditManager;
  private CreditInfoManager creditInfoManager;

  //======================================================================
  // Methods for RetHeadRetManager
  //======================================================================


  public void setCreditInfoManager(CreditInfoManager creditInfoManager)
{
	this.creditInfoManager = creditInfoManager;
}

public BaseWrapper loadRetailerContactForRetailer(BaseWrapper baseWrapper) throws
      FrameworkCheckedException
  {
    try
    {
      this.retHeadRetCreditManager.loadRetailerContactForRetailer(baseWrapper);
    }
    catch (Exception ex)
    {
      throw this.frameworkExceptionTranslator.translate(ex,
          this.frameworkExceptionTranslator.FIND_ACTION);
    }
    return baseWrapper;

  }

  public BaseWrapper createRetHeadRetTransaction(BaseWrapper baseWrapper) throws
      FrameworkCheckedException
  {
    try
    {
      this.retHeadRetCreditManager.createRetHeadRetTransaction(baseWrapper);
    }
    catch (Exception ex)
    {
      throw this.frameworkExceptionTranslator.translate(ex,
          this.frameworkExceptionTranslator.INSERT_ACTION);
    }
    return baseWrapper;

  }

  public BaseWrapper updateRetailerContactBalanceForRetailer(BaseWrapper
      baseWrapper) throws FrameworkCheckedException
  {
    try
    {
      this.retHeadRetCreditManager.updateRetailerContactBalanceForRetailer(
          baseWrapper);
    }
    catch (Exception ex)
    {
      throw this.frameworkExceptionTranslator.translate(ex,
          this.frameworkExceptionTranslator.UPDATE_ACTION);
    }
    return baseWrapper;

  }

  //======================================================================
  // Methods for RetRetCreditManager
  //======================================================================


  public BaseWrapper loadRetailerContactForRetailers(BaseWrapper baseWrapper) throws
      FrameworkCheckedException
  {
    try
    {
      this.retRetCreditManager.loadRetailerContactForRetailers(baseWrapper);
    }
    catch (Exception ex)
    {
      throw this.frameworkExceptionTranslator.translate(ex,
          this.frameworkExceptionTranslator.FIND_ACTION);
    }
    return baseWrapper;

  }

  public BaseWrapper createRetRetTransaction(BaseWrapper baseWrapper) throws
      FrameworkCheckedException
  {
    try
    {
      this.retRetCreditManager.createRetRetTransaction(baseWrapper);
    }
    catch (Exception ex)
    {
      throw this.frameworkExceptionTranslator.translate(ex,
          this.frameworkExceptionTranslator.INSERT_ACTION);
    }
    return baseWrapper;

  }

  public BaseWrapper updateRetailerContactBalanceforRetailers(BaseWrapper
      baseWrapper) throws FrameworkCheckedException
  {
    try
    {
      this.retRetCreditManager.updateRetailerContactBalanceforRetailers(
          baseWrapper);
    }
    catch (Exception ex)
    {
      throw this.frameworkExceptionTranslator.translate(ex,
          this.frameworkExceptionTranslator.UPDATE_ACTION);
    }
    return baseWrapper;

  }

  public WorkFlowWrapper updateRetailerContactBalanceforRetailersSecond(
      WorkFlowWrapper
      workFlowWrapper) throws FrameworkCheckedException
  {
    try
    {
      this.retRetCreditManager.updateRetailerContactBalanceforRetailersSecond(
          workFlowWrapper);
    }
    catch (Exception ex)
    {
      throw this.frameworkExceptionTranslator.translate(ex,
          this.frameworkExceptionTranslator.UPDATE_ACTION);
    }
    return workFlowWrapper;
  }

  //======================================================================
  // Methods for Inov8DistributorCredit
  //======================================================================

  public BaseWrapper loadDistributorContactForInov8(BaseWrapper baseWrapper) throws
      FrameworkCheckedException
  {
    try
    {
      this.inov8DistributorCreditManager.loadDistributorContactForInov8(
          baseWrapper);
    }
    catch (Exception ex)
    {
      throw this.frameworkExceptionTranslator.translate(ex,
          this.frameworkExceptionTranslator.FIND_ACTION);
    }
    return baseWrapper;
  }

  public BaseWrapper createInov8DistributorTransaction(BaseWrapper baseWrapper) throws
      FrameworkCheckedException
  {
    try
    {
      this.inov8DistributorCreditManager.createInov8DistributorTransaction(
          baseWrapper);
    }
    catch (Exception ex)
    {
      throw this.frameworkExceptionTranslator.translate(ex,
          this.frameworkExceptionTranslator.INSERT_ACTION);
    }
    return baseWrapper;

  }

  public BaseWrapper updateInov8DistributorContactBalance(BaseWrapper
      baseWrapper) throws
      FrameworkCheckedException
  {
    try
    {
      this.inov8DistributorCreditManager.updateInov8DistributorContactBalance(
          baseWrapper);
    }
    catch (Exception ex)
    {
      throw this.frameworkExceptionTranslator.translate(ex,
          this.frameworkExceptionTranslator.UPDATE_ACTION);
    }
    return baseWrapper;
  }

  public BaseWrapper loadOperator(BaseWrapper baseWrapper) throws
      FrameworkCheckedException
  {
    try
    {
      this.inov8DistributorCreditManager.loadOperator(baseWrapper);
    }
    catch (Exception ex)
    {
      throw this.frameworkExceptionTranslator.translate(ex,
          this.frameworkExceptionTranslator.FIND_ACTION);
    }
    return baseWrapper;
  }

  //======================================================================
  // Methods for DistDistCredit
  //======================================================================

  public BaseWrapper updateDistToDistContactBalance(BaseWrapper baseWrapper) throws
      FrameworkCheckedException
  {
    try
    {
      this.distDistCreditManager.updateDistToDistContactBalance(baseWrapper);
    }
    catch (Exception ex)
    {
      throw this.frameworkExceptionTranslator.translate(ex,
          this.frameworkExceptionTranslator.UPDATE_ACTION);
    }
    return baseWrapper;
  }

  public BaseWrapper loadDistributorContact(BaseWrapper baseWrapper) throws
      FrameworkCheckedException
  {
    try
    {
      this.distDistCreditManager.loadDistributorContact(baseWrapper);
    }
    catch (Exception ex)
    {
      throw this.frameworkExceptionTranslator.translate(ex,
          this.frameworkExceptionTranslator.UPDATE_ACTION);
    }
    return baseWrapper;
  }

  //======================================================================
  // Methods for DistRetCredit
  //======================================================================
  public BaseWrapper updateDistributorRetailerContactBalance(BaseWrapper
      baseWrapper) throws
      FrameworkCheckedException
  {
    try
    {
      this.distRetCreditManager.updateDistributorRetailerContactBalance(
          baseWrapper);
    }
    catch (Exception ex)
    {
      throw this.frameworkExceptionTranslator.translate(ex,
          this.frameworkExceptionTranslator.UPDATE_ACTION);
    }

    return baseWrapper;
  }

  public BaseWrapper loadRetailerContact(BaseWrapper baseWrapper) throws
      FrameworkCheckedException
  {
    try
    {
      this.distRetCreditManager.loadRetailerContact(baseWrapper);
    }
    catch (Exception ex)
    {
      throw this.frameworkExceptionTranslator.translate(ex,
          this.frameworkExceptionTranslator.UPDATE_ACTION);
    }

    return baseWrapper;

  }

  //======================================================================
  // Other Methods
  //======================================================================

  public void setRetHeadRetCreditManager(RetHeadRetCreditManager
                                         retHeadRetCreditManager)
  {
    this.retHeadRetCreditManager = retHeadRetCreditManager;
  }

  public void setFrameworkExceptionTranslator(FrameworkExceptionTranslator
                                              frameworkExceptionTranslator)
  {
    this.frameworkExceptionTranslator = frameworkExceptionTranslator;
  }

  public void setRetRetCreditManager(RetRetCreditManager retRetCreditManager)
  {
    this.retRetCreditManager = retRetCreditManager;
  }

  public void setInov8DistributorCreditManager(Inov8DistributorCreditManager
                                               inov8DistributorCreditManager)
  {
    this.inov8DistributorCreditManager = inov8DistributorCreditManager;
  }

  public void setDistDistCreditManager(DistDistCreditManager
                                       distDistCreditManager)
  {
    this.distDistCreditManager = distDistCreditManager;
  }

  public void setDistRetCreditManager(DistRetCreditManager distRetCreditManager)
  {
    this.distRetCreditManager = distRetCreditManager;
  }

  public void setRetCustomerCreditManager(RetCustomerCreditManager
                                          retCustomerCreditManager)
  {
    this.retCustomerCreditManager = retCustomerCreditManager;
  }

  public void setInov8CustomerCreditManager(Inov8CustomerCreditManager
                                            inov8CustomerCreditManager)
  {
    this.inov8CustomerCreditManager = inov8CustomerCreditManager;
  }

  //======================================================================
  // Methods for RetCustomer added by Syed Ahmad Bilal
  //======================================================================
  public BaseWrapper updateRetToCustContactBalance(BaseWrapper baseWrapper) throws
      FrameworkCheckedException
  {
    try
    {
      this.retCustomerCreditManager.updateRetToCustContactBalance(baseWrapper);
    }
    catch (Exception ex)
    {
      throw this.frameworkExceptionTranslator.translate(ex,
          this.frameworkExceptionTranslator.UPDATE_ACTION);
    }
    return baseWrapper;
  }

  //======================================================================
  // Method for Inov8 Customer Credit transfer, added by Syed Ahmad Bilal
//======================================================================
  public BaseWrapper updateInov8ToCustomerContactBalance(BaseWrapper
      baseWrapper) throws
      FrameworkCheckedException
  {
    try
    {
      this.inov8CustomerCreditManager.updateInov8ToCustomerContactBalance(
          baseWrapper);
    }
    catch (Exception ex)
    {
      throw this.frameworkExceptionTranslator.translate(ex,
          this.frameworkExceptionTranslator.UPDATE_ACTION);
    }
    return baseWrapper;
  }

public SearchBaseWrapper searchDistributorOrRetailer(SearchBaseWrapper searchBaseWrapper) throws FrameworkCheckedException
{
	try
    {
      this.creditInfoManager.searchDistributorOrRetailer(searchBaseWrapper);
    }
    catch (Exception ex)
    {
      throw this.frameworkExceptionTranslator.translate(ex,
          this.frameworkExceptionTranslator.FIND_ACTION);
    }
    return searchBaseWrapper;
}
}
