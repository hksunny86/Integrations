package com.inov8.microbank.webapp.action.common;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.inov8.integration.i8sb.constants.I8SBConstants;
import com.inov8.integration.i8sb.vo.I8SBSwitchControllerRequestVO;
import com.inov8.integration.i8sb.vo.I8SBSwitchControllerResponseVO;
import com.inov8.microbank.common.util.*;
import com.inov8.microbank.server.service.financialintegrationmodule.switchmodule.ESBAdapter;
import org.ajaxtags.helpers.AjaxXmlBuilder;
import org.apache.commons.lang.StringEscapeUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.ServletRequestUtils;

import com.inov8.microbank.common.model.BBAccountsViewModel;
import com.inov8.microbank.common.model.StakeholderBankInfoModel;
import com.inov8.microbank.common.wrapper.switchmodule.SwitchWrapper;
import com.inov8.microbank.common.wrapper.switchmodule.SwitchWrapperImpl;
import com.inov8.microbank.common.wrapper.workflow.WorkFlowWrapper;
import com.inov8.microbank.common.wrapper.workflow.WorkFlowWrapperImpl;
import com.inov8.microbank.server.service.financialintegrationmodule.AbstractFinancialInstitution;
import com.inov8.microbank.server.service.financialintegrationmodule.switchmodule.phoenix.PhoenixConstantsInterface;
import com.inov8.microbank.server.service.manualadjustmentmodule.ManualAdjustmentManager;
import com.inov8.microbank.server.service.stakeholdermodule.StakeholderBankInfoManager;
import com.inov8.microbank.server.service.switchmodule.iris.model.CustomerAccount;
import com.inov8.microbank.webapp.action.ajax.AjaxController;
import com.inov8.ola.util.CustomerAccountTypeConstants;

public class FetchTitleManualAdjustmentController extends AjaxController {


    private static Log logger = LogFactory.getLog(FetchTitleManualAdjustmentController.class);
    private ManualAdjustmentManager manualAdjustmentManager;
    private StakeholderBankInfoManager stkholderBankInfoManager;
    private AbstractFinancialInstitution phoenixFinancialInstitution;

    @Autowired
    private ESBAdapter esbAdapter;

    /* (non-Javadoc)
     * @see com.inov8.microbank.webapp.action.ajax.AjaxController#getResponseContent(javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)
     */
    @Override
    public String getResponseContent(HttpServletRequest request, HttpServletResponse response) throws Exception {
        ThreadLocalAppUser.setAppUserModel(UserUtils.getCurrentUser());
        AjaxXmlBuilder ajaxXmlBuilder = new AjaxXmlBuilder();
        String name = "null";
        String errMsg = null;
        String accountNo = "";
        Boolean isBulk = false;
        String acType = ServletRequestUtils.getRequiredStringParameter(request, "type");
        if (acType.equals("1")) {
            accountNo = ServletRequestUtils.getRequiredStringParameter(request, "accountNo");
            isBulk = ServletRequestUtils.getBooleanParameter(request, "isBulk");
            isBulk = (isBulk == null) ? false : isBulk; // null check
            logger.info("BB A/C No " + accountNo);
            if (StringUtil.isNullOrEmpty(accountNo)) {
                errMsg = "Please enter Account Number";
                name = null;
            } else {
                BBAccountsViewModel model = new BBAccountsViewModel();
                model.setAccountNumber(EncryptionUtil.encryptWithDES(accountNo));
                model = manualAdjustmentManager.getBBAccountsViewModel(model);
                if (model != null) {
                    if (model.getAccountTypeId() != CustomerAccountTypeConstants.LEVEL_2_CORPORATE && isBulk == true) {
                        errMsg = "Account Type should be  L2";
                    } else {
                        name = model.getTitle();
                        if (name == null || name.equals("null") || name.trim().equals("")) {
                            errMsg = "Account does not exist against provided account number";
                        } else if (model.getAccountTypeId() != null && model.getAccountTypeId() == CustomerAccountTypeConstants.SETTLEMENT) {
                            logger.info("Settlement Acc Type loaded against accNumber:" + accountNo + " ... so SKIPPING account status/active check");
                        } else {
                            // Agent / Customer scenario
                            if (model.getIsActive() == null || !model.getIsActive()) {
                                errMsg = "Account is not active against provided account number";
                                name = null;
                            } else if (model.getStatusId() == null || (model.getStatusId().longValue() != 1 && model.getStatusId().longValue() != 4)) {
                                errMsg = "Account Status is not active against provided account number";
                                name = null;
                            } else if (model.getAcState() != null && model.getAcState().equalsIgnoreCase("CLOSED")) {
                                errMsg = "Account is CLOSED against provided account number";
                                name = null;
                            }
                        }
                    }
                } else {
                    errMsg = "Account does not exist against provided account number";
                }
            }
        } else if (acType.equals("2")) {
            SwitchWrapper switchWrapper = new SwitchWrapperImpl();
            switchWrapper.setBankId(CommissionConstantsInterface.BANK_ID);
            switchWrapper.setPaymentModeId(PaymentModeConstantsInterface.CORE_BANKING_ACCOUNT);
            CustomerAccount custAcct = new CustomerAccount();
            WorkFlowWrapper workFlowWrapper = new WorkFlowWrapperImpl();
            switchWrapper.setWorkFlowWrapper(workFlowWrapper);
            switchWrapper.setCustomerAccount(custAcct);
            logger.info("Core bank A/C No " + ServletRequestUtils.getRequiredStringParameter(request, "accountNo"));
            switchWrapper.getCustomerAccount().setNumber(ServletRequestUtils.getRequiredStringParameter(request, "accountNo"));
            switchWrapper.getCustomerAccount().setType(PhoenixConstantsInterface.CURRENT_ACCOUNT_TYPE);
            switchWrapper.getCustomerAccount().setCurrency(PhoenixConstantsInterface.DEFAULT_CURRENCY_TYPE);
            try {

                    if ((ServletRequestUtils.getStringParameter(request, "productId") != null && ServletRequestUtils.getRequiredStringParameter(request, "productId").equals(MessageUtil.getMessage("SalaryDisbursementProductId")))) {//set product Id of Salary Disbursement
                        I8SBSwitchControllerRequestVO requestVO = ESBAdapter.prepareTitleFetchForSalaryDisbursement
                                (I8SBConstants.RequestType_IBFTTitleFetch);

                        requestVO.setCurrencyCode(PhoenixConstantsInterface.DEFAULT_CURRENCY_TYPE);
                        requestVO.setAccountId1(EncryptionUtil.encryptWithAES("682ede816988e58fb6d057d9d85605e0",
                                ServletRequestUtils.getRequiredStringParameter(request, "accountNo")));
                        requestVO.setAccountId2(EncryptionUtil.encryptWithAES("682ede816988e58fb6d057d9d85605e0",
                                ServletRequestUtils.getRequiredStringParameter(request, "accountNo")));
                        requestVO.setTransactionCodeDesc("Salary Disbursement");
//					requestVO.setBankId(String.valueOf(CommissionConstantsInterface.BANK_ID));
//					requestVO.setCardEmborsingName(debitCardModel.getDebitCardEmbosingName());
//					requestVO.setCardBranchCode(MessageUtil.getMessage("debit.card.branch.code"));
//					requestVO.setIssuedDate(String.valueOf(debitCardModel.getIssuanceDate()));
//					requestVO.setExpiryDate(String.valueOf(debitCardModel.getExpiryDate()));

                        SwitchWrapper i8sbSwitchWrapper = new SwitchWrapperImpl();

                        i8sbSwitchWrapper.setI8SBSwitchControllerRequestVO(requestVO);
                        requestVO = i8sbSwitchWrapper.getI8SBSwitchControllerRequestVO();
                        I8SBSwitchControllerResponseVO responseVO = requestVO.getI8SBSwitchControllerResponseVO();
                        i8sbSwitchWrapper = this.esbAdapter.makeI8SBCall(i8sbSwitchWrapper);
                        responseVO = i8sbSwitchWrapper.getI8SBSwitchControllerResponseVO();
                        ESBAdapter.processI8sbResponseCode(responseVO, false);
                        if (!responseVO.getResponseCode().equals("I8SB-200")) {
                            throw new Exception("Error in Fetching Title");
                        }
                        name = responseVO.getAccountTitle();
                        name = StringEscapeUtils.escapeXml(name);
                        if (name == null || name.equals("null")) {
                            errMsg = "Account does not exist against provided account number";
                        }
                    }

                else {
                    phoenixFinancialInstitution.titleFetch(switchWrapper);
                    name = switchWrapper.getCustomerAccount().getTitleOfTheAccount();
                    name = StringEscapeUtils.escapeXml(name);
                    if (name == null || name.equals("null")) {
                        errMsg = "Account does not exist against provided account number";
                    }
                }

            } catch (Exception ex) {
                logger.error(ex.getMessage(), ex);
                if (ex.getMessage().equals("6075")) {
                    errMsg = "An error has occurred, please try again or contact your service provider";
                } else {
                    errMsg = "Account does not exist against provided account number";
                }
                ex.printStackTrace();
            }
        } else if (acType.equals("3")) {
            accountNo = ServletRequestUtils.getRequiredStringParameter(request, "accountNo");
            logger.info("Oracle Financial A/C No " + accountNo);
            StakeholderBankInfoModel model = new StakeholderBankInfoModel();
            model.setAccountNo(accountNo);
            model = stkholderBankInfoManager.loadStakeholderBankInfoModel(model);
            if (model != null) {
                name = model.getName();
                if (name == null || name.equals("null")) {
                    errMsg = "Account does not exist against provided account number";
                }
                if (null != name && (!model.getActive())) {
                    errMsg = "Account is not active against provided account number";
                    name = null;
                }
            } else {
                errMsg = "Account does not exist against provided account number";
            }
        }

        ajaxXmlBuilder.addItem("name", (name == null || name.equals("null")) ? " " : name);
        ajaxXmlBuilder.addItem("errMsg", errMsg);
        return ajaxXmlBuilder.toString();
    }

    public void setManualAdjustmentManager(ManualAdjustmentManager manualAdjustmentManager) {
        this.manualAdjustmentManager = manualAdjustmentManager;
    }

    public void setPhoenixFinancialInstitution(AbstractFinancialInstitution phoenixFinancialInstitution) {
        this.phoenixFinancialInstitution = phoenixFinancialInstitution;
    }

    public void setStkholderBankInfoManager(StakeholderBankInfoManager stkholderBankInfoManager) {
        this.stkholderBankInfoManager = stkholderBankInfoManager;
    }

    public void setEsbAdapter(ESBAdapter esbAdapter) {
        this.esbAdapter = esbAdapter;
    }
}