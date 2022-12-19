package com.inov8.agentmate.model;

public class ServiceModel {

	public String id;
	public String name;
	public String gfId;
	public String label;
	public String supName;
	private int type;

	public ServiceModel(String id, String gfId, String label, String supName,
			String name, int pType ) {
		this.id = id;
		this.name = name;
		this.gfId = gfId;
		this.label = label;
		this.supName = supName;
		this.setType(pType);
	}

	public boolean equals(Object obj) {
		if (!(obj instanceof ServiceModel))
			return false;
		ServiceModel s = (ServiceModel) obj;
		if (s.id.equals(this.id))
			return true;
		else
			return false;
	}

	public String getLabel() {
		return this.name;
	}

	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}
}