package com.inov8.microbank.common.model;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;

import com.inov8.framework.common.model.BasePersistableModel;

/**
 * @author Atif Hussain
 */
@Entity
@org.hibernate.annotations.Entity(dynamicInsert = true)
@Table(name = "AGENT_MERCHANT_DETAIL_VIEW")
public class AgentMerchantDetailViewModel extends BasePersistableModel
{

	/**
	 * 
	 */
	private static final long serialVersionUID = -4314564915059294654L;
	
	private Long agentMerchantDetailId;
	private String initialAppFormNo;
	private String businessName;
	private String empId;
	private String empName;
	private Date createdOn;
	private String userName;
	private Date startDate;
	private Date endDate;

	public AgentMerchantDetailViewModel()
	{

	}

	@Column(name = "AGENT_MERCHANT_DETAIL_ID", nullable = false)
	@Id
	public Long getAgentMerchantDetailId()
	{
		return this.agentMerchantDetailId;
	}

	public void setAgentMerchantDetailId(Long agentMerchantDetailId)
	{
		if (agentMerchantDetailId != null)
		{
			this.agentMerchantDetailId = agentMerchantDetailId;
		}
	}

	/**
	 * Return the primary key.
	 * 
	 * @return Long with the primary key.
	 */
	@Override
	@javax.persistence.Transient
	public Long getPrimaryKey()
	{
		return this.getAgentMerchantDetailId();
	}

	/**
	 * Set the primary key.
	 * 
	 * @param primaryKey
	 *            the primary key
	 */
	@Override
	@javax.persistence.Transient
	public void setPrimaryKey(Long primaryKey)
	{
		this.setAgentMerchantDetailId(primaryKey);
	}

	@Column(name = "INITIAL_APP_FORM_NUMBER")
	public String getInitialAppFormNo()
	{
		return this.initialAppFormNo;
	}

	public void setInitialAppFormNo(String initialAppFormNo)
	{
		if (initialAppFormNo != null)
		{
			this.initialAppFormNo = initialAppFormNo;
		}
	}

	@Column(name = "BUSINESS_NAME")
	public String getBusinessName()
	{
		return this.businessName;
	}

	public void setBusinessName(String businessName)
	{
		if (businessName != null)
		{
			this.businessName = businessName;
		}
	}

	@Column(name = "EMP_ID")
	public String getEmpId()
	{
		return this.empId;
	}

	public void setEmpId(String empId)
	{
		if (empId != null)
		{
			this.empId = empId;
		}
	}

	@Column(name = "EMP_NAME")
	public String getEmpName()
	{
		return this.empName;
	}

	public void setEmpName(String empName)
	{
		if (empName != null)
		{
			this.empName = empName;
		}
	}

	/**
	 * Helper method for Struts with displaytag
	 */
	@Override
	@javax.persistence.Transient
	public String getPrimaryKeyParameter()
	{
		String parameters = "";
		parameters += "&agentMerchantDetailId=" + this.getAgentMerchantDetailId();
		return parameters;
	}

	/**
	 * Helper method for default Sorting on Primary Keys
	 */
	@Override
	@javax.persistence.Transient
	public String getPrimaryKeyFieldName()
	{
		String primaryKeyFieldName = "agentMerchantDetailId";
		return primaryKeyFieldName;
	}

	@Column(name = "USERNAME")
	public String getUserName()
	{
		return this.userName;
	}

	public void setUserName(String userName)
	{
		if (userName != null)
		{
			this.userName = userName;
		}
	}

	@Column(name = "CREATED_ON")
	public Date getCreatedOn()
	{
		return createdOn;
	}

	public void setCreatedOn(Date createdOn)
	{
		this.createdOn = createdOn;
	}

	@Transient
	public Date getStartDate()
	{
		return startDate;
	}

	public void setStartDate(Date startDate)
	{
		this.startDate = startDate;
	}

	@Transient
	public Date getEndDate()
	{
		return endDate;
	}

	public void setEndDate(Date endDate)
	{
		this.endDate = endDate;
	}
}