package com.inov8.microbank.debitcard.service;

import com.inov8.framework.common.exception.FrameworkCheckedException;
import com.inov8.framework.common.wrapper.BaseWrapper;
import com.inov8.framework.common.wrapper.SearchBaseWrapper;
import com.inov8.microbank.cardconfiguration.model.CardStateModel;
import com.inov8.microbank.cardconfiguration.model.CardStatusModel;
import com.inov8.microbank.common.model.AppUserModel;
import com.inov8.microbank.common.model.CardProdCodeModel;
import com.inov8.microbank.common.model.veriflymodule.DebitCardChargesSafRepoModel;
import com.inov8.microbank.debitcard.model.*;

import java.util.List;

public interface DebitCardManager {
    List<DebitCardModel> getDebitCardModelByMobileAndNIC(String mobileNo,
                                                         String nic) throws FrameworkCheckedException;
    public DebitCardMailingAddressModel saveOrUpdateDebitCardMailingAddress(DebitCardMailingAddressModel debitCardMailingAddressModel) throws FrameworkCheckedException;
    public DebitCardModel saveOrUpdateDebitCardModel(DebitCardModel debitCardModel) throws FrameworkCheckedException;
    public DebitCardModel saveOrUpdateDebitCardModelForAnnualFee(DebitCardModel debitCardModel) throws FrameworkCheckedException;
    public DebitCardModel saveOrUpdateDebitCardModelForReIssuanceFee(DebitCardModel debitCardModel) throws FrameworkCheckedException;
    public DebitCardModel saveOrUpdateDebitCardModelForIssuanceFee(DebitCardModel debitCardModel) throws FrameworkCheckedException;
    public DebitCardModel saveOrUpdateReIssuanceDebitCardModel(DebitCardModel debitCardModel) throws FrameworkCheckedException;
    public DebitCardChargesSafRepoModel saveOrUpdateDebitChargesSafRepoCardModel(DebitCardChargesSafRepoModel debitCardChargesSafRepoModel) throws FrameworkCheckedException;
    public BaseWrapper updateDebitCardIssuenceRequestWithAuthorization(BaseWrapper baseWrapper) throws FrameworkCheckedException;
    public void updateBulkDebitCardModel(List<DebitCardModel> list) throws FrameworkCheckedException;
    void saveDebitCardImportExportSchedulerRequest() throws FrameworkCheckedException;
    void saveDebitCardImportExportReissuanceSchedulerRequest(DebitCardModel debitCardModel) throws FrameworkCheckedException;

    public List<CardStateModel> getAllCardStates() throws FrameworkCheckedException;
    public List<CardStatusModel> getAllCardSatus() throws FrameworkCheckedException;
    public List<CardProdCodeModel> getAllCardProductTypes() throws FrameworkCheckedException;

    public List<DebitCardViewModel> searchDebitCardData(SearchBaseWrapper searchBaseWrapper) throws FrameworkCheckedException;

    //Added by Abubakar
    public List<DebitCardRequestsViewModel> searchDebitCardRequestsData(SearchBaseWrapper searchBaseWrapper) throws FrameworkCheckedException;

    public List<DebitCardModel> getDebitCardModelByState(Long cardStateId) throws FrameworkCheckedException;

    public List<DebitCardExportDataViewModel> getDataToExport() throws FrameworkCheckedException;

    public DebitCardModel getDebitCardModelByDebitCardId(Long debitCardId) throws FrameworkCheckedException;


    public DebitCardModel getDebitCradModelByNicAndState(String cnic, Long cardStausId) throws FrameworkCheckedException;

    List<DebitCardModel> loadAllCardsOnRenewRequired() throws FrameworkCheckedException;
    List<DebitCardModel> loadAllCardsOnRenewRequiredForAnnualFee() throws FrameworkCheckedException;
    List<DebitCardModel> loadAllCardsOnReIssuanceRequired() throws FrameworkCheckedException;
    List<DebitCardChargesSafRepoModel> loadAllDebitCardFeeChargesRequired() throws FrameworkCheckedException;




    String makeFeeDeductionCommand(String fee,DebitCardModel model,Long productId,Long cardFeeTypeId,Long deviceTypeId) throws FrameworkCheckedException;
    public void saveDebitCardChargesSafRepoRequiresNewTransaction(DebitCardModel model, Long cardFeeTypeId, Long ProductId, Double totalAmount) throws FrameworkCheckedException;
    String makeFeeDeductionDebitCardFailedCommand(DebitCardChargesSafRepoModel debitCardChargesSafRepoModel) throws  FrameworkCheckedException;
}
