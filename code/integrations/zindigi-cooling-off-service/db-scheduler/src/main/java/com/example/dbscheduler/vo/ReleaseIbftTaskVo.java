package com.example.dbscheduler.vo;


import lombok.Data;

import java.io.Serializable;

@Data
public class ReleaseIbftTaskVo implements Serializable {
    private final static long serialVersionUID = 1L;

    private String userName;
    private String password;
    private String mobileNumber;
    private String transactionId;
    private String referenceNo;
    private String dateTime;
    private String rrn;
    private String channelId;
    private String terminalId;
    private String reserved1;
    private String reserved2;
    private String reserved3;
    private String reserved4;
    private String reserved5;
    private String reserved6;
    private String reserved7;
    private String reserved8;
    private String reserved9;
    private String reserved10;
    private String hashData;

}
