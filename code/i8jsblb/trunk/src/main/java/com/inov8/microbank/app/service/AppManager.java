package com.inov8.microbank.app.service;

import com.inov8.framework.common.exception.FrameworkCheckedException;
import com.inov8.framework.common.wrapper.BaseWrapper;
import com.inov8.framework.common.wrapper.SearchBaseWrapper;
import com.inov8.microbank.app.vo.AppInfoVO;
import com.inov8.microbank.app.vo.AppVO;

import java.util.List;

public interface AppManager
{
	AppVO validateAppVersion(BaseWrapper baseWrapper) throws FrameworkCheckedException;

	List<AppInfoVO> loadAppInfoList(Long appUserTypeId) throws FrameworkCheckedException;

	String getDefaultAppURL(Long appUserTypeId) throws FrameworkCheckedException;

	SearchBaseWrapper loadApplicationVersion(SearchBaseWrapper searchBaseWrapper) throws
			FrameworkCheckedException;

	BaseWrapper loadApplicationVersion(BaseWrapper baseWrapper) throws
			FrameworkCheckedException;

	SearchBaseWrapper searchApplicationVersion(SearchBaseWrapper searchBaseWrapper) throws
			FrameworkCheckedException;

	BaseWrapper updateApplicationVersion(BaseWrapper baseWrapper) throws
			FrameworkCheckedException;

	BaseWrapper createApplicationVersion(BaseWrapper baseWrapper) throws
			FrameworkCheckedException;
	
	
	public void sendSMSToUsers(String userName,String pin,boolean isConsumerApp);
}
