package com.inov8.microbank.webapp.action.portal.linkpaymentmodemodule;

import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import com.inov8.microbank.common.model.portal.linkpaymentmodemodule.LinkPaymentModeModel;

public class LinkPaymentModeValidator implements Validator {
	private String nickName;

	private String mfsId;

	private String accountNo;

	private Long accountType;

	private Long cardType;

	private String cardNo;

	private String expiryDate;

	public String getCardNo() {
		return cardNo;
	}

	public void setCardNo(String cardNo) {
		this.cardNo = cardNo;
	}

	public Long getCardType() {
		return cardType;
	}

	public void setCardType(Long cardType) {
		this.cardType = cardType;
	}

	public String getExpiryDate() {
		return expiryDate;
	}

	public void setExpiryDate(String expiryDate) {
		this.expiryDate = expiryDate;
	}

	public String getAccountNo() {
		return accountNo;
	}

	public void setAccountNo(String accountNo) {
		this.accountNo = accountNo;
	}

	public Long getAccountType() {
		return accountType;
	}

	public void setAccountType(Long accountType) {
		this.accountType = accountType;
	}

	public String getMfsId() {
		return mfsId;
	}

	public void setMfsId(String mfsId) {
		this.mfsId = mfsId;
	}

	public String getNickName() {
		return nickName;
	}

	public void setNickName(String nickName) {
		this.nickName = nickName;
	}

	public boolean supports(Class clazz) {
		// TODO Auto-generated method stub
		return clazz.equals(LinkPaymentModeModel.class);

	}

	public void validate(Object object, Errors errors) {
		// TODO Auto-generated method stub
		LinkPaymentModeModel linkPaymentModeModel = (LinkPaymentModeModel) object;

		if (linkPaymentModeModel != null) {
			if (linkPaymentModeModel.getName() != null
					&& linkPaymentModeModel.getName().equalsIgnoreCase("")) {
				errors.rejectValue("name", "linkPaymentModeModel.name", null,
						"Value required.");
			}
			if (linkPaymentModeModel.getMfsId() != null
					&& linkPaymentModeModel.getMfsId().equalsIgnoreCase("")) {
				errors.rejectValue("mfsId", "linkPaymentModeModel.mfsid",
						null, "Value required.");
			}
			if (linkPaymentModeModel.getPaymentMode() != null) {

				if (linkPaymentModeModel.getPaymentMode() == 3L) {
					if (linkPaymentModeModel.getAccountNo() != null
							&& linkPaymentModeModel.getAccountNo()
									.equalsIgnoreCase("")) {
						errors.rejectValue("accountNo",
								"linkPaymentModeModel.accountNo", null,
								"Value required.");

					}

				} else if (linkPaymentModeModel.getPaymentMode() == 1L) {
					if (linkPaymentModeModel.getExpiryDate() != null) {

					} else {
						errors.rejectValue("expiryDate",
								"linkPaymentModeModel.expiryDate", null,
								"Value required.");

					}

					if (linkPaymentModeModel.getCardType() != null) {

					} else {
						errors.rejectValue("cardType",
								"linkPaymentModeModel.cardType", null,
								"Value required.");

					}

					if (linkPaymentModeModel.getCardNo() != null
							&& linkPaymentModeModel.getCardNo()
									.equalsIgnoreCase("")) {
						errors.rejectValue("cardNo",
								"linkPaymentModeModel.cardNo", null,
								"Value required.");

					}

				} else if (linkPaymentModeModel.getPaymentMode() == 2) {
					if (linkPaymentModeModel.getCardType() != null) {

					} else {
						errors.rejectValue("cardType",
								"linkPaymentModeModel.cardType", null,
								"Value required.");

					}

					if (linkPaymentModeModel.getCardNo() != null
							&& linkPaymentModeModel.getCardNo()
									.equalsIgnoreCase("")) {
						errors.rejectValue("cardNo",
								"linkPaymentModeModel.cardNo", null,
								"Value required.");

					}

				}

			} else {

				errors.rejectValue("accountType",
						"linkPaymentModeModel.accountType", null,
						"Value required.");

			}

		}

	}

}
