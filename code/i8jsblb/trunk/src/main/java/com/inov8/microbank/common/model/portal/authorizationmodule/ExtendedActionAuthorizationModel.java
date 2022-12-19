package com.inov8.microbank.common.model.portal.authorizationmodule;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.Date;

@XmlRootElement(name="actionAuthorizationModel")
@XmlAccessorType(XmlAccessType.NONE)
public class ExtendedActionAuthorizationModel extends ActionAuthorizationModel {

	private static final long serialVersionUID = -6198817657898086948L;
	@XmlElement
	private Date createdOnFromDate;
	@XmlElement
    private Date createdOnToDate;
	@XmlElement
    private Date checkededOnFromDate;
	@XmlElement
    private Date checkedOnToDate;

	public ExtendedActionAuthorizationModel() {
	}

	public Date getCreatedOnFromDate() {
		return createdOnFromDate;
	}

	public void setCreatedOnFromDate(Date createdOnFromDate) {
		this.createdOnFromDate = createdOnFromDate;
	}

	public Date getCreatedOnToDate() {
		return createdOnToDate;
	}

	public void setCreatedOnToDate(Date createdOnToDate) {
		this.createdOnToDate = createdOnToDate;
	}

	public Date getCheckededOnFromDate() {
		return checkededOnFromDate;
	}

	public void setCheckededOnFromDate(Date checkededOnFromDate) {
		this.checkededOnFromDate = checkededOnFromDate;
	}

	public Date getCheckedOnToDate() {
		return checkedOnToDate;
	}

	public void setCheckedOnToDate(Date checkedOnToDate) {
		this.checkedOnToDate = checkedOnToDate;
	}

}
