package com.inov8.integration.i8sb.vo;

import com.thoughtworks.xstream.annotations.XStreamAlias;

import java.io.Serializable;
@XStreamAlias("StopCheque")
public class StopCheque implements Serializable {

    private final static long serialVersionUID = 1L;

    private String chequeNoStart;
    private String chequeNoEnd;

    public String getChequeNoStart() {
        return chequeNoStart;
    }

    public void setChequeNoStart(String chequeNoStart) {
        this.chequeNoStart = chequeNoStart;
    }

    public String getChequeNoEnd() {
        return chequeNoEnd;
    }

    public void setChequeNoEnd(String chequeNoEnd) {
        this.chequeNoEnd = chequeNoEnd;
    }
}
