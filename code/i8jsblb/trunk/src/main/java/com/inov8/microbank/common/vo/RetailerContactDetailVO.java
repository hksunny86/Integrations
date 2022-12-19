package com.inov8.microbank.common.vo;

public class RetailerContactDetailVO {

    private Double latitude;
	private Double longitude;
    private Double distanceDisplacement;
    private String firstName;
    private String lastName;
    private String contactNo;
    private String address;
    
    
    public Double getLatitude() {
		return latitude;
	}
	public void setLatitude(Double latitude) {
		this.latitude = latitude;
	}
	public Double getLongitude() {
		return longitude;
	}
	public void setLongitude(Double longitude) {
		this.longitude = longitude;
	}
	public Double getDistanceDisplacement() {
		return distanceDisplacement;
	}
	public void setDistanceDisplacement(Double distanceDisplacement) {
		this.distanceDisplacement = distanceDisplacement;
	}
	public String getFirstName() {
		return firstName;
	}
	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}
	public String getLastName() {
		return lastName;
	}
	public void setLastName(String lastName) {
		this.lastName = lastName;
	}
	public String getContactNo() {
		return contactNo;
	}
	public void setContactNo(String contactNo) {
		this.contactNo = contactNo;
	}
	public String getAddress() {
		return address;
	}
	public void setAddress(String address) {
		this.address = address;
	}

}
