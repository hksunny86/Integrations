package com.example.dbscheduler.vo;


import lombok.Data;

import java.io.Serializable;

@Data
public class EmailUpdateTaskVo implements Serializable {
    private static final long serialversionUID = 1L;

    private Long appUserId;
    private String updatedEmail;
}
