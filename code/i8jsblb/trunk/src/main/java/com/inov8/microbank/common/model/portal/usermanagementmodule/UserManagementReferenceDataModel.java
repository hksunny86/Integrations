package com.inov8.microbank.common.model.portal.usermanagementmodule;

import java.io.Serializable;

public class UserManagementReferenceDataModel implements Serializable {
    /**
	 * 
	 */
	private static final long serialVersionUID = -6245821797865830406L;
	private String nameField;
	private Long idField;
	private String dataType;
	public String getDataType() {
		return dataType;
	}
	public void setDataType(String dataType) {
		this.dataType = dataType;
	}
	public Long getIdField() {
		return idField;
	}
	public void setIdField(Long idField) {
		this.idField = idField;
	}
	public String getNameField() {
		return nameField;
	}
	public void setNameField(String nameField) {
		this.nameField = nameField;
	}
	
}
