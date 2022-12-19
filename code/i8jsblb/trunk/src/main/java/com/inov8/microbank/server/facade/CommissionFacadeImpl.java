package com.inov8.microbank.server.facade;

import com.inov8.framework.common.exception.FrameworkCheckedException;
import com.inov8.framework.common.exception.FrameworkExceptionTranslator;
import com.inov8.framework.common.wrapper.BaseWrapper;
import com.inov8.framework.common.wrapper.SearchBaseWrapper;
import com.inov8.microbank.common.model.CommissionRateModel;
import com.inov8.microbank.common.model.CommissionTransactionModel;
import com.inov8.microbank.common.model.UnsettledAgentCommModel;
import com.inov8.microbank.common.util.TransactionProductEnum;
import com.inov8.microbank.common.wrapper.commission.CommissionWrapper;
import com.inov8.microbank.common.wrapper.workflow.WorkFlowWrapper;
import com.inov8.microbank.server.service.commissionmodule.CommissionAmountsHolder;
import com.inov8.microbank.server.service.commissionmodule.CommissionManager;
import com.inov8.microbank.server.service.commissionmodule.CommissionRateManager;
import com.inov8.microbank.server.service.commissionmodule.CommissionReasonManager;

/**
 * <p>Title: </p>
 *
 * <p>Description: </p>
 *
 * <p>Copyright: Copyright (c) 2006</p>
 *
 * <p>Company: </p>
 *
 * @author not attributable
 * @version 1.0
 */

public class CommissionFacadeImpl
    implements CommissionFacade
{
  private CommissionManager commissionManager;
  private CommissionRateManager commissionRateManager;
  private CommissionReasonManager commissionReasonManager;
  private FrameworkExceptionTranslator frameworkExceptionTranslator;

  public CommissionFacadeImpl()
  {
  }

  public void setCommissionManager(CommissionManager
                                   commissionManager)
  {
    this.commissionManager = commissionManager;
  }

  public CommissionWrapper calculateCommission(WorkFlowWrapper
                                               workFlowWrapper) throws
      FrameworkCheckedException
  {
    try
    {
      return commissionManager.calculateCommission(workFlowWrapper);
    }
    catch (Exception ex)
    {
      throw this.frameworkExceptionTranslator.translate(ex,
          this.frameworkExceptionTranslator.
          FIND_BY_PRIMARY_KEY_ACTION);
    }
  }

  //======================================================================
  // Methods for CommissionRateManager
  //======================================================================

  public SearchBaseWrapper loadCommissionRate(SearchBaseWrapper
                                              searchBaseWrapper) throws
      FrameworkCheckedException
  {
    try
    {
      this.commissionRateManager.loadCommissionRate(searchBaseWrapper);
    }
    catch (Exception ex)
    {
      throw this.frameworkExceptionTranslator.translate(ex,
          this.frameworkExceptionTranslator.
          FIND_BY_PRIMARY_KEY_ACTION);
    }
    return searchBaseWrapper;
  }

  public BaseWrapper loadCommissionRate(BaseWrapper baseWrapper) throws
      FrameworkCheckedException
  {
    try
    {
      this.commissionRateManager.loadCommissionRate(baseWrapper);
    }
    catch (Exception ex)
    {
      throw this.frameworkExceptionTranslator.translate(ex,
          this.frameworkExceptionTranslator.
          FIND_BY_PRIMARY_KEY_ACTION);
    }
    return baseWrapper;
  }

  public SearchBaseWrapper searchCommissionRate(SearchBaseWrapper
                                                searchBaseWrapper) throws
      FrameworkCheckedException
  {
    try
    {
      this.commissionRateManager.searchCommissionRate(searchBaseWrapper);
    }
    catch (Exception ex)
    {
      throw this.frameworkExceptionTranslator.translate(ex,
          this.frameworkExceptionTranslator.FIND_ACTION);
    }
    return searchBaseWrapper;
  }

  public BaseWrapper updateCommissionRate(BaseWrapper baseWrapper) throws
      FrameworkCheckedException
  {
    try
    {
      this.commissionRateManager.updateCommissionRate(baseWrapper);
    }
    catch (Exception ex)
    {
      throw this.frameworkExceptionTranslator.translate(ex,
          this.frameworkExceptionTranslator.INSERT_ACTION);
    }
    return baseWrapper;
  }

  public BaseWrapper createCommissionRate(BaseWrapper baseWrapper) throws
      FrameworkCheckedException
  {
    try
    {
      this.commissionRateManager.createCommissionRate(baseWrapper);
    }
    catch (Exception ex)
    {
      throw this.frameworkExceptionTranslator.translate(ex,
          this.frameworkExceptionTranslator.INSERT_ACTION);
    }
    return baseWrapper;
  }

  //======================================================================
  // Methods for CommissionReasonManager
  //======================================================================

  public SearchBaseWrapper loadCommissionReason(SearchBaseWrapper
                                                searchBaseWrapper) throws
      FrameworkCheckedException
  {
    try
    {
      this.commissionReasonManager.loadCommissionReason(searchBaseWrapper);
    }
    catch (Exception ex)
    {
      throw this.frameworkExceptionTranslator.translate(ex,
          this.frameworkExceptionTranslator.
          FIND_BY_PRIMARY_KEY_ACTION);
    }
    return searchBaseWrapper;
  }

  public SearchBaseWrapper searchCommissionReason(SearchBaseWrapper
                                                  searchBaseWrapper) throws
      FrameworkCheckedException
  {
    try
    {
      this.commissionReasonManager.searchCommissionReason(searchBaseWrapper);
    }
    catch (Exception ex)
    {
      throw this.frameworkExceptionTranslator.translate(ex,
          this.frameworkExceptionTranslator.FIND_ACTION);
    }
    return searchBaseWrapper;
  }

  public BaseWrapper updateCommissionReason(BaseWrapper baseWrapper) throws
      FrameworkCheckedException
  {
    try
    {
      this.commissionReasonManager.updateCommissionReason(baseWrapper);
    }
    catch (Exception ex)
    {
      throw this.frameworkExceptionTranslator.translate(ex,
          this.frameworkExceptionTranslator.INSERT_ACTION);
    }
    return baseWrapper;
  }

  public BaseWrapper createCommissionReason(BaseWrapper baseWrapper) throws
      FrameworkCheckedException
  {
    try
    {
      this.commissionReasonManager.createCommissionReason(baseWrapper);
    }
    catch (Exception ex)
    {
      throw this.frameworkExceptionTranslator.translate(ex,
          this.frameworkExceptionTranslator.INSERT_ACTION);
    }
    return baseWrapper;
  }

  //=======================================================================
  // Other Methods
  //=======================================================================

  public void setCommissionRateManager(CommissionRateManager
                                       commissionRateManager)
  {
    this.commissionRateManager = commissionRateManager;
  }

  public void setCommissionReasonManager(CommissionReasonManager
                                         commissionReasonManager)
  {
    this.commissionReasonManager = commissionReasonManager;
  }

  public void setFrameworkExceptionTranslator(FrameworkExceptionTranslator
                                              frameworkExceptionTranslator)
  {
    this.frameworkExceptionTranslator = frameworkExceptionTranslator;
  }
  
	public SearchBaseWrapper getCommissionTransactionModel(SearchBaseWrapper baseWrapper) throws FrameworkCheckedException {
		return commissionManager.getCommissionTransactionModel(baseWrapper);
	}

	public Boolean removeCommissionTransactionModel(CommissionTransactionModel commissionTxModel) throws FrameworkCheckedException {
		return commissionManager.removeCommissionTransactionModel(commissionTxModel);
	}

	@Override
	public BaseWrapper loadAgentCommission(BaseWrapper baseWrapper)
			throws FrameworkCheckedException {
		try
	    {
	      this.commissionManager.loadAgentCommission(baseWrapper);
	    }
	    catch (Exception ex)
	    {
	      throw this.frameworkExceptionTranslator.translate(ex,
	          this.frameworkExceptionTranslator.FIND_ACTION);
	    }
	    return baseWrapper;
	}

	@Override
	public Boolean updateCommissionTransaction(Boolean isSettled, Boolean isPosted, Object[] params, TransactionProductEnum productEnum, Integer legNumber) {

		return commissionManager.updateCommissionTransaction(isSettled, isPosted, params, productEnum, legNumber);
	}

	@Override
	public SearchBaseWrapper getCommissionRateData(	CommissionRateModel commissionRateModel) {
		// TODO Auto-generated method stub
		return commissionManager.getCommissionRateData(commissionRateModel);
	}

	@Override
	public CommissionAmountsHolder loadCommissionDetails(Long transactionDetailId) throws FrameworkCheckedException {
		return commissionManager.loadCommissionDetails(transactionDetailId);
	}

	@Override
	public CommissionAmountsHolder loadCommissionDetailsUnsettled(Long transactionDetailId) throws FrameworkCheckedException {
		return commissionManager.loadCommissionDetailsUnsettled(transactionDetailId);
	}

	@Override
	public void calculateHierarchyCommission(WorkFlowWrapper workFlowWrapper)
			throws FrameworkCheckedException {
		this.commissionManager.calculateHierarchyCommission(workFlowWrapper);
		
	}

	@Override
	public void makeAgent2CommissionSettlement(WorkFlowWrapper wrapper) throws Exception {
		this.commissionManager.makeAgent2CommissionSettlement(wrapper);
	}

	@Override
	public void saveUnsettledCommission(UnsettledAgentCommModel model, Long agentAppUserId) throws FrameworkCheckedException {
		this.commissionManager.saveUnsettledCommission(model, agentAppUserId);		
	}

	@Override
	public SearchBaseWrapper searchUnsettledAgentCommission(SearchBaseWrapper searchBaseWrapper) throws FrameworkCheckedException {
		return this.commissionManager.searchUnsettledAgentCommission(searchBaseWrapper);		
	}
}
