package com.inov8.jsblconsumer.model;

import java.io.Serializable;

public class ProductModel implements Serializable {
	private static final long serialVersionUID = 1L;
	private String id;
	private String flowId;
	private String name;
	private String label;
	private String minamt;
	private String minamtf;
	private String maxamt;
	private String maxamtf;
	private String amtRequired;
	private String doValidate;
	private String type;
	private String multiple;
	private String minConsumerLength;
	private String inRequired;
	private String ppRequired;
	private String prodDenom;
	private String denomFlag;
	private String denomString;
	private String URL;

	public String getMaxConsumerLength() {
		return maxConsumerLength;
	}

	public void setMaxConsumerLength(String maxConsumerLength) {
		this.maxConsumerLength = maxConsumerLength;
	}

	public String getMinConsumerLength() {
		return minConsumerLength;
	}

	public void setMinConsumerLength(String minConsumerLength) {
		this.minConsumerLength = minConsumerLength;
	}

	private String maxConsumerLength;

	public ProductModel(String pid, String flowId, String name) {
		super();
		this.id = pid;
		this.flowId = flowId;
		this.name = name;
	}

	public String getId() {
		return id;
	}

	public void setId(String pid) {
		this.id = pid;
	}

	public String getFlowId() {
		return flowId;
	}

	public void setFlowId(String flowId) {
		this.flowId = flowId;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getLabel() {
		return label;
	}

	public void setLabel(String label) {
		this.label = label;
	}

	public String getMinamt() {
		return minamt;
	}

	public void setMinamt(String minamt) {
		this.minamt = minamt;
	}

	public String getMinamtf() {
		return minamtf;
	}

	public void setMinamtf(String minamtf) {
		this.minamtf = minamtf;
	}

	public String getMaxamt() {
		return maxamt;
	}

	public void setMaxamt(String maxamt) {
		this.maxamt = maxamt;
	}

	public String getMaxamtf() {
		return maxamtf;
	}

	public void setMaxamtf(String maxamtf) {
		this.maxamtf = maxamtf;
	}

	public String getAmtRequired() {
		return amtRequired;
	}

	public void setAmtRequired(String amtRequired) {
		this.amtRequired = amtRequired;
	}

	public String getDoValidate() {
		return doValidate;
	}

	public void setDoValidate(String doValidate) {
		this.doValidate = doValidate;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getMultiple() {
		return multiple;
	}

	public void setMultiple(String multiple) {
		this.multiple = multiple;
	}

	public String getInRequired() {
		return inRequired;
	}

	public void setInRequired(String inRequired) {
		this.inRequired = inRequired;
	}

	public String getPpRequired() {
		return ppRequired;
	}

	public void setPpRequired(String ppRequired) {
		this.ppRequired = ppRequired;
	}

	public String getProdDenom() {
		return prodDenom;
	}

	public void setProdDenom(String prodDenom) {
		this.prodDenom = prodDenom;
	}

	public String getDenomFlag() {
		return denomFlag;
	}

	public void setDenomFlag(String denomFlag) {
		this.denomFlag = denomFlag;
	}

	public String getDenomString() {
		return denomString;
	}

	public void setDenomString(String denomString) {
		this.denomString = denomString;
	}

	public String getURL() {
		return URL;
	}

	public void setURL(String URL) {
		this.URL = URL;
	}
}