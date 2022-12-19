package com.inov8.integration.loyalty;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

/**
 * Created by bkr on 7/22/2016.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class GiftCardVO {

    private String cardBalance;
    private String description;
	private String id;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getCardBalance() {
        return cardBalance;
    }

    public void setCardBalance(String cardBalance) {
        this.cardBalance = cardBalance;
    }

	/**
	 * @return the description
	 */
	public String getDescription() {
		return description;
	}

	/**
	 * @param description the description to set
	 */
	public void setDescription(String description) {
		this.description = description;
	}
}
