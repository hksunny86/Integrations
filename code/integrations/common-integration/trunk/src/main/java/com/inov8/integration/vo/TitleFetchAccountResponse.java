package com.inov8.integration.vo;

import java.io.Serializable;

public class TitleFetchAccountResponse implements Serializable {

    private static final long serialVersionUID = -7443862375180198891L;

    private String accountTitle;
    private String accountType;
    private String availableDebitLimitDaily;
    private String availableDebitLimitMonthly;
    private String availableDebitLimitYearly;
    private String availableCreditLimitDaily;
    private String availableCreditLimitMonthly;
    private String availableCreditLimitYearly;

    public String getAccountTitle() {
        return accountTitle;
    }

    public void setAccountTitle(String accountTitle) {
        this.accountTitle = accountTitle;
    }

    public String getAccountType() {
        return accountType;
    }

    public void setAccountType(String accountType) {
        this.accountType = accountType;
    }

    public String getAvailableDebitLimitDaily() {
        return availableDebitLimitDaily;
    }

    public void setAvailableDebitLimitDaily(String availableDebitLimitDaily) {
        this.availableDebitLimitDaily = availableDebitLimitDaily;
    }

    public String getAvailableDebitLimitMonthly() {
        return availableDebitLimitMonthly;
    }

    public void setAvailableDebitLimitMonthly(String availableDebitLimitMonthly) {
        this.availableDebitLimitMonthly = availableDebitLimitMonthly;
    }

    public String getAvailableDebitLimitYearly() {
        return availableDebitLimitYearly;
    }

    public void setAvailableDebitLimitYearly(String availableDebitLimitYearly) {
        this.availableDebitLimitYearly = availableDebitLimitYearly;
    }

    public String getAvailableCreditLimitDaily() {
        return availableCreditLimitDaily;
    }

    public void setAvailableCreditLimitDaily(String availableCreditLimitDaily) {
        this.availableCreditLimitDaily = availableCreditLimitDaily;
    }

    public String getAvailableCreditLimitMonthly() {
        return availableCreditLimitMonthly;
    }

    public void setAvailableCreditLimitMonthly(String availableCreditLimitMonthly) {
        this.availableCreditLimitMonthly = availableCreditLimitMonthly;
    }

    public String getAvailableCreditLimitYaarly() {
        return availableCreditLimitYearly;
    }

    public void setAvailableCreditLimitYearly(String availableCreditLimitYearly) {
        this.availableCreditLimitYearly = availableCreditLimitYearly;
    }
}
