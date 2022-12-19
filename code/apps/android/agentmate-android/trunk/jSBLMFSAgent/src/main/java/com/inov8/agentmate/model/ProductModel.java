package com.inov8.agentmate.model;

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
    private String consumerMinLength;
    private String consumerMaxLength;
    private String ppAllowed;

    public String getMinConsumerLength() {
        return consumerMinLength;
    }

    public void setConsumerMinLength(String consumerMinLength) {
        this.consumerMinLength = consumerMinLength;
    }

    public String getMaxConsumerLength() {
        return consumerMaxLength;
    }

    public void setConsumerMaxLength(String consumerMaxLength) {
        this.consumerMaxLength = consumerMaxLength;
    }

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

	public String getPpAllowed() {
		return ppAllowed;
	}

	public void setPpAllowed(String ppAllowed) {
		this.ppAllowed = ppAllowed;
	}
}