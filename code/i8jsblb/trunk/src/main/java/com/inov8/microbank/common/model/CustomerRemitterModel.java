package com.inov8.microbank.common.model;
import com.inov8.framework.common.model.AssociationModel;
import com.inov8.framework.common.model.BasePersistableModel;

import javax.persistence.*;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Entity
@org.hibernate.annotations.Entity(dynamicInsert = true)
@javax.persistence.SequenceGenerator(name = "CUSTOMER_REMITTER_SEQ",sequenceName = "CUSTOMER_REMITTER_SEQ", allocationSize=1)
@Table(name = "CUSTOMER_REMMITER")
public class CustomerRemitterModel extends BasePersistableModel implements Serializable
{
    public CustomerRemitterModel(){

    }
    private Long customerRemitterId;
    //private Long customerId;
    private String remittanceLocation;
    private String relationship;
    private Long isActive;
    private CustomerModel customerIdCustomerModel;

    @Column(name = "CUSTOMER_REMITTER_ID" , nullable = false )
    @Id @GeneratedValue(strategy= GenerationType.SEQUENCE, generator="CUSTOMER_REMITTER_SEQ")
    public Long getCustomerRemitterId() {
        return customerRemitterId;
    }

    public void setCustomerRemitterId(Long customerRemitterId) {
        this.customerRemitterId = customerRemitterId;
    }
    /**
     * Returns the value of the <code>customerIdCustomerModel</code> relation property.
     *
     * @return the value of the <code>customerIdCustomerModel</code> relation property.
     */
    @ManyToOne(cascade = CascadeType.REFRESH, fetch = FetchType.LAZY)
    @JoinColumn(name = "CUSTOMER_ID")
    public CustomerModel getRelationCustomerIdCustomerModel() {
        return customerIdCustomerModel;
    }

    /**
     * Returns the value of the <code>customerIdCustomerModel</code> relation property.
     *
     * @return the value of the <code>customerIdCustomerModel</code> relation property.
     */
    @javax.persistence.Transient
    public CustomerModel getCustomerIdCustomerModel() {
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
        if (null != customerModel) {
            setRelationCustomerIdCustomerModel((CustomerModel) customerModel.clone());
        }
    }

    /**
     * Returns the value of the <code>customerId</code> property.
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
     */

    @javax.persistence.Transient
    public void setCustomerId(Long customerId) {
        if (customerId == null) {
            customerIdCustomerModel = null;
        } else {
            customerIdCustomerModel = new CustomerModel();
            customerIdCustomerModel.setCustomerId(customerId);
        }
    }

    @Column(name = "REMITTANCE_LOCATION"  , length=50 )
    public String getRemittanceLocation() {
        return remittanceLocation;
    }

    public void setRemittanceLocation(String remittanceLocation) {
        this.remittanceLocation = remittanceLocation;
    }

    @Column(name = "RELATIONSHIP"  , length=50 )
    public String getRelationship() {
        return relationship;
    }

    public void setRelationship(String relationship) {
        this.relationship = relationship;
    }

    @javax.persistence.Transient
    public void setPrimaryKey(Long customerRemitterId) {
        setCustomerRemitterId(customerRemitterId);
    }

    @javax.persistence.Transient
    public Long getPrimaryKey() {
        return getCustomerRemitterId();
    }

    @javax.persistence.Transient
    public String getPrimaryKeyParameter() {
        String parameters = "";
        parameters += "&customerRemitterId=" + getCustomerRemitterId();
        return parameters;
    }

    @javax.persistence.Transient
    public String getPrimaryKeyFieldName() {
        String primaryKeyFieldName = "customerRemitterId";
        return primaryKeyFieldName;
    }

    @javax.persistence.Transient
    @Override
    public List<AssociationModel> getAssociationModelList() {
        List<AssociationModel> associationModelList = new ArrayList<AssociationModel>();
        AssociationModel associationModel = null;

        associationModel = new AssociationModel();
        associationModel.setClassName("CustomerModel");
        associationModel.setPropertyName("relationCustomerIdCustomerModel");
        associationModel.setValue(getRelationCustomerIdCustomerModel());

        associationModelList.add(associationModel);

        return associationModelList;
    }

    @Column(name = "IS_ACTIVE")
    public Long getIsActive() {
        return isActive;
    }

    public void setIsActive(Long isActive) {
        this.isActive = isActive;
    }
}
