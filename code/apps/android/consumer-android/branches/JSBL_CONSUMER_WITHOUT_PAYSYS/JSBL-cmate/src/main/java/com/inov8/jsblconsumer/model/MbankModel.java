package com.inov8.jsblconsumer.model;

import java.io.Serializable;

/**
 * Created by ZeeshanManzoor on 8/10/2016.
 */
public class MbankModel implements Serializable {

    public String id;
    public String imd;
    public String name;
    public String minlength;
    public String maxlength;

    public MbankModel(String id, String name, String imd, String minlength, String maxlength) {
        this.id = id;
        this.name = name;
        this.imd = imd;
        this.minlength = minlength;
        this.maxlength = maxlength;
    }

    public String getMaxLength() {
        return maxlength;
    }

    public String getMinLength() {
        return minlength;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getImd() {
        return imd;
    }

    public void setImd(String imd) {
        this.imd = imd;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return name;
    }
}
