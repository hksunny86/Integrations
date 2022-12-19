package com.inov8.microbank.common.util;

import com.inov8.framework.common.exception.FrameworkCheckedException;
import com.inov8.microbank.server.service.integration.vo.MiddlewareAdviceVO;

public class CoreAdviceUtil {

    public static final String ADVICE_TYPE_CREDIT = "Credit";
    public static final String ADVICE_TYPE_REVERSAL = "Reversal";
    public static final String ADVICE_TYPE_BILL_PAYMENT = "Bill Payment Advice";
    public static final String CHALLAN_COLLECTION_ADVICE = "Challan Collection";
    public static final String ACCOUNNT_OPENING_ADVICE = "Account Opening";

    public static final String AGENT_EXCISE_TAX_ADVICE = "Excise and Taxation";

    public static String getAdviceTypeName(MiddlewareAdviceVO middlewareAdviceVO) {
        String adviceType = "";
        Long productId = middlewareAdviceVO.getProductId();
        if (middlewareAdviceVO.getIsCreditAdvice() != null) {
            adviceType = (middlewareAdviceVO.getIsCreditAdvice()) ? ADVICE_TYPE_CREDIT : ADVICE_TYPE_REVERSAL;
        } else if (middlewareAdviceVO.getIsBillPaymentRequest()) {
            adviceType = ADVICE_TYPE_BILL_PAYMENT;
        } else if (productId != null && (productId.equals(ProductConstantsInterface.AGENT_ET_COLLECTION)
                || productId.equals(ProductConstantsInterface.AGENT_KP_CHALLAN_COLLECTION)
                || productId.equals(ProductConstantsInterface.AGENT_VALLENCIA_COLLECTION)
                || productId.equals(ProductConstantsInterface.AGENT_KP_CHALLAN_COLLECTION_BY_ACCOUNT)
                || productId.equals(ProductConstantsInterface.AGENT_LICENSE_FEE_COLLECTION)
                || productId.equals(ProductConstantsInterface.AGENT_BALUCHISTAN_ET_COLLECTION)
                || productId.equals(ProductConstantsInterface.AGENT_E_LEARNING_MANAGEMENT_SYSTEM)
                || productId.equals(ProductConstantsInterface.CUSTOMER_ET_COLLECTION)
                || productId.equals(ProductConstantsInterface.CUSTOMER_KP_CHALLAN_COLLECTION)
                ||productId.equals(ProductConstantsInterface.CUSTOMER_VALLENCIA_COLLECTION)
                || productId.equals(ProductConstantsInterface.CUSTOMER_BALUCHISTAN_ET_COLLECTION)
                || productId.equals(ProductConstantsInterface.CUSTOMER_E_LEARNING_MANAGEMENT_SYSTEM)
                || productId.equals(ProductConstantsInterface.LICENSE_FEE_COLLECTION)
                || productId.equals(ProductConstantsInterface.IHL_ISLAMABAD_CAMP)
                || productId.equals(ProductConstantsInterface.IHL_Gujranawala_CAMP)
                || productId.equals(ProductConstantsInterface.SARHAD_UNIVERSITY_COLLECTION)
                || productId.equals(ProductConstantsInterface.CUST_IHL_Gujranawala_CAMP)
                || productId.equals(ProductConstantsInterface.CUST_IHL_ISLAMABAD_CAMP)
                ||productId.equals(ProductConstantsInterface.CUST_SARHAD_UNIVERSITY_COLLECTION)
                || productId.equals(50055L) || productId.equals(50056L))) {
            adviceType = CHALLAN_COLLECTION_ADVICE;
        } else if (productId != null && productId.equals(ProductConstantsInterface.AGENT_EXCISE_AND_TAXATION)) {
            adviceType = AGENT_EXCISE_TAX_ADVICE;
        }
          else if (middlewareAdviceVO.getAdviceType().equals(ACCOUNNT_OPENING_ADVICE)) {
            adviceType = ACCOUNNT_OPENING_ADVICE;
        }
        return adviceType;
    }

    public static MiddlewareAdviceVO prepareMiddlewareAdviceVO(CoreAdviceInterface model) throws FrameworkCheckedException {
        MiddlewareAdviceVO middlewareAdviceVO = new MiddlewareAdviceVO();
        if (model.getProductId() != null && (model.getProductId().equals(ProductConstantsInterface.AGENT_BB_TO_IBFT) || model.getProductId().equals(ProductConstantsInterface.CUSTOMER_BB_TO_IBFT))) {
            middlewareAdviceVO.setAccountTitle(model.getBeneAccountTitle());
            middlewareAdviceVO.setSenderBankImd(model.getSenderBankImd());
            middlewareAdviceVO.setBankIMD(model.getBeneBankImd());
            middlewareAdviceVO.setCrDr(model.getCrDr());
            middlewareAdviceVO.setBeneIBAN(model.getBeneIBAN());
            middlewareAdviceVO.setBeneBankName(model.getBeneBankName());
            middlewareAdviceVO.setBeneBranchName(model.getBeneBranchName());
            middlewareAdviceVO.setSenderIBAN(model.getSenderIBAN());
            middlewareAdviceVO.setSenderName(model.getSenderName());
            middlewareAdviceVO.setCardAcceptorNameAndLocation(model.getCardAcceptorNameAndLocation());
            middlewareAdviceVO.setAgentId(model.getAgentId());
            middlewareAdviceVO.setPAN(model.getCnicNo());
            middlewareAdviceVO.setCnicNo(model.getCnicNo());
            middlewareAdviceVO.setPurposeOfPayment(model.getPurposeOfPayment());
        }
        if (model.getProductId() != null && (model.getProductId().equals(ProductConstantsInterface.CUST_ACCOUNT_OPENING)
                || model.getProductId().equals(ProductConstantsInterface.ACCOUNT_OPENING)
                || model.getProductId().equals(ProductConstantsInterface.PORTAL_ACCOUNT_OPENING))) {
            middlewareAdviceVO.setCnicNo(model.getCnicNo());
            middlewareAdviceVO.setConsumerNo(model.getConsumerNo());
        }

        middlewareAdviceVO.setProductId(model.getProductId());
        middlewareAdviceVO.setAdviceType(model.getAdviceType());
        middlewareAdviceVO.setAccountNo1(model.getFromAccount());
        middlewareAdviceVO.setAccountNo2(model.getToAccount());
        middlewareAdviceVO.setTransactionAmount(String.valueOf(model.getTransactionAmount()));
        middlewareAdviceVO.setMicrobankTransactionCode(model.getTransactionCode());
        middlewareAdviceVO.setMicrobankTransactionCodeId(model.getTransactionCodeId());
        middlewareAdviceVO.setStan(model.getStan());
        middlewareAdviceVO.setRequestTime(model.getRequestTime());
        middlewareAdviceVO.getDataMap().put("TRANSACTION_ID", model.getTransactionId());
        middlewareAdviceVO.setTransmissionTime(model.getTransmissionTime());
        if (model.getIntgTransactionTypeId() != null && model.getIntgTransactionTypeId().longValue() == IntgTransactionTypeConstantsInterface.BILL_PAYMENT_PHOENIX) {
            middlewareAdviceVO.setIsBillPaymentRequest(Boolean.TRUE);
            middlewareAdviceVO.setIntgTransactionTypeId(IntgTransactionTypeConstantsInterface.BILL_PAYMENT_PHOENIX);
            middlewareAdviceVO.setDateTimeLocalTransaction(model.getTransmissionTime());
            middlewareAdviceVO.setBillAggregator(model.getBillAggregator());
            middlewareAdviceVO.getDataMap().put("ACTION_LOG_ID", model.getActionLogId());
            middlewareAdviceVO.setCompnayCode(model.getCompnayCode());
            middlewareAdviceVO.setCnicNo(model.getCnicNo());
            middlewareAdviceVO.setConsumerNo(model.getConsumerNo());
            middlewareAdviceVO.setBillCategoryId(model.getBillCategoryCode());
            middlewareAdviceVO.setRetrievalReferenceNumber(model.getRetrievalReferenceNumber());
            middlewareAdviceVO.setUbpStan(model.getUbpBBStan());
        } else if (model.getIntgTransactionTypeId() != null && model.getIntgTransactionTypeId().longValue() == IntgTransactionTypeConstantsInterface.CREDIT_ACCOUNT_ADVICE_CORE) {
            middlewareAdviceVO.setIsCreditAdvice(true);
            middlewareAdviceVO.setIntgTransactionTypeId(model.getIntgTransactionTypeId());
            middlewareAdviceVO.setRetrievalReferenceNumber(model.getRetrievalReferenceNumber());
            middlewareAdviceVO.setDateTimeLocalTransaction(model.getTransmissionTime());

        } else if (model.getIntgTransactionTypeId() != null && model.getIntgTransactionTypeId().longValue() == IntgTransactionTypeConstantsInterface.TRANSFER_IN_CORE_REVERSAL) {
            middlewareAdviceVO.setIsCreditAdvice(false);
            middlewareAdviceVO.setIntgTransactionTypeId(IntgTransactionTypeConstantsInterface.TRANSFER_IN_CORE_REVERSAL);
            middlewareAdviceVO.setReversalSTAN(model.getReversalStan());
            middlewareAdviceVO.setReversalRequestTime(model.getReversalRequestTime());
        }
        if (null != model.getConsumerNo()) {
            middlewareAdviceVO.setConsumerNo(model.getConsumerNo());
//			middlewareAdviceVO.setIsCreditAdvice(true);
        }

        return middlewareAdviceVO;
    }

}
