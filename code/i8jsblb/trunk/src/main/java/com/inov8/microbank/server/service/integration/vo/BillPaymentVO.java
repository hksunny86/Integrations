package com.inov8.microbank.server.service.integration.vo;


/**
 * <p>Title: </p>
 *
 * <p>Description: </p>
 *
 * <p>Copyright: Copyright (c) 2006</p>
 *
 * <p>Company: inov8 Limited</p>
 *
 * @author Jawwad Farooq
 * @version 1.0
 */
public interface BillPaymentVO extends ProductVO
{

  public Double getBillAmount();
  public Double getCurrentBillAmount();
  public void setBillAmount(Double billAmount);
  
  public void setConsumerNo(String consumerNo);
  public String getConsumerNo();
  public String responseXML() ;
  

}
