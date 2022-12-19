package com.inov8.microbank.common.vo.agenthierarchy;

import java.util.ArrayList;
import java.util.List;

import javax.faces.model.SelectItem;

import com.inov8.microbank.common.model.DistributorModel;

public class DistributorVO extends DistributorModel {
	
	private boolean editMode;
	private List<SelectItem>  bankList= new ArrayList<SelectItem>();
	private List<SelectItem>  mnoList= new ArrayList<SelectItem>();
	private Long mnoId;
	private Long bankId;
	
	public Long getMnoId() {
		return mnoId;
	}

	public void setMnoId(Long mnoId) {
		this.mnoId = mnoId;
	}

	public Long getBankId() {
		return bankId;
	}

	public void setBankId(Long bankId) {
		this.bankId = bankId;
	}

	public List<SelectItem> getBankList() {
		return bankList;
	}

	public void setBankList(List<SelectItem> bankList) {
		this.bankList = bankList;
	}

	public List<SelectItem> getMnoList() {
		return mnoList;
	}

	public void setMnoList(List<SelectItem> mnoList) {
		this.mnoList = mnoList;
	}
	
	public boolean isEditMode() {
		return editMode;
	}

	public void setEditMode(boolean editMode) {
		this.editMode = editMode;
	}

	public DistributorVO() 
	{
		super();
		// TODO Auto-generated constructor stub
	}
}
