package com.inov8.integration.loyalty;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.io.Serializable;
import java.util.List;

/**
 * Created by Umar on 6/28/2016.
 */

@JsonIgnoreProperties(ignoreUnknown = true)
public class Merchants implements Serializable{

    private String id;
    private String brandId;
    private String name;
    private String distance;
    private String contact;
    private String address;
    private String latitude;
    private String longitude;
    private String discount;
    private String category;
    private String checkIns;
    private String reviews;
    private String logo;
    private List<Ad> ads;
    private List<GiftCardVO> giftCard;
    private String merchantMfsId;


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

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getLatitude() {
        return latitude;
    }

    public void setLatitude(String lattitude) {
        this.latitude = lattitude;
    }

    public String getLongitude() {
        return longitude;
    }

    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }

    public String getDiscount() {
        return discount;
    }

    public void setDiscount(String discount) {
        this.discount = discount;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getCheckIns() {
        return checkIns;
    }

    public void setCheckIns(String checkIns) {
        this.checkIns = checkIns;
    }

    public String getLogo() {
        return logo;
    }

    public void setLogo(String logo) {
        this.logo = logo;
    }

    public List<Ad> getAds() {
        return ads;
    }

    public void setAds(List<Ad> ads) {
        this.ads = ads;
    }

    public List<GiftCardVO> getGiftCard() {
        return giftCard;
    }

    public void setGiftCard(List<GiftCardVO> giftCard) {
        this.giftCard = giftCard;
    }

    @Override
    public String toString() {
        return "Merchants{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", distance='" + distance + '\'' +
                ", contact='" + contact + '\'' +
                ", address='" + address + '\'' +
                ", lattitude='" + latitude + '\'' +
                ", longitude='" + longitude + '\'' +
                ", discount='" + discount + '\'' +
                ", category='" + category + '\'' +
                ", checkIns='" + checkIns + '\'' +
                ", reviews='" + reviews + '\'' +
                ", logo='" + logo + '\'' +
                ", ads=" + ads +
                ", giftCard=" + giftCard +
                '}';
    }

	/**
	 * @return the reviews
	 */
	public String getReviews() {
		return reviews;
	}

	/**
	 * @param reviews the reviews to set
	 */
	public void setReviews(String reviews) {
		this.reviews = reviews;
	}

	/**
	 * @return the brandId
	 */
	public String getBrandId() {
		return brandId;
	}

	/**
	 * @param brandId the brandId to set
	 */
	public void setBrandId(String brandId) {
		this.brandId = brandId;
	}

	/**
	 * @return the merchantMfsId
	 */
	public String getMerchantMfsId() {
		return merchantMfsId;
	}

	/**
	 * @param merchantMfsId the merchantMfsId to set
	 */
	public void setMerchantMfsId(String merchantMfsId) {
		this.merchantMfsId = merchantMfsId;
	}

    
}
