package com.inov8.microbank.fonepay.controller;

import com.inov8.framework.common.exception.FrameworkCheckedException;
import com.inov8.framework.common.wrapper.BaseWrapper;
import com.inov8.framework.common.wrapper.BaseWrapperImpl;
import com.inov8.microbank.common.util.ActionAuthorizationConstantsInterface;
import com.inov8.microbank.common.util.MessageUtil;
import com.inov8.microbank.common.util.PortalConstants;
import com.inov8.microbank.common.util.UserUtils;
import com.inov8.microbank.fonepay.model.VirtualCardEnableDisableVO;
import com.inov8.microbank.fonepay.model.VirtualCardModel;
import com.inov8.microbank.fonepay.service.FonePayManager;
import com.inov8.microbank.webapp.action.ajax.AjaxController;
import org.codehaus.jackson.map.ObjectMapper;
import org.joda.time.DateTime;
import org.springframework.web.bind.ServletRequestUtils;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Created by Attique on 7/26/2017.
 */
public class BlockUnblockVCardAjax extends AjaxController {

    private FonePayManager fonePayManager;

    @Override
    public String getResponseContent(HttpServletRequest request, HttpServletResponse response) throws Exception {

        VirtualCardModel virtualCardReportModel=new VirtualCardModel();

        virtualCardReportModel.setVirtualCardId(Long.parseLong( ServletRequestUtils.getStringParameter(request, "vrtualCardId")));
        virtualCardReportModel=fonePayManager.searchCardById(virtualCardReportModel);
        String s=ServletRequestUtils.getStringParameter(request, "isBlocked");


        BaseWrapper baseWrapper=new BaseWrapperImpl();
        baseWrapper.setBasePersistableModel(virtualCardReportModel);
        VirtualCardEnableDisableVO virtualCardEnableDisableVO=new VirtualCardEnableDisableVO();
        virtualCardEnableDisableVO.setAppUserId(virtualCardReportModel.getAppUserId());
        virtualCardEnableDisableVO.setCreatedBy(UserUtils.getCurrentUser().getAppUserId());
        virtualCardEnableDisableVO.setCreatedOn(DateTime.now().toDate());
        virtualCardEnableDisableVO.setCardNo(virtualCardReportModel.getCardNo());
        baseWrapper.setBasePersistableModel(virtualCardEnableDisableVO);
        this.populateAuthenticationParams(baseWrapper,request,virtualCardEnableDisableVO);
        fonePayManager.updateVirtualCardStatusWithAuthorization(baseWrapper);

        return "";
    }

    protected void populateAuthenticationParams(BaseWrapper baseWrapper, HttpServletRequest req, Object model) throws FrameworkCheckedException {

        VirtualCardEnableDisableVO virtualCardModel = (VirtualCardEnableDisableVO) model;
        ObjectMapper mapper = new ObjectMapper();
        Long actionAuthorizationId =null;
/*        try
        {
            actionAuthorizationId = ServletRequestUtils.getLongParameter(req,"authId");
        } catch (ServletRequestBindingException e1) {
            e1.printStackTrace();
        }*/

/*        DateFormat df = new SimpleDateFormat(PortalDateUtils.SHORT_DATE_FORMAT);
        mapper.setDateFormat(df);*/

        String modelJsonString = null;
        try {

            modelJsonString = mapper.writeValueAsString(virtualCardModel);
        } catch (IOException e) {
            e.printStackTrace();
            throw new FrameworkCheckedException(MessageUtil.getMessage(ActionAuthorizationConstantsInterface.REF_DATA_EXCEPTION_MSG));
        }

        baseWrapper.putObject(ActionAuthorizationConstantsInterface.KEY_METHODE_NAME, "updateVirtualCardStatusWithAuthorization");
        baseWrapper.putObject(ActionAuthorizationConstantsInterface.KEY_MODEL_CLASS, VirtualCardEnableDisableVO.class.getSimpleName());
        baseWrapper.putObject(ActionAuthorizationConstantsInterface.KEY_MODEL_CLASS_QUALIFIED_NAME, VirtualCardEnableDisableVO.class.getName());
        baseWrapper.putObject(ActionAuthorizationConstantsInterface.KEY_MODEL_STRING, modelJsonString);
        baseWrapper.putObject(ActionAuthorizationConstantsInterface.KEY_MANAGER_BEAN_NAME,"fonePayManager");
        baseWrapper.putObject(ActionAuthorizationConstantsInterface.KEY_REQ_REF_ID,virtualCardModel.getCardNo().toString());

        baseWrapper.putObject(PortalConstants.KEY_ACTION_AUTH_ID,actionAuthorizationId);
        baseWrapper.putObject(PortalConstants.KEY_USECASE_ID, PortalConstants.BLOCK_UNBLOCK_VIRTUAL_CARD_USECASE_ID);
        baseWrapper.putObject(PortalConstants.KEY_ACTION_ID, PortalConstants.ACTION_UPDATE);
        baseWrapper.putObject(ActionAuthorizationConstantsInterface.KEY_FORM_NAME, "p_blockunblockvirtualcard");
    }



    public void setFonePayManager(FonePayManager fonePayManager) {
        this.fonePayManager = fonePayManager;
    }
}
