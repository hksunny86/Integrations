package com.inov8.microbank.common.model;

import com.inov8.framework.common.model.BasePersistableModel;

import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@org.hibernate.annotations.Entity(dynamicInsert = true)
@Table(name = "BILL_STATUS")
public class BillStatusModel extends BasePersistableModel {
    @Override
    public void setPrimaryKey(Long aLong) {

    }

    @Override
    public Long getPrimaryKey() {
        return null;
    }

    @Override
    public String getPrimaryKeyParameter() {
        return null;
    }

    @Override
    public String getPrimaryKeyFieldName() {
        return null;
    }
}
