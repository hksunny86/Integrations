package com.inov8.jsblconsumer.model;

import java.io.Serializable;

public class AdModel implements Serializable {

    /**
     *
     */
    private static final long serialVersionUID = 1L;
    private int id;
    private String name;
    private String imageUrl;
    private String type;


    public AdModel(String name, String imageUrl) {
        super();
        this.name = name;
        this.imageUrl = imageUrl;
    }

    public AdModel() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }


}
