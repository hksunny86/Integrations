package com.inov8.microbank.common.util;

import java.io.Serializable;
import java.text.DecimalFormat;

public class TaxValueBean implements Serializable{

	private static final long serialVersionUID = 5963726650017423388L;
	
	private Double fedRate = 0.0D;
	private Double fedAmount = 0.0D;
	private Double whtRate = 0.0D;
	private Double whtAmount = 0.0D;
	private Boolean agent2WhtApplicable = false;
	
	public TaxValueBean(){
		
	}
	
	public TaxValueBean(Double fedRate, Double fedAmount, Double whtRate, Double whtAmount, Boolean agent2WhtApplicable){
		if(fedRate != null){
			this.fedRate = fedRate;
		}
		if(fedAmount != null){
			this.fedAmount = fedAmount;
		}
		if(whtRate != null){
			this.whtRate = whtRate;
		}
		if(whtAmount != null){
			this.whtAmount = whtAmount;
		}
		if(agent2WhtApplicable != null){
			this.agent2WhtApplicable = agent2WhtApplicable;
		}

	}
	
	public void roundTwoDecimals(){
		this.fedAmount  = roundTwoDecimals(fedAmount);
		this.whtAmount  = roundTwoDecimals(whtAmount);
	}
	
	private Double roundTwoDecimals(Double value) {
		Double roundedValue = new Double(0.0);
		if(value != null){
			DecimalFormat twoDForm = new DecimalFormat("#.##");
			roundedValue =  Double.valueOf(twoDForm.format(value));
		}
		return roundedValue;
	}
	
	public Double getFedRate() {
		return fedRate;
	}
	public void setFedRate(Double fedRate) {
		this.fedRate = fedRate;
	}
	public Double getFedAmount() {
		return fedAmount;
	}
	public void setFedAmount(Double fedAmount) {
		this.fedAmount = fedAmount;
	}
	public Double getWhtRate() {
		return whtRate;
	}
	public void setWhtRate(Double whtRate) {
		this.whtRate = whtRate;
	}
	public Double getWhtAmount() {
		return whtAmount;
	}
	public void setWhtAmount(Double whtAmount) {
		this.whtAmount = whtAmount;
	}
	public Boolean getAgent2WhtApplicable() {
		return agent2WhtApplicable;
	}
	public void setAgent2WhtApplicable(Boolean agent2WhtApplicable) {
		this.agent2WhtApplicable = agent2WhtApplicable;
	}
	
}
