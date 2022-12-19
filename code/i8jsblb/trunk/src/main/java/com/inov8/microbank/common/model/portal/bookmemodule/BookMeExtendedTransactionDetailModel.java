package com.inov8.microbank.common.model.portal.bookmemodule;

import javax.xml.bind.annotation.XmlRootElement;
import java.util.Date;

@XmlRootElement
public class BookMeExtendedTransactionDetailModel extends BookMeTransactionDetailViewModel {

    private static final long serialVersionUID = 2436347487879090899L;
    private Date startDate;
    private Date endDate;
    private Date updatedOnStartDate;
    private Date updatedOnEndDate;




    @javax.persistence.Transient
    public Date getEndDate() {
        return endDate;
    }

    @javax.persistence.Transient
    public Date getStartDate() {
        return startDate;
    }

    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    @javax.persistence.Transient
    public Date getUpdatedOnStartDate() {
        return updatedOnStartDate;
    }

    public void setUpdatedOnStartDate(Date updatedOnStartDate) {
        this.updatedOnStartDate = updatedOnStartDate;
    }

    @javax.persistence.Transient
    public Date getUpdatedOnEndDate() {
        return updatedOnEndDate;
    }

    public void setUpdatedOnEndDate(Date updatedOnEndDate) {
        this.updatedOnEndDate = updatedOnEndDate;
    }


}
