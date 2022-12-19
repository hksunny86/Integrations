package com.inov8.microbank.common.util;

import com.inov8.framework.common.exception.FrameworkCheckedException;
import com.inov8.microbank.common.model.RetailerModel;

public interface FranchiseCreator{
	public abstract void send(RetailerModel message) throws FrameworkCheckedException;
}