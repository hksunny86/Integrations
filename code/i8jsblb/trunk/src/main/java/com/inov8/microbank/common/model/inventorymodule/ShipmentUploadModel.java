package com.inov8.microbank.common.model.inventorymodule;

import com.inov8.framework.common.model.BaseModel;

/**
 * Command class to handle uploading of a file.
 */
public class ShipmentUploadModel implements BaseModel
{
  /**
	 * 
	 */
	private static final long serialVersionUID = -4460310203783986334L;

private byte[] file;

  private Long shipmentId;

  private Boolean userName;
  private Boolean pin;
  private Boolean serialNo;
  private Boolean additionalField1;
  private Boolean additionalField2;

  public void setFile(byte[] file)
  {
    this.file = file;
  }

  public void setUserName(Boolean userName)
  {
    this.userName = userName;
  }

  public void setSerialNo(Boolean serialNo)
  {
    this.serialNo = serialNo;
  }

  public void setPin(Boolean pin)
  {
    this.pin = pin;
  }

  public void setAdditionalField2(Boolean additionalField2)
  {
    this.additionalField2 = additionalField2;
  }

  public void setAdditionalField1(Boolean additionalField1)
  {
    this.additionalField1 = additionalField1;
  }

  public void setShipmentId(Long shipmentId)
  {
    this.shipmentId = shipmentId;
  }

  public byte[] getFile()
  {
    return file;
  }

  public Boolean getUserName()
  {
    return userName;
  }

  public Boolean getSerialNo()
  {
    return serialNo;
  }

  public Boolean getPin()
  {
    return pin;
  }

  public Boolean getAdditionalField2()
  {
    return additionalField2;
  }

  public Boolean getAdditionalField1()
  {
    return additionalField1;
  }

  public Long getShipmentId()
  {
    return shipmentId;
  }
}
