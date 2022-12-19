package com.inov8.microbank.common.model.portal.mfsaccountmodule;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

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
import javax.persistence.Transient;
import javax.persistence.Version;

import org.springframework.web.multipart.MultipartFile;

import com.inov8.framework.common.model.AssociationModel;
import com.inov8.framework.common.model.BasePersistableModel;
import com.inov8.microbank.common.model.AppUserModel;
import com.inov8.microbank.common.model.RetailerContactModel;

@Entity
@org.hibernate.annotations.Entity(dynamicInsert = true)
@javax.persistence.SequenceGenerator(name = "APPLICANT_DETAIL_seq",sequenceName = "APPLICANT_DETAIL_seq", allocationSize=1)
@Table(name = "APPLICANT_DETAIL")

public class ApplicantDetailModel extends BasePersistableModel implements Serializable{
	private static final long serialVersionUID = -3267147436283807823L;
	private Long applicantDetailId;
	private Long applicantTypeId;
	public String name;
    public String ntn;
    private MultipartFile acOpeningPic;
    private MultipartFile fatcaFormPic;
    private MultipartFile idFrontPic;
    private MultipartFile idBackPic;
    private MultipartFile usCitizenPic;
    private byte[] acOpeningPicByte;
    private byte[] fatcaFormPicByte;
    private byte[] idFrontPicByte;
    private byte[] idBackPicByte;
    private byte[] usCitizenPicByte;
    private Long idType;
    private String idTypeName;
    public String idNumber;
    private Date idExpiryDate;
    private String fatherHusbandName;
    private String employerName;
    private Date visaExpiryDate;
    private Long title;
    private String titleTxt;
    private String motherMaidenName;
    private Long residentialStatus;
    private Long maritalStatus;
    private String maritalStatusName;
    private Long mailingAddressType;
    private String nationality;
    private Boolean usCitizen;
    private Boolean jointAc;
    private Boolean eitherOnly;
    private Date dob;
    private String birthPlace;
    private String gender;
    private String email;
    private String landLineNo;
    private String contactNo;
    private String mobileNo;
    private String fax;
    private Long occupation;
    private Long profession;
    private String occupationName;
    private String professionName;
    private Long customerId;
    private RetailerContactModel retailerContactModel;
    private String buisnessAddress;
    private String residentialAddress;
    private Long buisnessCity;
    private Long residentialCity;
    private String buisnessCityName;
    private String residentialCityName;
    private AppUserModel createdByAppUserModel;
    private AppUserModel updatedByAppUserModel;
    private Date createdOn;
    private Date updatedOn;
    private Integer versionNo;
	private Boolean verisysDone;
	private Boolean screeningPerformed;
	private String birthPlaceName;
	private Long index;

    public ApplicantDetailModel() {
	}

	@Override
	public void setPrimaryKey(Long primaryKey) {
		setApplicantDetailId(primaryKey);
	}

	@Transient
	@Override
	public Long getPrimaryKey() {
		return getApplicantDetailId();
	}

	@Transient
	@Override
	public String getPrimaryKeyParameter() {
		return "&applicantDetailId="+getApplicantDetailId();
	}

	@Transient
	@Override
	public String getPrimaryKeyFieldName() {
		return "applicantDetailId";
	}

	@Transient
	@Override
	public List<AssociationModel> getAssociationModelList()
	{
		List<AssociationModel> associationModelList = new ArrayList<>();
		AssociationModel associationModel = new AssociationModel();
		associationModel.setClassName("RetailerContactModel");
		associationModel.setPropertyName("retailerContactModel");
		associationModel.setValue(getRetailerContactModel());
		associationModelList.add(associationModel);
		return associationModelList;
	}

	@Id
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="APPLICANT_DETAIL_seq")
	@Column(name = "APPLICANT_DETAIL_ID")
	public Long getApplicantDetailId() {
		return applicantDetailId;
	}

	public void setApplicantDetailId(Long applicantDetailId) {
		this.applicantDetailId = applicantDetailId;
	}

	@Column(name="APPLICANT_TYPE_ID")
	public Long getApplicantTypeId() {
		return applicantTypeId;
	}

	public void setApplicantTypeId(Long applicantTypeId) {
		this.applicantTypeId = applicantTypeId;
	}

	@Column(name="NAME")
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}

	@Column(name="NTN")
	public String getNtn() {
		return ntn;
	}
	public void setNtn(String ntn) {
		this.ntn = ntn;
	}

	@Column(name="FATHER_HUSBAND_NAME")
	public String getFatherHusbandName() {
		return fatherHusbandName;
	}
	public void setFatherHusbandName(String fatherHusbandName) {
		this.fatherHusbandName = fatherHusbandName;
	}

	@Column(name="DOB")
	public Date getDob() {
		return dob;
	}
	public void setDob(Date dob) {
		this.dob = dob;
	}

	@Column(name="BIRTH_PLACE")
	public String getBirthPlace() {
		return birthPlace;
	}
	public void setBirthPlace(String birthPlace) {
		this.birthPlace = birthPlace;
	}

	@Column(name="GENDER")
	public String getGender() {
		return gender;
	}
	public void setGender(String gender) {
		this.gender = gender;
	}

	@Column(name="EMAIL")
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}

	@Column(name="LANDLINE_NO")
	public String getLandLineNo() {
		return landLineNo;
	}
	public void setLandLineNo(String landLineNo) {
		this.landLineNo = landLineNo;
	}

	@Column(name="CONTACT_NO")
	public String getContactNo() {
		return contactNo;
	}
	public void setContactNo(String contactNo) {
		this.contactNo = contactNo;
	}

	@Column(name="FAX")
	public String getFax() {
		return fax;
	}
	public void setFax(String fax) {
		this.fax = fax;
	}

	@Transient
	public String getBuisnessAddress() {
		return buisnessAddress;
	}
	public void setBuisnessAddress(String buisnessAddress) {
		this.buisnessAddress = buisnessAddress;
	}

	@Column(name = "CUSTOMER_ID")
	public Long getCustomerId() {
		return customerId;
	}																																																									   
	  
	public void setCustomerId(Long customerId) {
		this.customerId = customerId;    
	}

	@ManyToOne(cascade = CascadeType.REFRESH, fetch = FetchType.LAZY)
	@JoinColumn(name = "RETAILER_CONTACT_ID")
	public RetailerContactModel getRetailerContactModel() {
		return retailerContactModel;
	}

	public void setRetailerContactModel(RetailerContactModel retailerContactModel) {
		this.retailerContactModel = retailerContactModel;
	}

	@Transient
	public Long getRetailerContactId(){
		if(retailerContactModel == null)
		{
			return null;
		}
		return retailerContactModel.getRetailerContactId();
	}

	public void setRetailerContactId(Long retailerContactId) {
		if (retailerContactId == null) {
			retailerContactModel = null;
		} else {
			retailerContactModel = new RetailerContactModel();
			retailerContactModel.setRetailerContactId(retailerContactId);
		}
	}

	@Column(name="OCCUPATION_ID")
	public Long getOccupation() {
		return occupation;
	}

	public void setOccupation(Long occupation) {
		this.occupation = occupation;
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

    @javax.persistence.Transient
    public Long getUpdatedBy() {
       if (updatedByAppUserModel != null) {
          return updatedByAppUserModel.getAppUserId();
       } else {
          return null;
       }
    }

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

    @javax.persistence.Transient
    public Long getCreatedBy() {
       if (createdByAppUserModel != null) {
          return createdByAppUserModel.getAppUserId();
       } else {
          return null;
       }
    }

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
    
    @Column(name="CREATED_ON")
    public Date getCreatedOn() {
        return this.createdOn;
    }
    
    public void setCreatedOn(Date createdOn) {
        this.createdOn = createdOn;
    }
    
    @Column(name="UPDATED_ON")
    public Date getUpdatedOn() {
        return this.updatedOn;
    }
    
    public void setUpdatedOn(Date updatedOn) {
        this.updatedOn = updatedOn;
    }
    
    @Version
    @Column(name="VERSION_NO")
    public Integer getVersionNo() {
        return this.versionNo;
    }
    
    public void setVersionNo(Integer versionNo) {
        this.versionNo = versionNo;
    }

    @Transient
	public MultipartFile getFatcaFormPic() {
		return fatcaFormPic;
	}

	public void setFatcaFormPic(MultipartFile fatcaFormPic) {
		if (fatcaFormPic != null) {
			this.fatcaFormPic = fatcaFormPic;
		}
	}

	@Transient
	public MultipartFile getIdFrontPic() {
		return idFrontPic;
	}

	public void setIdFrontPic(MultipartFile idFrontPic) {
		if (idFrontPic != null) {
			this.idFrontPic = idFrontPic;
		}
	}

	@Transient
	public MultipartFile getIdBackPic() {
		return idBackPic;
	}

	public void setIdBackPic(MultipartFile idBackPic) {
		if (idBackPic != null) {
			this.idBackPic = idBackPic;
		}
	}

	@Transient
	public byte[] getFatcaFormPicByte() {
		return fatcaFormPicByte;
	}

	public void setFatcaFormPicByte(byte[] fatcaFormPicByte) {
		if (fatcaFormPicByte != null) {
			this.fatcaFormPicByte = fatcaFormPicByte;
		}
	}

	@Transient
	public byte[] getIdFrontPicByte() {
		return idFrontPicByte;
	}

	public void setIdFrontPicByte(byte[] idFrontPicByte) {
		if (idFrontPicByte != null) {
			this.idFrontPicByte = idFrontPicByte;
		}
	}

	@Transient
	public byte[] getIdBackPicByte() {
		return idBackPicByte;
	}

	public void setIdBackPicByte(byte[] idBackPicByte) {
		if (idBackPicByte != null) {
			this.idBackPicByte = idBackPicByte;
		}
	}

	@Column(name="ID_DOCUMENT_NO")
	public String getIdNumber() {
		return idNumber;
	}

	public void setIdNumber(String idNumber) {
		if (idNumber != null) {
			this.idNumber = idNumber;
		}
	}

	@Column(name="ID_DOCUMENT_EXPIRY")
	public Date getIdExpiryDate() {
		return idExpiryDate;
	}

	public void setIdExpiryDate(Date idExpiryDate) {
		
			this.idExpiryDate = idExpiryDate;
		
	}

	@Column(name="EMPLOYER_NAME")
	public String getEmployerName() {
		return employerName;
	}

	public void setEmployerName(String employerName) {
		this.employerName = employerName;
	}

	@Column(name="DATE_OF_VISA_EXPIRY")
	public Date getVisaExpiryDate() {
		return visaExpiryDate;
	}

	public void setVisaExpiryDate(Date visaExpiryDate) {
		this.visaExpiryDate = visaExpiryDate;
	}

	@Column(name="TITLE")
	public Long getTitle() {
		return title;
	}

	public void setTitle(Long title) {
		this.title = title;
	}

	@Column(name="MOTHER_MAIDEN_NAME")
	public String getMotherMaidenName() {
		return motherMaidenName;
	}

	public void setMotherMaidenName(String motherMaidenName) {
		this.motherMaidenName = motherMaidenName;
	}

	@Column(name="RESIDENTIAL_STATUS")
	public Long getResidentialStatus() {
		return residentialStatus;
	}

	public void setResidentialStatus(Long residentialStatus) {
		this.residentialStatus = residentialStatus;
	}

	@Column(name="MARITAL_STATUS")
	public Long getMaritalStatus() {
		return maritalStatus;
	}

	public void setMaritalStatus(Long maritalStatus) {
		this.maritalStatus = maritalStatus;
	}

	@Column(name="MAILING_ADDRESS_TYPE")
	public Long getMailingAddressType() {
		return mailingAddressType;
	}

	public void setMailingAddressType(Long mailingAddressType) {
		this.mailingAddressType = mailingAddressType;
	}

	@Column(name="NATIONALITY")
	public String getNationality() {
		return nationality;
	}

	public void setNationality(String nationality) {
		this.nationality = nationality;
	}

	@Column(name="US_CITIZEN")
	public Boolean getUsCitizen() {
		return usCitizen;
	}

	public void setUsCitizen(Boolean usCitizen) {
		this.usCitizen = usCitizen;
	}

	@Column(name="JOINT_AC")
	public Boolean getJointAc() {
		return jointAc;
	}

	public void setJointAc(Boolean jointAc) {
		if (jointAc != null) {
			this.jointAc = jointAc;
		}
	}

	@Column(name="EITHER_ONLY")
	public Boolean getEitherOnly() {
		return eitherOnly;
	}

	public void setEitherOnly(Boolean eitherOnly) {
		if (eitherOnly != null) {
			this.eitherOnly = eitherOnly;
		}
	}

	@Column(name="MOBILE_NO")
	public String getMobileNo() {
		return mobileNo;
	}

	public void setMobileNo(String mobileNo) {
		if (mobileNo != null) {
			this.mobileNo = mobileNo;
		}
	}

	@Column(name="PROFESSION_ID")
	public Long getProfession() {
		return profession;
	}

	public void setProfession(Long profession) {
		this.profession = profession;
	}

	@Transient
	public MultipartFile getUsCitizenPic() {
		return usCitizenPic;
	}

	public void setUsCitizenPic(MultipartFile usCitizenPic) {
		this.usCitizenPic = usCitizenPic;
	}

	@Transient
	public byte[] getUsCitizenPicByte() {
		return usCitizenPicByte;
	}

	public void setUsCitizenPicByte(byte[] usCitizenPicByte) {
		this.usCitizenPicByte = usCitizenPicByte;
	}

	@Column(name="ID_DOCUMENT_TYPE")
	public Long getIdType() {
		return idType;
	}

	public void setIdType(Long idType) {
		if (idType != null) {
			this.idType = idType;
		}
	}

	@Transient
	public String getResidentialAddress() {
		return residentialAddress;
	}

	public void setResidentialAddress(String residentialAddress) {
		this.residentialAddress = residentialAddress;
	}	

	@Transient
	public MultipartFile getAcOpeningPic() {
		return acOpeningPic;
	}

	public void setAcOpeningPic(MultipartFile acOpeningPic) {
		if (acOpeningPic != null) {
			this.acOpeningPic = acOpeningPic;
		}
	}

	@Transient
	public byte[] getAcOpeningPicByte() {
		return acOpeningPicByte;
	}

	public void setAcOpeningPicByte(byte[] acOpeningPicByte) {
		if (acOpeningPicByte != null) {
			this.acOpeningPicByte = acOpeningPicByte;
		}
	}
	
	@Column(name = "VERISYS")
	public Boolean getVerisysDone() {
		return verisysDone;
	}

	public void setVerisysDone(Boolean verisysDone) {
		if (verisysDone != null) {
			this.verisysDone = verisysDone;
		}
	}

	@Column(name = "SCREENING")
	public Boolean getScreeningPerformed() {
		return screeningPerformed;
	}

	public void setScreeningPerformed(Boolean screeningPerformed) {
		if (screeningPerformed != null) {
			this.screeningPerformed = screeningPerformed;
		}
	}

	@Transient
	public Long getBuisnessCity() {
		return buisnessCity;
	}

	public void setBuisnessCity(Long buisnessCity) {
		
			this.buisnessCity = buisnessCity;
		
	}

	@Transient
	public Long getResidentialCity() {
		return residentialCity;
	}

	public void setResidentialCity(Long residentialCity) {
		this.residentialCity = residentialCity;
	}

	@Transient
	public String getBirthPlaceName() {
		return birthPlaceName;
	}

	public void setBirthPlaceName(String birthPlaceName) {
		this.birthPlaceName = birthPlaceName;
	}

	@Transient
	public String getTitleTxt() {
		return titleTxt;
	}

	public void setTitleTxt(String titleTxt) {
			this.titleTxt = titleTxt;
	}

	@Transient
	public String getIdTypeName() {
		return idTypeName;
	}

	public void setIdTypeName(String idTypeName) {
		if (idTypeName != null) {
			this.idTypeName = idTypeName;
		}
	}

	@Transient
	public String getOccupationName() {
		return occupationName;
	}

	public void setOccupationName(String occupationName) {
		
			this.occupationName = occupationName;
		
	}

	@Transient
	public String getProfessionName() {
		return professionName;
	}

	public void setProfessionName(String professionName) {
		
			this.professionName = professionName;
		
	}

	@Transient
	public String getMaritalStatusName() {
		return maritalStatusName;
	}

	public void setMaritalStatusName(String maritalStatusName) {
		
			this.maritalStatusName = maritalStatusName;
		
	}

	@Transient
	public String getBuisnessCityName() {
		return buisnessCityName;
	}

	public void setBuisnessCityName(String buisnessCityName) {
		
			this.buisnessCityName = buisnessCityName;
		
	}

	@Transient
	public String getResidentialCityName() {
		return residentialCityName;
	}

	public void setResidentialCityName(String residentialCityName) {
		
			this.residentialCityName = residentialCityName;
		
	}

	@Transient
	public Long getIndex() {
		return index;
	}

	public void setIndex(Long index) {
		this.index = index;
	}

	
}
