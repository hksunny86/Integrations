package com.inov8.microbank.common.vo.agenthierarchy;

import com.inov8.microbank.common.model.agenthierarchy.RegionModel;

public class RegionVO extends RegionModel 
{

	boolean deleted;
	boolean selected;
	boolean edited;
	
	public RegionVO() {
		super();
		// TODO Auto-generated constructor stub
	}

	public boolean isDeleted() {
		return deleted;
	}

	public void setDeleted(boolean deleted) {
		this.deleted = deleted;
	}

	public boolean isSelected() {
		return selected;
	}

	public void setSelected(boolean selected) {
		this.selected = selected;
	}

	public boolean isEdited() {
		return edited;
	}

	public void setEdited(boolean edited) {
		this.edited = edited;
	}	
	
	
}
