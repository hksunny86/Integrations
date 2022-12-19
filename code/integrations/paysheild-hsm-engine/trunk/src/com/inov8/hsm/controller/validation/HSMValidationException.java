package com.inov8.hsm.controller.validation;

import com.inov8.hsm.util.VersionInfo;

@VersionInfo(lastModified = "05/12/2014", releaseVersion = "1.0", version = "1.0", createdBy = "Zeeshan Ahmed, Faisal Basra",tags="")
public class HSMValidationException extends RuntimeException {

	private static final long serialVersionUID = -2714364496807971467L;

	public HSMValidationException(String message) {
		super(message);
	}

	public HSMValidationException(Throwable throwable) {
		super(throwable);
	}

	public HSMValidationException(Throwable throwable, String message) {
		super(message, throwable);
	}
}
