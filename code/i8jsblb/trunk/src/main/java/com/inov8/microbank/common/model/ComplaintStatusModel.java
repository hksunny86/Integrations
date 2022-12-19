package com.inov8.microbank.common.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import com.inov8.framework.common.model.BasePersistableModel;

/**
 * ComplaintStatusModel entity.
 * @author Muhammad Omar Butt
 */
@Entity
@org.hibernate.annotations.Entity(dynamicInsert = true)
@javax.persistence.SequenceGenerator(name = "COMPLAINT_STATUS_SEQ",sequenceName = "COMPLAINT_STATUS_SEQ", allocationSize=1)
@Table(name = "COMPLAINT_STATUS")

public class ComplaintStatusModel  extends BasePersistableModel {
     
	private Long statusId;
    private String name;

    public ComplaintStatusModel() {
    }

    @Column(name = "COMPLAINT_STATUS_ID" , nullable = false )
    @Id
    @GeneratedValue(strategy=GenerationType.SEQUENCE, generator="COMPLAINT_STATUS_SEQ")
    public Long getStatusId() {
        return this.statusId;
    }
    
    public void setStatusId(Long statusId) {
        this.statusId = statusId;
    }

    @Column(name="NAME")
    public String getName() {
        return this.name;
    }
    
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Return the primary key.
     *
     * @return Long with the primary key.
     */
   @javax.persistence.Transient
   public Long getPrimaryKey() {
        return getStatusId();
    }

    /**
     * Set the primary key.
     *
     * @param primaryKey the primary key
     */
   @javax.persistence.Transient
   public void setPrimaryKey(Long primaryKey) {
	   setStatusId(primaryKey);
    }

	@Override
	public String getPrimaryKeyFieldName() {
		return null;
	}

	@Override
	public String getPrimaryKeyParameter() {
		return null;
	}

   
}