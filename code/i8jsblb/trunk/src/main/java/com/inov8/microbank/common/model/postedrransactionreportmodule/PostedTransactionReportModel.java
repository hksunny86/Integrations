package com.inov8.microbank.common.model.postedrransactionreportmodule; 

import java.util.Date;

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
import javax.persistence.Version;

import com.inov8.framework.common.model.BasePersistableModel;
import com.inov8.microbank.common.model.AppUserModel;
import com.inov8.microbank.common.model.ProductModel;
import com.inov8.microbank.common.model.TransactionCodeModel;

/**
 * PostedTransactionReport entity. @author MyEclipse Persistence Tools
 */
@Entity
@org.hibernate.annotations.Entity(dynamicInsert = true)
@javax.persistence.SequenceGenerator(name = "POSTED_TRANSACTION_REPORT_SEQ",sequenceName = "POSTED_TRANSACTION_REPORT_SEQ")
@Table( name = "POSTED_TRANSACTION_REPORT")
public class PostedTransactionReportModel extends BasePersistableModel implements java.io.Serializable
{

    /**
     * serialVersionUID
     */
    private static final long serialVersionUID = 5980666663396313788L;
    // Fields    

    private Long                postedTransactionReportId;
    private ProductModel             product;
    private IntgTransactionTypeModel intgTransactionType;
    private Long     transactionCodeId;
    private Double                amount;
    private String              responseCode;
    private String              fromAccount;
    private String              toAccount;
    private String              refCode;
    private String                consumerNo;
    private Integer               versionNo;
    private Date createdOn;
    private Date updatedOn;
    private String              description;
    private String              systemTraceAuditNumber;

	private AppUserModel updatedByAppUserModel;
    private AppUserModel createdByAppUserModel;
    
    // Constructors

    /** default constructor */
    public PostedTransactionReportModel()
    {
    }

    // Property accessors
    @Column(name = "POSTED_TRANS_RPT_ID" , nullable = false )
    @Id @GeneratedValue(strategy=GenerationType.SEQUENCE, generator="POSTED_TRANSACTION_REPORT_SEQ")
    public Long getPostedTransactionReportId()
    {
        return this.postedTransactionReportId;
    }

    public void setPostedTransactionReportId(Long postedTransactionReportId )
    {
        this.postedTransactionReportId = postedTransactionReportId;
    }

    
    
    @ManyToOne(cascade = CascadeType.REFRESH, fetch = FetchType.LAZY)
    @JoinColumn(name = "CREATED_BY" )    
    public AppUserModel getRelationCreatedByAppUserModel(){
       return createdByAppUserModel;
    }
    
    
    @javax.persistence.Transient
    public void setRelationCreatedByAppUserModel(AppUserModel appUserModel) {
    	this.createdByAppUserModel = appUserModel;
    }

    
    @ManyToOne(cascade = CascadeType.REFRESH, fetch = FetchType.LAZY)
    @JoinColumn(name = "UPDATED_BY")    
    public AppUserModel getRelationUpdatedByAppUserModel(){
       return this.updatedByAppUserModel;
    }

    
    @javax.persistence.Transient
    public void setRelationUpdatedByAppUserModel(AppUserModel appUserModel) {
       this.updatedByAppUserModel = appUserModel;
    }
    
    @javax.persistence.Transient
    public AppUserModel getCreatedByAppUserModel()
    {
        return getRelationCreatedByAppUserModel();
    }

    @javax.persistence.Transient
    public void setCreatedByAppUserModel( AppUserModel appUserByCreatedBy )
    {
    	if(appUserByCreatedBy != null)
    	{
    		setRelationCreatedByAppUserModel((AppUserModel)appUserByCreatedBy.clone());
    	}   
    }
    
    
    @javax.persistence.Transient
    public AppUserModel getUpdatedByAppUserModel()
    {
        return getRelationUpdatedByAppUserModel();
    }
    
    @javax.persistence.Transient
    public void setUpdatedByAppUserModel( AppUserModel appUserByUpdatedBy )
    {
    	if(null != appUserByUpdatedBy)
    	{
    		setRelationUpdatedByAppUserModel((AppUserModel)appUserByUpdatedBy.clone());
    	}
    }

    @ManyToOne(cascade = CascadeType.REFRESH, fetch = FetchType.LAZY)
    @JoinColumn( name = "PRODUCT_ID" )
    public ProductModel getProduct()
    {
        return this.product;
    }

    
    public void setProduct( ProductModel product )
    {
        this.product = product;
    }

    @ManyToOne(cascade = CascadeType.REFRESH, fetch = FetchType.LAZY)
    @JoinColumn( name = "INTG_TRANSACTION_TYPE_ID", nullable = true )
    public IntgTransactionTypeModel getIntgTransactionType()
    {
        return this.intgTransactionType;
    }

    public void setIntgTransactionType( IntgTransactionTypeModel intgTransactionType )
    {
        this.intgTransactionType = intgTransactionType;
    }

//    @ManyToOne(cascade = CascadeType.REFRESH, fetch = FetchType.LAZY)
//    @JoinColumn( name = "TRANSACTION_CODE_ID", nullable=true )
//    public TransactionCodeModel getTransactionCode()
//    {
//        return this.transactionCode;
//    }

//    public void setTransactionCode( TransactionCodeModel transactionCode )
//    {
//        this.transactionCode = transactionCode;
//    }

    
    @Column(name = "AMOUNT", precision = 16, scale = 4)
    public Double getAmount()
    {
        return this.amount;
    }

    public void setAmount(Double amount)
    {
        this.amount = amount;
    }

    @Column( name = "RESPONSE_CODE", length = 50 )
    public String getResponseCode()
    {
        return this.responseCode;
    }

    public void setResponseCode( String responseCode )
    {
        this.responseCode = responseCode;
    }

    @Column( name = "FROM_ACCOUNT", length = 50 )
    public String getFromAccount()
    {
        return this.fromAccount;
    }

    public void setFromAccount( String fromAccount )
    {
        this.fromAccount = fromAccount;
    }

    @Column( name = "TO_ACCOUNT", length = 50 )
    public String getToAccount()
    {
        return this.toAccount;
    }

    public void setToAccount( String toAccount )
    {
        this.toAccount = toAccount;
    }

    @Column( name = "REF_CODE", length = 50 )
    public String getRefCode()
    {
        return this.refCode;
    }

    public void setRefCode( String refCode )
    {
        this.refCode = refCode;
    }

    @Column(name = "CONSUMER_NO", length = 30)
    public String getConsumerNo()
    {
        return this.consumerNo;
    }

    public void setConsumerNo( String consumerNo )
    {
        this.consumerNo = consumerNo;
    }

    @Version 
    @Column(name = "VERSION_NO")
    public Integer getVersionNo()
    {
        return this.versionNo;
    }

    public void setVersionNo( Integer versionNo )
    {
        this.versionNo = versionNo;
    }

    @Column( name = "CREATED_ON", length = 7 )
    public Date getCreatedOn()
    {
        return this.createdOn;
    }

    public void setCreatedOn(Date createdOn)
    {
        this.createdOn = createdOn;
    }

    @Column( name = "UPDATED_ON", length = 7 )
    public Date getUpdatedOn()
    {
        return this.updatedOn;
    }

    public void setUpdatedOn(Date updatedOn )
    {
        this.updatedOn = updatedOn;
    }

    @Column( name = "DESCRIPTION", length = 250 )
    public String getDescription()
    {
        return this.description;
    }

    public void setDescription( String description )
    {
        this.description = description;
    }

    /**
     * Returns the value of the <code>updatedBy</code> property.
     *
     */
    @javax.persistence.Transient
    public Long getUpdatedBy() {
       if (this.updatedByAppUserModel != null) {
          return this.updatedByAppUserModel.getAppUserId();
       } else {
          return null;
       }
    }

    /**
     * Sets the value of the <code>updatedBy</code> property.
     *
     * @param updatedBy the value for the <code>updatedBy</code> property
 																																																																															    */
    
    @javax.persistence.Transient
    public void setUpdatedBy(Long appUserId) {
       if(appUserId == null)
       {      
    	   this.updatedByAppUserModel = null;
       }
       else
       {
         this.updatedByAppUserModel = new AppUserModel();
       	this.updatedByAppUserModel.setAppUserId(appUserId);
       }      
    }

    /**
     * Returns the value of the <code>createdBy</code> property.
     *
     */
    @javax.persistence.Transient
    public Long getCreatedBy() {
       if (this.createdByAppUserModel != null) {
          return this.createdByAppUserModel.getAppUserId();
       } else {
          return null;
       }
    }

    /**
     * Sets the value of the <code>createdBy</code> property.
     *
     * @param createdBy the value for the <code>createdBy</code> property
 																																																																															    */
    
    @javax.persistence.Transient
    public void setCreatedBy(Long appUserId) {
       if(appUserId == null)
       {      
    	   this.createdByAppUserModel = null;
       }
       else
       {
         this.createdByAppUserModel = new AppUserModel();
         this.createdByAppUserModel.setAppUserId(appUserId);
       }      
    }
    
        
    /**
     * Returns the value of the <code>intgTransactionTypeId</code> property.
     *
     */
    @javax.persistence.Transient
    public Long getIntgTransactionTypeId() {
       if (intgTransactionType != null) {
          return intgTransactionType.getIntgTransactionTypeId();
       } else {
          return null;
       }
    }
    
    /**
     * Sets the value of the <code>intgTransactionTypeId</code> property.
     *
     * @param intgTransactionTypeId the value for the <code>intgTransactionTypeId</code> property
     * 
    */
     
    @javax.persistence.Transient
	public void setIntgTransactionTypeId(Long intgTransactionTypeId) 
	{
		if(intgTransactionTypeId == null)
		{      
			this.intgTransactionType = null;
		}
		else
		{
			intgTransactionType = new IntgTransactionTypeModel(); 
			intgTransactionType.setIntgTransactionTypeId(intgTransactionTypeId);
		}      
   }
    
    
    /**
     * Returns the value of the <code>transactionCodeId</code> property.
     *
     */
    @Column( name = "TRANSACTION_CODE_ID")
    public Long getTransactionCodeId() {
        return transactionCodeId;
    }
    
    /**
     * Sets the value of the <code>transactionCodeId</code> property.
     *
     * @param transactionCodeId the value for the <code>transactionCodeId</code> property
     * 
    */
     
	public void setTransactionCodeId(Long transactionCodeId)
	{
        this.transactionCodeId = transactionCodeId;
    }
    
    
    /**
     * Returns the value of the <code>productId</code> property.
     *
     */
    @javax.persistence.Transient
    public Long getProductId() {
       if (product != null) {
          return product.getProductId();
       } else {
          return null;
       }
    }
    
    /**
     * Sets the value of the <code>productId</code> property.
     *
     * @param productId the value for the <code>productId</code> property
     * 
    */
     
    @javax.persistence.Transient
	public void setProductId(Long productId) 
	{
		if(productId == null)
		{      
			this.product = null;
		}
		else
		{
			product = new ProductModel();
			product.setProductId(productId);
		}      
   }
    
	@Override
	@javax.persistence.Transient
	public Long getPrimaryKey() {
		return getPostedTransactionReportId();
	}

	@Override
	@javax.persistence.Transient
	public String getPrimaryKeyFieldName() {
		return "postedTransactionReportId";
	}

	@Override
	@javax.persistence.Transient
	public String getPrimaryKeyParameter() {
	    String parameter = "&postedTransactionReportId=" + getPostedTransactionReportId();
        return parameter;
	}

	@Override
	@javax.persistence.Transient
	public void setPrimaryKey(Long primaryKey) {
		setPostedTransactionReportId( primaryKey );
	}
	
	@Column(name="STAN")
    public String getSystemTraceAuditNumber() {
		return systemTraceAuditNumber;
	}

	public void setSystemTraceAuditNumber(String systemTraceAuditNumber) {
		this.systemTraceAuditNumber = systemTraceAuditNumber;
	}

}
