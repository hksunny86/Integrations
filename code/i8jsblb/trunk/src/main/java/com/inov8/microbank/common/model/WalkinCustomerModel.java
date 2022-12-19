package com.inov8.microbank.common.model;

import com.inov8.framework.common.model.BasePersistableModel;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

/**
 * The WalkinCustomer entity bean.
 *
 * @spring.bean name="WalkinCustomer"
 */

@Entity
@org.hibernate.annotations.Entity(dynamicInsert = true)
@org.hibernate.annotations.GenericGenerator(name="WALK_IN_CUSTOMER_SEQ", strategy = "sequence", parameters = { @org.hibernate.annotations.Parameter(name="sequence", value="WALK_IN_CUSTOMER_SEQ") } )
//@javax.persistence.SequenceGenerator(name = "WALK_IN_CUSTOMER_SEQ", sequenceName = "WALK_IN_CUSTOMER_SEQ")
@Table(name = "WALK_IN_CUSTOMER")
public class WalkinCustomerModel extends BasePersistableModel implements Serializable {

	private static final long serialVersionUID = 1L;

	private Long walkinCustomerId;
	private String mobileNumber;
	private String cnic;
	private String description;
	private Date createdOn;
	private Date updatedOn;	
	private AppUserModel updatedByAppUserModel;
	private AppUserModel createdByAppUserModel;	
	private Integer versionNo;

	public WalkinCustomerModel() {}

	public WalkinCustomerModel(Long walkinCustomerId) {
		
		this.setPrimaryKey(walkinCustomerId);
	}
	public WalkinCustomerModel(String cnic) {
		this.cnic = cnic;
	}
	public WalkinCustomerModel(String cnic, String mobileNumber) {
		this.cnic = cnic;
		this.mobileNumber = mobileNumber;
	}
	

    @Id 
    @Column(name = "WALK_IN_CUSTOMER_ID" , nullable = false)
    @GeneratedValue(strategy=GenerationType.SEQUENCE, generator="WALK_IN_CUSTOMER_SEQ")
	public Long getWalkinCustomerId() {
		return this.walkinCustomerId;
	}

	public void setWalkinCustomerId(Long walkinCustomerId) {
		this.walkinCustomerId = walkinCustomerId;
	}

	@Column(name = "MOBILE_NO" )
	public String getMobileNumber() {
		return mobileNumber;
	}

	public void setMobileNumber(String mobileNumber) {
		this.mobileNumber = mobileNumber;
	}
	
	@Column(name = "CNIC" )
	public String getCnic() {
		return cnic;
	}

	public void setCnic(String cnic) {
		this.cnic = cnic;
	}
	
	@Column(name = "DESCRIPTION" )
	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}
	
	@Column(name = "CREATED_ON" , nullable = false )
	public Date getCreatedOn() {
		return createdOn;
	}

	public void setCreatedOn(Date createdOn) {
		this.createdOn = createdOn;
	}

    @Column(name = "UPDATED_ON" , nullable = false )
	public Date getUpdatedOn() {
		return updatedOn;
	}

	public void setUpdatedOn(Date updatedOn) {
		this.updatedOn = updatedOn;
	}
	
	@ManyToOne(cascade = CascadeType.REFRESH, fetch = FetchType.LAZY)
	@JoinColumn(name = "UPDATED_BY") 
	public AppUserModel getUpdatedByAppUserModel() {
		return updatedByAppUserModel;
	}

	public void setUpdatedByAppUserModel(AppUserModel updatedByAppUserModel) {
		this.updatedByAppUserModel = updatedByAppUserModel;
	}
	@ManyToOne(cascade = CascadeType.REFRESH, fetch = FetchType.LAZY)
	@JoinColumn(name = "CREATED_BY") 
	public AppUserModel getCreatedByAppUserModel() {
		return createdByAppUserModel;
	}

	public void setCreatedByAppUserModel(AppUserModel createdByAppUserModel) {
		this.createdByAppUserModel = createdByAppUserModel;
	}
	
    @Version 
    @Column(name = "VERSION_NO")
	public Integer getVersionNo() {
		return versionNo;
	}

	public void setVersionNo(Integer versionNo) {
		this.versionNo = versionNo;
	}
	
	
	@javax.persistence.Transient
	public Long getPrimaryKey() {
		return getWalkinCustomerId();
	}

	@javax.persistence.Transient
	public void setPrimaryKey(Long primaryKey) {
		setWalkinCustomerId(primaryKey);
	}

	@javax.persistence.Transient
	public String getPrimaryKeyParameter() {
		return "&walkinCustomerId=" + getWalkinCustomerId();
	}

	@javax.persistence.Transient
	public String getPrimaryKeyFieldName() { 
		return "walkinCustomerId";				
	}	
}