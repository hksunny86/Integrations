package com.inov8.microbank.server.service.lescomodule;

import com.inov8.framework.common.exception.FrameworkCheckedException;
import com.inov8.framework.common.wrapper.BaseWrapper;
import com.inov8.framework.common.wrapper.SearchBaseWrapper;

public interface LescoCollectionManager
{
	public BaseWrapper createLescoCollection(BaseWrapper baseWrapper) throws FrameworkCheckedException;
	public SearchBaseWrapper searchLescoCollection(SearchBaseWrapper searchBaseWrapper) throws FrameworkCheckedException;
	public SearchBaseWrapper searchLescoLog(SearchBaseWrapper searchBaseWrapper)throws FrameworkCheckedException;
	public BaseWrapper saveOrUpdateLescoCollection(BaseWrapper baseWrapper) throws FrameworkCheckedException;
	public BaseWrapper loadLescoCollection(BaseWrapper baseWrapper) throws FrameworkCheckedException;
	
	
}
