package com.example.dbscheduler.vo;

import lombok.Data;

import java.io.Serializable;

@Data
public class ResetPintTaskVo implements Serializable {
    private static final long serialversionUID = 10l;
    private Long appUserId;
}
