package com.inov8.microbank.server.service.productmodule;

import com.inov8.framework.common.exception.FrameworkCheckedException;
import com.inov8.framework.common.wrapper.BaseWrapper;
import com.inov8.microbank.common.wrapper.workflow.WorkFlowWrapper;
import com.inov8.microbank.server.service.integration.dispenser.ProductDispenser;
import com.inov8.microbank.server.service.integration.vo.ProductVO;


/**
 *
 * <p>Company: </p>
 *
 * @author Jawwad Farooq
 * @version 1.0
 */

public interface ProductDispenseController
{
	  public ProductDispenser loadProductDispenser( WorkFlowWrapper workFlowWrapper ) throws FrameworkCheckedException;
	  public ProductVO loadProductVO( BaseWrapper baseWrapper ) throws FrameworkCheckedException ;
	  public ProductDispenser loadProductDispenserByProductId(WorkFlowWrapper workFlowWrapper) throws FrameworkCheckedException;
	  public ProductVO loadProductVOWithoutLoadingProd(BaseWrapper baseWrapper) throws FrameworkCheckedException;

}
