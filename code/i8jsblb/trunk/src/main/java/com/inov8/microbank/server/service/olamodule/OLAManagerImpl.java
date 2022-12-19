package com.inov8.microbank.server.service.olamodule;

import java.sql.Timestamp;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import org.springframework.web.bind.ServletRequestBindingException;
import org.springframework.web.bind.ServletRequestUtils;

import com.inov8.framework.common.exception.FrameworkCheckedException;
import com.inov8.framework.common.wrapper.BaseWrapper;
import com.inov8.framework.common.wrapper.BaseWrapperImpl;
import com.inov8.framework.common.wrapper.SearchBaseWrapper;
import com.inov8.microbank.common.exception.WorkFlowException;
import com.inov8.microbank.common.model.ActionLogModel;
import com.inov8.microbank.common.model.AppUserModel;
import com.inov8.microbank.common.model.BankModel;
import com.inov8.microbank.common.util.PortalConstants;
import com.inov8.microbank.common.util.ThreadLocalActionLog;
import com.inov8.microbank.common.util.UserUtils;
import com.inov8.microbank.common.util.WorkFlowErrorCodeConstants;
import com.inov8.microbank.common.wrapper.switchmodule.SwitchWrapper;
import com.inov8.microbank.common.wrapper.switchmodule.SwitchWrapperImpl;
import com.inov8.microbank.server.service.actionlogmodule.ActionLogManager;
import com.inov8.microbank.server.service.financialintegrationmodule.FinancialInstitution;
import com.inov8.microbank.server.service.financialintegrationmodule.switchmodule.ola.OLASwitchImpl;
import com.inov8.ola.integration.vo.OLAVO;
import com.inov8.ola.server.service.account.AccountManager;

public class OLAManagerImpl implements OLAManager {
	FinancialInstitution olaVeriflyFinancialInstitution;
	private AccountManager accountManager;
	private ActionLogManager actionLogManager;
	
	protected static Log logger	= LogFactory.getLog(OLAManagerImpl.class);
	
	
	public OLAVO makeTxForQueue(OLAVO olaVO) throws Exception
	{
		try
	    {
			olaVO = this.accountManager.makeTx(olaVO);
			
			processOLAResponseCode(olaVO.getResponseCode());
		      
	    }
	    catch (Exception ex)
	    {
	    	logger.error(ex);
		    throw ex ;
	    }
	    
	    return olaVO;		
	}
	
	/*
	 * Helper method to translate OLA Response Codes.
	 * 
	 * if Response Codes is other than "00" then send appropriate error message
	 */
	private void processOLAResponseCode(String responseCode) throws WorkFlowException{
		
		logger.info("[OLASwitchImpl.processOLAResponseCode] ==> OLA Resp Code:"+responseCode);
		
		switch (responseCode) {
		
		case "00" : break;
		
		case "01" : throw new WorkFlowException( WorkFlowErrorCodeConstants.INSUFFICIENT_ACC_BALANCE );
		
		case "02" : throw new WorkFlowException( WorkFlowErrorCodeConstants.INACTIVE_ACCOUNT );
		
		case "03" : throw new WorkFlowException( WorkFlowErrorCodeConstants.DELETED_ACCOUNT );
		
		case "04" : throw new WorkFlowException( WorkFlowErrorCodeConstants.DATA_TYPE_MISMATCH );

		case "08" : throw new WorkFlowException( WorkFlowErrorCodeConstants.GENERAL_ERROR );
		
		case "09" : throw new WorkFlowException( WorkFlowErrorCodeConstants.DAILY_CREDIT_LIMIT_BUSTED );
		
		case "10" : throw new WorkFlowException( WorkFlowErrorCodeConstants.GENERAL_ERROR );
		
		case "11" : throw new WorkFlowException( WorkFlowErrorCodeConstants.MONTHLY_CREDIT_LIMIT_BUSTED );
		
		case "12" : throw new WorkFlowException( WorkFlowErrorCodeConstants.GENERAL_ERROR );
		
		case "13" : throw new WorkFlowException( WorkFlowErrorCodeConstants.YEARLY_CREDIT_LIMIT_BUSTED );
		
		case "14" : throw new WorkFlowException( WorkFlowErrorCodeConstants.GENERAL_ERROR );
		
		case "15" : throw new WorkFlowException( WorkFlowErrorCodeConstants.BALANCE_LIMIT_BUSTED );
		
		case "16" : throw new WorkFlowException( WorkFlowErrorCodeConstants.GENERAL_ERROR );
		
		case "17" : throw new WorkFlowException( WorkFlowErrorCodeConstants.DAILY_DEBIT_LIMIT_BUSTED );
		
		case "18" : throw new WorkFlowException( WorkFlowErrorCodeConstants.GENERAL_ERROR );
		
		case "19" : throw new WorkFlowException( WorkFlowErrorCodeConstants.MONTHLY_DEBIT_LIMIT_BUSTED );
		
		case "20" : throw new WorkFlowException( WorkFlowErrorCodeConstants.GENERAL_ERROR );
		
		case "21" : throw new WorkFlowException( WorkFlowErrorCodeConstants.YEARLY_DEBIT_LIMIT_BUSTED );
			
		
		
		case "29" : throw new WorkFlowException( WorkFlowErrorCodeConstants.MONTHLY_THROUGHPUT_LIMIT_NOT_DEFINED );
		
		case "30" : throw new WorkFlowException( WorkFlowErrorCodeConstants.MONTHLY_CREDIT_THROUGHPUT_LIMIT_BUSTED );
		
		case "31" : throw new WorkFlowException( WorkFlowErrorCodeConstants.MONTHLY_THROUGHPUT_LIMIT_EXCEPTION );
		
		case "32" : throw new WorkFlowException( WorkFlowErrorCodeConstants.MONTHLY_DEBIT_THROUGHPUT_LIMIT_BUSTED );

		case "99" : throw new WorkFlowException( WorkFlowErrorCodeConstants.USABLE_ACCOUNT_BALANCE_INSUFFICIENT );
		
		default : {
			throw new WorkFlowException( WorkFlowErrorCodeConstants.TRANSACTION_TIMEOUT ) ; 
		}		

			
		}
	}
	
	
	
	public BaseWrapper createAccount(BaseWrapper baseWrapper, HttpServletRequest httpServletRequest) throws WorkFlowException, Exception {
		
		BankModel bankModel = (BankModel)httpServletRequest.getAttribute("bankModel");
		SwitchWrapper sWrapper = new SwitchWrapperImpl();
		AppUserModel appUserModel = UserUtils.getCurrentUser();
		if (httpServletRequest.getParameter(PortalConstants.KEY_ACTION_ID) != null) {
			// if (request.getParameter(KEY_ACTION_NAME).equalsIgnoreCase("1"))
			// {

			sWrapper.putObject(PortalConstants.KEY_ACTION_ID, httpServletRequest
					.getParameter(PortalConstants.KEY_ACTION_ID));
			sWrapper.putObject(PortalConstants.KEY_USECASE_ID,
					PortalConstants.OLA_CREATE_ACCOUNT);
		}
		ActionLogModel actionLogModel = this.logBeforeAction(ServletRequestUtils.getLongParameter(
				httpServletRequest, PortalConstants.KEY_ACTION_ID),
				PortalConstants.OLA_CREATE_ACCOUNT, appUserModel
						.getAppUserId());
		ThreadLocalActionLog.setActionLogId(actionLogModel.getActionLogId());
		if (bankModel == null){
		sWrapper.setBankId(UserUtils.getCurrentUser().getBankUserIdBankUserModel()
				.getBankId());
		}else{
			sWrapper.setBankId(bankModel.getBankId());
		}
		OLAVO ola = (OLAVO)baseWrapper.getObject("olaVo");
		if (bankModel == null){
		sWrapper.setBankId(UserUtils.getCurrentUser().getBankUserIdBankUserModel()
				.getBankId());
		}else{
			sWrapper.setBankId(bankModel.getBankId());	
		}
		sWrapper.setOlavo(ola);		
	try
	{
		sWrapper = olaVeriflyFinancialInstitution.createAccount(sWrapper);
	}
	catch(Exception e)
	{
		e.printStackTrace();
		
		throw new FrameworkCheckedException(WorkFlowErrorCodeConstants.PHOENIX_SERVICE_DOWN_MSG);
		
		
	
	}
		baseWrapper.putObject("olaVo", sWrapper.getOlavo());
		logAfterAction(actionLogModel,String.valueOf(appUserModel.getAppUserId()));										
		return baseWrapper;
	}

	public BaseWrapper loadAccount(BaseWrapper baseWrapper, HttpServletRequest httpServletRequest) throws FrameworkCheckedException {
		// TODO Auto-generated method stub
		return null;
	}

	public SearchBaseWrapper loadAccount(SearchBaseWrapper searchBaseWrapper) throws FrameworkCheckedException {
		// TODO Auto-generated method stub
		return null;
	}

	public List<OLAVO> makeAccount(SearchBaseWrapper searchBaseWrapper ,HttpServletRequest httpServletRequest ) throws FrameworkCheckedException, ServletRequestBindingException{

		SwitchWrapper sWrapper = new SwitchWrapperImpl();
		AppUserModel appUserModel = UserUtils.getCurrentUser();
		if (httpServletRequest.getParameter(PortalConstants.KEY_ACTION_ID) != null) {
			// if (request.getParameter(KEY_ACTION_NAME).equalsIgnoreCase("1"))
			// {

			sWrapper.putObject(PortalConstants.KEY_ACTION_ID, httpServletRequest
					.getParameter(PortalConstants.KEY_ACTION_ID));
			sWrapper.putObject(PortalConstants.KEY_USECASE_ID,
					PortalConstants.OLA_SEARCH_ACCOUNT);
		}
		ActionLogModel actionLogModel = this.logBeforeAction(ServletRequestUtils.getLongParameter(
				httpServletRequest, PortalConstants.KEY_ACTION_ID),
				PortalConstants.OLA_SEARCH_ACCOUNT, appUserModel
						.getAppUserId());
		ThreadLocalActionLog.setActionLogId(actionLogModel.getActionLogId());
		sWrapper.setBankId(UserUtils.getCurrentUser().getBankUserIdBankUserModel()
				.getBankId());
		
		sWrapper.setOlavo((OLAVO)searchBaseWrapper.getObject(OLAVO.class.getName()));
	try
	{
		sWrapper = olaVeriflyFinancialInstitution
				.getAllOlaAccounts(sWrapper);
	}
	catch(Exception e)
	{
		throw new FrameworkCheckedException(WorkFlowErrorCodeConstants.PHOENIX_SERVICE_DOWN_MSG);
	
	}
		List<OLAVO> olaList = sWrapper.getOlaAccountsList();				
		logAfterAction(actionLogModel,String.valueOf(appUserModel.getAppUserId()));
		return olaList;
	}

	
	public BaseWrapper updateAccount(BaseWrapper baseWrapper, HttpServletRequest httpServletRequest) throws FrameworkCheckedException, ServletRequestBindingException {
		BankModel bankModel = (BankModel)httpServletRequest.getAttribute("bankModel");
		SwitchWrapper sWrapper = new SwitchWrapperImpl();
		AppUserModel appUserModel = UserUtils.getCurrentUser();
		if (httpServletRequest.getParameter(PortalConstants.KEY_ACTION_ID) != null) {
			// if (request.getParameter(KEY_ACTION_NAME).equalsIgnoreCase("1"))
			// {

			sWrapper.putObject(PortalConstants.KEY_ACTION_ID, httpServletRequest
					.getParameter(PortalConstants.KEY_ACTION_ID));
			sWrapper.putObject(PortalConstants.KEY_USECASE_ID,
					PortalConstants.OLA_UPDATE_ACCOUNT);
		}
		ActionLogModel actionLogModel = this.logBeforeAction(ServletRequestUtils.getLongParameter(
				httpServletRequest, PortalConstants.KEY_ACTION_ID),
				PortalConstants.OLA_UPDATE_ACCOUNT, appUserModel
						.getAppUserId());
		ThreadLocalActionLog.setActionLogId(actionLogModel.getActionLogId());
		if (bankModel == null){
		sWrapper.setBankId(UserUtils.getCurrentUser().getBankUserIdBankUserModel()
				.getBankId());
		sWrapper.setBankId(UserUtils.getCurrentUser().getBankUserIdBankUserModel()
				.getBankId());
		}else{
			sWrapper.setBankId(bankModel.getBankId());
			sWrapper.setBankId(bankModel.getBankId());
		}
		OLAVO ola = (OLAVO)baseWrapper.getObject("olaVo");		
		
		sWrapper.setOlavo(ola);
		try
		{
		olaVeriflyFinancialInstitution.updateAccountInfo(sWrapper);
		}
		catch(Exception e)
		{
			throw new FrameworkCheckedException(WorkFlowErrorCodeConstants.PHOENIX_SERVICE_DOWN_MSG);
		
		}
		finally{
		logAfterAction(actionLogModel,String.valueOf(appUserModel.getAppUserId()));
		}
		
		return baseWrapper;

	}

	private ActionLogModel logBeforeAction(Long actionId, Long useCaseId,
			Long appUserId) throws FrameworkCheckedException {
		ActionLogModel actionLogModel = new ActionLogModel();
		actionLogModel.setActionId(actionId);
		actionLogModel.setUsecaseId(useCaseId);
		actionLogModel.setActionStatusId(PortalConstants.ACTION_STATUS_START); // the
		// process
		// is
		// starting
		actionLogModel.setCustomField1(String.valueOf(appUserId));
		actionLogModel.setAppUserId(UserUtils.getCurrentUser().getAppUserId());
		actionLogModel.setUserName(UserUtils.getCurrentUser().getUsername());
		actionLogModel.setStartTime(new Timestamp(new Date().getTime()));
		actionLogModel.setDeviceTypeId(PortalConstants.WEB_TYPE_DEVICE);
		BaseWrapper baseWrapperActionLog = new BaseWrapperImpl();
		baseWrapperActionLog.setBasePersistableModel(actionLogModel);
		baseWrapperActionLog = this.actionLogManager
				.createOrUpdateActionLogRequiresNewTransaction(baseWrapperActionLog);
		return actionLogModel;
	}

	private void logAfterAction(ActionLogModel actionLogModel, String appUserId)
			throws FrameworkCheckedException {
		BaseWrapper baseWrapperActionLog = new BaseWrapperImpl();
		actionLogModel.setActionStatusId(PortalConstants.ACTION_STATUS_END); // the
		// process
		// is
		// starting

		actionLogModel.setEndTime(new Timestamp(new Date().getTime()));
		baseWrapperActionLog.setBasePersistableModel(actionLogModel);
		baseWrapperActionLog = this.actionLogManager
				.createOrUpdateActionLog(baseWrapperActionLog);
	}
	
	public ActionLogManager getActionLogManager() {
		return actionLogManager;
	}

	public void setActionLogManager(ActionLogManager actionLogManager) {
		this.actionLogManager = actionLogManager;
	}

	public FinancialInstitution getOlaVeriflyFinancialInstitution() {
		return olaVeriflyFinancialInstitution;
	}

	public void setOlaVeriflyFinancialInstitution(
			FinancialInstitution olaVeriflyFinancialInstitution) {
		this.olaVeriflyFinancialInstitution = olaVeriflyFinancialInstitution;
	}
	
	public void setAccountManager(AccountManager accountManager) {
		this.accountManager = accountManager;
	}

}
