package com.inov8.microbank.server.dao.handlermodule;

import java.util.List;

import com.inov8.framework.server.dao.framework.BaseDAO;
import com.inov8.microbank.common.model.retailermodule.HandlerSearchViewModel;

/**
 * 
 * <p>
 * Title: Microbank
 * </p>
 * 
 * <p>
 * Description: Backened application for POS terminal
 * </p>
 * 
 * <p>
 * Copyright: Copyright (c) 2006
 * </p>
 * 
 * <p>
 * Company: Inov8 Ltd
 * </p>
 * 
 * @author Atif Hussain
 * @version 1.0
 * 
 */

public interface HandlerSearchViewDAO extends
		BaseDAO<HandlerSearchViewModel, Long> {

	List<HandlerSearchViewModel> searchHandler(HandlerSearchViewModel handlerSearchViewModel);
}
