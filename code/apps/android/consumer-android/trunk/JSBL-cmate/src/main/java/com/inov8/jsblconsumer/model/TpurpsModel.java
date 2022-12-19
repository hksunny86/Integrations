package com.inov8.jsblconsumer.model;

import java.io.Serializable;

/**
 * Created by DELL on 10/19/2016.
 */

public class TpurpsModel implements Serializable {

    public String id;
    public String name;

    public TpurpsModel(String id, String name) {
        this.id = id;
        this.name = name;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
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
