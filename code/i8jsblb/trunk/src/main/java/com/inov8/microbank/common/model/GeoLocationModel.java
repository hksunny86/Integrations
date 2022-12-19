package com.inov8.microbank.common.model;

import java.beans.Transient;
import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Version;

import com.inov8.framework.common.model.BasePersistableModel;


@Entity
@org.hibernate.annotations.Entity(dynamicInsert = true)
@javax.persistence.SequenceGenerator(name = "GEO_LOCATION_seq",sequenceName = "GEO_LOCATION_seq", allocationSize=1)
@Table(name = "GEO_LOCATION")
public class GeoLocationModel extends BasePersistableModel implements Serializable {
	
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -2744832190520057291L;
	private Long geoLocationId;
	private Double latitude;
	private Double longitude;
	private Long createdBy;
	private Long updatedBy;
	private Date createdOn;
	private Date updatedOn;
	private Integer versionNo;
	
	
	@Id
	@GeneratedValue(strategy=GenerationType.SEQUENCE , generator="GEO_LOCATION_seq")
	@Column(name="GEO_LOCATION_ID")
	public Long getGeoLocationId() {
		return geoLocationId;
	}
	public void setGeoLocationId(Long geoLocationId) {
		this.geoLocationId = geoLocationId;
	}
	
	@Column(name="LATITUDE")
	public Double getLatitude() {
		return latitude;
	}
	public void setLatitude(Double latitude) {
		this.latitude = latitude;
	}
	
	@Column(name="LONGITUDE")
	public Double getLongitude() {
		return longitude;
	}
	public void setLongitude(Double longitude) {
		this.longitude = longitude;
	}
	
	@Column(name="CREATED_BY")
	public Long getCreatedBy() {
		return createdBy;
	}
	public void setCreatedBy(Long createdBy) {
		this.createdBy = createdBy;
	}
	
	@Column(name="UPDATED_BY")
	public Long getUpdatedBy() {
		return updatedBy;
	}
	public void setUpdatedBy(Long updatedBy) {
		this.updatedBy = updatedBy;
	}
	
	@Column(name="CREATED_ON")
	public Date getCreatedOn() {
		return createdOn;
	}
	public void setCreatedOn(Date createdOn) {
		this.createdOn = createdOn;
	}
	
	@Column(name="UPDATED_ON")
	public Date getUpdatedOn() {
		return updatedOn;
	}
	public void setUpdatedOn(Date updatedOn) {
		this.updatedOn = updatedOn;
	}
	
	@javax.persistence.Transient
	public Long getPrimaryKey() {
		return getGeoLocationId();
	}
	
	@javax.persistence.Transient
	public String getPrimaryKeyFieldName() {
		String primaryKeyFieldName = "geoLocationId";
		return primaryKeyFieldName;
	}

	@javax.persistence.Transient
	public String getPrimaryKeyParameter() {
		  String parameters = "";
	      parameters += "&geoLocationId=" + getGeoLocationId();
	      return parameters;
	}
	@javax.persistence.Transient
	public void setPrimaryKey(Long arg0) {
		this.setGeoLocationId(arg0);
	}
	
	
     @Version 
	 @Column(name = "VERSION_NO" , nullable = false )
     public Integer getVersionNo() {
    	 return versionNo;
  }

  /**
   * Sets the value of the <code>versionNo</code> property.
   *
   * @param versionNo the value for the <code>versionNo</code> property
   *    
		    */

  public void setVersionNo(Integer versionNo) {
     this.versionNo = versionNo;
  }

}
