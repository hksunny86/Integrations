package com.inov8.microbank.common.model.messagemodule;

import java.io.Serializable;

public class NovaAlertMessage implements Serializable {
    private static final long serialVersionUID = -9092045361360147857L;
    private String mobileNo;
    private String text;
    private String reserved1;
    private String reserved2;
    private String reserved3;
    private String reserved4;


    public NovaAlertMessage(String mobileNo, String text, String reserved1, String reserved2, String reserved3, String reserved4) {
        this.mobileNo = mobileNo;
        this.text = text;
        this.reserved1 = reserved1;
        this.reserved2 = reserved2;
        this.reserved3 = reserved3;
        this.reserved4 = reserved4;
    }

	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}

	public String getReserved1() {
        return reserved1;
    }

    public void setReserved1(String reserved1) {
        this.reserved1 = reserved1;
    }

    public String getReserved2() {
        return reserved2;
    }

    public void setReserved2(String reserved2) {
        this.reserved2 = reserved2;
    }

    public String getReserved3() {
        return reserved3;
    }

    public void setReserved3(String reserved3) {
        this.reserved3 = reserved3;
    }

    public String getReserved4() {
        return reserved4;
    }

    public void setReserved4(String reserved4) {
        this.reserved4 = reserved4;
    }

    public String getMobileNo() {
        return mobileNo;
    }

    public void setMobileNo(String mobileNo) {
        this.mobileNo = mobileNo;
    }

    @Override
    public String toString() {
        return new StringBuilder().append(mobileNo).toString();
    }

}
