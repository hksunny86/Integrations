package com.inov8.hsm.controller.validation;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.inov8.hsm.dto.PayShieldDTO;
import com.inov8.hsm.util.VersionInfo;
@VersionInfo(lastModified = "05/12/2014", releaseVersion = "1.0", version = "1.0", createdBy = "Zeeshan Ahmed, Faisal Basra",tags="")
public class HSMTransactionRequestValidator {

	private static Logger logger = LoggerFactory.getLogger(HSMTransactionRequestValidator.class.getSimpleName());

	public static void validateGenerateSystemPINRequest(PayShieldDTO payShieldDTO) {
		if (StringUtils.isEmpty(payShieldDTO.getPAN())) {
			logger.debug("[FAILED] PAN not provided: ");
			throw new HSMValidationException("Validation PAN [FAILED] PAN not provided.");
		}
		
	}

	public static void validateGenerateUserPINRequest(PayShieldDTO payShieldDTO) {
		if (StringUtils.isEmpty(payShieldDTO.getPAN())) {
			logger.debug("[FAILED] PAN not provided: ");
			throw new HSMValidationException("Validation PAN [FAILED] PAN not provided.");
		}
	}

	public static void validateChangeSystemPINRequest(PayShieldDTO payShieldDTO) {
		if (StringUtils.isEmpty(payShieldDTO.getPAN())) {
			logger.debug("[FAILED] PAN not provided: ");
			throw new HSMValidationException("Validation PAN [FAILED] PAN not provided.");
		}
	}

	
}
