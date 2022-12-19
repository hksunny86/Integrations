package com.inov8.hsm.controller;

import com.inov8.hsm.dto.PayShieldDTO;

public interface IPayShieldSwitchController {

    /**
     *
     * @param 	PAN				PAN for Generating System PIN
     * @return	PIN				PIN for given PAN
     * @return	PVV				PVV for generated PIN
     * @return	responseCode	Response Code for given request
     * @throws Exception
     */
    public PayShieldDTO generateSystemPIN(PayShieldDTO payShieldDTO) throws Exception;

    /**
     *
     * @param 	PAN				User PAN for Generating User PVV
     * @param	PIN				User PIN for Generating User PVV
     * @return	PVV				PVV for user PIN
     * @return	responseCode	Response Code for given request
     * @throws Exception
     */
    public PayShieldDTO generateUserPIN(PayShieldDTO payShieldDTO) throws Exception;

    /**
     *
     * @param 	PAN				PAN for Change PIN
     * @param	oldPIN			Old PIN for given PAN
     * @param	oldPVV			Old PVV for generated PIN
     * @return	PIN				New System Generated PIN
     * @return	PVV				PVV for New PIN
     * @return	responseCode	Response Code for given request
     * @throws Exception
     */
    public PayShieldDTO changeSystemPIN(PayShieldDTO payShieldDTO) throws Exception;

    /**
     *
     * @param 	PAN				PAN for Change PIN
     * @param	oldPIN			Old PIN for given PAN
     * @param	oldPVV			Old PVV for generated PIN
     * @param	PIN				New User PIN
     * @return	PIN				PVV for New PIN
     * @return	responseCode	Response Code for given request
     * @throws Exception
     */
    public PayShieldDTO changeUserPIN(PayShieldDTO payShieldDTO) throws Exception;

    /**
     *
     * @param 	PAN				PAN for Verification
     * @return	PIN				PIN for Verification
     * @return	PVV				PVV for Verification
     * @return	responseCode	Response Code for given request
     * @throws Exception
     */
    public PayShieldDTO verifyPIN(PayShieldDTO payShieldDTO) throws Exception;

    /**
     *
     * @param 	PAN				PAN for Verification
     * @param	PIN				PIN for Verification
     * @return	PIN				Encrypted PIN
     * @return	responseCode	Response Code for given request
     * @throws Exception
     */
    public PayShieldDTO createPINBlock(PayShieldDTO payShieldDTO) throws Exception;

}
