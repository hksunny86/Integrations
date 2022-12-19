package com.inov8.verifly.common.model;

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
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Transient;

import com.inov8.framework.common.model.AssociationModel;
import com.inov8.framework.common.model.BasePersistableModel;

/**
 * The CardTypeModel entity bean.
 *
 * @author  Shoaib Akhtar  Inov8 Limited
 * @version $Revision: 1.20 $, $Date: 2006/03/06 19:29:08 $
 *
 *
 * @spring.bean name="CardTypeModel"
 */
@Entity
@org.hibernate.annotations.Entity(dynamicInsert = true)
@javax.persistence.SequenceGenerator(name = "CARD_TYPE_seq",sequenceName = "VF_CARD_TYPE_seq", allocationSize=1)
@Table(name = "VF_CARD_TYPE")
public class VfCardTypeModel extends BasePersistableModel {



   private transient Collection<AccountInfoModel> cardTypeIdAccountInfoModelList = new ArrayList<AccountInfoModel>();

   private Long cardTypeId;
   private String name;
   private String description;

   /**
    * Default constructor.
    */
   public VfCardTypeModel() {
   }

    /**
     * Return the primary key.
     *
     * @return Long with the primary key.
     */
   @javax.persistence.Transient
   public Long getPrimaryKey() {
        return getCardTypeId();
    }

    /**
     * Set the primary key.
     *
     * @param primaryKey the primary key
     */
   @javax.persistence.Transient
   public void setPrimaryKey(Long primaryKey) {
       setCardTypeId(primaryKey);
    }

   /**
    * Returns the value of the <code>cardTypeId</code> property.
    *
    */
      @Column(name = "CARD_TYPE_ID" , nullable = false )
   @Id @GeneratedValue(strategy=GenerationType.SEQUENCE, generator="CARD_TYPE_seq")
   public Long getCardTypeId() {
      return cardTypeId;
   }

   /**
    * Sets the value of the <code>cardTypeId</code> property.
    *
    * @param cardTypeId the value for the <code>cardTypeId</code> property
    *
		    */

   public void setCardTypeId(Long cardTypeId) {
      this.cardTypeId = cardTypeId;
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
    * Returns the value of the <code>description</code> property.
    *
    */
      @Column(name = "DESCRIPTION"  , length=255 )
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
    * @spring.validator-var name="maxlength" value="255"
    */

   public void setDescription(String description) {
      this.description = description;
   }


   /**
    * Add the related AccountInfoModel to this one-to-many relation.
    *
    * @param accountInfoModel object to be added.
    */

   public void addCardTypeIdAccountInfoModel(AccountInfoModel accountInfoModel) {
      accountInfoModel.setRelationCardTypeIdCardTypeModel(this);
      cardTypeIdAccountInfoModelList.add(accountInfoModel);
   }

   /**
    * Remove the related AccountInfoModel to this one-to-many relation.
    *
    * @param accountInfoModel object to be removed.
    */

   public void removeCardTypeIdAccountInfoModel(AccountInfoModel accountInfoModel) {
      accountInfoModel.setRelationCardTypeIdCardTypeModel(null);
      cardTypeIdAccountInfoModelList.remove(accountInfoModel);
   }

   /**
    * Get a list of related AccountInfoModel objects of the CardTypeModel object.
    * These objects are in a bidirectional one-to-many relation by the CardTypeId member.
    *
    * @return Collection of AccountInfoModel objects.
    *
    */

   @OneToMany(cascade = CascadeType.REFRESH, fetch = FetchType.LAZY, mappedBy = "relationCardTypeIdCardTypeModel")
   @JoinColumn(name = "CARD_TYPE_ID")
   public Collection<AccountInfoModel> getCardTypeIdAccountInfoModelList() throws Exception {
   		return cardTypeIdAccountInfoModelList;
   }


   /**
    * Set a list of AccountInfoModel related objects to the CardTypeModel object.
    * These objects are in a bidirectional one-to-many relation by the CardTypeId member.
    *
    * @param accountInfoModelList the list of related objects.
    */
    public void setCardTypeIdAccountInfoModelList(Collection<AccountInfoModel> accountInfoModelList) throws Exception {
		this.cardTypeIdAccountInfoModelList = accountInfoModelList;
   }




    /**
     * Used by the display tag library for rendering a checkbox in the list.
     * @return String with a HTML checkbox.
     */
    @Transient
    public String getCheckbox() {
        String checkBox = "<input type=\"checkbox\" name=\"checkbox";
        checkBox += "_"+ getCardTypeId();
        checkBox += "\"/>";
        return checkBox;
    }

   /**
    * Helper method for Struts with displaytag
    */
   @javax.persistence.Transient
   public String getPrimaryKeyParameter() {
      String parameters = "";
      parameters += "&cardTypeId=" + getCardTypeId();
      return parameters;
   }
	/**
     * Helper method for default Sorting on Primary Keys
     */
    @javax.persistence.Transient
    public String getPrimaryKeyFieldName()
    {
			String primaryKeyFieldName = "cardTypeId";
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


    	return associationModelList;
    }

}
