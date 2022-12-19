package com.inov8.microbank.server.service.handlermodule;

import java.util.List;

import com.inov8.framework.common.exception.FrameworkCheckedException;
import com.inov8.framework.common.model.ExampleConfigHolderModel;
import com.inov8.framework.common.wrapper.BaseWrapper;
import com.inov8.framework.common.wrapper.SearchBaseWrapper;
import com.inov8.microbank.common.model.HandlerModel;
import com.inov8.microbank.common.model.retailermodule.HandlerSearchViewModel;

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

public interface HandlerManager
{
  SearchBaseWrapper loadHandler(SearchBaseWrapper searchBaseWrapper) throws
      FrameworkCheckedException;

  BaseWrapper loadHandler(BaseWrapper baseWrapper) throws
      FrameworkCheckedException;

  SearchBaseWrapper searchHandler(SearchBaseWrapper searchBaseWrapper) throws
      FrameworkCheckedException;
  
  SearchBaseWrapper searchHandlerView(SearchBaseWrapper searchBaseWrapper)throws
  FrameworkCheckedException;

  BaseWrapper createOrUpdateHandler(BaseWrapper baseWrapper) throws
      FrameworkCheckedException;

  List<Long> getRetalerDataMap(Long retailerContactId);
  List<HandlerSearchViewModel> findHandlerViews(BaseWrapper baseWrapper);
  int countByExample(HandlerModel handlerModel, ExampleConfigHolderModel exampleConfigHolderModel);
  
  
  public Boolean isAgentWebEnabled(Long handlerId);
  public Boolean isDebitCardFeeEnabled(Long handlerId);
}
