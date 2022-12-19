package com.inov8.integration.vo.daewoo.json;


import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class Booking {

    @JsonProperty("BOOKCODE")
    private String bookCode;
    @JsonProperty("MSG")
    private String msg;
    @JsonProperty("DUPLICATE")
    private String duplicate;
    @JsonProperty("SMS")
    private String sms;
    @JsonProperty("BOOKNO")
    private String bookNumber;
    @JsonProperty("ROUTECHECK")
    private String rounteCheck;
    @JsonProperty("API_OUT")
    private String apiOut;

    public String getBookCode() {
        return bookCode;
    }

    public void setBookCode(String bookCode) {
        this.bookCode = bookCode;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public String getDuplicate() {
        return duplicate;
    }

    public void setDuplicate(String duplicate) {
        this.duplicate = duplicate;
    }

    public String getSms() {
        return sms;
    }

    public void setSms(String sms) {
        this.sms = sms;
    }

    public String getBookNumber() {
        return bookNumber;
    }

    public void setBookNumber(String bookNumber) {
        this.bookNumber = bookNumber;
    }

    public String getRounteCheck() {
        return rounteCheck;
    }

    public void setRounteCheck(String rounteCheck) {
        this.rounteCheck = rounteCheck;
    }

    public String getApiOut() {
        return apiOut;
    }

    public void setApiOut(String apiOut) {
        this.apiOut = apiOut;
    }
}
