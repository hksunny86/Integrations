package com.inov8.microbank.tax.model;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.Columns;

import com.inov8.framework.common.model.BasePersistableModel;





@Entity
@org.hibernate.annotations.Entity(dynamicInsert = true)
@javax.persistence.SequenceGenerator(name = "WHT_DEDUC_SCHEDULER_STATUS_seq", sequenceName = "WHT_DEDUC_SCHEDULER_STATUS_seq", allocationSize = 1)
@Table(name = "WHT_DEDUCTION_SCHEDULER_STATUS")
public class WHTDeductionSchedularStatusModel extends BasePersistableModel {

	private Date start_date;
	private Date transaction_date;
	private Boolean initialization_status;
	private Boolean completion_status;
	private Long wht_deduction_schedular_id;
	private Date created_on;
	private Date updated_on;
	
	
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "WHT_DEDUC_SCHEDULER_STATUS_seq")
	@Column(name = "WHT_DEDUCTION_SCHEDULER_ID")
	public Long getWht_deduction_schedular_id() {
		return wht_deduction_schedular_id;
	}

	public void setWht_deduction_schedular_id(Long wht_deduction_schedular_id) {
		this.wht_deduction_schedular_id = wht_deduction_schedular_id;
	}

	@Column(name = "START_DATE")
	public Date getStart_date() {
		return start_date;
	}

	public void setStart_date(Date start_date) {
		this.start_date = start_date;
	}

	@Column(name = "TRANSACTION_DATE")
	public Date getTransaction_date() {
		return transaction_date;
	}

	public void setTransaction_date(Date transaction_date) {
		this.transaction_date =  transaction_date;
	}

	@Column(name = "INITIALISATION_STATUS")
	public Boolean getInitialization_status() {
		return initialization_status;
	}

	public void setInitialization_status(Boolean initialization_status) {
		this.initialization_status = initialization_status;
	}

	@Column(name = "COMPLETION_STATUS")
	public Boolean getCompletion_status() {
		return completion_status;
	}

	public void setCompletion_status(Boolean completion_status) {
		this.completion_status = completion_status;
	}

	@javax.persistence.Transient
	public Long getPrimaryKey() {
		return getWht_deduction_schedular_id();
	}

	@javax.persistence.Transient
	public String getPrimaryKeyFieldName() {
		return "wht_deduction_schedular_id";
	}

	@javax.persistence.Transient
	public String getPrimaryKeyParameter() {
		return "&wht_deduction_schedular_id=" + getPrimaryKey();
	}

	@javax.persistence.Transient
	public void setPrimaryKey(Long arg0) {
		setWht_deduction_schedular_id(arg0);
	}

	@Column(name = "CREATED_ON")
	public Date getCreated_on() {
		return created_on;
	}

	public void setCreated_on(Date created_on) {
		this.created_on = created_on;
	}

	@Column(name = "UPDATED_ON")
	public Date getUpdated_on() {
		return updated_on;
	}

	public void setUpdated_on(Date updated_on) {
		this.updated_on = updated_on;
	}


}
