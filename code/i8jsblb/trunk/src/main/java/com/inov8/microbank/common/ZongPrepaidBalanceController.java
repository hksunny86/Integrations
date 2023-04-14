package com.inov8.microbank.common;

import com.inov8.framework.common.model.PagingHelperModel;
import com.inov8.framework.common.model.SortingOrder;
import com.inov8.framework.webapp.action.BaseFormSearchController;
import com.inov8.integration.i8sb.constants.I8SBConstants;
import com.inov8.integration.i8sb.vo.I8SBSwitchControllerRequestVO;
import com.inov8.integration.i8sb.vo.I8SBSwitchControllerResponseVO;
import com.inov8.microbank.common.model.ZongPreparedBalanceModel;
import com.inov8.microbank.common.util.MessageUtil;
import com.inov8.microbank.common.util.StringUtil;
import com.inov8.microbank.common.wrapper.switchmodule.SwitchWrapper;
import com.inov8.microbank.common.wrapper.switchmodule.SwitchWrapperImpl;
import com.inov8.microbank.server.service.financialintegrationmodule.switchmodule.ESBAdapter;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.*;

// @Created On 4/11/2023 : Tuesday
// @Created By muhammad.aqeel
public class ZongPrepaidBalanceController extends BaseFormSearchController {
    private ESBAdapter esbAdapter;
    public ZongPrepaidBalanceController() {
        super.setCommandName("zongPreparedBalanceModel");
        super.setCommandClass(ZongPreparedBalanceModel.class);
    }
    @Override
    protected Map loadReferenceData(HttpServletRequest httpServletRequest) throws Exception {
        Map<String, Object> referenceDataMap = new HashMap<>(1);
        String mobileNo = MessageUtil.getMessage("zong.mobile.no");
        ZongPreparedBalanceModel zongPreparedBalanceModel = new ZongPreparedBalanceModel();;
        logger.info("ZongPreparedBalanceController.loadReferenceData() request prepare and sent to I8SB");
        SwitchWrapper sWrapper = new SwitchWrapperImpl();
        I8SBSwitchControllerRequestVO requestVO = prepareI8sbRequest(mobileNo);
        sWrapper.setI8SBSwitchControllerRequestVO(requestVO);
        sWrapper = this.esbAdapter.makeI8SBCall(sWrapper);
        I8SBSwitchControllerResponseVO responseVO = sWrapper.getI8SBSwitchControllerRequestVO().getI8SBSwitchControllerResponseVO();
        if(responseVO!=null  && !StringUtil.isNullOrEmpty(responseVO.getRemainingBalance())){
            zongPreparedBalanceModel.setBalance(Double.parseDouble(responseVO.getRemainingBalance()));
            zongPreparedBalanceModel.setMobileNo(Long.valueOf(mobileNo));
        }
        referenceDataMap.put("zongModel",zongPreparedBalanceModel);
        return referenceDataMap;
    }

    @Override
    protected ModelAndView onSearch(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o, PagingHelperModel pagingHelperModel, LinkedHashMap<String, SortingOrder> linkedHashMap) throws Exception {
        return null;
    }

    private I8SBSwitchControllerRequestVO prepareI8sbRequest(String mobileNo){
        I8SBSwitchControllerRequestVO requestVO = new I8SBSwitchControllerRequestVO();
        requestVO.setMobileNumber(mobileNo);
        requestVO.setI8sbClientID(I8SBConstants.I8SB_Client_ID_JSBL);
        requestVO.setI8sbClientTerminalID(I8SBConstants.I8SB_Client_Terminal_ID_BLB);
        requestVO.setI8sbChannelID(I8SBConstants.I8SB_Channel_ID_PUSHNOTIFICATION);
        requestVO.setRequestType(I8SBConstants.RequestType_SendPushNotification);
        return requestVO;
    }

    public void setEsbAdapter(ESBAdapter esbAdapter) {
        this.esbAdapter = esbAdapter;
    }
}
