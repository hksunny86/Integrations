package com.inov8.jsblconsumer.model;

import java.io.Serializable;

public class LocationModel implements Serializable {
    private static final long serialVersionUID = 1L;
    private String name;
    private String distance;
    private String contact;
    private String add;
    private String latitude;
    private String longitude;

    public LocationModel(String name, String distance, String contact,
                         String add, String latitude, String longitude) {
        super();
        this.name = name;
        this.distance = distance;
        this.contact = contact;
        this.add = add;
        this.latitude = latitude;
        this.longitude = longitude;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDistance() {
        return distance;
    }

    public void setDistance(String distance) {
        this.distance = distance;
    }

    public String getContact() {
        return contact;
    }

    public void setContact(String contact) {
        this.contact = contact;
    }

    public String getAdd() {
        return add;
    }

    public void setAdd(String add) {
        this.add = add;
    }

    public String getLatitude() {
        return latitude;
    }

    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }

    public String getLongitude() {
        return longitude;
    }

    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }
}
