package com.example.dbscheduler.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.util.Date;

@Entity
@Table(name = "SYSTEM_CONFIG")
@Data
public class SystemConfig {

    @Column(name = "CONFIG_KEY")
    @Id
    private String key;
    @Column(name = "CONFIG_VALUE")
    private String value;
    @Column(name = "description")
    private String description;
    @Column(name = "LAST_UPDATED" , nullable = false )
    private Date updatedOn;

}
