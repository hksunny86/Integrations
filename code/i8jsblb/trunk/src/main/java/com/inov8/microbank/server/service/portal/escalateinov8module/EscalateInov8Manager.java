package com.inov8.microbank.server.service.portal.escalateinov8module;

import com.inov8.framework.common.exception.FrameworkCheckedException;
import com.inov8.framework.common.wrapper.BaseWrapper;
import com.inov8.framework.common.wrapper.SearchBaseWrapper;

/**
 * <p>
 * Title:
 * </p>
 * 
 * <p>
 * Description:
 * </p>
 * 
 * <p>
 * Copyright: Copyright (c) 2006
 * </p>
 * 
 * <p>
 * Company:
 * </p>
 * 
 * @author Asad Hayat
 * @version 1.0
 */

public interface EscalateInov8Manager {

	SearchBaseWrapper searchEscalateInov8Status(
			SearchBaseWrapper searchBaseWrapper)
			throws FrameworkCheckedException;

	BaseWrapper retrieveI8EscalateForm(BaseWrapper baseWrapper)
			throws FrameworkCheckedException;

	BaseWrapper makeResolveDispute(BaseWrapper baseWrapper)
			throws FrameworkCheckedException;

}
