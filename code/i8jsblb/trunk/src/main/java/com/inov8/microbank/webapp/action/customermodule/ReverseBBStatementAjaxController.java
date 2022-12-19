package com.inov8.microbank.webapp.action.customermodule;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.ajaxtags.helpers.AjaxXmlBuilder;
import org.springframework.web.bind.ServletRequestUtils;

import com.inov8.microbank.common.model.ActionLogModel;
import com.inov8.microbank.common.model.AppUserModel;
import com.inov8.microbank.common.model.SmartMoneyAccountModel;
import com.inov8.microbank.common.model.TransactionCodeModel;
import com.inov8.microbank.common.util.PaymentModeConstantsInterface;
import com.inov8.microbank.common.util.ThreadLocalActionLog;
import com.inov8.microbank.common.util.ThreadLocalAppUser;
import com.inov8.microbank.common.util.UserUtils;
import com.inov8.microbank.common.wrapper.switchmodule.SwitchWrapper;
import com.inov8.microbank.common.wrapper.switchmodule.SwitchWrapperImpl;
import com.inov8.microbank.common.wrapper.workflow.WorkFlowWrapperImpl;
import com.inov8.microbank.server.service.financialintegrationmodule.OLAVeriflyFinancialInstitutionImpl;
import com.inov8.microbank.server.service.portal.mfsaccountmodule.MfsAccountManager;
import com.inov8.microbank.server.service.postedtransactionmodule.PostedTransactionsManager;
import com.inov8.microbank.webapp.action.ajax.AjaxController;
import com.inov8.ola.integration.vo.OLAVO;
import com.inov8.ola.util.EncryptionUtil;
import com.inov8.ola.util.ReasonConstants;
import com.inov8.verifly.common.model.AccountInfoModel;
import com.inov8.verifly.common.model.LogModel;
import com.inov8.verifly.common.wrapper.VeriflyBaseWrapper;
import com.inov8.verifly.common.wrapper.VeriflyBaseWrapperImpl;

public class ReverseBBStatementAjaxController extends AjaxController{

	private OLAVeriflyFinancialInstitutionImpl olaVeriflyFinancialInstitution;
	private MfsAccountManager mfsAccountManager;
	
	@Override
	public String getResponseContent(HttpServletRequest request, HttpServletResponse response) throws Exception{
		AjaxXmlBuilder ajaxXmlBuilder = new AjaxXmlBuilder();
		try{
			Long ledgerId = null;
			boolean result = false;
			String ledgerIdStr = ServletRequestUtils.getStringParameter(request, "ledgerId");
			String trxId = ServletRequestUtils.getStringParameter(request, "trxId");
			String appUserIdEncrypted = ServletRequestUtils.getRequiredStringParameter(request, "appUserId");
	        String appUserIdDecrypted = EncryptionUtil.decryptWithDES( appUserIdEncrypted );
	        Long appUserId = Long.valueOf( appUserIdDecrypted );
	        AppUserModel appUserModel = mfsAccountManager.getAppUserModelByPrimaryKey(appUserId);
	        SmartMoneyAccountModel smaModel = mfsAccountManager.getSmartMoneyAccountByCustomerId(appUserModel.getCustomerId());
	        ThreadLocalAppUser.setAppUserModel(appUserModel);
	        
			if (null != ledgerIdStr && ledgerIdStr.trim().length() > 0 ){
				ledgerId = new Long(ledgerIdStr);
				SwitchWrapper switchWrapper = new SwitchWrapperImpl();
				switchWrapper.setWorkFlowWrapper(new WorkFlowWrapperImpl());
				TransactionCodeModel trxCodeModel = new TransactionCodeModel();
				trxCodeModel.setCode(trxId);
				switchWrapper.getWorkFlowWrapper().setTransactionCodeModel(trxCodeModel);
				OLAVO olaVo = new OLAVO();
				olaVo.setLedgerId(ledgerId);
				olaVo.setReasonId(ReasonConstants.MANNUAL_REVERSAL);
				switchWrapper.setOlavo(olaVo);
				switchWrapper.setPaymentModeId( PaymentModeConstantsInterface.BRANCHLESS_BANKING_ACCOUNT);
				switchWrapper.setBankId(50110L);
				olaVo.setMicrobankTransactionCode(trxId);
				VeriflyBaseWrapper veriflyBaseWrapper = new VeriflyBaseWrapperImpl();
				AccountInfoModel accountInfoModel = new AccountInfoModel();
				accountInfoModel.setCustomerId(appUserModel.getCustomerId());
				accountInfoModel.setAccountNick(smaModel.getName());
	
				LogModel logModel = new LogModel();
				logModel.setCreatdByUserId(appUserId);
				logModel.setCreatedBy(appUserModel.getUsername());
				logModel.setTransactionCodeId(null);
				ActionLogModel actionLogModel = new ActionLogModel();
				if(null == ThreadLocalActionLog.getActionLogId()){
					actionLogModel.setActionLogId(1l);
					ThreadLocalActionLog.setActionLogId(1l);
				}else{

					actionLogModel.setActionLogId(ThreadLocalActionLog.getActionLogId());

				}
				
				veriflyBaseWrapper.setAccountInfoModel(accountInfoModel);
				veriflyBaseWrapper.setLogModel(logModel);
				
				veriflyBaseWrapper.setBasePersistableModel(smaModel);
				
				veriflyBaseWrapper = olaVeriflyFinancialInstitution.verifyCredentials(veriflyBaseWrapper);
				boolean errorMessagesFlag = veriflyBaseWrapper.isErrorStatus();
				if(errorMessagesFlag) {
					switchWrapper.setAccountInfoModel(veriflyBaseWrapper.getAccountInfoModel());
					switchWrapper.getOlavo().setPayingAccNo(veriflyBaseWrapper.getAccountInfoModel().getAccountNo());
					olaVeriflyFinancialInstitution.rollback(switchWrapper);
					result = true;
					
				}
				
//				result = postedTransactionsManager.resetPostedTransaction(trxLogId,resType);
			}
			
			if(result){
				String msgKey = "bbstatement.reversal.success";
	
				ajaxXmlBuilder.addItem("message", getMessage(request, msgKey, new String[] { trxId }));
				ajaxXmlBuilder.addItem("currentBtn", ledgerId.toString());//TODO: 
			}else{
				ajaxXmlBuilder.addItem("message", getMessage(request, "bbstatement.reversal.failure"));
			}
		}catch(Exception ex){
			ex.printStackTrace();
			ajaxXmlBuilder.addItem("message", getMessage(request, "bbstatement.reversal.failure"));
			
		}finally{
			ThreadLocalAppUser.remove();
		}
		
		return ajaxXmlBuilder.toString();
	}

	public void setOlaVeriflyFinancialInstitution(
			OLAVeriflyFinancialInstitutionImpl olaVeriflyFinancialInstitution) {
		this.olaVeriflyFinancialInstitution = olaVeriflyFinancialInstitution;
	}

	public void setMfsAccountManager(MfsAccountManager mfsAccountManager) {
		this.mfsAccountManager = mfsAccountManager;
	}

}
