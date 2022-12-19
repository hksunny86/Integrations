package com.inov8.microbank.common.model;

import com.inov8.framework.common.model.BasePersistableModel;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@org.hibernate.annotations.Entity(dynamicInsert = true)
@javax.persistence.SequenceGenerator(name = "BILL_PAYMENTS_DUE_DATE_seq",sequenceName = "BILL_PAYMENTS_DUE_DATE_seq", allocationSize=1)
@Table(name = "BILL_PAYMENTS_DUE_DATE")
public class BillPaymentsDueDateModel extends BasePersistableModel implements Serializable {

    private Long pk;
    private Long productId;
    private String name;
    private String productCode;
    private String description;
    private String comments;

    @javax.persistence.Transient
    public void setPrimaryKey(Long primaryKey) {
        setPk(primaryKey);
    }

    @javax.persistence.Transient
    public Long getPrimaryKey() {
        return getPk();
    }

    @javax.persistence.Transient
    public String getPrimaryKeyParameter() {
        String parameters = "";
        parameters += "&pk=" + getPk();
        return parameters;
    }

    @javax.persistence.Transient
    public String getPrimaryKeyFieldName()
    {
        String primaryKeyFieldName = "pk";
        return primaryKeyFieldName;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "BILL_PAYMENTS_DUE_DATE_seq")
    @Column(name = "PK"  ) public Long getPk() { return pk; }

    public void setPk(Long pk) { this.pk = pk; }

    @Column(name = "PRODUCT_ID" , nullable = false )
    public Long getProductId() {return productId;}

    public void setProductId(Long productId) {this.productId = productId;}

    @Column(name = "NAME" , nullable = false )
    public String getName() {return name;}

    public void setName(String name) {this.name = name;}

    @Column(name = "PRODUCT_CODE" , nullable = false )
    public String getProductCode() {return productCode;}

    public void setProductCode(String productCode) {this.productCode = productCode;}

    @Column(name = "DESCRIPTION" , nullable = false )
    public String getDescription() {return description;}

    public void setDescription(String description) {this.description = description;}

    @Column(name = "COMMENTS" , nullable = false )
    public String getComments() {return comments;}

    public void setComments(String comments) {this.comments = comments;}
}
