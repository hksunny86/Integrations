package com.inov8.microbank.server.dao.debitCardChargesmodule;

import com.inov8.framework.common.exception.FrameworkCheckedException;
import com.inov8.framework.server.dao.framework.BaseDAO;
import com.inov8.microbank.common.model.AppUserModel;
import com.inov8.microbank.common.model.veriflymodule.DebitCardChargesSafRepoModel;
import com.inov8.microbank.debitcard.model.DebitCardModel;
import org.hibernate.HibernateException;

import java.util.List;

public interface DebitCardChargesDAO extends BaseDAO<DebitCardChargesSafRepoModel,Long> {

    List<DebitCardChargesSafRepoModel> loadAllDebitCardCharges() throws FrameworkCheckedException;

    public void createOrUpdateDebitCardChargesSafRepoRequiresNewTransaction(DebitCardChargesSafRepoModel model) throws FrameworkCheckedException;
    void updateDebitCardFeeDeductionSafRepo(DebitCardChargesSafRepoModel model) throws FrameworkCheckedException;
    public DebitCardModel loadAllCardsOnRenewRequired(DebitCardChargesSafRepoModel debitCardModel) throws FrameworkCheckedException;
    public DebitCardChargesSafRepoModel loadExistingDebitCardChargesSafRepe(DebitCardChargesSafRepoModel debitCardChargesSafRepoModel) throws FrameworkCheckedException;

}
