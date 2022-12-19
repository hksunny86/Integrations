package com.inov8.microbank.webapp.action.portal.transactiondetaili8module;

import com.inov8.framework.common.model.ExampleConfigHolderModel;
import com.inov8.framework.common.wrapper.BaseWrapper;
import com.inov8.framework.common.wrapper.BaseWrapperImpl;
import com.inov8.framework.common.wrapper.SearchBaseWrapper;
import com.inov8.framework.common.wrapper.SearchBaseWrapperImpl;
import com.inov8.integration.i8sb.vo.I8SBSwitchControllerRequestVO;
import com.inov8.microbank.common.model.AuditLogModel;
import com.inov8.microbank.common.model.BISPCustNadraVerificationModel;
import com.inov8.microbank.common.model.TransactionCodeModel;
import com.inov8.microbank.common.model.auditlogmodule.AuditLogListViewModel;
import com.inov8.microbank.common.util.*;
import com.inov8.microbank.common.vo.transactionreversal.TransactionReversalVo;
import com.inov8.microbank.common.wrapper.switchmodule.SwitchWrapper;
import com.inov8.microbank.common.wrapper.switchmodule.SwitchWrapperImpl;
import com.inov8.microbank.server.dao.failurelogmodule.AuditLogDAO;
import com.inov8.microbank.server.facade.ThirdPartyCashOutQueingPreProcessor;
import com.inov8.microbank.server.service.auditlogmodule.AuditLogListViewManager;
import com.inov8.microbank.server.service.mfsmodule.CommonCommandManager;
import com.inov8.microbank.webapp.action.ajax.AjaxController;
import org.codehaus.jackson.map.ObjectMapper;
import org.hibernate.criterion.MatchMode;
import org.springframework.web.bind.ServletRequestUtils;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Date;
import java.util.List;

public class ReverseBISPTransactionAjaxController extends AjaxController {

    private CommonCommandManager commonCommandManager;

    @Override
    public String getResponseContent(HttpServletRequest request, HttpServletResponse response) throws Exception {
        try{
            String transactionCodeId = ServletRequestUtils.getStringParameter(request, "transactionCodeId");
            if(transactionCodeId != null && !transactionCodeId.equals("")){
                BaseWrapper baseWrapper = this.populateAuthenticationParamForBISPReversal(transactionCodeId);
                commonCommandManager.initiateBISPReversalRequestWithAuthorization(baseWrapper);
            }
        }
        catch (Exception ex){
            ex.printStackTrace();
        }
        return "Success";
    }

    private BaseWrapper populateAuthenticationParamForBISPReversal(String transactionCode)
    {
        BISPCustNadraVerificationModel custNadraVerificationModel = null;
        BISPCustNadraVerificationModel model = new BISPCustNadraVerificationModel();
        model.setTransactionCode(transactionCode);
        List<BISPCustNadraVerificationModel> list = commonCommandManager.getBispCustNadraVerificationDAO().findByExample(model).getResultsetList();
        if(list != null && !list.isEmpty())
            custNadraVerificationModel = list.get(0);
        BaseWrapper baseWrapper = new BaseWrapperImpl();
        TransactionReversalVo reversalVo = new TransactionReversalVo();
        if(custNadraVerificationModel != null){
            reversalVo.setBaflSessionId(custNadraVerificationModel.getBaflSessionId());
            reversalVo.setNadraSessionId(custNadraVerificationModel.getNadraSessionId());
            reversalVo.setBaflWalletId(custNadraVerificationModel.getBaflWalletId());
            reversalVo.setBaflTransactionNumber(custNadraVerificationModel.getBaflTransactionNumber());
        }
        reversalVo.setTransactionCode(transactionCode);
        reversalVo.setBtnName("bispReversalRetry");

        ObjectMapper mapper = new ObjectMapper();
        String modelJsonString = null;
        try {
            modelJsonString = mapper.writeValueAsString(reversalVo);
        } catch (IOException e) {
            e.printStackTrace();
        }
        Long actionAuthorizationId =null;
        String initialModelJsonString = null;
        baseWrapper.putObject(ActionAuthorizationConstantsInterface.KEY_OLD_MODEL_STRING, initialModelJsonString);
        baseWrapper.putObject(ActionAuthorizationConstantsInterface.KEY_METHODE_NAME, "initiateBISPReversalRequestWithAuthorization");
        baseWrapper.putObject(ActionAuthorizationConstantsInterface.KEY_MODEL_CLASS, TransactionReversalVo.class.getSimpleName());
        baseWrapper.putObject(ActionAuthorizationConstantsInterface.KEY_MODEL_CLASS_QUALIFIED_NAME, TransactionReversalVo.class.getName());
        baseWrapper.putObject(ActionAuthorizationConstantsInterface.KEY_MODEL_STRING, modelJsonString);
        baseWrapper.putObject("managerName","commonCommandManager");
        baseWrapper.putObject(ActionAuthorizationConstantsInterface.KEY_REQ_REF_ID, UserUtils.getCurrentUser().getAppUserId().toString());
        baseWrapper.putObject(PortalConstants.KEY_ACTION_AUTH_ID,actionAuthorizationId);
        baseWrapper.putObject(PortalConstants.KEY_USECASE_ID, PortalConstants.KEY_BISP_REVERSAL_USECASE_ID);
        baseWrapper.putObject(PortalConstants.KEY_ACTION_ID, PortalConstants.ACTION_UPDATE);
        baseWrapper.putObject(CommandFieldConstants.KEY_TRANSACTION_ID,transactionCode);
        baseWrapper.putObject(ActionAuthorizationConstantsInterface.KEY_FORM_NAME, "p_bisptransactiondetailreport");
        baseWrapper.setBasePersistableModel(reversalVo);
        return baseWrapper;

    }

    public void setCommonCommandManager(CommonCommandManager commonCommandManager) {
        this.commonCommandManager = commonCommandManager;
    }



}
