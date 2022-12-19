package com.inov8.microbank.server.service.mnomodule;

import com.inov8.framework.common.exception.FrameworkCheckedException;
import com.inov8.framework.common.wrapper.BaseWrapper;
import com.inov8.framework.common.wrapper.SearchBaseWrapper;

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

public interface MnoDialingCodeManager
{
  public BaseWrapper createOrUpdateMnoDialingCode(BaseWrapper baseWrapper) throws
      FrameworkCheckedException;

  public SearchBaseWrapper searchMnoDialingCode(SearchBaseWrapper searchBaseWrapper) throws
  FrameworkCheckedException;
  public SearchBaseWrapper loadMnoDialingCode(SearchBaseWrapper searchBaseWrapper) throws 
  FrameworkCheckedException; 
  
  public SearchBaseWrapper findMnoDialingCode(SearchBaseWrapper searchBaseWrapper)throws FrameworkCheckedException;
}
