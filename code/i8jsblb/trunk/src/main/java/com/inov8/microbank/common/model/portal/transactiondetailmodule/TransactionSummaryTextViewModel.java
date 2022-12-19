package com.inov8.microbank.common.model.portal.transactiondetailmodule;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import com.inov8.framework.common.model.BasePersistableModel;

/**
 * The TransactionSummaryTextViewModel entity bean.
 *
 * @author  Muhammad Omar  
 * @version $Revision: 1.00 $, $Date: 2012/05/10 19:29:08 $
 *
 *
 * @spring.bean name="TransactionSummaryTextViewModel"
 */
@Entity
@org.hibernate.annotations.Entity(mutable = false)
@Table(name = "TRANSACTION_SUMMARY_TEXT_VIEW")
public class TransactionSummaryTextViewModel extends BasePersistableModel implements Serializable {
  

	private static final long serialVersionUID = -262373219033039747L;

	private String transactionCode;
   private String transactionSummaryText;
   /**
    * Default constructor.
    */
   public TransactionSummaryTextViewModel() {
   }   



   /**
    * Returns the value of the <code>transactionCode</code> property.
    *
    */
      @Column(name = "TRANSACTION_CODE" , nullable = true , length=50 )
      @Id
      public String getTransactionCode() {
      return transactionCode;
   }

   /**
    * Sets the value of the <code>transactionCode</code> property.
    *
    * @param transactionCode the value for the <code>transactionCode</code> property
    *    
	* @spring.validator type="maxlength"     
    * @spring.validator-args arg1value="${var:maxlength}"
    * @spring.validator-var name="maxlength" value="50"
    */

   public void setTransactionCode(String transactionCode) {
      this.transactionCode = transactionCode;
   }

    @Column(name = "TRANSACTION_SUMMARY_TEXT" , nullable = true , length=233 )
	public String getTransactionSummaryText() {
		return transactionSummaryText;
	}

	public void setTransactionSummaryText(String transactionSummaryText) {
		this.transactionSummaryText = transactionSummaryText;
	}       

	   /**
	    * Helper method for Struts with displaytag
	    */
	   @javax.persistence.Transient
	   public String getPrimaryKeyParameter() {
	      String parameters = "";
	      parameters += "&transactionCode=" + getTransactionCode();
	      return parameters;
	   }
	   
		/**
	     * Helper method for default Sorting on Primary Keys
	     */
	    @javax.persistence.Transient
	    public String getPrimaryKeyFieldName()
	    { 
				String primaryKeyFieldName = "transactionCode";
				return primaryKeyFieldName;				
	    }

		@Override
		public Long getPrimaryKey() {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public void setPrimaryKey(Long arg0) {
			// TODO Auto-generated method stub
		}

}
