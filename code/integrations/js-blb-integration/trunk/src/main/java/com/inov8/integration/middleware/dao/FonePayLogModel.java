package com.inov8.integration.middleware.dao;


import javax.persistence.*;
import java.io.Serializable;

@Entity
@javax.persistence.SequenceGenerator(name = "FONEPAY_INTEGERATION_LOG_SEQ",sequenceName = "FONEPAY_INTEGERATION_LOG_SEQ", allocationSize=1)
@Table(name = "FONEPAY_INTEGERATION_LOG")
//@JsonInclude(JsonInclude.Include.NON_NULL)
public class FonePayLogModel  implements Serializable{


    /**
     *
     */
    private static final long serialVersionUID = 5684111190933720743L;
    private Long fonePayIntegrationLogId;
    private String requestType;
    private String rrn;
    private String mobile_no;
    private String cnic;
    private String response_code;
    private String response_description;
    private String transactionId;
    private java.sql.Timestamp created_on;
    private java.sql.Timestamp updated_on;
    private String input;
    private String output;
    private String stan;

    //transient properties
    // private Date fromDate;
    // private Date toDate;



    @Column(name = "FONPAY_INTEGERATION_LOG_ID" , nullable = false )
    @Id
    @GeneratedValue(strategy=GenerationType.SEQUENCE, generator="FONEPAY_INTEGERATION_LOG_SEQ")
    public Long getFonePayIntegrationLogId() {
        return fonePayIntegrationLogId;
    }
    public void setFonePayIntegrationLogId(Long fonePayIntegrationLogId) {
        this.fonePayIntegrationLogId = fonePayIntegrationLogId;
    }

    @Column(name = "REQUEST_TYPE"  )
    public String getRequestType() {
        return requestType;
    }
    public void setRequestType(String requestType) {
        this.requestType = requestType;
    }

    @Column(name = "RRN"  )
    public String getRrn() {
        return rrn;
    }
    public void setRrn(String rrn) {
        this.rrn = rrn;
    }

    @Column(name = "MOBILE_NO"  )
    public String getMobile_no() {
        return mobile_no;
    }
    public void setMobile_no(String mobile_no) {
        this.mobile_no = mobile_no;
    }

    @Column(name = "CNIC"  )
    public String getCnic() {
        return cnic;
    }
    public void setCnic(String cnic) {
        this.cnic = cnic;
    }

    @Column(name = "RESPONSE_CODE"  )
    public String getResponse_code() {
        return response_code;
    }
    public void setResponse_code(String response_code) {
        this.response_code = response_code;
    }

    @Column(name = "RESPONSE_DESCRIPTION"  )
    public String getResponse_description() {
        return response_description;
    }
    public void setResponse_description(String response_description) {
        this.response_description = response_description;
    }

    @Column(name = "TRANSACTION_ID"  )
    public String getTransactionId() {
        return transactionId;
    }
    public void setTransactionId(String transactionId) {
        this.transactionId = transactionId;
    }

    @Column(name = "CREATED_ON"  )
    public java.sql.Timestamp getCreated_on() {
        return created_on;
    }
    public void setCreated_on(java.sql.Timestamp created_on) {
        this.created_on = created_on;
    }

    @Column(name = "UPDATED_ON"  )
    public java.sql.Timestamp getUpdated_on() {
        return updated_on;
    }
    public void setUpdated_on(java.sql.Timestamp updated_on) {
        this.updated_on = updated_on;
    }

    @Column(name = "INPUT"  )
    public String getInput() {
        return input;
    }
    public void setInput(String input) {
        this.input = input;
    }

    @Column(name = "OUTPUT"  )
    public String getOutput() {
        return output;
    }
    public void setOutput(String output) {
        this.output = output;
    }

    @Column(name = "STAN"  )
    public String getStan() {
        return stan;
    }

    public void setStan(String stan) {
        this.stan = stan;
    }

//    @javax.persistence.Transient
//    public Long getPrimaryKey() {
//        return getFonePayIntegrationLogId();
//    }
//
//    @JsonSerialize(include=JsonSerialize.Inclusion.NON_NULL)
//    @javax.persistence.Transient
//    public String getPrimaryKeyFieldName() {
//        String primaryKeyFieldName = "fonePayIntegrationLogId";
//        return primaryKeyFieldName;
//    }
//
//    @JsonSerialize(include=JsonSerialize.Inclusion.NON_NULL)
//    @javax.persistence.Transient
//    public String getPrimaryKeyParameter() {
//        String parameters = "";
//        parameters += "&fonePayIntegrationLogId=" + getFonePayIntegrationLogId();
//        return parameters;
//    }
//
//    public void setPrimaryKey(Long arg0) {
//        setFonePayIntegrationLogId(arg0);
//    }

	/*@javax.persistence.Transient
	public Date getFromDate() {
		return fromDate;
	}
	public void setFromDate(Date fromDate) {
		this.fromDate = fromDate;
	}

	@javax.persistence.Transient
	public Date getToDate() {
		return toDate;
	}
	public void setToDate(Date toDate) {
		this.toDate = toDate;
	}*/

}