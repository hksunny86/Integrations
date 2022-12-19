package com.inov8.microbank.server.service.suppliermodule;

import com.inov8.framework.common.exception.FrameworkCheckedException;
import com.inov8.microbank.common.wrapper.suppliermodule.SupplierWrapper;
import com.inov8.microbank.common.wrapper.workflow.WorkFlowWrapper;


/**
 * <p>Title: </p>
 *
 * <p>Description: </p>
 *
 * <p>Copyright: Copyright (c) 2006</p>
 *
 * <p>Company: </p>
 *
 * @author Jawwad Farooq
 * @version 1.0
 */

public class LescoStub
    implements Supplier
{
//  private FieldGroupManager fieldGroupManager;
  /**
   * rollback
   *
   * @param transactions SupplierWrapper
   * @return SupplierWrapper
   * @throws FrameworkCheckedException
   * @todo Implement this com.inov8.microbank.server.service.suppliermodule.Supplier method
   */
  public SupplierWrapper rollback(SupplierWrapper transactions) throws
      FrameworkCheckedException
  {
    return null;
  }

  /**
   * updateSupplier
   *
   * @param supplier SupplierWrapper
   * @return SupplierWrapper
   * @throws FrameworkCheckedException
   * @todo Implement this com.inov8.microbank.server.service.suppliermodule.Supplier method
   */
  public SupplierWrapper updateSupplier(SupplierWrapper supplier) throws
      FrameworkCheckedException
  {
    return null;
  }

  /**
   * verify
   *
   * @param supplier SupplierWrapper
   * @return SupplierWrapper
   * @throws FrameworkCheckedException
   * @todo Implement this com.inov8.microbank.server.service.suppliermodule.Supplier method
   */
  public SupplierWrapper verify(SupplierWrapper supplier,
                                WorkFlowWrapper workFlowWrapper) throws
      FrameworkCheckedException
  {
/*
    if (workFlowWrapper.getScreenModel().
        getActionIdActionModel().getName().equalsIgnoreCase(SupplierConstants.
        LESCO_GET_BILL_INFO))
    {
      BaseWrapper baseWrapper = new BaseWrapperImpl();
      Long fieldGroupID = workFlowWrapper.getScreenModel().
          getOutputFieldGroupIdFieldGroupModel().getPrimaryKey();
      baseWrapper.setBasePersistableModel(workFlowWrapper.getScreenModel().
                                          getOutputFieldGroupIdFieldGroupModel());

//      FieldGroupModel fieldGroup = new FieldGroupModel();
//
//      fieldGroup = (FieldGroupModel)this.fieldGroupManager.loadFieldGroup(
//          baseWrapper).getBasePersistableModel();

      try
      {
//        List<FieldGroupFieldModel> fields = (List<FieldGroupFieldModel>)
//            fieldGroup.
//            getFieldGroupIdFieldGroupFieldModelList();

        int transactionDetailCounter = 0;

        TransactionDetailModel txModel = (TransactionDetailModel) ( (List)
            workFlowWrapper.getTransactionModel().
            getTransactionIdTransactionDetailModelList()).get(0);
        txModel.setCustomField1("2500.00");
        txModel.setCustomField2("September 2006");
        txModel.setCustomField3("445522331155");
        txModel.setCustomField4("October 7, 2006");

//        for (FieldGroupFieldModel fieldGroupField : fields)
//        {
//          System.out.println("()()()" +
//                             fieldGroupField.getCustomFieldIdCustomFieldModel().
//                             getName());
//        }
      }
      catch (Exception ex)
      {
        ex.printStackTrace();
        throw new FrameworkCheckedException("", ex);
      }
    }
*/
    return supplier;
  }

//  public void setFieldGroupManager(FieldGroupManager fieldGroupManager)
//  {
//    this.fieldGroupManager = fieldGroupManager;
//  }
}
