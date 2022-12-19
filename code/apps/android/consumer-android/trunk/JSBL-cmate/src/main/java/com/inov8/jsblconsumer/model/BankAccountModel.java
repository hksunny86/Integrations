package com.inov8.jsblconsumer.model;

public class BankAccountModel {

	private String id;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String number;
	public String type;
	public String currency;
	public String nick;
	public boolean status;
	public boolean needsUpdation = false;
	public boolean isDefault = false;
	public boolean cvv;
	public boolean tPin;
	public boolean mPin;
	public boolean isBPinReq;

	public String bankNick;

	public BankAccountModel(String id, String number, String type, String currency,
			String nick, boolean status, boolean isDefault,
			boolean needsUpdation, boolean cvv, boolean tPin, boolean mPin,
			boolean isBPinReq) {
		this.id = id;
		this.number = number;
		this.type = type;
		this.currency = currency;
		this.nick = nick;
		this.status = status;
		this.needsUpdation = needsUpdation;
		this.isDefault = isDefault;
		this.cvv = cvv;
		this.tPin = tPin;
		this.mPin = mPin;
		this.isBPinReq = isBPinReq;
	}

	public void setBank(String bankNick) {
		this.bankNick = bankNick;
	}

	@Override
	public boolean equals(Object obj) {
		if (!(obj instanceof BankAccountModel))
			return false;
		BankAccountModel b = (BankAccountModel) obj;
		if (b.id.equals(this.id))
			return true;
		else
			return false;
	}

	public String getLabel() {
		String label = null;
		if (this.number != null)
			label = this.number;
		else if (this.nick != null)
			label = this.nick;
		return label;
	}
}
