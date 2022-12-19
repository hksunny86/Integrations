package com.inov8.microbank.server.service.suppliermodule;

import com.inov8.microbank.common.wrapper.suppliermodule.SupplierWrapper;
import com.inov8.microbank.common.wrapper.workflow.WorkFlowWrapper;

/**
 *
 * @author Jawwad Farooq
 * @version 1.0
 */

public class SupplierController
{
  public SupplierController()
  {
  }

  /**
   * This method delegates the requests to the Supplier's implementation class for some task and retrieves
   * information from the supplier.
   * @param supplierInfo SupplierWrapper
   * @throws Exception
   */
  public void verify(SupplierWrapper supplierInfo,
                     WorkFlowWrapper workFlowWrapper) throws Exception
  {
    Supplier supplier = getSupplier(supplierInfo);
    supplier.verify(supplierInfo, workFlowWrapper);
  }

  /**
   * This method delegates the requests to the Supplier's implementation class and updates
   * the supplier.
   * @param supplierInfo SupplierWrapper
   * @throws Exception
   */
  public void updateSupplier(SupplierWrapper supplierInfo,
                             WorkFlowWrapper workFlowWrapper) throws Exception
  {
    Supplier supplier = getSupplier(supplierInfo);
    supplier.verify(supplierInfo, workFlowWrapper);
  }

  /**
   *
   * @throws Exception
   */
  public void rollback() throws Exception
  {

  }

  /**
   * This method calls the SupplierFactory to get the required Supplier's implementation class
   * @param supplierWrapper SupplierWrapper
   * @return Supplier
   * @throws Exception
   */
  private Supplier getSupplier(SupplierWrapper supplierWrapper) throws
      Exception
  {
    return new SupplierStub();
    /**
     * @todo ****** UN-COMMENT THE FOLLOWING LINE OF CODE ********
     */
//    return SupplierFactory.getSupplier(supplierWrapper);
  }

}
