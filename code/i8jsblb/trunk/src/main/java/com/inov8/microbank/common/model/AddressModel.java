package com.inov8.microbank.common.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
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
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Transient;

import com.inov8.framework.common.model.AssociationModel;
import com.inov8.framework.common.model.BasePersistableModel;

/**
 * The AddressModel entity bean.
 *
 * @author  Usman Ashraf  Inov8 Limited
 * @version $Revision: 1.20 $, $Date: 2006/03/06 19:29:08 $
 *
 *
 * @spring.bean name="AddressModel"
 */
@Entity
@org.hibernate.annotations.Entity(dynamicInsert = true)
@javax.persistence.SequenceGenerator(name = "ADDRESS_seq",sequenceName = "ADDRESS_seq",allocationSize=1)
@Table(name = "ADDRESS")
public class AddressModel extends BasePersistableModel implements Serializable {
  

   /**
	 * 
	 */
	private static final long serialVersionUID = -319161491341645679L;
private PostalOfficeModel postalOfficeIdPostalOfficeModel;
   private DistrictModel districtIdDistrictModel;
   private CountryModel countryIdCountryModel;
   private CityModel cityIdCityModel;

   private Collection<CustomerAddressesModel> addressIdCustomerAddressesModelList = new ArrayList<CustomerAddressesModel>();
   private Collection<RetailerContactAddressesModel> addressIdRetailerContactAddressesModelList = new ArrayList<RetailerContactAddressesModel>();

   private Long addressId;
   private String houseNo;
   private String streetNo;
   private String otherDistrict;
   private String otherCity;
   private String province;
   private String otherPostalOffice;
   private String ntn;
   private String nearestLandMark;
   private Long postalCode;
   private String streetAddress;
   private String fullAddress;

   /**
    * Default constructor.
    */
   public AddressModel() {
   }   

    /**
     * Return the primary key.
     *
     * @return Long with the primary key.
     */
   @javax.persistence.Transient
   public Long getPrimaryKey() {
        return getAddressId();
    }

    /**
     * Set the primary key.
     *
     * @param primaryKey the primary key
     */
   @javax.persistence.Transient
   public void setPrimaryKey(Long primaryKey) {
       setAddressId(primaryKey);
    }

   /**
    * Returns the value of the <code>addressId</code> property.
    *
    */
      @Column(name = "ADDRESS_ID" , nullable = false )
   @Id @GeneratedValue(strategy=GenerationType.SEQUENCE, generator="ADDRESS_seq")
   public Long getAddressId() {
      return addressId;
   }

   /**
    * Sets the value of the <code>addressId</code> property.
    *
    * @param addressId the value for the <code>addressId</code> property
    *    
		    */

   public void setAddressId(Long addressId) {
      this.addressId = addressId;
   }

   /**
    * Returns the value of the <code>houseNo</code> property.
    *
    */
      @Column(name = "HOUSE_NO"  , length=50 )
   public String getHouseNo() {
      return houseNo;
   }

   /**
    * Sets the value of the <code>houseNo</code> property.
    *
    * @param houseNo the value for the <code>houseNo</code> property
    *    
		    * @spring.validator type="maxlength"     
    * @spring.validator-args arg1value="${var:maxlength}"
    * @spring.validator-var name="maxlength" value="50"
    */

   public void setHouseNo(String houseNo) {
      this.houseNo = houseNo;
   }
   
    @Column(name = "NTN"  , length=50 )
    public String getNtn() {
	 return ntn;
    }

	public void setNtn(String ntn) {
		this.ntn = ntn;
	}
	
	@Column(name = "NEAREST_LAND_MARK"  , length=100 )
	public String getNearestLandMark() {
		return nearestLandMark;
	}
	
	public void setNearestLandMark(String nearestLandMark) {
		this.nearestLandMark = nearestLandMark;
	}

/**
    * Returns the value of the <code>streetNo</code> property.
    *
    */
      @Column(name = "STREET_NO"  , length=50 )
   public String getStreetNo() {
      return streetNo;
   }

   /**
    * Sets the value of the <code>streetNo</code> property.
    *
    * @param streetNo the value for the <code>streetNo</code> property
    *    
		    * @spring.validator type="maxlength"     
    * @spring.validator-args arg1value="${var:maxlength}"
    * @spring.validator-var name="maxlength" value="50"
    */

   public void setStreetNo(String streetNo) {
      this.streetNo = streetNo;
   }

   /**
    * Returns the value of the <code>otherDistrict</code> property.
    *
    */
      @Column(name = "OTHER_DISTRICT"  , length=50 )
   public String getOtherDistrict() {
      return otherDistrict;
   }

   /**
    * Sets the value of the <code>otherDistrict</code> property.
    *
    * @param otherDistrict the value for the <code>otherDistrict</code> property
    *    
		    * @spring.validator type="maxlength"     
    * @spring.validator-args arg1value="${var:maxlength}"
    * @spring.validator-var name="maxlength" value="50"
    */

   public void setOtherDistrict(String otherDistrict) {
      this.otherDistrict = otherDistrict;
   }

   /**
    * Returns the value of the <code>otherCity</code> property.
    *
    */
      @Column(name = "OTHER_CITY"  , length=50 )
   public String getOtherCity() {
      return otherCity;
   }

   /**
    * Sets the value of the <code>otherCity</code> property.
    *
    * @param otherCity the value for the <code>otherCity</code> property
    *    
		    * @spring.validator type="maxlength"     
    * @spring.validator-args arg1value="${var:maxlength}"
    * @spring.validator-var name="maxlength" value="50"
    */

   public void setOtherCity(String otherCity) {
      this.otherCity = otherCity;
   }

   /**
    * Returns the value of the <code>otherPostalOffice</code> property.
    *
    */
      @Column(name = "OTHER_POSTAL_OFFICE"  , length=50 )
   public String getOtherPostalOffice() {
      return otherPostalOffice;
   }

   /**
    * Sets the value of the <code>otherPostalOffice</code> property.
    *
    * @param otherPostalOffice the value for the <code>otherPostalOffice</code> property
    *    
		    * @spring.validator type="maxlength"     
    * @spring.validator-args arg1value="${var:maxlength}"
    * @spring.validator-var name="maxlength" value="50"
    */

   public void setOtherPostalOffice(String otherPostalOffice) {
      this.otherPostalOffice = otherPostalOffice;
   }

   /**
    * Returns the value of the <code>postalOfficeIdPostalOfficeModel</code> relation property.
    *
    * @return the value of the <code>postalOfficeIdPostalOfficeModel</code> relation property.
    *
    */
   @ManyToOne(cascade = CascadeType.REFRESH, fetch = FetchType.LAZY)
   @JoinColumn(name = "POSTAL_OFFICE_ID")    
   public PostalOfficeModel getRelationPostalOfficeIdPostalOfficeModel(){
      return postalOfficeIdPostalOfficeModel;
   }
    
   /**
    * Returns the value of the <code>postalOfficeIdPostalOfficeModel</code> relation property.
    *
    * @return the value of the <code>postalOfficeIdPostalOfficeModel</code> relation property.
    *
    */
   @javax.persistence.Transient
   public PostalOfficeModel getPostalOfficeIdPostalOfficeModel(){
      return getRelationPostalOfficeIdPostalOfficeModel();
   }

   /**
    * Sets the value of the <code>postalOfficeIdPostalOfficeModel</code> relation property.
    *
    * @param postalOfficeModel a value for <code>postalOfficeIdPostalOfficeModel</code>.
    */
   @javax.persistence.Transient
   public void setRelationPostalOfficeIdPostalOfficeModel(PostalOfficeModel postalOfficeModel) {
      this.postalOfficeIdPostalOfficeModel = postalOfficeModel;
   }
   
   /**
    * Sets the value of the <code>postalOfficeIdPostalOfficeModel</code> relation property.
    *
    * @param postalOfficeModel a value for <code>postalOfficeIdPostalOfficeModel</code>.
    */
   @javax.persistence.Transient
   public void setPostalOfficeIdPostalOfficeModel(PostalOfficeModel postalOfficeModel) {
      if(null != postalOfficeModel)
      {
      	setRelationPostalOfficeIdPostalOfficeModel((PostalOfficeModel)postalOfficeModel.clone());
      }      
   }
   

   /**
    * Returns the value of the <code>districtIdDistrictModel</code> relation property.
    *
    * @return the value of the <code>districtIdDistrictModel</code> relation property.
    *
    */
   @ManyToOne(cascade = CascadeType.REFRESH, fetch = FetchType.LAZY)
   @JoinColumn(name = "DISTRICT_ID")    
   public DistrictModel getRelationDistrictIdDistrictModel(){
      return districtIdDistrictModel;
   }
    
   /**
    * Returns the value of the <code>districtIdDistrictModel</code> relation property.
    *
    * @return the value of the <code>districtIdDistrictModel</code> relation property.
    *
    */
   @javax.persistence.Transient
   public DistrictModel getDistrictIdDistrictModel(){
      return getRelationDistrictIdDistrictModel();
   }

   /**
    * Sets the value of the <code>districtIdDistrictModel</code> relation property.
    *
    * @param districtModel a value for <code>districtIdDistrictModel</code>.
    */
   @javax.persistence.Transient
   public void setRelationDistrictIdDistrictModel(DistrictModel districtModel) {
      this.districtIdDistrictModel = districtModel;
   }
   
   /**
    * Sets the value of the <code>districtIdDistrictModel</code> relation property.
    *
    * @param districtModel a value for <code>districtIdDistrictModel</code>.
    */
   @javax.persistence.Transient
   public void setDistrictIdDistrictModel(DistrictModel districtModel) {
      if(null != districtModel)
      {
      	setRelationDistrictIdDistrictModel((DistrictModel)districtModel.clone());
      }      
   }
   

   /**
    * Returns the value of the <code>countryIdCountryModel</code> relation property.
    *
    * @return the value of the <code>countryIdCountryModel</code> relation property.
    *
    */
   @ManyToOne(cascade = CascadeType.REFRESH, fetch = FetchType.LAZY)
   @JoinColumn(name = "COUNTRY_ID")    
   public CountryModel getRelationCountryIdCountryModel(){
      return countryIdCountryModel;
   }
    
   /**
    * Returns the value of the <code>countryIdCountryModel</code> relation property.
    *
    * @return the value of the <code>countryIdCountryModel</code> relation property.
    *
    */
   @javax.persistence.Transient
   public CountryModel getCountryIdCountryModel(){
      return getRelationCountryIdCountryModel();
   }

   /**
    * Sets the value of the <code>countryIdCountryModel</code> relation property.
    *
    * @param countryModel a value for <code>countryIdCountryModel</code>.
    */
   @javax.persistence.Transient
   public void setRelationCountryIdCountryModel(CountryModel countryModel) {
      this.countryIdCountryModel = countryModel;
   }
   
   /**
    * Sets the value of the <code>countryIdCountryModel</code> relation property.
    *
    * @param countryModel a value for <code>countryIdCountryModel</code>.
    */
   @javax.persistence.Transient
   public void setCountryIdCountryModel(CountryModel countryModel) {
      if(null != countryModel)
      {
      	setRelationCountryIdCountryModel((CountryModel)countryModel.clone());
      }      
   }
   

   /**
    * Returns the value of the <code>cityIdCityModel</code> relation property.
    *
    * @return the value of the <code>cityIdCityModel</code> relation property.
    *
    */
   @ManyToOne(cascade = CascadeType.REFRESH, fetch = FetchType.LAZY)
   @JoinColumn(name = "CITY_ID")    
   public CityModel getRelationCityIdCityModel(){
      return cityIdCityModel;
   }
    
   /**
    * Returns the value of the <code>cityIdCityModel</code> relation property.
    *
    * @return the value of the <code>cityIdCityModel</code> relation property.
    *
    */
   @javax.persistence.Transient
   public CityModel getCityIdCityModel(){
      return getRelationCityIdCityModel();
   }

   /**
    * Sets the value of the <code>cityIdCityModel</code> relation property.
    *
    * @param cityModel a value for <code>cityIdCityModel</code>.
    */
   @javax.persistence.Transient
   public void setRelationCityIdCityModel(CityModel cityModel) {
      this.cityIdCityModel = cityModel;
   }
   
   /**
    * Sets the value of the <code>cityIdCityModel</code> relation property.
    *
    * @param cityModel a value for <code>cityIdCityModel</code>.
    */
   @javax.persistence.Transient
   public void setCityIdCityModel(CityModel cityModel) {
      if(null != cityModel)
      {
      	setRelationCityIdCityModel((CityModel)cityModel.clone());
      }      
   }
   


   /**
    * Add the related CustomerAddressesModel to this one-to-many relation.
    *
    * @param customerAddressesModel object to be added.
    */
    
   public void addAddressIdCustomerAddressesModel(CustomerAddressesModel customerAddressesModel) {
      customerAddressesModel.setRelationAddressIdAddressModel(this);
      addressIdCustomerAddressesModelList.add(customerAddressesModel);
   }
   
   /**
    * Remove the related CustomerAddressesModel to this one-to-many relation.
    *
    * @param customerAddressesModel object to be removed.
    */
   
   public void removeAddressIdCustomerAddressesModel(CustomerAddressesModel customerAddressesModel) {      
      customerAddressesModel.setRelationAddressIdAddressModel(null);
      addressIdCustomerAddressesModelList.remove(customerAddressesModel);      
   }

   /**
    * Get a list of related CustomerAddressesModel objects of the AddressModel object.
    * These objects are in a bidirectional one-to-many relation by the AddressId member.
    *
    * @return Collection of CustomerAddressesModel objects.
    *
    */
   
   @OneToMany(cascade = CascadeType.REFRESH, fetch = FetchType.LAZY, mappedBy = "relationAddressIdAddressModel")
   @JoinColumn(name = "ADDRESS_ID")
   public Collection<CustomerAddressesModel> getAddressIdCustomerAddressesModelList() throws Exception {
   		return addressIdCustomerAddressesModelList;
   }


   /**
    * Set a list of CustomerAddressesModel related objects to the AddressModel object.
    * These objects are in a bidirectional one-to-many relation by the AddressId member.
    *
    * @param customerAddressesModelList the list of related objects.
    */
    public void setAddressIdCustomerAddressesModelList(Collection<CustomerAddressesModel> customerAddressesModelList) throws Exception {
		this.addressIdCustomerAddressesModelList = customerAddressesModelList;
   }


   /**
    * Add the related RetailerContactAddressesModel to this one-to-many relation.
    *
    * @param retailerContactAddressesModel object to be added.
    */
    
   public void addAddressIdRetailerContactAddressesModel(RetailerContactAddressesModel retailerContactAddressesModel) {
      retailerContactAddressesModel.setRelationAddressIdAddressModel(this);
      addressIdRetailerContactAddressesModelList.add(retailerContactAddressesModel);
   }
   
   /**
    * Remove the related RetailerContactAddressesModel to this one-to-many relation.
    *
    * @param retailerContactAddressesModel object to be removed.
    */
   
   public void removeAddressIdRetailerContactAddressesModel(RetailerContactAddressesModel retailerContactAddressesModel) {      
      retailerContactAddressesModel.setRelationAddressIdAddressModel(null);
      addressIdRetailerContactAddressesModelList.remove(retailerContactAddressesModel);      
   }

   /**
    * Get a list of related RetailerContactAddressesModel objects of the AddressModel object.
    * These objects are in a bidirectional one-to-many relation by the AddressId member.
    *
    * @return Collection of RetailerContactAddressesModel objects.
    *
    */
   
   @OneToMany(cascade = CascadeType.REFRESH, fetch = FetchType.LAZY, mappedBy = "relationAddressIdAddressModel")
   @JoinColumn(name = "ADDRESS_ID")
   public Collection<RetailerContactAddressesModel> getAddressIdRetailerContactAddressesModelList() throws Exception {
   		return addressIdRetailerContactAddressesModelList;
   }


   /**
    * Set a list of RetailerContactAddressesModel related objects to the AddressModel object.
    * These objects are in a bidirectional one-to-many relation by the AddressId member.
    *
    * @param retailerContactAddressesModelList the list of related objects.
    */
    public void setAddressIdRetailerContactAddressesModelList(Collection<RetailerContactAddressesModel> retailerContactAddressesModelList) throws Exception {
		this.addressIdRetailerContactAddressesModelList = retailerContactAddressesModelList;
   }



   /**
    * Returns the value of the <code>postalOfficeId</code> property.
    *
    */
   @javax.persistence.Transient
   public Long getPostalOfficeId() {
      if (postalOfficeIdPostalOfficeModel != null) {
         return postalOfficeIdPostalOfficeModel.getPostalOfficeId();
      } else {
         return null;
      }
   }

   /**
    * Sets the value of the <code>postalOfficeId</code> property.
    *
    * @param postalOfficeId the value for the <code>postalOfficeId</code> property
																							    */
   
   @javax.persistence.Transient
   public void setPostalOfficeId(Long postalOfficeId) {
      if(postalOfficeId == null)
      {      
      	postalOfficeIdPostalOfficeModel = null;
      }
      else
      {
        postalOfficeIdPostalOfficeModel = new PostalOfficeModel();
      	postalOfficeIdPostalOfficeModel.setPostalOfficeId(postalOfficeId);
      }      
   }

   /**
    * Returns the value of the <code>districtId</code> property.
    *
    */
   @javax.persistence.Transient
   public Long getDistrictId() {
      if (districtIdDistrictModel != null) {
         return districtIdDistrictModel.getDistrictId();
      } else {
         return null;
      }
   }

   /**
    * Sets the value of the <code>districtId</code> property.
    *
    * @param districtId the value for the <code>districtId</code> property
									    * @spring.validator type="required"
															    */
   
   @javax.persistence.Transient
   public void setDistrictId(Long districtId) {
      if(districtId == null)
      {      
      	districtIdDistrictModel = null;
      }
      else
      {
        districtIdDistrictModel = new DistrictModel();
      	districtIdDistrictModel.setDistrictId(districtId);
      }      
   }

   /**
    * Returns the value of the <code>countryId</code> property.
    *
    */
   @javax.persistence.Transient
   public Long getCountryId() {
      if (countryIdCountryModel != null) {
         return countryIdCountryModel.getCountryId();
      } else {
         return null;
      }
   }

   /**
    * Sets the value of the <code>countryId</code> property.
    *
    * @param countryId the value for the <code>countryId</code> property
																							    */
   
   @javax.persistence.Transient
   public void setCountryId(Long countryId) {
      if(countryId == null)
      {      
      	countryIdCountryModel = null;
      }
      else
      {
        countryIdCountryModel = new CountryModel();
      	countryIdCountryModel.setCountryId(countryId);
      }      
   }

   /**
    * Returns the value of the <code>cityId</code> property.
    *
    */
   @javax.persistence.Transient
   public Long getCityId() {
      if (cityIdCityModel != null) {
         return cityIdCityModel.getCityId();
      } else {
         return null;
      }
   }

   /**
    * Sets the value of the <code>cityId</code> property.
    *
    * @param cityId the value for the <code>cityId</code> property
													    * @spring.validator type="required"
											    */
   
   @javax.persistence.Transient
   public void setCityId(Long cityId) {
      if(cityId == null)
      {      
      	cityIdCityModel = null;
      }
      else
      {
        cityIdCityModel = new CityModel();
      	cityIdCityModel.setCityId(cityId);
      }      
   }

    @Column(name="POSTAL_CODE")
	public Long getPostalCode() {
		return postalCode;
	}

	public void setPostalCode(Long postalCode) {
		this.postalCode = postalCode;
	}

	@Column(name="STREET_ADDRESS")
	public String getStreetAddress() {
		return streetAddress;
	}

	public void setStreetAddress(String streetAddress) {
		this.streetAddress = streetAddress;
	}
	
	@Column(name="FULL_ADDRESS")
	public String getFullAddress() {
		return fullAddress;
	}

	public void setFullAddress(String fullAddress) {
		if (fullAddress != null) {
			this.fullAddress = fullAddress;
		}
	}  

    /**
     * Used by the display tag library for rendering a checkbox in the list.
     * @return String with a HTML checkbox.
     */
    @Transient
    public String getCheckbox() {
        String checkBox = "<input type=\"checkbox\" name=\"checkbox";
        checkBox += "_"+ getAddressId();
        checkBox += "\"/>";
        return checkBox;
    }

   /**
    * Helper method for Struts with displaytag
    */
   @javax.persistence.Transient
   public String getPrimaryKeyParameter() {
      String parameters = "";
      parameters += "&addressId=" + getAddressId();
      return parameters;
   }
	/**
     * Helper method for default Sorting on Primary Keys
     */
    @javax.persistence.Transient
    public String getPrimaryKeyFieldName()
    { 
			String primaryKeyFieldName = "addressId";
			return primaryKeyFieldName;				
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
    	
    	associationModel.setClassName("PostalOfficeModel");
    	associationModel.setPropertyName("relationPostalOfficeIdPostalOfficeModel");   		
   		associationModel.setValue(getRelationPostalOfficeIdPostalOfficeModel());
   		
   		associationModelList.add(associationModel);
   		
			      associationModel = new AssociationModel();
    	
    	associationModel.setClassName("DistrictModel");
    	associationModel.setPropertyName("relationDistrictIdDistrictModel");   		
   		associationModel.setValue(getRelationDistrictIdDistrictModel());
   		
   		associationModelList.add(associationModel);
   		
			      associationModel = new AssociationModel();
    	
    	associationModel.setClassName("CountryModel");
    	associationModel.setPropertyName("relationCountryIdCountryModel");   		
   		associationModel.setValue(getRelationCountryIdCountryModel());
   		
   		associationModelList.add(associationModel);
   		
			      associationModel = new AssociationModel();
    	
    	associationModel.setClassName("CityModel");
    	associationModel.setPropertyName("relationCityIdCityModel");   		
   		associationModel.setValue(getRelationCityIdCityModel());
   		
   		associationModelList.add(associationModel);
   		
   		associationModel = new AssociationModel();
    	
    	associationModel.setClassName("PostalOfficeModel");
    	associationModel.setPropertyName("relationPostalOfficeIdPostalOfficeModel");   		
   		associationModel.setValue(getRelationPostalOfficeIdPostalOfficeModel());
   		
   		associationModelList.add(associationModel);
			    	
    	return associationModelList;
    }

    @javax.persistence.Transient
	public String getProvince() {
		return province;
	}

	public void setProvince(String province) {
		this.province = province;
	}

}
