package com.inov8.microbank.webapp.action.loginmodule;

import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.Controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Created by Attique on 8/16/2018.
 */
public class SOLoginFormController implements Controller {

    public SOLoginFormController()
    {
    }

    public ModelAndView handleRequest(HttpServletRequest httpServletRequest,
                                      HttpServletResponse httpServletResponse) throws
            Exception
    {

        return new ModelAndView("scologin");
    }
}
