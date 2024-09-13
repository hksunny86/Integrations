package com.example.dbscheduler.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.io.Serializable;

@Entity
@Table (name = "APP_USER")
@Data
public class AppUser implements Serializable {

    private static final long serialVersionUID = 11L;

    @Column(name = "APP_USER_ID" , nullable = false )
    @Id
//    @GeneratedValue(strategy=GenerationType.SEQUENCE, generator="APP_USER_seq")
    private Long id;

    @Column(name = "COOLING_OFF_APPLICABLE")
    private Boolean coolingOfApplicable;

    @Column(name= "EMAIL")
    private String email;

}
