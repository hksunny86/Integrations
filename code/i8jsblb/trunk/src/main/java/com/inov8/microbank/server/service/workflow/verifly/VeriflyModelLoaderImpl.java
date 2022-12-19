package com.inov8.microbank.server.service.workflow.verifly;

import com.inov8.framework.common.wrapper.BaseWrapper;
import com.inov8.microbank.common.model.VeriflyModel;
import com.inov8.microbank.server.dao.veriflymodule.VeriflyDAO;


public class VeriflyModelLoaderImpl implements VeriflyModelLoader
{
  private VeriflyDAO veriflyDAO;


  public BaseWrapper loadVerifly( BaseWrapper baseWrapper )
  {
    VeriflyModel veriflyModel = (VeriflyModel)baseWrapper.getBasePersistableModel() ;
    veriflyModel = this.veriflyDAO.findByPrimaryKey( veriflyModel.getVeriflyId() ) ;
    baseWrapper.setBasePersistableModel( veriflyModel );
    return baseWrapper ;
  }

  public void setVeriflyDAO(VeriflyDAO veriflyDAO)
  {
    this.veriflyDAO = veriflyDAO;
  }

}
