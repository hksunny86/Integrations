package com.inov8.microbank.server.service.smartmoneymodule;

import com.inov8.framework.common.exception.FrameworkCheckedException;
import com.inov8.framework.common.util.CustomList;
import com.inov8.framework.common.wrapper.BaseWrapper;
import com.inov8.framework.common.wrapper.SearchBaseWrapper;
import com.inov8.microbank.common.model.AppUserModel;
import com.inov8.microbank.common.model.RetailerContactModel;
import com.inov8.microbank.common.model.SmartMoneyAccountModel;

/**
 * <p>Title: </p>
 *
 * <p>Description: </p>
 *
 * <p>Copyright: Copyright (c) 2006</p>
 *
 * <p>Company: </p>
 *
 * @author Asad Hayat
 * @version 1.0
 */

public interface SmartMoneyAccountManager
{
	public SmartMoneyAccountModel getSMALinkedWithCoreAccount(Long retailerContactId) throws FrameworkCheckedException;
	
	SearchBaseWrapper loadSmartMoneyAccount(SearchBaseWrapper searchBaseWrapper) throws FrameworkCheckedException;

	BaseWrapper loadSmartMoneyAccount(BaseWrapper baseWrapper) throws FrameworkCheckedException;

	BaseWrapper loadOLASmartMoneyAccount(BaseWrapper baseWrapper) throws FrameworkCheckedException;

	SearchBaseWrapper searchSmartMoneyAccount(SearchBaseWrapper searchBaseWrapper) throws FrameworkCheckedException;

	BaseWrapper updateSmartMoneyAccount(BaseWrapper baseWrapper) throws FrameworkCheckedException;

	BaseWrapper createSmartMoneyAccount(BaseWrapper baseWrapper) throws FrameworkCheckedException;

	public BaseWrapper searchSmartMoneyAccount(BaseWrapper baseWrapper) throws FrameworkCheckedException;

	// Added by Jawwad - To get the OLA account for retailer or distributor head
	public BaseWrapper loadOLASMAForRetOrDistHead(BaseWrapper baseWrapper) throws FrameworkCheckedException;
	
	public SmartMoneyAccountModel getSMAccountByRetailerContactId(Long retailerContactId)  throws FrameworkCheckedException;

	public AppUserModel getAppUserModel(AppUserModel appUserModel);
	
	public SmartMoneyAccountModel getSMAccountByRetailer(RetailerContactModel retailerContactModel) throws FrameworkCheckedException;
	
	public int countSmartMoneyAccountModel(BaseWrapper baseWrapper) throws FrameworkCheckedException;
	public CustomList<SmartMoneyAccountModel> loadCustomerSmartMoneyAccountByHQL( SmartMoneyAccountModel smartMoneyAccountModel ) throws FrameworkCheckedException;
	public SmartMoneyAccountModel getSMAccountByHandlerId(Long handlerId) throws FrameworkCheckedException;
	
	public void blockSmartMoneyAccount(AppUserModel appUserModel) throws Exception;
	
	public abstract SmartMoneyAccountModel loadSmartMoneyAccountModel(AppUserModel appUserModel) throws FrameworkCheckedException;

	public abstract SmartMoneyAccountModel loadSmartMoneyAccountModel(AppUserModel appUserModel, Long paymentModeId) throws FrameworkCheckedException ;

	public BaseWrapper updateSmartMoneyAccountDormancyWithAuthorization(BaseWrapper baseWrapper) throws FrameworkCheckedException;


	SmartMoneyAccountModel getSmartMoneyAccountByCustomerIdAndPaymentModeId(SmartMoneyAccountModel smartMoneyAccountModel) throws FrameworkCheckedException;

	SmartMoneyAccountModel getInActiveSMA(AppUserModel appUserModel, Long paymentModeId,Long statusId);

	//Added by Abubakar
	public BaseWrapper updateDebitBlockWithAuthorization(BaseWrapper baseWrapper) throws FrameworkCheckedException;
	public BaseWrapper updateDebitBlock(Long appUserId, Double amount,Boolean isDebitBlocked) throws FrameworkCheckedException;
	void validateDebitBlock(SmartMoneyAccountModel smaModel, Double txAmount, Double balance) throws FrameworkCheckedException;
}
