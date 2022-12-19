package com.inov8.microbank.hra.airtimetopup.vo;

import com.inov8.framework.common.model.BasePersistableModel;

public class AirTimeTopUpVO extends BasePersistableModel {

    private Double dollarRate;
    private Double topUpRate;

    public Double getTopUpRate() {
        return topUpRate;
    }

    public void setTopUpRate(Double topUpRate) {
        this.topUpRate = topUpRate;
    }

    @Override
    public void setPrimaryKey(Long aLong) {

    }

    @Override
    public Long getPrimaryKey() {
        return null;
    }

    @Override
    public String getPrimaryKeyParameter() {
        return null;
    }

    @Override
    public String getPrimaryKeyFieldName() {
        return null;
    }

    public Double getDollarRate() {
        return dollarRate;
    }

    public void setDollarRate(Double dollarRate) {
        this.dollarRate = dollarRate;
    }
}
