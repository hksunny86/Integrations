package com.inov8.microbank.server.service.operatormodule;

import com.inov8.framework.common.exception.FrameworkCheckedException;
import com.inov8.framework.common.wrapper.BaseWrapper;
import com.inov8.framework.common.wrapper.SearchBaseWrapper;

public interface OperatorBankInfoManager {
	BaseWrapper loadOperatorBankInfo(BaseWrapper baseWrapper) throws
    FrameworkCheckedException;
  
  SearchBaseWrapper loadOperatorBankInfo(SearchBaseWrapper searchBaseWrapper) throws
  FrameworkCheckedException;
  
  SearchBaseWrapper searchOperatorBankInfo(SearchBaseWrapper searchBaseWrapper) throws
  FrameworkCheckedException;
  
  BaseWrapper createOperatorBankInfo(BaseWrapper baseWrapper) throws
  FrameworkCheckedException;
  
  BaseWrapper updateOperatorBankInfo(BaseWrapper baseWrapper) throws
  FrameworkCheckedException;

}
