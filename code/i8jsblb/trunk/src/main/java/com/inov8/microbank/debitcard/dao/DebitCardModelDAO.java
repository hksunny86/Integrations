package com.inov8.microbank.debitcard.dao;

import com.inov8.framework.common.exception.FrameworkCheckedException;
import com.inov8.framework.server.dao.framework.BaseDAO;
import com.inov8.microbank.debitcard.model.DebitCardModel;

import java.util.List;

public interface DebitCardModelDAO extends BaseDAO<DebitCardModel,Long> {
    List<DebitCardModel> getDebitCardModelByMobileAndNIC(String mobileNo,
                                                         String nic) throws FrameworkCheckedException;

    DebitCardModel getDebitCardModelByCardNumber(String cardNo) throws FrameworkCheckedException;

    List<DebitCardModel> getDebitCardModelByState(Long cardStateId) throws FrameworkCheckedException;

    public DebitCardModel getDebitCradModelByNicAndState(String cnic, Long cardStausId) throws FrameworkCheckedException;

    DebitCardModel getDebitCardModelByCustomerAppUserId(Long appUserId) throws FrameworkCheckedException;

    DebitCardModel getDebitCardModelByDebitCardId(Long debitCardId) throws FrameworkCheckedException;

    DebitCardModel getDebitCardModelByCustomerAppUserId(Long appUserId, Long cardStatus) throws FrameworkCheckedException;

    //    DebitCardModel getDebitCardModelByAppUserId(Long appUserId) throws FrameworkCheckedException;
    DebitCardModel getDebitCardModelByAppUserIdAndCardStateId(Long aapUserId,Long[] cardStateId) throws FrameworkCheckedException;

    List<DebitCardModel> loadAllCardsOnRenewRequired() throws FrameworkCheckedException;
    List<DebitCardModel> loadAllCardsOnRenewRequiredForAnnualFee() throws FrameworkCheckedException;

    List<DebitCardModel> loadAllCardsOnReIssuanceRequired() throws FrameworkCheckedException;

    void updateDebitCardFeeDeductionDate(DebitCardModel model) throws FrameworkCheckedException;
    void updateDebitCardFeeDeductionDateForAnnualFee(DebitCardModel model) throws FrameworkCheckedException;
    void updateDebitCardFeeDeductionDateForReIssuanceFee(DebitCardModel model) throws FrameworkCheckedException;
    void updateDebitCardFeeDeductionDateForIssuanceFee(DebitCardModel model) throws FrameworkCheckedException;
}
