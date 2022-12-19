package com.inov8.hsm.controller;

import java.util.Date;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import com.inov8.hsm.controller.validation.HSMTransactionRequestValidator;
import com.inov8.hsm.controller.validation.HSMValidationException;
import com.inov8.hsm.dto.PayShieldDTO;
import com.inov8.hsm.service.IntegrationService;
import com.inov8.hsm.util.ConfigReader;
import com.inov8.hsm.util.VersionInfo;

@Controller("PayShieldSwitchController")
@VersionInfo(lastModified = "05/12/2014", releaseVersion = "1.0", version = "1.0", createdBy = "Zeeshan Ahmed, Faisal Basra",tags="HSM Controller")
public class PayShieldSwitchController implements IPayShieldSwitchController {
	private static Logger logger = LoggerFactory.getLogger(IPayShieldSwitchController.class.getSimpleName());
	private boolean production = Boolean.valueOf(ConfigReader.getInstance().getProperty("production", "true"));

	@Autowired
	private IntegrationService integrationService;

	@Override
	public PayShieldDTO generateSystemPIN(PayShieldDTO payShieldDTO) throws Exception {
		long startTime = new Date().getTime(); // start time
		try {
			// Validate Request
			HSMTransactionRequestValidator.validateGenerateSystemPINRequest(payShieldDTO);
			logger.info("<<< Processing Generate System PIN >>> " + payShieldDTO.print(true));

			payShieldDTO = integrationService.generateSystemPIN(payShieldDTO);

			logger.info("******* DEBUG LOGS FOR GENERATE SYSTEM PIN *********");
			logger.info("ResponseCode: " + payShieldDTO.getResponseCode());
			logger.info("Retrival Reference Number: " + payShieldDTO.getUPID());
			logger.info("Microbank Transaction Code: " + payShieldDTO.getMicrobankTransactionCode());

		} catch (HSMValidationException e) {
			logger.error("Validation Failed for Generate System PIN Request", e);
			payShieldDTO.setResponseCode("420");
		} catch (Exception e) {
			logger.error("Internal Error@ Generate System PIN Request", e);
			payShieldDTO.setResponseCode("220");
		}

		long endTime = new Date().getTime(); // end time
		long difference = endTime - startTime; // check different
		logger.debug("[ GENERATE SYSTEM PIN REQUEST PROCESSED IN: " + difference + " milliseconds" + " ]");

		payShieldDTO.setTransmissionTime(new Date());
		logger.info("<<< Processed Generate System PIN >>> " + payShieldDTO.print(production));
		return payShieldDTO;
	}

	@Override
	public PayShieldDTO generateUserPIN(PayShieldDTO payShieldDTO) throws Exception {
		long startTime = new Date().getTime(); // start time
		try {
			// Validate Request
			HSMTransactionRequestValidator.validateGenerateUserPINRequest(payShieldDTO);
			logger.info("<<< Processing Generate User PIN >>> " + payShieldDTO.print(true));

			payShieldDTO = integrationService.generateUserPIN(payShieldDTO);

			logger.info("******* DEBUG LOGS FOR GENERATE USER PIN *********");
			logger.info("ResponseCode: " + payShieldDTO.getResponseCode());
			logger.info("Retrival Reference Number: " + payShieldDTO.getUPID());
			logger.info("Microbank Transaction Code: " + payShieldDTO.getMicrobankTransactionCode());

		} catch (HSMValidationException e) {
			logger.error("Validation Failed for Change System PIN Request", e);
			payShieldDTO.setResponseCode("420");
		} catch (Exception e) {
			logger.error("Internal Error@ Change System PIN Request", e);
			payShieldDTO.setResponseCode("220");
		}

		long endTime = new Date().getTime(); // end time
		long difference = endTime - startTime; // check different
		logger.debug("[ GENERATE USER PIN REQUEST PROCESSED IN " + difference + " milliseconds" + " ]");

		payShieldDTO.setTransmissionTime(new Date());
		logger.info("<<< Processed Generate User PIN >>> " + payShieldDTO.print(production));
		return payShieldDTO;
	}

	@Override
	public PayShieldDTO changeSystemPIN(PayShieldDTO payShieldDTO) throws Exception {
		long startTime = new Date().getTime(); // start time
		try {
			// Validate Request
			HSMTransactionRequestValidator.validateChangeSystemPINRequest(payShieldDTO);
			logger.info("<<< Processing Change System PIN >>> " + payShieldDTO.print(true));

			payShieldDTO = integrationService.changeSystemPIN(payShieldDTO);

			logger.info("******* DEBUG LOGS FOR CHANGE SYSTEM PIN *********");
			logger.info("ResponseCode: " + payShieldDTO.getResponseCode());
			logger.info("Retrival Reference Number: " + payShieldDTO.getUPID());
			logger.info("Microbank Transaction Code: " + payShieldDTO.getMicrobankTransactionCode());

		} catch (HSMValidationException e) {
			logger.error("Validation Failed for Change System PIN Request", e);
			payShieldDTO.setResponseCode("420");
		} catch (Exception e) {
			logger.error("Internal Error@ Change System PIN Request", e);
			payShieldDTO.setResponseCode("220");
		}

		long endTime = new Date().getTime(); // end time
		long difference = endTime - startTime; // check different
		logger.debug("[ GENERATE CHANGE SYSTEM PIN REQUEST PROCESSED IN " + difference + " milliseconds" + " ]");

		payShieldDTO.setTransmissionTime(new Date());
		logger.info("<<< Processed Change System PIN >>> " + payShieldDTO.print(production));
		return payShieldDTO;
	}

	@Override
	public PayShieldDTO changeUserPIN(PayShieldDTO payShieldDTO) throws Exception {
		long startTime = new Date().getTime(); // start time
		try {
			// Validate Request
			HSMTransactionRequestValidator.validateChangeSystemPINRequest(payShieldDTO);
			logger.info("<<< Processing Change User PIN >>>" + payShieldDTO.print(true));

			payShieldDTO = integrationService.changeUserPIN(payShieldDTO);

			logger.info("******* DEBUG LOGS FOR CHANGE USER PIN *********");
			logger.info("ResponseCode: " + payShieldDTO.getResponseCode());
			logger.info("Retrival Reference Number: " + payShieldDTO.getUPID());
			logger.info("Microbank Transaction Code: " + payShieldDTO.getMicrobankTransactionCode());

		} catch (HSMValidationException e) {
			logger.error("Validation Failed for Change User PIN Request", e);
			payShieldDTO.setResponseCode("420");
		} catch (Exception e) {
			logger.error("Internal Error@ Change User PIN Request", e);
			payShieldDTO.setResponseCode("220");
		}

		long endTime = new Date().getTime(); // end time
		long difference = endTime - startTime; // check different
		logger.debug("[ GENERATE CHANGE USER PIN REQUEST PROCESSED IN ]" + difference + " milliseconds");

		payShieldDTO.setTransmissionTime(new Date());
		logger.info("<<< Processed Change User PIN >>>" + payShieldDTO.print(production));
		return payShieldDTO;
	}

	@Override
	public PayShieldDTO verifyPIN(PayShieldDTO payShieldDTO) throws Exception {
		long startTime = new Date().getTime(); // start time
		try {
			// Validate Request
			HSMTransactionRequestValidator.validateGenerateSystemPINRequest(payShieldDTO);
			logger.info("<<< Processing Verify PIN >>>" + payShieldDTO.print(production));

			payShieldDTO = integrationService.verifyPIN(payShieldDTO);

			logger.info("******* DEBUG LOGS FOR VERIFY PIN *********");
			logger.info("ResponseCode: " + payShieldDTO.getResponseCode());
			logger.info("Retrival Reference Number: " + payShieldDTO.getUPID());
			logger.info("Microbank Transaction Code: " + payShieldDTO.getMicrobankTransactionCode());

		} catch (HSMValidationException e) {
			logger.error("Validation Failed for Verify PIN Request", e);
			payShieldDTO.setResponseCode("420");
		} catch (Exception e) {
			logger.error("Internal Error@ Verify PIN Request", e);
			payShieldDTO.setResponseCode("220");
		}

		long endTime = new Date().getTime(); // end time
		long difference = endTime - startTime; // check different
		logger.debug("[ VERIFY PIN REQUEST PROCESSED IN " + difference + " milliseconds" + " ]");

		payShieldDTO.setTransmissionTime(new Date());
		logger.info("<<< Processed Verify PIN >>> " + payShieldDTO.print(production));
		return payShieldDTO;
	}

}
