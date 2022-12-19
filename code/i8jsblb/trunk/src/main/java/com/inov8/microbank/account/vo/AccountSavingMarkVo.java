package com.inov8.microbank.account.vo;

import com.inov8.framework.common.model.BasePersistableModel;
import org.codehaus.jackson.map.annotate.JsonSerialize;

import java.io.Serializable;

/**
 * Created by Malik on 8/17/2016.
 */
@JsonSerialize(include = JsonSerialize.Inclusion.NON_NULL)
public class AccountSavingMarkVo extends BasePersistableModel implements Serializable
{

        private static final long serialVersionUID = 1L;

        private Long appUserId;
        private boolean  accountType;
        private String mfsId;
        private String comments;
        private String encryptedAppUserId;
        private Boolean isAgent;
        private Long usecaseId;
        private String cnicNo;

    public AccountSavingMarkVo()
            {

            }

    public AccountSavingMarkVo(Long appUserId, Boolean accountType ) {
            super();
            this.appUserId = appUserId;
            this.accountType = accountType;
        }

        public Long getAppUserId()
        {
            return appUserId;
        }

        public void setAppUserId(Long appUserId)
        {
            this.appUserId = appUserId;
        }

    public Boolean getAccountType()
    {
        return accountType;
    }

    public void setAccountType(boolean accountType) {
        this.accountType = accountType;
    }

    @Override
    public void setPrimaryKey(Long aLong)
    {

    }

    @Override
    public Long getPrimaryKey()
    {
        return null;
    }

    @Override
    public String getPrimaryKeyParameter()
    {
        return null;
    }

    @Override
    public String getPrimaryKeyFieldName()
    {
        return null;
    }


    public String getMfsId()
    {
        return mfsId;
    }

    public void setMfsId(String mfsId)
    {
        this.mfsId = mfsId;
    }

    public String getComments()
    {
        return comments;
    }

    public void setComments(String comments)
    {
        this.comments = comments;
    }


    public String getEncryptedAppUserId()
    {
        return encryptedAppUserId;
    }

    public void setEncryptedAppUserId(String encryptedAppUserId)
    {
        this.encryptedAppUserId = encryptedAppUserId;
    }

    public Boolean getIsAgent()
    {
        return isAgent;
    }

    public void setIsAgent(Boolean isAgent)
    {
        this.isAgent = isAgent;
    }


    public Long getUsecaseId()
    {
        return usecaseId;
    }

    public void setUsecaseId(Long usecaseId)
    {
        this.usecaseId = usecaseId;
    }

    public String getCnicNo()
    {
        return cnicNo;
    }

    public void setCnicNo(String cnicNo)
    {
        this.cnicNo = cnicNo;
    }

}
