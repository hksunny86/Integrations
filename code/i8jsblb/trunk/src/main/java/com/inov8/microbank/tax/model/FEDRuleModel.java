package com.inov8.microbank.tax.model;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.persistence.Version;

import com.inov8.framework.common.model.AssociationModel;
import com.inov8.framework.common.model.BasePersistableModel;
import com.inov8.microbank.common.model.AppUserModel;
import com.inov8.microbank.common.model.ProductModel;
import com.inov8.microbank.common.model.ServiceModel;
import com.inov8.microbank.common.model.TaxRegimeModel;
import org.springframework.jdbc.core.RowMapper;

@Entity
@org.hibernate.annotations.Entity(dynamicInsert = true)
@javax.persistence.SequenceGenerator(name = "FED_RULE_seq", sequenceName = "FED_RULE_seq", allocationSize = 1)
@Table(name = "FED_RULE")
public class FEDRuleModel extends BasePersistableModel implements RowMapper{

	private static final long serialVersionUID = 1L;
	private Long fedRuleId;
	private TaxRegimeModel taxRegimeModel;
	private ServiceModel serviceModel;
	private ProductModel productModel;
	private Double rate;
	private Boolean inclusive;	//1 exclusive, 0 inclusive
	private Boolean active;	//apply

	private AppUserModel createdByAppUserModel;
	private AppUserModel updatedByAppUserModel;
	private Date updatedOn;
	private Date createdOn;
	private Integer versionNo;

	@javax.persistence.Transient
	@Override
	public List<AssociationModel> getAssociationModelList() {
		List<AssociationModel> associationModelList = new ArrayList<AssociationModel>();
		AssociationModel associationModel = new AssociationModel();
		associationModel.setClassName("AppUserModel");
		associationModel.setPropertyName("createdByAppUserModel");
		associationModel.setValue(this.getCreatedByAppUserModel());
		associationModelList.add(associationModel);

		associationModel = new AssociationModel();
		associationModel.setClassName("AppUserModel");
		associationModel.setPropertyName("updatedByAppUserModel");
		associationModel.setValue(this.getUpdatedByAppUserModel());
		associationModelList.add(associationModel);

		associationModel = new AssociationModel();
		associationModel.setClassName("ServiceModel");
		associationModel.setPropertyName("serviceModel");
		associationModel.setValue(this.getServiceModel());
		associationModelList.add(associationModel);

		return associationModelList;
	}

	@Id
	@Column(name = "FED_RULE_ID", nullable = false)
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "FED_RULE_seq")
	public Long getFedRuleId() {
		return fedRuleId;
	}

	public void setFedRuleId(Long fedRuleId) {
		this.fedRuleId = fedRuleId;
	}

	@ManyToOne(cascade = CascadeType.REFRESH, fetch = FetchType.EAGER)
	@JoinColumn(name = "TAX_REGIME_ID")
	public TaxRegimeModel getTaxRegimeModel() {
		return taxRegimeModel;
	}

	public void setTaxRegimeModel(TaxRegimeModel taxRegimeModel) {
		this.taxRegimeModel = taxRegimeModel;
	}

	@ManyToOne(cascade = CascadeType.REFRESH, fetch = FetchType.LAZY)
	@JoinColumn(name = "SERVICE_ID")
	public ServiceModel getServiceModel() {
		return serviceModel;
	}

	public void setServiceModel(ServiceModel serviceModel) {
		this.serviceModel = serviceModel;
	}

	@ManyToOne(cascade = CascadeType.REFRESH, fetch = FetchType.LAZY)
	@JoinColumn(name = "PRODUCT_ID")
	public ProductModel getProductModel() {
		return productModel;
	}

	public void setProductModel(ProductModel productModel) {
		this.productModel = productModel;
	}

	@Column(name = "RATE", nullable = true)
	public Double getRate() {
		return rate;
	}

	public void setRate(Double rate) {
		this.rate = rate;
	}

	@Column(name = "INCLUSIVE", nullable = false)
	public Boolean getInclusive() {
		return inclusive;
	}

	public void setInclusive(Boolean inclusive) {
		this.inclusive = inclusive;
	}

	@Column(name = "IS_ACTIVE", nullable = false)
	public Boolean getActive() {
		return active;
	}

	public void setActive(Boolean active) {
		this.active = active;
	}
	
	@ManyToOne(cascade = CascadeType.REFRESH, fetch = FetchType.LAZY)
	@JoinColumn(name = "CREATED_BY")
	public AppUserModel getCreatedByAppUserModel() {
		return createdByAppUserModel;
	}

	public void setCreatedByAppUserModel(AppUserModel createdByAppUserModel) {
		this.createdByAppUserModel = createdByAppUserModel;
	}

	@ManyToOne(cascade = CascadeType.REFRESH, fetch = FetchType.LAZY)
	@JoinColumn(name = "UPDATED_BY")
	public AppUserModel getUpdatedByAppUserModel() {
		return updatedByAppUserModel;
	}

	public void setUpdatedByAppUserModel(AppUserModel updatedByAppUserModel) {
		this.updatedByAppUserModel = updatedByAppUserModel;
	}
	
	/**
	    * Returns the value of the <code>appUserId</code> property.
	    *
	    */
	   @javax.persistence.Transient
	   public Long getCreatedBy() {
	      if (createdByAppUserModel != null) {
	         return createdByAppUserModel.getAppUserId();
	      } else {
	         return null;
	      }
	   }

	   /**
	    * Sets the value of the <code>appUserId</code> property.
	    *
	    * @param appUserId the value for the <code>appUserId</code> property
																																																						    */
	   
	   @javax.persistence.Transient
	   public void setCreatedBy(Long appUserId) {
	      if(appUserId == null)
	      {      
	      	createdByAppUserModel = null;
	      }
	      else
	      {
	        createdByAppUserModel = new AppUserModel();
	      	createdByAppUserModel.setAppUserId(appUserId);
	      }      
	   }

	   /**
	    * Returns the value of the <code>appUserId</code> property.
	    *
	    */
	   @javax.persistence.Transient
	   public Long getUpdatedBy() {
	      if (updatedByAppUserModel != null) {
	         return updatedByAppUserModel.getAppUserId();
	      } else {
	         return null;
	      }
	   }

	   /**
	    * Sets the value of the <code>appUserId</code> property.
	    *
	    * @param appUserId the value for the <code>appUserId</code> property
																																																						    */
	   
	   @javax.persistence.Transient
	   public void setUpdatedBy(Long appUserId) {
	      if(appUserId == null)
	      {      
	      	updatedByAppUserModel = null;
	      }
	      else
	      {
	        updatedByAppUserModel = new AppUserModel();
	      	updatedByAppUserModel.setAppUserId(appUserId);
	      }      
	   }

	@Column(name = "UPDATED_ON", nullable = false)
	public Date getUpdatedOn() {
		return updatedOn;
	}

	public void setUpdatedOn(Date updatedOn) {
		this.updatedOn = updatedOn;
	}

	@Column(name = "CREATED_ON", nullable = false)
	public Date getCreatedOn() {
		return createdOn;
	}

	public void setCreatedOn(Date createdOn) {
		this.createdOn = createdOn;
	}

	@Version
	@Column(name = "VERSION_NO", nullable = false)
	public Integer getVersionNo() {
		return versionNo;
	}

	public void setVersionNo(Integer versionNo) {
		this.versionNo = versionNo;
	}

	@Transient
	@Override
	public Long getPrimaryKey() {
		return getFedRuleId();
	}

	@Transient
	@Override
	public String getPrimaryKeyFieldName() {
		return "fedRuleId";
	}

	@Transient
	@Override
	public String getPrimaryKeyParameter() {
		return "&fedRuleId=" + getPrimaryKey();
	}

	@Override
	public void setPrimaryKey(Long arg0) {
		setFedRuleId(arg0);
	}

	@Transient
	public void setServiceId(Long serviceId) {
		if(serviceModel == null)
			serviceModel = new ServiceModel();
		
		serviceModel.setServiceId(serviceId);
	}
	
	@Transient
	public Long getServiceId() {
		Long serviceId=null;
		if(serviceModel != null)
			serviceId = serviceModel.getServiceId();
		
		return serviceId;
	}
	
	@Transient
	public void setProductId(Long productId) {
		if(productModel == null)
			productModel = new ProductModel();
		
		productModel.setProductId(productId);
	}
	
	@Transient
	public void setTaxRegimeId(Long taxRegimeId) {
		if(taxRegimeModel == null)
			taxRegimeModel = new TaxRegimeModel();
		
		taxRegimeModel.setTaxRegimeId(taxRegimeId);
	}	
	
	@Transient
	public Long getTaxRegimeId() {
		Long taxRegimeId=null;
		if(taxRegimeModel != null)
			taxRegimeId = taxRegimeModel.getTaxRegimeId();
		
		return taxRegimeId;
	}	
	
	@Transient
	public Long getProductId() {
		Long productId = null;
		if(productModel != null)
			productId = productModel.getProductId();
		
		return productId;
	}

	@Override
	public Object mapRow(ResultSet resultSet, int i) throws SQLException {
	   	FEDRuleModel vo=new FEDRuleModel();
	   	vo.setFedRuleId(resultSet.getLong("FED_RULE_ID"));
	   	vo.setTaxRegimeId(resultSet.getLong("TAX_REGIME_ID"));
	   	vo.setServiceId(resultSet.getLong("SERVICE_ID"));
	   	vo.setCreatedBy(resultSet.getLong("CREATED_BY"));
	   	vo.setUpdatedBy(resultSet.getLong("UPDATED_BY"));
	   	vo.setCreatedOn(resultSet.getDate("CREATED_ON"));
	   	vo.setUpdatedOn(resultSet.getDate("UPDATED_ON"));
	   	vo.setActive(resultSet.getBoolean("IS_ACTIVE"));
	   	vo.setInclusive(resultSet.getBoolean("INCLUSIVE"));
	   	vo.setProductId(resultSet.getLong("PRODUCT_ID"));
	   	vo.setRate(resultSet.getDouble("RATE"));
	   	vo.setVersionNo(resultSet.getInt("VERSION_NO"));
		return vo;
	}
}
