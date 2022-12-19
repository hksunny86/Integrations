package com.inov8.microbank.updatecustomername.model;


import com.inov8.framework.common.model.AssociationModel;
import com.inov8.framework.common.model.BasePersistableModel;
import com.inov8.microbank.common.model.ActionStatusModel;
import com.sun.org.apache.xpath.internal.operations.Bool;

import javax.persistence.*;
import javax.xml.bind.annotation.XmlElement;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Entity
@org.hibernate.annotations.Entity(dynamicInsert = true)
@javax.persistence.SequenceGenerator(name = "UPDATE_CUSTOMER_NAME_seq",sequenceName = "UPDATE_CUSTOMER_NAME_seq", allocationSize=1)
@Table(name = "UPDATE_CUSTOMER_NAME")
public class UpdateCustomerNameModel extends BasePersistableModel implements Serializable {

    private Long updateCustomerNameId;
    private String cnic;
    private String mobileNo;
    private String firstName;
    private String lastName;
    private Boolean updated;
    private Date createdOn;
    private Date updatedOn;
    private Long creadtedBy;
    private Long updatedBy;
    private String nadraName;
    private Date start;
    private Date end;

    @XmlElement
    private ActionStatusModel actionStatusIdActionStatusModel;



    @Column(name = "UPDATE_CUSTOMER_NAME_ID" )
    @Id
    @GeneratedValue(strategy=GenerationType.SEQUENCE, generator="UPDATE_CUSTOMER_NAME_seq")
    public Long getUpdateCustomerNameId() {
        return updateCustomerNameId;
    }

    public void setUpdateCustomerNameId(Long updateCustomerNameId) {
        this.updateCustomerNameId = updateCustomerNameId;
    }

    @Column(name = "CNIC" )
    public String getCnic() {
        return cnic;
    }

    public void setCnic(String cnic) {
        this.cnic = cnic;
    }
    @Column(name = "MOBILE_NO")
    public String getMobileNo() {
        return mobileNo;
    }

    public void setMobileNo(String mobileNo) {
        this.mobileNo = mobileNo;
    }
    @Column(name = "FIRST_NAME")
    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }
    @Column(name = "LAST_NAME")
    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    @Column(name = "IS_UPDATED")
    public Boolean getUpdated() {
        return updated;
    }

    public void setUpdated(Boolean updated) {
        this.updated = updated;
    }

    @Column(name = "CREATED_ON")
    public Date getCreatedOn() {
        return createdOn;
    }

    public void setCreatedOn(Date createdOn) {
        this.createdOn = createdOn;
    }

    @Column(name = "UPDATED_ON")
    public Date getUpdatedOn() {
        return updatedOn;
    }

    public void setUpdatedOn(Date updatedOn) {
        this.updatedOn = updatedOn;
    }

    @Column(name = "CREATED_BY")
    public Long getCreadtedBy() {
        return creadtedBy;
    }

    public void setCreadtedBy(Long creadtedBy) {
        this.creadtedBy = creadtedBy;
    }

    @Column(name = "UPDATED_BY")
    public Long getUpdatedBy() {
        return updatedBy;
    }

    public void setUpdatedBy(Long updatedBy) {
        this.updatedBy = updatedBy;
    }

    @Column(name = "NADRA_NAME" )
    public String getNadraName() {
        return nadraName;
    }

    public void setNadraName(String nadraName) {
        this.nadraName = nadraName;
    }

    @javax.persistence.Transient
    public void setPrimaryKey(Long aLong) {
        setUpdateCustomerNameId(aLong);
    }

    @javax.persistence.Transient
    public Long getPrimaryKey() {
        return getUpdateCustomerNameId();
    }

    @javax.persistence.Transient
    public String getPrimaryKeyParameter() {
        String parameters = "";
        parameters += "&updateCustomerNameId=" + getUpdateCustomerNameId();
        return parameters;
    }

    @javax.persistence.Transient
    public String getPrimaryKeyFieldName()
    {
        String primaryKeyFieldName = "updateCustomerNameId";
        return primaryKeyFieldName;
    }

    @ManyToOne(cascade = CascadeType.REFRESH, fetch = FetchType.LAZY)
    @JoinColumn(name = "ACTION_STATUS_ID")
    public ActionStatusModel getRelationActionStatusIdActionStatusModel() {
        return actionStatusIdActionStatusModel;
    }

    @javax.persistence.Transient
    public ActionStatusModel getActionStatusIdActionStatusModel() {
        return getRelationActionStatusIdActionStatusModel();
    }

    @javax.persistence.Transient
    public void setRelationActionStatusIdActionStatusModel(ActionStatusModel actionStatusModel) {
        this.actionStatusIdActionStatusModel = actionStatusModel;
    }

    @javax.persistence.Transient
    public void setActionStatusIdActionStatusModel(ActionStatusModel actionStatusModel) {
        if (null != actionStatusModel) {
            setRelationActionStatusIdActionStatusModel((ActionStatusModel) actionStatusModel.clone());
        }
    }

    @javax.persistence.Transient
    public Long getActionStatusId() {
        if (actionStatusIdActionStatusModel != null) {
            return actionStatusIdActionStatusModel.getActionStatusId();
        } else {
            return null;
        }
    }


    @javax.persistence.Transient
    public void setActionStatusId(Long actionStatusId) {
        if (actionStatusId == null) {
            actionStatusIdActionStatusModel = null;
        } else {
            actionStatusIdActionStatusModel = new ActionStatusModel();
            actionStatusIdActionStatusModel.setActionStatusId(actionStatusId);
        }
    }

    @Override
    @Transient
    public List<AssociationModel> getAssociationModelList() {
        List<AssociationModel> associationModelList = new ArrayList<AssociationModel>();
        AssociationModel associationModel = null;
        associationModel = new AssociationModel();
        associationModel.setClassName("ActionStatusModel");
        associationModel.setPropertyName("relationActionStatusIdActionStatusModel");
        associationModel.setValue(getRelationActionStatusIdActionStatusModel());

       /* associationModel = new AssociationModel();

        associationModel.setClassName("BlinkCustomerRegistrationStateModel");
        associationModel.setPropertyName("relationBlinkRegistrationStateIdBlinkCustomerRegistrationStateModel");
        associationModel.setValue(getRelationBlinkRegistrationStateIdBlinkCustomerRegistrationStateModel());
*/
        associationModelList.add(associationModel);
        return associationModelList;
    }

    @Transient
    public Date getStart() {
        return start;
    }

    public void setStart(Date start) {
        this.start = start;
    }

    @Transient
    public Date getEnd() {
        return end;
    }

    public void setEnd(Date end) {
        this.end = end;
    }


}
