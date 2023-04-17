package com.inov8.microbank.common.model.customermodule;

import com.inov8.framework.common.model.AssociationModel;
import com.inov8.framework.common.model.BasePersistableModel;
import com.inov8.microbank.common.model.CustomerModel;

import javax.persistence.*;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Entity
@org.hibernate.annotations.Entity(dynamicInsert = true)
@SequenceGenerator(name = "MERCHAN_ACCOUNT_PIC_SEQ", sequenceName = "MERCHAN_ACCOUNT_PIC_SEQ", allocationSize = 2)
@Table(name = " MERCHAN_ACCOUNT_PICTURE")
public class MerchantAccountPictureModel extends BasePersistableModel implements
        Serializable {

    private static final long serialVersionUID = -6499330678059839685L;

    private Long customerPictureId;
    private Long applicantTypeId;
    private PictureTypeModel pictureTypeModel;
    private CustomerModel customerModel;
    // private String picture;
    private byte[] picture;
    private Long createdBy;
    private Long updatedBy;
    private Date createdOn;
    private Date updatedOn;
    private Long versionNo;
    private Long	applicantDetailId;

    public MerchantAccountPictureModel() {
    }

    @Override
    @Transient
    public Long getPrimaryKey() {
        return getCustomerPictureId();
    }

    @Override
    @Transient
    public String getPrimaryKeyFieldName() {
        return "customerPictureId";
    }

    @Override
    @Transient
    public String getPrimaryKeyParameter() {
        return "&customerPictureId=" + getCustomerPictureId();
    }

    @Override
    public void setPrimaryKey(Long primaryKey) {
        setCustomerPictureId(primaryKey);
    }

    @Override
    @Transient
    public List<AssociationModel> getAssociationModelList() {
        List<AssociationModel> associationModelList = new ArrayList<AssociationModel>();
        AssociationModel associationModel = null;

        associationModel = new AssociationModel();

        associationModel.setClassName("CustomerModel");
        associationModel.setPropertyName("relationCustomerModel");
        associationModel.setValue(getRelationCustomerModel());

        associationModel = new AssociationModel();

        associationModel.setClassName("PictureTypeModel");
        associationModel.setPropertyName("relationPictureTypeModel");
        associationModel.setValue(getRelationPictureTypeModel());

        associationModelList.add(associationModel);

        return associationModelList;
    }

    // Property accessors
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "MERCHAN_ACCOUNT_PIC_SEQ")
    @Column(name = "MERCHAN_ACCOUNT_PICTURE_ID")
    public Long getCustomerPictureId() {
        return this.customerPictureId;
    }

    public void setCustomerPictureId(Long customerPictureId) {
        this.customerPictureId = customerPictureId;
    }

    // ------------------------------------------------------------------
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "PICTURE_TYPE_ID")
    public PictureTypeModel getRelationPictureTypeModel() {
        return pictureTypeModel;
    }

    @Transient
    public void setRelationPictureTypeModel(PictureTypeModel pictureTypeModel) {
        this.pictureTypeModel = pictureTypeModel;
    }

    @Transient
    public PictureTypeModel getPictureTypeModel() {
        return pictureTypeModel;
    }

    @Transient
    public void setPictureTypeModel(PictureTypeModel pictureTypeModel) {
        this.pictureTypeModel = pictureTypeModel;
    }

    @Transient
    public Long getPictureTypeId() {
        if (pictureTypeModel != null) {
            return pictureTypeModel.getPictureTypeId();
        } else {
            return null;
        }
    }

    @Transient
    public void setPictureTypeId(Long pictureTypeId) {
        if (pictureTypeId == null) {
            this.pictureTypeModel = null;
        } else {
            pictureTypeModel = new PictureTypeModel();
            pictureTypeModel.setPictureTypeId(pictureTypeId);
        }
    }

    // ------------------------------------------------------------------
    @ManyToOne(cascade = CascadeType.REFRESH, fetch = FetchType.LAZY)
    @JoinColumn(name = "CUSTOMER_ID")
    public CustomerModel getRelationCustomerModel() {
        return customerModel;
    }

    @Transient
    public void setRelationCustomerModel(CustomerModel customerModel) {
        this.customerModel = customerModel;
    }

    @Transient
    public CustomerModel getCustomerModel() {
        return customerModel;
    }

    @Transient
    public void setCustomerModel(CustomerModel customerModel) {
        this.customerModel = customerModel;
    }

    @Transient
    public Long getCustomerId() {
        if (customerModel != null) {
            return customerModel.getCustomerId();
        } else {
            return null;
        }
    }

    @Transient
    public void setCustomerId(Long customerId) {
        if (customerId == null) {
            this.customerModel = null;
        } else {
            customerModel = new CustomerModel();
            customerModel.setCustomerId(customerId);
        }
    }

    // ------------------------------------------------------------------

    @Column(name = "PICTURE")
    public byte[] getPicture() {
        return this.picture;
    }

    public void setPicture(byte[] picture) {
        this.picture = picture;
    }

    @Column(name = "CREATED_BY")
    public Long getCreatedBy() {
        return this.createdBy;
    }

    public void setCreatedBy(Long createdBy) {
        this.createdBy = createdBy;
    }

    @Column(name = "UPDATED_BY")
    public Long getUpdatedBy() {
        return this.updatedBy;
    }

    public void setUpdatedBy(Long updatedBy) {
        this.updatedBy = updatedBy;
    }

    @Column(name = "CREATED_ON")
    public Date getCreatedOn() {
        return this.createdOn;
    }

    public void setCreatedOn(Date createdOn) {
        this.createdOn = createdOn;
    }

    @Column(name = "UPDATED_ON")
    public Date getUpdatedOn() {
        return this.updatedOn;
    }

    public void setUpdatedOn(Date updatedOn) {
        this.updatedOn = updatedOn;
    }

    @Version
    @Column(name = "VERSION_NO")
    public Long getVersionNo() {
        return this.versionNo;
    }

    public void setVersionNo(Long versionNo) {
        this.versionNo = versionNo;
    }

    @Column(name = "APPLICANT_TYPE_ID")
    public Long getApplicantTypeId() {
        return applicantTypeId;
    }

    public void setApplicantTypeId(Long applicantTypeId) {
        this.applicantTypeId = applicantTypeId;
    }

//    @Column(name = "IS_DISCREPANT")
//    public Boolean getDiscrepant() {
//        return discrepant;
//    }
//
//    public void setDiscrepant(Boolean discrepant) {
//        this.discrepant = discrepant;
//    }

    @Column(name = "APPLICANT_DETAIL_ID")
    public Long getApplicantDetailId() {
        return applicantDetailId;
    }

    public void setApplicantDetailId(Long applicantDetailId) {
        this.applicantDetailId = applicantDetailId;
    }
}
