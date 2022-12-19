package com.inov8.microbank.captcha;

import nl.captcha.Captcha;
import org.springframework.web.util.WebUtils;

import javax.servlet.http.HttpServletRequest;
import java.io.Serializable;

public class CaptchaAuthenticationDetails implements Serializable {

	private static final long serialVersionUID = 8047091036777813803L;

	private final String answer;
	private final Captcha captcha;

	public CaptchaAuthenticationDetails(HttpServletRequest req) {
		this.answer = req.getParameter("j_captcha");
		this.captcha = (Captcha) WebUtils.getSessionAttribute(req, "captcha");
	}

	public String getAnswer() {
		return answer;
	}

	public Captcha getCaptcha() {
		return captcha;
	}

}
