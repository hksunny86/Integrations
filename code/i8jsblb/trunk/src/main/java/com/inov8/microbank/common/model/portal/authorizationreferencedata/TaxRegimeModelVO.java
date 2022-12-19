package com.inov8.microbank.common.model.portal.authorizationreferencedata;

import java.io.Serializable;
import java.util.Date;

import org.codehaus.jackson.map.annotate.JsonSerialize;

import com.inov8.framework.common.model.BasePersistableModel;
import com.inov8.microbank.common.model.TaxRegimeModel;

@JsonSerialize(include = JsonSerialize.Inclusion.NON_NULL)
public class TaxRegimeModelVO extends BasePersistableModel implements Serializable{

	private static final long serialVersionUID = 56086637763821028L;
	
	private Long				taxRegimeId;
	private String				name;
	private String				description;
	private Double				fed;
	private Long				createdByAppUserModelId;
	private Long				updatedByAppUserModelId;
	private Date				createdOn;
	private Date				updatedOn;
	private Integer				versionNo;
	private Boolean				isActive;
	

	public TaxRegimeModelVO() {
		
	}
	
	public TaxRegimeModelVO(TaxRegimeModel taxRegimeModel) {
		
		this.taxRegimeId = taxRegimeModel.getTaxRegimeId();
		this.name = taxRegimeModel.getName();
		this.description = taxRegimeModel.getDescription();
		this.fed = taxRegimeModel.getFed();
		this.createdByAppUserModelId = taxRegimeModel.getCreatedBy();
		this.updatedByAppUserModelId = taxRegimeModel.getUpdatedBy();
		this.createdOn = taxRegimeModel.getCreatedOn();
		this.updatedOn = taxRegimeModel.getUpdatedOn();
		this.versionNo = taxRegimeModel.getVersionNo();
		this.isActive = taxRegimeModel.getIsActive();
	}
	
	public Long getTaxRegimeId() {
		return taxRegimeId;
	}
	public void setTaxRegimeId(Long taxRegimeId) {
		this.taxRegimeId = taxRegimeId;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public Double getFed() {
		return fed;
	}
	public void setFed(Double fed) {
		this.fed = fed;
	}
	public Long getCreatedByAppUserModelId() {
		return createdByAppUserModelId;
	}
	public void setCreatedByAppUserModelId(Long createdByAppUserModelId) {
		this.createdByAppUserModelId = createdByAppUserModelId;
	}
	public Long getUpdatedByAppUserModelId() {
		return updatedByAppUserModelId;
	}
	public void setUpdatedByAppUserModelId(Long updatedByAppUserModelId) {
		this.updatedByAppUserModelId = updatedByAppUserModelId;
	}
	public Date getCreatedOn() {
		return createdOn;
	}
	public void setCreatedOn(Date createdOn) {
		this.createdOn = createdOn;
	}
	public Date getUpdatedOn() {
		return updatedOn;
	}
	public void setUpdatedOn(Date updatedOn) {
		this.updatedOn = updatedOn;
	}
	public Integer getVersionNo() {
		return versionNo;
	}
	public void setVersionNo(Integer versionNo) {
		this.versionNo = versionNo;
	}
	public Boolean getIsActive() {
		return isActive;
	}
	public void setIsActive(Boolean isActive) {
		this.isActive = isActive;
	}

	@Override
	public Long getPrimaryKey() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getPrimaryKeyFieldName() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getPrimaryKeyParameter() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void setPrimaryKey(Long arg0) {
		// TODO Auto-generated method stub
		
	}

}
