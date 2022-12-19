package com.inov8.microbank.common.model;

import com.inov8.framework.common.model.AssociationModel;
import com.inov8.framework.common.model.BasePersistableModel;

import javax.persistence.*;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by Muhammad Sajid on 12/8/2016.
 */

@Entity
@org.hibernate.annotations.Entity(dynamicInsert = true)
@javax.persistence.SequenceGenerator(name = "REGISTERED_CONSUMER_SEQ",sequenceName = "REGISTERED_CONSUMER_SEQ",allocationSize=1)
@Table(name = "REGISTERED_CONSUMER")
public class RegisteredConsumerModel extends BasePersistableModel implements Serializable {


    private AppUserModel createdByAppUserModel;
    private AppUserModel updatedByAppUserModel;
    private CustomerModel customerIdCustomerModel;
//    private ProductModel productIdProductModel;

    private Long   registeredConsumerId;
    private String registeredConsumerNo;
//    private String registeredConsumerName;

    private Date createdOn;
    private Date updatedOn;
    private Integer versionNo;
    private String comments;
    private Boolean isActive;
    private String companyId;
    private String companyCategory;
    private String nickName;

    @Column(name = "COMPANY_ID")
    public String getCompanyId() {
        return companyId;
    }

    public void setCompanyId(String companyId) {
        this.companyId = companyId;
    }

    @Column(name = "COMPANY_CATEGORY")
    public String getCompanyCategory() {
        return companyCategory;
    }

    public void setCompanyCategory(String companyCategory) {
        this.companyCategory = companyCategory;
    }

    @Column(name = "NICK_NAME")
    public String getNickName() {
        return nickName;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
    }
    /**
     * Default constructor.
     */
    public RegisteredConsumerModel() {
    }

    /**
     * Return the primary key.
     *
     * @return Long with the primary key.
     */
    @javax.persistence.Transient
    public Long getPrimaryKey() {
        return getRegisteredConsumerId();
    }

    /**
     * Set the primary key.
     *
     * @param primaryKey the primary key
     */
    @javax.persistence.Transient
    public void setPrimaryKey(Long primaryKey) {
        setRegisteredConsumerId(primaryKey);
    }

    /**
     * Returns the value of the <code>registeredConsumerId</code> property.
     *
     */
    @Column(name = "REGISTERED_CONSUMER_ID" , nullable = false )
    @Id
    @GeneratedValue(strategy= GenerationType.SEQUENCE, generator="REGISTERED_CONSUMER_SEQ")
    public Long getRegisteredConsumerId() {
        return registeredConsumerId;
    }

    /**
     * Sets the value of the <code>registeredConsumerId</code> property.
     *
     * @param registeredConsumerId the value for the <code>registeredConsumerId</code> property
     *
     */

    public void setRegisteredConsumerId(Long registeredConsumerId) {
        this.registeredConsumerId = registeredConsumerId;
    }


    /**
     * Helper method for Struts with displaytag
     */
    @javax.persistence.Transient
    public String getPrimaryKeyParameter() {
        String parameters = "";
        parameters += "&registeredConsumerId=" + getRegisteredConsumerId();
        return parameters;
    }
    /**
     * Helper method for default Sorting on Primary Keys
     */
    @javax.persistence.Transient
    public String getPrimaryKeyFieldName()
    {
        String primaryKeyFieldName = "registeredConsumerId";
        return primaryKeyFieldName;
    }


    /**
     * Returns the value of the <code>registeredConsumerNo</code> property.
     *
     */
    @Column(name = "REGISTERED_CONSUMER_NO" , nullable = false , length=100 )
    public String getRegisteredConsumerNo() {
        return registeredConsumerNo;
    }

    public void setRegisteredConsumerNo(String registeredConsumerNo) {
        this.registeredConsumerNo = registeredConsumerNo;
    }
    /**
     * Returns the value of the <code>registeredConsumerName</code> property.
     *
     */
    /*@Column(name = "REGISTERED_CONSUMER_NAME" , nullable = true , length=100 )
    public String getRegisteredConsumerName() {
        return registeredConsumerName;
    }

    public void setRegisteredConsumerName(String registeredConsumerName) {
        this.registeredConsumerName = registeredConsumerName;
    }*/

    /**
     * Returns the value of the <code>createdOn</code> property.
     *
     */
    @Column(name = "CREATED_ON" , nullable = false )
    public Date getCreatedOn() {
        return createdOn;
    }

    /**
     * Sets the value of the <code>createdOn</code> property.
     *
     * @param createdOn the value for the <code>createdOn</code> property
     *
     */

    public void setCreatedOn(Date createdOn) {
        this.createdOn = createdOn;
    }

    /**
     * Returns the value of the <code>updatedOn</code> property.
     *
     */
    @Column(name = "UPDATED_ON" , nullable = false )
    public Date getUpdatedOn() {
        return updatedOn;
    }

    /**
     * Sets the value of the <code>updatedOn</code> property.
     *
     * @param updatedOn the value for the <code>updatedOn</code> property
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


    /**
     * Returns the value of the <code>customerIdCustomerModel</code> relation property.
     *
     * @return the value of the <code>customerIdCustomerModel</code> relation property.
     *
     */
    @ManyToOne(cascade = CascadeType.REFRESH, fetch = FetchType.LAZY)
    @JoinColumn(name = "CUSTOMER_ID")
    public CustomerModel getRelationCustomerIdCustomerModel(){
        return customerIdCustomerModel;
    }

    /**
     * Returns the value of the <code>customerIdCustomerModel</code> relation property.
     *
     * @return the value of the <code>customerIdCustomerModel</code> relation property.
     *
     */
    @javax.persistence.Transient
    public CustomerModel getCustomerIdCustomerModel(){
        return getRelationCustomerIdCustomerModel();
    }

    /**
     * Sets the value of the <code>customerIdCustomerModel</code> relation property.
     *
     * @param customerModel a value for <code>customerIdCustomerModel</code>.
     */
    @javax.persistence.Transient
    public void setRelationCustomerIdCustomerModel(CustomerModel customerModel) {
        this.customerIdCustomerModel = customerModel;
    }

    /**
     * Sets the value of the <code>customerIdCustomerModel</code> relation property.
     *
     * @param customerModel a value for <code>customerIdCustomerModel</code>.
     */
    @javax.persistence.Transient
    public void setCustomerIdCustomerModel(CustomerModel customerModel) {
        if(null != customerModel)
        {
            setRelationCustomerIdCustomerModel((CustomerModel)customerModel.clone());
        }
    }

    /**
     * Returns the value of the <code>productIdProductModel</code> relation property.
     *
     * @return the value of the <code>productIdProductModel</code> relation property.
     *
     */
/*    @ManyToOne(cascade = CascadeType.REFRESH, fetch = FetchType.LAZY)
    @JoinColumn(name = "PRODUCT_ID")
    public ProductModel getRelationProductIdProductModel(){
        return productIdProductModel;
    }*/

    /**
     * Returns the value of the <code>productIdProductModel</code> relation property.
     *
     * @return the value of the <code>productIdProductModel</code> relation property.
     *
     */
    /*@javax.persistence.Transient
    public ProductModel getProductIdProductModel(){
        return getRelationProductIdProductModel();
    }
*/
    /**
     * Sets the value of the <code>productIdProductModel</code> relation property.
     *
     * @param productModel a value for <code>productIdProductModel</code>.
     */
    /*@javax.persistence.Transient
    public void setRelationProductIdProductModel(ProductModel productModel) {
        this.productIdProductModel = productModel;
    }*/

    /**
     * Sets the value of the <code>productIdProductModel</code> relation property.
     *
     * @param productModel a value for <code>productIdProductModel</code>.
     */
    /*@javax.persistence.Transient
    public void setProductIdProductModel(ProductModel productModel) {
        if(null != productModel)
        {
            setRelationProductIdProductModel((ProductModel)productModel.clone());
        }
    }*/

    /**
     * Returns the value of the <code>productId</code> property.
     *
     */
   /* @javax.persistence.Transient
    public Long getProductId() {
        if (productIdProductModel != null) {
            return productIdProductModel.getProductId();
        } else {
            return null;
        }
    }*/

    /**
     * Sets the value of the <code>productId</code> property.
     *
     * @param productId the value for the <code>productId</code> property
     * @spring.validator type="required"
     */

    /*@javax.persistence.Transient
    public void setProductId(Long productId) {
        if(productId == null)
        {
            productIdProductModel = null;
        }
        else
        {
            productIdProductModel = new ProductModel();
            productIdProductModel.setProductId(productId);
        }
    }
*/


    /**
     * Returns the value of the <code>createdByAppUserModel</code> relation property.
     *
     * @return the value of the <code>createdByAppUserModel</code> relation property.
     *
     */
    @ManyToOne(cascade = CascadeType.REFRESH, fetch = FetchType.LAZY)
    @JoinColumn(name = "CREATED_BY")
    public AppUserModel getRelationCreatedByAppUserModel(){
        return createdByAppUserModel;
    }

    /**
     * Returns the value of the <code>createdByAppUserModel</code> relation property.
     *
     * @return the value of the <code>createdByAppUserModel</code> relation property.
     *
     */
    @javax.persistence.Transient
    public AppUserModel getCreatedByAppUserModel(){
        return getRelationCreatedByAppUserModel();
    }

    /**
     * Sets the value of the <code>createdByAppUserModel</code> relation property.
     *
     * @param appUserModel a value for <code>createdByAppUserModel</code>.
     */
    @javax.persistence.Transient
    public void setRelationCreatedByAppUserModel(AppUserModel appUserModel) {
        this.createdByAppUserModel = appUserModel;
    }

    /**
     * Sets the value of the <code>createdByAppUserModel</code> relation property.
     *
     * @param appUserModel a value for <code>createdByAppUserModel</code>.
     */
    @javax.persistence.Transient
    public void setCreatedByAppUserModel(AppUserModel appUserModel) {
        if(null != appUserModel)
        {
            setRelationCreatedByAppUserModel((AppUserModel)appUserModel.clone());
        }
    }


    /**
     * Returns the value of the <code>updatedByAppUserModel</code> relation property.
     *
     * @return the value of the <code>updatedByAppUserModel</code> relation property.
     *
     */
    @ManyToOne(cascade = CascadeType.REFRESH, fetch = FetchType.LAZY)
    @JoinColumn(name = "UPDATED_BY")
    public AppUserModel getRelationUpdatedByAppUserModel(){
        return updatedByAppUserModel;
    }

    /**
     * Returns the value of the <code>updatedByAppUserModel</code> relation property.
     *
     * @return the value of the <code>updatedByAppUserModel</code> relation property.
     *
     */
    @javax.persistence.Transient
    public AppUserModel getUpdatedByAppUserModel(){
        return getRelationUpdatedByAppUserModel();
    }

    /**
     * Sets the value of the <code>updatedByAppUserModel</code> relation property.
     *
     * @param appUserModel a value for <code>updatedByAppUserModel</code>.
     */
    @javax.persistence.Transient
    public void setRelationUpdatedByAppUserModel(AppUserModel appUserModel) {
        this.updatedByAppUserModel = appUserModel;
    }

    /**
     * Sets the value of the <code>updatedByAppUserModel</code> relation property.
     *
     * @param appUserModel a value for <code>updatedByAppUserModel</code>.
     */
    @javax.persistence.Transient
    public void setUpdatedByAppUserModel(AppUserModel appUserModel) {
        if(null != appUserModel)
        {
            setRelationUpdatedByAppUserModel((AppUserModel)appUserModel.clone());
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

    /**
     * Returns the value of the <code>customerId</code> property.
     *
     */
    @javax.persistence.Transient
    public Long getCustomerId() {
        if (customerIdCustomerModel != null) {
            return customerIdCustomerModel.getCustomerId();
        } else {
            return null;
        }
    }

    /**
     * Sets the value of the <code>customerId</code> property.
     *
     * @param customerId the value for the <code>customerId</code> property
     * @spring.validator type="required"
     */

    @javax.persistence.Transient
    public void setCustomerId(Long customerId) {
        if(customerId == null)
        {
            customerIdCustomerModel = null;
        }
        else
        {
            customerIdCustomerModel = new CustomerModel();
            customerIdCustomerModel.setCustomerId(customerId);
        }
    }

    /**
     * Helper method for Complex Example Queries
     */
    @javax.persistence.Transient
    @Override
    public List<AssociationModel> getAssociationModelList()
    {
        List<AssociationModel> associationModelList = new ArrayList<AssociationModel>();
        AssociationModel associationModel = null;


        associationModel = new AssociationModel();

        associationModel.setClassName("CustomerModel");
        associationModel.setPropertyName("relationCustomerIdCustomerModel");
        associationModel.setValue(getRelationCustomerIdCustomerModel());

        associationModelList.add(associationModel);


        /*associationModel = new AssociationModel();

        associationModel.setClassName("ProductModel");
        associationModel.setPropertyName("relationProductIdProductModel");
        associationModel.setValue(getRelationProductIdProductModel());*/

       // associationModelList.add(associationModel);

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


    @Column(name = "COMMENTS")
    public String getComments()
    {
        return comments;
    }

    public void setComments( String comments )
    {
        this.comments = comments;
    }

    @Column(name = "IS_ACTIVE")
    public Boolean getActive() {
        return isActive;
    }

    public void setActive(Boolean active) {
        isActive = active;
    }
}
