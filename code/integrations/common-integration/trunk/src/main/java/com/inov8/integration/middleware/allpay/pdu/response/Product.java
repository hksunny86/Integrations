package com.inov8.integration.middleware.allpay.pdu.response;

import java.io.Serializable;
import java.util.List;

public class Product implements Serializable {

	private static final long serialVersionUID = -956394369554841557L;

	private String id;
	private String name;
	private String supplierName;
	private String description;
	private String serviceName;
	private String minLimit;
	private String maxLimit;
	private String charges;
	private String status;
	
	private List<Denomination> denominations;

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

	public String getSupplierName() {
		return supplierName;
	}

	public void setSupplierName(String supplierName) {
		this.supplierName = supplierName;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getServiceName() {
		return serviceName;
	}

	public void setServiceName(String serviceName) {
		this.serviceName = serviceName;
	}

	public String getMinLimit() {
		return minLimit;
	}

	public void setMinLimit(String minLimit) {
		this.minLimit = minLimit;
	}

	public String getMaxLimit() {
		return maxLimit;
	}

	public void setMaxLimit(String maxLimit) {
		this.maxLimit = maxLimit;
	}

	public String getCharges() {
		return charges;
	}

	public void setCharges(String charges) {
		this.charges = charges;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public List<Denomination> getDenominations() {
		return denominations;
	}

	public void setDenominations(List<Denomination> denominations) {
		this.denominations = denominations;
	}

}
