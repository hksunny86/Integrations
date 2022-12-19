package com.inov8.verifly.server.service.logmodule;

import com.inov8.verifly.common.wrapper.logmodule.LogWrapper;

public interface LogManager
{
  public LogWrapper insertLogRequiresNewTransaction(LogWrapper logWrapper)throws Exception;
  public LogWrapper updateLogRequiresNewTransaction(LogWrapper logWrapper)throws Exception;
  public LogWrapper viewTransactionLog(LogWrapper logWrapper)throws Exception;

}
