package com.inov8.integration.middleware.controller;

import com.inov8.integration.vo.VirtualCardIntegrationVO;

/**
 * Created by inov8 on 8/23/2016.
 */
public interface VirtualCardIntegrationController {
    VirtualCardIntegrationVO linkCard(VirtualCardIntegrationVO messageVO) throws Exception;

    VirtualCardIntegrationVO createLinkedCard(VirtualCardIntegrationVO messageVO) throws Exception;

    VirtualCardIntegrationVO orderCard(VirtualCardIntegrationVO messageVO) throws Exception;

    VirtualCardIntegrationVO activateCard(VirtualCardIntegrationVO messageVO) throws Exception;

    VirtualCardIntegrationVO getActiveLinkedCards(VirtualCardIntegrationVO messageVO) throws Exception;

    VirtualCardIntegrationVO changePin(VirtualCardIntegrationVO messageVO) throws Exception;

    VirtualCardIntegrationVO resetPin(VirtualCardIntegrationVO messageVO) throws Exception;

    VirtualCardIntegrationVO updateBearer(VirtualCardIntegrationVO messageVO) throws Exception;

    VirtualCardIntegrationVO transferLink(VirtualCardIntegrationVO messageVO) throws Exception;

    VirtualCardIntegrationVO stopCard(VirtualCardIntegrationVO messageVO) throws Exception;

    VirtualCardIntegrationVO unstopCard(VirtualCardIntegrationVO messageVO) throws Exception;

    VirtualCardIntegrationVO cardStatus(VirtualCardIntegrationVO messageVO) throws Exception;

    VirtualCardIntegrationVO set3dSecureCode(VirtualCardIntegrationVO messageVO) throws Exception;

    VirtualCardIntegrationVO balance(VirtualCardIntegrationVO messageVO) throws Exception;

    VirtualCardIntegrationVO deduct(VirtualCardIntegrationVO messageVO) throws Exception;

    VirtualCardIntegrationVO deductAdjustment(VirtualCardIntegrationVO messageVO) throws Exception;

    VirtualCardIntegrationVO deductReversal(VirtualCardIntegrationVO messageVO) throws Exception;

    VirtualCardIntegrationVO loadAdjustment(VirtualCardIntegrationVO messageVO) throws Exception;

    VirtualCardIntegrationVO loadReversal(VirtualCardIntegrationVO messageVO) throws Exception;

    VirtualCardIntegrationVO validatePIN(VirtualCardIntegrationVO messageVO) throws Exception;

    VirtualCardIntegrationVO stop(VirtualCardIntegrationVO messageVO) throws Exception;

}
