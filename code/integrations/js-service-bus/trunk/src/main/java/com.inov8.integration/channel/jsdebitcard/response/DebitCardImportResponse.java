package com.inov8.integration.channel.jsdebitcard.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.inov8.integration.exception.I8SBRunTimeException;
import com.inov8.integration.i8sb.enums.I8SBKeysOfCollectionEnum;
import com.inov8.integration.i8sb.vo.DebitCardCharges;
import com.inov8.integration.i8sb.vo.DebitCardStatus;
import com.inov8.integration.i8sb.vo.DebitCardStatusVO;
import com.inov8.integration.i8sb.vo.I8SBSwitchControllerResponseVO;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.List;

/**
 * Created by inov8 on 02/19/2019.
 */

@XmlRootElement
@JsonInclude(JsonInclude.Include.NON_NULL)
public class DebitCardImportResponse extends Response {

    private String code;
    private String description;
    private List<DebitCardCharges> cardChargesList;
    private List<DebitCardStatusVO> cardStatusList;

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

    public List<DebitCardCharges> getCardChargesList() {
        return cardChargesList;
    }

    public void setCardChargesList(List<DebitCardCharges> cardChargesList) {
        this.cardChargesList = cardChargesList;
    }

    public List<DebitCardStatusVO> getCardStatusList() {
        return cardStatusList;
    }

    public void setCardStatusList(List<DebitCardStatusVO> cardStatusList) {
        this.cardStatusList = cardStatusList;
    }

    @Override
    public I8SBSwitchControllerResponseVO populateI8SBSwitchControllerResponseVO() throws I8SBRunTimeException {
        I8SBSwitchControllerResponseVO i8SBSwitchControllerResponseVO = new I8SBSwitchControllerResponseVO();
        i8SBSwitchControllerResponseVO.setResponseCode(this.code);
        i8SBSwitchControllerResponseVO.setDescription(this.description);
        i8SBSwitchControllerResponseVO.addListToCollection(I8SBKeysOfCollectionEnum.DebitCardStatus.getValue(),this.cardStatusList);
        i8SBSwitchControllerResponseVO.addListToCollection(I8SBKeysOfCollectionEnum.DebitCardCharges.getValue(),this.cardChargesList);
        return i8SBSwitchControllerResponseVO;
    }
}
