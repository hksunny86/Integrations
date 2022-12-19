package com.inov8.microbank.common.model;

import java.io.Serializable;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;
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
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.persistence.Version;

import com.inov8.framework.common.model.AssociationModel;
import com.inov8.framework.common.model.BasePersistableModel;
import org.springframework.jdbc.core.RowMapper;

/**
 * The CommissionRateModel entity bean.
 * 
 * @author Usman Ashraf Inov8 Limited
 * @version $Revision: 1.20 $, $Date: 2006/03/06 19:29:08 $
 * 
 * 
 * @spring.bean name="CommissionRateModel"
 */
@Entity
@org.hibernate.annotations.Entity(dynamicInsert = true)
@javax.persistence.SequenceGenerator(name = "PRODUCT_CHARGES_DEFAULT_SEQ", sequenceName = "PRODUCT_CHARGES_DEFAULT_SEQ", allocationSize = 1)
@Table(name = "PRODUCT_CHARGES_DEFAULT")
public class CommissionRateDefaultModel extends BasePersistableModel implements
		Serializable,RowMapper {

	private ProductModel productIdProductModel;
	private AppUserModel createdByAppUserModel;
	private AppUserModel updatedByAppUserModel;
	
	private Long commissionRateDefaultId;
	private Date createdOn;
	private Date updatedOn;
	private Integer versionNo;
	private Double exclusiveFixAmount;
	private Double exclusivePercentAmount;
	private Double inclusiveFixAmount;
	private Double inclusivePercentAmount;

	/**
	 * Default constructor.
	 */
	public CommissionRateDefaultModel() {
	}

	/**
	 * Return the primary key.
	 * 
	 * @return Long with the primary key.
	 */
	@javax.persistence.Transient
	public Long getPrimaryKey() {
		return getCommissionRateDefaultId();
	}

	/**
	 * Set the primary key.
	 * 
	 * @param primaryKey
	 *            the primary key
	 */
	@javax.persistence.Transient
	public void setPrimaryKey(Long primaryKey) {
		setCommissionRateDefaultId(primaryKey);
	}

	/**
	 * Returns the value of the <code>CommissionRateDefaultId</code> property.
	 * 
	 */
	@Column(name = "PRODUCT_CHARGES_DEF_ID", nullable = false)
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "PRODUCT_CHARGES_DEFAULT_SEQ")
	public Long getCommissionRateDefaultId() {
		return commissionRateDefaultId;
	}

	/**
	 * Sets the value of the <code>CommissionRateDefaultId</code> property.
	 * 
	 * @param CommissionRateDefaultId
	 *            the value for the <code>CommissionRateDefaultId</code> property
	 * 
	 */

	public void setCommissionRateDefaultId(Long commissionRateDefaultId) {
		this.commissionRateDefaultId = commissionRateDefaultId;
	}

	
	/**
	 * Returns the value of the <code>exclusiveFixAmount</code> property.
	 * 
	 */
	@Column(name = "EXCLUSIVE_FIX_AMOUNT", nullable = true)
	public Double getExclusiveFixAmount() {
		return exclusiveFixAmount;
	}

	/**
	 * Sets the value of the <code>exclusiveFixAmount</code> property.
	 * 
	 * @param rate
	 *            the value for the <code>exclusiveFixAmount</code> property
	 * 
	 * @spring.validator type="required"
	 * @spring.validator type="double"
	 * @spring.validator type="doubleRange"
	 * @spring.validator-args arg1value="${var:min}"
	 * @spring.validator-var name="min" value="0"
	 * @spring.validator-args arg2value="${var:max}"
	 * @spring.validator-var name="max" value="99999999999.9999"
	 */

	public void setExclusiveFixAmount(Double exclusiveFixAmount) {
		this.exclusiveFixAmount = exclusiveFixAmount;
	}

	
	/**
	 * Returns the value of the <code>exclusiveFixAmount</code> property.
	 * 
	 */
	@Column(name = "INCLUSIVE_FIX_AMOUNT", nullable = true)
	public Double getInclusiveFixAmount() {
		return inclusiveFixAmount;
	}

	/**
	 * Sets the value of the <code>inclusiveFixAmount</code> property.
	 * 
	 * @param rate
	 *            the value for the <code>inclusiveFixAmount</code> property
	 * 
	 * @spring.validator type="required"
	 * @spring.validator type="double"
	 * @spring.validator type="doubleRange"
	 * @spring.validator-args arg1value="${var:min}"
	 * @spring.validator-var name="min" value="0"
	 * @spring.validator-args arg2value="${var:max}"
	 * @spring.validator-var name="max" value="99999999999.9999"
	 */

	public void setInclusiveFixAmount(Double inclusiveFixAmount) {
		this.inclusiveFixAmount = inclusiveFixAmount;
	}

	/**
	 * Returns the value of the <code>exclusivePercentAmount</code> property.
	 * 
	 */
	@Column(name = "EXCLUSIVE_PERCENT_AMOUNT", nullable = true)
	public Double getExclusivePercentAmount() {
		return exclusivePercentAmount;
	}

	/**
	 * Sets the value of the <code>exclusivePercentAmount</code> property.
	 * 
	 * @param rate
	 *            the value for the <code>exclusivePercentAmount</code> property
	 * 
	 * @spring.validator type="required"
	 * @spring.validator type="double"
	 * @spring.validator type="doubleRange"
	 * @spring.validator-args arg1value="${var:min}"
	 * @spring.validator-var name="min" value="0"
	 * @spring.validator-args arg2value="${var:max}"
	 * @spring.validator-var name="max" value="99999999999.9999"
	 */

	public void setExclusivePercentAmount(Double exclusivePercentAmount) {
		this.exclusivePercentAmount = exclusivePercentAmount;
	}


	/**
	 * Returns the value of the <code>inclusivePercentAmount</code> property.
	 * 
	 */
	@Column(name = "INCLUSIVE_PERCENT_AMOUNT", nullable = true)
	public Double getInclusivePercentAmount() {
		return inclusivePercentAmount;
	}

	/**
	 * Sets the value of the <code>inclusivePercentAmount</code> property.
	 * 
	 * @param rate
	 *            the value for the <code>inclusivePercentAmount</code> property
	 * 
	 * @spring.validator type="required"
	 * @spring.validator type="double"
	 * @spring.validator type="doubleRange"
	 * @spring.validator-args arg1value="${var:min}"
	 * @spring.validator-var name="min" value="0"
	 * @spring.validator-args arg2value="${var:max}"
	 * @spring.validator-var name="max" value="99999999999.9999"
	 */

	public void setInclusivePercentAmount(Double inclusivePercentAmount) {
		this.inclusivePercentAmount = inclusivePercentAmount;
	}

	/**
	 * Returns the value of the <code>createdOn</code> property.
	 * 
	 */
	@Column(name = "CREATED_ON", nullable = false)
	public Date getCreatedOn() {
		return createdOn;
	}

	/**
	 * Sets the value of the <code>createdOn</code> property.
	 * 
	 * @param createdOn
	 *            the value for the <code>createdOn</code> property
	 * 
	 */

	public void setCreatedOn(Date createdOn) {
		this.createdOn = createdOn;
	}

	/**
	 * Returns the value of the <code>updatedOn</code> property.
	 * 
	 */
	@Column(name = "UPDATED_ON", nullable = false)
	public Date getUpdatedOn() {
		return updatedOn;
	}

	/**
	 * Sets the value of the <code>updatedOn</code> property.
	 * 
	 * @param updatedOn
	 *            the value for the <code>updatedOn</code> property
	 * 
	 */

	public void setUpdatedOn(Date updatedOn) {
		this.updatedOn = updatedOn;
	}

	/**
	 * Returns the value of the <code>versionNo</code> property.
	 * 
	 */
	@Version
	@Column(name = "VERSION_NO", nullable = false)
	public Integer getVersionNo() {
		return versionNo;
	}

	/**
	 * Sets the value of the <code>versionNo</code> property.
	 * 
	 * @param versionNo
	 *            the value for the <code>versionNo</code> property
	 * 
	 */

	public void setVersionNo(Integer versionNo) {
		this.versionNo = versionNo;
	}


	/**
	 * Returns the value of the <code>productIdProductModel</code> relation
	 * property.
	 * 
	 * @return the value of the <code>productIdProductModel</code> relation
	 *         property.
	 * 
	 */
	@ManyToOne(cascade = CascadeType.REFRESH, fetch = FetchType.LAZY)
	@JoinColumn(name = "PRODUCT_ID")
	public ProductModel getRelationProductIdProductModel() {
		return productIdProductModel;
	}

	/**
	 * Returns the value of the <code>productIdProductModel</code> relation
	 * property.
	 * 
	 * @return the value of the <code>productIdProductModel</code> relation
	 *         property.
	 * 
	 */
	@javax.persistence.Transient
	public ProductModel getProductIdProductModel() {
		return getRelationProductIdProductModel();
	}

	/**
	 * Sets the value of the <code>productIdProductModel</code> relation
	 * property.
	 * 
	 * @param productModel
	 *            a value for <code>productIdProductModel</code>.
	 */
	@javax.persistence.Transient
	public void setRelationProductIdProductModel(ProductModel productModel) {
		this.productIdProductModel = productModel;
	}

	/**
	 * Sets the value of the <code>productIdProductModel</code> relation
	 * property.
	 * 
	 * @param productModel
	 *            a value for <code>productIdProductModel</code>.
	 */
	@javax.persistence.Transient
	public void setProductIdProductModel(ProductModel productModel) {
		if (null != productModel) {
			setRelationProductIdProductModel((ProductModel) productModel
					.clone());
		}
	}


	/**
	 * Returns the value of the <code>createdByAppUserModel</code> relation
	 * property.
	 * 
	 * @return the value of the <code>createdByAppUserModel</code> relation
	 *         property.
	 * 
	 */
	@ManyToOne(cascade = CascadeType.REFRESH, fetch = FetchType.LAZY)
	@JoinColumn(name = "CREATED_BY")
	public AppUserModel getRelationCreatedByAppUserModel() {
		return createdByAppUserModel;
	}

	/**
	 * Returns the value of the <code>createdByAppUserModel</code> relation
	 * property.
	 * 
	 * @return the value of the <code>createdByAppUserModel</code> relation
	 *         property.
	 * 
	 */
	@javax.persistence.Transient
	public AppUserModel getCreatedByAppUserModel() {
		return getRelationCreatedByAppUserModel();
	}

	/**
	 * Sets the value of the <code>createdByAppUserModel</code> relation
	 * property.
	 * 
	 * @param appUserModel
	 *            a value for <code>createdByAppUserModel</code>.
	 */
	@javax.persistence.Transient
	public void setRelationCreatedByAppUserModel(AppUserModel appUserModel) {
		this.createdByAppUserModel = appUserModel;
	}

	/**
	 * Sets the value of the <code>createdByAppUserModel</code> relation
	 * property.
	 * 
	 * @param appUserModel
	 *            a value for <code>createdByAppUserModel</code>.
	 */
	@javax.persistence.Transient
	public void setCreatedByAppUserModel(AppUserModel appUserModel) {
		if (null != appUserModel) {
			setRelationCreatedByAppUserModel((AppUserModel) appUserModel
					.clone());
		}
	}

	/**
	 * Returns the value of the <code>updatedByAppUserModel</code> relation
	 * property.
	 * 
	 * @return the value of the <code>updatedByAppUserModel</code> relation
	 *         property.
	 * 
	 */
	@ManyToOne(cascade = CascadeType.REFRESH, fetch = FetchType.LAZY)
	@JoinColumn(name = "UPDATED_BY")
	public AppUserModel getRelationUpdatedByAppUserModel() {
		return updatedByAppUserModel;
	}

	/**
	 * Returns the value of the <code>updatedByAppUserModel</code> relation
	 * property.
	 * 
	 * @return the value of the <code>updatedByAppUserModel</code> relation
	 *         property.
	 * 
	 */
	@javax.persistence.Transient
	public AppUserModel getUpdatedByAppUserModel() {
		return getRelationUpdatedByAppUserModel();
	}

	/**
	 * Sets the value of the <code>updatedByAppUserModel</code> relation
	 * property.
	 * 
	 * @param appUserModel
	 *            a value for <code>updatedByAppUserModel</code>.
	 */
	@javax.persistence.Transient
	public void setRelationUpdatedByAppUserModel(AppUserModel appUserModel) {
		this.updatedByAppUserModel = appUserModel;
	}

	/**
	 * Sets the value of the <code>updatedByAppUserModel</code> relation
	 * property.
	 * 
	 * @param appUserModel
	 *            a value for <code>updatedByAppUserModel</code>.
	 */
	@javax.persistence.Transient
	public void setUpdatedByAppUserModel(AppUserModel appUserModel) {
		if (null != appUserModel) {
			setRelationUpdatedByAppUserModel((AppUserModel) appUserModel
					.clone());
		}
	}

	/**
	 * Add the related CommissionTransactionModel to this one-to-many relation.
	 * 
	 * @param commissionTransactionModel
	 *            object to be added.
	 */


	/**
	 * Returns the value of the <code>productId</code> property.
	 * 
	 */
	@javax.persistence.Transient
	public Long getProductId() {
		if (productIdProductModel != null) {
			return productIdProductModel.getProductId();
		} else {
			return null;
		}
	}

	/**
	 * Sets the value of the <code>productId</code> property.
	 * 
	 * @param productId
	 *            the value for the <code>productId</code> property
	 * @spring.validator type="required"
	 */

	@javax.persistence.Transient
	public void setProductId(Long productId) {
		if (productId == null) {
			productIdProductModel = null;
		} else {
			productIdProductModel = new ProductModel();
			productIdProductModel.setProductId(productId);
		}
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
	 * @param appUserId
	 *            the value for the <code>appUserId</code> property
	 */

	@javax.persistence.Transient
	public void setCreatedBy(Long appUserId) {
		if (appUserId == null) {
			createdByAppUserModel = null;
		} else {
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
	 * @param appUserId
	 *            the value for the <code>appUserId</code> property
	 */

	@javax.persistence.Transient
	public void setUpdatedBy(Long appUserId) {
		if (appUserId == null) {
			updatedByAppUserModel = null;
		} else {
			updatedByAppUserModel = new AppUserModel();
			updatedByAppUserModel.setAppUserId(appUserId);
		}
	}

	/**
	 * Used by the display tag library for rendering a checkbox in the list.
	 * 
	 * @return String with a HTML checkbox.
	 */
	@Transient
	public String getCheckbox() {
		String checkBox = "<input type=\"checkbox\" name=\"checkbox";
		checkBox += "_" + getCommissionRateDefaultId();
		checkBox += "\"/>";
		return checkBox;
	}

	/**
	 * Helper method for Struts with displaytag
	 */
	@javax.persistence.Transient
	public String getPrimaryKeyParameter() {
		String parameters = "";
		parameters += "&commissionRateDefaultId=" + getCommissionRateDefaultId();
		return parameters;
	}

	/**
	 * Helper method for default Sorting on Primary Keys
	 */
	@javax.persistence.Transient
	public String getPrimaryKeyFieldName() {
		String primaryKeyFieldName = "commissionRateDefaultId";
		return primaryKeyFieldName;
	}

	


	/**
	 * Helper method for Complex Example Queries
	 */
	@javax.persistence.Transient
	@Override
	public List<AssociationModel> getAssociationModelList() {
		List<AssociationModel> associationModelList = new ArrayList<AssociationModel>();
		AssociationModel associationModel = null;

		associationModel = new AssociationModel();

		associationModel.setClassName("ProductModel");
		associationModel.setPropertyName("relationProductIdProductModel");
		associationModel.setValue(getRelationProductIdProductModel());

		associationModelList.add(associationModel);

		associationModel = new AssociationModel();

		associationModel.setClassName("AppUserModel");
		associationModel.setPropertyName("relationCreatedByAppUserModel");
		associationModel.setValue(getRelationCreatedByAppUserModel());

		associationModelList.add(associationModel);

		associationModel = new AssociationModel();

		associationModel.setClassName("AppUserModel");
		associationModel.setPropertyName("relationUpdatedByAppUserModel");
		associationModel.setValue(getRelationUpdatedByAppUserModel());

		associationModelList.add(associationModel);

		return associationModelList;
	}

	@Override
	public Object mapRow(ResultSet resultSet, int i) throws SQLException {
		CommissionRateDefaultModel model = new CommissionRateDefaultModel();
		model.setCommissionRateDefaultId(resultSet.getLong("PRODUCT_CHARGES_DEF_ID"));
		model.setProductId(resultSet.getLong("PRODUCT_ID"));
		model.setExclusiveFixAmount(resultSet.getDouble("EXCLUSIVE_FIX_AMOUNT"));
		model.setExclusivePercentAmount(resultSet.getDouble("EXCLUSIVE_PERCENT_AMOUNT"));
		model.setInclusivePercentAmount(resultSet.getDouble("INCLUSIVE_PERCENT_AMOUNT"));
		model.setInclusiveFixAmount(resultSet.getDouble("INCLUSIVE_FIX_AMOUNT"));
		model.setCreatedOn(resultSet.getDate("CREATED_ON"));
		model.setUpdatedOn(resultSet.getDate("CREATED_ON"));
		model.setCreatedBy(resultSet.getLong("CREATED_BY"));
		model.setUpdatedBy(resultSet.getLong("UPDATED_BY"));
		return model;
	}
}
