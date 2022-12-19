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
 * The VeriflyModel entity bean.
 *
 * @author  Jawwad Farooq  Inov8 Limited
 * @version $Revision: 1.20 $, $Date: 2006/03/06 19:29:08 $
 *
 *
 * @spring.bean name="VeriflyModel"
 */
@Entity
@org.hibernate.annotations.Entity(dynamicInsert = true)
@javax.persistence.SequenceGenerator(name = "VERIFLY_seq",sequenceName = "VERIFLY_seq",allocationSize=1)
@Table(name = "VERIFLY")
public class VeriflyModel extends BasePersistableModel implements Serializable {
  

   private VeriflyModeModel veriflyModeIdVeriflyModeModel;

   /**
	 * 
	 */
	private static final long serialVersionUID = -7886966591801798918L;

private Collection<BankModel> veriflyIdBankModelList = new ArrayList<BankModel>();

   private Long veriflyId;
   private String name;
   private String userId;
   private String password;
   private String url;
   private String key;
   private String description;
   private Boolean active;
   private String mod;

   /**
    * Default constructor.
    */
   public VeriflyModel() {
   }   

    /**
     * Return the primary key.
     *
     * @return Long with the primary key.
     */
   @javax.persistence.Transient
   public Long getPrimaryKey() {
        return getVeriflyId();
    }

    /**
     * Set the primary key.
     *
     * @param primaryKey the primary key
     */
   @javax.persistence.Transient
   public void setPrimaryKey(Long primaryKey) {
       setVeriflyId(primaryKey);
    }

   /**
    * Returns the value of the <code>veriflyId</code> property.
    *
    */
      @Column(name = "VERIFLY_ID" , nullable = false )
   @Id @GeneratedValue(strategy=GenerationType.SEQUENCE, generator="VERIFLY_seq")
   public Long getVeriflyId() {
      return veriflyId;
   }

   /**
    * Sets the value of the <code>veriflyId</code> property.
    *
    * @param veriflyId the value for the <code>veriflyId</code> property
    *    
		    */

   public void setVeriflyId(Long veriflyId) {
      this.veriflyId = veriflyId;
   }

   /**
    * Returns the value of the <code>name</code> property.
    *
    */
      @Column(name = "NAME" , nullable = false , length=50 )
   public String getName() {
      return name;
   }

   /**
    * Sets the value of the <code>name</code> property.
    *
    * @param name the value for the <code>name</code> property
    *    
		    * @spring.validator type="required"
    * @spring.validator type="maxlength"     
    * @spring.validator-args arg1value="${var:maxlength}"
    * @spring.validator-var name="maxlength" value="50"
    * @spring.validator type="mask"
    * @spring.validator-args arg1value="${mask}"
    * @spring.validator-var name="mask" value="^[a-zA-Z0-9]*$"
    */

   public void setName(String name) {
      this.name = name;
   }

   /**
    * Returns the value of the <code>userId</code> property.
    *
    */
      @Column(name = "USER_ID" , nullable = false , length=50 )
   public String getUserId() {
      return userId;
   }

   /**
    * Sets the value of the <code>userId</code> property.
    *
    * @param userId the value for the <code>userId</code> property
    *    
		    * @spring.validator type="required"
    * @spring.validator type="maxlength"     
    * @spring.validator-args arg1value="${var:maxlength}"
    * @spring.validator-var name="maxlength" value="50"
    */

   public void setUserId(String userId) {
      this.userId = userId;
   }

   /**
    * Returns the value of the <code>password</code> property.
    *
    */
      @Column(name = "PASSWORD" , nullable = false , length=50 )
   public String getPassword() {
      return password;
   }

   /**
    * Sets the value of the <code>password</code> property.
    *
    * @param password the value for the <code>password</code> property
    *    
		    * @spring.validator type="required"
    * @spring.validator type="maxlength"     
    * @spring.validator-args arg1value="${var:maxlength}"
    * @spring.validator-var name="maxlength" value="50"
    */

   public void setPassword(String password) {
      this.password = password;
   }

   /**
    * Returns the value of the <code>url</code> property.
    *
    */
      @Column(name = "URL" , nullable = false , length=250 )
   public String getUrl() {
      return url;
   }

   /**
    * Sets the value of the <code>url</code> property.
    *
    * @param url the value for the <code>url</code> property
    *    
		    * @spring.validator type="required"
    * @spring.validator type="maxlength"     
    * @spring.validator-args arg1value="${var:maxlength}"
    * @spring.validator-var name="maxlength" value="250"
    */

   public void setUrl(String url) {
      this.url = url;
   }

   /**
    * Returns the value of the <code>key</code> property.
    *
    */
      @Column(name = "KEY"  , length=4000 )
   public String getKey() {
      return key;
   }

   /**
    * Sets the value of the <code>key</code> property.
    *
    * @param key the value for the <code>key</code> property
    *    
		    * @spring.validator type="maxlength"     
    * @spring.validator-args arg1value="${var:maxlength}"
    * @spring.validator-var name="maxlength" value="4000"
    */

   public void setKey(String key) {
      this.key = key;
   }

   /**
    * Returns the value of the <code>description</code> property.
    *
    */
      @Column(name = "DESCRIPTION"  , length=250 )
   public String getDescription() {
      return description;
   }

   /**
    * Sets the value of the <code>description</code> property.
    *
    * @param description the value for the <code>description</code> property
    *    
		    * @spring.validator type="maxlength"     
    * @spring.validator-args arg1value="${var:maxlength}"
    * @spring.validator-var name="maxlength" value="250"
    */

   public void setDescription(String description) {
      this.description = description;
   }

   /**
    * Returns the value of the <code>active</code> property.
    *
    */
      @Column(name = "IS_ACTIVE" , nullable = false )
   public Boolean getActive() {
      return active;
   }

   /**
    * Sets the value of the <code>active</code> property.
    *
    * @param active the value for the <code>active</code> property
    *    
		    */

   public void setActive(Boolean active) {
      this.active = active;
   }

   /**
    * Returns the value of the <code>mod</code> property.
    *
    */
      @Column(name = "MOD"  , length=50 )
   public String getMod() {
      return mod;
   }

   /**
    * Sets the value of the <code>mod</code> property.
    *
    * @param mod the value for the <code>mod</code> property
    *    
		    * @spring.validator type="maxlength"     
    * @spring.validator-args arg1value="${var:maxlength}"
    * @spring.validator-var name="maxlength" value="50"
    */

   public void setMod(String mod) {
      this.mod = mod;
   }

   /**
    * Returns the value of the <code>veriflyModeIdVeriflyModeModel</code> relation property.
    *
    * @return the value of the <code>veriflyModeIdVeriflyModeModel</code> relation property.
    *
    */
   @ManyToOne(cascade = CascadeType.REFRESH, fetch = FetchType.LAZY)
   @JoinColumn(name = "VERIFLY_MODE_ID")    
   public VeriflyModeModel getRelationVeriflyModeIdVeriflyModeModel(){
      return veriflyModeIdVeriflyModeModel;
   }
    
   /**
    * Returns the value of the <code>veriflyModeIdVeriflyModeModel</code> relation property.
    *
    * @return the value of the <code>veriflyModeIdVeriflyModeModel</code> relation property.
    *
    */
   @javax.persistence.Transient
   public VeriflyModeModel getVeriflyModeIdVeriflyModeModel(){
      return getRelationVeriflyModeIdVeriflyModeModel();
   }

   /**
    * Sets the value of the <code>veriflyModeIdVeriflyModeModel</code> relation property.
    *
    * @param veriflyModeModel a value for <code>veriflyModeIdVeriflyModeModel</code>.
    */
   @javax.persistence.Transient
   public void setRelationVeriflyModeIdVeriflyModeModel(VeriflyModeModel veriflyModeModel) {
      this.veriflyModeIdVeriflyModeModel = veriflyModeModel;
   }
   
   /**
    * Sets the value of the <code>veriflyModeIdVeriflyModeModel</code> relation property.
    *
    * @param veriflyModeModel a value for <code>veriflyModeIdVeriflyModeModel</code>.
    */
   @javax.persistence.Transient
   public void setVeriflyModeIdVeriflyModeModel(VeriflyModeModel veriflyModeModel) {
      if(null != veriflyModeModel)
      {
      	setRelationVeriflyModeIdVeriflyModeModel((VeriflyModeModel)veriflyModeModel.clone());
      }      
   }
   


   /**
    * Add the related BankModel to this one-to-many relation.
    *
    * @param bankModel object to be added.
    */
    
   public void addVeriflyIdBankModel(BankModel bankModel) {
      bankModel.setRelationVeriflyIdVeriflyModel(this);
      veriflyIdBankModelList.add(bankModel);
   }
   
   /**
    * Remove the related BankModel to this one-to-many relation.
    *
    * @param bankModel object to be removed.
    */
   
   public void removeVeriflyIdBankModel(BankModel bankModel) {      
      bankModel.setRelationVeriflyIdVeriflyModel(null);
      veriflyIdBankModelList.remove(bankModel);      
   }

   /**
    * Get a list of related BankModel objects of the VeriflyModel object.
    * These objects are in a bidirectional one-to-many relation by the VeriflyId member.
    *
    * @return Collection of BankModel objects.
    *
    */
   
   @OneToMany(cascade = CascadeType.REFRESH, fetch = FetchType.LAZY, mappedBy = "relationVeriflyIdVeriflyModel")
   @JoinColumn(name = "VERIFLY_ID")
   public Collection<BankModel> getVeriflyIdBankModelList() throws Exception {
   		return veriflyIdBankModelList;
   }


   /**
    * Set a list of BankModel related objects to the VeriflyModel object.
    * These objects are in a bidirectional one-to-many relation by the VeriflyId member.
    *
    * @param bankModelList the list of related objects.
    */
    public void setVeriflyIdBankModelList(Collection<BankModel> bankModelList) throws Exception {
		this.veriflyIdBankModelList = bankModelList;
   }



   /**
    * Returns the value of the <code>veriflyModeId</code> property.
    *
    */
   @javax.persistence.Transient
   public Long getVeriflyModeId() {
      if (veriflyModeIdVeriflyModeModel != null) {
         return veriflyModeIdVeriflyModeModel.getVeriflyModeId();
      } else {
         return null;
      }
   }

   /**
    * Sets the value of the <code>veriflyModeId</code> property.
    *
    * @param veriflyModeId the value for the <code>veriflyModeId</code> property
																							    */
   
   @javax.persistence.Transient
   public void setVeriflyModeId(Long veriflyModeId) {
      if(veriflyModeId == null)
      {      
      	veriflyModeIdVeriflyModeModel = null;
      }
      else
      {
        veriflyModeIdVeriflyModeModel = new VeriflyModeModel();
      	veriflyModeIdVeriflyModeModel.setVeriflyModeId(veriflyModeId);
      }      
   }


    /**
     * Used by the display tag library for rendering a checkbox in the list.
     * @return String with a HTML checkbox.
     */
    @Transient
    public String getCheckbox() {
        String checkBox = "<input type=\"checkbox\" name=\"checkbox";
        checkBox += "_"+ getVeriflyId();
        checkBox += "\"/>";
        return checkBox;
    }

   /**
    * Helper method for Struts with displaytag
    */
   @javax.persistence.Transient
   public String getPrimaryKeyParameter() {
      String parameters = "";
      parameters += "&veriflyId=" + getVeriflyId();
      return parameters;
   }
	/**
     * Helper method for default Sorting on Primary Keys
     */
    @javax.persistence.Transient
    public String getPrimaryKeyFieldName()
    { 
			String primaryKeyFieldName = "veriflyId";
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
    	
    	associationModel.setClassName("VeriflyModeModel");
    	associationModel.setPropertyName("relationVeriflyModeIdVeriflyModeModel");   		
   		associationModel.setValue(getRelationVeriflyModeIdVeriflyModeModel());
   		
   		associationModelList.add(associationModel);
   		
			    	
    	return associationModelList;
    }    
          
}
