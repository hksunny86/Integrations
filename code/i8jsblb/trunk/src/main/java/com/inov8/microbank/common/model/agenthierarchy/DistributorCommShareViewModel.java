package com.inov8.microbank.common.model.agenthierarchy;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;

import com.inov8.framework.common.model.BasePersistableModel;
import org.springframework.jdbc.core.RowMapper;


/**
 * HolidayModel entity.
 * @author Rizwan Munir
 */
@Entity
@org.hibernate.annotations.Entity(mutable = false)
@Table(name = "DISTRIBUTOR_COM_SHARE_VIEW")
public class DistributorCommShareViewModel extends BasePersistableModel implements RowMapper {

	private static final long serialVersionUID = -1427106253293008955L;

	private Long pk;
	private Long appUserId;
	private Long productId;
	private Long distributorLevelId;
	private Long distributorId;
	private Long parentAppUserId;
	private Long parentRetailerContactId;
	private Double parentCommissionShare;
	private Double commissionShare;
	private Boolean head;


    public DistributorCommShareViewModel() {
    	
    }

    
    /**
     * Return the primary key.
     *
     * @return Long with the primary key.
     */
   @javax.persistence.Transient
   public Long getPrimaryKey() {
        return getPk();
    }

    /**
     * Set the primary key.
     *
     * @param primaryKey the primary key
     */
   @javax.persistence.Transient
   public void setPrimaryKey(Long primaryKey) {
       setPk(primaryKey);
    }

   /**
    * Returns the value of the <code>pk</code> property.
    *
    */
      @Column(name = "PK" , nullable = false  )
      @Id 
   public Long getPk() {
      return pk;
   }

   /**
    * Sets the value of the <code>pk</code> property.
    *
    * @param pk the value for the <code>pk</code> property
    *    
		    */

   public void setPk(Long pk) {
      this.pk = pk;
   }
    
    @Column(name = "APP_USER_ID")
    public Long getAppUserId() {
		return appUserId;
	}

    @Column(name = "PRODUCT_ID")
    public Long getProductId() {
		return productId;
	}

    @Column(name = "DISTRIBUTOR_LEVEL_ID")
    public Long getDistributorLevelId() {
		return distributorLevelId;
	}

    @Column(name = "DISTRIBUTOR_ID")
    public Long getDistributorId() {
		return distributorId;
	}

    @Column(name = "PARENT_APP_USER_ID")
    public Long getParentAppUserId() {
		return parentAppUserId;
	}

    @Column(name = "PARENT_RETAILER_CONTACT_ID")
    public Long getParentRetailerContactId() {
		return parentRetailerContactId;
	}

    @Column(name = "PARENT_COMMISSION_SHARE")
    public Double getParentCommissionShare() {
		return parentCommissionShare;
	}

    @Column(name = "COMMISSION_SHARE")
    public Double getCommissionShare() {
		return commissionShare;
	}

    /**
     * Returns the value of the <code>head</code> property.
     *
     */
    @Column(name = "IS_HEAD" , nullable = false )
    public Boolean getHead() {
    	return head;
    }

    public void setAppUserId(Long appUserId) {
    	this.appUserId = appUserId;
    }

	public void setProductId(Long productId) {
		this.productId = productId;
	}

	public void setDistributorLevelId(Long distributorLevelId) {
		this.distributorLevelId = distributorLevelId;
	}

	public void setDistributorId(Long distributorId) {
		this.distributorId = distributorId;
	}

	public void setParentAppUserId(Long parentAppUserId) {
		this.parentAppUserId = parentAppUserId;
	}

	public void setParentRetailerContactId(Long parentRetailerContactId) {
		this.parentRetailerContactId = parentRetailerContactId;
	}

	public void setParentCommissionShare(Double parentCommissionShare) {
		this.parentCommissionShare = parentCommissionShare;
	}

	public void setCommissionShare(Double commissionShare) {
		this.commissionShare = commissionShare;
	}

	/**
	 * Sets the value of the <code>head</code> property.
	 *
	 * @param head the value for the <code>head</code> property
	 *    
	 */

	public void setHead(Boolean head) {
		this.head = head;
	}

    /**
     * Used by the display tag library for rendering a checkbox in the list.
     * @return String with a HTML checkbox.
     */
    @Transient
    public String getCheckbox() {
        String checkBox = "<input type=\"checkbox\" name=\"checkbox";
        checkBox += "_"+ getPk();
        checkBox += "\"/>";
        return checkBox;
    }

   /**
    * Helper method for Struts with displaytag
    */
   @javax.persistence.Transient
   public String getPrimaryKeyParameter() {
      String parameters = "";
      parameters += "&pk=" + getPk();
      return parameters;
   }
	/**
     * Helper method for default Sorting on Primary Keys
     */
    @javax.persistence.Transient
    public String getPrimaryKeyFieldName()
    { 
			String primaryKeyFieldName = "pk";
			return primaryKeyFieldName;				
    }

    @Override
    public Object mapRow(ResultSet resultSet, int i) throws SQLException {
        DistributorCommShareViewModel vo=new DistributorCommShareViewModel();
        vo.setPk(resultSet.getLong("PK"));
        vo.setAppUserId(resultSet.getLong("APP_USER_ID"));
        vo.setProductId(resultSet.getLong("PRODUCT_ID"));
        vo.setDistributorLevelId(resultSet.getLong("DISTRIBUTOR_LEVEL_ID"));
        vo.setDistributorId(resultSet.getLong("DISTRIBUTOR_ID"));
        vo.setParentAppUserId(resultSet.getLong("PARENT_APP_USER_ID"));
        vo.setParentRetailerContactId(resultSet.getLong("PARENT_RETAILER_CONTACT_ID"));
        vo.setParentCommissionShare(resultSet.getDouble("PARENT_COMMISSION_SHARE"));
        vo.setCommissionShare(resultSet.getDouble("COMMISSION_SHARE"));
        vo.setHead(resultSet.getBoolean("IS_HEAD"));

        return vo;
    }
}