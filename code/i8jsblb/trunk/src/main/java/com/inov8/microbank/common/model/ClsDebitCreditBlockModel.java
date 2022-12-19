package com.inov8.microbank.common.model;

import com.inov8.framework.common.model.BasePersistableModel;
import org.springframework.jdbc.core.RowMapper;

import javax.persistence.*;
import java.io.Serializable;
import java.sql.ResultSet;
import java.sql.SQLException;

@Entity
@org.hibernate.annotations.Entity(dynamicInsert = true)
//@SequenceGenerator(name = "CLS_DEBIT_CREDIT_BLOCKED_SEQ", sequenceName = "CLS_DEBIT_CREDIT_BLOCKED_SEQ", allocationSize = 2)
@Table(name = "CLS_DEBIT_CREDIT_BLOCKED")
public class ClsDebitCreditBlockModel extends BasePersistableModel implements
        Serializable,Cloneable, RowMapper {

    private Long clsDebitCreditBlockId;
    private String state;
    private String status;

    public ClsDebitCreditBlockModel() {
    }
    @Override
    @Transient
    public Long getPrimaryKey() {
        return getClsDebitCreditBlockId();
    }

    @Override
    @Transient
    public String getPrimaryKeyFieldName() {
        return "clsPendingAccountOpeningId";
    }

    @Override
    @Transient
    public String getPrimaryKeyParameter() {
        return "&clsDebitCreditBlockId=" + getClsDebitCreditBlockId();
    }

    @Override
    public void setPrimaryKey(Long primaryKey) {
        setClsDebitCreditBlockId(primaryKey);
    }


    @Id
//    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "CLS_PENDING_ACCOUNT_OPENING_SEQ")
    @Column(name = "CLS_DEBIT_CREDIT_BLOCKED_ID")
    public Long getClsDebitCreditBlockId() {
        return clsDebitCreditBlockId;
    }

    public void setClsDebitCreditBlockId(Long clsDebitCreditBlockId) {
        this.clsDebitCreditBlockId = clsDebitCreditBlockId;
    }

    @Column(name = "STATE")
    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    @Column(name = "STATUS")
    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    @Override
    public Object mapRow(ResultSet resultSet, int i) throws SQLException {
        ClsDebitCreditBlockModel model = new ClsDebitCreditBlockModel();
        model.setClsDebitCreditBlockId(resultSet.getLong("CLS_DEBIT_CREDIT_BLOCKED_ID"));
        model.setState(resultSet.getString("STATE"));
        model.setStatus(resultSet.getString("STATUS"));
        return model;
    }
}
