package com.inov8.microbank.server.service.integration.vo;

import com.inov8.framework.common.exception.FrameworkCheckedException;
import com.inov8.framework.common.wrapper.BaseWrapper;


/**
 * @author Jawwad Farooq
 * @version 1.0
 */
public interface ProductVO
{

  public void setResponseCode(String responseCode) ;
  public String getResponseCode() ;

  ProductVO populateVO( ProductVO productVO, BaseWrapper baseWrapper );
  void validateVO( ProductVO productVO )throws FrameworkCheckedException;




}
