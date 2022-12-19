package com.inov8.microbank.server.service.operatormodule;

import com.inov8.framework.common.exception.FrameworkCheckedException;
import com.inov8.framework.common.wrapper.BaseWrapper;

/**
 * <p>Title: </p>
 *
 * <p>Description: </p>
 *
 * <p>Copyright: Copyright (c) 2006</p>
 *
 * <p>Company: </p>
 *
 * @author Jawwad Farooq
 * @version 1.0
 */
public interface OperatorManager
{

  public BaseWrapper loadOperator(BaseWrapper searchBaseWrapper) throws
      FrameworkCheckedException;

  public BaseWrapper loadOperatorByAppUser(BaseWrapper searchBaseWrapper) throws
      FrameworkCheckedException;

  public BaseWrapper updateOperator(BaseWrapper baseWrapper) throws
      FrameworkCheckedException;
  
  public BaseWrapper getOperatorBankInfo(BaseWrapper baseWrapper) throws
  	  FrameworkCheckedException;

}
