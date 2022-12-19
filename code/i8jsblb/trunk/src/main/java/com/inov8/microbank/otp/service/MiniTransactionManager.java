package com.inov8.microbank.otp.service;

import com.inov8.framework.common.exception.FrameworkCheckedException;
import com.inov8.framework.common.wrapper.SearchBaseWrapper;
import com.inov8.microbank.common.exception.CommandException;
import com.inov8.microbank.common.model.MiniTransactionModel;
import com.inov8.microbank.common.util.MiniXMLUtil;

/**
 * Created by AhmedMo on 8/20/2015.
 */
public interface MiniTransactionManager {
    MiniTransactionModel saveOrUpdate(MiniTransactionModel miniTransactionModel) throws FrameworkCheckedException;

    MiniTransactionModel generateOTP(MiniTransactionModel miniTransactionModel, String messageKey, String operation) throws FrameworkCheckedException;

    Long verifyOTP(MiniTransactionModel miniTransactionModel, String pin) throws FrameworkCheckedException;

    Long resendOTP(MiniTransactionModel miniTransactionModel, String messageKey, String operation) throws FrameworkCheckedException;

    MiniTransactionModel getModelWithDefaults(String encKey, Long commandId, String mobileNo, Long appUserId) throws FrameworkCheckedException;
    boolean updateIfExpired(MiniTransactionModel miniTransactionModel);
    SearchBaseWrapper getMiniTransactionModelByTransactionCode(SearchBaseWrapper searchBaseWrapper) throws FrameworkCheckedException;
    MiniTransactionModel getMiniTransactionModelWithDefaults(Long operatorId, Long commandId, String mobileNo, Long appUserId, Long actionLogId) throws FrameworkCheckedException;
	public abstract MiniTransactionModel populateModelWithDefaults(String encKey, int keyLength, MiniTransactionModel miniTransactionModel) throws FrameworkCheckedException;
	void validateOneTimePin(MiniTransactionModel miniTransactionModel, String oneTimePin, String encType) throws CommandException, FrameworkCheckedException;
	MiniTransactionModel findBYPrimaryKey(Long miniTransactionModelId);
    boolean updateIfExpired(MiniTransactionModel miniTransactionModel, Long productId);
}
