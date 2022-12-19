package com.inov8.microbank.server.service.productdeviceflowmodule;

import com.inov8.framework.common.exception.FrameworkCheckedException;
import com.inov8.framework.common.wrapper.BaseWrapper;
import com.inov8.framework.common.wrapper.SearchBaseWrapper;
import com.inov8.microbank.common.model.ProductDeviceFlowModel;

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

public interface ProductDeviceFlowManager {
	public BaseWrapper createProductDeviceFlow(BaseWrapper baseWrapper) throws FrameworkCheckedException;
	public BaseWrapper updateProductDeviceFlow(BaseWrapper baseWrapper) throws FrameworkCheckedException;
	public BaseWrapper loadProductDeviceFlowByPrimaryKey(BaseWrapper baseWrapper) throws FrameworkCheckedException;
	public SearchBaseWrapper searchProductDeviceFlowListView(SearchBaseWrapper searchBaseWrapper) throws FrameworkCheckedException;
	public int  isproductDeviceFlow(ProductDeviceFlowModel productDeviceFlowModel) throws FrameworkCheckedException;
}
