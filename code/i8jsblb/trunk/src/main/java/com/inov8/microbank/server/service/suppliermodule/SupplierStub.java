package com.inov8.microbank.server.service.suppliermodule;

import com.inov8.framework.common.exception.FrameworkCheckedException;
import com.inov8.microbank.common.model.SupplierModel;
import com.inov8.microbank.common.wrapper.suppliermodule.SupplierWrapper;
import com.inov8.microbank.common.wrapper.workflow.WorkFlowWrapper;

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
 * @author Rizwan-ur-Rehman
 * @version 1.0
 *
 */

public class SupplierStub
    implements Supplier
{

  public SupplierWrapper verify(SupplierWrapper supplier,
                                WorkFlowWrapper workFlowWrapper) throws
      FrameworkCheckedException
  {
    SupplierWrapper supplierWrapper = supplier;
    SupplierModel supplierModel = new SupplierModel();
    supplierModel = supplierWrapper.getSupplierModel();
    supplierModel.setAddress1("Address 1");
    supplierModel.setName("Operator");
    supplierModel.setContactName("Operator");
//    supplierModel.setClassName(
//        "com.inov8.microbank.server.service.suppliermodule.Operator");
    supplierModel.setActive(Boolean.TRUE);
    supplierModel.setVendor(Boolean.TRUE);
    supplierWrapper.setSupplierModel(supplierModel);
    supplierWrapper.setResponseCode("00");

    return supplierWrapper;
  }

  public SupplierWrapper updateSupplier(SupplierWrapper supplier) throws
      FrameworkCheckedException
  {
    SupplierWrapper supplierWrapper = supplier;
    SupplierModel supplierModel = new SupplierModel();
    supplierModel = supplierWrapper.getSupplierModel();
    supplierModel.setAddress1("Address 1");
    supplierModel.setName("Operator");
    supplierModel.setContactName("Operator");
//    supplierModel.setClassName(
//        "com.inov8.microbank.server.service.suppliermodule.Operator");
    supplierModel.setActive(Boolean.TRUE);
    supplierModel.setVendor(Boolean.TRUE);
    supplierWrapper.setSupplierModel(supplierModel);
    supplierWrapper.setResponseCode("00");

    return supplierWrapper;
  }

  public SupplierWrapper rollback(SupplierWrapper supplier) throws
      FrameworkCheckedException
  {
    SupplierWrapper supplierWrapper = supplier;
    SupplierModel supplierModel = new SupplierModel();
    supplierModel = supplierWrapper.getSupplierModel();
    supplierModel.setAddress1("Address 1");
    supplierModel.setName("Operator");
    supplierModel.setContactName("Operator");
//    supplierModel.setClassName(
//        "com.inov8.microbank.server.service.suppliermodule.Operator");
    supplierModel.setActive(Boolean.TRUE);
    supplierModel.setVendor(Boolean.TRUE);
    supplierWrapper.setSupplierModel(supplierModel);
    supplierWrapper.setResponseCode("00");

    return supplierWrapper;
  }

}
