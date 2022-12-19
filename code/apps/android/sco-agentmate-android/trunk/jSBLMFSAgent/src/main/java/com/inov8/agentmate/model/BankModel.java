package com.inov8.agentmate.model;

import java.util.ArrayList;

public class BankModel {
	public String id;
	public String nick;
	public String key;
	public String mod;
	public boolean isBank;
	public String min;
	public String max;

	public boolean pinLevel;
	public ArrayList<BankAccountModel> bankAccs = null;

	public BankModel(String id, String nick, String key, String mod, boolean isBank,
			boolean pinLevel) {
		this.id = id;
		this.nick = nick;
		this.key = key;
		this.mod = mod;
		this.isBank = isBank;
		this.pinLevel = pinLevel;
	}

	public BankModel(String id, String nick, String min, String max) {
		this.id = id;
		this.nick = nick;
		this.min = min;
		this.max = max;
	}

	public void addAccounts(BankAccountModel bankAcc) {

		if (this.bankAccs == null) {
			this.bankAccs = new ArrayList<BankAccountModel>();
		}
		bankAcc.setBank(this.nick);
		this.bankAccs.add(bankAcc);
	}

	public BankAccountModel getBankAccount(byte index) {
		return this.bankAccs.get(index);
	}

	public BankAccountModel getAccountById(String id) {
		BankAccountModel bankAcc = null;
		if (this.bankAccs != null && id != null && !id.equals(""))
			for (byte i = 0; i < this.bankAccs.size(); i++) {
				bankAcc = this.bankAccs.get(i);
				if (bankAcc.getId().equals(id.trim()))
					return bankAcc;
			}
		return null;
	}

	public void addBankAccs(ArrayList<BankAccountModel> bankAccList) {
		if (bankAccList != null) {
			if (this.bankAccs == null)
				this.bankAccs = new ArrayList<BankAccountModel>();

			byte size = (byte) bankAccList.size();
			for (byte indx = 0; indx < size; indx++) {
				this.bankAccs.add((BankAccountModel) bankAccList.get(indx));
			}
		}
	}

	public String getLabel() {
		return this.nick;
	}

	public String getId(){ return this.id;}


	public String getMin() {
		return min;
	}

	public void setMin(String min) {
		this.min = min;
	}

	public String getMax() {
		return max;
	}

	public void setMax(String max) {
		this.max = max;
	}
}
