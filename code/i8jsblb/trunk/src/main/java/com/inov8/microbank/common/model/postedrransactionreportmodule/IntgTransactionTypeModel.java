package com.inov8.microbank.common.model.postedrransactionreportmodule;

import java.sql.Timestamp;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import com.inov8.framework.common.model.BasePersistableModel;
import com.inov8.microbank.common.model.AppUserModel;
import com.inov8.microbank.common.model.ProductIntgModuleInfoModel;
import com.inov8.microbank.common.model.SwitchModel;


/**
 * IntgTransactionType entity. @author MyEclipse Persistence Tools
 */
@Entity
@org.hibernate.annotations.Entity(dynamicInsert = true)
@javax.persistence.SequenceGenerator(name = "INTG_TRANSACTION_TYPE_SEQ",sequenceName = "INTG_TRANSACTION_TYPE_SEQ", allocationSize=1)
@Table(name="INTG_TRANSACTION_TYPE")

public class IntgTransactionTypeModel extends BasePersistableModel implements java.io.Serializable {


    // Fields    

	private static final long serialVersionUID = 1L;
	private Long intgTransactionTypeId;
     private AppUserModel appUserByUpdatedBy;
     private ProductIntgModuleInfoModel productIntgModuleInfo;
     private SwitchModel switchRef;
     private AppUserModel appUserByCreatedBy;
     private String transactionType;
     private Short versionNo;
     private Timestamp createdOn;
     private Timestamp updatedOn;
     private String description;
     private Set<PostedTransactionReportModel> postedTransactionReports = new HashSet<PostedTransactionReportModel>(0);


    // Constructors

    /** default constructor */
    public IntgTransactionTypeModel() {
    }

	/** minimal constructor */
    public IntgTransactionTypeModel(Long intgTransactionTypeId) {
        this.intgTransactionTypeId = intgTransactionTypeId;
    }
   
    // Property accessors
    @Id 
    @Column(name="INTG_TRANSACTION_TYPE_ID", unique=true, nullable=false, precision=10, scale=0)

    public Long getIntgTransactionTypeId() {
        return this.intgTransactionTypeId;
    }
    
    public void setIntgTransactionTypeId(Long intgTransactionTypeId) {
        this.intgTransactionTypeId = intgTransactionTypeId;
    }
	@ManyToOne(fetch=FetchType.LAZY)
        @JoinColumn(name="UPDATED_BY")

    public AppUserModel getAppUserByUpdatedBy() {
        return this.appUserByUpdatedBy;
    }
    
    public void setAppUserByUpdatedBy(AppUserModel appUserByUpdatedBy) {
        this.appUserByUpdatedBy = appUserByUpdatedBy;
    }
	@ManyToOne(fetch=FetchType.LAZY)
        @JoinColumn(name="PRODUCT_INTG_MODULE_INFO_ID")

    public ProductIntgModuleInfoModel getProductIntgModuleInfo() {
        return this.productIntgModuleInfo;
    }
    
    public void setProductIntgModuleInfo(ProductIntgModuleInfoModel productIntgModuleInfo) {
        this.productIntgModuleInfo = productIntgModuleInfo;
    }
	@ManyToOne(fetch=FetchType.LAZY)
        @JoinColumn(name="SWITCH_ID")

    public SwitchModel getSwitchRef() {
        return this.switchRef;
    }
    
    public void setSwitchRef(SwitchModel switchRef) {
        this.switchRef = switchRef;
    }
	@ManyToOne(fetch=FetchType.LAZY)
        @JoinColumn(name="CREATED_BY")

    public AppUserModel getAppUserByCreatedBy() {
        return this.appUserByCreatedBy;
    }
    
    public void setAppUserByCreatedBy(AppUserModel appUserByCreatedBy) {
        this.appUserByCreatedBy = appUserByCreatedBy;
    }
    
    @Column(name="TRANSACTION_TYPE", length=50)

    public String getTransactionType() {
        return this.transactionType;
    }
    
    public void setTransactionType(String transactionType) {
        this.transactionType = transactionType;
    }
    
    @Column(name="VERSION_NO", precision=4, scale=0)

    public Short getVersionNo() {
        return this.versionNo;
    }
    
    public void setVersionNo(Short versionNo) {
        this.versionNo = versionNo;
    }
    
    @Column(name="CREATED_ON", length=7)

    public Timestamp getCreatedOn() {
        return this.createdOn;
    }
    
    public void setCreatedOn(Timestamp createdOn) {
        this.createdOn = createdOn;
    }
    
    @Column(name="UPDATED_ON", length=7)

    public Timestamp getUpdatedOn() {
        return this.updatedOn;
    }
    
    public void setUpdatedOn(Timestamp updatedOn) {
        this.updatedOn = updatedOn;
    }
    
    @Column(name="DESCRIPTION", length=250)

    public String getDescription() {
        return this.description;
    }
    
    public void setDescription(String description) {
        this.description = description;
    }
@OneToMany(cascade=CascadeType.ALL, fetch=FetchType.LAZY, mappedBy="intgTransactionType")

    public Set<PostedTransactionReportModel> getPostedTransactionReports() {
        return this.postedTransactionReports;
    }
    
    public void setPostedTransactionReports(Set<PostedTransactionReportModel> postedTransactionReports) {
        this.postedTransactionReports = postedTransactionReports;
    }

	@Override
	@javax.persistence.Transient
	public Long getPrimaryKey() {
		return getIntgTransactionTypeId();
	}

	@Override
	@javax.persistence.Transient
	public String getPrimaryKeyFieldName() {
	    return "intgTransactionTypeId";
	}

	@Override
	@javax.persistence.Transient
	public String getPrimaryKeyParameter() {
	    String parameter = "&intgTransactionTypeId=" + getIntgTransactionTypeId();
        return parameter;
	}

	@Override
	@javax.persistence.Transient
	public void setPrimaryKey(Long primaryKey) {
		setIntgTransactionTypeId( primaryKey );
	}

}