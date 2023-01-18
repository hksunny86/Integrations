package com.inov8.microbank.debitcard.service;

import com.inov8.framework.common.exception.FrameworkCheckedException;
import com.inov8.framework.common.util.CustomList;
import com.inov8.framework.common.wrapper.BaseWrapper;
import com.inov8.framework.common.wrapper.BaseWrapperImpl;
import com.inov8.framework.common.wrapper.SearchBaseWrapper;
import com.inov8.framework.server.dao.framework.v2.GenericDao;
import com.inov8.integration.i8sb.constants.I8SBConstants;
import com.inov8.integration.i8sb.enums.I8SBKeysOfCollectionEnum;
import com.inov8.integration.i8sb.vo.CardDetailVO;
import com.inov8.integration.i8sb.vo.DebitCardStatusVO;
import com.inov8.integration.i8sb.vo.I8SBSwitchControllerRequestVO;
import com.inov8.integration.i8sb.vo.I8SBSwitchControllerResponseVO;
import com.inov8.microbank.cardconfiguration.common.CardConstantsInterface;
import com.inov8.microbank.cardconfiguration.dao.CardStateDAO;
import com.inov8.microbank.cardconfiguration.dao.CardStatusDAO;
import com.inov8.microbank.cardconfiguration.model.CardFeeRuleModel;
import com.inov8.microbank.cardconfiguration.model.CardStateModel;
import com.inov8.microbank.cardconfiguration.model.CardStatusModel;
import com.inov8.microbank.common.model.*;
import com.inov8.microbank.common.model.messagemodule.SmsMessage;
import com.inov8.microbank.common.model.veriflymodule.DebitCardChargesSafRepoModel;
import com.inov8.microbank.common.util.*;
import com.inov8.microbank.common.wrapper.commission.CommissionWrapper;
import com.inov8.microbank.common.wrapper.switchmodule.SwitchWrapper;
import com.inov8.microbank.common.wrapper.switchmodule.SwitchWrapperImpl;
import com.inov8.microbank.common.wrapper.workflow.WorkFlowWrapper;
import com.inov8.microbank.common.wrapper.workflow.WorkFlowWrapperImpl;
import com.inov8.microbank.debitcard.dao.*;
import com.inov8.microbank.debitcard.model.*;
import com.inov8.microbank.debitcard.vo.DebitCardVO;
import com.inov8.microbank.server.dao.debitCardChargesmodule.DebitCardChargesDAO;
import com.inov8.microbank.server.dao.fetchcardtype.FetchCardTypeDAO;
import com.inov8.microbank.server.dao.retailermodule.RetailerContactDAO;
import com.inov8.microbank.server.dao.retailermodule.RetailerDAO;
import com.inov8.microbank.server.service.commandmodule.CommandManager;
import com.inov8.microbank.server.service.commissionmodule.CommissionAmountsHolder;
import com.inov8.microbank.server.service.financialintegrationmodule.switchmodule.ESBAdapter;
import com.inov8.microbank.server.service.smssendermodule.SmsSenderService;
import com.inov8.verifly.common.constants.CardTypeConstants;
import com.inov8.verifly.common.model.AccountInfoModel;
import org.apache.log4j.Logger;
import org.springframework.util.StopWatch;
import org.w3c.dom.Document;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpression;
import javax.xml.xpath.XPathFactory;
import java.io.StringReader;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

public class DebitCardManagerImpl implements DebitCardManager {

    private static final Logger LOGGER = Logger.getLogger(DebitCardManagerImpl.class);
    private DebitCardModelDAO debitCardModelDAO;
    private DebitCardMailingAddressDAO debitCardMailingAddressDAO;
    private DebitCardChargesDAO debitCardChargesDAO;
    private DebitCardViewModelDAO debitCardViewModelDAO;
    private DebitCardRequestsViewModelDAO debitCardRequestsViewModelDAO;
    private DebitCardExportDataViewDAO debitCardExportDataViewDAO;
    private CardStateDAO cardStateDAO;
    private CardStatusDAO cardStatusDAO;
    private FetchCardTypeDAO fetchCardTypeDAO;
    private CommandManager commandManager;
    //Debit Card Import Export Scheduler Request
    private ESBAdapter esbAdapter;
    private SmsSenderService smsSenderService;
    private SmsMessage smsMessage;
    private GenericDao genericDAO;
    private RetailerContactDAO retailerContactDAO;
    private RetailerDAO retailerDAO;
    private ArrayList<SmsMessage> validCardSmsList = new ArrayList<>(0);
    private ArrayList<SmsMessage> rejectedCardSmsList = new ArrayList<>(0);
    DocumentBuilderFactory domFactory = null;
    private String transactionCode;

    @Override
    public List<DebitCardModel> getDebitCardModelByMobileAndNIC(String mobileNo, String nic) throws FrameworkCheckedException {
        return debitCardModelDAO.getDebitCardModelByMobileAndNIC(mobileNo, nic);
    }

    @Override
    public DebitCardMailingAddressModel saveOrUpdateDebitCardMailingAddress(DebitCardMailingAddressModel debitCardMailingAddressModel) throws FrameworkCheckedException {
        debitCardMailingAddressModel = debitCardMailingAddressDAO.saveOrUpdate(debitCardMailingAddressModel);
        return debitCardMailingAddressModel;
    }

    @Override
    public DebitCardModel saveOrUpdateDebitCardModel(DebitCardModel debitCardModel) throws FrameworkCheckedException {
        debitCardModelDAO.updateDebitCardFeeDeductionDate(debitCardModel);
        return debitCardModel;
    }

    @Override
    public DebitCardModel saveOrUpdateDebitCardModelForAnnualFee(DebitCardModel debitCardModel) throws FrameworkCheckedException {
        debitCardModelDAO.updateDebitCardFeeDeductionDateForAnnualFee(debitCardModel);
        return debitCardModel;
    }

    @Override
    public DebitCardModel saveOrUpdateDebitCardModelForReIssuanceFee(DebitCardModel debitCardModel) throws FrameworkCheckedException {
        debitCardModelDAO.updateDebitCardFeeDeductionDateForReIssuanceFee(debitCardModel);
        return debitCardModel;
    }

    @Override
    public DebitCardModel saveOrUpdateDebitCardModelForIssuanceFee(DebitCardModel debitCardModel) throws FrameworkCheckedException {
        debitCardModelDAO.updateDebitCardFeeDeductionDateForIssuanceFee(debitCardModel);
        return debitCardModel;
    }

    @Override
    public DebitCardModel saveOrUpdateReIssuanceDebitCardModel(DebitCardModel debitCardModel) throws FrameworkCheckedException {
        debitCardModelDAO.saveOrUpdate(debitCardModel);
        return debitCardModel;
    }


    @Override
    public DebitCardChargesSafRepoModel saveOrUpdateDebitChargesSafRepoCardModel(DebitCardChargesSafRepoModel debitCardChargesSafRepoModel) throws FrameworkCheckedException {
        debitCardChargesDAO.updateDebitCardFeeDeductionSafRepo(debitCardChargesSafRepoModel);
        return debitCardChargesSafRepoModel;
    }

    @Override
    public BaseWrapper updateDebitCardIssuenceRequestWithAuthorization(BaseWrapper baseWrapper) throws FrameworkCheckedException {
        DebitCardVO debitCardVO = (DebitCardVO) baseWrapper.getBasePersistableModel();
        DebitCardModel debitCardModel = debitCardModelDAO.getDebitCardModelByCardNumber(debitCardVO.getCardNo());

        debitCardModel.setCardStatusId(CardConstantsInterface.CARD_STATUS_APPROVED);
        debitCardModel.setUpdatedOn(new Date());
        debitCardModel.setUpdatedBy(UserUtils.getCurrentUser().getAppUserId());

        debitCardModel = debitCardModelDAO.saveOrUpdate(debitCardModel);
        baseWrapper.setBasePersistableModel(debitCardModel);

        return baseWrapper;
    }

    @Override
    public void updateBulkDebitCardModel(List<DebitCardModel> list) throws FrameworkCheckedException {
        debitCardModelDAO.saveOrUpdateCollection(list);
    }

    private HashMap<String, List<String>> prepareReIssuanceData(List<DebitCardStatusVO> importedData) {
        HashMap<String, List<String>> map = new HashMap<>(0);
        for (DebitCardStatusVO status : importedData) {
            List<String> statusList = new ArrayList<>(0);
            if (map.containsKey(status.getRelationshipNo())) {
                statusList = map.get(status.getRelationshipNo());
                statusList.add(status.getStatus());
                map.remove(status.getRelationshipNo());
                map.put(status.getRelationshipNo(), statusList);
            } else {
                statusList.add(status.getStatus());
                map.put(status.getRelationshipNo(), statusList);
            }
        }
        return map;
    }


    private HashMap<String, List<String>> prepareDebitReIssuanceData(List<CardDetailVO> importedData) {
        HashMap<String, List<String>> map = new HashMap<>(0);
        for (CardDetailVO status : importedData) {
            List<String> statusList = new ArrayList<>(0);
            if (map.containsKey(status.getCif())) {
                statusList = map.get(status.getCif());
                statusList.add(status.getCardStatus());
                map.remove(status.getCif());
                map.put(status.getCif(), statusList);
            } else {
                statusList.add(status.getCardStatus());
                map.put(status.getCif(), statusList);
            }
        }
        return map;
    }

    private DebitCardModel prepareDebitCardModelForReIssuance(DebitCardStatusVO statusVO, DebitCardModel model, Date date, Date feeDeductionDate) {
        DebitCardModel reIssuanceModel = new DebitCardModel();
        reIssuanceModel.setCreatedOn(new Date());
        reIssuanceModel.setUpdatedOn(new Date());
        reIssuanceModel.setCreatedBy(3L);//Scheduler App User Id
        reIssuanceModel.setUpdatedBy(3L);//Scheduler App User Id
        reIssuanceModel.setIssuanceDate(new Date());
        reIssuanceModel.setReIssuanceDate(new Date());
        reIssuanceModel.setImportedOn(new Date());
        reIssuanceModel.setActivationDate(new Date());
        reIssuanceModel.setFeeDeductionDate(null);
        reIssuanceModel.setAppId(4L);// Vision Channel ID
        reIssuanceModel = this.setCardPropsAgainstStatus(reIssuanceModel, statusVO.getStatus());
        if (!statusVO.getStatus().equals("003")) {
            reIssuanceModel.setMobileNo(statusVO.getAccountNo());
            reIssuanceModel.setCnic(statusVO.getRelationshipNo());
            reIssuanceModel.setCardNo(statusVO.getPan());
        }
        reIssuanceModel.setDebitCardEmbosingName(model.getDebitCardEmbosingName());
        reIssuanceModel.setAppUserId(model.getAppUserId());
        reIssuanceModel.setSmartMoneyAccountId(model.getSmartMoneyAccountId());
        reIssuanceModel.setMailingAddressId(model.getMailingAddressId());
        reIssuanceModel.setReIssuanceStatus(CardConstantsInterface.CARD_STATUS_ACTIVE);
        reIssuanceModel.setIsApproved(model.getIsApproved());


        return reIssuanceModel;
    }

    private DebitCardModel prepareDebitCardModelForReIssuanceDebitCard(CardDetailVO statusVO, DebitCardModel model, Date date, Date feeDeductionDate) {
        DebitCardModel reIssuanceModel = new DebitCardModel();
        reIssuanceModel.setCreatedOn(new Date());
        reIssuanceModel.setUpdatedOn(new Date());
        reIssuanceModel.setCreatedBy(3L);//Scheduler App User Id
        reIssuanceModel.setUpdatedBy(3L);//Scheduler App User Id
        reIssuanceModel.setIssuanceDate(new Date());
        reIssuanceModel.setReIssuanceDate(new Date());
        reIssuanceModel.setImportedOn(new Date());
        reIssuanceModel.setActivationDate(new Date());
        reIssuanceModel.setFeeDeductionDate(null);
        reIssuanceModel.setAppId(4L);// Vision Channel ID
        reIssuanceModel = this.setCardPropsAgainstStatus(reIssuanceModel, statusVO.getCardStatus());
        if (!statusVO.getCardStatus().equals("003")) {
            reIssuanceModel.setMobileNo(statusVO.getAccountNumber());
            reIssuanceModel.setCnic(statusVO.getCif());
            reIssuanceModel.setCardNo(statusVO.getCardNo());
        }
        reIssuanceModel.setDebitCardEmbosingName(model.getDebitCardEmbosingName());
        reIssuanceModel.setAppUserId(model.getAppUserId());
        reIssuanceModel.setSmartMoneyAccountId(model.getSmartMoneyAccountId());
        reIssuanceModel.setMailingAddressId(model.getMailingAddressId());
        reIssuanceModel.setReIssuanceStatus(CardConstantsInterface.CARD_STATUS_ACTIVE);
        reIssuanceModel.setIsApproved(model.getIsApproved());

        return reIssuanceModel;
    }

    @Override
    public void saveDebitCardImportExportSchedulerRequest() throws FrameworkCheckedException {
        LOGGER.info("*********** Before Synchronized Block ***********");
        synchronized (this) {
            Integer exportedSize = 0;
            StopWatch stopWatch = new StopWatch();
            if (validCardSmsList != null && validCardSmsList.size() > 0)
                validCardSmsList.clear();
            if (rejectedCardSmsList != null && rejectedCardSmsList.size() > 0)
                validCardSmsList.clear();
            rejectedCardSmsList = new ArrayList<>(0);
            stopWatch.start("DebitCardImportExport Scheduler init");
            SwitchWrapper i8sbSwitchWrapper = new SwitchWrapperImpl();
            I8SBSwitchControllerRequestVO requestVO = ESBAdapter.prepareRequestVoForDebitCard(I8SBConstants.RequestType_JSDEBITCARD_IMPORT);
            i8sbSwitchWrapper.setI8SBSwitchControllerRequestVO(requestVO);
            i8sbSwitchWrapper = this.esbAdapter.makeI8SBCall(i8sbSwitchWrapper);
            requestVO = i8sbSwitchWrapper.getI8SBSwitchControllerRequestVO();
            I8SBSwitchControllerResponseVO responseVO = requestVO.getI8SBSwitchControllerResponseVO();
            List<DebitCardStatusVO> importedData = (List<DebitCardStatusVO>) responseVO.getList(I8SBKeysOfCollectionEnum.DebitCardStatus.getValue());
            if (importedData != null && !importedData.isEmpty()) {
                LOGGER.info("Total " + importedData.size() + " accounts were Imported for Debit Card Issuance.");
                HashMap<String, List<String>> mapList = this.prepareReIssuanceData(importedData);
                List<String> statusList = null;
                DebitCardModel cardModel = null;
                List<DebitCardModel> importedList = new ArrayList<>(0);
                List<DebitCardModel> reIssuanceList = new ArrayList<>(0);
                HashMap<String, Boolean> reIssuanceMap = new HashMap<>();
                BaseWrapper reIssuanceWrapper = new BaseWrapperImpl();
                for (DebitCardStatusVO status : importedData) {
                    cardModel = this.getDebitCradModelByNicAndState(status.getRelationshipNo(), CardConstantsInterface.CARD_STATUS_IN_PROCESS);
                    //Insert or Update to implement Call Center Debit Card Re-Issuance
                    statusList = mapList.get(status.getRelationshipNo());
//                    String customerBalance = customerbalance(status.getRelationshipNo());
                    if (!statusList.isEmpty() && statusList.size() == 2) {
                        Double amount = null;

                        String s = status.getStatus();
                        String mapKey = status.getRelationshipNo() + s;
                        if (reIssuanceMap.isEmpty() || !reIssuanceMap.containsKey(mapKey)) {
                            if (s.equals("003")) {
//                                amount = checkCustomerBalanceForDebitCard(cardModel);
                                reIssuanceWrapper.putObject(mapKey, cardModel);
//                                if (!(Double.parseDouble(customerBalance) < amount)) {
                                cardModel = this.setCardPropsAgainstStatus(cardModel, "003");
                                cardModel.setImportedOn(new Date());
                                cardModel.setUpdatedBy(3L);//Scheduler App User Id
                                importedList.add(cardModel);

//                                }
                                reIssuanceMap.put(mapKey, Boolean.TRUE);
                            } else {
                                if (cardModel == null) {
                                    if (reIssuanceWrapper.getObject(status.getRelationshipNo() + "003") != null)
                                        cardModel = (DebitCardModel) reIssuanceWrapper.getObject(status.getRelationshipNo() + "003");
                                }
                                DebitCardModel reIssuanceModel = prepareDebitCardModelForReIssuance(status, cardModel, cardModel.getIssuanceDate(), cardModel.getFeeDeductionDate());
//                                Double amount1 = checkCustomerBalanceForDebitCard(reIssuanceModel);
//                                if (Double.parseDouble(customerBalance) < amount1) {
//                                    LOGGER.info("*********** Debit Card Issuance/Re-Issuance Failed Due To Low Balance and Save Into DebitCardChargesSafRepo***********");
//                                    saveDebitCardChargesSafRepoRequiresNewTransaction(reIssuanceModel, 2l, ProductConstantsInterface.DEBIT_CARD_RE_ISSUANCE, amount1);
//                                } else {
                                reIssuanceList.add(reIssuanceModel);
                            }
                            reIssuanceMap.put(status.getRelationshipNo() + s, Boolean.TRUE);
//                                }


                        }
                    } else {
                        if (cardModel != null) {
                            cardModel = this.setCardPropsAgainstStatus(cardModel, status.getStatus());
                            cardModel.setCardNo(status.getPan());
                            cardModel.setMobileNo(status.getAccountNo());
                            cardModel.setCnic(status.getRelationshipNo());
                            cardModel.setImportedOn(new Date());
                            importedList.add(cardModel);
                        }
                    }
                }
                if (importedList != null && !importedList.isEmpty()) {
                    this.updateBulkDebitCardModel(importedList);
                }
                if (reIssuanceList != null && !reIssuanceList.isEmpty())
                    this.updateBulkDebitCardModel(reIssuanceList);

                //Save ReIssuance Records
                String response = null;
                if (!reIssuanceList.isEmpty()) {
                    for (DebitCardModel model : reIssuanceList) {
//                        response = makeFeeDeductionCommand(null, model,
//                                ProductConstantsInterface.DEBIT_CARD_RE_ISSUANCE, CardConstantsInterface.CARD_FEE_TYPE_RE_ISSUANCE, DeviceTypeConstantsInterface.MOBILE);
//                        if (response != null)
                        debitCardModelDAO.saveOrUpdate(model);
                    }
                }
            }
            if (validCardSmsList != null && org.apache.commons.collections.CollectionUtils.isNotEmpty(validCardSmsList))
                smsSenderService.sendSmsList(validCardSmsList);
            if (rejectedCardSmsList != null && org.apache.commons.collections.CollectionUtils.isNotEmpty(rejectedCardSmsList))
                smsSenderService.sendSmsList(rejectedCardSmsList);
            List<DebitCardExportDataViewModel> list = this.getDataToExport();
            if (list != null && !list.isEmpty()) {
                LOGGER.info("Export List Size :: " + list.size());
                DebitCardModel debitCardModel = null;
                List<DebitCardModel> exportDebitCardList = new ArrayList<>(0);
                requestVO = ESBAdapter.prepareRequestVoForDebitCard(I8SBConstants.RequestType_JSDEBITCARD_EXPORT);
                requestVO = ESBAdapter.prepareDebitCardIssuenceRequest(requestVO, list);
                i8sbSwitchWrapper.setI8SBSwitchControllerRequestVO(requestVO);

                i8sbSwitchWrapper = this.esbAdapter.makeI8SBCall(i8sbSwitchWrapper);

                responseVO = i8sbSwitchWrapper.getI8SBSwitchControllerResponseVO();
                ESBAdapter.processI8sbResponseCode(responseVO, false);
                for (DebitCardExportDataViewModel model : list) {
                    debitCardModel = this.getDebitCardModelByDebitCardId(model.getDebitCardId());
                    debitCardModel.setCardStatusId(CardConstantsInterface.CARD_STATUS_IN_PROCESS);
                    debitCardModel.setInProgressDate(new Date());
                    debitCardModel.setExportedOn(new Date());
                    exportDebitCardList.add(debitCardModel);
                }
                if (exportDebitCardList != null && !exportDebitCardList.isEmpty()) {
                    updateBulkDebitCardModel(exportDebitCardList);
                    exportedSize = exportDebitCardList.size();
                }
            }
            LOGGER.info("Total " + exportedSize.toString() + " accounts were Exported for Debit Card Issuance.");
            stopWatch.stop();
            LOGGER.info(stopWatch.prettyPrint());
        }
    }

    @Override
    public void saveDebitCardImportExportReissuanceSchedulerRequest(DebitCardModel debitCardModel) throws FrameworkCheckedException {
        List<DebitCardModel> importedList = new ArrayList<>(0);
        List<DebitCardModel> reIssuanceList = new ArrayList<>(0);
        SwitchWrapper i8sbSwitchWrapper = new SwitchWrapperImpl();
        I8SBSwitchControllerRequestVO requestVO = ESBAdapter.prepareRequestVoForDebitCardReIssuance(I8SBConstants.RequestType_VISION_DEBIT_CARD);
        requestVO.setCNIC(debitCardModel.getCnic());
        i8sbSwitchWrapper.setI8SBSwitchControllerRequestVO(requestVO);

        i8sbSwitchWrapper = this.esbAdapter.makeI8SBCall(i8sbSwitchWrapper);
        requestVO = i8sbSwitchWrapper.getI8SBSwitchControllerRequestVO();
        List<CardDetailVO> importedData = requestVO.getI8SBSwitchControllerResponseVO().getCardDetailList();

        if (importedData != null && !importedData.isEmpty()) {
            LOGGER.info("Total " + importedData.size() + " accounts were Imported for Debit Card Issuance.");
            HashMap<String, List<String>> mapList = this.prepareDebitReIssuanceData(importedData);
            List<String> statusList = null;
            DebitCardModel cardModel = null;
            HashMap<String, Boolean> reIssuanceMap = new HashMap<>();
            BaseWrapper reIssuanceWrapper = new BaseWrapperImpl();
            for (CardDetailVO status : importedData) {
                cardModel = this.getDebitCradModelByNicAndState(status.getCif(), CardConstantsInterface.CARD_STATUS_IN_PROCESS);
                statusList = mapList.get(status.getCif());
                if (!statusList.isEmpty() && statusList.size() == 2) {
                    String s = status.getCardStatus();
                    String mapKey = status.getCif() + s;
                    if (reIssuanceMap.isEmpty() || !reIssuanceMap.containsKey(mapKey)) {
                        if (s.equals("003")) {
                            reIssuanceWrapper.putObject(mapKey, cardModel);
                            cardModel = this.setCardPropsAgainstStatus(cardModel, "003");
                            cardModel.setImportedOn(new Date());
                            cardModel.setUpdatedBy(3L);//Scheduler App User Id
                            cardModel.setReissuance("0");
                            importedList.add(cardModel);
                            reIssuanceMap.put(mapKey, Boolean.TRUE);
                        } else {
                            if (cardModel == null) {
                                if (reIssuanceWrapper.getObject(status.getCif() + "003") != null)
                                    cardModel = (DebitCardModel) reIssuanceWrapper.getObject(status.getCif() + "003");
                            }
                            DebitCardModel reIssuanceModel = prepareDebitCardModelForReIssuanceDebitCard(status, cardModel, cardModel.getIssuanceDate(), cardModel.getFeeDeductionDate());
                            reIssuanceList.add(reIssuanceModel);
                        }
                        reIssuanceMap.put(status.getCif() + s, Boolean.TRUE);
                    }
                } else {
                    if (cardModel != null) {
                        cardModel = this.setCardPropsAgainstStatus(cardModel, status.getCardStatus());
                        cardModel.setCardNo(status.getCardNo());
                        cardModel.setMobileNo(status.getAccountNumber());
                        cardModel.setCnic(status.getCif());
                        cardModel.setImportedOn(new Date());
                        importedList.add(cardModel);
                    }
                }
            }
            if (importedList != null && !importedList.isEmpty()) {
                this.updateBulkDebitCardModel(importedList);
            }
            if (reIssuanceList != null && !reIssuanceList.isEmpty())
                this.updateBulkDebitCardModel(reIssuanceList);

            //Save ReIssuance Records
            String response = null;
            if (!reIssuanceList.isEmpty()) {
                for (DebitCardModel model : reIssuanceList) {
                    debitCardModelDAO.saveOrUpdate(model);
                }
            }
        }

    }

    @Override
    public String makeFeeDeductionCommand(String fee, DebitCardModel model, Long productId, Long cardFeeTypeId, Long deviceTypeId) throws FrameworkCheckedException {
        String mobileNo = model.getMobileNo();
        String cNic = model.getCnic();
        StringBuilder sb = new StringBuilder();
        WorkFlowWrapper workFlowWrapper = new WorkFlowWrapperImpl();

        if (fee == null || (fee != null && fee.equals(""))) {
            sb.append("Start of calculateDebitCardFee() in DebitCardManagerImpl.executeIssuanceFeeCommand() for Product :: ").append(productId.toString());
            sb.append("\nand Mobile # :: " + mobileNo + " and CNIC :: " + cNic + " and FeeType :: " + cardFeeTypeId.toString()).append(" at Time ::" + new Date());
            LOGGER.info(sb.toString());

            AppUserModel appUserModel = commandManager.getCommonCommandManager().loadAppUserByMobileAndType(model.getMobileNo());
            SmartMoneyAccountModel sma = commandManager.getCommonCommandManager().getSmartMoneyAccountByAppUserModelAndPaymentModId(appUserModel,
                    PaymentModeConstantsInterface.BRANCHLESS_BANKING_ACCOUNT);

            workFlowWrapper = new WorkFlowWrapperImpl();

//            if (productId == ProductConstantsInterface.DEBIT_CARD_RE_ISSUANCE) {
            workFlowWrapper = commandManager.getCommonCommandManager().calculateDebitCardFeeForAPI(mobileNo, cNic, null, null, null,
                    productId, cardFeeTypeId, sma.getCardProdId(), DeviceTypeConstantsInterface.MOBILE, model);
//            } else {
//                workFlowWrapper = commandManager.getCommonCommandManager().calculateDebitCardFee(mobileNo, cNic, null, null, null,
//                        productId, cardFeeTypeId, DeviceTypeConstantsInterface.MOBILE, model);
//            }
            fee = String.valueOf(workFlowWrapper.getCommissionAmountsHolder().getTransactionAmount());
            sb.append("End of calculateDebitCardFee() in DebitCardManagerImpl.executeIssuanceFeeCommand() for Product :: ").append(productId.toString());
            sb.append("\nand Mobile # :: " + mobileNo + " and CNIC :: " + cNic + " and FeeType :: " + cardFeeTypeId.toString()).append(" at Time ::" + new Date());
            LOGGER.info(sb.toString());
        } else {
            sb.append("Fee already calculated for Product :: ").append(productId.toString());
            sb.append("\nand Mobile # :: " + mobileNo + " and CNIC :: " + cNic + " and FeeType :: " + cardFeeTypeId.toString());
            sb.append("\n and Fee = " + fee);
            LOGGER.info(sb.toString());
        }
        sb = new StringBuilder();
        BaseWrapper dWrapper = new BaseWrapperImpl();
        dWrapper.putObject(CommandFieldConstants.KEY_MOB_NO, mobileNo);
        dWrapper.putObject(CommandFieldConstants.KEY_AMOUNT, fee.toString());
        dWrapper.putObject(CommandFieldConstants.KEY_PROD_ID, productId.toString());
        dWrapper.putObject(CommandFieldConstants.KEY_DEVICE_TYPE_ID, deviceTypeId.toString());
        dWrapper.putObject(CommandFieldConstants.KEY_TXAM, fee.toString());
        dWrapper.putObject(CommandFieldConstants.KEY_TPAM, "0");
        dWrapper.putObject(CommandFieldConstants.KEY_CUSTOMER_MOBILE, mobileNo);
        dWrapper.putObject(CommandFieldConstants.KEY_TAMT, fee);
        dWrapper.putObject(CommandFieldConstants.KEY_TERMINAL_ID, "MOBILE");
        dWrapper.putObject(CommandFieldConstants.KEY_STAN, "");
        dWrapper.putObject(CommandFieldConstants.KEY_PAYMENT_MODE, "");
        dWrapper.putObject(CommandFieldConstants.KEY_APP_ID, "2");//Customer Initiated Transaction
        sb.append("Start of executeIssuanceFee() in DebitCardManagerImpl for Product :: ").append(productId.toString());
        sb.append(" \nand Mobile # :: " + mobileNo + " and CNIC :: " + cNic + " and FeeType :: " + cardFeeTypeId.toString()).append(" at Time ::" + new Date());
        LOGGER.info(sb.toString());
        String response = null;
        if (fee != null && !fee.equals("") && !fee.equals("0.0"))
            response = commandManager.executeCommand(dWrapper, CommandFieldConstants.CMD_DEBIT_CARD_CW);

        if(workFlowWrapper.getCommissionAmountsHolder() != null) {
            fee = String.valueOf(workFlowWrapper.getCommissionAmountsHolder().getTransactionAmount());
            if (workFlowWrapper.getCommissionAmountsHolder().getExclusivePercentAmount() > 0.0 || workFlowWrapper.getCommissionAmountsHolder().getExclusiveFixAmount() > 0.0) {
                fee = String.valueOf(Double.valueOf(fee) + workFlowWrapper.getCommissionAmountsHolder().getStakeholderCommissionsMap().get(CommissionConstantsInterface.FED_STAKE_HOLDER_ID));
            }
            else{
                fee = String.valueOf(workFlowWrapper.getCommissionAmountsHolder().getTransactionAmount());
            }
        }
        else{
            fee = "0.0";
        }

        if (response != null)
            this.populateXMLParams(response);

        Calendar date = Calendar.getInstance();
        Date startDate = null;

        if(workFlowWrapper.getCardFeeRuleModel() != null) {
            if(productId.equals(ProductConstantsInterface.DEBIT_CARD_ANNUAL_FEE)){
                if(workFlowWrapper.getCardFeeRuleModel().getInstallmentPlan().equals("QUARTERLY")) {
                    date.setTime(new Date());
                    date.add(Calendar.MONTH, 3);
                    startDate = date.getTime();
                    DateFormat format = new SimpleDateFormat("dd/MMM/yyyy");
                    String dateStr = null;

//                    dateStr = format.format(date);

                    model.setLastInstallmentDateForAnnual(new Date());
                    model.setNewInstallmentDateForAnnual(startDate);
                    model.setNoOfInstallmentsAnnual(workFlowWrapper.getCardFeeRuleModel().getNoOfInstallments());
                    if(model.getRemainingNoOfInstallmentsAnnual() != 0) {
                        model.setRemainingNoOfInstallmentsAnnual(model.getRemainingNoOfInstallmentsAnnual() - 1);
                    }
                    else{
                        model.setRemainingNoOfInstallmentsAnnual(workFlowWrapper.getCardFeeRuleModel().getNoOfInstallments() - 1);
                    }
                }
                else if(workFlowWrapper.getCardFeeRuleModel().getInstallmentPlan().equals("BI-ANNUAL")){
                    date.setTime(new Date());
                    date.add(Calendar.MONTH, 6);
                    startDate = date.getTime();
                    DateFormat format = new SimpleDateFormat("dd/MMM/yyyy");
                    String dateStr = null;

//                    dateStr = format.format(date);

                    model.setLastInstallmentDateForAnnual(new Date());
                    model.setNewInstallmentDateForAnnual(startDate);
                    model.setNoOfInstallmentsAnnual(workFlowWrapper.getCardFeeRuleModel().getNoOfInstallments());
                    if(model.getRemainingNoOfInstallmentsAnnual() != 0) {
                        model.setRemainingNoOfInstallmentsAnnual(model.getRemainingNoOfInstallmentsAnnual() - 1);
                    }
                    else{
                        model.setRemainingNoOfInstallmentsAnnual(workFlowWrapper.getCardFeeRuleModel().getNoOfInstallments() - 1);
                    }

                }
                else{
                    date.setTime(new Date());
                    date.add(Calendar.MONTH, 12);
                    startDate = date.getTime();
                    DateFormat format = new SimpleDateFormat("dd/MMM/yyyy");
                    String dateStr = null;

//                    dateStr = format.format(date);

                    model.setLastInstallmentDateForAnnual(new Date());
                    model.setNoOfInstallmentsAnnual(workFlowWrapper.getCardFeeRuleModel().getNoOfInstallments());
                    if(model.getRemainingNoOfInstallmentsAnnual() != 0) {
                        model.setRemainingNoOfInstallmentsAnnual(model.getRemainingNoOfInstallmentsAnnual() - 1);
                    }
                    else{
                        model.setRemainingNoOfInstallmentsAnnual(workFlowWrapper.getCardFeeRuleModel().getNoOfInstallments() - 1);
                    }

//                    model.setNewInstallmentDateForAnnual(dateStr);
                }
                model.setIsInstallments(workFlowWrapper.getCardFeeRuleModel().getIsInstallments());
            }
            else if(productId.equals(ProductConstantsInterface.CUSTOMER_DEBIT_CARD_ISSUANCE) || productId.equals(ProductConstantsInterface.DEBIT_CARD_ISSUANCE)){
                if(workFlowWrapper.getCardFeeRuleModel().getInstallmentPlan().equals("QUARTERLY")){
                    date.setTime(new Date());
                    date.add(Calendar.MONTH, 3);
                    startDate = date.getTime();
                    DateFormat format = new SimpleDateFormat("dd/MMM/yyyy");
                    String dateStr = null;

//                    dateStr = format.format(date);

                    model.setLastInstallmentDateForIssuance(new Date());
                    model.setNewInstallmentDateForIssuance(startDate);
                    model.setNoOfInstallments(workFlowWrapper.getCardFeeRuleModel().getNoOfInstallments());
                    model.setRemainingNoOfInstallments(model.getRemainingNoOfInstallments() - 1);
                }
                else if(workFlowWrapper.getCardFeeRuleModel().getInstallmentPlan().equals("BI-ANNUAL")){
                    date.setTime(new Date());
                    date.add(Calendar.MONTH, 6);
                    startDate = date.getTime();
                    DateFormat format = new SimpleDateFormat("dd/MMM/yyyy");
                    String dateStr = null;

//                    dateStr = format.format(date);

                    model.setLastInstallmentDateForIssuance(new Date());
                    model.setNewInstallmentDateForIssuance(startDate);
                    model.setNoOfInstallments(workFlowWrapper.getCardFeeRuleModel().getNoOfInstallments());
                    model.setRemainingNoOfInstallments(model.getRemainingNoOfInstallments() - 1);

                }
                else{
                    date.setTime(new Date());
                    date.add(Calendar.MONTH, 12);
                    DateFormat format = new SimpleDateFormat("dd/MMM/yyyy");
                    String dateStr = null;

//                    dateStr = format.format(date);

                    model.setLastInstallmentDateForIssuance(new Date());
                    model.setNoOfInstallments(workFlowWrapper.getCardFeeRuleModel().getNoOfInstallments());
                    model.setRemainingNoOfInstallments(model.getRemainingNoOfInstallments() - 1);

                }
            }
            else{
                if(workFlowWrapper.getCardFeeRuleModel().getInstallmentPlan().equals("QUARTERLY")){
                    startDate = null;
                    date.setTime(new Date());
                    date.add(Calendar.MONTH, 3);
                    startDate = date.getTime();
//                    DateFormat format = new SimpleDateFormat("dd/MMM/yyyy");
//                    String dateStr = null;
//
//                    dateStr = format.format(String.valueOf(date));

                    model.setLastInstallmentDateForReIssuance(new Date());
                    model.setNewInstallmentDateForReIssuance(startDate);
                    model.setNoOfInstallments(workFlowWrapper.getCardFeeRuleModel().getNoOfInstallments());
                    model.setRemainingNoOfInstallments(model.getRemainingNoOfInstallments() - 1);

                }
                else if(workFlowWrapper.getCardFeeRuleModel().getInstallmentPlan().equals("BI-ANNUAL")){
                    startDate = null;
                    date.setTime(new Date());
                    date.add(Calendar.MONTH, 6);
                    startDate = date.getTime();
//                    DateFormat format = new SimpleDateFormat("dd/MMM/yyyy");
//                    String dateStr = null;
//
//                    dateStr = format.format(String.valueOf(date));

                    model.setLastInstallmentDateForReIssuance(new Date());
                    model.setNewInstallmentDateForReIssuance(startDate);
                    model.setNoOfInstallments(workFlowWrapper.getCardFeeRuleModel().getNoOfInstallments());
                    model.setRemainingNoOfInstallments(model.getRemainingNoOfInstallments() - 1);

                }
                else{
                    date.setTime(new Date());
                    date.add(Calendar.MONTH, 12);
                    DateFormat format = new SimpleDateFormat("dd/MMM/yyyy");
                    String dateStr = null;

//                    dateStr = format.format(date);

                    model.setLastInstallmentDateForReIssuance(new Date());
//                    model.setNewInstallmentDateForReIssuance(startDate);
                    model.setNoOfInstallments(workFlowWrapper.getCardFeeRuleModel().getNoOfInstallments());
                    model.setRemainingNoOfInstallments(model.getRemainingNoOfInstallments() - 1);
                }
            }
//            debitCardModelDAO.saveOrUpdateDebitCard(model);
            model.setTransactionCode(transactionCode);
            model.setFee(fee);
        }


        sb.append("End of executeIssuanceFee() in DebitCardManagerImpl for Product :: ").append(productId.toString());
        sb.append("\nand Mobile # :: " + mobileNo + " and CNIC :: " + cNic + " and FeeType :: " + cardFeeTypeId.toString()).append(" at Time ::" + new Date());
        sb.append("\n Response :: " + response);
        LOGGER.info(sb.toString());
        return response;
    }
    private String customerbalance(String cnic) throws FrameworkCheckedException {
        AppUserModel appUserModel = new AppUserModel();
        appUserModel = commandManager.getCommonCommandManager().getAppUserModelByCNIC(cnic);
        AccountInfoModel accountInfoModel = null;
        try {
            accountInfoModel = commandManager.getCommonCommandManager().getAccountInfoModel(appUserModel.getCustomerId(), PaymentModeConstantsInterface.BRANCHLESS_BANKING_ACCOUNT);
        } catch (Exception e) {
            throw new FrameworkCheckedException(e.getMessage());
        }
        SmartMoneyAccountModel smartMoneyAccountModel = commandManager.getCommonCommandManager().getSmartMoneyAccountByAppUserModelAndPaymentModId(appUserModel,
                PaymentModeConstantsInterface.BRANCHLESS_BANKING_ACCOUNT);
        String customerBalance = null;

        try {
            customerBalance = commandManager.getCommonCommandManager().getAccountBalance(accountInfoModel, smartMoneyAccountModel);
        } catch (Exception e) {
            throw new FrameworkCheckedException(e.getMessage());
        }
        return customerBalance;
    }

    private Double checkCustomerBalanceForDebitCard(DebitCardModel debitCardModel) throws FrameworkCheckedException {
        ProductModel productModel = new ProductModel();
        Long productId = ProductConstantsInterface.DEBIT_CARD_RE_ISSUANCE;
        AppUserModel appUserModel = new AppUserModel();
        WorkFlowWrapper workFlowWrapper = new WorkFlowWrapperImpl();
        Long distributorId = null;
        CommissionWrapper commissionWrapper;
        CustomerModel customerModel = null;
        appUserModel = commandManager.getCommonCommandManager().getAppUserWithRegistrationState(debitCardModel.getMobileNo(), debitCardModel.getCnic(), RegistrationStateConstantsInterface.VERIFIED);
        if (customerModel == null) {
            CustomerModel cModel = new CustomerModel();
            cModel.setCustomerId(appUserModel.getCustomerId());
            customerModel = commandManager.getCommonCommandManager().getCustomerModelById(appUserModel.getCustomerId());
        }
        AccountInfoModel accountInfoModel = null;
        try {
            accountInfoModel = commandManager.getCommonCommandManager().getAccountInfoModel(appUserModel.getCustomerId(), PaymentModeConstantsInterface.BRANCHLESS_BANKING_ACCOUNT);
        } catch (Exception e) {
            throw new FrameworkCheckedException(e.getMessage());
        }
        SmartMoneyAccountModel smartMoneyAccountModel = commandManager.getCommonCommandManager().getSmartMoneyAccountByAppUserModelAndPaymentModId(appUserModel,
                PaymentModeConstantsInterface.BRANCHLESS_BANKING_ACCOUNT);
        if (productModel == null || (productModel != null && productModel.getProductId() == null)) {
            BaseWrapper baseWrapper1 = new BaseWrapperImpl();
            productModel = new ProductModel();
            productModel.setProductId(productId);
            baseWrapper1.setBasePersistableModel(productModel);
            baseWrapper1 = commandManager.getCommonCommandManager().loadProduct(baseWrapper1);
            productModel = (ProductModel) baseWrapper1.getBasePersistableModel();
        }
        if (productId.equals(ProductConstantsInterface.DEBIT_CARD_ISSUANCE)) {
            RetailerContactModel retailerContactModel = retailerContactDAO.findByPrimaryKey(ThreadLocalAppUser.getAppUserModel().getRetailerContactId());
            RetailerModel retailerModel = retailerDAO.findByPrimaryKey(retailerContactModel.getRetailerId());
            distributorId = retailerModel.getDistributorId();
        }
        workFlowWrapper.setProductModel(productModel);

        Double fee = 0.0D;
        CardFeeRuleModel cardFeeRuleModel = new CardFeeRuleModel(CardTypeConstants.DEBIT_CARD, UserTypeConstantsInterface.CUSTOMER,
                customerModel.getSegmentId(), distributorId, 2l, customerModel.getCustomerAccountTypeId());
        if (UserUtils.getCurrentUser().getAppUserTypeId().equals(UserTypeConstantsInterface.MNO))
            cardFeeRuleModel.setMnoId(50028L);
        else
            cardFeeRuleModel.setMnoId(50027L);
        CardFeeRuleModel model = commandManager.getCommonCommandManager().getCardConfigurationManager().loadCardFeeRuleModel(cardFeeRuleModel);
        if (model != null)
            fee = model.getAmount();
        workFlowWrapper.setTransactionAmount(fee);
        workFlowWrapper.setSmartMoneyAccountModel(smartMoneyAccountModel);
        TransactionModel transactionModel = new TransactionModel();
        workFlowWrapper.setProductModel(productModel);
        TransactionTypeModel transactionTypeModel = new TransactionTypeModel();
        transactionTypeModel.setTransactionTypeId(TransactionTypeConstantsInterface.DEBIT_CARD_CW_TX);
        workFlowWrapper.setProductModel(productModel);
        workFlowWrapper.setTransactionTypeModel(transactionTypeModel);
        SegmentModel segmentModel = new SegmentModel();
        segmentModel.setSegmentId(customerModel.getSegmentId());
        workFlowWrapper.setSegmentModel(segmentModel);
        DeviceTypeModel deviceTypeModel = new DeviceTypeModel();
        deviceTypeModel.setDeviceTypeId(DeviceTypeConstantsInterface.MOBILE);
        workFlowWrapper.setDeviceTypeModel(deviceTypeModel);
        workFlowWrapper.setDeviceTypeModel(new DeviceTypeModel());
        workFlowWrapper.getDeviceTypeModel().setDeviceTypeId(DeviceTypeConstantsInterface.MOBILE);
        transactionModel.setTransactionAmount(Double.valueOf(fee));
        workFlowWrapper.setTransactionModel(transactionModel);
        workFlowWrapper.setTaxRegimeModel(customerModel.getTaxRegimeIdTaxRegimeModel());
        if (ThreadLocalAppUser.getAppUserModel() == null)
            ThreadLocalAppUser.setAppUserModel(appUserModel);
        commissionWrapper = commandManager.getCommonCommandManager().calculateCommission(workFlowWrapper);
        CommissionAmountsHolder commissionAmountsHolder = (CommissionAmountsHolder) commissionWrapper.getCommissionWrapperHashMap()
                .get(CommissionConstantsInterface.COMMISSION_AMOUNTS_HOLDER);
        Double transactionAmount = commissionAmountsHolder.getTransactionAmount();
        return transactionAmount;

    }


    @Override
    public String makeFeeDeductionDebitCardFailedCommand(DebitCardChargesSafRepoModel debitCardChargesSafRepoModel) throws FrameworkCheckedException {
        AppUserModel appUserModel = new AppUserModel();
        String response = null;
        DebitCardModel cardModel = null;

        appUserModel = commandManager.getCommonCommandManager().getAppUserWithRegistrationState(debitCardChargesSafRepoModel.getMobileNo(), debitCardChargesSafRepoModel.getCnic(), RegistrationStateConstantsInterface.VERIFIED);
        AccountInfoModel accountInfoModel = null;
        try {
            accountInfoModel = commandManager.getCommonCommandManager().getAccountInfoModel(appUserModel.getCustomerId(), PaymentModeConstantsInterface.BRANCHLESS_BANKING_ACCOUNT);
        } catch (Exception e) {
            throw new FrameworkCheckedException(e.getMessage());
        }
        SmartMoneyAccountModel smartMoneyAccountModel = commandManager.getCommonCommandManager().getSmartMoneyAccountByAppUserModelAndPaymentModId(appUserModel,
                PaymentModeConstantsInterface.BRANCHLESS_BANKING_ACCOUNT);
        String customerBalance = null;

        try {
            customerBalance = commandManager.getCommonCommandManager().getAccountBalance(accountInfoModel, smartMoneyAccountModel);
        } catch (Exception e) {
            throw new FrameworkCheckedException(e.getMessage());
        }

        if (Double.parseDouble(customerBalance) >= debitCardChargesSafRepoModel.getTransactionAmount()) {
            if (debitCardChargesSafRepoModel.getCardTypeConstant().equals(CardConstantsInterface.CARD_FEE_TYPE_RE_ISSUANCE)) {
                List<DebitCardModel> reIssuanceList = new ArrayList<>(0);
                List<DebitCardModel> importedList = new ArrayList<>(0);
                cardModel = this.getDebitCradModelByNicAndState(debitCardChargesSafRepoModel.getCnic(), CardConstantsInterface.CARD_STATUS_IN_PROCESS);
                if (cardModel != null) {
                    cardModel.setImportedOn(new Date());
                    cardModel.setUpdatedBy(3L);//Scheduler App User Id
                    cardModel.setCardStatusId(CardConstantsInterface.CARD_STATUS_REJECTED);
                    cardModel.setDeniedOn(new Date());
                    cardModel.setCardStateId(CardConstantsInterface.CARD_STATE_HOT);
                    cardModel.setHotOn(new Date());
                    Long curretnTime = System.currentTimeMillis();
                    cardModel.setCnic(cardModel.getCnic() + PortalConstants.PREFIX_SETTLED_ACCOUNT + curretnTime);
                    cardModel.setMobileNo(cardModel.getMobileNo() + PortalConstants.PREFIX_SETTLED_ACCOUNT + curretnTime);
                    cardModel.setCardNo(cardModel.getCardNo() + PortalConstants.PREFIX_SETTLED_ACCOUNT + curretnTime);
                    smsMessage = new SmsMessage(cardModel.getMobileNo(), MessageUtil.getMessage("debit.card.req.rejected"));
                    rejectedCardSmsList.add(smsMessage);
                    importedList.add(cardModel);
                }
                if (importedList != null && !importedList.isEmpty()) {
                    this.updateBulkDebitCardModel(importedList);
                }
                DebitCardModel reIssuanceModel = prepareDebitCardChargesModelForReIssuance(debitCardChargesSafRepoModel, cardModel, cardModel.getIssuanceDate(), cardModel.getFeeDeductionDate());
                reIssuanceList.add(reIssuanceModel);
                StringBuilder sb = new StringBuilder();
                if (reIssuanceList != null && !reIssuanceList.isEmpty())
                    this.updateBulkDebitCardModel(reIssuanceList);
                sb = new StringBuilder();
                BaseWrapper dWrapper = new BaseWrapperImpl();
                dWrapper.putObject(CommandFieldConstants.KEY_MOB_NO, debitCardChargesSafRepoModel.getMobileNo());
                dWrapper.putObject(CommandFieldConstants.KEY_AMOUNT, debitCardChargesSafRepoModel.getTransactionAmount());
                dWrapper.putObject(CommandFieldConstants.KEY_PROD_ID, debitCardChargesSafRepoModel.getProductId().toString());
                dWrapper.putObject(CommandFieldConstants.KEY_DEVICE_TYPE_ID, DeviceTypeConstantsInterface.MOBILE);
                dWrapper.putObject(CommandFieldConstants.KEY_TXAM, debitCardChargesSafRepoModel.getTransactionAmount().toString());
                dWrapper.putObject(CommandFieldConstants.KEY_TPAM, "0");
                dWrapper.putObject(CommandFieldConstants.KEY_CUSTOMER_MOBILE, debitCardChargesSafRepoModel.getMobileNo());
                dWrapper.putObject(CommandFieldConstants.KEY_TAMT, debitCardChargesSafRepoModel.getTransactionAmount());
                dWrapper.putObject(CommandFieldConstants.KEY_TERMINAL_ID, "MOBILE");
                dWrapper.putObject(CommandFieldConstants.KEY_STAN, "");
                dWrapper.putObject(CommandFieldConstants.KEY_PAYMENT_MODE, "");
                dWrapper.putObject(CommandFieldConstants.KEY_APP_ID, "2");//Customer Initiated Transaction
                sb.append("Start of executeIssuanceFee() in DebitCardManagerImpl for Product :: ").append(debitCardChargesSafRepoModel.getProductId().toString());
                sb.append(" \nand Mobile # :: " + debitCardChargesSafRepoModel.getMobileNo() + " and CNIC :: " + debitCardChargesSafRepoModel.getCnic() + " and FeeType :: " + debitCardChargesSafRepoModel.getCardTypeConstant().toString()).append(" at Time ::" + new Date());
                LOGGER.info(sb.toString());

                if (debitCardChargesSafRepoModel.getTransactionAmount() != null && !debitCardChargesSafRepoModel.getTransactionAmount().equals("") && !debitCardChargesSafRepoModel.getTransactionAmount().equals("0.0"))
                    response = commandManager.executeCommand(dWrapper, CommandFieldConstants.CMD_DEBIT_CARD_CW);

                if (response != null)
                    debitCardModelDAO.saveOrUpdate(reIssuanceModel);
                debitCardChargesSafRepoModel.setIsCompleted("1");
                debitCardChargesSafRepoModel.setTransactionstatus("Completed");
                this.saveOrUpdateDebitChargesSafRepoCardModel(debitCardChargesSafRepoModel);

            } else {

                DebitCardModel debitCardModel1 = null;
                debitCardModel1 = debitCardChargesDAO.loadAllCardsOnRenewRequired(debitCardChargesSafRepoModel);
                if (debitCardModel1 == null) {
                    debitCardChargesSafRepoModel.setIsCompleted("1");
                    debitCardChargesSafRepoModel.setTransactionstatus("Completed");
                    this.saveOrUpdateDebitChargesSafRepoCardModel(debitCardChargesSafRepoModel);
                } else {
                    StringBuilder sb = new StringBuilder();
                    sb = new StringBuilder();
                    BaseWrapper dWrapper = new BaseWrapperImpl();
                    dWrapper.putObject(CommandFieldConstants.KEY_MOB_NO, debitCardChargesSafRepoModel.getMobileNo());
                    dWrapper.putObject(CommandFieldConstants.KEY_AMOUNT, debitCardChargesSafRepoModel.getTransactionAmount());
                    dWrapper.putObject(CommandFieldConstants.KEY_PROD_ID, debitCardChargesSafRepoModel.getProductId().toString());
                    dWrapper.putObject(CommandFieldConstants.KEY_DEVICE_TYPE_ID, DeviceTypeConstantsInterface.MOBILE);
                    dWrapper.putObject(CommandFieldConstants.KEY_TXAM, debitCardChargesSafRepoModel.getTransactionAmount().toString());
                    dWrapper.putObject(CommandFieldConstants.KEY_TPAM, "0");
                    dWrapper.putObject(CommandFieldConstants.KEY_CUSTOMER_MOBILE, debitCardChargesSafRepoModel.getMobileNo());
                    dWrapper.putObject(CommandFieldConstants.KEY_TAMT, debitCardChargesSafRepoModel.getTransactionAmount());
                    dWrapper.putObject(CommandFieldConstants.KEY_TERMINAL_ID, "MOBILE");
                    dWrapper.putObject(CommandFieldConstants.KEY_STAN, "");
                    dWrapper.putObject(CommandFieldConstants.KEY_PAYMENT_MODE, "");
                    dWrapper.putObject(CommandFieldConstants.KEY_APP_ID, "2");//Customer Initiated Transaction
                    sb.append("Start of executeIssuanceFee() in DebitCardManagerImpl for Product :: ").append(debitCardChargesSafRepoModel.getProductId().toString());
                    sb.append(" \nand Mobile # :: " + debitCardChargesSafRepoModel.getMobileNo() + " and CNIC :: " + debitCardChargesSafRepoModel.getCnic() + " and FeeType :: " + debitCardChargesSafRepoModel.getCardTypeConstant().toString()).append(" at Time ::" + new Date());
                    LOGGER.info(sb.toString());

                    if (debitCardChargesSafRepoModel.getTransactionAmount() != null && !debitCardChargesSafRepoModel.getTransactionAmount().equals("") && !debitCardChargesSafRepoModel.getTransactionAmount().equals("0.0"))
                        response = commandManager.executeCommand(dWrapper, CommandFieldConstants.CMD_DEBIT_CARD_CW);

                    sb.append("End of executeIssuanceFee() in DebitCardManagerImpl for Product :: ").append(debitCardChargesSafRepoModel.getProductId().toString());
                    sb.append("\nand Mobile # :: " + debitCardChargesSafRepoModel.getMobileNo() + " and CNIC :: " + debitCardChargesSafRepoModel.getCnic() + " and FeeType :: " + debitCardChargesSafRepoModel.getCardTypeConstant().toString()).append(" at Time ::" + new Date());
                    sb.append("\n Response :: " + response);
                    LOGGER.info(sb.toString());
                    if (response != null) {
                        DebitCardModel debitCardModel = new DebitCardModel();
                        debitCardModel.setUpdatedOn(new Date());
                        debitCardModel.setFeeDeductionDate(debitCardChargesSafRepoModel.getCreatedOn());
                        debitCardModel.setCardNo(debitCardChargesSafRepoModel.getDebitCardNo());
                        debitCardModel.setCardStateId(debitCardChargesSafRepoModel.getCardStateId());
                        debitCardModel.setCardStatusId(debitCardChargesSafRepoModel.getCardStatusId());
                        debitCardModel.setCnic(debitCardChargesSafRepoModel.getCnic());
                        debitCardModel.setDebitCardId(debitCardChargesSafRepoModel.getAccountId());
                        this.saveOrUpdateDebitCardModel(debitCardModel);
                        debitCardChargesSafRepoModel.setIsCompleted("1");
                        debitCardChargesSafRepoModel.setTransactionstatus("Completed");
                        this.saveOrUpdateDebitChargesSafRepoCardModel(debitCardChargesSafRepoModel);
                    }
                }
            }
        }

        return response;
    }

    private DebitCardModel prepareDebitCardChargesModelForReIssuance(DebitCardChargesSafRepoModel model, DebitCardModel cardModel, Date date, Date feeDeductionDate) {
        DebitCardModel reIssuanceModel = new DebitCardModel();
        reIssuanceModel.setCreatedOn(new Date());
        reIssuanceModel.setUpdatedOn(new Date());
        reIssuanceModel.setCreatedBy(3L);//Scheduler App User Id
        reIssuanceModel.setUpdatedBy(3L);//Scheduler App User Id
        reIssuanceModel.setIssuanceDate(date);
        reIssuanceModel.setImportedOn(new Date());
        reIssuanceModel.setFeeDeductionDate(feeDeductionDate);
        reIssuanceModel.setReIssuanceDate(model.getCreatedOn());
        reIssuanceModel.setActivationDate(new Date());
        reIssuanceModel.setAppId(4L);// Vision Channel ID
        reIssuanceModel = this.setReIssuanceCardPropsAgainstStatus(reIssuanceModel, String.valueOf(model.getCardStatusId()));
        if (!model.getCardStatusId().equals("3")) {
            reIssuanceModel.setMobileNo(model.getMobileNo());
            reIssuanceModel.setCnic(model.getCnic());
            reIssuanceModel.setCardNo(model.getDebitCardNo());
        }
        reIssuanceModel.setDebitCardEmbosingName(cardModel.getDebitCardEmbosingName());
        reIssuanceModel.setAppUserId(cardModel.getAppUserId());
        reIssuanceModel.setSmartMoneyAccountId(cardModel.getSmartMoneyAccountId());
        reIssuanceModel.setMailingAddressId(cardModel.getMailingAddressId());
        return reIssuanceModel;
    }


    public void saveDebitCardChargesSafRepoRequiresNewTransaction(DebitCardModel model, Long cardFeeTypeId, Long ProductId, Double totalAmount) throws FrameworkCheckedException {
        StringBuilder sb = new StringBuilder();

        DebitCardChargesSafRepoModel debitCardChargesSafRepoModel = new DebitCardChargesSafRepoModel();
        debitCardChargesSafRepoModel.setDebitCardNo(model.getCardNo());
        debitCardChargesSafRepoModel.setMobileNo(model.getMobileNo());
        debitCardChargesSafRepoModel.setCardStateId(model.getCardStateId());
        if (cardFeeTypeId.equals(CardConstantsInterface.CARD_FEE_TYPE_ANNUAL)) {
            debitCardChargesSafRepoModel.setAccountId(model.getDebitCardId());
        } else {
            debitCardChargesSafRepoModel.setAccountId(null);
        }
        debitCardChargesSafRepoModel.setCardStatusId(model.getCardStatusId());
        debitCardChargesSafRepoModel.setCardTypeConstant(cardFeeTypeId);
        debitCardChargesSafRepoModel.setProductId(ProductId);
        debitCardChargesSafRepoModel.setTransactionAmount(totalAmount);
        debitCardChargesSafRepoModel.setTransactionstatus("Failed");
        debitCardChargesSafRepoModel.setCnic(model.getCnic());
        debitCardChargesSafRepoModel.setTransactionDate(new Date());
        debitCardChargesSafRepoModel.setIsCompleted("0");
        if (ThreadLocalAppUser.getAppUserModel() != null && ThreadLocalAppUser.getAppUserModel().getAppUserId() != null) {
            debitCardChargesSafRepoModel.setCreatedBy(ThreadLocalAppUser.getAppUserModel().getAppUserId());
        } else {
            debitCardChargesSafRepoModel.setCreatedBy(2L);
        }
        debitCardChargesSafRepoModel.setCreatedOn(new Date());
        debitCardChargesSafRepoModel.setUpdatedOn(new Date());
        sb.append("[ComonCommandManagerImpl.saveDebitCardChargesSafRepo] Inserting into saveDebitCardChargesSafRepo:").append(ProductId.toString());
        sb.append(" \nand Mobile # :: " + model.getMobileNo() + " and CNIC :: " + model.getCnic() + " and FeeType :: " + cardFeeTypeId.toString()).append(" at Time ::" + new Date());
        LOGGER.info(sb.toString());
        DebitCardChargesSafRepoModel debitCardChargesSafRepoModel1 = null;
        debitCardChargesSafRepoModel1 = debitCardChargesDAO.loadExistingDebitCardChargesSafRepe(debitCardChargesSafRepoModel);
        if (debitCardChargesSafRepoModel1 != null) {

            throw new FrameworkCheckedException("DebitCardChargesSafRepo Data Already Exist With Mobile Number : " + debitCardChargesSafRepoModel1.getMobileNo() + " and Product ID:" + debitCardChargesSafRepoModel1.getMobileNo());
        } else {
            debitCardChargesDAO.createOrUpdateDebitCardChargesSafRepoRequiresNewTransaction(debitCardChargesSafRepoModel);

        }

    }


    private DebitCardModel setReIssuanceCardPropsAgainstStatus(DebitCardModel cardModel, String cardStatus) {
        if (cardStatus.equalsIgnoreCase("1") || cardStatus.equalsIgnoreCase("1")) {
            if (cardStatus.equalsIgnoreCase("1")) {
                cardModel.setCardStateId(CardConstantsInterface.CARD_STATE_COLD);//
                cardModel.setColdOn(new Date());
                cardModel.setCardStatusId(CardConstantsInterface.CARD_STATUS_ACTIVE);
                cardModel.setActiveOn(new Date());
                smsMessage = new SmsMessage(cardModel.getMobileNo(), MessageUtil.getMessage("debit.card.req.active"));
                validCardSmsList.add(smsMessage);
            } else {
                cardModel.setCardStateId(CardConstantsInterface.CARD_STATE_WARM);//
                cardModel.setWarmOn(new Date());
                cardModel.setCardStatusId(CardConstantsInterface.CARD_STATUS_DE_ACTIVATED);
                cardModel.setDeActiveOn(new Date());
                smsMessage = new SmsMessage(cardModel.getMobileNo(), MessageUtil.getMessage("debit.card.req.successful"));
                validCardSmsList.add(smsMessage);
            }
            if (cardModel.getIssuanceDate() == null && cardModel.getActivationDate() == null) {
                cardModel.setActivationDate(new Date());
                cardModel.setIssuanceDate(new Date());
            }
        } else if (cardStatus.equalsIgnoreCase("3")) {
            cardModel.setCardStatusId(CardConstantsInterface.CARD_STATUS_REJECTED);
            cardModel.setDeniedOn(new Date());
            cardModel.setCardStateId(CardConstantsInterface.CARD_STATE_HOT);
            cardModel.setHotOn(new Date());
            Long curretnTime = System.currentTimeMillis();
            cardModel.setCnic(cardModel.getCnic() + PortalConstants.PREFIX_SETTLED_ACCOUNT + curretnTime);
            cardModel.setMobileNo(cardModel.getMobileNo() + PortalConstants.PREFIX_SETTLED_ACCOUNT + curretnTime);
            cardModel.setCardNo(cardModel.getCardNo() + PortalConstants.PREFIX_SETTLED_ACCOUNT + curretnTime);
            //Card Masked
            smsMessage = new SmsMessage(cardModel.getMobileNo(), MessageUtil.getMessage("debit.card.req.rejected"));
            rejectedCardSmsList.add(smsMessage);
        }
        return cardModel;
    }


    private DebitCardModel setCardPropsAgainstStatus(DebitCardModel cardModel, String cardStatus) {
        if (cardStatus.equalsIgnoreCase("001") || cardStatus.equalsIgnoreCase("002")) {
            if (cardStatus.equalsIgnoreCase("001")) {
                cardModel.setCardStateId(CardConstantsInterface.CARD_STATE_COLD);//
                cardModel.setColdOn(new Date());
                cardModel.setCardStatusId(CardConstantsInterface.CARD_STATUS_ACTIVE);
                cardModel.setActiveOn(new Date());
                smsMessage = new SmsMessage(cardModel.getMobileNo(), MessageUtil.getMessage("debit.card.req.active"));
                validCardSmsList.add(smsMessage);
            } else {
                cardModel.setCardStateId(CardConstantsInterface.CARD_STATE_WARM);//
                cardModel.setWarmOn(new Date());
                cardModel.setCardStatusId(CardConstantsInterface.CARD_STATUS_DE_ACTIVATED);
                cardModel.setDeActiveOn(new Date());
                smsMessage = new SmsMessage(cardModel.getMobileNo(), MessageUtil.getMessage("debit.card.req.successful"));
                validCardSmsList.add(smsMessage);
            }
            if (cardModel.getIssuanceDate() == null && cardModel.getActivationDate() == null) {
                cardModel.setActivationDate(new Date());
                cardModel.setIssuanceDate(new Date());
            }
        } else if (cardStatus.equalsIgnoreCase("003")) {
            cardModel.setCardStatusId(CardConstantsInterface.CARD_STATUS_REJECTED);
            cardModel.setDeniedOn(new Date());
            cardModel.setCardStateId(CardConstantsInterface.CARD_STATE_HOT);
            cardModel.setHotOn(new Date());
            Long curretnTime = System.currentTimeMillis();
            cardModel.setCnic(cardModel.getCnic() + PortalConstants.PREFIX_SETTLED_ACCOUNT + curretnTime);
            cardModel.setMobileNo(cardModel.getMobileNo() + PortalConstants.PREFIX_SETTLED_ACCOUNT + curretnTime);
            cardModel.setCardNo(cardModel.getCardNo() + PortalConstants.PREFIX_SETTLED_ACCOUNT + curretnTime);
            //Card Masked
            smsMessage = new SmsMessage(cardModel.getMobileNo(), MessageUtil.getMessage("debit.card.req.rejected"));
            rejectedCardSmsList.add(smsMessage);
        }
        return cardModel;
    }

    @Override
    public List<CardStateModel> getAllCardStates() throws FrameworkCheckedException {
        CustomList<CardStateModel> customList = cardStateDAO.findAll();
        List<CardStateModel> list = null;
        if (customList != null && !customList.getResultsetList().isEmpty())
            list = customList.getResultsetList();

        return list;
    }

    @Override
    public List<CardStatusModel> getAllCardSatus() throws FrameworkCheckedException {
        CustomList<CardStatusModel> customList = cardStatusDAO.findAll();
        List<CardStatusModel> list = null;
        if (customList != null && !customList.getResultsetList().isEmpty())
            list = customList.getResultsetList();

        return list;
    }

    @Override
    public List<CardProdCodeModel> getAllCardProductTypes() throws FrameworkCheckedException {
        CustomList<CardProdCodeModel> customList = fetchCardTypeDAO.findAll();
        List<CardProdCodeModel> list = null;
        if (customList != null && !customList.getResultsetList().isEmpty())
            list = customList.getResultsetList();

        return list;
    }

    @Override
    public List<DebitCardViewModel> searchDebitCardData(SearchBaseWrapper searchBaseWrapper) throws FrameworkCheckedException {
        /*ExampleConfigHolderModel exampleConfigHolderModel = new ExampleConfigHolderModel();
        exampleConfigHolderModel.setEnableLike(Boolean.TRUE);*/
        CustomList<DebitCardViewModel> customList = debitCardViewModelDAO.findByExampleUnSorted((DebitCardViewModel) searchBaseWrapper.getBasePersistableModel(),
                searchBaseWrapper.getPagingHelperModel(), searchBaseWrapper.getSortingOrderMap(),
                searchBaseWrapper.getDateRangeHolderModel(), null, (String) null);

        List<DebitCardViewModel> list = customList.getResultsetList();
        return list;
    }

    @Override
    public List<DebitCardRequestsViewModel> searchDebitCardRequestsData(SearchBaseWrapper searchBaseWrapper) throws FrameworkCheckedException {
        CustomList<DebitCardRequestsViewModel> customList = debitCardRequestsViewModelDAO.findByExample((DebitCardRequestsViewModel) searchBaseWrapper.getBasePersistableModel(),
                searchBaseWrapper.getPagingHelperModel(), searchBaseWrapper.getSortingOrderMap(),
                searchBaseWrapper.getDateRangeHolderModel());

        List<DebitCardRequestsViewModel> list = customList.getResultsetList();
        return list;
    }

    @Override
    public List<DebitCardModel> getDebitCardModelByState(Long cardStateId) throws FrameworkCheckedException {
        return debitCardModelDAO.getDebitCardModelByState(cardStateId);
    }

    @Override
    public List<DebitCardExportDataViewModel> getDataToExport() throws FrameworkCheckedException {
        CustomList<DebitCardExportDataViewModel> customList = debitCardExportDataViewDAO.findAll();
        if (customList != null && !customList.getResultsetList().isEmpty())
            return customList.getResultsetList();
        return null;
    }

    @Override
    public DebitCardModel getDebitCardModelByDebitCardId(Long debitCardId) throws FrameworkCheckedException {
        DebitCardModel debitCardModel = debitCardModelDAO.findByPrimaryKey(debitCardId);
        return debitCardModel;
    }

    @Override
    public DebitCardModel getDebitCradModelByNicAndState(String cnic, Long cardStausId) throws FrameworkCheckedException {
        return debitCardModelDAO.getDebitCradModelByNicAndState(cnic, cardStausId);
    }

    @Override
    public List<DebitCardModel> loadAllCardsOnRenewRequired() throws FrameworkCheckedException {
        return debitCardModelDAO.loadAllCardsOnRenewRequired();
    }

    @Override
    public List<DebitCardModel> loadAllCardsOnRenewRequiredForAnnualFee() throws FrameworkCheckedException {
        return debitCardModelDAO.loadAllCardsOnRenewRequiredForAnnualFee();
    }

    @Override
    public List<DebitCardModel> loadAllCardsOnReIssuanceRequired() throws FrameworkCheckedException {
        return debitCardModelDAO.loadAllCardsOnReIssuanceRequired();
    }


    @Override
    public List<DebitCardChargesSafRepoModel> loadAllDebitCardFeeChargesRequired() throws FrameworkCheckedException {
        return debitCardChargesDAO.loadAllDebitCardCharges();
    }

    public void populateXMLParams(String xml) {
        LOGGER.info("populateProductPurchase(...) in DebitCardIssuanceCommand " + xml);
        NodeList nodeList = this.executeXPathQuery(xml, "//trans/*");
        for (int i = 0; i < nodeList.getLength(); i++) {
            transactionCode = nodeList.item(0).getAttributes().getNamedItem(CommandFieldConstants.KEY_TX_ID).getNodeValue();
            /*NodeList childNodeList = nodeList.item(i).getChildNodes();
            for (int j = 0; j < childNodeList.getLength(); j++) {
                NodeList nList = childNodeList.item(j).getChildNodes();
                if (nList != null && nList.getLength() == 0) {
                    NamedNodeMap namedNodeMap = childNodeList.item(0).getAttributes();
                    if (namedNodeMap != null) {
                        if (namedNodeMap.getNamedItem("ID") != null && !namedNodeMap.getNamedItem("ID").getNodeValue().equals(""))
                            transactionCode = namedNodeMap.getNamedItem("ID").getNodeValue();
                    }
                }
            }*/
        }
    }

    private NodeList executeXPathQuery(String xml, String xpathExpression) {
        Object result = null;
        try {
            domFactory = DocumentBuilderFactory.newInstance();
            domFactory.setNamespaceAware(true); // never forget this!
            DocumentBuilder builder = domFactory.newDocumentBuilder();
            Document doc = builder.parse(new InputSource(new StringReader(xml)));
            XPathFactory factory = XPathFactory.newInstance();
            XPath xpath = factory.newXPath();
            XPathExpression expr = xpath.compile(xpathExpression);
            result = expr.evaluate(doc, XPathConstants.NODESET);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return (NodeList) result;
    }

    public void setDebitCardModelDAO(DebitCardModelDAO debitCardModelDAO) {
        this.debitCardModelDAO = debitCardModelDAO;
    }

    public void setDebitCardMailingAddressDAO(DebitCardMailingAddressDAO debitCardMailingAddressDAO) {
        this.debitCardMailingAddressDAO = debitCardMailingAddressDAO;
    }

    public void setCardStateDAO(CardStateDAO cardStateDAO) {
        this.cardStateDAO = cardStateDAO;
    }

    public void setCardStatusDAO(CardStatusDAO cardStatusDAO) {
        this.cardStatusDAO = cardStatusDAO;
    }

    public void setDebitCardViewModelDAO(DebitCardViewModelDAO debitCardViewModelDAO) {
        this.debitCardViewModelDAO = debitCardViewModelDAO;
    }

    public void setDebitCardExportDataViewDAO(DebitCardExportDataViewDAO debitCardExportDataViewDAO) {
        this.debitCardExportDataViewDAO = debitCardExportDataViewDAO;
    }

    public void setEsbAdapter(ESBAdapter esbAdapter) {
        this.esbAdapter = esbAdapter;
    }

    public void setSmsSenderService(SmsSenderService smsSenderService) {
        this.smsSenderService = smsSenderService;
    }

    public void setCommandManager(CommandManager commandManager) {
        this.commandManager = commandManager;
    }

    public void setDebitCardChargesDAO(DebitCardChargesDAO debitCardChargesDAO) {
        this.debitCardChargesDAO = debitCardChargesDAO;
    }


    public void setGenericDAO(GenericDao genericDAO) {
        this.genericDAO = genericDAO;
    }

    public void setDebitCardRequestsViewModelDAO(DebitCardRequestsViewModelDAO debitCardRequestsViewModelDAO) {
        this.debitCardRequestsViewModelDAO = debitCardRequestsViewModelDAO;
    }

    public void setFetchCardTypeDAO(FetchCardTypeDAO fetchCardTypeDAO) {
        this.fetchCardTypeDAO = fetchCardTypeDAO;
    }
}
