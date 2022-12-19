package com.inov8.integration.channel.APIGEE.response.ThirdPartyCashOut;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.inov8.integration.channel.APIGEE.response.Response;
import com.inov8.integration.exception.I8SBRunTimeException;
import com.inov8.integration.i8sb.vo.I8SBSwitchControllerResponseVO;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
@JsonInclude(JsonInclude.Include.NON_ABSENT)
public class TitlefetchResponse extends Response {
    @JsonProperty("code")
    private String code;
    @JsonProperty("data")
    private Data data;
    @JsonProperty("description")
    private String description;


    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }


    public Data getData() {
        return data;
    }

    public void setData(Data data) {
        this.data = data;
    }

    @Override
    public I8SBSwitchControllerResponseVO populateI8SBSwitchControllerResponseVO() throws I8SBRunTimeException {
        I8SBSwitchControllerResponseVO i8SBSwitchControllerResponseVO = new I8SBSwitchControllerResponseVO();
        i8SBSwitchControllerResponseVO.setResponseCode(this.getCode());
        i8SBSwitchControllerResponseVO.setDescription(this.getDescription());
        if (null!=this.getData()) {
            i8SBSwitchControllerResponseVO.setSessionId(this.getData().getItem().getTransactionRefrence());
            i8SBSwitchControllerResponseVO.setAccountTitle(this.getData().getItem().getAccountTitle());
            i8SBSwitchControllerResponseVO.setMobilePhone(this.getData().getItem().getFromAccount());
            i8SBSwitchControllerResponseVO.setConsumerNumber(this.getData().getItem().getFromAccount());
        }

        return i8SBSwitchControllerResponseVO;
    }


    @JsonInclude(JsonInclude.Include.NON_NULL)
   public static class Data {
        @JsonProperty("Item")
        private Item item;


        public Item getItem() {
            return item;
        }

        public void setItem(Item item) {
            this.item = item;
        }

        @JsonInclude(JsonInclude.Include.NON_NULL)
        public  static class Item {
            @JsonProperty("toAccount")
            private String toAccount;
            @JsonProperty("transactionReference")
            private String transactionRefrence;
            @JsonProperty("accountTitle")
            private String accountTitle;
            @JsonProperty("fromAccount")
            private String fromAccount;

            public String getToAccount() {
                return toAccount;
            }

            public void setToAccount(String toAccount) {
                this.toAccount = toAccount;
            }

            public String getTransactionRefrence() {
                return transactionRefrence;
            }

            public void setTransactionRefrence(String transactionRefrence) {
                this.transactionRefrence = transactionRefrence;
            }

            public String getAccountTitle() {
                return accountTitle;
            }

            public void setAccountTitle(String accountTitle) {
                this.accountTitle = accountTitle;
            }

            public String getFromAccount() {
                return fromAccount;
            }

            public void setFromAccount(String fromAccount) {
                this.fromAccount = fromAccount;
            }
        }

    }


}
