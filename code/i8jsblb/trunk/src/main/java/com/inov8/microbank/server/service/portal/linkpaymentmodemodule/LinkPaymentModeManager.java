package com.inov8.microbank.server.service.portal.linkpaymentmodemodule;

import com.inov8.framework.common.exception.FrameworkCheckedException;
import com.inov8.framework.common.wrapper.BaseWrapper;
import com.inov8.framework.common.wrapper.SearchBaseWrapper;

public interface LinkPaymentModeManager {

	SearchBaseWrapper loadLinkPaymentMode(SearchBaseWrapper searchBaseWrapper) throws
	FrameworkCheckedException;

	BaseWrapper loadLinkPaymentMode(BaseWrapper baseWrapper) throws
	FrameworkCheckedException;

	SearchBaseWrapper searchLinkPaymentMode(SearchBaseWrapper searchBaseWrapper) throws
	FrameworkCheckedException;

	BaseWrapper updateLinkPaymentMode(BaseWrapper baseWrapper) throws
	FrameworkCheckedException;

	BaseWrapper createLinkPaymentMode(BaseWrapper baseWrapper) throws
	FrameworkCheckedException;
	
	SearchBaseWrapper searchAccounts(SearchBaseWrapper searchBaseWrapper) throws
	FrameworkCheckedException;
	
	BaseWrapper getSmartMoneyAccountInfo(BaseWrapper baseWrapper) throws 
	FrameworkCheckedException;
	
	BaseWrapper changeAccountNick(BaseWrapper baseWrapper) throws 
	FrameworkCheckedException;
	
	BaseWrapper loadUserInfoListViewByPrimaryKey(BaseWrapper baseWrapper) throws 
	FrameworkCheckedException;
	
	BaseWrapper loadAllPayUserInfoListViewByPrimaryKey(BaseWrapper baseWrapper) throws 
	FrameworkCheckedException;
	
	BaseWrapper loadUserInfoListViewByMfsId(BaseWrapper baseWrapper) throws
	FrameworkCheckedException;
	
	BaseWrapper loadUserInfoListViewByAllPayId(BaseWrapper baseWrapper) throws
	FrameworkCheckedException;	
	public  boolean isFirstSmartMoneyAccount(Long customerId,Long custType);
	public boolean isFirstAccountOtherThanOla(String allpayId);
	public BaseWrapper createLinkPaymentModeForBulk(BaseWrapper baseWrapper) throws FrameworkCheckedException;

	BaseWrapper createLinkPaymentModeForL3(BaseWrapper baseWrapper) throws FrameworkCheckedException;
}
